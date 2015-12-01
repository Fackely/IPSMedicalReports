package com.sysmedica.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class FichaRubCongenitaForm extends ValidatorForm {

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
    
    private int codigoFichaRubCongenita;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
    private int clasificacionInicial;
	private String nombreTutor;
	private String lugarNacimientoPaciente;
	private int fuenteNotificacion;
	private String nombreMadre;
	private String edadMadre;
	private String embarazos;
	private int carneVacunacion;
	private int vacunaRubeola;
	private String numeroDosis;
	private String fechaUltimaDosis;
	private int rubeolaConfirmada;
	private String semanasEmbarazo;
	private int similarRubeola;
	private String semanasEmbarazo2;
	private int expuestaRubeola;
	private String semanasEmbarazo3;
	private String donde;
	private int viajes;
	private String semanasEmbarazo4;
	private String dondeViajo;
	private String apgar;
	private int bajoPesoNacimiento;
	private String peso;
	private int pequenoEdadGesta;
	private String semanasEdad;
	private int cataratas;
	private int glaucoma;
	private int retinopatia;
	private int otrosOjo;
	private int arterioso;
	private int estenosis;
	private int otrosCorazon;
	private int sordera;
	private int otrosOido;
	private int microCefalia;
	private int sicomotor;
	private int purpura;
	private int hepatomegalia;
	private int ictericia;
	private int esplenomegalia;
	private int osteopatia;
	private int meningoencefalitis;
	private int otrosGeneral;
	private int examenesEspeciales;
	private String examen;
	private int anatomoPatologico;
	private String examen2;
	private int compatibleSrc;
	private int dxFinal;
	private String nombreInvestigador;
	private String telefonoInvestigador;
	
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
    	
    	clasificacionInicial = 0;
    	nombreTutor = "";
    	lugarNacimientoPaciente = "";
    	fuenteNotificacion = 0;
    	nombreMadre = "";
    	edadMadre = "";
    	embarazos = "";
    	carneVacunacion = 0;
    	vacunaRubeola = 0;
    	numeroDosis = "";
    	fechaUltimaDosis = "";
    	rubeolaConfirmada = 0;
    	semanasEmbarazo = "";
    	similarRubeola = 0;
    	semanasEmbarazo2 = "";
    	expuestaRubeola = 0;
    	semanasEmbarazo3 = "";
    	donde = "";
    	viajes = 0;
    	semanasEmbarazo4 = "";
    	dondeViajo = "";
    	apgar = "";
    	bajoPesoNacimiento = 0;
    	peso = "";
    	pequenoEdadGesta = 0;
    	semanasEdad = "";
    	cataratas = 0;
    	glaucoma = 0;
    	retinopatia = 0;
    	otrosOjo = 0;
    	arterioso = 0;
    	estenosis = 0;
    	otrosCorazon = 0;
    	sordera = 0;
    	otrosOido = 0;
    	microCefalia = 0;
    	sicomotor = 0;
    	purpura = 0;
    	hepatomegalia = 0;
    	ictericia = 0;
    	esplenomegalia = 0;
    	osteopatia = 0;
    	meningoencefalitis = 0;
    	otrosGeneral = 0;
    	examenesEspeciales = 0;
    	examen = "";
    	anatomoPatologico = 0;
    	examen2 = "";
    	compatibleSrc = 0;
    	dxFinal = 0;
    	nombreInvestigador = "";
    	telefonoInvestigador = "";
        
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
            String fechaUltimaDosisTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaUltimaDosis);
        	
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
        	
        		int edad = Integer.parseInt(edadMadre);
        	}
        	catch (NumberFormatException nfe) {
        		
        		errores.add("El campo Edad de la Madre debe ser Numérico", new ActionMessage("error.epidemiologia.valornumericogeneral","Edad de la Madre "));
        	}
        	
        	
        	try {
            	
        		int embar = Integer.parseInt(embarazos);
        	}
        	catch (NumberFormatException nfe) {
        		
        		errores.add("El campo Embarazos debe ser Numérico", new ActionMessage("error.epidemiologia.valornumericogeneral","Embarazos "));
        	}
        	
        	
        	if (numeroDosis.length()>0) {
	        	try {
	            	
	        		int numdosis = Integer.parseInt(numeroDosis);
	        	}
	        	catch (NumberFormatException nfe) {
	        		
	        		errores.add("El campo Número de Dosis debe ser Numérico", new ActionMessage("error.epidemiologia.valornumericogeneral","Número de Dosis "));
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
            String fechaUltimaDosisTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaUltimaDosis);
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	
        	

        	try {
        	
        		int edad = Integer.parseInt(edadMadre);
        	}
        	catch (NumberFormatException nfe) {
        		
        		errores.add("El campo Edad de la Madre debe ser Numérico", new ActionMessage("error.epidemiologia.valornumericogeneral","Edad de la Madre "));
        	}
        	
        	
        	try {
            	
        		int embar = Integer.parseInt(embarazos);
        	}
        	catch (NumberFormatException nfe) {
        		
        		errores.add("El campo Embarazos debe ser Numérico", new ActionMessage("error.epidemiologia.valornumericogeneral","Embarazos "));
        	}
        	
        	
        	if (numeroDosis.length()>0) {
	        	try {
	            	
	        		int numdosis = Integer.parseInt(numeroDosis);
	        	}
	        	catch (NumberFormatException nfe) {
	        		
	        		errores.add("El campo Número de Dosis debe ser Numérico", new ActionMessage("error.epidemiologia.valornumericogeneral","Número de Dosis "));
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



	public int getAnatomoPatologico() {
		return anatomoPatologico;
	}



	public void setAnatomoPatologico(int anatomoPatologico) {
		this.anatomoPatologico = anatomoPatologico;
	}



	public int getAnyoSemanaEpi() {
		return anyoSemanaEpi;
	}



	public void setAnyoSemanaEpi(int anyoSemanaEpi) {
		this.anyoSemanaEpi = anyoSemanaEpi;
	}



	public String getApgar() {
		return apgar;
	}



	public void setApgar(String apgar) {
		this.apgar = apgar;
	}



	public int getAreaProcedencia() {
		return areaProcedencia;
	}



	public void setAreaProcedencia(int areaProcedencia) {
		this.areaProcedencia = areaProcedencia;
	}



	public int getArterioso() {
		return arterioso;
	}



	public void setArterioso(int arterioso) {
		this.arterioso = arterioso;
	}



	public String getAseguradora() {
		return aseguradora;
	}



	public void setAseguradora(String aseguradora) {
		this.aseguradora = aseguradora;
	}



	public int getBajoPesoNacimiento() {
		return bajoPesoNacimiento;
	}



	public void setBajoPesoNacimiento(int bajoPesoNacimiento) {
		this.bajoPesoNacimiento = bajoPesoNacimiento;
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



	public int getCataratas() {
		return cataratas;
	}



	public void setCataratas(int cataratas) {
		this.cataratas = cataratas;
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



	public int getClasificacionInicial() {
		return clasificacionInicial;
	}



	public void setClasificacionInicial(int clasificacionInicial) {
		this.clasificacionInicial = clasificacionInicial;
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



	public int getCodigoFichaRubCongenita() {
		return codigoFichaRubCongenita;
	}



	public void setCodigoFichaRubCongenita(int codigoFichaRubCongenita) {
		this.codigoFichaRubCongenita = codigoFichaRubCongenita;
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



	public int getCompatibleSrc() {
		return compatibleSrc;
	}



	public void setCompatibleSrc(int compatibleSrc) {
		this.compatibleSrc = compatibleSrc;
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



	public String getDonde() {
		return donde;
	}



	public void setDonde(String donde) {
		this.donde = donde;
	}



	public String getDondeViajo() {
		return dondeViajo;
	}



	public void setDondeViajo(String dondeViajo) {
		this.dondeViajo = dondeViajo;
	}



	public int getDxFinal() {
		return dxFinal;
	}



	public void setDxFinal(int dxFinal) {
		this.dxFinal = dxFinal;
	}



	public String getEdad() {
		return edad;
	}



	public void setEdad(String edad) {
		this.edad = edad;
	}



	public String getEdadMadre() {
		return edadMadre;
	}



	public void setEdadMadre(String edadMadre) {
		this.edadMadre = edadMadre;
	}



	public String getEmbarazos() {
		return embarazos;
	}



	public void setEmbarazos(String embarazos) {
		this.embarazos = embarazos;
	}



	public boolean isEpidemiologia() {
		return epidemiologia;
	}



	public void setEpidemiologia(boolean epidemiologia) {
		this.epidemiologia = epidemiologia;
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



	public int getEstenosis() {
		return estenosis;
	}



	public void setEstenosis(int estenosis) {
		this.estenosis = estenosis;
	}



	public String getEtnia() {
		return etnia;
	}



	public void setEtnia(String etnia) {
		this.etnia = etnia;
	}



	public String getExamen() {
		return examen;
	}



	public void setExamen(String examen) {
		this.examen = examen;
	}



	public String getExamen2() {
		return examen2;
	}



	public void setExamen2(String examen2) {
		this.examen2 = examen2;
	}



	public int getExamenesEspeciales() {
		return examenesEspeciales;
	}



	public void setExamenesEspeciales(int examenesEspeciales) {
		this.examenesEspeciales = examenesEspeciales;
	}



	public int getExpuestaRubeola() {
		return expuestaRubeola;
	}



	public void setExpuestaRubeola(int expuestaRubeola) {
		this.expuestaRubeola = expuestaRubeola;
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



	public int getFuenteNotificacion() {
		return fuenteNotificacion;
	}



	public void setFuenteNotificacion(int fuenteNotificacion) {
		this.fuenteNotificacion = fuenteNotificacion;
	}



	public String getGenero() {
		return genero;
	}



	public void setGenero(String genero) {
		this.genero = genero;
	}



	public int getGlaucoma() {
		return glaucoma;
	}



	public void setGlaucoma(int glaucoma) {
		this.glaucoma = glaucoma;
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



	public boolean isHospitalizado() {
		return hospitalizado;
	}



	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}



	public int getIctericia() {
		return ictericia;
	}



	public void setIctericia(int ictericia) {
		this.ictericia = ictericia;
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



	public String getLoginUsuario() {
		return loginUsuario;
	}



	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}



	public String getLugarNacimientoPaciente() {
		return lugarNacimientoPaciente;
	}



	public void setLugarNacimientoPaciente(String lugarNacimientoPaciente) {
		this.lugarNacimientoPaciente = lugarNacimientoPaciente;
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



	public int getMeningoencefalitis() {
		return meningoencefalitis;
	}



	public void setMeningoencefalitis(int meningoencefalitis) {
		this.meningoencefalitis = meningoencefalitis;
	}



	public int getMicroCefalia() {
		return microCefalia;
	}



	public void setMicroCefalia(int microCefalia) {
		this.microCefalia = microCefalia;
	}



	public String getMunicipioNotifica() {
		return municipioNotifica;
	}



	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}



	public String getNombreInvestigador() {
		return nombreInvestigador;
	}



	public void setNombreInvestigador(String nombreInvestigador) {
		this.nombreInvestigador = nombreInvestigador;
	}



	public String getNombreMadre() {
		return nombreMadre;
	}



	public void setNombreMadre(String nombreMadre) {
		this.nombreMadre = nombreMadre;
	}



	public String getNombreProfesional() {
		return nombreProfesional;
	}



	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}



	public String getNombreTutor() {
		return nombreTutor;
	}



	public void setNombreTutor(String nombreTutor) {
		this.nombreTutor = nombreTutor;
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



	public String getNumeroDosis() {
		return numeroDosis;
	}



	public void setNumeroDosis(String numeroDosis) {
		this.numeroDosis = numeroDosis;
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



	public int getOsteopatia() {
		return osteopatia;
	}



	public void setOsteopatia(int osteopatia) {
		this.osteopatia = osteopatia;
	}



	public int getOtrosCorazon() {
		return otrosCorazon;
	}



	public void setOtrosCorazon(int otrosCorazon) {
		this.otrosCorazon = otrosCorazon;
	}



	public int getOtrosGeneral() {
		return otrosGeneral;
	}



	public void setOtrosGeneral(int otrosGeneral) {
		this.otrosGeneral = otrosGeneral;
	}



	public int getOtrosOido() {
		return otrosOido;
	}



	public void setOtrosOido(int otrosOido) {
		this.otrosOido = otrosOido;
	}



	public int getOtrosOjo() {
		return otrosOjo;
	}



	public void setOtrosOjo(int otrosOjo) {
		this.otrosOjo = otrosOjo;
	}



	public String getPais() {
		return pais;
	}



	public void setPais(String pais) {
		this.pais = pais;
	}



	public int getPequenoEdadGesta() {
		return pequenoEdadGesta;
	}



	public void setPequenoEdadGesta(int pequenoEdadGesta) {
		this.pequenoEdadGesta = pequenoEdadGesta;
	}



	public String getPeso() {
		return peso;
	}



	public void setPeso(String peso) {
		this.peso = peso;
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



	public int getPurpura() {
		return purpura;
	}



	public void setPurpura(int purpura) {
		this.purpura = purpura;
	}



	public String getRegimenSalud() {
		return regimenSalud;
	}



	public void setRegimenSalud(String regimenSalud) {
		this.regimenSalud = regimenSalud;
	}



	public int getRetinopatia() {
		return retinopatia;
	}



	public void setRetinopatia(int retinopatia) {
		this.retinopatia = retinopatia;
	}



	public int getRubeolaConfirmada() {
		return rubeolaConfirmada;
	}



	public void setRubeolaConfirmada(int rubeolaConfirmada) {
		this.rubeolaConfirmada = rubeolaConfirmada;
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



	public String getSemanasEdad() {
		return semanasEdad;
	}



	public void setSemanasEdad(String semanasEdad) {
		this.semanasEdad = semanasEdad;
	}



	public String getSemanasEmbarazo() {
		return semanasEmbarazo;
	}



	public void setSemanasEmbarazo(String semanasEmbarazo) {
		this.semanasEmbarazo = semanasEmbarazo;
	}



	public String getSemanasEmbarazo2() {
		return semanasEmbarazo2;
	}



	public void setSemanasEmbarazo2(String semanasEmbarazo2) {
		this.semanasEmbarazo2 = semanasEmbarazo2;
	}



	public String getSemanasEmbarazo3() {
		return semanasEmbarazo3;
	}



	public void setSemanasEmbarazo3(String semanasEmbarazo3) {
		this.semanasEmbarazo3 = semanasEmbarazo3;
	}



	public String getSemanasEmbarazo4() {
		return semanasEmbarazo4;
	}



	public void setSemanasEmbarazo4(String semanasEmbarazo4) {
		this.semanasEmbarazo4 = semanasEmbarazo4;
	}



	public int getSicomotor() {
		return sicomotor;
	}



	public void setSicomotor(int sicomotor) {
		this.sicomotor = sicomotor;
	}



	public int getSimilarRubeola() {
		return similarRubeola;
	}



	public void setSimilarRubeola(int similarRubeola) {
		this.similarRubeola = similarRubeola;
	}



	public String getSire() {
		return sire;
	}



	public void setSire(String sire) {
		this.sire = sire;
	}



	public int getSordera() {
		return sordera;
	}



	public void setSordera(int sordera) {
		this.sordera = sordera;
	}



	public String getTelefonoInvestigador() {
		return telefonoInvestigador;
	}



	public void setTelefonoInvestigador(String telefonoInvestigador) {
		this.telefonoInvestigador = telefonoInvestigador;
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



	public int getVacunaRubeola() {
		return vacunaRubeola;
	}



	public void setVacunaRubeola(int vacunaRubeola) {
		this.vacunaRubeola = vacunaRubeola;
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



	public int getViajes() {
		return viajes;
	}



	public void setViajes(int viajes) {
		this.viajes = viajes;
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
