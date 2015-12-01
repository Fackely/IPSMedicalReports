package com.sysmedica.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class FichaLepraForm extends ValidatorForm {


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
    private int codigoFichaLepra;
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
    
    private int criterioClinico;
	private String indiceBacilar;
	private int clasificacion;
	private int resultadosBiopsia;
	private int ojoDerecho;
	private int ojoIzquierdo;
	private int manoDerecha;
	private int manoIzquierda;
	private int pieDerecho;
	private int pieIzquierdo;
	private int tipoCasoLepra;
	private int tieneCicatriz;
	private int fuenteContagio;
	private int metodoCaptacion;
	private String fechaInvestigacion;
	private int tieneConvivientes;
	private String totalConvivientes;
	private String totalExaminados;
	private String sanosConCicatriz;
	private String sanosSinCicatriz;
	private String sintomaticosConCicatriz;
	private String sintomaticosSinCicatriz;
	private String vacunadosBcg;
	private String motivoNoAplicacion;
	private String investigadoPor;
	private String telefonoInvestigador;
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
    	
    	criterioClinico = 0;
    	indiceBacilar = "";
    	clasificacion = 0;
    	resultadosBiopsia = 0;
    	ojoDerecho = 0;
    	ojoIzquierdo = 0;
    	manoDerecha = 0;
    	manoIzquierda = 0;
    	pieDerecho = 0;
    	pieIzquierdo = 0;
    	tipoCasoLepra = 0;
    	tieneCicatriz = 0;
    	fuenteContagio = 0;
    	metodoCaptacion = 0;
    	fechaInvestigacion = "";
    	tieneConvivientes = 0;
    	totalConvivientes = "";
    	totalExaminados = "";
    	sanosConCicatriz = "";
    	sanosSinCicatriz = "";
    	sintomaticosConCicatriz = "";
    	sintomaticosSinCicatriz = "";
    	vacunadosBcg = "";
    	motivoNoAplicacion = "";
    	investigadoPor = "";
    	telefonoInvestigador = "";
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
            String fechaInvestigacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInvestigacion);
        	
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
        	
        	
        	
        	if (fechaInvestigacion.trim().length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaInvestigacion)) {
	                
	                errores.add("Campo Fecha de Investigacion no valido", new ActionMessage("errors.formatoFechaInvalido","de Investigacion"));
	            }
	            else if (fechaInvestigacionTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Investigacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Investigacion", "actual"));
				}
	            else if (fechaInicioSintomasTransformada.compareTo(fechaInvestigacionTransformada)>0) {
					
					errores.add("Campo Fecha de Investigacion no valido", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de Investigacion", "de Inicio de Sintomas"));
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
            String fechaInvestigacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInvestigacion);
        	
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
        	
        	

        	if (fechaInvestigacion.trim().length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaInvestigacion)) {
	                
	                errores.add("Campo Fecha de Investigacion no valido", new ActionMessage("errors.formatoFechaInvalido","de Investigacion"));
	            }
	            else if (fechaInvestigacionTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Investigacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Investigacion", "actual"));
				}
	            else if (fechaInicioSintomasTransformada.compareTo(fechaInvestigacionTransformada)>0) {
					
					errores.add("Campo Fecha de Investigacion no valido", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de Investigacion", "de Inicio de Sintomas"));
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




	public int getClasificacion() {
		return clasificacion;
	}




	public void setClasificacion(int clasificacion) {
		this.clasificacion = clasificacion;
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




	public int getCodigoFichaLepra() {
		return codigoFichaLepra;
	}




	public void setCodigoFichaLepra(int codigoFichaLepra) {
		this.codigoFichaLepra = codigoFichaLepra;
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




	public int getCriterioClinico() {
		return criterioClinico;
	}




	public void setCriterioClinico(int criterioClinico) {
		this.criterioClinico = criterioClinico;
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




	public boolean isFichamodulo() {
		return fichamodulo;
	}




	public void setFichamodulo(boolean fichamodulo) {
		this.fichamodulo = fichamodulo;
	}




	public int getFuenteContagio() {
		return fuenteContagio;
	}




	public void setFuenteContagio(int fuenteContagio) {
		this.fuenteContagio = fuenteContagio;
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




	public String getIndiceBacilar() {
		return indiceBacilar;
	}




	public void setIndiceBacilar(String indiceBacilar) {
		this.indiceBacilar = indiceBacilar;
	}




	public int getInstitucionAtendio() {
		return institucionAtendio;
	}




	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
	}




	public String getInvestigadoPor() {
		return investigadoPor;
	}




	public void setInvestigadoPor(String investigadoPor) {
		this.investigadoPor = investigadoPor;
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




	public int getManoDerecha() {
		return manoDerecha;
	}




	public void setManoDerecha(int manoDerecha) {
		this.manoDerecha = manoDerecha;
	}




	public int getManoIzquierda() {
		return manoIzquierda;
	}




	public void setManoIzquierda(int manoIzquierda) {
		this.manoIzquierda = manoIzquierda;
	}




	public int getMetodoCaptacion() {
		return metodoCaptacion;
	}




	public void setMetodoCaptacion(int metodoCaptacion) {
		this.metodoCaptacion = metodoCaptacion;
	}




	public String getMotivoNoAplicacion() {
		return motivoNoAplicacion;
	}




	public void setMotivoNoAplicacion(String motivoNoAplicacion) {
		this.motivoNoAplicacion = motivoNoAplicacion;
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




	public int getOjoDerecho() {
		return ojoDerecho;
	}




	public void setOjoDerecho(int ojoDerecho) {
		this.ojoDerecho = ojoDerecho;
	}




	public int getOjoIzquierdo() {
		return ojoIzquierdo;
	}




	public void setOjoIzquierdo(int ojoIzquierdo) {
		this.ojoIzquierdo = ojoIzquierdo;
	}




	public String getPais() {
		return pais;
	}




	public void setPais(String pais) {
		this.pais = pais;
	}




	public int getPieDerecho() {
		return pieDerecho;
	}




	public void setPieDerecho(int pieDerecho) {
		this.pieDerecho = pieDerecho;
	}




	public int getPieIzquierdo() {
		return pieIzquierdo;
	}




	public void setPieIzquierdo(int pieIzquierdo) {
		this.pieIzquierdo = pieIzquierdo;
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




	public int getResultadosBiopsia() {
		return resultadosBiopsia;
	}




	public void setResultadosBiopsia(int resultadosBiopsia) {
		this.resultadosBiopsia = resultadosBiopsia;
	}




	public String getSanosConCicatriz() {
		return sanosConCicatriz;
	}




	public void setSanosConCicatriz(String sanosConCicatriz) {
		this.sanosConCicatriz = sanosConCicatriz;
	}




	public String getSanosSinCicatriz() {
		return sanosSinCicatriz;
	}




	public void setSanosSinCicatriz(String sanosSinCicatriz) {
		this.sanosSinCicatriz = sanosSinCicatriz;
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




	public String getSintomaticosConCicatriz() {
		return sintomaticosConCicatriz;
	}




	public void setSintomaticosConCicatriz(String sintomaticosConCicatriz) {
		this.sintomaticosConCicatriz = sintomaticosConCicatriz;
	}




	public String getSintomaticosSinCicatriz() {
		return sintomaticosSinCicatriz;
	}




	public void setSintomaticosSinCicatriz(String sintomaticosSinCicatriz) {
		this.sintomaticosSinCicatriz = sintomaticosSinCicatriz;
	}




	public String getSire() {
		return sire;
	}




	public void setSire(String sire) {
		this.sire = sire;
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




	public int getTieneCicatriz() {
		return tieneCicatriz;
	}




	public void setTieneCicatriz(int tieneCicatriz) {
		this.tieneCicatriz = tieneCicatriz;
	}




	public int getTieneConvivientes() {
		return tieneConvivientes;
	}




	public void setTieneConvivientes(int tieneConvivientes) {
		this.tieneConvivientes = tieneConvivientes;
	}




	public int getTipoCaso() {
		return tipoCaso;
	}




	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}




	public int getTipoCasoLepra() {
		return tipoCasoLepra;
	}




	public void setTipoCasoLepra(int tipoCasoLepra) {
		this.tipoCasoLepra = tipoCasoLepra;
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




	public String getTotalConvivientes() {
		return totalConvivientes;
	}




	public void setTotalConvivientes(String totalConvivientes) {
		this.totalConvivientes = totalConvivientes;
	}




	public String getTotalExaminados() {
		return totalExaminados;
	}




	public void setTotalExaminados(String totalExaminados) {
		this.totalExaminados = totalExaminados;
	}




	public String getUrlFichasAnteriores() {
		return urlFichasAnteriores;
	}




	public void setUrlFichasAnteriores(String urlFichasAnteriores) {
		this.urlFichasAnteriores = urlFichasAnteriores;
	}




	public String getVacunadosBcg() {
		return vacunadosBcg;
	}




	public void setVacunadosBcg(String vacunadosBcg) {
		this.vacunadosBcg = vacunadosBcg;
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
