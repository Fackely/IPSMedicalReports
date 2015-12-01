package com.sysmedica.mundo.saludpublica;

import org.apache.log4j.Logger;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.mundo.saludpublica.Paciente;
import com.sysmedica.dao.FichaTetanosDao;


public class FichaTetanos {

	private Logger logger = Logger.getLogger(FichaTetanos.class);
	
	private boolean activa;
	
	private boolean notificar;
    private String loginUsuario;
    
    //*************************************
    // DATOS CARA A :
    
    // Informacion general
    private String sire;
    private String municipioNotifica;
    private String departamentoNotifica;
    private String lugarNotifica;
    private int institucionAtendio;
    private String fechaNotificacion;
    
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
    
    private int codigoFichaTetanos;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
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
    
    private String nombreMadre;
    private int edadMadre;
    private String fechaNacimientoMadre;
    private String fechaEgresoHospital;
    private boolean nacimientoTraumatico;
    private boolean llantoNacer;
    private boolean mamabaNormal;
    private boolean dejoMamar;
    private String fechaDejo;
    private boolean dificultadRespiratoria;
    private boolean episodiosApnea;
    private boolean hipotermia;
    private boolean hipertermia;
    private boolean fontAbombada;
    private boolean rigidezNuca;
    private boolean trismus;
    private boolean convulsiones;
    private boolean espasmos;
    private boolean contracciones;
    private boolean opistotonos;
    private boolean llantoExcesivo;
    private boolean sepsisUmbilical;
    private int numeroEmbarazos;
    private boolean asistioControl;
    private String explicacionNoAsistencia;
    private boolean atendidoPorMedico;
    private boolean atendidoPorEnfermero;
    private boolean atendidoPorAuxiliar;
    private boolean atendidoPorPromotor;
    private boolean atendidoPorOtro;
    private String quienAtendio;
    private int numeroControlesPrevios;
    private String fechaUltimoControl;
    private boolean madreVivioMismoLugar;
    private String codigoMunicipioVivienda;
    private String codigoDepartamentoVivienda;
    private boolean antecedenteVacunaAnti;
    private int dosisDpt;
    private String explicacionNoVacuna;
    private String fechaDosisTd1;
    private String fechaDosisTd2;
    private String fechaDosisTd3;
    private String fechaDosisTd4;
    private int lugarParto;
    private String institucionParto;
    private String fechaIngresoParto;
    private String fechaEgresoParto;
    private int quienAtendioParto;
    private String instrumentoCordon;
    private String metodoEsterilizacion;
    private boolean recibioInformacionMunon;
    private boolean aplicacionSustanciasMunon;
    private String cualesSustancias;
    private int distanciaMinutos;
    private String fechaInvestigacionCampo;
    private String fechaVacunacion;
    private int dosisTd1AplicadasMef;
    private int dosisTd2AplicadasMef;
    private int dosisTd3AplicadasMef;
    private int dosisTd4AplicadasMef;
    private int dosisTd5AplicadasMef;
    private int dosisTd1AplicadasGest;
    private int dosisTd2AplicadasGest;
    private int dosisTd3AplicadasGest;
    private int dosisTd4AplicadasGest;
    private int dosisTd5AplicadasGest;
    private int coberturaLograda;
    private String pais;
    private int areaProcedencia;
    
    private String lugarVivienda;
    
    private Paciente paciente;
    
    FichaTetanosDao fichaTetanosDao;
    
    public static int codigoGlobal;
    
