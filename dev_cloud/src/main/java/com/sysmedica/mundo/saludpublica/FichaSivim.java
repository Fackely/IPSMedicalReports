package com.sysmedica.mundo.saludpublica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.mundo.saludpublica.Paciente;
import com.sysmedica.dao.FichaSivimDao;

public class FichaSivim {

	FichaSivimDao fichaSivimDao;
	
	private boolean notificar;
    private String loginUsuario;
    
    //*************************************
    // Informacion general
    private String municipioNotifica;
    private String departamentoNotifica;
    private String lugarNotifica;
    private int institucionAtendio;
    private String fechaNotificacion;
    private String fechaConsultaGeneral;
    private String codigoMunProcedencia;
    private String codigoDepProcedencia;
    private String lugarProcedencia;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int estadoFicha;
    private String nombreProfesional;
    private int codigoFichaSivim;
        
    /*
     * Atributos del paciente
     */
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
    
    private Paciente paciente;
    
    //**************************************
    // DATOS CARA B :
    
    private int escolaridad;
    private boolean mujerGestante;
    private boolean habitanteCalle;
    private boolean personaDiscapacitada;
    private int tipoAtencion;
    private boolean solo;
    private boolean padre;
    private boolean madre;
    private boolean padrastro;
    private boolean madrastra;
    private boolean hermanos;
    private boolean conyuge;
    private boolean hijos;
    private boolean abuelos;
    private boolean otros;
    private boolean violenciaFisica;
    private boolean violenciaEmocional;
    private boolean violenciaSexual;
    private boolean violenciaEconomica;
    private boolean violenciaNegligencia;
    private boolean violenciaAbandono;
    private boolean ocurrioAntesFisica;
    private boolean ocurrioAntesEmocional;
    private boolean ocurrioAntesSexual;
    private boolean ocurrioAntesEconomica;
    private boolean ocurrioAntesNegligencia;
    private boolean ocurrioAntesAbandono;
    private int lugarFisica;
    private int lugarEmocional;
    private int lugarSexual;
    private int lugarEconomica;
    private int lugarNegligencia;
    private int lugarAbandono;
    private String observaciones;
    
    
    /**
     * Inicializa el acceso a Base de Datos de este objeto
     * @param tipoBD
     * @return
     */
    public boolean init(String tipoBD)
	{
			boolean wasInited = false;
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			if (myFactory != null)
			{
				fichaSivimDao = myFactory.getFichaSivimDao();
				wasInited = (fichaSivimDao != null);
			}
			return wasInited;
	}
    
    
    
