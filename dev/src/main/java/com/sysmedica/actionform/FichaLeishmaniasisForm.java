package com.sysmedica.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class FichaLeishmaniasisForm extends ValidatorForm {


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
    private int codigoFichaLeishmaniasis;
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
    
    private String numeroLesiones;
    private int localizacionLesiones;
    private String anchoLesion1;
    private String largoLesion1;
    private String anchoLesion2;
    private String largoLesion2;
    private String anchoLesion3;
    private String largoLesion3;
    private int cicatrices;
    private String tiempo;
    private int unidadTiempo;
    private int antecedenteTrauma;
    private int mucosaAfectada;
    private int rinorrea;
    private int epistaxis;
    private int obstruccion;
    private int disfonia;
    private int disfagia;
    private int hiperemia;
    private int ulceracion;
    private int perforacion;
    private int destruccion;
    private int fiebre;
    private int hepatomegalia;
    private int esplenomegalia;
    private int anemia;
    private int leucopenia;
    private int trombocitopenia;
    private int recibioTratamiento;
    private String numeroVeces;
    private int medicamentoRecibio;
    private String otroMedicamento;
    private String pesoPaciente;
    private String volumenDiario;
    private String diasTratamiento;
    private String totalAmpollas;
    
    private HashMap lesiones;
    private HashMap datosLaboratorio;
    
    private int numeroOtrosTipos;
	

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
    
    private int cara;
    private int tronco;
    private int superiores;
    private int inferiores;
    
    
    public void reset() {
    	
    	numeroLesiones = "";
        localizacionLesiones = 0;
        anchoLesion1 = "";
        largoLesion1 = "";
        anchoLesion2 = "";
        largoLesion2 = "";
        anchoLesion3 = "";
        largoLesion3 = "";
        cicatrices = 0;
        tiempo = "";
        unidadTiempo = 0;
        antecedenteTrauma = 0;
        mucosaAfectada = 0;
        rinorrea = 0;
        epistaxis = 0;
        obstruccion = 0;
        disfonia = 0;
        disfagia = 0;
        hiperemia = 0;
        ulceracion = 0;
        perforacion = 0;
        destruccion = 0;
        fiebre = 0;
        hepatomegalia = 0;
        esplenomegalia = 0;
        anemia = 0;
        leucopenia = 0;
        trombocitopenia = 0;
        recibioTratamiento = 0;
        numeroVeces = "";
        medicamentoRecibio = 0;
        otroMedicamento = "";
        pesoPaciente = "";
        volumenDiario = "";
        diasTratamiento = "";
        totalAmpollas = "";
        
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
        numeroOtrosTipos = 0;
        
        lesiones = new HashMap();
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




	public boolean isActiva() {
		return activa;
	}




	public void setActiva(boolean activa) {
		this.activa = activa;
	}




	public String getAnchoLesion1() {
		return anchoLesion1;
	}




	public void setAnchoLesion1(String anchoLesion1) {
		this.anchoLesion1 = anchoLesion1;
	}




	public String getAnchoLesion2() {
		return anchoLesion2;
	}




	public void setAnchoLesion2(String anchoLesion2) {
		this.anchoLesion2 = anchoLesion2;
	}




	public String getAnchoLesion3() {
		return anchoLesion3;
	}




	public void setAnchoLesion3(String anchoLesion3) {
		this.anchoLesion3 = anchoLesion3;
	}




	public int getAnemia() {
		return anemia;
	}




	public void setAnemia(int anemia) {
		this.anemia = anemia;
	}




	public int getAntecedenteTrauma() {
		return antecedenteTrauma;
	}




	public void setAntecedenteTrauma(int antecedenteTrauma) {
		this.antecedenteTrauma = antecedenteTrauma;
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




	public int getCicatrices() {
		return cicatrices;
	}




	public void setCicatrices(int cicatrices) {
		this.cicatrices = cicatrices;
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




	public int getCodigoFichaAcciOfidico() {
		return codigoFichaAcciOfidico;
	}




	public void setCodigoFichaAcciOfidico(int codigoFichaAcciOfidico) {
		this.codigoFichaAcciOfidico = codigoFichaAcciOfidico;
	}




	public int getCodigoFichaLeishmaniasis() {
		return codigoFichaLeishmaniasis;
	}




	public void setCodigoFichaLeishmaniasis(int codigoFichaLeishmaniasis) {
		this.codigoFichaLeishmaniasis = codigoFichaLeishmaniasis;
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




	public int getDestruccion() {
		return destruccion;
	}




	public void setDestruccion(int destruccion) {
		this.destruccion = destruccion;
	}




	public String getDiagnosticosSeleccionados() {
		return diagnosticosSeleccionados;
	}




	public void setDiagnosticosSeleccionados(String diagnosticosSeleccionados) {
		this.diagnosticosSeleccionados = diagnosticosSeleccionados;
	}




	public String getDiasTratamiento() {
		return diasTratamiento;
	}




	public void setDiasTratamiento(String diasTratamiento) {
		this.diasTratamiento = diasTratamiento;
	}




	public String getDireccionPaciente() {
		return direccionPaciente;
	}




	public void setDireccionPaciente(String direccionPaciente) {
		this.direccionPaciente = direccionPaciente;
	}




	public int getDisfagia() {
		return disfagia;
	}




	public void setDisfagia(int disfagia) {
		this.disfagia = disfagia;
	}




	public int getDisfonia() {
		return disfonia;
	}




	public void setDisfonia(int disfonia) {
		this.disfonia = disfonia;
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




	public boolean isEpidemiologia() {
		return epidemiologia;
	}




	public void setEpidemiologia(boolean epidemiologia) {
		this.epidemiologia = epidemiologia;
	}




	public int getEpistaxis() {
		return epistaxis;
	}




	public void setEpistaxis(int epistaxis) {
		this.epistaxis = epistaxis;
	}




	public int getEsplenomegalia() {
		return esplenomegalia;
	}




	public void setEsplenomegalia(int esplenomegalia) {
		this.esplenomegalia = esplenomegalia;
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




	public int getHepatomegalia() {
		return hepatomegalia;
	}




	public void setHepatomegalia(int hepatomegalia) {
		this.hepatomegalia = hepatomegalia;
	}




	public int getHiperemia() {
		return hiperemia;
	}




	public void setHiperemia(int hiperemia) {
		this.hiperemia = hiperemia;
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




	public String getLargoLesion1() {
		return largoLesion1;
	}




	public void setLargoLesion1(String largoLesion1) {
		this.largoLesion1 = largoLesion1;
	}




	public String getLargoLesion2() {
		return largoLesion2;
	}




	public void setLargoLesion2(String largoLesion2) {
		this.largoLesion2 = largoLesion2;
	}




	public String getLargoLesion3() {
		return largoLesion3;
	}




	public void setLargoLesion3(String largoLesion3) {
		this.largoLesion3 = largoLesion3;
	}




	public int getLeucopenia() {
		return leucopenia;
	}




	public void setLeucopenia(int leucopenia) {
		this.leucopenia = leucopenia;
	}




	public int getLocalizacionLesiones() {
		return localizacionLesiones;
	}




	public void setLocalizacionLesiones(int localizacionLesiones) {
		this.localizacionLesiones = localizacionLesiones;
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




	public int getMedicamentoRecibio() {
		return medicamentoRecibio;
	}




	public void setMedicamentoRecibio(int medicamentoRecibio) {
		this.medicamentoRecibio = medicamentoRecibio;
	}




	public int getMucosaAfectada() {
		return mucosaAfectada;
	}




	public void setMucosaAfectada(int mucosaAfectada) {
		this.mucosaAfectada = mucosaAfectada;
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




	public String getNumeroLesiones() {
		return numeroLesiones;
	}




	public void setNumeroLesiones(String numeroLesiones) {
		this.numeroLesiones = numeroLesiones;
	}




	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}




	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}




	public String getNumeroVeces() {
		return numeroVeces;
	}




	public void setNumeroVeces(String numeroVeces) {
		this.numeroVeces = numeroVeces;
	}




	public int getObstruccion() {
		return obstruccion;
	}




	public void setObstruccion(int obstruccion) {
		this.obstruccion = obstruccion;
	}




	public String getOcupacion() {
		return ocupacion;
	}




	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}




	public String getOtroMedicamento() {
		return otroMedicamento;
	}




	public void setOtroMedicamento(String otroMedicamento) {
		this.otroMedicamento = otroMedicamento;
	}




	public String getPais() {
		return pais;
	}




	public void setPais(String pais) {
		this.pais = pais;
	}




	public int getPerforacion() {
		return perforacion;
	}




	public void setPerforacion(int perforacion) {
		this.perforacion = perforacion;
	}




	public String getPesoPaciente() {
		return pesoPaciente;
	}




	public void setPesoPaciente(String pesoPaciente) {
		this.pesoPaciente = pesoPaciente;
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




	public int getRecibioTratamiento() {
		return recibioTratamiento;
	}




	public void setRecibioTratamiento(int recibioTratamiento) {
		this.recibioTratamiento = recibioTratamiento;
	}




	public String getRegimenSalud() {
		return regimenSalud;
	}




	public void setRegimenSalud(String regimenSalud) {
		this.regimenSalud = regimenSalud;
	}




	public int getRinorrea() {
		return rinorrea;
	}




	public void setRinorrea(int rinorrea) {
		this.rinorrea = rinorrea;
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




	public String getTiempo() {
		return tiempo;
	}




	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
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




	public String getTotalAmpollas() {
		return totalAmpollas;
	}




	public void setTotalAmpollas(String totalAmpollas) {
		this.totalAmpollas = totalAmpollas;
	}




	public int getTrombocitopenia() {
		return trombocitopenia;
	}




	public void setTrombocitopenia(int trombocitopenia) {
		this.trombocitopenia = trombocitopenia;
	}




	public int getUlceracion() {
		return ulceracion;
	}




	public void setUlceracion(int ulceracion) {
		this.ulceracion = ulceracion;
	}




	public int getUnidadTiempo() {
		return unidadTiempo;
	}




	public void setUnidadTiempo(int unidadTiempo) {
		this.unidadTiempo = unidadTiempo;
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




	public String getVolumenDiario() {
		return volumenDiario;
	}




	public void setVolumenDiario(String volumenDiario) {
		this.volumenDiario = volumenDiario;
	}




	public String getZonaDomicilio() {
		return zonaDomicilio;
	}




	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}





	public HashMap getLesiones() {
		return lesiones;
	}




	public void setLesiones(HashMap lesiones) {
		this.lesiones = lesiones;
	}




	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}




	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
	}




	public int getNumeroOtrosTipos() {
		return numeroOtrosTipos;
	}




	public void setNumeroOtrosTipos(int numeroOtrosTipos) {
		this.numeroOtrosTipos = numeroOtrosTipos;
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




	public int getCara() {
		return cara;
	}




	public void setCara(int cara) {
		this.cara = cara;
	}




	public int getInferiores() {
		return inferiores;
	}




	public void setInferiores(int inferiores) {
		this.inferiores = inferiores;
	}




	public int getSuperiores() {
		return superiores;
	}




	public void setSuperiores(int superiores) {
		this.superiores = superiores;
	}




	public int getTronco() {
		return tronco;
	}




	public void setTronco(int tronco) {
		this.tronco = tronco;
	}
    
    
    
}
