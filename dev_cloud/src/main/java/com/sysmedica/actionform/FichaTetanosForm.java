package com.sysmedica.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class FichaTetanosForm extends ValidatorForm {

	private boolean activa;
	
	private boolean hayServicios;
    private boolean hayLaboratorios;
    
    private int codigoInstitucion;
    
	private boolean notificar;
    private String loginUsuario;
    private int codigoFichaTetanos;
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
    
    private String nombreMadre;
    private int edadMadre;
    private String fechaNacimientoMadre;
    private String fechaEgresoHospital;
    private boolean nacimientoTraumatico;
    private boolean llantoNacer;
    private boolean mamabaNormal;
    private boolean dejoMamar;
    private String fechaDejo;
    private boolean dificultadRespiratoria;
    private boolean episodiosApnea;
    private boolean hipotermia;
    private boolean hipertermia;
    private boolean fontAbombada;
    private boolean rigidezNuca;
    private boolean trismus;
    private boolean convulsiones;
    private boolean espasmos;
    private boolean contracciones;
    private boolean opistotonos;
    private boolean llantoExcesivo;
    private boolean sepsisUmbilical;
    private int numeroEmbarazos;
    private boolean asistioControl;
    private String explicacionNoAsistencia;
    private boolean atendidoPorMedico;
    private boolean atendidoPorEnfermero;
    private boolean atendidoPorAuxiliar;
    private boolean atendidoPorPromotor;
    private boolean atendidoPorOtro;
    private String quienAtendio;
    private int numeroControlesPrevios;
    private String fechaUltimoControl;
    private boolean madreVivioMismoLugar;
    private String codigoMunicipioVivienda;
    private String codigoDepartamentoVivienda;
    private String lugarVivienda;
    private boolean antecedenteVacunaAnti;
    private int dosisDpt;
    private String explicacionNoVacuna;
    private String fechaDosisTd1;
    private String fechaDosisTd2;
    private String fechaDosisTd3;
    private String fechaDosisTd4;
    private int lugarParto;
    private String institucionParto;
    private String fechaIngresoParto;
    private String fechaEgresoParto;
    private int quienAtendioParto;
    private String instrumentoCordon;
    private String metodoEsterilizacion;
    private boolean recibioInformacionMunon;
    private boolean aplicacionSustanciasMunon;
    private String cualesSustancias;
    private int distanciaMinutos;
    private String fechaInvestigacionCampo;
    private String fechaVacunacion;
    private int dosisTd1AplicadasMef;
    private int dosisTd2AplicadasMef;
    private int dosisTd3AplicadasMef;
    private int dosisTd4AplicadasMef;
    private int dosisTd5AplicadasMef;
    private int dosisTd1AplicadasGest;
    private int dosisTd2AplicadasGest;
    private int dosisTd3AplicadasGest;
    private int dosisTd4AplicadasGest;
    private int dosisTd5AplicadasGest;
    private int coberturaLograda;
    private String pais;
    private int areaProcedencia;
    private String grupoPoblacional;
    
    private String estado;
    
    
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
    
    
    public void reset() {
    	
    	sire = "";
        nombreMadre = "";
        fechaNacimientoMadre = "";
        fechaEgresoHospital = "";
        nacimientoTraumatico = false;
        llantoNacer = false;
        mamabaNormal = false;
        dejoMamar = false;
        fechaDejo = "";
        dificultadRespiratoria = false;
        episodiosApnea = false;
        hipotermia = false;
        hipertermia = false;
        fontAbombada = false;
        rigidezNuca = false;
        trismus = false;
        convulsiones = false;
        espasmos = false;
        contracciones = false;
        opistotonos = false;
        llantoExcesivo = false;
        sepsisUmbilical = false;
        
        asistioControl = false;
        explicacionNoAsistencia = "";
        atendidoPorMedico = false;
        atendidoPorEnfermero = false;
        atendidoPorAuxiliar = false;
        atendidoPorPromotor = false;
        atendidoPorOtro = false;
        quienAtendio = "";
        
        fechaUltimoControl = "";
        madreVivioMismoLugar = false;
        
        
        antecedenteVacunaAnti = false;
        
        explicacionNoVacuna = "";
        fechaDosisTd1 = "";
        fechaDosisTd2 = "";
        fechaDosisTd3 = "";
        fechaDosisTd4 = "";
        lugarParto = 0;
        institucionParto = "";
        fechaIngresoParto = "";
        fechaEgresoParto = "";
        quienAtendioParto = 0;
        instrumentoCordon = "";
        metodoEsterilizacion = "";
        recibioInformacionMunon = false;
        aplicacionSustanciasMunon = false;
        cualesSustancias = "";
        
        fechaInvestigacionCampo = "";
        fechaVacunacion = "";
        
        distanciaMinutos = 0;
        fechaInvestigacionCampo = "";
        fechaVacunacion = "";
        dosisTd1AplicadasMef = 0;
        dosisTd2AplicadasMef = 0;
        dosisTd3AplicadasMef = 0;
        dosisTd4AplicadasMef = 0;
        dosisTd5AplicadasMef = 0;
        dosisTd1AplicadasGest = 0;
        dosisTd2AplicadasGest = 0;
        dosisTd3AplicadasGest = 0;
        dosisTd4AplicadasGest = 0;
        dosisTd5AplicadasGest = 0;
        coberturaLograda = 0;
        
        estaVivo = false;
        
        numeroEmbarazos = 0;
        dosisDpt = 0;
        
        String ciudad = ValoresPorDefecto.getCiudadNacimiento(codigoInstitucion);
        String lugar = ciudad.split("-")[1]+"-"+ciudad.split("-")[0];
               
        lugarNotifica = lugar;
        lugarProcedencia = lugar;
        lugarVivienda = lugar;
        
        fechaInicioSint = "";
        tipoCaso = 0;
        
        fechaConsultaGeneral = "";
        codigoMunProcedencia = "";
        codigoDepProcedencia = "";
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
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	
        	String fechaNacimientoMadreTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaNacimientoMadre);
        	String fechaEgresoHospitalTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaEgresoHospital);
        	String fechaDejoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDejo);
        	
        	String fechaUltimoControlTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaUltimoControl);
        	
        	String fechaDosisTd1Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaDosisTd1);
        	String fechaDosisTd2Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaDosisTd2);
        	String fechaDosisTd3Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaDosisTd3);
        	String fechaDosisTd4Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaDosisTd4);
        	
        	String fechaIngresoPartoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaIngresoParto);
        	String fechaEgresoPartoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaEgresoParto);
        	
        	String fechaInvestigacionCampoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInvestigacionCampo);
        	String fechaVacunacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunacion);
        	
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
            
        	if (fechaNacimientoMadre.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaNacimientoMadre)) {
	                
	                errores.add("Campo Fecha Nacimiento Madre no valido", new ActionMessage("errors.formatoFechaInvalido","Nacimiento del Recien Nacido"));
	            }
	            else if (fechaNacimientoMadreTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha Nacimiento Madre no valido", new ActionMessage("errors.fechaPosteriorIgualActual","Nacimiento del Recien Nacido", "actual"));
				}
            }
        	        	
        	if (fechaEgresoHospital.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaEgresoHospital)) {
	                
	                errores.add("Campo Fecha Egreso Hospital no valido", new ActionMessage("errors.formatoFechaInvalido","Egreso Hospital"));
	            }
	            else if (fechaEgresoHospitalTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha Egreso Hospital no valido", new ActionMessage("errors.fechaPosteriorIgualActual","Egreso Hospital", "actual"));
				}
	            else if (fechaEgresoHospitalTransformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha Egreso Hospital no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","Egreso Hospital", "de Nacimiento del Recien Nacido"));
				}
            }
        	
