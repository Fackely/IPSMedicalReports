package com.sysmedica.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class FichaAcciOfidicoForm extends ValidatorForm {
	
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
    private int codigoFichaGenerica;
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
    
    private int codigoFichaAcciOfidico;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
    private String fechaAccidente;
	private String nombreVereda;
	private int actividadAccidente;
	private String cualActividad;
	private int tipoAtencionInicial;
	private String cualAtencion;
	private int practicasNoMedicas;
	private String cualPractica;
	private int localizacionMordedura;
	private int huellasColmillos;
	private int serpienteIdentificada;
	private int serpienteCapturada;
	private int generoAgenteAgresor;
	private String cualAgente;
	private int nombreAgenteAgresor;
	private String cualLocal;
	private String cualComplicacion;
	private String cualSistemica;
	private int severidadAccidente;
	private int empleoSuero;
	private int diasTranscurridos;
	private int horasTranscurridas;
	private int tipoSueroAntiofidico;
	private String cualSuero;
	private int dosisSuero;
	private int horasSuero;
	private int minutosSuero;
	private int tratamientoQuirurgico;
	private int tipoTratamiento;
	
	private HashMap manifestacionesLocales;
	private HashMap manifestacionesSistemicas;
	private HashMap complicacionesLocales;
	private HashMap complicacionesSistemicas;
    
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
    	
    	fechaAccidente = "";
    	nombreVereda = "";
    	actividadAccidente = 0;
    	cualActividad = "";
    	tipoAtencionInicial = 0;
    	cualAtencion = "";
    	practicasNoMedicas = 0;
    	cualPractica = "";
    	localizacionMordedura = 0;
    	huellasColmillos = 0;
    	serpienteIdentificada = 0;
    	serpienteCapturada = 0;
    	generoAgenteAgresor = 0;
    	cualAgente = "";
    	nombreAgenteAgresor = 0;
    	cualLocal = "";
    	cualComplicacion = "";
    	cualSistemica = "";
    	severidadAccidente = 0;
    	empleoSuero = 0;
    	diasTranscurridos = 0;
    	horasTranscurridas = 0;
    	tipoSueroAntiofidico = 0;
    	cualSuero = "";
    	dosisSuero = 0;
    	horasSuero = 0;
    	minutosSuero = 0;
    	tratamientoQuirurgico = 0;
    	tipoTratamiento = 0;
    	
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

    	manifestacionesLocales = new HashMap();
    	manifestacionesSistemicas = new HashMap();
    	complicacionesLocales = new HashMap();
    	complicacionesSistemicas = new HashMap();
        
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
            String fechaAccidenteTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaAccidente);
        	
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
        	
        	
        	
        	if (fechaAccidente.trim().length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaAccidente)) {
	                
	                errores.add("Campo Fecha de Accidente no valido", new ActionMessage("errors.formatoFechaInvalido","de Accidente"));
	            }
	            else if (fechaAccidenteTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Accidente no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Accidente", "actual"));
				}
	            else if (fechaAccidenteTransformada.compareTo(fechaInicioSintomasTransformada)>0) {
					
					errores.add("Campo Fecha de Accidente no valido", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia","de Accidente", "de Inicio de Sintomas"));
				}
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
	        	
            }
            catch (NullPointerException npe) {}
        }
        
        if (estado.equals("validar")) {
        	
        	String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
            String fechaAccidenteTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaAccidente);
        	
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
        	
        	

        	if (fechaAccidente.trim().length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaAccidente)) {
	                
	                errores.add("Campo Fecha de Accidente no valido", new ActionMessage("errors.formatoFechaInvalido","de Accidente"));
	            }
	            else if (fechaAccidenteTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Accidente no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Accidente", "actual"));
				}
	            else if (fechaAccidenteTransformada.compareTo(fechaInicioSintomasTransformada)>0) {
					
					errores.add("Campo Fecha de Accidente no valido", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia","de Accidente", "de Inicio de Sintomas"));
				}
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
	            
            }
            catch (NullPointerException npe) {}
        }
        
        return errores;
    }
    


	public boolean isActiva() {
		return activa;
	}


	public void setActiva(boolean activa) {
		this.activa = activa;
	}


	public int getActividadAccidente() {
		return actividadAccidente;
	}


	public void setActividadAccidente(int actividadAccidente) {
		this.actividadAccidente = actividadAccidente;
	}


	public int getAnyoSemanaEpi() {
		return anyoSemanaEpi;
	}


	public void setAnyoSemanaEpi(int anyoSemanaEpi) {
		this.anyoSemanaEpi = anyoSemanaEpi;
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


	public int getCodigoFichaGenerica() {
		return codigoFichaGenerica;
	}


	public void setCodigoFichaGenerica(int codigoFichaGenerica) {
		this.codigoFichaGenerica = codigoFichaGenerica;
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


	public String getCualActividad() {
		return cualActividad;
	}


	public void setCualActividad(String cualActividad) {
		this.cualActividad = cualActividad;
	}


	public String getCualAgente() {
		return cualAgente;
	}


	public void setCualAgente(String cualAgente) {
		this.cualAgente = cualAgente;
	}


	public String getCualAtencion() {
		return cualAtencion;
	}


	public void setCualAtencion(String cualAtencion) {
		this.cualAtencion = cualAtencion;
	}


	public String getCualComplicacion() {
		return cualComplicacion;
	}


	public void setCualComplicacion(String cualComplicacion) {
		this.cualComplicacion = cualComplicacion;
	}


	public String getCualLocal() {
		return cualLocal;
	}


	public void setCualLocal(String cualLocal) {
		this.cualLocal = cualLocal;
	}


	public String getCualPractica() {
		return cualPractica;
	}


	public void setCualPractica(String cualPractica) {
		this.cualPractica = cualPractica;
	}


	public String getCualSistemica() {
		return cualSistemica;
	}


	public void setCualSistemica(String cualSistemica) {
		this.cualSistemica = cualSistemica;
	}


	public String getCualSuero() {
		return cualSuero;
	}


	public void setCualSuero(String cualSuero) {
		this.cualSuero = cualSuero;
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


	public int getDiasTranscurridos() {
		return diasTranscurridos;
	}


	public void setDiasTranscurridos(int diasTranscurridos) {
		this.diasTranscurridos = diasTranscurridos;
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


	public int getDosisSuero() {
		return dosisSuero;
	}


	public void setDosisSuero(int dosisSuero) {
		this.dosisSuero = dosisSuero;
	}


	public String getEdad() {
		return edad;
	}


	public void setEdad(String edad) {
		this.edad = edad;
	}


	public int getEmpleoSuero() {
		return empleoSuero;
	}


	public void setEmpleoSuero(int empleoSuero) {
		this.empleoSuero = empleoSuero;
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


	public String getEtnia() {
		return etnia;
	}


	public void setEtnia(String etnia) {
		this.etnia = etnia;
	}


	public String getFechaAccidente() {
		return fechaAccidente;
	}


	public void setFechaAccidente(String fechaAccidente) {
		this.fechaAccidente = fechaAccidente;
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


	public boolean isFichamodulo() {
		return fichamodulo;
	}


	public void setFichamodulo(boolean fichamodulo) {
		this.fichamodulo = fichamodulo;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public int getGeneroAgenteAgresor() {
		return generoAgenteAgresor;
	}


	public void setGeneroAgenteAgresor(int generoAgenteAgresor) {
		this.generoAgenteAgresor = generoAgenteAgresor;
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


	public int getHorasSuero() {
		return horasSuero;
	}


	public void setHorasSuero(int horasSuero) {
		this.horasSuero = horasSuero;
	}


	public int getHorasTranscurridas() {
		return horasTranscurridas;
	}


	public void setHorasTranscurridas(int horasTranscurridas) {
		this.horasTranscurridas = horasTranscurridas;
	}


	public boolean isHospitalizado() {
		return hospitalizado;
	}


	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}


	public int getHuellasColmillos() {
		return huellasColmillos;
	}


	public void setHuellasColmillos(int huellasColmillos) {
		this.huellasColmillos = huellasColmillos;
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


	public int getInstitucionAtendio() {
		return institucionAtendio;
	}


	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
	}


	public int getLocalizacionMordedura() {
		return localizacionMordedura;
	}


	public void setLocalizacionMordedura(int localizacionMordedura) {
		this.localizacionMordedura = localizacionMordedura;
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


	public int getMinutosSuero() {
		return minutosSuero;
	}


	public void setMinutosSuero(int minutosSuero) {
		this.minutosSuero = minutosSuero;
	}


	public String getMunicipioNotifica() {
		return municipioNotifica;
	}


	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}


	public int getNombreAgenteAgresor() {
		return nombreAgenteAgresor;
	}


	public void setNombreAgenteAgresor(int nombreAgenteAgresor) {
		this.nombreAgenteAgresor = nombreAgenteAgresor;
	}


	public String getNombreProfesional() {
		return nombreProfesional;
	}


	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}


	public String getNombreVereda() {
		return nombreVereda;
	}


	public void setNombreVereda(String nombreVereda) {
		this.nombreVereda = nombreVereda;
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


	public int getPracticasNoMedicas() {
		return practicasNoMedicas;
	}


	public void setPracticasNoMedicas(int practicasNoMedicas) {
		this.practicasNoMedicas = practicasNoMedicas;
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


	public int getSerpienteCapturada() {
		return serpienteCapturada;
	}


	public void setSerpienteCapturada(int serpienteCapturada) {
		this.serpienteCapturada = serpienteCapturada;
	}


	public int getSerpienteIdentificada() {
		return serpienteIdentificada;
	}


	public void setSerpienteIdentificada(int serpienteIdentificada) {
		this.serpienteIdentificada = serpienteIdentificada;
	}


	public int getSeveridadAccidente() {
		return severidadAccidente;
	}


	public void setSeveridadAccidente(int severidadAccidente) {
		this.severidadAccidente = severidadAccidente;
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


	public int getTipoAtencionInicial() {
		return tipoAtencionInicial;
	}


	public void setTipoAtencionInicial(int tipoAtencionInicial) {
		this.tipoAtencionInicial = tipoAtencionInicial;
	}


	public int getTipoCaso() {
		return tipoCaso;
	}


	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
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


	public int getTipoSueroAntiofidico() {
		return tipoSueroAntiofidico;
	}


	public void setTipoSueroAntiofidico(int tipoSueroAntiofidico) {
		this.tipoSueroAntiofidico = tipoSueroAntiofidico;
	}


	public int getTipoTratamiento() {
		return tipoTratamiento;
	}


	public void setTipoTratamiento(int tipoTratamiento) {
		this.tipoTratamiento = tipoTratamiento;
	}


	public int getTratamientoQuirurgico() {
		return tratamientoQuirurgico;
	}


	public void setTratamientoQuirurgico(int tratamientoQuirurgico) {
		this.tratamientoQuirurgico = tratamientoQuirurgico;
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


	public String getZonaDomicilio() {
		return zonaDomicilio;
	}


	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}



	public int getCodigoFichaAcciOfidico() {
		return codigoFichaAcciOfidico;
	}



	public void setCodigoFichaAcciOfidico(int codigoFichaAcciOfidico) {
		this.codigoFichaAcciOfidico = codigoFichaAcciOfidico;
	}



	public HashMap getComplicacionesLocales() {
		return complicacionesLocales;
	}



	public void setComplicacionesLocales(HashMap complicacionesLocales) {
		this.complicacionesLocales = complicacionesLocales;
	}



	public HashMap getComplicacionesSistemicas() {
		return complicacionesSistemicas;
	}



	public void setComplicacionesSistemicas(HashMap complicacionesSistemicas) {
		this.complicacionesSistemicas = complicacionesSistemicas;
	}



	public HashMap getManifestacionesLocales() {
		return manifestacionesLocales;
	}



	public void setManifestacionesLocales(HashMap manifestacionesLocales) {
		this.manifestacionesLocales = manifestacionesLocales;
	}



	public HashMap getManifestacionesSistemicas() {
		return manifestacionesSistemicas;
	}



	public void setManifestacionesSistemicas(HashMap manifestacionesSistemicas) {
		this.manifestacionesSistemicas = manifestacionesSistemicas;
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
