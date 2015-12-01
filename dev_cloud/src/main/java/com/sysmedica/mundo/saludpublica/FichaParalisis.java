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
import com.sysmedica.dao.FichaParalisisDao;

public class FichaParalisis {

	private Logger logger = Logger.getLogger(FichaParalisis.class);
	
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
    
    private int codigoFichaParalisis;
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
    private String nombrePadre;
    private String fechaInicioInvestigacion;
    private int numeroDosis;
    private String fechaUltimaDosis;
    private int tieneCarnet;
    private int fiebre;
    private int respiratorios;
    private int digestivos;
    private int instalacion;
    private int dolorMuscular;
    private int signosMeningeos1;
    private int fiebreInicioParalisis;
    private int progresion;
    private String fechaInicioParalisis;
    private int musculosRespiratorios;
    private int signosMeningeos2;
    private int babinsky;
    private int brudzinsky;
    private int paresCraneanos;
    private int liquidocefalo;
    private String fechaTomaLiquido;
    private int celulas;
    private int globulosRojos;
    private int leucocitos;
    private int proteinas;
    private int glucosa;
    private int electromiografia;
    private String fechaTomaElectro;
    private int velocidadConduccion;
    private int resultadoConduccion;
    private String fechaTomaVelocidad;
    private String impresionDiagnostica;
    private int muestraMateriaFecal;
    private String fechaTomaFecal;
    private String fechaEnvioFecal;
    private String fechaRecepcionFecal;
    private String fechaResultadoFecal;
    private int virusAislado;
    private String fechaVacunacionBloqueo;
    private String fechaCulminacionVacunacion;
    private String municipiosVacunados;
    private int codigoExtremidad1;
    private int codigoExtremidad2;
    private int codigoExtremidad3;
    private int codigoExtremidad4;
    private int codigoGrupoEdad1;
    private int codigoGrupoEdad2;
    private int codigoGrupoEdad3;
    private String pais;
    private int areaProcedencia;
    
    private HashMap datosExtremidades;
    private HashMap datosGrupoEdad;
    
    private Paciente paciente;
    
    private FichaParalisisDao fichaParalisisDao;
    
    public static int codigoGlobal;
    
    private String telefonoContacto;
    
