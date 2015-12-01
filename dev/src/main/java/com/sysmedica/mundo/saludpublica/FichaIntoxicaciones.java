package com.sysmedica.mundo.saludpublica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.FichaIntoxicacionesDao;

public class FichaIntoxicaciones {

	private Logger logger = Logger.getLogger(FichaSifilis.class);
	
	private boolean activa;
	
	private boolean notificar;
    private String loginUsuario;
    
    private Paciente paciente;
    
    //*************************************
    // DATOS CARA A :
    
    // Informacion general
    private String sire;
    private String municipioNotifica;
    private String departamentoNotifica;
    private String lugarNotifica;
    private int institucionAtendio;
    private String fechaNotificacion;
    
    private String pais;
    private int areaProcedencia;
    
    // Informacion del paciente
    private int codigoConvenio;
    
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
    
    FichaIntoxicacionesDao fichaIntoxicacionesDao;
    
    public FichaIntoxicaciones()
    {
    	reset();
        init(System.getProperty("TIPOBD"));
    }
    
    
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
        alerta = 0;
        investigacion = 0;
        fechaInvestigacion = "";
        fechaInforma = "";
        nombreResponsable = "";
        telefonoResponsable = "";
        observaciones = "";
        
        paciente = new Paciente();
    }
    
    
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
			fichaIntoxicacionesDao = myFactory.getFichaIntoxicacionesDao();
			wasInited = (fichaIntoxicacionesDao != null);
		}
		return wasInited;
	}
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaIntoxicacionesDao.insertarFichaCompleta(con,
												    		numeroSolicitud,
															loginUsuario,
															codigoPaciente,
															codigoDiagnostico,
															estadoFicha,
															codigoConvenio,
															nombreProfesional,
														    
														    sire,
															notificar,
															
															codigoFichaIntoxicaciones,										    
														    lugarProcedencia,
														    fechaConsultaGeneral,
														    fechaInicioSint,
														    tipoCaso,
														    hospitalizado,
														    fechaHospitalizacion,
														    estaVivo,
														    fechaDefuncion,
														    lugarNotifica,
														    institucionAtendio,
														    
														    tipoIntoxicacion,
														    nombreProducto,
														    tipoExposicion,
														    produccion,
														    almacenamiento,
														    agricola,
														    saludPublica,
														    domiciliaria,
														    tratHumano,
														    tratVeterinario,
														    transporte,
														    mezcla,
														    mantenimiento,
														    cultivo,
														    otros,
														    otraActividad,
														    fechaExposicion,
														    horaExposicion,
														    viaExposicion,
														    otraViaExposicion,
														    escolaridad,
														    embarazada,
														    vinculoLaboral,
														    afiliadoArp,
														    nombreArp,
														    codgoArp,
														    estCivil,
														    alerta,
														    investigacion,
														    fechaInvestigacion,
														    fechaInforma,
														    nombreResponsable,
														    telefonoResponsable,
														    observaciones,
														    
														    activa,
														    pais,
														    areaProcedencia);
    }
    
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaIntoxicacionesDao.modificarFicha(con,
														sire,
														notificar,
													    loginUsuario,
													    codigoFichaIntoxicaciones,
													    codigoPaciente,
													    codigoDiagnostico,
													    codigoNotificacion,
													    numeroSolicitud,
													    estadoFicha,
													    
													    lugarProcedencia,
													    fechaConsultaGeneral,
													    fechaInicioSint,
													    tipoCaso,
													    hospitalizado,
													    fechaHospitalizacion,
													    estaVivo,
													    fechaDefuncion,
													    lugarNotifica,
													    institucionAtendio,
													    
													    tipoIntoxicacion,
													    nombreProducto,
													    tipoExposicion,
													    produccion,
													    almacenamiento,
													    agricola,
													    saludPublica,
													    domiciliaria,
													    tratHumano,
													    tratVeterinario,
													    transporte,
													    mezcla,
													    mantenimiento,
													    cultivo,
													    otros,
													    otraActividad,
													    fechaExposicion,
													    horaExposicion,
													    viaExposicion,
													    otraViaExposicion,
													    escolaridad,
													    embarazada,
													    vinculoLaboral,
													    afiliadoArp,
													    nombreArp,
													    codgoArp,
													    estCivil,
													    alerta,
													    investigacion,
													    fechaInvestigacion,
													    fechaInforma,
													    nombreResponsable,
													    telefonoResponsable,
													    observaciones,
													    pais,
													    areaProcedencia);
    }
    
    
    
    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaIntoxicacionesDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    	
    	try {
    		
    		if (rs.next()) {
    			
			    paciente.setCiudadResidencia(rs.getString("ciu_vivienda"));
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
			    paciente.setEtnia(rs.getString("etnia"));
			    
			    String grupoPoblacional = rs.getString("grupoPoblacional");
			    int desplazado = 0;
			    
			    if (grupoPoblacional.equals(ConstantesIntegridadDominio.acronimoDesplazados)) {
			    	desplazado = 1;
			    }
			    
			    paciente.setDesplazado(desplazado);
			    paciente.setGrupoPoblacional(ValoresPorDefecto.getIntegridadDominio(grupoPoblacional).toString());
			    
			    paciente.setTipoId(rs.getString("tipoId"));
			    paciente.setPaisExpedicion(rs.getString("codigo_pais_id"));
			    paciente.setPaisNacimiento(rs.getString("codigo_pais_nacimiento"));
			    paciente.setPaisResidencia(rs.getString("codigo_pais_vivienda"));
			    
			    if (empezarnuevo==false) {
				    paciente.setAseguradora(rs.getString("aseguradora"));
				    paciente.setRegimenSalud(rs.getString("regimenSalud"));
			    }
			    
    		}
    	}
    	catch (SQLException sqle) {
            
    		sqle.printStackTrace();
        }
    }
    
    
    public void cargarDatos(Connection con, int codigo) 
    {
    	this.codigoFichaIntoxicaciones = codigo;
    	
    	ResultSet rs = fichaIntoxicacionesDao.consultaTodoFicha(con,codigo);
    	
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
			    
			    
			    String grupoPoblacional = rs.getString("grupoPoblacional");
			    
			    paciente.setGrupoPoblacional(ValoresPorDefecto.getIntegridadDominio(grupoPoblacional).toString());
			    paciente.setTipoId(rs.getString("tipoId"));
			    paciente.setPaisExpedicion(rs.getString("codigo_pais_id"));
			    paciente.setPaisNacimiento(rs.getString("codigo_pais_nacimiento"));
			    paciente.setPaisResidencia(rs.getString("codigo_pais_vivienda"));
			    
			    this.setSire(rs.getString("sire"));
			    this.setEstadoFicha(rs.getInt("estado"));
			    
			    this.setTipoIntoxicacion(rs.getInt("tipoIntoxicacion"));
			    this.setNombreProducto(rs.getString("nombreProducto"));
			    this.setTipoExposicion(rs.getInt("tipoExposicion"));
			    this.setProduccion(rs.getInt("produccion"));
			    this.setAlmacenamiento(rs.getInt("almacenamiento"));
			    this.setAgricola(rs.getInt("agricola"));
			    this.setSaludPublica(rs.getInt("saludPublica"));
			    this.setDomiciliaria(rs.getInt("domiciliaria"));
			    this.setTratHumano(rs.getInt("tratHumano"));
			    this.setTratVeterinario(rs.getInt("tratVeterinario"));
			    this.setTransporte(rs.getInt("transporte"));
			    this.setMezcla(rs.getInt("mezcla"));
			    this.setMantenimiento(rs.getInt("mantenimiento"));
			    this.setCultivo(rs.getInt("cultivo"));
			    this.setOtros(rs.getInt("otros"));
			    this.setOtraActividad(rs.getString("otraActividad"));
			    this.setFechaExposicion(rs.getString("fechaExposicion"));
			    this.setHoraExposicion(rs.getInt("horaExposicion"));
			    this.setViaExposicion(rs.getInt("viaExposicion"));
			    this.setOtraViaExposicion(rs.getString("otraViaExposicion"));
			    this.setEscolaridad(rs.getInt("escolaridad"));
			    this.setEmbarazada(rs.getInt("embarazada"));
			    this.setVinculoLaboral(rs.getInt("vinculoLaboral"));
			    this.setAfiliadoArp(rs.getInt("afiliadoArp"));
			    this.setNombreArp(rs.getString("nombreArp"));
			    this.setCodgoArp(rs.getInt("codgoArp"));
			    this.setAlerta(rs.getInt("alerta"));
			    this.setInvestigacion(rs.getInt("investigacion"));
			    this.setFechaInvestigacion(rs.getString("fechaInvestigacion"));
			    this.setFechaInforma(rs.getString("fechaInforma"));
			    this.setNombreResponsable(rs.getString("nombreResponsable"));
			    this.setTelefonoResponsable(rs.getString("telefonoResponsable"));
			    
			    this.setDepartamentoNotifica(rs.getString("departamentoNotifica"));
			    this.setMunicipioNotifica(rs.getString("municipioNotifica"));
			    this.setInstitucionAtendio(rs.getInt("nombreUnidad"));
			    this.setFechaConsultaGeneral(rs.getString("fechaConsultaGeneral"));
			    this.setCodigoMunProcedencia(rs.getString("municipioProcedencia"));
			    this.setCodigoDepProcedencia(rs.getString("departamentoProcedencia"));
			    this.setFechaInicioSint(rs.getString("fechaInicioSintomas"));
			    this.setTipoCaso(rs.getInt("tipoCaso"));
			    this.setHospitalizado(rs.getBoolean("hospitalizado"));
			    this.setFechaHospitalizacion(rs.getString("fechaHospitalizacion"));
			    this.setEstaVivo(rs.getBoolean("condicionFinal"));
			    this.setFechaDefuncion(rs.getString("fechaDefuncion"));
			    this.setNombreProfesional(rs.getString("nombreProfesional"));
			    this.setPais(rs.getString("pais"));
			    this.setAreaProcedencia(rs.getInt("areaProcedencia"));
			    this.setFechaNotificacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaDiligenciamiento")));
    		}
    	}
    	catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
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
	public int getCodigoFichaIntoxicaciones() {
		return codigoFichaIntoxicaciones;
	}
	public void setCodigoFichaIntoxicaciones(int codigoFichaIntoxicaciones) {
		this.codigoFichaIntoxicaciones = codigoFichaIntoxicaciones;
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
	public int getTipoExposicion() {
		return tipoExposicion;
	}
	public void setTipoExposicion(int tipoExposicion) {
		this.tipoExposicion = tipoExposicion;
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
	public int getViaExposicion() {
		return viaExposicion;
	}
	public void setViaExposicion(int viaExposicion) {
		this.viaExposicion = viaExposicion;
	}


	public int getAreaProcedencia() {
		return areaProcedencia;
	}


	public void setAreaProcedencia(int areaProcedencia) {
		this.areaProcedencia = areaProcedencia;
	}


	public String getPais() {
		return pais;
	}


	public void setPais(String pais) {
		this.pais = pais;
	}


	public Paciente getPaciente() {
		return paciente;
	}


	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
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
    
    
}
