package com.sysmedica.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

import com.sysmedica.mundo.saludpublica.Paciente;

public class FichaLesionesForm extends ValidatorForm {

	private transient Logger logger=Logger.getLogger(FichaLesionesForm.class);
	
	private boolean activa;
	
	private boolean hayServicios;
    private boolean hayLaboratorios;
    
	//*************************************
    // DATOS CARA A :
    
    // Informacion general
    private String sire;
    private String municipioNotifica;
    private String departamentoNotifica;
    private String lugarNotifica;
    private String fechaNotificacion;
    private int semanaEpidemiologica;
    private int anyoSemanaEpi;
        
    private String valorDivDx;
    
    private int codigoEnfNotificable;
    
    private boolean fichamodulo;
    private String esPrimeraVez; 
    private boolean vieneDeFichasAnteriores;
    
    // Informacion del paciente
    private int codigoConvenio;
    
    // Notificacion
    private String fechaConsultaGeneral;
    private String codigoMunProcedencia;
    private String codigoDepProcedencia;
    private String lugarProcedencia;
    private String fechaInicioSint;
    private String nombreProfesional;
	
    //  Identificacion del paciente
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
	
	private int institucionAtendio;
    private int tipoCaso;
    private boolean hospitalizado;
    private String fechaHospitalizacion;
    private boolean estaVivo;
    private String fechaDefuncion;
    
    private int codigoFichaLesiones;
    private int codigoFichaPaciente;
    private String codigoDiagnostico;
    private int numeroSolicitud;
    private String fechaDiligenciamiento;
    private String horaDiligenciamiento;
    
    private int estadoFicha;
    private String loginUsuario;
    private Paciente paciente;
    
    private int codigoPaciente;
    private int codigoNotificacion;
    private boolean notificar;
    
    public static int codigoGlobal;
    
    
    //*************************************
    // DATOS CARA B :
    
