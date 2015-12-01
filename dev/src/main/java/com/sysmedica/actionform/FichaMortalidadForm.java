package com.sysmedica.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class FichaMortalidadForm extends ValidatorForm {

	private boolean activa;
	
	private boolean hayServicios;
    private boolean hayLaboratorios;
    
    private int codigoInstitucion;
    
	private boolean notificar;
    private String loginUsuario;
    private int codigoFichaMortalidad;
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
    
    private int sitioDefuncion;
	private String descripcionSitio;
	private int convivencia;
	private String otroConvivencia;
	private int escolaridad;
	private int fecundidad;
	private String gestaciones;
	private String partos;
	private String cesareas;
	private String abortos;
	private String sustanciasSico;
	private String trastornoMental;
	private String infecciones;
	private String factoresRiesgo;
	private int controlPrenatal;
	private String cuantosControles;
	private int trimInicio;
	private int controlesRealizadosPor;
	private int nivelAtencion;
	private int clasificacionRiesgo;
	private int remisionesOportunas;
	private String complicacionesAntecedentes;
	private HashMap complicaciones;
	private int momentoFallecimiento;
	private String semanasGestacion;
	private int tipoParto;
	private int atendidoPor;
	private int nivelAtencion2;
	private int momentoMuerteRelacion;
	private String edadGestacional;
	private String pesoNacimiento;
	private String tallaNacimiento;
	private int sexo;
	private String apgarNacimiento1;
	private String apgarNacimiento5;
	private String apgarNacimiento15;
	private int nivelAtencion3;
	private int adaptacionNeonatal;
	private String causaDirectaDefuncion;
	private String causaBasicaDefuncion;
	private int muerteDemora;
	private int causaMuerteDet;
	private String muertos;
	private String vivos;
	private int semanaInicioCpn;
	private int quienClasificoRiesgo;
	private int remisionOportunaComplica;
	private String muerteDemora1;
	private String muerteDemora2;
	private String muerteDemora3;
	private String muerteDemora4;
	private HashMap antecedentes;
	
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
	
	
	public void reset()
    {
    	sire = "";
        municipioNotifica = "";
        departamentoNotifica = "";
        lugarNotifica = "";
        institucionAtendio = 0;
        fechaNotificacion = "";
        
        fechaConsultaGeneral = "";
        codigoMunProcedencia = "";
        codigoDepProcedencia = "";
        lugarProcedencia = "";
        fechaInicioSint = "";
        tipoCaso = 0;
        hospitalizado = false;
        fechaHospitalizacion = "";
        estaVivo = true;
        fechaDefuncion = "";
        nombreProfesional = "";
        
        sitioDefuncion = 0;
    	descripcionSitio = "";
    	convivencia = 0;
    	otroConvivencia = "";
    	escolaridad = 0;
    	fecundidad = 0;
    	gestaciones = "";
    	partos = "";
    	cesareas = "";
    	abortos = "";
    	sustanciasSico = "";
    	trastornoMental = "";
    	infecciones = "";
    	factoresRiesgo = "";
    	controlPrenatal = 0;
    	cuantosControles = "";
    	trimInicio = 0;
    	controlesRealizadosPor = 0;
    	nivelAtencion = 0;
    	clasificacionRiesgo = 0;
    	remisionesOportunas = 0;
    	complicaciones = new HashMap();
    	momentoFallecimiento = 0;
    	semanasGestacion = "";
    	tipoParto = 0;
    	atendidoPor = 0;
    	nivelAtencion2 = 0;
    	momentoMuerteRelacion = 0;
    	edadGestacional = "";
    	pesoNacimiento = "";
    	tallaNacimiento = "";
    	sexo = 0;
    	apgarNacimiento1 = "";
    	apgarNacimiento5 = "";
    	apgarNacimiento15 = "";
    	nivelAtencion3 = 0;
    	adaptacionNeonatal = 0;
    	causaDirectaDefuncion = "";
    	causaBasicaDefuncion = "";
    	muerteDemora = 0;
    	causaMuerteDet = 0;
    	
    	complicacionesAntecedentes = "";
    	
    	estaVivo = true;
    	
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
        fechaDefuncion = "";
        nombreProfesional = "";
        
        muertos = "";
    	vivos = "";
    	semanaInicioCpn = 0;
    	quienClasificoRiesgo = 0;
    	remisionOportunaComplica = 0;
    	muerteDemora1 = "";
    	muerteDemora2 = "";
    	muerteDemora3 = "";
    	muerteDemora4 = "";
    	antecedentes = new HashMap();
    	
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
        	
        	String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
            boolean hayComplicaciones = false;
        	
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
            else {
            	errores.add("El campo Fecha de Defuncion es Requerido", new ActionMessage("errors.required","El campo fecha de Defuncion"));
            }
            
            try {
	        	for (int i=1;i<complicaciones.size()+1;i++) {
	        		
	        		String val = complicaciones.get("complicacion_"+i).toString();
	        		
	        		if (val.equals("true")) {
	        			
	        			hayComplicaciones=true;
	        			break;
	        		}
	        	}
	        	
	        	if (!hayComplicaciones) {
	        		
	        		errores.add("No se agregaron complicaciones del embarazo", new ActionMessage("error.epidemiologia.complicaembarazo"));
	        	}
        	}
        	catch (NullPointerException npe) {}
        }
        if (estado.equals("validar")) {
        	
        	String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
            boolean hayComplicaciones = false;
        	
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
            else {
            	errores.add("El campo Fecha de Defuncion es Requerido", new ActionMessage("errors.required","El campo fecha de Defuncion"));
            }
            
            
            try {
	        	for (int i=1;i<complicaciones.size()+1;i++) {
	        		
	        		String val = complicaciones.get("complicacion_"+i).toString();
	        		
	        		if (val.equals("true")) {
	        			
	        			hayComplicaciones=true;
	        			break;
	        		}
	        	}
	        	
	        	if (!hayComplicaciones) {
	        		
	        		errores.add("No se agregaron complicaciones del embarazo", new ActionMessage("error.epidemiologia.complicaembarazo"));
	        	}
        	}
        	catch (NullPointerException npe) {}
        }

        return errores;
    }


	public String getAbortos() {
		return abortos;
	}


	public void setAbortos(String abortos) {
		this.abortos = abortos;
	}


	public int getAdaptacionNeonatal() {
		return adaptacionNeonatal;
	}


	public void setAdaptacionNeonatal(int adaptacionNeonatal) {
		this.adaptacionNeonatal = adaptacionNeonatal;
	}


	public String getApgarNacimiento1() {
		return apgarNacimiento1;
	}


	public void setApgarNacimiento1(String apgarNacimiento1) {
		this.apgarNacimiento1 = apgarNacimiento1;
	}


	public String getApgarNacimiento15() {
		return apgarNacimiento15;
	}


	public void setApgarNacimiento15(String apgarNacimiento15) {
		this.apgarNacimiento15 = apgarNacimiento15;
	}


	public String getApgarNacimiento5() {
		return apgarNacimiento5;
	}


	public void setApgarNacimiento5(String apgarNacimiento5) {
		this.apgarNacimiento5 = apgarNacimiento5;
	}


	public String getAseguradora() {
		return aseguradora;
	}


	public void setAseguradora(String aseguradora) {
		this.aseguradora = aseguradora;
	}


	public int getAtendidoPor() {
		return atendidoPor;
	}


	public void setAtendidoPor(int atendidoPor) {
		this.atendidoPor = atendidoPor;
	}


	public String getBarrioResidencia() {
		return barrioResidencia;
	}


	public void setBarrioResidencia(String barrioResidencia) {
		this.barrioResidencia = barrioResidencia;
	}


	public String getCausaBasicaDefuncion() {
		return causaBasicaDefuncion;
	}


	public void setCausaBasicaDefuncion(String causaBasicaDefuncion) {
		this.causaBasicaDefuncion = causaBasicaDefuncion;
	}


	public String getCausaDirectaDefuncion() {
		return causaDirectaDefuncion;
	}


	public void setCausaDirectaDefuncion(String causaDirectaDefuncion) {
		this.causaDirectaDefuncion = causaDirectaDefuncion;
	}


	public int getCausaMuerteDet() {
		return causaMuerteDet;
	}


	public void setCausaMuerteDet(int causaMuerteDet) {
		this.causaMuerteDet = causaMuerteDet;
	}


	public String getCesareas() {
		return cesareas;
	}


	public void setCesareas(String cesareas) {
		this.cesareas = cesareas;
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


	public int getClasificacionRiesgo() {
		return clasificacionRiesgo;
	}


	public void setClasificacionRiesgo(int clasificacionRiesgo) {
		this.clasificacionRiesgo = clasificacionRiesgo;
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


	public int getCodigoFichaMortalidad() {
		return codigoFichaMortalidad;
	}


	public void setCodigoFichaMortalidad(int codigoFichaMortalidad) {
		this.codigoFichaMortalidad = codigoFichaMortalidad;
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


	public HashMap getComplicaciones() {
		return complicaciones;
	}


	public void setComplicaciones(HashMap complicaciones) {
		this.complicaciones = complicaciones;
	}


	public int getControlesRealizadosPor() {
		return controlesRealizadosPor;
	}


	public void setControlesRealizadosPor(int controlesRealizadosPor) {
		this.controlesRealizadosPor = controlesRealizadosPor;
	}


	public int getControlPrenatal() {
		return controlPrenatal;
	}


	public void setControlPrenatal(int controlPrenatal) {
		this.controlPrenatal = controlPrenatal;
	}


	public int getConvivencia() {
		return convivencia;
	}


	public void setConvivencia(int convivencia) {
		this.convivencia = convivencia;
	}


	public String getCuantosControles() {
		return cuantosControles;
	}


	public void setCuantosControles(String cuantosControles) {
		this.cuantosControles = cuantosControles;
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


	public String getDescripcionSitio() {
		return descripcionSitio;
	}


	public void setDescripcionSitio(String descripcionSitio) {
		this.descripcionSitio = descripcionSitio;
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


	public String getEdad() {
		return edad;
	}


	public void setEdad(String edad) {
		this.edad = edad;
	}


	public String getEdadGestacional() {
		return edadGestacional;
	}


	public void setEdadGestacional(String edadGestacional) {
		this.edadGestacional = edadGestacional;
	}


	public int getEscolaridad() {
		return escolaridad;
	}


	public void setEscolaridad(int escolaridad) {
		this.escolaridad = escolaridad;
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


	public String getFactoresRiesgo() {
		return factoresRiesgo;
	}


	public void setFactoresRiesgo(String factoresRiesgo) {
		this.factoresRiesgo = factoresRiesgo;
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


	public int getFecundidad() {
		return fecundidad;
	}


	public void setFecundidad(int fecundidad) {
		this.fecundidad = fecundidad;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public String getGestaciones() {
		return gestaciones;
	}


	public void setGestaciones(String gestaciones) {
		this.gestaciones = gestaciones;
	}


	public boolean isHospitalizado() {
		return hospitalizado;
	}


	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}


	public String getInfecciones() {
		return infecciones;
	}


	public void setInfecciones(String infecciones) {
		this.infecciones = infecciones;
	}


	public int getInstitucionAtendio() {
		return institucionAtendio;
	}


	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
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


	public int getMomentoFallecimiento() {
		return momentoFallecimiento;
	}


	public void setMomentoFallecimiento(int momentoFallecimiento) {
		this.momentoFallecimiento = momentoFallecimiento;
	}


	public int getMomentoMuerteRelacion() {
		return momentoMuerteRelacion;
	}


	public void setMomentoMuerteRelacion(int momentoMuerteRelacion) {
		this.momentoMuerteRelacion = momentoMuerteRelacion;
	}


	public int getMuerteDemora() {
		return muerteDemora;
	}


	public void setMuerteDemora(int muerteDemora) {
		this.muerteDemora = muerteDemora;
	}


	public String getMunicipioNotifica() {
		return municipioNotifica;
	}


	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}


	public int getNivelAtencion() {
		return nivelAtencion;
	}


	public void setNivelAtencion(int nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}


	public int getNivelAtencion2() {
		return nivelAtencion2;
	}


	public void setNivelAtencion2(int nivelAtencion2) {
		this.nivelAtencion2 = nivelAtencion2;
	}


	public int getNivelAtencion3() {
		return nivelAtencion3;
	}


	public void setNivelAtencion3(int nivelAtencion3) {
		this.nivelAtencion3 = nivelAtencion3;
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


	public String getOtroConvivencia() {
		return otroConvivencia;
	}


	public void setOtroConvivencia(String otroConvivencia) {
		this.otroConvivencia = otroConvivencia;
	}


	public String getPartos() {
		return partos;
	}


	public void setPartos(String partos) {
		this.partos = partos;
	}


	public String getPesoNacimiento() {
		return pesoNacimiento;
	}


	public void setPesoNacimiento(String pesoNacimiento) {
		this.pesoNacimiento = pesoNacimiento;
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


	public String getRegimenSalud() {
		return regimenSalud;
	}


	public void setRegimenSalud(String regimenSalud) {
		this.regimenSalud = regimenSalud;
	}


	public int getRemisionesOportunas() {
		return remisionesOportunas;
	}


	public void setRemisionesOportunas(int remisionesOportunas) {
		this.remisionesOportunas = remisionesOportunas;
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


	public String getSemanasGestacion() {
		return semanasGestacion;
	}


	public void setSemanasGestacion(String semanasGestacion) {
		this.semanasGestacion = semanasGestacion;
	}


	public int getSexo() {
		return sexo;
	}


	public void setSexo(int sexo) {
		this.sexo = sexo;
	}


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
	}


	public int getSitioDefuncion() {
		return sitioDefuncion;
	}


	public void setSitioDefuncion(int sitioDefuncion) {
		this.sitioDefuncion = sitioDefuncion;
	}


	public String getSustanciasSico() {
		return sustanciasSico;
	}


	public void setSustanciasSico(String sustanciasSico) {
		this.sustanciasSico = sustanciasSico;
	}


	public String getTallaNacimiento() {
		return tallaNacimiento;
	}


	public void setTallaNacimiento(String tallaNacimiento) {
		this.tallaNacimiento = tallaNacimiento;
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


	public int getTipoParto() {
		return tipoParto;
	}


	public void setTipoParto(int tipoParto) {
		this.tipoParto = tipoParto;
	}


	public String getTrastornoMental() {
		return trastornoMental;
	}


	public void setTrastornoMental(String trastornoMental) {
		this.trastornoMental = trastornoMental;
	}


	public int getTrimInicio() {
		return trimInicio;
	}


	public void setTrimInicio(int trimInicio) {
		this.trimInicio = trimInicio;
	}


	public String getZonaDomicilio() {
		return zonaDomicilio;
	}


	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}


	public String getComplicacionesAntecedentes() {
		return complicacionesAntecedentes;
	}


	public void setComplicacionesAntecedentes(String complicacionesAntecedentes) {
		this.complicacionesAntecedentes = complicacionesAntecedentes;
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


	public String getMuerteDemora1() {
		return muerteDemora1;
	}


	public void setMuerteDemora1(String muerteDemora1) {
		this.muerteDemora1 = muerteDemora1;
	}


	public String getMuerteDemora2() {
		return muerteDemora2;
	}


	public void setMuerteDemora2(String muerteDemora2) {
		this.muerteDemora2 = muerteDemora2;
	}


	public String getMuerteDemora3() {
		return muerteDemora3;
	}


	public void setMuerteDemora3(String muerteDemora3) {
		this.muerteDemora3 = muerteDemora3;
	}


	public String getMuerteDemora4() {
		return muerteDemora4;
	}


	public void setMuerteDemora4(String muerteDemora4) {
		this.muerteDemora4 = muerteDemora4;
	}


	public String getMuertos() {
		return muertos;
	}


	public void setMuertos(String muertos) {
		this.muertos = muertos;
	}


	public int getQuienClasificoRiesgo() {
		return quienClasificoRiesgo;
	}


	public void setQuienClasificoRiesgo(int quienClasificoRiesgo) {
		this.quienClasificoRiesgo = quienClasificoRiesgo;
	}


	public int getRemisionOportunaComplica() {
		return remisionOportunaComplica;
	}


	public void setRemisionOportunaComplica(int remisionOportunaComplica) {
		this.remisionOportunaComplica = remisionOportunaComplica;
	}


	public int getSemanaInicioCpn() {
		return semanaInicioCpn;
	}


	public void setSemanaInicioCpn(int semanaInicioCpn) {
		this.semanaInicioCpn = semanaInicioCpn;
	}


	public String getVivos() {
		return vivos;
	}


	public void setVivos(String vivos) {
		this.vivos = vivos;
	}


	public HashMap getAntecedentes() {
		return antecedentes;
	}


	public void setAntecedentes(HashMap antecedentes) {
		this.antecedentes = antecedentes;
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