if (fechaDejo.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaDejo)) {
	                
	                errores.add("Campo Fecha en que Dejo de Mamar no valido", new ActionMessage("errors.formatoFechaInvalido","en que Dejo de Mamar"));
	            }
	            else if (fechaDejoTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha en que Dejo de Mamar no valido", new ActionMessage("errors.fechaPosteriorIgualActual","en que Dejo de Mamar", "actual"));
				}
	            else if (fechaDejoTransformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha en que Dejo de Mamar no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","en que Dejo de Mamar", "de Nacimiento del Recien Nacido"));
				}
            }
        	
        	if (fechaUltimoControl.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaUltimoControl)) {
	                
	                errores.add("Campo Fecha de Ultimo Control no valido", new ActionMessage("errors.formatoFechaInvalido","de Ultimo Control"));
	            }
	            else if (fechaUltimoControlTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Ultimo Control no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ultimo Control", "actual"));
				}
	            /*
	            else if (fechaUltimoControlTransformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha de Ultimo Control no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Ultimo Control", "de Nacimiento de la Madre"));
				}
				*/
            }
        	
        	if (fechaDosisTd1.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaDosisTd1)) {
	                
	                errores.add("Campo Fecha de Dosis Td1 no valido", new ActionMessage("errors.formatoFechaInvalido","de Dosis Td1"));
	            }
	            else if (fechaDosisTd1Transformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Dosis Td1 no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Dosis Td1", "actual"));
				}
	            /*
	            else if (fechaDosisTd1Transformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha de Dosis Td1 no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Dosis Td1", "de Nacimiento de la Madre"));
				}
				*/
            }
        	
        	if (fechaDosisTd2.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaDosisTd2)) {
	                
	                errores.add("Campo Fecha de Dosis Td2 no valido", new ActionMessage("errors.formatoFechaInvalido","de Dosis Td2"));
	            }
	            else if (fechaDosisTd2Transformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Dosis Td2 no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Dosis Td2", "actual"));
				}
	            /*
	            else if (fechaDosisTd2Transformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha de Dosis Td2 no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Dosis Td2", "de Nacimiento de la Madre"));
				}
				*/
            }
        	
        	if (fechaDosisTd3.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaDosisTd3)) {
	                
	                errores.add("Campo Fecha de Dosis Td3 no valido", new ActionMessage("errors.formatoFechaInvalido","de Dosis Td3"));
	            }
	            else if (fechaDosisTd3Transformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Dosis Td3 no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Dosis Td3", "actual"));
				}
	            /*
	            else if (fechaDosisTd3Transformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha de Dosis Td3 no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Dosis Td3", "de Nacimiento de la Madre"));
				}
				*/
            }
        	
        	if (fechaDosisTd4.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaDosisTd4)) {
	                
	                errores.add("Campo Fecha de Dosis Td4 no valido", new ActionMessage("errors.formatoFechaInvalido","de Dosis Td4"));
	            }
	            else if (fechaDosisTd4Transformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Dosis Td4 no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Dosis Td4", "actual"));
				}
	            /*
	            else if (fechaDosisTd4Transformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha de Dosis Td4 no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Dosis Td4", "de Nacimiento de la Madre"));
				}
				*/
            }
        	
        	if (fechaIngresoParto.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaIngresoParto)) {
	                
	                errores.add("Campo Fecha de Ingreso Parto no valido", new ActionMessage("errors.formatoFechaInvalido","de Ingreso Parto"));
	            }
	            else if (fechaIngresoPartoTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Ingreso Parto no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ingreso Parto", "actual"));
				}
            }
        	
        	if (fechaEgresoParto.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaEgresoParto)) {
	                
	                errores.add("Campo Fecha de Egreso Parto no valido", new ActionMessage("errors.formatoFechaInvalido","de Egreso Parto"));
	            }
	            else if (fechaEgresoPartoTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Egreso Parto no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Egreso Parto", "actual"));
				}
            }
        	
        	if (fechaInvestigacionCampo.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaInvestigacionCampo)) {
	                
	                errores.add("Campo Fecha de Investigacion de Campo no valido", new ActionMessage("errors.formatoFechaInvalido","de Investigacion de Campo"));
	            }
	            else if (fechaInvestigacionCampoTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Investigacion de Campo no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Investigacion de Campo", "actual"));
				}
            }
        	
        	if (fechaVacunacion.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaVacunacion)) {
	                
	                errores.add("Campo Fecha de Vacunacion no valido", new ActionMessage("errors.formatoFechaInvalido","de Vacunacion"));
	            }
	            else if (fechaVacunacionTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Vacunacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Vacunacion", "actual"));
				}
            }
        }
        
        if (estado.equals("validar")) {
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
  
        	String fechaNacimientoMadreTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaNacimientoMadre);
        	String fechaEgresoHospitalTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaEgresoHospital);
        	String fechaDejoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDejo);
        	
        	String fechaUltimoControlTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaUltimoControl);
        	
        	String fechaDosisTd1Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaDosisTd1);
        	String fechaDosisTd2Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaDosisTd2);
        	String fechaDosisTd3Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaDosisTd3);
        	String fechaDosisTd4Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaDosisTd4);
        	
        	String fechaIngresoPartoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaIngresoParto);
        	String fechaEgresoPartoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaEgresoParto);
        	
        	String fechaInvestigacionCampoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInvestigacionCampo);
        	String fechaVacunacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunacion);
        	
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
	            else if (fechaHospitalizacionTransformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha de Hospitalizacion no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Hospitalizacion", "de Nacimiento de la Madre"));
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
        	
        	if (fechaNacimientoMadre.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaNacimientoMadre)) {
	                
	                errores.add("Campo Fecha Nacimiento del Recien Nacido no valido", new ActionMessage("errors.formatoFechaInvalido","Nacimiento del Recien Nacido"));
	            }
	            else if (fechaNacimientoMadreTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha Nacimiento del Recien Nacido no valido", new ActionMessage("errors.fechaPosteriorIgualActual","Nacimiento del Recien Nacido", "actual"));
				}
            }
        	
        	if (fechaEgresoHospital.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaEgresoHospital)) {
	                
	                errores.add("Campo Fecha Egreso Hospital no valido", new ActionMessage("errors.formatoFechaInvalido","Egreso Hospital"));
	            }
	            else if (fechaEgresoHospitalTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha Egreso Hospital no valido", new ActionMessage("errors.fechaPosteriorIgualActual","Egreso Hospital", "actual"));
				}
	            else if (fechaEgresoHospitalTransformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha Egreso Hospital no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","Egreso Hospital", "de Nacimiento del Recien Nacido"));
				}
            }
        	
        	if (fechaDejo.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaDejo)) {
	                
	                errores.add("Campo Fecha en que Dejo de Mamar no valido", new ActionMessage("errors.formatoFechaInvalido","en que Dejo de Mamar"));
	            }
	            else if (fechaDejoTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha en que Dejo de Mamar no valido", new ActionMessage("errors.fechaPosteriorIgualActual","en que Dejo de Mamar", "actual"));
				}
	            else if (fechaDejoTransformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha en que Dejo de Mamar no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","en que Dejo de Mamar", "de Nacimiento del Recien Nacido"));
				}
            }
        	
        	if (fechaUltimoControl.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaUltimoControl)) {
	                
	                errores.add("Campo Fecha de Ultimo Control no valido", new ActionMessage("errors.formatoFechaInvalido","de Ultimo Control"));
	            }
	            else if (fechaUltimoControlTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Ultimo Control no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ultimo Control", "actual"));
				}
	            /*
	            else if (fechaUltimoControlTransformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha de Ultimo Control no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Ultimo Control", "de Nacimiento de la Madre"));
				}
				*/
            }
        	
        	if (fechaDosisTd1.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaDosisTd1)) {
	                
	                errores.add("Campo Fecha de Dosis Td1 no valido", new ActionMessage("errors.formatoFechaInvalido","de Dosis Td1"));
	            }
	            else if (fechaDosisTd1Transformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Dosis Td1 no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Dosis Td1", "actual"));
				}
	            /*
	            else if (fechaDosisTd1Transformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha de Dosis Td1 no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Dosis Td1", "de Nacimiento de la Madre"));
				}
				*/
            }
        	
        	if (fechaDosisTd2.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaDosisTd2)) {
	                
	                errores.add("Campo Fecha de Dosis Td2 no valido", new ActionMessage("errors.formatoFechaInvalido","de Dosis Td2"));
	            }
	            else if (fechaDosisTd2Transformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Dosis Td2 no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Dosis Td2", "actual"));
				}
	            /*
	            else if (fechaDosisTd2Transformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha de Dosis Td2 no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Dosis Td2", "de Nacimiento de la Madre"));
				}
				*/
            }
        	
        	if (fechaDosisTd3.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaDosisTd3)) {
	                
	                errores.add("Campo Fecha de Dosis Td3 no valido", new ActionMessage("errors.formatoFechaInvalido","de Dosis Td3"));
	            }
	            else if (fechaDosisTd3Transformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Dosis Td3 no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Dosis Td3", "actual"));
				}
	            /*
	            else if (fechaDosisTd3Transformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha de Dosis Td3 no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Dosis Td3", "de Nacimiento de la Madre"));
				}
				*/
            }
        	
        	if (fechaDosisTd4.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaDosisTd4)) {
	                
	                errores.add("Campo Fecha de Dosis Td4 no valido", new ActionMessage("errors.formatoFechaInvalido","de Dosis Td4"));
	            }
	            else if (fechaDosisTd4Transformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Dosis Td4 no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Dosis Td4", "actual"));
				}
	            /*
	            else if (fechaDosisTd4Transformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha de Dosis Td4 no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Dosis Td4", "de Nacimiento de la Madre"));
				}
				*/
            }
        	
        	if (fechaIngresoParto.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaIngresoParto)) {
	                
	                errores.add("Campo Fecha de Ingreso Parto no valido", new ActionMessage("errors.formatoFechaInvalido","de Ingreso Parto"));
	            }
	            else if (fechaIngresoPartoTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Ingreso Parto no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ingreso Parto", "actual"));
				}
	            /*
	            else if (fechaIngresoPartoTransformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha de Ingreso Parto no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Ingreso Parto", "de Nacimiento de la Madre"));
				}
				*/
            }
        	
        	if (fechaEgresoParto.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaEgresoParto)) {
	                
	                errores.add("Campo Fecha de Egreso Parto no valido", new ActionMessage("errors.formatoFechaInvalido","de Egreso Parto"));
	            }
	            else if (fechaEgresoPartoTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Egreso Parto no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Egreso Parto", "actual"));
				}
	            else if (fechaEgresoPartoTransformada.compareTo(fechaNacimientoMadreTransformada)<0) {
					
					errores.add("Campo Fecha de Egreso Parto no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Egreso Parto", "de Nacimiento del Recien Nacido"));
				}
	            else if (fechaEgresoPartoTransformada.compareTo(fechaIngresoPartoTransformada)<0) {
					
					errores.add("Campo Fecha de Egreso Parto no valido", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Egreso Parto", "de Ingreso al Parto"));
				}
            }
        	
        	if (fechaInvestigacionCampo.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaInvestigacionCampo)) {
	                
	                errores.add("Campo Fecha de Investigacion de Campo no valido", new ActionMessage("errors.formatoFechaInvalido","de Investigacion de Campo"));
	            }
	            else if (fechaInvestigacionCampoTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Investigacion de Campo no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Investigacion de Campo", "actual"));
				}
            }
        	
        	if (fechaVacunacion.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaVacunacion)) {
	                
	                errores.add("Campo Fecha de Vacunacion no valido", new ActionMessage("errors.formatoFechaInvalido","de Vacunacion"));
	            }
	            else if (fechaVacunacionTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Vacunacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Vacunacion", "actual"));
				}
            }
        	
        }
	    
	    return errores;
	}  
    

	public boolean isAntecedenteVacunaAnti() {
		return antecedenteVacunaAnti;
	}

	public void setAntecedenteVacunaAnti(boolean antecedenteVacunaAnti) {
		this.antecedenteVacunaAnti = antecedenteVacunaAnti;
	}

	public boolean isAplicacionSustanciasMunon() {
		return aplicacionSustanciasMunon;
	}

	public void setAplicacionSustanciasMunon(boolean aplicacionSustanciasMunon) {
		this.aplicacionSustanciasMunon = aplicacionSustanciasMunon;
	}

	public boolean isAsistioControl() {
		return asistioControl;
	}

	public void setAsistioControl(boolean asistioControl) {
		this.asistioControl = asistioControl;
	}

	public boolean isAtendidoPorAuxiliar() {
		return atendidoPorAuxiliar;
	}

	public void setAtendidoPorAuxiliar(boolean atendidoPorAuxiliar) {
		this.atendidoPorAuxiliar = atendidoPorAuxiliar;
	}

	public boolean isAtendidoPorEnfermero() {
		return atendidoPorEnfermero;
	}

	public void setAtendidoPorEnfermero(boolean atendidoPorEnfermero) {
		this.atendidoPorEnfermero = atendidoPorEnfermero;
	}

	public boolean isAtendidoPorMedico() {
		return atendidoPorMedico;
	}

	public void setAtendidoPorMedico(boolean atendidoPorMedico) {
		this.atendidoPorMedico = atendidoPorMedico;
	}

	public boolean isAtendidoPorOtro() {
		return atendidoPorOtro;
	}

	public void setAtendidoPorOtro(boolean atendidoPorOtro) {
		this.atendidoPorOtro = atendidoPorOtro;
	}

	public boolean isAtendidoPorPromotor() {
		return atendidoPorPromotor;
	}

	public void setAtendidoPorPromotor(boolean atendidoPorPromotor) {
		this.atendidoPorPromotor = atendidoPorPromotor;
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

	public int getCoberturaLograda() {
		return coberturaLograda;
	}

	public void setCoberturaLograda(int coberturaLograda) {
		this.coberturaLograda = coberturaLograda;
	}

	public String getCodigoDepartamentoVivienda() {
		return codigoDepartamentoVivienda;
	}

	public void setCodigoDepartamentoVivienda(String codigoDepartamentoVivienda) {
		this.codigoDepartamentoVivienda = codigoDepartamentoVivienda;
	}

	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}

	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}

	public int getCodigoFichaTetanos() {
		return codigoFichaTetanos;
	}

	public void setCodigoFichaTetanos(int codigoFichaTetanos) {
		this.codigoFichaTetanos = codigoFichaTetanos;
	}

	public String getCodigoMunicipioVivienda() {
		return codigoMunicipioVivienda;
	}

	public void setCodigoMunicipioVivienda(String codigoMunicipioVivienda) {
		this.codigoMunicipioVivienda = codigoMunicipioVivienda;
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

	public boolean isContracciones() {
		return contracciones;
	}

	public void setContracciones(boolean contracciones) {
		this.contracciones = contracciones;
	}

	public boolean isConvulsiones() {
		return convulsiones;
	}

	public void setConvulsiones(boolean convulsiones) {
		this.convulsiones = convulsiones;
	}

	public String getCualesSustancias() {
		return cualesSustancias;
	}

	public void setCualesSustancias(String cualesSustancias) {
		this.cualesSustancias = cualesSustancias;
	}

	public boolean isDejoMamar() {
		return dejoMamar;
	}

	public void setDejoMamar(boolean dejoMamar) {
		this.dejoMamar = dejoMamar;
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

	public boolean isDificultadRespiratoria() {
		return dificultadRespiratoria;
	}

	public void setDificultadRespiratoria(boolean dificultadRespiratoria) {
		this.dificultadRespiratoria = dificultadRespiratoria;
	}

	public String getDireccionPaciente() {
		return direccionPaciente;
	}

	public void setDireccionPaciente(String direccionPaciente) {
		this.direccionPaciente = direccionPaciente;
	}

	public int getDistanciaMinutos() {
		return distanciaMinutos;
	}

	public void setDistanciaMinutos(int distanciaMinutos) {
		this.distanciaMinutos = distanciaMinutos;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public int getDosisDpt() {
		return dosisDpt;
	}

	public void setDosisDpt(int dosisDpt) {
		this.dosisDpt = dosisDpt;
	}

	public int getDosisTd1AplicadasGest() {
		return dosisTd1AplicadasGest;
	}

	public void setDosisTd1AplicadasGest(int dosisTd1AplicadasGest) {
		this.dosisTd1AplicadasGest = dosisTd1AplicadasGest;
	}

	public int getDosisTd1AplicadasMef() {
		return dosisTd1AplicadasMef;
	}

	public void setDosisTd1AplicadasMef(int dosisTd1AplicadasMef) {
		this.dosisTd1AplicadasMef = dosisTd1AplicadasMef;
	}

	public int getDosisTd2AplicadasGest() {
		return dosisTd2AplicadasGest;
	}

	public void setDosisTd2AplicadasGest(int dosisTd2AplicadasGest) {
		this.dosisTd2AplicadasGest = dosisTd2AplicadasGest;
	}

	public int getDosisTd2AplicadasMef() {
		return dosisTd2AplicadasMef;
	}

	public void setDosisTd2AplicadasMef(int dosisTd2AplicadasMef) {
		this.dosisTd2AplicadasMef = dosisTd2AplicadasMef;
	}

	public int getDosisTd3AplicadasGest() {
		return dosisTd3AplicadasGest;
	}

	public void setDosisTd3AplicadasGest(int dosisTd3AplicadasGest) {
		this.dosisTd3AplicadasGest = dosisTd3AplicadasGest;
	}

	public int getDosisTd3AplicadasMef() {
		return dosisTd3AplicadasMef;
	}

	public void setDosisTd3AplicadasMef(int dosisTd3AplicadasMef) {
		this.dosisTd3AplicadasMef = dosisTd3AplicadasMef;
	}

	public int getDosisTd4AplicadasGest() {
		return dosisTd4AplicadasGest;
	}

	public void setDosisTd4AplicadasGest(int dosisTd4AplicadasGest) {
		this.dosisTd4AplicadasGest = dosisTd4AplicadasGest;
	}

	public int getDosisTd4AplicadasMef() {
		return dosisTd4AplicadasMef;
	}

	public void setDosisTd4AplicadasMef(int dosisTd4AplicadasMef) {
		this.dosisTd4AplicadasMef = dosisTd4AplicadasMef;
	}

	public int getDosisTd5AplicadasGest() {
		return dosisTd5AplicadasGest;
	}

	public void setDosisTd5AplicadasGest(int dosisTd5AplicadasGest) {
		this.dosisTd5AplicadasGest = dosisTd5AplicadasGest;
	}

	public int getDosisTd5AplicadasMef() {
		return dosisTd5AplicadasMef;
	}

	public void setDosisTd5AplicadasMef(int dosisTd5AplicadasMef) {
		this.dosisTd5AplicadasMef = dosisTd5AplicadasMef;
	}

	public String getEdad() {
		return edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}

	public int getEdadMadre() {
		return edadMadre;
	}

	public void setEdadMadre(int edadMadre) {
		this.edadMadre = edadMadre;
	}

	public boolean isEpisodiosApnea() {
		return episodiosApnea;
	}

	public void setEpisodiosApnea(boolean episodiosApnea) {
		this.episodiosApnea = episodiosApnea;
	}

	public boolean isEspasmos() {
		return espasmos;
	}

	public void setEspasmos(boolean espasmos) {
		this.espasmos = espasmos;
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

	public String getExplicacionNoAsistencia() {
		return explicacionNoAsistencia;
	}

	public void setExplicacionNoAsistencia(String explicacionNoAsistencia) {
		this.explicacionNoAsistencia = explicacionNoAsistencia;
	}

	public String getExplicacionNoVacuna() {
		return explicacionNoVacuna;
	}

	public void setExplicacionNoVacuna(String explicacionNoVacuna) {
		this.explicacionNoVacuna = explicacionNoVacuna;
	}

	public String getFechaDejo() {
		return fechaDejo;
	}

	public void setFechaDejo(String fechaDejo) {
		this.fechaDejo = fechaDejo;
	}

	public String getFechaDosisTd1() {
		return fechaDosisTd1;
	}

	public void setFechaDosisTd1(String fechaDosisTd1) {
		this.fechaDosisTd1 = fechaDosisTd1;
	}

	public String getFechaDosisTd2() {
		return fechaDosisTd2;
	}

	public void setFechaDosisTd2(String fechaDosisTd2) {
		this.fechaDosisTd2 = fechaDosisTd2;
	}

	public String getFechaDosisTd3() {
		return fechaDosisTd3;
	}

	public void setFechaDosisTd3(String fechaDosisTd3) {
		this.fechaDosisTd3 = fechaDosisTd3;
	}

	public String getFechaDosisTd4() {
		return fechaDosisTd4;
	}

	public void setFechaDosisTd4(String fechaDosisTd4) {
		this.fechaDosisTd4 = fechaDosisTd4;
	}

	public String getFechaEgresoHospital() {
		return fechaEgresoHospital;
	}

	public void setFechaEgresoHospital(String fechaEgresoHospital) {
		this.fechaEgresoHospital = fechaEgresoHospital;
	}

	public String getFechaEgresoParto() {
		return fechaEgresoParto;
	}

	public void setFechaEgresoParto(String fechaEgresoParto) {
		this.fechaEgresoParto = fechaEgresoParto;
	}

	public String getFechaIngresoParto() {
		return fechaIngresoParto;
	}

	public void setFechaIngresoParto(String fechaIngresoParto) {
		this.fechaIngresoParto = fechaIngresoParto;
	}

	public String getFechaInvestigacionCampo() {
		return fechaInvestigacionCampo;
	}

	public void setFechaInvestigacionCampo(String fechaInvestigacionCampo) {
		this.fechaInvestigacionCampo = fechaInvestigacionCampo;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getFechaNacimientoMadre() {
		return fechaNacimientoMadre;
	}

	public void setFechaNacimientoMadre(String fechaNacimientoMadre) {
		this.fechaNacimientoMadre = fechaNacimientoMadre;
	}

	public String getFechaUltimoControl() {
		return fechaUltimoControl;
	}

	public void setFechaUltimoControl(String fechaUltimoControl) {
		this.fechaUltimoControl = fechaUltimoControl;
	}

	public String getFechaVacunacion() {
		return fechaVacunacion;
	}

	public void setFechaVacunacion(String fechaVacunacion) {
		this.fechaVacunacion = fechaVacunacion;
	}

	public boolean isFontAbombada() {
		return fontAbombada;
	}

	public void setFontAbombada(boolean fontAbombada) {
		this.fontAbombada = fontAbombada;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public boolean isHipertermia() {
		return hipertermia;
	}

	public void setHipertermia(boolean hipertermia) {
		this.hipertermia = hipertermia;
	}

	public boolean isHipotermia() {
		return hipotermia;
	}

	public void setHipotermia(boolean hipotermia) {
		this.hipotermia = hipotermia;
	}

	public String getInstitucionParto() {
		return institucionParto;
	}

	public void setInstitucionParto(String institucionParto) {
		this.institucionParto = institucionParto;
	}

	public String getInstrumentoCordon() {
		return instrumentoCordon;
	}

	public void setInstrumentoCordon(String instrumentoCordon) {
		this.instrumentoCordon = instrumentoCordon;
	}

	public boolean isLlantoExcesivo() {
		return llantoExcesivo;
	}

	public void setLlantoExcesivo(boolean llantoExcesivo) {
		this.llantoExcesivo = llantoExcesivo;
	}

	public boolean isLlantoNacer() {
		return llantoNacer;
	}

	public void setLlantoNacer(boolean llantoNacer) {
		this.llantoNacer = llantoNacer;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public int getLugarParto() {
		return lugarParto;
	}

	public void setLugarParto(int lugarParto) {
		this.lugarParto = lugarParto;
	}

	public boolean isMadreVivioMismoLugar() {
		return madreVivioMismoLugar;
	}

	public void setMadreVivioMismoLugar(boolean madreVivioMismoLugar) {
		this.madreVivioMismoLugar = madreVivioMismoLugar;
	}

	public boolean isMamabaNormal() {
		return mamabaNormal;
	}

	public void setMamabaNormal(boolean mamabaNormal) {
		this.mamabaNormal = mamabaNormal;
	}

	public String getMetodoEsterilizacion() {
		return metodoEsterilizacion;
	}

	public void setMetodoEsterilizacion(String metodoEsterilizacion) {
		this.metodoEsterilizacion = metodoEsterilizacion;
	}

	public boolean isNacimientoTraumatico() {
		return nacimientoTraumatico;
	}

	public void setNacimientoTraumatico(boolean nacimientoTraumatico) {
		this.nacimientoTraumatico = nacimientoTraumatico;
	}

	public String getNombreMadre() {
		return nombreMadre;
	}

	public void setNombreMadre(String nombreMadre) {
		this.nombreMadre = nombreMadre;
	}

	public boolean isNotificar() {
		return notificar;
	}

	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}

	public int getNumeroControlesPrevios() {
		return numeroControlesPrevios;
	}

	public void setNumeroControlesPrevios(int numeroControlesPrevios) {
		this.numeroControlesPrevios = numeroControlesPrevios;
	}

	public int getNumeroEmbarazos() {
		return numeroEmbarazos;
	}

	public void setNumeroEmbarazos(int numeroEmbarazos) {
		this.numeroEmbarazos = numeroEmbarazos;
	}

	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public boolean isOpistotonos() {
		return opistotonos;
	}

	public void setOpistotonos(boolean opistotonos) {
		this.opistotonos = opistotonos;
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

	public String getQuienAtendio() {
		return quienAtendio;
	}

	public void setQuienAtendio(String quienAtendio) {
		this.quienAtendio = quienAtendio;
	}

	public int getQuienAtendioParto() {
		return quienAtendioParto;
	}

	public void setQuienAtendioParto(int quienAtendioParto) {
		this.quienAtendioParto = quienAtendioParto;
	}

	public boolean isRecibioInformacionMunon() {
		return recibioInformacionMunon;
	}

	public void setRecibioInformacionMunon(boolean recibioInformacionMunon) {
		this.recibioInformacionMunon = recibioInformacionMunon;
	}

	public boolean isRigidezNuca() {
		return rigidezNuca;
	}

	public void setRigidezNuca(boolean rigidezNuca) {
		this.rigidezNuca = rigidezNuca;
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

	public boolean isSepsisUmbilical() {
		return sepsisUmbilical;
	}

	public void setSepsisUmbilical(boolean sepsisUmbilical) {
		this.sepsisUmbilical = sepsisUmbilical;
	}

	public String getSire() {
		return sire;
	}

	public void setSire(String sire) {
		this.sire = sire;
	}

	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}

	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}

	public boolean isTrismus() {
		return trismus;
	}

	public void setTrismus(boolean trismus) {
		this.trismus = trismus;
	}


	public String getLugarVivienda() {
		return lugarVivienda;
	}


	public void setLugarVivienda(String lugarVivienda) {
		this.lugarVivienda = lugarVivienda;
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