    private int estadoIngreso;
    private String fechaOcurrencia;
    private int horaOcurrencia;
    private String lugarOcurrencia;
    private int localidad;
    private String municipioOcurrencia;
    private String departamentoOcurrencia;
    private int armaFuego;
    private int armaCortopunzante;
    private int armaContundente;
    private int asfixia;
    private int intoxicacion;
    private int inmersion;
    private int explosivo;
    private int polvora;
    private int otracausa;
    private int caida;
    private int mordedura;
    private int vivienda;
    private int lugarTrabajo;
    private int lugarEstudio;
    private int taberna;
    private int establecimientoPublico;
    private int viaPublica;
    private int otroLugar;
    private int lesionAccidenteTrabajo;
    private int codigoArp;
    private int actividadDuranteHecho;
    private int lesionIntencional;
    private int tipoLesion;
    private int tipoAccidente;
    private int tipoVehiculo;
    private int condicionLesionado;
    private int tipoViolencia;
    private int agresor;
    private int denunciado;
    private int consumoAlcohol;
    private int consumoOtrasSustancias;
    private String impresionDiagnostica;
    private int gravedadLesion;
    private int escolaridad;
    private int fueraBogota;
    private String horaAtencion;
    private String nombreDiagnostico;
    private String acronimo;
    
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
    
    
    public void reset()
    {
    	sire="";
    	institucionAtendio = 0;
    	fechaInicioSint = "";
    	tipoCaso = 0;
    	hospitalizado = false;
    	fechaHospitalizacion = "";
    	estaVivo = false;
    	fechaDefuncion = "";
    	paciente = new Paciente();
    	
    	estadoIngreso = 0;
        fechaOcurrencia = "";
        horaOcurrencia = 0;
        lugarOcurrencia = "";
        localidad = 0;
        municipioOcurrencia = "";
        departamentoOcurrencia = "";
        armaFuego = 0;
        armaCortopunzante = 0;
        armaContundente = 0;
        asfixia = 0;
        intoxicacion = 0;
        inmersion = 0;
        explosivo = 0;
        polvora = 0;
        otracausa = 0;
        caida = 0;
        mordedura = 0;
        vivienda = 0;
        lugarTrabajo = 0;
        lugarEstudio = 0;
        taberna = 0;
        establecimientoPublico = 0;
        viaPublica = 0;
        otroLugar = 0;
        lesionAccidenteTrabajo = 0;
        codigoArp = 0;
        actividadDuranteHecho = 0;
        lesionIntencional = 0;
        tipoLesion = 0;
        tipoVehiculo = 0;
        condicionLesionado = 0;
        tipoViolencia = 0;
        agresor = 0;
        denunciado = 0;
        consumoAlcohol = 0;
        consumoOtrasSustancias = 0;
        impresionDiagnostica = "";
        gravedadLesion = 0;
        horaAtencion = "";
        acronimo = "";
        nombreDiagnostico = "";
        
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
        	
        	String fechaOcurrenciaTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaOcurrencia);
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	
        	try {
	            if (fechaOcurrencia.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaOcurrencia)) {
		                
		                errores.add("Campo Fecha de Ocurrencia no valido", new ActionMessage("errors.formatoFechaInvalido","de Ocurrencia"));
		            }
		            else if (fechaOcurrenciaTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Ocurrencia no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ocurrencia", "actual"));
					}
	            }
        	}
        	catch (NullPointerException npe) {}
        }
        if (estado.equals("validar")) {
        	
        	String fechaOcurrenciaTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaOcurrencia);
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	
        	try {
	            if (fechaOcurrencia.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaOcurrencia)) {
		                
		                errores.add("Campo Fecha de Ocurrencia no valido", new ActionMessage("errors.formatoFechaInvalido","de Ocurrencia"));
		            }
		            else if (fechaOcurrenciaTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Ocurrencia no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ocurrencia", "actual"));
					}
	            }
        	}
        	catch (NullPointerException npe) {}
        }
        
        return errores;
    }


	public static int getCodigoGlobal() {
		return codigoGlobal;
	}


	public static void setCodigoGlobal(int codigoGlobal) {
		FichaLesionesForm.codigoGlobal = codigoGlobal;
	}


	public int getActividadDuranteHecho() {
		return actividadDuranteHecho;
	}


	public void setActividadDuranteHecho(int actividadDuranteHecho) {
		this.actividadDuranteHecho = actividadDuranteHecho;
	}


	public int getAgresor() {
		return agresor;
	}


	public void setAgresor(int agresor) {
		this.agresor = agresor;
	}


	public int getArmaContundente() {
		return armaContundente;
	}


	public void setArmaContundente(int armaContundente) {
		this.armaContundente = armaContundente;
	}


	public int getArmaCortopunzante() {
		return armaCortopunzante;
	}


	public void setArmaCortopunzante(int armaCortopunzante) {
		this.armaCortopunzante = armaCortopunzante;
	}


	public int getArmaFuego() {
		return armaFuego;
	}


	public void setArmaFuego(int armaFuego) {
		this.armaFuego = armaFuego;
	}


	public int getAsfixia() {
		return asfixia;
	}


	public void setAsfixia(int asfixia) {
		this.asfixia = asfixia;
	}


	public int getCaida() {
		return caida;
	}


	public void setCaida(int caida) {
		this.caida = caida;
	}


	public int getCodigoArp() {
		return codigoArp;
	}


	public void setCodigoArp(int codigoArp) {
		this.codigoArp = codigoArp;
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


	public int getCodigoFichaLesiones() {
		return codigoFichaLesiones;
	}


	public void setCodigoFichaLesiones(int codigoFichaLesiones) {
		this.codigoFichaLesiones = codigoFichaLesiones;
	}


	public int getCodigoFichaPaciente() {
		return codigoFichaPaciente;
	}


	public void setCodigoFichaPaciente(int codigoFichaPaciente) {
		this.codigoFichaPaciente = codigoFichaPaciente;
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


	public int getCondicionLesionado() {
		return condicionLesionado;
	}


	public void setCondicionLesionado(int condicionLesionado) {
		this.condicionLesionado = condicionLesionado;
	}


	public int getConsumoAlcohol() {
		return consumoAlcohol;
	}


	public void setConsumoAlcohol(int consumoAlcohol) {
		this.consumoAlcohol = consumoAlcohol;
	}


	public int getConsumoOtrasSustancias() {
		return consumoOtrasSustancias;
	}


	public void setConsumoOtrasSustancias(int consumoOtrasSustancias) {
		this.consumoOtrasSustancias = consumoOtrasSustancias;
	}


	public int getDenunciado() {
		return denunciado;
	}


	public void setDenunciado(int denunciado) {
		this.denunciado = denunciado;
	}


	public String getDepartamentoNotifica() {
		return departamentoNotifica;
	}


	public void setDepartamentoNotifica(String departamentoNotifica) {
		this.departamentoNotifica = departamentoNotifica;
	}


	public String getDepartamentoOcurrencia() {
		return departamentoOcurrencia;
	}


	public void setDepartamentoOcurrencia(String departamentoOcurrencia) {
		this.departamentoOcurrencia = departamentoOcurrencia;
	}


	public int getEstablecimientoPublico() {
		return establecimientoPublico;
	}


	public void setEstablecimientoPublico(int establecimientoPublico) {
		this.establecimientoPublico = establecimientoPublico;
	}


	public int getEstadoFicha() {
		return estadoFicha;
	}


	public void setEstadoFicha(int estadoFicha) {
		this.estadoFicha = estadoFicha;
	}


	public int getEstadoIngreso() {
		return estadoIngreso;
	}


	public void setEstadoIngreso(int estadoIngreso) {
		this.estadoIngreso = estadoIngreso;
	}


	public boolean isEstaVivo() {
		return estaVivo;
	}


	public void setEstaVivo(boolean estaVivo) {
		this.estaVivo = estaVivo;
	}


	public int getExplosivo() {
		return explosivo;
	}


	public void setExplosivo(int explosivo) {
		this.explosivo = explosivo;
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


	public String getFechaDiligenciamiento() {
		return fechaDiligenciamiento;
	}


	public void setFechaDiligenciamiento(String fechaDiligenciamiento) {
		this.fechaDiligenciamiento = fechaDiligenciamiento;
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


	public String getFechaOcurrencia() {
		return fechaOcurrencia;
	}


	public void setFechaOcurrencia(String fechaOcurrencia) {
		this.fechaOcurrencia = fechaOcurrencia;
	}


	public int getGravedadLesion() {
		return gravedadLesion;
	}


	public void setGravedadLesion(int gravedadLesion) {
		this.gravedadLesion = gravedadLesion;
	}


	public String getHoraDiligenciamiento() {
		return horaDiligenciamiento;
	}


	public void setHoraDiligenciamiento(String horaDiligenciamiento) {
		this.horaDiligenciamiento = horaDiligenciamiento;
	}


	public int getHoraOcurrencia() {
		return horaOcurrencia;
	}


	public void setHoraOcurrencia(int horaOcurrencia) {
		this.horaOcurrencia = horaOcurrencia;
	}


	public boolean isHospitalizado() {
		return hospitalizado;
	}


	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}


	public String getImpresionDiagnostica() {
		return impresionDiagnostica;
	}


	public void setImpresionDiagnostica(String impresionDiagnostica) {
		this.impresionDiagnostica = impresionDiagnostica;
	}


	public int getInmersion() {
		return inmersion;
	}


	public void setInmersion(int inmersion) {
		this.inmersion = inmersion;
	}


	public int getInstitucionAtendio() {
		return institucionAtendio;
	}


	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
	}


	public int getIntoxicacion() {
		return intoxicacion;
	}


	public void setIntoxicacion(int intoxicacion) {
		this.intoxicacion = intoxicacion;
	}


	public int getLesionAccidenteTrabajo() {
		return lesionAccidenteTrabajo;
	}


	public void setLesionAccidenteTrabajo(int lesionAccidenteTrabajo) {
		this.lesionAccidenteTrabajo = lesionAccidenteTrabajo;
	}


	public int getLesionIntencional() {
		return lesionIntencional;
	}


	public void setLesionIntencional(int lesionIntencional) {
		this.lesionIntencional = lesionIntencional;
	}


	public int getLocalidad() {
		return localidad;
	}


	public void setLocalidad(int localidad) {
		this.localidad = localidad;
	}


	public String getLoginUsuario() {
		return loginUsuario;
	}


	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}


	public int getLugarEstudio() {
		return lugarEstudio;
	}


	public void setLugarEstudio(int lugarEstudio) {
		this.lugarEstudio = lugarEstudio;
	}


	public String getLugarNotifica() {
		return lugarNotifica;
	}


	public void setLugarNotifica(String lugarNotifica) {
		this.lugarNotifica = lugarNotifica;
	}


	public String getLugarOcurrencia() {
		return lugarOcurrencia;
	}


	public void setLugarOcurrencia(String lugarOcurrencia) {
		this.lugarOcurrencia = lugarOcurrencia;
	}


	public String getLugarProcedencia() {
		return lugarProcedencia;
	}


	public void setLugarProcedencia(String lugarProcedencia) {
		this.lugarProcedencia = lugarProcedencia;
	}


	public int getLugarTrabajo() {
		return lugarTrabajo;
	}


	public void setLugarTrabajo(int lugarTrabajo) {
		this.lugarTrabajo = lugarTrabajo;
	}


	public int getMordedura() {
		return mordedura;
	}


	public void setMordedura(int mordedura) {
		this.mordedura = mordedura;
	}


	public String getMunicipioNotifica() {
		return municipioNotifica;
	}


	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}


	public String getMunicipioOcurrencia() {
		return municipioOcurrencia;
	}


	public void setMunicipioOcurrencia(String municipioOcurrencia) {
		this.municipioOcurrencia = municipioOcurrencia;
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


	public int getOtracausa() {
		return otracausa;
	}


	public void setOtracausa(int otracausa) {
		this.otracausa = otracausa;
	}


	public int getOtroLugar() {
		return otroLugar;
	}


	public void setOtroLugar(int otroLugar) {
		this.otroLugar = otroLugar;
	}


	public Paciente getPaciente() {
		return paciente;
	}


	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}


	public int getPolvora() {
		return polvora;
	}


	public void setPolvora(int polvora) {
		this.polvora = polvora;
	}


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
	}


	public int getTaberna() {
		return taberna;
	}


	public void setTaberna(int taberna) {
		this.taberna = taberna;
	}


	public int getTipoCaso() {
		return tipoCaso;
	}


	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}


	public int getTipoLesion() {
		return tipoLesion;
	}


	public void setTipoLesion(int tipoLesion) {
		this.tipoLesion = tipoLesion;
	}


	public int getTipoVehiculo() {
		return tipoVehiculo;
	}


	public void setTipoVehiculo(int tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}


	public int getTipoViolencia() {
		return tipoViolencia;
	}


	public void setTipoViolencia(int tipoViolencia) {
		this.tipoViolencia = tipoViolencia;
	}


	public int getViaPublica() {
		return viaPublica;
	}


	public void setViaPublica(int viaPublica) {
		this.viaPublica = viaPublica;
	}


	public int getVivienda() {
		return vivienda;
	}


	public void setVivienda(int vivienda) {
		this.vivienda = vivienda;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
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


	public String getEstadoCivil() {
		return estadoCivil;
	}


	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}


	public String getEtnia() {
		return etnia;
	}


	public void setEtnia(String etnia) {
		this.etnia = etnia;
	}


	public String getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public String getOcupacion() {
		return ocupacion;
	}


	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
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


	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}


	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
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


	public int getEscolaridad() {
		return escolaridad;
	}


	public void setEscolaridad(int escolaridad) {
		this.escolaridad = escolaridad;
	}


	public int getFueraBogota() {
		return fueraBogota;
	}


	public void setFueraBogota(int fueraBogota) {
		this.fueraBogota = fueraBogota;
	}


	public int getTipoAccidente() {
		return tipoAccidente;
	}


	public void setTipoAccidente(int tipoAccidente) {
		this.tipoAccidente = tipoAccidente;
	}


	public String getHoraAtencion() {
		return horaAtencion;
	}


	public void setHoraAtencion(String horaAtencion) {
		this.horaAtencion = horaAtencion;
	}


	public String getAcronimo() {
		return acronimo;
	}


	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}


	public String getNombreDiagnostico() {
		return nombreDiagnostico;
	}


	public void setNombreDiagnostico(String nombreDiagnostico) {
		this.nombreDiagnostico = nombreDiagnostico;
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