    public void reset()
    {
    	notificar = false;
        loginUsuario = "";
        
        municipioNotifica = "";
        departamentoNotifica = "";
        lugarNotifica = "";
        institucionAtendio = 0;
        fechaNotificacion = "";
        fechaConsultaGeneral = "";
        codigoMunProcedencia = "";
        codigoDepProcedencia = "";
        lugarProcedencia = "";
        
        primerNombre = "";
        segundoNombre = "";
        primerApellido = "";
        segundoApellido = "";
        departamentoNacimiento = "";
        ciudadNacimiento = "";
        departamentoResidencia = "";
        ciudadResidencia = "";
        direccionPaciente = "";
        telefonoPaciente = "";
        fechaNacimiento = "";
        edad = "";
        genero = "";
        estadoCivil = "";
        documento = "";
        
        paciente = new Paciente();
        
        escolaridad = 0;
        mujerGestante = false;
        habitanteCalle = false;
        personaDiscapacitada = false;
        tipoAtencion = 0;
        solo = false;
        padre = false;
        madre = false;
        padrastro = false;
        madrastra = false;
        hermanos = false;
        conyuge = false;
        hijos = false;
        abuelos = false;
        otros = false;
        violenciaFisica = false;
        violenciaEmocional = false;
        violenciaSexual = false;
        violenciaEconomica = false;
        violenciaNegligencia = false;
        violenciaAbandono = false;
        ocurrioAntesFisica = false;
        ocurrioAntesEmocional = false;
        ocurrioAntesSexual = false;
        ocurrioAntesEconomica = false;
        ocurrioAntesNegligencia = false;
        ocurrioAntesAbandono = false;
        lugarFisica = 0;
        lugarEmocional = 0;
        lugarSexual = 0;
        lugarEconomica = 0;
        lugarNegligencia = 0;
        lugarAbandono = 0;
        observaciones = "";
        
        paciente = new Paciente();
    }
    
    
    public int insertarFicha(Connection con) 
    {
    	return fichaSivimDao.insertarFicha(con,
											loginUsuario,
											codigoPaciente,
											codigoDiagnostico,
											estadoFicha,
											nombreProfesional);
    }
    
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaSivimDao.modificarFicha(con,
											loginUsuario,
										    codigoFichaSivim,
										    estadoFicha,
										    
										    escolaridad,
										    mujerGestante,
										    habitanteCalle,
										    personaDiscapacitada,
										    tipoAtencion,
										    solo,
										    padre,
										    madre,
										    padrastro,
										    madrastra,
										    hermanos,
										    conyuge,
										    hijos,
										    abuelos,
										    otros,
										    violenciaFisica,
										    violenciaEmocional,
										    violenciaSexual,
										    violenciaEconomica,
										    violenciaNegligencia,
										    violenciaAbandono,
										    ocurrioAntesFisica,
										    ocurrioAntesEmocional,
										    ocurrioAntesSexual,
										    ocurrioAntesEconomica,
										    ocurrioAntesNegligencia,
										    ocurrioAntesAbandono,
										    lugarFisica,
										    lugarEmocional,
										    lugarSexual,
										    lugarEconomica,
										    lugarNegligencia,
										    lugarAbandono,
										    observaciones,
										    
										    lugarProcedencia,
										    fechaConsultaGeneral,
										    institucionAtendio);
    }
    
    
    
    public void cargarDatos(Connection con, int codigo) 
    {
    	this.codigoFichaSivim = codigo;
    	
    	ResultSet rs = fichaSivimDao.consultarTodoFichaSivim(con,codigo);
    	
    	try {
    		if (rs.next()) {
    			paciente.setCiudadNacimiento(rs.getString("ciu_nacimiento"));
			    paciente.setCiudadResidencia(rs.getString("ciu_vivienda"));
			    paciente.setDepartamentoNacimiento(rs.getString("dep_nacimiento"));
			    paciente.setDepartamentoResidencia(rs.getString("dep_vivienda"));
			    paciente.setDireccion(rs.getString("direccion_paciente"));
			    paciente.setDocumento(rs.getString("numero_identificacion"));
			    paciente.setEstadoCivil(rs.getString("estado_civil"));
			    paciente.setFechaNacimiento(rs.getString("fecha_nacimiento"));
			    paciente.setGenero(rs.getString("sexo"));
			    paciente.setPrimerApellido(rs.getString("primer_apellido"));
			    paciente.setSegundoApellido(rs.getString("segundo_apellido"));
			    paciente.setPrimerNombre(rs.getString("primer_nombre"));
			    paciente.setSegundoNombre(rs.getString("segundo_nombre"));
			    paciente.setTelefono(rs.getString("telefono_paciente"));
			    
			    paciente.setBarrioResidencia(rs.getString("barrio"));
			    paciente.setZonaDomicilio(rs.getString("zonaDomicilio"));
			    paciente.setOcupacion(rs.getString("ocupacionPaciente"));
			    paciente.setAseguradora(rs.getString("aseguradora"));
			    paciente.setRegimenSalud(rs.getString("regimenSalud"));
			    paciente.setEtnia(rs.getString("etnia"));
			    paciente.setDesplazado(rs.getInt("desplazado"));
			    paciente.setTipoId(rs.getString("tipoId"));
			    paciente.setPaisExpedicion(rs.getString("codigo_pais_id"));
			    paciente.setPaisNacimiento(rs.getString("codigo_pais_nacimiento"));
			    paciente.setPaisResidencia(rs.getString("codigo_pais_vivienda"));
			    
			    this.setEscolaridad(rs.getInt("escolaridad"));
			    this.setMujerGestante(rs.getBoolean("mujerGestante"));
			    this.setHabitanteCalle(rs.getBoolean("habitanteCalle"));
			    this.setPersonaDiscapacitada(rs.getBoolean("personaDiscapacitada"));
			    this.setTipoAtencion(rs.getInt("tipoAtencion"));
			    this.setSolo(rs.getBoolean("solo"));
			    this.setPadre(rs.getBoolean("padre"));
			    this.setMadre(rs.getBoolean("madre"));
			    this.setPadrastro(rs.getBoolean("padrastro"));
			    this.setMadrastra(rs.getBoolean("madrastra"));
			    this.setHermanos(rs.getBoolean("hermanos"));
			    this.setConyuge(rs.getBoolean("conyuge"));
			    this.setHijos(rs.getBoolean("hijos"));
			    this.setAbuelos(rs.getBoolean("abuelos"));
			    this.setOtros(rs.getBoolean("otros"));
			    this.setViolenciaFisica(rs.getBoolean("violenciaFisica"));
			    this.setViolenciaEmocional(rs.getBoolean("violenciaEmocional"));
			    this.setViolenciaSexual(rs.getBoolean("violenciaSexual"));
			    this.setViolenciaEconomica(rs.getBoolean("violenciaEconomica"));
			    this.setViolenciaNegligencia(rs.getBoolean("violenciaNegligencia"));
			    this.setViolenciaAbandono(rs.getBoolean("violenciaAbandono"));
			    this.setOcurrioAntesFisica(rs.getBoolean("ocurrioAntesFisica"));
			    this.setOcurrioAntesEmocional(rs.getBoolean("ocurrioAntesEmocional"));
			    this.setOcurrioAntesSexual(rs.getBoolean("ocurrioAntesSexual"));
			    this.setOcurrioAntesEconomica(rs.getBoolean("ocurrioAntesEconomica"));
			    this.setOcurrioAntesNegligencia(rs.getBoolean("ocurrioAntesNegligencia"));
			    this.setOcurrioAntesAbandono(rs.getBoolean("ocurrioAntesAbandono"));
			    this.setLugarFisica(rs.getInt("lugarFisica"));
			    this.setLugarEmocional(rs.getInt("lugarEmocional"));
			    this.setLugarSexual(rs.getInt("lugarSexual"));
			    this.setLugarEconomica(rs.getInt("lugarEconomica"));
			    this.setLugarNegligencia(rs.getInt("lugarNegligencia"));
			    this.setLugarAbandono(rs.getInt("lugarAbandono"));
			    this.setObservaciones(rs.getString("observaciones"));
			    
			    this.setCodigoMunProcedencia(rs.getString("municipioProcedencia"));
			    this.setCodigoDepProcedencia(rs.getString("departamentoProcedencia"));
			    this.setInstitucionAtendio(rs.getInt("nombreUnidad"));
			    this.setFechaConsultaGeneral(rs.getString("institucionatendio"));
    		}
    	}
    	catch (SQLException sqle) {
    		
    		sqle.printStackTrace();
    	}
    }
    
    
    public FichaSivim() 
    {	
    	reset();
		init(System.getProperty("TIPOBD"));
    }



	public boolean isAbuelos() {
		return abuelos;
	}



	public void setAbuelos(boolean abuelos) {
		this.abuelos = abuelos;
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



	public String getCodigoDepProcedencia() {
		return codigoDepProcedencia;
	}



	public void setCodigoDepProcedencia(String codigoDepProcedencia) {
		this.codigoDepProcedencia = codigoDepProcedencia;
	}



	public String getCodigoMunProcedencia() {
		return codigoMunProcedencia;
	}



	public void setCodigoMunProcedencia(String codigoMunProcedencia) {
		this.codigoMunProcedencia = codigoMunProcedencia;
	}



	public boolean isConyuge() {
		return conyuge;
	}



	public void setConyuge(boolean conyuge) {
		this.conyuge = conyuge;
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



	public String getFechaConsultaGeneral() {
		return fechaConsultaGeneral;
	}



	public void setFechaConsultaGeneral(String fechaConsultaGeneral) {
		this.fechaConsultaGeneral = fechaConsultaGeneral;
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



	public boolean isHabitanteCalle() {
		return habitanteCalle;
	}



	public void setHabitanteCalle(boolean habitanteCalle) {
		this.habitanteCalle = habitanteCalle;
	}



	public boolean isHermanos() {
		return hermanos;
	}



	public void setHermanos(boolean hermanos) {
		this.hermanos = hermanos;
	}



	public boolean isHijos() {
		return hijos;
	}



	public void setHijos(boolean hijos) {
		this.hijos = hijos;
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



	public int getLugarAbandono() {
		return lugarAbandono;
	}



	public void setLugarAbandono(int lugarAbandono) {
		this.lugarAbandono = lugarAbandono;
	}



	public int getLugarEconomica() {
		return lugarEconomica;
	}



	public void setLugarEconomica(int lugarEconomica) {
		this.lugarEconomica = lugarEconomica;
	}



	public int getLugarEmocional() {
		return lugarEmocional;
	}



	public void setLugarEmocional(int lugarEmocional) {
		this.lugarEmocional = lugarEmocional;
	}



	public int getLugarFisica() {
		return lugarFisica;
	}



	public void setLugarFisica(int lugarFisica) {
		this.lugarFisica = lugarFisica;
	}



	public int getLugarNegligencia() {
		return lugarNegligencia;
	}



	public void setLugarNegligencia(int lugarNegligencia) {
		this.lugarNegligencia = lugarNegligencia;
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



	public int getLugarSexual() {
		return lugarSexual;
	}



	public void setLugarSexual(int lugarSexual) {
		this.lugarSexual = lugarSexual;
	}



	public boolean isMadrastra() {
		return madrastra;
	}



	public void setMadrastra(boolean madrastra) {
		this.madrastra = madrastra;
	}



	public boolean isMadre() {
		return madre;
	}



	public void setMadre(boolean madre) {
		this.madre = madre;
	}



	public boolean isMujerGestante() {
		return mujerGestante;
	}



	public void setMujerGestante(boolean mujerGestante) {
		this.mujerGestante = mujerGestante;
	}



	public String getMunicipioNotifica() {
		return municipioNotifica;
	}



	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}



	public boolean isNotificar() {
		return notificar;
	}



	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}



	public String getObservaciones() {
		return observaciones;
	}



	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}



	public boolean isOcurrioAntesAbandono() {
		return ocurrioAntesAbandono;
	}



	public void setOcurrioAntesAbandono(boolean ocurrioAntesAbandono) {
		this.ocurrioAntesAbandono = ocurrioAntesAbandono;
	}



	public boolean isOcurrioAntesEconomica() {
		return ocurrioAntesEconomica;
	}



	public void setOcurrioAntesEconomica(boolean ocurrioAntesEconomica) {
		this.ocurrioAntesEconomica = ocurrioAntesEconomica;
	}



	public boolean isOcurrioAntesEmocional() {
		return ocurrioAntesEmocional;
	}



	public void setOcurrioAntesEmocional(boolean ocurrioAntesEmocional) {
		this.ocurrioAntesEmocional = ocurrioAntesEmocional;
	}



	public boolean isOcurrioAntesFisica() {
		return ocurrioAntesFisica;
	}



	public void setOcurrioAntesFisica(boolean ocurrioAntesFisica) {
		this.ocurrioAntesFisica = ocurrioAntesFisica;
	}



	public boolean isOcurrioAntesNegligencia() {
		return ocurrioAntesNegligencia;
	}



	public void setOcurrioAntesNegligencia(boolean ocurrioAntesNegligencia) {
		this.ocurrioAntesNegligencia = ocurrioAntesNegligencia;
	}



	public boolean isOcurrioAntesSexual() {
		return ocurrioAntesSexual;
	}



	public void setOcurrioAntesSexual(boolean ocurrioAntesSexual) {
		this.ocurrioAntesSexual = ocurrioAntesSexual;
	}



	public boolean isOtros() {
		return otros;
	}



	public void setOtros(boolean otros) {
		this.otros = otros;
	}



	public Paciente getPaciente() {
		return paciente;
	}



	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}



	public boolean isPadrastro() {
		return padrastro;
	}



	public void setPadrastro(boolean padrastro) {
		this.padrastro = padrastro;
	}



	public boolean isPadre() {
		return padre;
	}



	public void setPadre(boolean padre) {
		this.padre = padre;
	}



	public boolean isPersonaDiscapacitada() {
		return personaDiscapacitada;
	}



	public void setPersonaDiscapacitada(boolean personaDiscapacitada) {
		this.personaDiscapacitada = personaDiscapacitada;
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



	public boolean isSolo() {
		return solo;
	}



	public void setSolo(boolean solo) {
		this.solo = solo;
	}



	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}



	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}



	public int getTipoAtencion() {
		return tipoAtencion;
	}



	public void setTipoAtencion(int tipoAtencion) {
		this.tipoAtencion = tipoAtencion;
	}



	public boolean isViolenciaAbandono() {
		return violenciaAbandono;
	}



	public void setViolenciaAbandono(boolean violenciaAbandono) {
		this.violenciaAbandono = violenciaAbandono;
	}



	public boolean isViolenciaEmocional() {
		return violenciaEmocional;
	}



	public void setViolenciaEmocional(boolean violenciaEmocional) {
		this.violenciaEmocional = violenciaEmocional;
	}



	public boolean isViolenciaFisica() {
		return violenciaFisica;
	}



	public void setViolenciaFisica(boolean violenciaFisica) {
		this.violenciaFisica = violenciaFisica;
	}



	public boolean isViolenciaNegligencia() {
		return violenciaNegligencia;
	}



	public void setViolenciaNegligencia(boolean violenciaNegligencia) {
		this.violenciaNegligencia = violenciaNegligencia;
	}



	public boolean isViolenciaSexual() {
		return violenciaSexual;
	}



	public void setViolenciaSexual(boolean violenciaSexual) {
		this.violenciaSexual = violenciaSexual;
	}



	public boolean isViolenciaEconomica() {
		return violenciaEconomica;
	}



	public void setViolenciaEconomica(boolean violenciaEconomica) {
		this.violenciaEconomica = violenciaEconomica;
	}



	public int getCodigoPaciente() {
		return codigoPaciente;
	}



	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}



	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}



	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}



	public int getEstadoFicha() {
		return estadoFicha;
	}



	public void setEstadoFicha(int estadoFicha) {
		this.estadoFicha = estadoFicha;
	}



	public String getNombreProfesional() {
		return nombreProfesional;
	}



	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}



	public int getCodigoFichaSivim() {
		return codigoFichaSivim;
	}



	public void setCodigoFichaSivim(int codigoFichaSivim) {
		this.codigoFichaSivim = codigoFichaSivim;
	}
}
