package com.sysmedica.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class FichaTosferinaForm extends ValidatorForm {

	private boolean fichamodulo;
	private String esPrimeraVez;
	private boolean vieneDeFichasAnteriores;
	private String urlFichasAnteriores;

	private boolean activa;
	private boolean hayServicios;
    private boolean hayLaboratorios;
    private int codigoInstitucion;
    private boolean notificar;
    private String loginUsuario;
    private int codigoFichaDifteria;
    private int codigoConvenio;
    private String valorDivDx;
    private int codigoEnfNotificable;
    
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
    
    private int codigoFichaTosferina;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
    private String nombrePadre;
    private String fechaInvestigacion;
    private int identificacionCaso;
    private int contactoCaso;
    private int carneVacunacion;
    private int dosisAplicadas;
    private int tipoVacuna;
    private String cualVacuna;
    private String fechaUltimaDosis;
    private int etapaEnfermedad;
    private int tos;
    private String duracionTos;
    private int tosParoxistica;
    private int estridor;
    private int apnea;
    private int fiebre;
    private int vomitoPostusivo;
    private int complicaciones;
    private int tipoComplicacion;
    private int tratamientoAntibiotico;
    private String tipoAntibiotico;
    private String duracionTratamiento;
    private int investigacionCampo;
    private String fechaOperacionBarrido;
    private String numeroContactos;
    private int quimioprofilaxis;
    private int totalPoblacion1;
    private int totalPoblacion2;
    private int totalPoblacion3;
    private int totalPoblacion4;
    private int dosisDpt1Grupo1;
    private int dosisDpt1Grupo2;
    private int dosisDpt1Grupo3;
    private int dosisDpt1Grupo4;
    private int dosisDpt2Grupo1;
    private int dosisDpt2Grupo2;
    private int dosisDpt2Grupo3;
    private int dosisDpt2Grupo4;
    private int dosisDpt3Grupo1;
    private int dosisDpt3Grupo2;
    private int dosisDpt3Grupo3;
    private int dosisDpt3Grupo4;
    private int dosisRef1Grupo1;
    private int dosisRef1Grupo2;
    private int dosisRef1Grupo3;
    private int dosisRef1Grupo4;
    private int dosisRef2Grupo1;
    private int dosisRef2Grupo2;
    private int dosisRef2Grupo3;
    private int dosisRef2Grupo4;
    private String municipiosVacunados;
    
    private HashMap datosLaboratorio;
    
    private int totalPoblacion;
    private int totalDpt1;
    private int totalDpt2;
    private int totalDpt3;
    private int totalRef1;
    private int totalRef2;
    private int totalTotal;
    private int totalGrupo1;
    private int totalGrupo2;
    private int totalGrupo3;
	

    private String pais;
    private int areaProcedencia;
    private String grupoPoblacional;
    
    
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
    
    private String estado;
    
    
    public void reset() {
    	
    	nombrePadre = "";
    	fechaInvestigacion = "";
    	identificacionCaso = 0;
        contactoCaso = 0;
        carneVacunacion = 0;
        dosisAplicadas = 0;
        tipoVacuna = 0;
        cualVacuna = "";
        fechaUltimaDosis = "";
        etapaEnfermedad = 0;
        tos = 0;
        duracionTos = "";
        tosParoxistica = 0;
        estridor = 0;
        apnea = 0;
        fiebre = 0;
        vomitoPostusivo = 0;
        complicaciones = 0;
        tipoComplicacion = 0;
        tratamientoAntibiotico = 0;
        tipoAntibiotico = "";
        duracionTratamiento = "";
        investigacionCampo = 0;
        fechaOperacionBarrido = "";
        numeroContactos = "";
        quimioprofilaxis = 0;
        totalPoblacion1 = 0;
        totalPoblacion2 = 0;
        totalPoblacion3 = 0;
        totalPoblacion4 = 0;
        dosisDpt1Grupo1 = 0;
        dosisDpt1Grupo2 = 0;
        dosisDpt1Grupo3 = 0;
        dosisDpt1Grupo4 = 0;
        dosisDpt2Grupo1 = 0;
        dosisDpt2Grupo2 = 0;
        dosisDpt2Grupo3 = 0;
        dosisDpt2Grupo4 = 0;
        dosisDpt3Grupo1 = 0;
        dosisDpt3Grupo2 = 0;
        dosisDpt3Grupo3 = 0;
        dosisDpt3Grupo4 = 0;
        dosisRef1Grupo1 = 0;
        dosisRef1Grupo2 = 0;
        dosisRef1Grupo3 = 0;
        dosisRef1Grupo4 = 0;
        dosisRef2Grupo1 = 0;
        dosisRef2Grupo2 = 0;
        dosisRef2Grupo3 = 0;
        dosisRef2Grupo4 = 0;
        municipiosVacunados = "";
        
    	String ciudad = ValoresPorDefecto.getCiudadNacimiento(codigoInstitucion);
        String lugar = ciudad.split("-")[1]+"-"+ciudad.split("-")[0];
        
        lugarNotifica = lugar;
        lugarProcedencia = lugar;
        
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
        
        datosLaboratorio = new HashMap();
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
        	
        	String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
            String fechaUltimaDosisTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaUltimaDosis);
            String fechaOperacionBarridoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaOperacionBarrido);
        	
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
	            
	            if (fechaUltimaDosis.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaUltimaDosis)) {
		                
		                errores.add("Campo Fecha de Ultima Dosis no valido", new ActionMessage("errors.formatoFechaInvalido","de Ultima Dosis"));
		            }
		            else if (fechaUltimaDosisTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Ultima Dosis no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ultima Dosis", "actual"));
					}
	            }
	            
	            if (fechaOperacionBarrido.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaOperacionBarrido)) {
		                
		                errores.add("Campo Fecha de Operacion Barrido no valido", new ActionMessage("errors.formatoFechaInvalido","de Operacion Barrido"));
		            }
		            else if (fechaOperacionBarridoTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Operacion Barrido no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Operacion Barrido", "actual"));
					}
	            }
	            
            }
            catch (NullPointerException npe) {}
            
            
            if (duracionTos.length()>0) {
	            try {
	            	int durTos = Integer.parseInt(duracionTos);
	            }
	            catch (NumberFormatException nfe) {
	            	
	            	errores.add("Campo Duración de Tos debe ser Numérico", new ActionMessage("error.epidemiologia.valornumericogeneral","Duración de Tos"));
	            }
            }
        }
        
        if (estado.equals("validar")) {
        	
        	String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
            String fechaUltimaDosisTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaUltimaDosis);
            String fechaOperacionBarridoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaOperacionBarrido);
        	
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
	            
	            if (fechaUltimaDosis.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaUltimaDosis)) {
		                
		                errores.add("Campo Fecha de Ultima Dosis no valido", new ActionMessage("errors.formatoFechaInvalido","de Ultima Dosis"));
		            }
		            else if (fechaUltimaDosisTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Ultima Dosis no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ultima Dosis", "actual"));
					}
	            }
	            
	            if (fechaOperacionBarrido.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaOperacionBarrido)) {
		                
		                errores.add("Campo Fecha de Operacion Barrido no valido", new ActionMessage("errors.formatoFechaInvalido","de Operacion Barrido"));
		            }
		            else if (fechaOperacionBarridoTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Operacion Barrido no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Operacion Barrido", "actual"));
					}
	            }
	            
            }
            catch (NullPointerException npe) {}
            
            if (duracionTos.length()>0) {
	            try {
	            	int durTos = Integer.parseInt(duracionTos);
	            }
	            catch (NumberFormatException nfe) {
	            	
	            	errores.add("Campo Duración de Tos debe ser Numérico", new ActionMessage("error.epidemiologia.valornumericogeneral","Duración de Tos"));
	            }
            }
        }
        
        return errores;
    }




	public boolean isActiva() {
		return activa;
	}




	public void setActiva(boolean activa) {
		this.activa = activa;
	}




	public int getAnyoSemanaEpi() {
		return anyoSemanaEpi;
	}




	public void setAnyoSemanaEpi(int anyoSemanaEpi) {
		this.anyoSemanaEpi = anyoSemanaEpi;
	}




	public int getApnea() {
		return apnea;
	}




	public void setApnea(int apnea) {
		this.apnea = apnea;
	}




	public int getAreaProcedencia() {
		return areaProcedencia;
	}




	public void setAreaProcedencia(int areaProcedencia) {
		this.areaProcedencia = areaProcedencia;
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




	public int getCarneVacunacion() {
		return carneVacunacion;
	}




	public void setCarneVacunacion(int carneVacunacion) {
		this.carneVacunacion = carneVacunacion;
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




	public int getCodigoConvenio() {
		return codigoConvenio;
	}




	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
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




	public int getCodigoEnfNotificable() {
		return codigoEnfNotificable;
	}




	public void setCodigoEnfNotificable(int codigoEnfNotificable) {
		this.codigoEnfNotificable = codigoEnfNotificable;
	}




	public int getCodigoFichaTosferina() {
		return codigoFichaTosferina;
	}




	public void setCodigoFichaTosferina(int codigoFichaTosferina) {
		this.codigoFichaTosferina = codigoFichaTosferina;
	}




	public int getCodigoFichaDifteria() {
		return codigoFichaDifteria;
	}




	public void setCodigoFichaDifteria(int codigoFichaDifteria) {
		this.codigoFichaDifteria = codigoFichaDifteria;
	}




	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}




	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
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




	public int getComplicaciones() {
		return complicaciones;
	}




	public void setComplicaciones(int complicaciones) {
		this.complicaciones = complicaciones;
	}




	public int getContactoCaso() {
		return contactoCaso;
	}




	public void setContactoCaso(int contactoCaso) {
		this.contactoCaso = contactoCaso;
	}




	public String getCualVacuna() {
		return cualVacuna;
	}




	public void setCualVacuna(String cualVacuna) {
		this.cualVacuna = cualVacuna;
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




	public String getDiagnosticosSeleccionados() {
		return diagnosticosSeleccionados;
	}




	public void setDiagnosticosSeleccionados(String diagnosticosSeleccionados) {
		this.diagnosticosSeleccionados = diagnosticosSeleccionados;
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




	public int getDosisDpt1Grupo1() {
		return dosisDpt1Grupo1;
	}




	public void setDosisDpt1Grupo1(int dosisDpt1Grupo1) {
		this.dosisDpt1Grupo1 = dosisDpt1Grupo1;
	}




	public int getDosisDpt1Grupo2() {
		return dosisDpt1Grupo2;
	}




	public void setDosisDpt1Grupo2(int dosisDpt1Grupo2) {
		this.dosisDpt1Grupo2 = dosisDpt1Grupo2;
	}




	public int getDosisDpt1Grupo3() {
		return dosisDpt1Grupo3;
	}




	public void setDosisDpt1Grupo3(int dosisDpt1Grupo3) {
		this.dosisDpt1Grupo3 = dosisDpt1Grupo3;
	}




	public int getDosisDpt1Grupo4() {
		return dosisDpt1Grupo4;
	}




	public void setDosisDpt1Grupo4(int dosisDpt1Grupo4) {
		this.dosisDpt1Grupo4 = dosisDpt1Grupo4;
	}




	public int getDosisDpt2Grupo1() {
		return dosisDpt2Grupo1;
	}




	public void setDosisDpt2Grupo1(int dosisDpt2Grupo1) {
		this.dosisDpt2Grupo1 = dosisDpt2Grupo1;
	}




	public int getDosisDpt2Grupo2() {
		return dosisDpt2Grupo2;
	}




	public void setDosisDpt2Grupo2(int dosisDpt2Grupo2) {
		this.dosisDpt2Grupo2 = dosisDpt2Grupo2;
	}




	public int getDosisDpt2Grupo3() {
		return dosisDpt2Grupo3;
	}




	public void setDosisDpt2Grupo3(int dosisDpt2Grupo3) {
		this.dosisDpt2Grupo3 = dosisDpt2Grupo3;
	}




	public int getDosisDpt2Grupo4() {
		return dosisDpt2Grupo4;
	}




	public void setDosisDpt2Grupo4(int dosisDpt2Grupo4) {
		this.dosisDpt2Grupo4 = dosisDpt2Grupo4;
	}




	public int getDosisDpt3Grupo1() {
		return dosisDpt3Grupo1;
	}




	public void setDosisDpt3Grupo1(int dosisDpt3Grupo1) {
		this.dosisDpt3Grupo1 = dosisDpt3Grupo1;
	}




	public int getDosisDpt3Grupo2() {
		return dosisDpt3Grupo2;
	}




	public void setDosisDpt3Grupo2(int dosisDpt3Grupo2) {
		this.dosisDpt3Grupo2 = dosisDpt3Grupo2;
	}




	public int getDosisDpt3Grupo3() {
		return dosisDpt3Grupo3;
	}




	public void setDosisDpt3Grupo3(int dosisDpt3Grupo3) {
		this.dosisDpt3Grupo3 = dosisDpt3Grupo3;
	}




	public int getDosisDpt3Grupo4() {
		return dosisDpt3Grupo4;
	}




	public void setDosisDpt3Grupo4(int dosisDpt3Grupo4) {
		this.dosisDpt3Grupo4 = dosisDpt3Grupo4;
	}




	public int getDosisRef1Grupo1() {
		return dosisRef1Grupo1;
	}




	public void setDosisRef1Grupo1(int dosisRef1Grupo1) {
		this.dosisRef1Grupo1 = dosisRef1Grupo1;
	}




	public int getDosisRef1Grupo2() {
		return dosisRef1Grupo2;
	}




	public void setDosisRef1Grupo2(int dosisRef1Grupo2) {
		this.dosisRef1Grupo2 = dosisRef1Grupo2;
	}




	public int getDosisRef1Grupo3() {
		return dosisRef1Grupo3;
	}




	public void setDosisRef1Grupo3(int dosisRef1Grupo3) {
		this.dosisRef1Grupo3 = dosisRef1Grupo3;
	}




	public int getDosisRef1Grupo4() {
		return dosisRef1Grupo4;
	}




	public void setDosisRef1Grupo4(int dosisRef1Grupo4) {
		this.dosisRef1Grupo4 = dosisRef1Grupo4;
	}




	public int getDosisRef2Grupo1() {
		return dosisRef2Grupo1;
	}




	public void setDosisRef2Grupo1(int dosisRef2Grupo1) {
		this.dosisRef2Grupo1 = dosisRef2Grupo1;
	}




	public int getDosisRef2Grupo2() {
		return dosisRef2Grupo2;
	}




	public void setDosisRef2Grupo2(int dosisRef2Grupo2) {
		this.dosisRef2Grupo2 = dosisRef2Grupo2;
	}




	public int getDosisRef2Grupo3() {
		return dosisRef2Grupo3;
	}




	public void setDosisRef2Grupo3(int dosisRef2Grupo3) {
		this.dosisRef2Grupo3 = dosisRef2Grupo3;
	}




	public int getDosisRef2Grupo4() {
		return dosisRef2Grupo4;
	}




	public void setDosisRef2Grupo4(int dosisRef2Grupo4) {
		this.dosisRef2Grupo4 = dosisRef2Grupo4;
	}




	public String getDuracionTos() {
		return duracionTos;
	}




	public void setDuracionTos(String duracionTos) {
		this.duracionTos = duracionTos;
	}




	public String getDuracionTratamiento() {
		return duracionTratamiento;
	}




	public void setDuracionTratamiento(String duracionTratamiento) {
		this.duracionTratamiento = duracionTratamiento;
	}




	public String getEdad() {
		return edad;
	}




	public void setEdad(String edad) {
		this.edad = edad;
	}




	public boolean isEpidemiologia() {
		return epidemiologia;
	}




	public void setEpidemiologia(boolean epidemiologia) {
		this.epidemiologia = epidemiologia;
	}




	public String getEsPrimeraVez() {
		return esPrimeraVez;
	}




	public void setEsPrimeraVez(String esPrimeraVez) {
		this.esPrimeraVez = esPrimeraVez;
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




	public int getEstridor() {
		return estridor;
	}




	public void setEstridor(int estridor) {
		this.estridor = estridor;
	}




	public int getEtapaEnfermedad() {
		return etapaEnfermedad;
	}




	public void setEtapaEnfermedad(int etapaEnfermedad) {
		this.etapaEnfermedad = etapaEnfermedad;
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




	public String getFechaOperacionBarrido() {
		return fechaOperacionBarrido;
	}




	public void setFechaOperacionBarrido(String fechaOperacionBarrido) {
		this.fechaOperacionBarrido = fechaOperacionBarrido;
	}




	public String getFechaUltimaDosis() {
		return fechaUltimaDosis;
	}




	public void setFechaUltimaDosis(String fechaUltimaDosis) {
		this.fechaUltimaDosis = fechaUltimaDosis;
	}




	public boolean isFichamodulo() {
		return fichamodulo;
	}




	public void setFichamodulo(boolean fichamodulo) {
		this.fichamodulo = fichamodulo;
	}




	public int getFiebre() {
		return fiebre;
	}




	public void setFiebre(int fiebre) {
		this.fiebre = fiebre;
	}




	public String getGenero() {
		return genero;
	}




	public void setGenero(String genero) {
		this.genero = genero;
	}




	public String getGrupoPoblacional() {
		return grupoPoblacional;
	}




	public void setGrupoPoblacional(String grupoPoblacional) {
		this.grupoPoblacional = grupoPoblacional;
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




	public boolean isHospitalizado() {
		return hospitalizado;
	}




	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
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




	public int getIdentificacionCaso() {
		return identificacionCaso;
	}




	public void setIdentificacionCaso(int identificacionCaso) {
		this.identificacionCaso = identificacionCaso;
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




	public int getInstitucionAtendio() {
		return institucionAtendio;
	}




	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
	}




	public int getInvestigacionCampo() {
		return investigacionCampo;
	}




	public void setInvestigacionCampo(int investigacionCampo) {
		this.investigacionCampo = investigacionCampo;
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




	public String getMunicipioNotifica() {
		return municipioNotifica;
	}




	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}




	public String getMunicipiosVacunados() {
		return municipiosVacunados;
	}




	public void setMunicipiosVacunados(String municipiosVacunados) {
		this.municipiosVacunados = municipiosVacunados;
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




	public int getNumero() {
		return numero;
	}




	public void setNumero(int numero) {
		this.numero = numero;
	}




	public String getNumeroContactos() {
		return numeroContactos;
	}




	public void setNumeroContactos(String numeroContactos) {
		this.numeroContactos = numeroContactos;
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




	public String getPais() {
		return pais;
	}




	public void setPais(String pais) {
		this.pais = pais;
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




	public int getQuimioprofilaxis() {
		return quimioprofilaxis;
	}




	public void setQuimioprofilaxis(int quimioprofilaxis) {
		this.quimioprofilaxis = quimioprofilaxis;
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




	public String getSegundoNombre() {
		return segundoNombre;
	}




	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}




	public int getSemanaEpidemiologica() {
		return semanaEpidemiologica;
	}




	public void setSemanaEpidemiologica(int semanaEpidemiologica) {
		this.semanaEpidemiologica = semanaEpidemiologica;
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




	public String getTipoAntibiotico() {
		return tipoAntibiotico;
	}




	public void setTipoAntibiotico(String tipoAntibiotico) {
		this.tipoAntibiotico = tipoAntibiotico;
	}




	public int getTipoCaso() {
		return tipoCaso;
	}




	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}




	public int getTipoComplicacion() {
		return tipoComplicacion;
	}




	public void setTipoComplicacion(int tipoComplicacion) {
		this.tipoComplicacion = tipoComplicacion;
	}




	public int getTipoDiagnostico() {
		return tipoDiagnostico;
	}




	public void setTipoDiagnostico(int tipoDiagnostico) {
		this.tipoDiagnostico = tipoDiagnostico;
	}




	public String getTipoId() {
		return tipoId;
	}




	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}




	public int getTipoVacuna() {
		return tipoVacuna;
	}




	public void setTipoVacuna(int tipoVacuna) {
		this.tipoVacuna = tipoVacuna;
	}




	public int getTos() {
		return tos;
	}




	public void setTos(int tos) {
		this.tos = tos;
	}




	public int getTosParoxistica() {
		return tosParoxistica;
	}




	public void setTosParoxistica(int tosParoxistica) {
		this.tosParoxistica = tosParoxistica;
	}




	public int getTotalPoblacion1() {
		return totalPoblacion1;
	}




	public void setTotalPoblacion1(int totalPoblacion1) {
		this.totalPoblacion1 = totalPoblacion1;
	}




	public int getTotalPoblacion2() {
		return totalPoblacion2;
	}




	public void setTotalPoblacion2(int totalPoblacion2) {
		this.totalPoblacion2 = totalPoblacion2;
	}




	public int getTotalPoblacion3() {
		return totalPoblacion3;
	}




	public void setTotalPoblacion3(int totalPoblacion3) {
		this.totalPoblacion3 = totalPoblacion3;
	}




	public int getTotalPoblacion4() {
		return totalPoblacion4;
	}




	public void setTotalPoblacion4(int totalPoblacion4) {
		this.totalPoblacion4 = totalPoblacion4;
	}




	public int getTratamientoAntibiotico() {
		return tratamientoAntibiotico;
	}




	public void setTratamientoAntibiotico(int tratamientoAntibiotico) {
		this.tratamientoAntibiotico = tratamientoAntibiotico;
	}




	public String getUrlFichasAnteriores() {
		return urlFichasAnteriores;
	}




	public void setUrlFichasAnteriores(String urlFichasAnteriores) {
		this.urlFichasAnteriores = urlFichasAnteriores;
	}




	public String getValorDiagnostico() {
		return valorDiagnostico;
	}




	public void setValorDiagnostico(String valorDiagnostico) {
		this.valorDiagnostico = valorDiagnostico;
	}




	public String getValorDivDx() {
		return valorDivDx;
	}




	public void setValorDivDx(String valorDivDx) {
		this.valorDivDx = valorDivDx;
	}




	public boolean isVieneDeFichasAnteriores() {
		return vieneDeFichasAnteriores;
	}




	public void setVieneDeFichasAnteriores(boolean vieneDeFichasAnteriores) {
		this.vieneDeFichasAnteriores = vieneDeFichasAnteriores;
	}




	public int getVomitoPostusivo() {
		return vomitoPostusivo;
	}




	public void setVomitoPostusivo(int vomitoPostusivo) {
		this.vomitoPostusivo = vomitoPostusivo;
	}




	public String getZonaDomicilio() {
		return zonaDomicilio;
	}




	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}




	public String getFechaInvestigacion() {
		return fechaInvestigacion;
	}




	public void setFechaInvestigacion(String fechaInvestigacion) {
		this.fechaInvestigacion = fechaInvestigacion;
	}




	public String getNombrePadre() {
		return nombrePadre;
	}




	public void setNombrePadre(String nombrePadre) {
		this.nombrePadre = nombrePadre;
	}




	public int getTotalDpt1() {
		return totalDpt1;
	}




	public void setTotalDpt1(int totalDpt1) {
		this.totalDpt1 = totalDpt1;
	}




	public int getTotalDpt2() {
		return totalDpt2;
	}




	public void setTotalDpt2(int totalDpt2) {
		this.totalDpt2 = totalDpt2;
	}




	public int getTotalDpt3() {
		return totalDpt3;
	}




	public void setTotalDpt3(int totalDpt3) {
		this.totalDpt3 = totalDpt3;
	}




	public int getTotalPoblacion() {
		return totalPoblacion;
	}




	public void setTotalPoblacion(int totalPoblacion) {
		this.totalPoblacion = totalPoblacion;
	}




	public int getTotalRef1() {
		return totalRef1;
	}




	public void setTotalRef1(int totalRef1) {
		this.totalRef1 = totalRef1;
	}




	public int getTotalRef2() {
		return totalRef2;
	}




	public void setTotalRef2(int totalRef2) {
		this.totalRef2 = totalRef2;
	}




	public int getTotalTotal() {
		return totalTotal;
	}




	public void setTotalTotal(int totalTotal) {
		this.totalTotal = totalTotal;
	}




	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}




	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
	}




	public int getTotalGrupo1() {
		return totalGrupo1;
	}




	public void setTotalGrupo1(int totalGrupo1) {
		this.totalGrupo1 = totalGrupo1;
	}




	public int getTotalGrupo2() {
		return totalGrupo2;
	}




	public void setTotalGrupo2(int totalGrupo2) {
		this.totalGrupo2 = totalGrupo2;
	}




	public int getTotalGrupo3() {
		return totalGrupo3;
	}




	public void setTotalGrupo3(int totalGrupo3) {
		this.totalGrupo3 = totalGrupo3;
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
