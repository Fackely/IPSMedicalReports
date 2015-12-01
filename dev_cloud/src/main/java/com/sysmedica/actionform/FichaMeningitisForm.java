package com.sysmedica.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class FichaMeningitisForm extends ValidatorForm {


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
    
    private int codigoFichaMeningitis;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
    private int vacunaAntihib;
    private int dosis;
    private String fechaPrimeraDosis;
    private String fechaUltimaDosis;
    private int tieneCarne;
    private int vacunaAntimenin;
    private int dosis2;
    private String fechaPrimeraDosis2;
    private String fechaUltimaDosis2;
    private int tieneCarne2;
    private int vacunaAntineumo;
    private int dosis3;
    private String fechaPrimeraDosis3;
    private String fechaUltimaDosis3;
    private int tieneCarne3;
    private int fiebre;
    private int rigidez;
    private int irritacion;
    private int rash;
    private int abombamiento;
    private int alteracion;
    private int usoAntibioticos;
    private String fechaUltimaDosis4;
    private String observaciones;
    private String cualAntibiotico;
    
    private HashMap datosLaboratorio;
	

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
    	
    	vacunaAntihib = 0;
        dosis = 0;
        fechaPrimeraDosis = "";
        fechaUltimaDosis = "";
        tieneCarne = 0;
        vacunaAntimenin = 0;
        dosis2 = 0;
        fechaPrimeraDosis2 = "";
        fechaUltimaDosis2 = "";
        tieneCarne2 = 0;
        vacunaAntineumo = 0;
        dosis3 = 0;
        fechaPrimeraDosis3 = "";
        fechaUltimaDosis3 = "";
        tieneCarne3 = 0;
        fiebre = 0;
        rigidez = 0;
        irritacion = 0;
        rash = 0;
        abombamiento = 0;
        alteracion = 0;
        usoAntibioticos = 0;
        fechaUltimaDosis4 = "";
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
	        	
            }
            catch (NullPointerException npe) {}
        }
        
        if (estado.equals("validar")) {
        	
        	String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
        	
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
	            
            }
            catch (NullPointerException npe) {}
        }
        
        return errores;
    }




	public int getAbombamiento() {
		return abombamiento;
	}




	public void setAbombamiento(int abombamiento) {
		this.abombamiento = abombamiento;
	}




	public boolean isActiva() {
		return activa;
	}




	public void setActiva(boolean activa) {
		this.activa = activa;
	}




	public int getAlteracion() {
		return alteracion;
	}




	public void setAlteracion(int alteracion) {
		this.alteracion = alteracion;
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




	public int getCodigoFichaMeningitis() {
		return codigoFichaMeningitis;
	}




	public void setCodigoFichaMeningitis(int codigoFichaMeningitis) {
		this.codigoFichaMeningitis = codigoFichaMeningitis;
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




	public int getDosis() {
		return dosis;
	}




	public void setDosis(int dosis) {
		this.dosis = dosis;
	}




	public int getDosis2() {
		return dosis2;
	}




	public void setDosis2(int dosis2) {
		this.dosis2 = dosis2;
	}




	public int getDosis3() {
		return dosis3;
	}




	public void setDosis3(int dosis3) {
		this.dosis3 = dosis3;
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




	public String getFechaPrimeraDosis() {
		return fechaPrimeraDosis;
	}




	public void setFechaPrimeraDosis(String fechaPrimeraDosis) {
		this.fechaPrimeraDosis = fechaPrimeraDosis;
	}




	public String getFechaPrimeraDosis2() {
		return fechaPrimeraDosis2;
	}




	public void setFechaPrimeraDosis2(String fechaPrimeraDosis2) {
		this.fechaPrimeraDosis2 = fechaPrimeraDosis2;
	}




	public String getFechaPrimeraDosis3() {
		return fechaPrimeraDosis3;
	}




	public void setFechaPrimeraDosis3(String fechaPrimeraDosis3) {
		this.fechaPrimeraDosis3 = fechaPrimeraDosis3;
	}




	public String getFechaUltimaDosis() {
		return fechaUltimaDosis;
	}




	public void setFechaUltimaDosis(String fechaUltimaDosis) {
		this.fechaUltimaDosis = fechaUltimaDosis;
	}




	public String getFechaUltimaDosis2() {
		return fechaUltimaDosis2;
	}




	public void setFechaUltimaDosis2(String fechaUltimaDosis2) {
		this.fechaUltimaDosis2 = fechaUltimaDosis2;
	}




	public String getFechaUltimaDosis3() {
		return fechaUltimaDosis3;
	}




	public void setFechaUltimaDosis3(String fechaUltimaDosis3) {
		this.fechaUltimaDosis3 = fechaUltimaDosis3;
	}




	public String getFechaUltimaDosis4() {
		return fechaUltimaDosis4;
	}




	public void setFechaUltimaDosis4(String fechaUltimaDosis4) {
		this.fechaUltimaDosis4 = fechaUltimaDosis4;
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




	public int getIrritacion() {
		return irritacion;
	}




	public void setIrritacion(int irritacion) {
		this.irritacion = irritacion;
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




	public int getRash() {
		return rash;
	}




	public void setRash(int rash) {
		this.rash = rash;
	}




	public String getRegimenSalud() {
		return regimenSalud;
	}




	public void setRegimenSalud(String regimenSalud) {
		this.regimenSalud = regimenSalud;
	}




	public int getRigidez() {
		return rigidez;
	}




	public void setRigidez(int rigidez) {
		this.rigidez = rigidez;
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




	public int getTieneCarne() {
		return tieneCarne;
	}




	public void setTieneCarne(int tieneCarne) {
		this.tieneCarne = tieneCarne;
	}




	public int getTieneCarne2() {
		return tieneCarne2;
	}




	public void setTieneCarne2(int tieneCarne2) {
		this.tieneCarne2 = tieneCarne2;
	}




	public int getTieneCarne3() {
		return tieneCarne3;
	}




	public void setTieneCarne3(int tieneCarne3) {
		this.tieneCarne3 = tieneCarne3;
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




	public String getUrlFichasAnteriores() {
		return urlFichasAnteriores;
	}




	public void setUrlFichasAnteriores(String urlFichasAnteriores) {
		this.urlFichasAnteriores = urlFichasAnteriores;
	}




	public int getUsoAntibioticos() {
		return usoAntibioticos;
	}




	public void setUsoAntibioticos(int usoAntibioticos) {
		this.usoAntibioticos = usoAntibioticos;
	}




	public int getVacunaAntihib() {
		return vacunaAntihib;
	}




	public void setVacunaAntihib(int vacunaAntihib) {
		this.vacunaAntihib = vacunaAntihib;
	}




	public int getVacunaAntimenin() {
		return vacunaAntimenin;
	}




	public void setVacunaAntimenin(int vacunaAntimenin) {
		this.vacunaAntimenin = vacunaAntimenin;
	}




	public int getVacunaAntineumo() {
		return vacunaAntineumo;
	}




	public void setVacunaAntineumo(int vacunaAntineumo) {
		this.vacunaAntineumo = vacunaAntineumo;
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




	public String getCualAntibiotico() {
		return cualAntibiotico;
	}




	public void setCualAntibiotico(String cualAntibiotico) {
		this.cualAntibiotico = cualAntibiotico;
	}




	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}




	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
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
