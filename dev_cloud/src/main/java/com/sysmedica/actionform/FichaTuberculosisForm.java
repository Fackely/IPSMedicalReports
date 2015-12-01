package com.sysmedica.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class FichaTuberculosisForm extends ValidatorForm {

	private boolean activa;
	private String urlFichasAnteriores;
	
	private int codigoInstitucion;
	
	private boolean hayServicios;
    private boolean hayLaboratorios;
    
	private boolean notificar;
    private String loginUsuario;
    private int codigoFichaTuberculosis;
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
    
    private boolean baciloscopia;
    private boolean cultivo;
    private boolean histopatologia;
    private boolean clinicaPaciente;
    private boolean nexoEpidemiologico;
    private boolean radiologico;
    private boolean tuberculina;
    private boolean ada;
    private int resultadoBk;
    private String resultadoAda;
    private String resultadoTuberculina;
    private int tipoTuberculosis;
    private boolean tieneCicatriz;
    private String fuenteContagio;
    private int metodoHallazgo;
    private String otroCual;
    private int asociadoVih;
    private boolean asesoriaVih;
    private String observaciones;
    private boolean otroDx;
    private int realizoCultivo;
    private String otroTipoTuberculosis;
    private int fuenteContagio2;
    
    private String codCiudadResidencia;
    private String codDepResidencia;
    private String nombreMunProcedencia;
    private String nombreDepProcedencia;
    private String nombreInstitucionAtendio;
    private String nombreEnfermedadNotificable;
    private String nombreMunNotifica;
    private String nombreDepNotifica;
    
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
        municipioNotifica = "";
        departamentoNotifica = "";
        institucionAtendio = 0;
        fechaNotificacion = "";
        
        fechaConsultaGeneral = "";
        codigoMunProcedencia = "";
        codigoDepProcedencia = "";
        fechaInicioSint = "";
        tipoCaso = 0;
        hospitalizado = false;
        fechaHospitalizacion = "";
        estaVivo = true;
        fechaDefuncion = "";
        nombreProfesional = "";
     
        baciloscopia = false;
        cultivo = false;
        histopatologia = false;
        clinicaPaciente = false;
        nexoEpidemiologico = false;
        radiologico = false;
        tuberculina = false;
        ada = false;
        resultadoBk = 0;
        resultadoAda = "";
        resultadoTuberculina = "";
        tipoTuberculosis = 0;
        tieneCicatriz = false;
        fuenteContagio = "";
        metodoHallazgo = 0;
        otroCual = "";
        asociadoVih = 0;
        asesoriaVih = false;
        observaciones = "";
        
        estaVivo = false;
        
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
        estaVivo = false;
        fechaDefuncion = "";
        nombreProfesional = "";
        otroDx = false;
        realizoCultivo = 0;
        otroTipoTuberculosis = "";
        fuenteContagio2 = 0;
        
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
            
            if (resultadoTuberculina.length()>0) {
	            try {
	            	int n = Integer.parseInt(resultadoTuberculina);
	            }
	            catch (NumberFormatException nfe) {
	            	errores.add("Resultado de Tuberculina No Valido", new ActionMessage("error.epidemiologia.resultadotuberculina"));
	            }
            }
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
            
            if (resultadoTuberculina.length()>0) {
	            try {
	            	int n = Integer.parseInt(resultadoTuberculina);
	            }
	            catch (NumberFormatException nfe) {
	            	errores.add("Resultado de Tuberculina No Valido", new ActionMessage("error.epidemiologia.resultadotuberculina"));
	            }
            }
		}
        
        return errores;
    }


	public boolean isAda() {
		return ada;
	}


	public void setAda(boolean ada) {
		this.ada = ada;
	}


	public String getAseguradora() {
		return aseguradora;
	}


	public void setAseguradora(String aseguradora) {
		this.aseguradora = aseguradora;
	}


	public boolean isAsesoriaVih() {
		return asesoriaVih;
	}


	public void setAsesoriaVih(boolean asesoriaVih) {
		this.asesoriaVih = asesoriaVih;
	}


	public int getAsociadoVih() {
		return asociadoVih;
	}


	public void setAsociadoVih(int asociadoVih) {
		this.asociadoVih = asociadoVih;
	}


	public boolean isBaciloscopia() {
		return baciloscopia;
	}


	public void setBaciloscopia(boolean baciloscopia) {
		this.baciloscopia = baciloscopia;
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


	public boolean isClinicaPaciente() {
		return clinicaPaciente;
	}


	public void setClinicaPaciente(boolean clinicaPaciente) {
		this.clinicaPaciente = clinicaPaciente;
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


	public int getCodigoFichaTuberculosis() {
		return codigoFichaTuberculosis;
	}


	public void setCodigoFichaTuberculosis(int codigoFichaTuberculosis) {
		this.codigoFichaTuberculosis = codigoFichaTuberculosis;
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


	public boolean isCultivo() {
		return cultivo;
	}


	public void setCultivo(boolean cultivo) {
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


	public String getFuenteContagio() {
		return fuenteContagio;
	}


	public void setFuenteContagio(String fuenteContagio) {
		this.fuenteContagio = fuenteContagio;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public boolean isHistopatologia() {
		return histopatologia;
	}


	public void setHistopatologia(boolean histopatologia) {
		this.histopatologia = histopatologia;
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


	public int getMetodoHallazgo() {
		return metodoHallazgo;
	}


	public void setMetodoHallazgo(int metodoHallazgo) {
		this.metodoHallazgo = metodoHallazgo;
	}


	public String getMunicipioNotifica() {
		return municipioNotifica;
	}


	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}


	public boolean isNexoEpidemiologico() {
		return nexoEpidemiologico;
	}


	public void setNexoEpidemiologico(boolean nexoEpidemiologico) {
		this.nexoEpidemiologico = nexoEpidemiologico;
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


	public String getOtroCual() {
		return otroCual;
	}


	public void setOtroCual(String otroCual) {
		this.otroCual = otroCual;
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


	public boolean isRadiologico() {
		return radiologico;
	}


	public void setRadiologico(boolean radiologico) {
		this.radiologico = radiologico;
	}


	public String getRegimenSalud() {
		return regimenSalud;
	}


	public void setRegimenSalud(String regimenSalud) {
		this.regimenSalud = regimenSalud;
	}


	public String getResultadoAda() {
		return resultadoAda;
	}


	public void setResultadoAda(String resultadoAda) {
		this.resultadoAda = resultadoAda;
	}


	public int getResultadoBk() {
		return resultadoBk;
	}


	public void setResultadoBk(int resultadoBk) {
		this.resultadoBk = resultadoBk;
	}


	public String getResultadoTuberculina() {
		return resultadoTuberculina;
	}


	public void setResultadoTuberculina(String resultadoTuberculina) {
		this.resultadoTuberculina = resultadoTuberculina;
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


	public boolean isTieneCicatriz() {
		return tieneCicatriz;
	}


	public void setTieneCicatriz(boolean tieneCicatriz) {
		this.tieneCicatriz = tieneCicatriz;
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


	public int getTipoTuberculosis() {
		return tipoTuberculosis;
	}


	public void setTipoTuberculosis(int tipoTuberculosis) {
		this.tipoTuberculosis = tipoTuberculosis;
	}


	public boolean isTuberculina() {
		return tuberculina;
	}


	public void setTuberculina(boolean tuberculina) {
		this.tuberculina = tuberculina;
	}


	public String getZonaDomicilio() {
		return zonaDomicilio;
	}


	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
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


	public String getNombreDepProcedencia() {
		return nombreDepProcedencia;
	}


	public void setNombreDepProcedencia(String nombreDepProcedencia) {
		this.nombreDepProcedencia = nombreDepProcedencia;
	}


	public String getNombreMunProcedencia() {
		return nombreMunProcedencia;
	}


	public void setNombreMunProcedencia(String nombreMunProcedencia) {
		this.nombreMunProcedencia = nombreMunProcedencia;
	}


	public String getNombreInstitucionAtendio() {
		return nombreInstitucionAtendio;
	}


	public void setNombreInstitucionAtendio(String nombreInstitucionAtendio) {
		this.nombreInstitucionAtendio = nombreInstitucionAtendio;
	}


	public String getNombreEnfermedadNotificable() {
		return nombreEnfermedadNotificable;
	}


	public void setNombreEnfermedadNotificable(String nombreEnfermedadNotificable) {
		this.nombreEnfermedadNotificable = nombreEnfermedadNotificable;
	}


	public String getNombreDepNotifica() {
		return nombreDepNotifica;
	}


	public void setNombreDepNotifica(String nombreDepNotifica) {
		this.nombreDepNotifica = nombreDepNotifica;
	}


	public String getNombreMunNotifica() {
		return nombreMunNotifica;
	}


	public void setNombreMunNotifica(String nombreMunNotifica) {
		this.nombreMunNotifica = nombreMunNotifica;
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


	public boolean isFichamodulo() {
		return fichamodulo;
	}


	public void setFichamodulo(boolean fichamodulo) {
		this.fichamodulo = fichamodulo;
	}


	public String getEsPrimeraVez() {
		return esPrimeraVez;
	}


	public void setEsPrimeraVez(String esPrimeraVez) {
		this.esPrimeraVez = esPrimeraVez;
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


	public int getFuenteContagio2() {
		return fuenteContagio2;
	}


	public void setFuenteContagio2(int fuenteContagio2) {
		this.fuenteContagio2 = fuenteContagio2;
	}


	public boolean isOtroDx() {
		return otroDx;
	}


	public void setOtroDx(boolean otroDx) {
		this.otroDx = otroDx;
	}


	public String getOtroTipoTuberculosis() {
		return otroTipoTuberculosis;
	}


	public void setOtroTipoTuberculosis(String otroTipoTuberculosis) {
		this.otroTipoTuberculosis = otroTipoTuberculosis;
	}


	public int getRealizoCultivo() {
		return realizoCultivo;
	}


	public void setRealizoCultivo(int realizoCultivo) {
		this.realizoCultivo = realizoCultivo;
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
