package com.sysmedica.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class FichaIntoxicacionesForm extends ValidatorForm {

private transient Logger logger=Logger.getLogger(FichaGenericaForm.class);
	
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
    
    private int codigoFichaIntoxicaciones;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
    private int tipoIntoxicacion;
    private String nombreProducto;
    private int tipoExposicion;
    private int produccion;
    private int almacenamiento;
    private int agricola;
    private int saludPublica;
    private int domiciliaria;
    private int tratHumano;
    private int tratVeterinario;
    private int transporte;
    private int mezcla;
    private int mantenimiento;
    private int cultivo;
    private int otros;
    private String otraActividad;
    private String fechaExposicion;
    private int horaExposicion;
    private int viaExposicion;
    private String otraViaExposicion;
    private int escolaridad;
    private int embarazada;
    private int vinculoLaboral;
    private int afiliadoArp;
    private String nombreArp;
    private int codgoArp;
    private int estCivil;
    private int alerta;
    private int investigacion;
    private String fechaInvestigacion;
    private String fechaInforma;
    private String nombreResponsable;
    private String telefonoResponsable;
    private String observaciones;
    
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
    	
    	tipoIntoxicacion = 0;
        nombreProducto = "";
        tipoExposicion = 0;
        produccion = 0;
        almacenamiento = 0;
        agricola = 0;
        saludPublica = 0;
        domiciliaria = 0;
        tratHumano = 0;
        tratVeterinario = 0;
        transporte = 0;
        mezcla = 0;
        mantenimiento = 0;
        cultivo = 0;
        otros = 0;
        otraActividad = "";
        fechaExposicion = "";
        horaExposicion = 0;
        viaExposicion = 0;
        otraViaExposicion = "";
        escolaridad = 0;
        embarazada = 0;
        vinculoLaboral = 0;
        afiliadoArp = 0;
        nombreArp = "";
        codgoArp = 0;
        estCivil = 0;
        alerta = 0;
        investigacion = 0;
        fechaInvestigacion = "";
        fechaInforma = "";
        nombreResponsable = "";
        telefonoResponsable = "";
        observaciones = "";
        
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
        	
        	String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
            String fechaExposicionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaExposicion);
        	
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
        	
        	
        	if (fechaExposicion.trim().length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaExposicion)) {
	                
	                errores.add("Campo Fecha de Inicio de Exposicion no valido", new ActionMessage("errors.formatoFechaInvalido","de Exposicion"));
	            }
	            else if (fechaExposicionTransformada.compareTo(fechaInicioSintomasTransformada)>0) {
					
					errores.add("Campo Fecha de Inicio de Exposicion no valido", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia","de Exposición", "de Inicio de Sintomas"));
				}
            }
            else {
            	errores.add("El campo Fecha de Exposición es Requerido", new ActionMessage("errors.required","El campo fecha de Exposición"));
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
            String fechaExposicionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaExposicion);
        	
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
        	
        	
        	if (fechaExposicion.trim().length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaExposicion)) {
	                
	                errores.add("Campo Fecha de Inicio de Exposicion no valido", new ActionMessage("errors.formatoFechaInvalido","de Exposicion"));
	            }
	            else if (fechaExposicionTransformada.compareTo(fechaInicioSintomasTransformada)>0) {
					
					errores.add("Campo Fecha de Inicio de Exposicion no valido", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia","de Exposición", "de Inicio de Sintomas"));
				}
            }
            else {
            	errores.add("El campo Fecha de Exposición es Requerido", new ActionMessage("errors.required","El campo fecha de Exposición"));
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
	public int getAgricola() {
		return agricola;
	}
	public void setAgricola(int agricola) {
		this.agricola = agricola;
	}
	public int getAlerta() {
		return alerta;
	}
	public void setAlerta(int alerta) {
		this.alerta = alerta;
	}
	public int getAlmacenamiento() {
		return almacenamiento;
	}
	public void setAlmacenamiento(int almacenamiento) {
		this.almacenamiento = almacenamiento;
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
	public int getCodigoFichaIntoxicaciones() {
		return codigoFichaIntoxicaciones;
	}
	public void setCodigoFichaIntoxicaciones(int codigoFichaIntoxicaciones) {
		this.codigoFichaIntoxicaciones = codigoFichaIntoxicaciones;
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
	public int getCultivo() {
		return cultivo;
	}
	public void setCultivo(int cultivo) {
		this.cultivo = cultivo;
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
	public int getDomiciliaria() {
		return domiciliaria;
	}
	public void setDomiciliaria(int domiciliaria) {
		this.domiciliaria = domiciliaria;
	}
	public String getEdad() {
		return edad;
	}
	public void setEdad(String edad) {
		this.edad = edad;
	}
	public int getEmbarazada() {
		return embarazada;
	}
	public void setEmbarazada(int embarazada) {
		this.embarazada = embarazada;
	}
	public boolean isEpidemiologia() {
		return epidemiologia;
	}
	public void setEpidemiologia(boolean epidemiologia) {
		this.epidemiologia = epidemiologia;
	}
	public int getEscolaridad() {
		return escolaridad;
	}
	public void setEscolaridad(int escolaridad) {
		this.escolaridad = escolaridad;
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
	public String getFechaExposicion() {
		return fechaExposicion;
	}
	public void setFechaExposicion(String fechaExposicion) {
		this.fechaExposicion = fechaExposicion;
	}
	public String getFechaHospitalizacion() {
		return fechaHospitalizacion;
	}
	public void setFechaHospitalizacion(String fechaHospitalizacion) {
		this.fechaHospitalizacion = fechaHospitalizacion;
	}
	public String getFechaInforma() {
		return fechaInforma;
	}
	public void setFechaInforma(String fechaInforma) {
		this.fechaInforma = fechaInforma;
	}
	public String getFechaInicioSint() {
		return fechaInicioSint;
	}
	public void setFechaInicioSint(String fechaInicioSint) {
		this.fechaInicioSint = fechaInicioSint;
	}
	public String getFechaInvestigacion() {
		return fechaInvestigacion;
	}
	public void setFechaInvestigacion(String fechaInvestigacion) {
		this.fechaInvestigacion = fechaInvestigacion;
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
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
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
	public int getHoraExposicion() {
		return horaExposicion;
	}
	public void setHoraExposicion(int horaExposicion) {
		this.horaExposicion = horaExposicion;
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
	public int getInvestigacion() {
		return investigacion;
	}
	public void setInvestigacion(int investigacion) {
		this.investigacion = investigacion;
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
	public int getMantenimiento() {
		return mantenimiento;
	}
	public void setMantenimiento(int mantenimiento) {
		this.mantenimiento = mantenimiento;
	}
	public int getMezcla() {
		return mezcla;
	}
	public void setMezcla(int mezcla) {
		this.mezcla = mezcla;
	}
	public String getMunicipioNotifica() {
		return municipioNotifica;
	}
	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}
	public String getNombreProducto() {
		return nombreProducto;
	}
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}
	public String getNombreProfesional() {
		return nombreProfesional;
	}
	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}
	public String getNombreResponsable() {
		return nombreResponsable;
	}
	public void setNombreResponsable(String nombreResponsable) {
		this.nombreResponsable = nombreResponsable;
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
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getOcupacion() {
		return ocupacion;
	}
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}
	public String getOtraActividad() {
		return otraActividad;
	}
	public void setOtraActividad(String otraActividad) {
		this.otraActividad = otraActividad;
	}
	public String getOtraViaExposicion() {
		return otraViaExposicion;
	}
	public void setOtraViaExposicion(String otraViaExposicion) {
		this.otraViaExposicion = otraViaExposicion;
	}
	public int getOtros() {
		return otros;
	}
	public void setOtros(int otros) {
		this.otros = otros;
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
	public int getProduccion() {
		return produccion;
	}
	public void setProduccion(int produccion) {
		this.produccion = produccion;
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
	public int getSaludPublica() {
		return saludPublica;
	}
	public void setSaludPublica(int saludPublica) {
		this.saludPublica = saludPublica;
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
	public String getTelefonoResponsable() {
		return telefonoResponsable;
	}
	public void setTelefonoResponsable(String telefonoResponsable) {
		this.telefonoResponsable = telefonoResponsable;
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
	public int getTipoExposicion() {
		return tipoExposicion;
	}
	public void setTipoExposicion(int tipoExposicion) {
		this.tipoExposicion = tipoExposicion;
	}
	public String getTipoId() {
		return tipoId;
	}
	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}
	public int getTipoIntoxicacion() {
		return tipoIntoxicacion;
	}
	public void setTipoIntoxicacion(int tipoIntoxicacion) {
		this.tipoIntoxicacion = tipoIntoxicacion;
	}
	public int getTransporte() {
		return transporte;
	}
	public void setTransporte(int transporte) {
		this.transporte = transporte;
	}
	public int getTratHumano() {
		return tratHumano;
	}
	public void setTratHumano(int tratHumano) {
		this.tratHumano = tratHumano;
	}
	public int getTratVeterinario() {
		return tratVeterinario;
	}
	public void setTratVeterinario(int tratVeterinario) {
		this.tratVeterinario = tratVeterinario;
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
	public int getViaExposicion() {
		return viaExposicion;
	}
	public void setViaExposicion(int viaExposicion) {
		this.viaExposicion = viaExposicion;
	}
	public String getZonaDomicilio() {
		return zonaDomicilio;
	}
	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
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



	public String getEstado() {
		return estado;
	}



	public void setEstado(String estado) {
		this.estado = estado;
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



	public String getUrlFichasAnteriores() {
		return urlFichasAnteriores;
	}



	public void setUrlFichasAnteriores(String urlFichasAnteriores) {
		this.urlFichasAnteriores = urlFichasAnteriores;
	}



	public int getAfiliadoArp() {
		return afiliadoArp;
	}



	public void setAfiliadoArp(int afiliadoArp) {
		this.afiliadoArp = afiliadoArp;
	}



	public int getCodgoArp() {
		return codgoArp;
	}



	public void setCodgoArp(int codgoArp) {
		this.codgoArp = codgoArp;
	}



	public int getEstCivil() {
		return estCivil;
	}



	public void setEstCivil(int estCivil) {
		this.estCivil = estCivil;
	}



	public String getNombreArp() {
		return nombreArp;
	}



	public void setNombreArp(String nombreArp) {
		this.nombreArp = nombreArp;
	}



	public int getVinculoLaboral() {
		return vinculoLaboral;
	}



	public void setVinculoLaboral(int vinculoLaboral) {
		this.vinculoLaboral = vinculoLaboral;
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