    public FichaTetanos() {
		
		reset();
		init(System.getProperty("TIPOBD"));
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
				fichaTetanosDao = myFactory.getFichaTetanosDao();
				wasInited = (fichaTetanosDao != null);
			}
			return wasInited;
	}
    
    
    public void reset() {
    	
    	sire = "";
        nombreMadre = "";
        fechaNacimientoMadre = "";
        fechaEgresoHospital = "";
        nacimientoTraumatico = false;
        llantoNacer = false;
        mamabaNormal = false;
        dejoMamar = false;
        fechaDejo = "";
        dificultadRespiratoria = false;
        episodiosApnea = false;
        hipotermia = false;
        hipertermia = false;
        fontAbombada = false;
        rigidezNuca = false;
        trismus = false;
        convulsiones = false;
        espasmos = false;
        contracciones = false;
        opistotonos = false;
        llantoExcesivo = false;
        sepsisUmbilical = false;
        
        asistioControl = false;
        explicacionNoAsistencia = "";
        atendidoPorMedico = false;
        atendidoPorEnfermero = false;
        atendidoPorAuxiliar = false;
        atendidoPorPromotor = false;
        atendidoPorOtro = false;
        quienAtendio = "";
        
        fechaUltimoControl = "";
        madreVivioMismoLugar = false;
        
        
        antecedenteVacunaAnti = false;
        
        explicacionNoVacuna = "";
        fechaDosisTd1 = "";
        fechaDosisTd2 = "";
        fechaDosisTd3 = "";
        fechaDosisTd4 = "";
        lugarParto = 0;
        institucionParto = "";
        fechaIngresoParto = "";
        fechaEgresoParto = "";
        quienAtendioParto = 0;
        instrumentoCordon = "";
        metodoEsterilizacion = "";
        recibioInformacionMunon = false;
        aplicacionSustanciasMunon = false;
        cualesSustancias = "";
        
        distanciaMinutos = 0;
        fechaInvestigacionCampo = "";
        fechaVacunacion = "";
        dosisTd1AplicadasMef = 0;
        dosisTd2AplicadasMef = 0;
        dosisTd3AplicadasMef = 0;
        dosisTd4AplicadasMef = 0;
        dosisTd5AplicadasMef = 0;
        dosisTd1AplicadasGest = 0;
        dosisTd2AplicadasGest = 0;
        dosisTd3AplicadasGest = 0;
        dosisTd4AplicadasGest = 0;
        dosisTd5AplicadasGest = 0;
        coberturaLograda = 0;
        
        fechaInvestigacionCampo = "";
        fechaVacunacion = "";
        
        numeroEmbarazos = 0;
        dosisDpt = 0;
        
        paciente = new Paciente();
        
        /*
        private int dosisTd1AplicadasMef;
        private int dosisTd2AplicadasMef;
        private int dosisTd3AplicadasMef;
        private int dosisTd4AplicadasMef;
        private int dosisTd5AplicadasMef;
        private int dosisTd1AplicadasGest;
        private int dosisTd21AplicadasGest;
        private int dosisTd3AplicadasGest;
        private int dosisTd4AplicadasGest;
        private int dosisTd5AplicadasGest;
        private int coberturaLograda;
        */
    }
    
    
    
    public int insertarFicha(Connection con) {
    	
    	return fichaTetanosDao.insertarFicha(con,
												notificar,
											    loginUsuario,
											    codigoFichaTetanos,
											    codigoPaciente,
											    codigoDiagnostico,
											    codigoNotificacion,
											    numeroSolicitud,
											    codigoConvenio,
												nombreProfesional,
											    estadoFicha);
    }
    
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaTetanosDao.modificarFicha(con,
												sire,
												loginUsuario,
											    codigoFichaTetanos,
											    estadoFicha,
											    
											    nombreMadre,
											    edadMadre,
											    fechaNacimientoMadre,
											    fechaEgresoHospital,
											    nacimientoTraumatico,
											    llantoNacer,
											    mamabaNormal,
											    dejoMamar,
											    fechaDejo,
											    dificultadRespiratoria,
											    episodiosApnea,
											    hipotermia,
											    hipertermia,
											    fontAbombada,
											    rigidezNuca,
											    trismus,
											    convulsiones,
											    espasmos,
											    contracciones,
											    opistotonos,
											    llantoExcesivo,
											    sepsisUmbilical,
											    numeroEmbarazos,
											    asistioControl,
											    explicacionNoAsistencia,
											    atendidoPorMedico,
											    atendidoPorEnfermero,
											    atendidoPorAuxiliar,
											    atendidoPorPromotor,
											    atendidoPorOtro,
											    quienAtendio,
											    numeroControlesPrevios,
											    fechaUltimoControl,
											    madreVivioMismoLugar,
											    codigoMunicipioVivienda,
											    codigoDepartamentoVivienda,
											    lugarVivienda,
											    antecedenteVacunaAnti,
											    dosisDpt,
											    explicacionNoVacuna,
											    fechaDosisTd1,
											    fechaDosisTd2,
											    fechaDosisTd3,
											    fechaDosisTd4,
											    lugarParto,
											    institucionParto,
											    fechaIngresoParto,
											    fechaEgresoParto,
											    quienAtendioParto,
											    instrumentoCordon,
											    metodoEsterilizacion,
											    recibioInformacionMunon,
											    aplicacionSustanciasMunon,
											    cualesSustancias,
											    distanciaMinutos,
											    fechaInvestigacionCampo,
											    fechaVacunacion,
											    dosisTd1AplicadasMef,
											    dosisTd2AplicadasMef,
											    dosisTd3AplicadasMef,
											    dosisTd4AplicadasMef,
											    dosisTd5AplicadasMef,
											    dosisTd1AplicadasGest,
											    dosisTd2AplicadasGest,
											    dosisTd3AplicadasGest,
											    dosisTd4AplicadasGest,
											    dosisTd5AplicadasGest,
											    coberturaLograda,
											    
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
											    pais,
											    areaProcedencia
											    );
    }
    
    
    
    public void cargarDatos(Connection con, int codigo) 
    {
    	this.codigoFichaTetanos = codigo;
    	
    	ResultSet rs = fichaTetanosDao.consultaTodoFicha(con,codigo);
    	
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
			    
			    this.setSire(rs.getString("sire"));
			    this.setNombreMadre(rs.getString("nombreMadre"));
			    this.setEdadMadre(rs.getInt("edadMadre"));
			    this.setFechaNacimientoMadre(rs.getString("fechaNacimiento"));
			    this.setFechaEgresoHospital(rs.getString("fechaEgresoHospital"));
			    this.setNacimientoTraumatico(rs.getBoolean("nacimientoTraumatico"));
			    this.setLlantoNacer(rs.getBoolean("llantoNacer"));
			    this.setMamabaNormal(rs.getBoolean("mamabaNormal"));
			    this.setDejoMamar(rs.getBoolean("dejoMamar"));
			    this.setFechaDejo(rs.getString("fechaDejo"));
			    this.setDificultadRespiratoria(rs.getBoolean("dificultadRespiratoria"));
			    this.setEpisodiosApnea(rs.getBoolean("episodiosApnea"));
			    this.setHipotermia(rs.getBoolean("hipotermia"));
			    this.setHipertermia(rs.getBoolean("hipertermia"));
			    this.setFontAbombada(rs.getBoolean("fontAbombada"));
			    this.setRigidezNuca(rs.getBoolean("rigidezNuca"));
			    this.setTrismus(rs.getBoolean("trismus"));
			    this.setConvulsiones(rs.getBoolean("convulsiones"));
			    this.setEspasmos(rs.getBoolean("espasmos"));
			    this.setContracciones(rs.getBoolean("contracciones"));
			    this.setOpistotonos(rs.getBoolean("opistotonos"));
			    this.setLlantoExcesivo(rs.getBoolean("llantoExcesivo"));
			    this.setSepsisUmbilical(rs.getBoolean("sepsisUmbilical"));
			    this.setNumeroEmbarazos(rs.getInt("numeroEmbarazos"));
			    this.setAsistioControl(rs.getBoolean("asistioControl"));
			    this.setExplicacionNoAsistencia(rs.getString("explicacionNoAsistencia"));
			    this.setAtendidoPorMedico(rs.getBoolean("atendidoPorMedico"));
			    this.setAtendidoPorEnfermero(rs.getBoolean("atendidoPorEnfermero"));
			    this.setAtendidoPorAuxiliar(rs.getBoolean("atendidoPorAuxiliar"));
			    this.setAtendidoPorPromotor(rs.getBoolean("atendidoPorPromotor"));
			    this.setAtendidoPorOtro(rs.getBoolean("atendidoPorOtro"));
			    this.setQuienAtendio(rs.getString("quienAtendio"));
			    this.setNumeroControlesPrevios(rs.getInt("numeroControlesPrevios"));
			    this.setFechaUltimoControl(rs.getString("fechaUltimoControl"));
			    this.setMadreVivioMismoLugar(rs.getBoolean("madreVivioMismoLugar"));
			    this.setCodigoMunicipioVivienda(rs.getString("codigoMunicipioVivienda"));
			    this.setCodigoDepartamentoVivienda(rs.getString("codigoDepVivienda"));
			    this.setAntecedenteVacunaAnti(rs.getBoolean("antecedenteVacunaAnti"));
			    this.setDosisDpt(rs.getInt("dosisDpt"));
			    this.setExplicacionNoVacuna(rs.getString("explicacionNoVacuna"));
			    this.setFechaDosisTd1(rs.getString("fechaDosisTd1"));
			    this.setFechaDosisTd2(rs.getString("fechaDosisTd2"));
			    this.setFechaDosisTd3(rs.getString("fechaDosisTd3"));
			    this.setFechaDosisTd4(rs.getString("fechaDosisTd4"));
			    this.setLugarParto(rs.getInt("lugarParto"));
			    this.setInstitucionParto(rs.getString("institucionParto"));
			    this.setFechaIngresoParto(rs.getString("fechaIngresoParto"));
			    this.setFechaEgresoParto(rs.getString("fechaEgresoParto"));
			    this.setQuienAtendioParto(rs.getInt("quienAtendioParto"));
			    this.setInstrumentoCordon(rs.getString("instrumentoCordon"));
			    this.setMetodoEsterilizacion(rs.getString("metodoEsterilizacion"));
			    this.setRecibioInformacionMunon(rs.getBoolean("recibioInformacionMunon"));
			    this.setAplicacionSustanciasMunon(rs.getBoolean("aplicacionSustanciasMunon"));
			    this.setCualesSustancias(rs.getString("cualesSustancias"));
			    this.setDistanciaMinutos(rs.getInt("distanciaMinutos"));
			    this.setFechaInvestigacionCampo(rs.getString("fechaInvestigacionCampo"));
			    this.setFechaVacunacion(rs.getString("fechaVacunacion"));
			    this.setDosisTd1AplicadasMef(rs.getInt("dosisTd1AplicadasMef"));
			    this.setDosisTd2AplicadasMef(rs.getInt("dosisTd2AplicadasMef"));
			    this.setDosisTd3AplicadasMef(rs.getInt("dosisTd3AplicadasMef"));
			    this.setDosisTd4AplicadasMef(rs.getInt("dosisTd4AplicadasMef"));
			    this.setDosisTd5AplicadasMef(rs.getInt("dosisTd5AplicadasMef"));
			    this.setDosisTd1AplicadasGest(rs.getInt("dosisTd1AplicadasGest"));
			    this.setDosisTd2AplicadasGest(rs.getInt("dosisTd2AplicadasGest"));
			    this.setDosisTd3AplicadasGest(rs.getInt("dosisTd3AplicadasGest"));
			    this.setDosisTd4AplicadasGest(rs.getInt("dosisTd4AplicadasGest"));
			    this.setDosisTd5AplicadasGest(rs.getInt("dosisTd5AplicadasGest"));
			    this.setCoberturaLograda(rs.getInt("coberturaLograda"));
			    this.setPais(rs.getString("pais"));
			    this.setAreaProcedencia(rs.getInt("areaProcedencia"));
			    
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
			    this.setFechaNotificacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaDiligenciamiento")));
    		}
    	}
    	catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
    }
    
    
    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaTetanosDao.consultaDatosPaciente(con,codigo,empezarnuevo);
    	
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
            
        }
    }
    
    
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaTetanosDao.insertarFichaCompleta(con,
														numeroSolicitud,
														loginUsuario,
														codigoPaciente,
														codigoDiagnostico,
														estadoFicha,
														codigoConvenio,
														nombreProfesional,
													    
													    sire,
													    
													    nombreMadre,
													    edadMadre,
													    fechaNacimientoMadre,
													    fechaEgresoHospital,
													    nacimientoTraumatico,
													    llantoNacer,
													    mamabaNormal,
													    dejoMamar,
													    fechaDejo,
													    dificultadRespiratoria,
													    episodiosApnea,
													    hipotermia,
													    hipertermia,
													    fontAbombada,
													    rigidezNuca,
													    trismus,
													    convulsiones,
													    espasmos,
													    contracciones,
													    opistotonos,
													    llantoExcesivo,
													    sepsisUmbilical,
													    numeroEmbarazos,
													    asistioControl,
													    explicacionNoAsistencia,
													    atendidoPorMedico,
													    atendidoPorEnfermero,
													    atendidoPorAuxiliar,
													    atendidoPorPromotor,
													    atendidoPorOtro,
													    quienAtendio,
													    numeroControlesPrevios,
													    fechaUltimoControl,
													    madreVivioMismoLugar,
													    codigoMunicipioVivienda,
													    codigoDepartamentoVivienda,
													    lugarVivienda,
													    antecedenteVacunaAnti,
													    dosisDpt,
													    explicacionNoVacuna,
													    fechaDosisTd1,
													    fechaDosisTd2,
													    fechaDosisTd3,
													    fechaDosisTd4,
													    lugarParto,
													    institucionParto,
													    fechaIngresoParto,
													    fechaEgresoParto,
													    quienAtendioParto,
													    instrumentoCordon,
													    metodoEsterilizacion,
													    recibioInformacionMunon,
													    aplicacionSustanciasMunon,
													    cualesSustancias,
													    distanciaMinutos,
													    fechaInvestigacionCampo,
													    fechaVacunacion,
													    dosisTd1AplicadasMef,
													    dosisTd2AplicadasMef,
													    dosisTd3AplicadasMef,
													    dosisTd4AplicadasMef,
													    dosisTd5AplicadasMef,
													    dosisTd1AplicadasGest,
													    dosisTd2AplicadasGest,
													    dosisTd3AplicadasGest,
													    dosisTd4AplicadasGest,
													    dosisTd5AplicadasGest,
													    coberturaLograda,
													    
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
													    activa,
													    pais,
													    areaProcedencia);
    }
    
    
	public boolean isAntecedenteVacunaAnti() {
		return antecedenteVacunaAnti;
	}
	public void setAntecedenteVacunaAnti(boolean antecedenteVacunaAnti) {
		this.antecedenteVacunaAnti = antecedenteVacunaAnti;
	}
	public boolean isAplicacionSustanciasMunon() {
		return aplicacionSustanciasMunon;
	}
	public void setAplicacionSustanciasMunon(boolean aplicacionSustanciasMunon) {
		this.aplicacionSustanciasMunon = aplicacionSustanciasMunon;
	}
	public boolean isAsistioControl() {
		return asistioControl;
	}
	public void setAsistioControl(boolean asistioControl) {
		this.asistioControl = asistioControl;
	}
	public boolean isAtendidoPorAuxiliar() {
		return atendidoPorAuxiliar;
	}
	public void setAtendidoPorAuxiliar(boolean atendidoPorAuxiliar) {
		this.atendidoPorAuxiliar = atendidoPorAuxiliar;
	}
	public boolean isAtendidoPorEnfermero() {
		return atendidoPorEnfermero;
	}
	public void setAtendidoPorEnfermero(boolean atendidoPorEnfermero) {
		this.atendidoPorEnfermero = atendidoPorEnfermero;
	}
	public boolean isAtendidoPorMedico() {
		return atendidoPorMedico;
	}
	public void setAtendidoPorMedico(boolean atendidoPorMedico) {
		this.atendidoPorMedico = atendidoPorMedico;
	}
	public boolean isAtendidoPorOtro() {
		return atendidoPorOtro;
	}
	public void setAtendidoPorOtro(boolean atendidoPorOtro) {
		this.atendidoPorOtro = atendidoPorOtro;
	}
	public boolean isAtendidoPorPromotor() {
		return atendidoPorPromotor;
	}
	public void setAtendidoPorPromotor(boolean atendidoPorPromotor) {
		this.atendidoPorPromotor = atendidoPorPromotor;
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
	public int getCoberturaLograda() {
		return coberturaLograda;
	}
	public void setCoberturaLograda(int coberturaLograda) {
		this.coberturaLograda = coberturaLograda;
	}
	public String getCodigoDepartamentoVivienda() {
		return codigoDepartamentoVivienda;
	}
	public void setCodigoDepartamentoVivienda(String codigoDepartamentoVivienda) {
		this.codigoDepartamentoVivienda = codigoDepartamentoVivienda;
	}
	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}
	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}
	public int getCodigoFichaTetanos() {
		return codigoFichaTetanos;
	}
	public void setCodigoFichaTetanos(int codigoFichaTetanos) {
		this.codigoFichaTetanos = codigoFichaTetanos;
	}
	public String getCodigoMunicipioVivienda() {
		return codigoMunicipioVivienda;
	}
	public void setCodigoMunicipioVivienda(String codigoMunicipioVivienda) {
		this.codigoMunicipioVivienda = codigoMunicipioVivienda;
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
	public boolean isContracciones() {
		return contracciones;
	}
	public void setContracciones(boolean contracciones) {
		this.contracciones = contracciones;
	}
	public boolean isConvulsiones() {
		return convulsiones;
	}
	public void setConvulsiones(boolean convulsiones) {
		this.convulsiones = convulsiones;
	}
	public String getCualesSustancias() {
		return cualesSustancias;
	}
	public void setCualesSustancias(String cualesSustancias) {
		this.cualesSustancias = cualesSustancias;
	}
	public boolean isDejoMamar() {
		return dejoMamar;
	}
	public void setDejoMamar(boolean dejoMamar) {
		this.dejoMamar = dejoMamar;
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
	public boolean isDificultadRespiratoria() {
		return dificultadRespiratoria;
	}
	public void setDificultadRespiratoria(boolean dificultadRespiratoria) {
		this.dificultadRespiratoria = dificultadRespiratoria;
	}
	public String getDireccionPaciente() {
		return direccionPaciente;
	}
	public void setDireccionPaciente(String direccionPaciente) {
		this.direccionPaciente = direccionPaciente;
	}
	public int getDistanciaMinutos() {
		return distanciaMinutos;
	}
	public void setDistanciaMinutos(int distanciaMinutos) {
		this.distanciaMinutos = distanciaMinutos;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public int getDosisDpt() {
		return dosisDpt;
	}
	public void setDosisDpt(int dosisDpt) {
		this.dosisDpt = dosisDpt;
	}
	public int getDosisTd1AplicadasGest() {
		return dosisTd1AplicadasGest;
	}
	public void setDosisTd1AplicadasGest(int dosisTd1AplicadasGest) {
		this.dosisTd1AplicadasGest = dosisTd1AplicadasGest;
	}
	public int getDosisTd1AplicadasMef() {
		return dosisTd1AplicadasMef;
	}
	public void setDosisTd1AplicadasMef(int dosisTd1AplicadasMef) {
		this.dosisTd1AplicadasMef = dosisTd1AplicadasMef;
	}
	public int getDosisTd2AplicadasGest() {
		return dosisTd2AplicadasGest;
	}
	public void setDosisTd2AplicadasGest(int dosisTd2AplicadasGest) {
		this.dosisTd2AplicadasGest = dosisTd2AplicadasGest;
	}
	public int getDosisTd2AplicadasMef() {
		return dosisTd2AplicadasMef;
	}
	public void setDosisTd2AplicadasMef(int dosisTd2AplicadasMef) {
		this.dosisTd2AplicadasMef = dosisTd2AplicadasMef;
	}
	public int getDosisTd3AplicadasGest() {
		return dosisTd3AplicadasGest;
	}
	public void setDosisTd3AplicadasGest(int dosisTd3AplicadasGest) {
		this.dosisTd3AplicadasGest = dosisTd3AplicadasGest;
	}
	public int getDosisTd3AplicadasMef() {
		return dosisTd3AplicadasMef;
	}
	public void setDosisTd3AplicadasMef(int dosisTd3AplicadasMef) {
		this.dosisTd3AplicadasMef = dosisTd3AplicadasMef;
	}
	public int getDosisTd4AplicadasGest() {
		return dosisTd4AplicadasGest;
	}
	public void setDosisTd4AplicadasGest(int dosisTd4AplicadasGest) {
		this.dosisTd4AplicadasGest = dosisTd4AplicadasGest;
	}
	public int getDosisTd4AplicadasMef() {
		return dosisTd4AplicadasMef;
	}
	public void setDosisTd4AplicadasMef(int dosisTd4AplicadasMef) {
		this.dosisTd4AplicadasMef = dosisTd4AplicadasMef;
	}
	public int getDosisTd5AplicadasGest() {
		return dosisTd5AplicadasGest;
	}
	public void setDosisTd5AplicadasGest(int dosisTd5AplicadasGest) {
		this.dosisTd5AplicadasGest = dosisTd5AplicadasGest;
	}
	public int getDosisTd5AplicadasMef() {
		return dosisTd5AplicadasMef;
	}
	public void setDosisTd5AplicadasMef(int dosisTd5AplicadasMef) {
		this.dosisTd5AplicadasMef = dosisTd5AplicadasMef;
	}
	public String getEdad() {
		return edad;
	}
	public void setEdad(String edad) {
		this.edad = edad;
	}
	public int getEdadMadre() {
		return edadMadre;
	}
	public void setEdadMadre(int edadMadre) {
		this.edadMadre = edadMadre;
	}
	public boolean isEpisodiosApnea() {
		return episodiosApnea;
	}
	public void setEpisodiosApnea(boolean episodiosApnea) {
		this.episodiosApnea = episodiosApnea;
	}
	public boolean isEspasmos() {
		return espasmos;
	}
	public void setEspasmos(boolean espasmos) {
		this.espasmos = espasmos;
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
	public String getExplicacionNoAsistencia() {
		return explicacionNoAsistencia;
	}
	public void setExplicacionNoAsistencia(String explicacionNoAsistencia) {
		this.explicacionNoAsistencia = explicacionNoAsistencia;
	}
	public String getExplicacionNoVacuna() {
		return explicacionNoVacuna;
	}
	public void setExplicacionNoVacuna(String explicacionNoVacuna) {
		this.explicacionNoVacuna = explicacionNoVacuna;
	}
	public String getFechaDejo() {
		return fechaDejo;
	}
	public void setFechaDejo(String fechaDejo) {
		this.fechaDejo = fechaDejo;
	}
	public String getFechaDosisTd1() {
		return fechaDosisTd1;
	}
	public void setFechaDosisTd1(String fechaDosisTd1) {
		this.fechaDosisTd1 = fechaDosisTd1;
	}
	public String getFechaDosisTd2() {
		return fechaDosisTd2;
	}
	public void setFechaDosisTd2(String fechaDosisTd2) {
		this.fechaDosisTd2 = fechaDosisTd2;
	}
	public String getFechaDosisTd3() {
		return fechaDosisTd3;
	}
	public void setFechaDosisTd3(String fechaDosisTd3) {
		this.fechaDosisTd3 = fechaDosisTd3;
	}
	public String getFechaDosisTd4() {
		return fechaDosisTd4;
	}
	public void setFechaDosisTd4(String fechaDosisTd4) {
		this.fechaDosisTd4 = fechaDosisTd4;
	}
	public String getFechaEgresoHospital() {
		return fechaEgresoHospital;
	}
	public void setFechaEgresoHospital(String fechaEgresoHospital) {
		this.fechaEgresoHospital = fechaEgresoHospital;
	}
	public String getFechaEgresoParto() {
		return fechaEgresoParto;
	}
	public void setFechaEgresoParto(String fechaEgresoParto) {
		this.fechaEgresoParto = fechaEgresoParto;
	}
	public String getFechaIngresoParto() {
		return fechaIngresoParto;
	}
	public void setFechaIngresoParto(String fechaIngresoParto) {
		this.fechaIngresoParto = fechaIngresoParto;
	}
	public String getFechaInvestigacionCampo() {
		return fechaInvestigacionCampo;
	}
	public void setFechaInvestigacionCampo(String fechaInvestigacionCampo) {
		this.fechaInvestigacionCampo = fechaInvestigacionCampo;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getFechaNacimientoMadre() {
		return fechaNacimientoMadre;
	}
	public void setFechaNacimientoMadre(String fechaNacimientoMadre) {
		this.fechaNacimientoMadre = fechaNacimientoMadre;
	}
	public String getFechaUltimoControl() {
		return fechaUltimoControl;
	}
	public void setFechaUltimoControl(String fechaUltimoControl) {
		this.fechaUltimoControl = fechaUltimoControl;
	}
	public String getFechaVacunacion() {
		return fechaVacunacion;
	}
	public void setFechaVacunacion(String fechaVacunacion) {
		this.fechaVacunacion = fechaVacunacion;
	}
	public boolean isFontAbombada() {
		return fontAbombada;
	}
	public void setFontAbombada(boolean fontAbombada) {
		this.fontAbombada = fontAbombada;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public boolean isHipertermia() {
		return hipertermia;
	}
	public void setHipertermia(boolean hipertermia) {
		this.hipertermia = hipertermia;
	}
	public boolean isHipotermia() {
		return hipotermia;
	}
	public void setHipotermia(boolean hipotermia) {
		this.hipotermia = hipotermia;
	}
	public String getInstitucionParto() {
		return institucionParto;
	}
	public void setInstitucionParto(String institucionParto) {
		this.institucionParto = institucionParto;
	}
	public String getInstrumentoCordon() {
		return instrumentoCordon;
	}
	public void setInstrumentoCordon(String instrumentoCordon) {
		this.instrumentoCordon = instrumentoCordon;
	}
	public boolean isLlantoExcesivo() {
		return llantoExcesivo;
	}
	public void setLlantoExcesivo(boolean llantoExcesivo) {
		this.llantoExcesivo = llantoExcesivo;
	}
	public boolean isLlantoNacer() {
		return llantoNacer;
	}
	public void setLlantoNacer(boolean llantoNacer) {
		this.llantoNacer = llantoNacer;
	}
	public String getLoginUsuario() {
		return loginUsuario;
	}
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	public int getLugarParto() {
		return lugarParto;
	}
	public void setLugarParto(int lugarParto) {
		this.lugarParto = lugarParto;
	}
	public boolean isMadreVivioMismoLugar() {
		return madreVivioMismoLugar;
	}
	public void setMadreVivioMismoLugar(boolean madreVivioMismoLugar) {
		this.madreVivioMismoLugar = madreVivioMismoLugar;
	}
	public boolean isMamabaNormal() {
		return mamabaNormal;
	}
	public void setMamabaNormal(boolean mamabaNormal) {
		this.mamabaNormal = mamabaNormal;
	}
	public String getMetodoEsterilizacion() {
		return metodoEsterilizacion;
	}
	public void setMetodoEsterilizacion(String metodoEsterilizacion) {
		this.metodoEsterilizacion = metodoEsterilizacion;
	}
	public boolean isNacimientoTraumatico() {
		return nacimientoTraumatico;
	}
	public void setNacimientoTraumatico(boolean nacimientoTraumatico) {
		this.nacimientoTraumatico = nacimientoTraumatico;
	}
	public String getNombreMadre() {
		return nombreMadre;
	}
	public void setNombreMadre(String nombreMadre) {
		this.nombreMadre = nombreMadre;
	}
	public boolean isNotificar() {
		return notificar;
	}
	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}
	public int getNumeroControlesPrevios() {
		return numeroControlesPrevios;
	}
	public void setNumeroControlesPrevios(int numeroControlesPrevios) {
		this.numeroControlesPrevios = numeroControlesPrevios;
	}
	public int getNumeroEmbarazos() {
		return numeroEmbarazos;
	}
	public void setNumeroEmbarazos(int numeroEmbarazos) {
		this.numeroEmbarazos = numeroEmbarazos;
	}
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	public boolean isOpistotonos() {
		return opistotonos;
	}
	public void setOpistotonos(boolean opistotonos) {
		this.opistotonos = opistotonos;
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
	public String getQuienAtendio() {
		return quienAtendio;
	}
	public void setQuienAtendio(String quienAtendio) {
		this.quienAtendio = quienAtendio;
	}
	public int getQuienAtendioParto() {
		return quienAtendioParto;
	}
	public void setQuienAtendioParto(int quienAtendioParto) {
		this.quienAtendioParto = quienAtendioParto;
	}
	public boolean isRecibioInformacionMunon() {
		return recibioInformacionMunon;
	}
	public void setRecibioInformacionMunon(boolean recibioInformacionMunon) {
		this.recibioInformacionMunon = recibioInformacionMunon;
	}
	public boolean isRigidezNuca() {
		return rigidezNuca;
	}
	public void setRigidezNuca(boolean rigidezNuca) {
		this.rigidezNuca = rigidezNuca;
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
	public boolean isSepsisUmbilical() {
		return sepsisUmbilical;
	}
	public void setSepsisUmbilical(boolean sepsisUmbilical) {
		this.sepsisUmbilical = sepsisUmbilical;
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
	public boolean isTrismus() {
		return trismus;
	}
	public void setTrismus(boolean trismus) {
		this.trismus = trismus;
	}


	public Paciente getPaciente() {
		return paciente;
	}


	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}


	public String getLugarVivienda() {
		return lugarVivienda;
	}


	public void setLugarVivienda(String lugarVivienda) {
		this.lugarVivienda = lugarVivienda;
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


	public String getCodigoMunProcedencia() {
		return codigoMunProcedencia;
	}


	public void setCodigoMunProcedencia(String codigoMunProcedencia) {
		this.codigoMunProcedencia = codigoMunProcedencia;
	}


	public String getDepartamentoNotifica() {
		return departamentoNotifica;
	}


	public void setDepartamentoNotifica(String departamentoNotifica) {
		this.departamentoNotifica = departamentoNotifica;
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


	public FichaTetanosDao getFichaTetanosDao() {
		return fichaTetanosDao;
	}


	public void setFichaTetanosDao(FichaTetanosDao fichaTetanosDao) {
		this.fichaTetanosDao = fichaTetanosDao;
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


	public int getTipoCaso() {
		return tipoCaso;
	}


	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}


	public boolean isActiva() {
		return activa;
	}


	public void setActiva(boolean activa) {
		this.activa = activa;
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
}
