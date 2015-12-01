package com.sysmedica.mundo.saludpublica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.FichaLesionesDao;

public class FichaLesiones {
	
	private Logger logger = Logger.getLogger(FichaLesiones.class);
	FichaLesionesDao fichaLesionesDao;
	
	private boolean activa;
	
	//*************************************
    // DATOS CARA A :
    
    // Informacion general
    private String sire;
    private String municipioNotifica;
    private String departamentoNotifica;
    private String lugarNotifica;
    private String fechaNotificacion;
    
    // Informacion del paciente
    private int codigoConvenio;
    
    // Notificacion
    private String fechaConsultaGeneral;
    private String codigoMunProcedencia;
    private String codigoDepProcedencia;
    private String lugarProcedencia;
    private String fechaInicioSint;
    private String nombreProfesional;
	
	
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
    private String nombreDiagnostico;
    private String acronimo;
    
    private String horaAtencion;
    
    
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
			fichaLesionesDao = myFactory.getFichaLesionesDao();
			wasInited = (fichaLesionesDao != null);
		}
		return wasInited;
	}
    
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
        fueraBogota = 0;
    }
    
    public FichaLesiones() {
    	
    	reset();
        init(System.getProperty("TIPOBD"));
    }
    
    
    public int insertarFicha(Connection con) {
    	
    	return fichaLesionesDao.insertarFicha(con,
											notificar,
										    loginUsuario,
										    codigoFichaLesiones,
										    codigoPaciente,
										    codigoDiagnostico,
										    codigoNotificacion,
										    numeroSolicitud,
										    estadoFicha,
										    codigoConvenio,
											nombreProfesional);
    }
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaLesionesDao.modificarFicha(con,
												sire,
												notificar,
											    loginUsuario,
											    codigoFichaLesiones,
											    codigoPaciente,
											    codigoDiagnostico,
											    codigoNotificacion,
											    numeroSolicitud,
											    estadoFicha,
											    
											    estadoIngreso,
											    fechaOcurrencia,
											    horaOcurrencia,
											    lugarOcurrencia,
											    localidad,
											    municipioOcurrencia,
											    departamentoOcurrencia,
											    armaFuego,
											    armaCortopunzante,
											    armaContundente,
											    asfixia,
											    intoxicacion,
											    inmersion,
											    explosivo,
											    polvora,
											    otracausa,
											    caida,
											    mordedura,
											    vivienda,
											    lugarTrabajo,
											    lugarEstudio,
											    taberna,
											    establecimientoPublico,
											    viaPublica,
											    otroLugar,
											    lesionAccidenteTrabajo,
											    codigoArp,
											    actividadDuranteHecho,
											    lesionIntencional,
											    tipoLesion,
											    tipoVehiculo,
											    condicionLesionado,
											    tipoViolencia,
											    agresor,
											    denunciado,
											    consumoAlcohol,
											    consumoOtrasSustancias,
											    impresionDiagnostica,
											    gravedadLesion,
											    institucionAtendio,
											    fueraBogota);
    }
    
    
    
    public void cargarDatos(Connection con, int codigo) {
    	
    	this.codigoFichaLesiones = codigo;
    	
    	ResultSet rs = fichaLesionesDao.consultaTodoFicha(con,codigo);
    	
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
			    			    
			    this.setEstadoFicha(rs.getInt("estado"));
			    this.setEstadoIngreso(rs.getInt("estadoIngreso"));
			    this.setFechaOcurrencia(rs.getString("fechaOcurrencia"));
			    this.setHoraOcurrencia(rs.getInt("horaOcurrencia"));
			    this.setLugarOcurrencia(rs.getString("lugarOcurrencia"));
			    this.setLocalidad(rs.getInt("localidad"));
			    this.setMunicipioOcurrencia(rs.getString("municipioOcurrencia"));
			    this.setDepartamentoOcurrencia(rs.getString("departamentoOcurrencia"));
			    this.setArmaFuego(rs.getInt("armaFuego"));
			    this.setArmaCortopunzante(rs.getInt("armaCortopunzante"));
			    this.setArmaContundente(rs.getInt("armaContundente"));
			    this.setAsfixia(rs.getInt("asfixia"));
			    this.setIntoxicacion(rs.getInt("intoxicacion"));
			    this.setInmersion(rs.getInt("inmersion"));
			    this.setExplosivo(rs.getInt("explosivo"));
			    this.setPolvora(rs.getInt("polvora"));
			    this.setOtracausa(rs.getInt("otracausa"));
			    this.setCaida(rs.getInt("caida"));
			    this.setMordedura(rs.getInt("mordedura"));
			    this.setVivienda(rs.getInt("vivienda"));
			    this.setLugarTrabajo(rs.getInt("lugarTrabajo"));
			    this.setLugarEstudio(rs.getInt("lugarEstudio"));
			    this.setTaberna(rs.getInt("taberna"));
			    this.setEstablecimientoPublico(rs.getInt("establecimientoPublico"));
			    this.setViaPublica(rs.getInt("viaPublica"));
			    this.setOtroLugar(rs.getInt("otroLugar"));
			    this.setLesionAccidenteTrabajo(rs.getInt("lesionAccidenteTrabajo"));
			    this.setCodigoArp(rs.getInt("codigoArp"));
			    this.setActividadDuranteHecho(rs.getInt("actividadDuranteHecho"));
			    this.setLesionIntencional(rs.getInt("lesionIntencional"));
			    this.setTipoLesion(rs.getInt("tipoLesion"));
			    this.setTipoVehiculo(rs.getInt("tipoVehiculo"));
			    this.setCondicionLesionado(rs.getInt("condicionLesionado"));
			    this.setTipoViolencia(rs.getInt("tipoViolencia"));
			    this.setAgresor(rs.getInt("agresor"));
			    this.setDenunciado(rs.getInt("denunciado"));
			    this.setConsumoAlcohol(rs.getInt("consumoAlcohol"));
			    this.setConsumoOtrasSustancias(rs.getInt("consumoOtrasSustancias"));
			    this.setImpresionDiagnostica(rs.getString("impresionDiagnostica"));
			    this.setGravedadLesion(rs.getInt("gravedadLesion"));
			    this.setInstitucionAtendio(rs.getInt("nombreUnidad"));
			    this.setFechaNotificacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaDiligenciamiento")));
			    this.setNombreProfesional(rs.getString("nombreProfesional"));
			    this.setFueraBogota(rs.getInt("fueraBogota"));
			    this.setHoraAtencion(rs.getString("horaDiligenciamiento"));
			    this.setAcronimo(rs.getString("acronimo"));
			    this.setNombreDiagnostico(rs.getString("nombreDiagnostico"));
			    
			    try {
			    	this.setEscolaridad(rs.getInt("escolaridad"));
			    }
			    catch (NullPointerException npe) {
			    	this.setEscolaridad(0);
			    }
    		}
    	}
    	catch (SQLException sqle) {}
    }
    
    
    
    public void cargarPaciente(Connection con, int codigo) {
    	
    	ResultSet rs = fichaLesionesDao.consultaDatosPaciente(con,codigo);
    	
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
			    paciente.setAseguradora(rs.getString("aseguradora"));
			    paciente.setRegimenSalud(rs.getString("regimenSalud"));
			    paciente.setEtnia(rs.getString("etnia"));
			//    paciente.setDesplazado(rs.getInt("desplazado"));
			    paciente.setTipoId(rs.getString("tipoId"));
			    paciente.setPaisExpedicion(rs.getString("codigo_pais_id"));
			    paciente.setPaisNacimiento(rs.getString("codigo_pais_nacimiento"));
			    paciente.setPaisResidencia(rs.getString("codigo_pais_vivienda"));
			    
    		}
    	}
    	catch (SQLException sqle) {
            
        }
    }

	
    
    
    public int insertarFichaCompleta(Connection con)
    {
    	return fichaLesionesDao.insertarFichaCompleta(con,
														numeroSolicitud,
														loginUsuario,
														codigoPaciente,
														codigoDiagnostico,
														estadoFicha,
														codigoConvenio,
														nombreProfesional,
													    
													    estadoIngreso,
													    fechaOcurrencia,
													    horaOcurrencia,
													    lugarOcurrencia,
													    localidad,
													    municipioOcurrencia,
													    departamentoOcurrencia,
													    armaFuego,
													    armaCortopunzante,
													    armaContundente,
													    asfixia,
													    intoxicacion,
													    inmersion,
													    explosivo,
													    polvora,
													    otracausa,
													    caida,
													    mordedura,
													    vivienda,
													    lugarTrabajo,
													    lugarEstudio,
													    taberna,
													    establecimientoPublico,
													    viaPublica,
													    otroLugar,
													    lesionAccidenteTrabajo,
													    codigoArp,
													    actividadDuranteHecho,
													    lesionIntencional,
													    tipoLesion,
													    tipoVehiculo,
													    condicionLesionado,
													    tipoViolencia,
													    agresor,
													    denunciado,
													    consumoAlcohol,
													    consumoOtrasSustancias,
													    impresionDiagnostica,
													    gravedadLesion,
													    institucionAtendio,
													    fueraBogota,
													    activa);
    }
    
	public static int getCodigoGlobal() {
		return codigoGlobal;
	}
	public static void setCodigoGlobal(int codigoGlobal) {
		FichaLesiones.codigoGlobal = codigoGlobal;
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

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}
}