    public FichaParalisis() {
		
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
				fichaParalisisDao = myFactory.getFichaParalisisDao();
				wasInited = (fichaParalisisDao != null);
			}
			return wasInited;
	}
    
    
    public void reset() {
    	
    	sire = "";
        nombreMadre = "";
        nombrePadre = "";
        fechaInicioInvestigacion = "";
        numeroDosis = 0;
        fechaUltimaDosis = "";
        tieneCarnet = 0;
        fiebre = 0;
        respiratorios = 0;
        digestivos = 0;
        instalacion = 0;
        dolorMuscular = 0;
        signosMeningeos1 = 0;
        fiebreInicioParalisis = 0;
        progresion = 0;
        fechaInicioParalisis = "";
        musculosRespiratorios = 0;
        signosMeningeos2 = 0;
        babinsky = 0;
        brudzinsky = 0;
        paresCraneanos = 0;
        liquidocefalo = 0;
        fechaTomaLiquido= "";
        celulas = 0;
        globulosRojos = 0;
        leucocitos = 0;
        proteinas = 0;
        glucosa = 0;
        electromiografia = 0;
        fechaTomaElectro = "";
        velocidadConduccion = 0;
        resultadoConduccion = 0;
        fechaTomaVelocidad = "";
        impresionDiagnostica = "";
        muestraMateriaFecal = 0;
        fechaTomaFecal = "";
        fechaEnvioFecal = "";
        fechaRecepcionFecal = "";
        fechaResultadoFecal = "";
        virusAislado = 0;
        fechaVacunacionBloqueo = "";
        fechaCulminacionVacunacion = "";
        municipiosVacunados = "";
        codigoExtremidad1 = 0;
        codigoExtremidad2 = 0;
        codigoExtremidad3 = 0;
        codigoExtremidad4 = 0;
        codigoGrupoEdad1 = 0;
        codigoGrupoEdad2 = 0;
        codigoGrupoEdad3 = 0;
        
        datosExtremidades = new HashMap();
        datosGrupoEdad = new HashMap();
        
        paciente = new Paciente();
    }
    
    
    public int insertarFicha(Connection con) 
    {
    	return fichaParalisisDao.insertarFicha(con,
												notificar,
											    loginUsuario,
											    codigoFichaParalisis,
											    codigoPaciente,
											    codigoDiagnostico,
											    codigoNotificacion,
											    numeroSolicitud,
											    codigoConvenio,
												nombreProfesional,
											    estadoFicha
											    );
    }
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaParalisisDao.modificarFicha(con,
												sire,
												loginUsuario,
											    codigoFichaParalisis,
											    estadoFicha,
												nombreMadre,
											    nombrePadre,
											    fechaInicioInvestigacion,
											    numeroDosis,
											    fechaUltimaDosis,
											    tieneCarnet,
											    fiebre,
											    respiratorios,
											    digestivos,
											    instalacion,
											    dolorMuscular,
											    signosMeningeos1,
											    fiebreInicioParalisis,
											    progresion,
											    fechaInicioParalisis,
											    musculosRespiratorios,
											    signosMeningeos2,
											    babinsky,
											    brudzinsky,
											    paresCraneanos,
											    liquidocefalo,
											    fechaTomaLiquido,
											    celulas,
											    globulosRojos,
											    leucocitos,
											    proteinas,
											    glucosa,
											    electromiografia,
											    fechaTomaElectro,
											    velocidadConduccion,
											    resultadoConduccion,
											    fechaTomaVelocidad,
											    impresionDiagnostica,
											    muestraMateriaFecal,
											    fechaTomaFecal,
											    fechaEnvioFecal,
											    fechaRecepcionFecal,
											    fechaResultadoFecal,
											    virusAislado,
											    fechaVacunacionBloqueo,
											    fechaCulminacionVacunacion,
											    municipiosVacunados,
											    codigoExtremidad1,
											    codigoExtremidad2,
											    codigoExtremidad3,
											    codigoExtremidad4,
											    codigoGrupoEdad1,
											    codigoGrupoEdad2,
											    codigoGrupoEdad3,
											    
											    datosExtremidades,
											    datosGrupoEdad,
											    
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
											    areaProcedencia);
    }
    
    
    
    
    public void cargarDatos(Connection con, int codigo) 
    {
    	this.codigoFichaParalisis = codigo;
    	
    	ResultSet rs = fichaParalisisDao.consultaTodoFicha(con,codigo);
    	
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
			    this.setEstadoFicha(rs.getInt("estado"));
			    this.setNombreMadre(rs.getString("nombreMadre"));
			    this.setNombrePadre(rs.getString("nombrePadre"));
			    this.setFechaInicioInvestigacion(rs.getString("fechaInicioInvestigacion"));
			    this.setNumeroDosis(rs.getInt("numeroDosis"));
			    this.setFechaUltimaDosis(rs.getString("fechaUltimaDosis"));
			    this.setTieneCarnet(rs.getInt("tieneCarnet"));
			    this.setFiebre(rs.getInt("fiebre"));
			    this.setRespiratorios(rs.getInt("respiratorios"));
			    this.setDigestivos(rs.getInt("digestivos"));
			    this.setInstalacion(rs.getInt("instalacion"));
			    this.setDolorMuscular(rs.getInt("dolorMuscular"));
			    this.setSignosMeningeos1(rs.getInt("signosMeningeos1"));
			    this.setFiebreInicioParalisis(rs.getInt("fiebreInicioParalisis"));
			    this.setProgresion(rs.getInt("progresion"));
			    this.setFechaInicioParalisis(rs.getString("fechaInicioParalisis"));
			    this.setMusculosRespiratorios(rs.getInt("musculosRespiratorios"));
			    this.setSignosMeningeos2(rs.getInt("signosMeningeos2"));
			    this.setBabinsky(rs.getInt("babinsky"));
			    this.setBrudzinsky(rs.getInt("brudzinsky"));
			    this.setParesCraneanos(rs.getInt("paresCraneanos"));
			    this.setLiquidocefalo(rs.getInt("liquidoCefalo"));
			    this.setFechaTomaLiquido(rs.getString("fechaTomaLiquido"));
			    this.setCelulas(rs.getInt("celulas"));
			    this.setGlobulosRojos(rs.getInt("globulosRojos"));
			    this.setLeucocitos(rs.getInt("leucocitos"));
			    this.setProteinas(rs.getInt("proteinas"));
			    this.setGlucosa(rs.getInt("glucosa"));
			    this.setElectromiografia(rs.getInt("electromiografia"));
			    this.setFechaTomaElectro(rs.getString("fechaTomaElectro"));
			    this.setVelocidadConduccion(rs.getInt("velocidadConduccion"));
			    this.setResultadoConduccion(rs.getInt("resultadoConduccion"));
			    this.setFechaTomaVelocidad(rs.getString("fechaTomaVelocidad"));
			    this.setImpresionDiagnostica(rs.getString("impresionDiagnostica"));
			    this.setMuestraMateriaFecal(rs.getInt("muestraMateriaFecal"));
			    this.setFechaTomaFecal(rs.getString("fechaTomaFecal"));
			    this.setFechaEnvioFecal(rs.getString("fechaEnvioFecal"));
			    this.setFechaRecepcionFecal(rs.getString("fechaRecepcionFecal"));
			    this.setFechaResultadoFecal(rs.getString("fechaResultadoFecal"));
			    this.setVirusAislado(rs.getInt("virusAislado"));
			    this.setFechaVacunacionBloqueo(rs.getString("fechaVacunacionBloqueo"));
			    this.setFechaCulminacionVacunacion(rs.getString("fechaCulminacionVacunacion"));
			    this.setMunicipiosVacunados(rs.getString("municipiosVacunados"));
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
			    this.setTelefonoContacto(rs.getString("telefonoContacto"));
		        
			    int codigoExt1 = rs.getInt("codigoextremidad1");
			    int codigoExt2 = rs.getInt("codigoextremidad2");
			    int codigoExt3 = rs.getInt("codigoextremidad3");
			    int codigoExt4 = rs.getInt("codigoextremidad4");
			    
			    int codigoGrup1 = rs.getInt("codigogrupo1");
			    int codigoGrup2 = rs.getInt("codigogrupo2");
			    int codigoGrup3 = rs.getInt("codigogrupo3");
			    
			    this.codigoExtremidad1 = codigoExt1;
			    this.codigoExtremidad2 = codigoExt2;
			    this.codigoExtremidad3 = codigoExt3;
			    this.codigoExtremidad4 = codigoExt4;
			    
			    this.codigoGrupoEdad1 = codigoGrup1;
			    this.codigoGrupoEdad2 = codigoGrup2;
			    this.codigoGrupoEdad3 = codigoGrup3;
			    
			    HashMap mapaExtremidades = new HashMap();
			    
			    ////////////////////////////////////////////////////////////////
			    
			    ResultSet rs2 = fichaParalisisDao.consultarDatosExtremidades(con,codigoExt1);
		    	
		    	rs2.next();
		    	
		    	mapaExtremidades.put("paresia_1",rs2.getBoolean("paresia"));
		    	mapaExtremidades.put("paralisis_1",rs2.getBoolean("paralisis"));
		    	mapaExtremidades.put("flaccida_1",rs2.getBoolean("flaccida"));
		    	mapaExtremidades.put("localizacion_1",rs2.getString("localizacion"));
		    	mapaExtremidades.put("sensibilidad_1",rs2.getString("sensibilidad"));
		    	mapaExtremidades.put("rot_1",rs2.getString("rot"));
			    
		    	////////////////////////////////////////////////////////////////
		    	
		    	rs2 = fichaParalisisDao.consultarDatosExtremidades(con,codigoExt2);
		    	
		    	rs2.next();
		    	
		    	mapaExtremidades.put("paresia_2",rs2.getBoolean("paresia"));
		    	mapaExtremidades.put("paralisis_2",rs2.getBoolean("paralisis"));
		    	mapaExtremidades.put("flaccida_2",rs2.getBoolean("flaccida"));
		    	mapaExtremidades.put("localizacion_2",rs2.getString("localizacion"));
		    	mapaExtremidades.put("sensibilidad_2",rs2.getString("sensibilidad"));
		    	mapaExtremidades.put("rot_2",rs2.getString("rot"));
			    
		    	////////////////////////////////////////////////////////////////
		    	
		    	rs2 = fichaParalisisDao.consultarDatosExtremidades(con,codigoExt3);
		    	
		    	rs2.next();
		    	
		    	mapaExtremidades.put("paresia_3",rs2.getBoolean("paresia"));
		    	mapaExtremidades.put("paralisis_3",rs2.getBoolean("paralisis"));
		    	mapaExtremidades.put("flaccida_3",rs2.getBoolean("flaccida"));
		    	mapaExtremidades.put("localizacion_3",rs2.getString("localizacion"));
		    	mapaExtremidades.put("sensibilidad_3",rs2.getString("sensibilidad"));
		    	mapaExtremidades.put("rot_3",rs2.getString("rot"));
			    
		    	////////////////////////////////////////////////////////////////
		    	
		    	rs2 = fichaParalisisDao.consultarDatosExtremidades(con,codigoExt4);
		    	
		    	rs2.next();
		    	
		    	mapaExtremidades.put("paresia_4",rs2.getBoolean("paresia"));
		    	mapaExtremidades.put("paralisis_4",rs2.getBoolean("paralisis"));
		    	mapaExtremidades.put("flaccida_4",rs2.getBoolean("flaccida"));
		    	mapaExtremidades.put("localizacion_4",rs2.getString("localizacion"));
		    	mapaExtremidades.put("sensibilidad_4",rs2.getString("sensibilidad"));
		    	mapaExtremidades.put("rot_4",rs2.getString("rot"));
			    
		    	////////////////////////////////////////////////////////////////
		    	
		    	this.setDatosExtremidades(mapaExtremidades);
		    	
		    	
		    	HashMap mapaGrupoEdad =  new HashMap();
		    	
		    	////////////////////////////////////////////////////////////////
			    
			    ResultSet rs3 = fichaParalisisDao.consultarDatosGrupoEdad(con,codigoGrup1);
		    	
		    	rs3.next();
		    	
		    	mapaGrupoEdad.put("poblacionmeta_1",rs3.getString("poblacionmeta"));
		    	mapaGrupoEdad.put("reciennacido_1",rs3.getString("reciennacido"));
		    	mapaGrupoEdad.put("vop1_1",rs3.getString("vop1"));
		    	mapaGrupoEdad.put("vop2_1",rs3.getString("vop2"));
		    	mapaGrupoEdad.put("vop3_1",rs3.getString("vop3"));
		    	mapaGrupoEdad.put("adicional_1",rs3.getString("adicional"));
			    
		    	////////////////////////////////////////////////////////////////
			    
		    	rs3 = fichaParalisisDao.consultarDatosGrupoEdad(con,codigoGrup2);
		    	
		    	rs3.next();
		    	
		    	mapaGrupoEdad.put("poblacionmeta_2",rs3.getString("poblacionmeta"));
		    	mapaGrupoEdad.put("reciennacido_2",rs3.getString("reciennacido"));
		    	mapaGrupoEdad.put("vop1_2",rs3.getString("vop1"));
		    	mapaGrupoEdad.put("vop2_2",rs3.getString("vop2"));
		    	mapaGrupoEdad.put("vop3_2",rs3.getString("vop3"));
		    	mapaGrupoEdad.put("adicional_2",rs3.getString("adicional"));
			    
		    	////////////////////////////////////////////////////////////////
		    	
		    	rs3 = fichaParalisisDao.consultarDatosGrupoEdad(con,codigoGrup3);
		    	
		    	rs3.next();
		    	
		    	mapaGrupoEdad.put("poblacionmeta_3",rs3.getString("poblacionmeta"));
		    	mapaGrupoEdad.put("reciennacido_3",rs3.getString("reciennacido"));
		    	mapaGrupoEdad.put("vop1_3",rs3.getString("vop1"));
		    	mapaGrupoEdad.put("vop2_3",rs3.getString("vop2"));
		    	mapaGrupoEdad.put("vop3_3",rs3.getString("vop3"));
		    	mapaGrupoEdad.put("adicional_3",rs3.getString("adicional"));
			    
		    	////////////////////////////////////////////////////////////////
			    
			    this.setDatosGrupoEdad(mapaGrupoEdad);
			    
			    /*
			    for (int i=1;i<5;i++) {
			    	
			    	String codigoExtStr = Integer.toString(codigo) + Integer.toString(i);
			    	int codigoExt = Integer.parseInt(codigoExtStr);
			    	
			    	ResultSet rs2 = fichaParalisisDao.consultarDatosExtremidades(con,codigoExt);
			    	
			    	rs2.next();
			    	
			    	mapaExtremidades.put("paresia_"+i,rs2.getBoolean("paresia"));
			    	mapaExtremidades.put("paralisis_"+i,rs2.getBoolean("paralisis"));
			    	mapaExtremidades.put("flaccida_"+i,rs2.getBoolean("flaccida"));
			    	mapaExtremidades.put("localizacion_"+i,rs2.getString("localizacion"));
			    	mapaExtremidades.put("sensibilidad_"+i,rs2.getString("sensibilidad"));
			    	mapaExtremidades.put("rot_"+i,rs2.getString("rot"));
			    	
			    }
			    */
			    
			    
			    
			    /*
			    ResultSet rs2 = fichaParalisisDao.consultarDatosExtremidades(con,codigo);
			    
			    logger.info("5");
			    
			    HashMap mapaExtremidades = new HashMap();
			    int i=1;
			    
			    while (rs2.next()) {
			    	
			    	logger.info("5.5");
			    	
			    	mapaExtremidades.put("paresia_"+i,rs2.getBoolean("paresia"));
			    	mapaExtremidades.put("paralisis_"+i,rs2.getBoolean("paralisis"));
			    	mapaExtremidades.put("flaccida_"+i,rs2.getBoolean("flaccida"));
			    	mapaExtremidades.put("localizacion_"+i,rs2.getInt("localizacion"));
			    	mapaExtremidades.put("sensibilidad_"+i,rs2.getInt("sensibilidad"));
			    	mapaExtremidades.put("rot_"+i,rs2.getInt("rot"));
			    	
			    	i++;
			    }
			    
			    logger.info("6");
			    
			    this.setDatosExtremidades(mapaExtremidades);
		    
			    logger.info(datosExtremidades.get("paresia_1").toString());
			    logger.info(datosExtremidades.get("paralisis_1").toString());
			    logger.info(datosExtremidades.get("flaccida_1").toString());
		
			
			    HashMap mapaGrupoEdad =  new HashMap();
			    
			    for (int i=1;i<4;i++) {
			    
			    	String codigoGrupoStr = Integer.toString(codigo) + Integer.toString(i);
			    	int codigoGru = Integer.parseInt(codigoGrupoStr);
			    	
			    	ResultSet rs3 = fichaParalisisDao.consultarDatosGrupoEdad(con,codigoGru);
			    	
			    	rs3.next();
			    	
			    	mapaGrupoEdad.put("poblacionmeta_"+i,rs3.getString("poblacionmeta"));
			    	mapaGrupoEdad.put("reciennacido_"+i,rs3.getString("reciennacido"));
			    	mapaGrupoEdad.put("vop1_"+i,rs3.getString("vop1"));
			    	mapaGrupoEdad.put("vop2_"+i,rs3.getString("vop2"));
			    	mapaGrupoEdad.put("vop3_"+i,rs3.getString("vop3"));
			    	mapaGrupoEdad.put("adicional_"+i,rs3.getString("adicional"));
			    }
			    
			    this.setDatosGrupoEdad(mapaGrupoEdad);
			    
		
			    ResultSet rs3 = fichaParalisisDao.consultarDatosGrupoEdad(con,codigo);
			    
			    logger.info("7");
			    
			    HashMap mapaGrupoEdad = new HashMap();
			    int j=1;
			    
			    while (rs3.next()) {
			    	
			    	logger.info("7.5");
			    	
			    	mapaGrupoEdad.put("poblacionmeta_"+i,rs3.getInt("poblacionmeta"));
			    	mapaGrupoEdad.put("reciennacido_"+i,rs3.getInt("reciennacido"));
			    	mapaGrupoEdad.put("vop1_"+i,rs3.getInt("vop1"));
			    	mapaGrupoEdad.put("vop2_"+i,rs3.getInt("vop2"));
			    	mapaGrupoEdad.put("vop3_"+i,rs3.getInt("vop3"));
			    	mapaGrupoEdad.put("adicional_"+i,rs3.getInt("adicional"));
			    	
			    	j++;
			    }
			    
			    logger.info("8");
			    			    
			    this.setDatosGrupoEdad(mapaGrupoEdad);
			    
			    logger.info(datosGrupoEdad.get("poblacionmeta_1").toString());
			    logger.info(datosGrupoEdad.get("reciennacido_1").toString());
		*/
    		}
    	}
    	catch (SQLException sqle) {
    		logger.info(sqle.getMessage());
    	}
    }
    
    
    
    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaParalisisDao.consultaDatosPaciente(con,codigo,empezarnuevo);
    	
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
    	return fichaParalisisDao.insertarFichaCompleta(con,
														numeroSolicitud,
														loginUsuario,
														codigoPaciente,
														codigoDiagnostico,
														estadoFicha,
														codigoConvenio,
														nombreProfesional,
													    
													    sire,
													    nombreMadre,
													    nombrePadre,
													    fechaInicioInvestigacion,
													    numeroDosis,
													    fechaUltimaDosis,
													    tieneCarnet,
													    fiebre,
													    respiratorios,
													    digestivos,
													    instalacion,
													    dolorMuscular,
													    signosMeningeos1,
													    fiebreInicioParalisis,
													    progresion,
													    fechaInicioParalisis,
													    musculosRespiratorios,
													    signosMeningeos2,
													    babinsky,
													    brudzinsky,
													    paresCraneanos,
													    liquidocefalo,
													    fechaTomaLiquido,
													    celulas,
													    globulosRojos,
													    leucocitos,
													    proteinas,
													    glucosa,
													    electromiografia,
													    fechaTomaElectro,
													    velocidadConduccion,
													    resultadoConduccion,
													    fechaTomaVelocidad,
													    impresionDiagnostica,
													    muestraMateriaFecal,
													    fechaTomaFecal,
													    fechaEnvioFecal,
													    fechaRecepcionFecal,
													    fechaResultadoFecal,
													    virusAislado,
													    fechaVacunacionBloqueo,
													    fechaCulminacionVacunacion,
													    municipiosVacunados,
													    codigoExtremidad1,
													    codigoExtremidad2,
													    codigoExtremidad3,
													    codigoExtremidad4,
													    codigoGrupoEdad1,
													    codigoGrupoEdad2,
													    codigoGrupoEdad3,
													    
													    datosExtremidades,
													    datosGrupoEdad,
													    
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
													    telefonoContacto,
													    activa,
													    pais,
													    areaProcedencia);
    }
    
    
    

	public int getBabinsky() {
		return babinsky;
	}

	public void setBabinsky(int babinsky) {
		this.babinsky = babinsky;
	}

	public int getBrudzinsky() {
		return brudzinsky;
	}

	public void setBrudzinsky(int brudzinsky) {
		this.brudzinsky = brudzinsky;
	}

	public int getCelulas() {
		return celulas;
	}

	public void setCelulas(int celulas) {
		this.celulas = celulas;
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

	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}

	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}

	public int getCodigoExtremidad1() {
		return codigoExtremidad1;
	}

	public void setCodigoExtremidad1(int codigoExtremidad1) {
		this.codigoExtremidad1 = codigoExtremidad1;
	}

	public int getCodigoExtremidad2() {
		return codigoExtremidad2;
	}

	public void setCodigoExtremidad2(int codigoExtremidad2) {
		this.codigoExtremidad2 = codigoExtremidad2;
	}

	public int getCodigoExtremidad3() {
		return codigoExtremidad3;
	}

	public void setCodigoExtremidad3(int codigoExtremidad3) {
		this.codigoExtremidad3 = codigoExtremidad3;
	}

	public int getCodigoExtremidad4() {
		return codigoExtremidad4;
	}

	public void setCodigoExtremidad4(int codigoExtremidad4) {
		this.codigoExtremidad4 = codigoExtremidad4;
	}

	public int getCodigoFichaParalisis() {
		return codigoFichaParalisis;
	}

	public void setCodigoFichaParalisis(int codigoFichaParalisis) {
		this.codigoFichaParalisis = codigoFichaParalisis;
	}

	public int getCodigoGrupoEdad1() {
		return codigoGrupoEdad1;
	}

	public void setCodigoGrupoEdad1(int codigoGrupoEdad1) {
		this.codigoGrupoEdad1 = codigoGrupoEdad1;
	}

	public int getCodigoGrupoEdad2() {
		return codigoGrupoEdad2;
	}

	public void setCodigoGrupoEdad2(int codigoGrupoEdad2) {
		this.codigoGrupoEdad2 = codigoGrupoEdad2;
	}

	public int getCodigoGrupoEdad3() {
		return codigoGrupoEdad3;
	}

	public void setCodigoGrupoEdad3(int codigoGrupoEdad3) {
		this.codigoGrupoEdad3 = codigoGrupoEdad3;
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

	public HashMap getDatosExtremidades() {
		return datosExtremidades;
	}

	public void setDatosExtremidades(HashMap datosExtremidades) {
		this.datosExtremidades = datosExtremidades;
	}

	public HashMap getDatosGrupoEdad() {
		return datosGrupoEdad;
	}

	public void setDatosGrupoEdad(HashMap datosGrupoEdad) {
		this.datosGrupoEdad = datosGrupoEdad;
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

	public int getDigestivos() {
		return digestivos;
	}

	public void setDigestivos(int digestivos) {
		this.digestivos = digestivos;
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

	public int getDolorMuscular() {
		return dolorMuscular;
	}

	public void setDolorMuscular(int dolorMuscular) {
		this.dolorMuscular = dolorMuscular;
	}

	public String getEdad() {
		return edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}

	public int getElectromiografia() {
		return electromiografia;
	}

	public void setElectromiografia(int electromiografia) {
		this.electromiografia = electromiografia;
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

	public String getFechaCulminacionVacunacion() {
		return fechaCulminacionVacunacion;
	}

	public void setFechaCulminacionVacunacion(String fechaCulminacionVacunacion) {
		this.fechaCulminacionVacunacion = fechaCulminacionVacunacion;
	}

	public String getFechaEnvioFecal() {
		return fechaEnvioFecal;
	}

	public void setFechaEnvioFecal(String fechaEnvioFecal) {
		this.fechaEnvioFecal = fechaEnvioFecal;
	}

	public String getFechaInicioInvestigacion() {
		return fechaInicioInvestigacion;
	}

	public void setFechaInicioInvestigacion(String fechaInicioInvestigacion) {
		this.fechaInicioInvestigacion = fechaInicioInvestigacion;
	}

	public String getFechaInicioParalisis() {
		return fechaInicioParalisis;
	}

	public void setFechaInicioParalisis(String fechaInicioParalisis) {
		this.fechaInicioParalisis = fechaInicioParalisis;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getFechaRecepcionFecal() {
		return fechaRecepcionFecal;
	}

	public void setFechaRecepcionFecal(String fechaRecepcionFecal) {
		this.fechaRecepcionFecal = fechaRecepcionFecal;
	}

	public String getFechaResultadoFecal() {
		return fechaResultadoFecal;
	}

	public void setFechaResultadoFecal(String fechaResultadoFecal) {
		this.fechaResultadoFecal = fechaResultadoFecal;
	}

	public String getFechaTomaElectro() {
		return fechaTomaElectro;
	}

	public void setFechaTomaElectro(String fechaTomaElectro) {
		this.fechaTomaElectro = fechaTomaElectro;
	}

	public String getFechaTomaFecal() {
		return fechaTomaFecal;
	}

	public void setFechaTomaFecal(String fechaTomaFecal) {
		this.fechaTomaFecal = fechaTomaFecal;
	}

	public String getFechaTomaLiquido() {
		return fechaTomaLiquido;
	}

	public void setFechaTomaLiquido(String fechaTomaLiquido) {
		this.fechaTomaLiquido = fechaTomaLiquido;
	}

	public String getFechaTomaVelocidad() {
		return fechaTomaVelocidad;
	}

	public void setFechaTomaVelocidad(String fechaTomaVelocidad) {
		this.fechaTomaVelocidad = fechaTomaVelocidad;
	}

	public String getFechaUltimaDosis() {
		return fechaUltimaDosis;
	}

	public void setFechaUltimaDosis(String fechaUltimaDosis) {
		this.fechaUltimaDosis = fechaUltimaDosis;
	}

	public String getFechaVacunacionBloqueo() {
		return fechaVacunacionBloqueo;
	}

	public void setFechaVacunacionBloqueo(String fechaVacunacionBloqueo) {
		this.fechaVacunacionBloqueo = fechaVacunacionBloqueo;
	}

	public int getFiebre() {
		return fiebre;
	}

	public void setFiebre(int fiebre) {
		this.fiebre = fiebre;
	}

	public int getFiebreInicioParalisis() {
		return fiebreInicioParalisis;
	}

	public void setFiebreInicioParalisis(int fiebreInicioParalisis) {
		this.fiebreInicioParalisis = fiebreInicioParalisis;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public int getGlobulosRojos() {
		return globulosRojos;
	}

	public void setGlobulosRojos(int globulosRojos) {
		this.globulosRojos = globulosRojos;
	}

	public int getGlucosa() {
		return glucosa;
	}

	public void setGlucosa(int glucosa) {
		this.glucosa = glucosa;
	}

	public String getImpresionDiagnostica() {
		return impresionDiagnostica;
	}

	public void setImpresionDiagnostica(String impresionDiagnostica) {
		this.impresionDiagnostica = impresionDiagnostica;
	}

	public int getInstalacion() {
		return instalacion;
	}

	public void setInstalacion(int instalacion) {
		this.instalacion = instalacion;
	}

	public int getLeucocitos() {
		return leucocitos;
	}

	public void setLeucocitos(int leucocitos) {
		this.leucocitos = leucocitos;
	}

	public int getLiquidocefalo() {
		return liquidocefalo;
	}

	public void setLiquidocefalo(int liquidocefalo) {
		this.liquidocefalo = liquidocefalo;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public int getMuestraMateriaFecal() {
		return muestraMateriaFecal;
	}

	public void setMuestraMateriaFecal(int muestraMateriaFecal) {
		this.muestraMateriaFecal = muestraMateriaFecal;
	}

	public String getMunicipiosVacunados() {
		return municipiosVacunados;
	}

	public void setMunicipiosVacunados(String municipiosVacunados) {
		this.municipiosVacunados = municipiosVacunados;
	}

	public int getMusculosRespiratorios() {
		return musculosRespiratorios;
	}

	public void setMusculosRespiratorios(int musculosRespiratorios) {
		this.musculosRespiratorios = musculosRespiratorios;
	}

	public String getNombreMadre() {
		return nombreMadre;
	}

	public void setNombreMadre(String nombreMadre) {
		this.nombreMadre = nombreMadre;
	}

	public String getNombrePadre() {
		return nombrePadre;
	}

	public void setNombrePadre(String nombrePadre) {
		this.nombrePadre = nombrePadre;
	}

	public boolean isNotificar() {
		return notificar;
	}

	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}

	public int getNumeroDosis() {
		return numeroDosis;
	}

	public void setNumeroDosis(int numeroDosis) {
		this.numeroDosis = numeroDosis;
	}

	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public int getParesCraneanos() {
		return paresCraneanos;
	}

	public void setParesCraneanos(int paresCraneanos) {
		this.paresCraneanos = paresCraneanos;
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

	public int getProgresion() {
		return progresion;
	}

	public void setProgresion(int progresion) {
		this.progresion = progresion;
	}

	public int getProteinas() {
		return proteinas;
	}

	public void setProteinas(int proteinas) {
		this.proteinas = proteinas;
	}

	public int getRespiratorios() {
		return respiratorios;
	}

	public void setRespiratorios(int respiratorios) {
		this.respiratorios = respiratorios;
	}

	public int getResultadoConduccion() {
		return resultadoConduccion;
	}

	public void setResultadoConduccion(int resultadoConduccion) {
		this.resultadoConduccion = resultadoConduccion;
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

	public int getSignosMeningeos1() {
		return signosMeningeos1;
	}

	public void setSignosMeningeos1(int signosMeningeos1) {
		this.signosMeningeos1 = signosMeningeos1;
	}

	public int getSignosMeningeos2() {
		return signosMeningeos2;
	}

	public void setSignosMeningeos2(int signosMeningeos2) {
		this.signosMeningeos2 = signosMeningeos2;
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

	public int getTieneCarnet() {
		return tieneCarnet;
	}

	public void setTieneCarnet(int tieneCarnet) {
		this.tieneCarnet = tieneCarnet;
	}

	public int getVelocidadConduccion() {
		return velocidadConduccion;
	}

	public void setVelocidadConduccion(int velocidadConduccion) {
		this.velocidadConduccion = velocidadConduccion;
	}

	public int getVirusAislado() {
		return virusAislado;
	}

	public void setVirusAislado(int virusAislado) {
		this.virusAislado = virusAislado;
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

	public FichaParalisisDao getFichaParalisisDao() {
		return fichaParalisisDao;
	}

	public void setFichaParalisisDao(FichaParalisisDao fichaParalisisDao) {
		this.fichaParalisisDao = fichaParalisisDao;
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

	public String getTelefonoContacto() {
		return telefonoContacto;
	}

	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
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
