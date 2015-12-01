package com.sysmedica.mundo.saludpublica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.FichaLeishmaniasisDao;

public class FichaLeishmaniasis {

	
	private Logger logger = Logger.getLogger(FichaEasv.class);
	
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
    
    private int codigoFichaLeishmaniasis;
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
    
    private HashMap sintomas;
    private HashMap datosLaboratorio;
    private HashMap lesiones;
    
    FichaLeishmaniasisDao fichaLeishmaniasisDao;
    
    private int cara;
    private int tronco;
    private int superiores;
    private int inferiores;
    
    public void reset()
    {
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
        
        datosLaboratorio = new HashMap();
        lesiones = new HashMap();
        paciente = new Paciente();
        
        cara = 0;
        tronco = 0;
        superiores = 0;
        inferiores = 0;
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
			fichaLeishmaniasisDao = myFactory.getFichaLeishmaniasisDao();
			wasInited = (fichaLeishmaniasisDao != null);
		}
		return wasInited;
	}
    
    
    public FichaLeishmaniasis()
    {
    	reset();
        init(System.getProperty("TIPOBD"));
    }
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaLeishmaniasisDao.insertarFichaCompleta(con,
												    		numeroSolicitud,
															loginUsuario,
															codigoPaciente,
															codigoDiagnostico,
															estadoFicha,
															codigoConvenio,
															nombreProfesional,
														    
														    sire,
															notificar,
															
															codigoFichaLeishmaniasis,										    
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
														    
														    numeroLesiones,
														    localizacionLesiones,
														    anchoLesion1,
														    largoLesion1,
														    anchoLesion2,
														    largoLesion2,
														    anchoLesion3,
														    largoLesion3,
														    cicatrices,
														    tiempo,
														    unidadTiempo,
														    antecedenteTrauma,
														    mucosaAfectada,
														    rinorrea,
														    epistaxis,
														    obstruccion,
														    disfonia,
														    disfagia,
														    hiperemia,
														    ulceracion,
														    perforacion,
														    destruccion,
														    fiebre,
														    hepatomegalia,
														    esplenomegalia,
														    anemia,
														    leucopenia,
														    trombocitopenia,
														    recibioTratamiento,
														    numeroVeces,
														    medicamentoRecibio,
														    otroMedicamento,
														    pesoPaciente,
														    volumenDiario,
														    diasTratamiento,
														    totalAmpollas,
														    
														    activa,
														    pais,
														    areaProcedencia,
														    lesiones,
														    
														    cara,
													        tronco,
													        superiores,
													        inferiores
														    );
    }
    
    
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaLeishmaniasisDao.modificarFicha(con,
													sire,
													notificar,
												    loginUsuario,
												    codigoFichaLeishmaniasis,
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
									
												    numeroLesiones,
												    localizacionLesiones,
												    anchoLesion1,
												    largoLesion1,
												    anchoLesion2,
												    largoLesion2,
												    anchoLesion3,
												    largoLesion3,
												    cicatrices,
												    tiempo,
												    unidadTiempo,
												    antecedenteTrauma,
												    mucosaAfectada,
												    rinorrea,
												    epistaxis,
												    obstruccion,
												    disfonia,
												    disfagia,
												    hiperemia,
												    ulceracion,
												    perforacion,
												    destruccion,
												    fiebre,
												    hepatomegalia,
												    esplenomegalia,
												    anemia,
												    leucopenia,
												    trombocitopenia,
												    recibioTratamiento,
												    numeroVeces,
												    medicamentoRecibio,
												    otroMedicamento,
												    pesoPaciente,
												    volumenDiario,
												    diasTratamiento,
												    totalAmpollas,
													pais,
												    areaProcedencia,
												    lesiones,
												    cara,
											        tronco,
											        superiores,
											        inferiores);
    }

    
    
    
    

    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaLeishmaniasisDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    	
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
    
    
    
    

    public void cargarDatos(Connection con, int codigo) 
    {
    	this.codigoFichaLeishmaniasis = codigo;
    	
    	ResultSet rs = fichaLeishmaniasisDao.consultaTodoFicha(con,codigo);
    	
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
    
			    this.setNumeroLesiones(rs.getString("numeroLesiones"));
			    this.setCara(rs.getInt("cara"));
			    this.setTronco(rs.getInt("tronco"));
			    this.setSuperiores(rs.getInt("superiores"));
			    this.setInferiores(rs.getInt("inferiores"));
			    this.setLocalizacionLesiones(rs.getInt("localizacionLesiones"));
			    this.setAnchoLesion1(rs.getString("anchoLesion1"));
			    this.setLargoLesion1(rs.getString("largoLesion1"));
			    this.setAnchoLesion2(rs.getString("anchoLesion2"));
			    this.setLargoLesion2(rs.getString("largoLesion2"));
			    this.setAnchoLesion3(rs.getString("anchoLesion3"));
			    this.setLargoLesion3(rs.getString("largoLesion3"));
			    this.setCicatrices(rs.getInt("cicatrices"));
			    this.setTiempo(rs.getString("tiempo"));
			    this.setUnidadTiempo(rs.getInt("unidadTiempo"));
			    this.setAntecedenteTrauma(rs.getInt("antecedenteTrauma"));
			    this.setMucosaAfectada(rs.getInt("mucosaAfectada"));
			    this.setRinorrea(rs.getInt("rinorrea"));
			    this.setEpistaxis(rs.getInt("epistaxis"));
			    this.setObstruccion(rs.getInt("obstruccion"));
			    this.setDisfonia(rs.getInt("disfonia"));
			    this.setDisfagia(rs.getInt("disfagia"));
			    this.setHiperemia(rs.getInt("hiperemia"));
			    this.setUlceracion(rs.getInt("ulceracion"));
			    this.setPerforacion(rs.getInt("perforacion"));
			    this.setDestruccion(rs.getInt("destruccion"));
			    this.setFiebre(rs.getInt("fiebre"));
			    this.setHepatomegalia(rs.getInt("hepatomegalia"));
			    this.setEsplenomegalia(rs.getInt("esplenomegalia"));
			    this.setAnemia(rs.getInt("anemia"));
			    this.setLeucopenia(rs.getInt("leucopenia"));
			    this.setTrombocitopenia(rs.getInt("trombocitopenia"));
			    this.setRecibioTratamiento(rs.getInt("recibiotratamiento"));
			    this.setNumeroVeces(rs.getString("numeroveces"));
			    this.setMedicamentoRecibio(rs.getInt("medicamentoRecibio"));
			    this.setOtroMedicamento(rs.getString("otroMedicamento"));
			    this.setPesoPaciente(rs.getString("pesoPaciente"));
			    this.setVolumenDiario(rs.getString("volumenDiario"));
			    this.setDiasTratamiento(rs.getString("diasTratamiento"));
			    this.setTotalAmpollas(rs.getString("totalAmpollas"));
			    
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
			    
			    
			    ResultSet rs2 = fichaLeishmaniasisDao.consultarDatosLaboratorio(con,codigo);
			    
			    HashMap datosLab = new HashMap();
			    int i=1;
			    
			    while (rs2.next()) {
			    	
			    	datosLab.put("fechaToma"+i,rs2.getString("fechatoma"));
			    	datosLab.put("fechaRecepcion"+i,rs2.getString("fechaRecepcion"));
			    	datosLab.put("muestra"+i,Integer.toString(rs2.getInt("muestra")));
			    	datosLab.put("prueba"+i,Integer.toString(rs2.getInt("prueba")));
			    	datosLab.put("agente"+i,Integer.toString(rs2.getInt("agente")));
			    	datosLab.put("resultado"+i,Integer.toString(rs2.getInt("resultado")));
			    	datosLab.put("fechaResultado"+i,rs2.getString("fecharesultado"));
			    	datosLab.put("valor"+i,rs2.getString("valor"));
			    	datosLab.put("codigo"+i,rs2.getInt("codigofichalaboratorios"));
			    	
			    	i++;
			    }
			    
			    this.setDatosLaboratorio(datosLab);
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


	public int getAreaProcedencia() {
		return areaProcedencia;
	}


	public void setAreaProcedencia(int areaProcedencia) {
		this.areaProcedencia = areaProcedencia;
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


	public int getCodigoFichaLeishmaniasis() {
		return codigoFichaLeishmaniasis;
	}


	public void setCodigoFichaLeishmaniasis(int codigoFichaLeishmaniasis) {
		this.codigoFichaLeishmaniasis = codigoFichaLeishmaniasis;
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


	public int getDestruccion() {
		return destruccion;
	}


	public void setDestruccion(int destruccion) {
		this.destruccion = destruccion;
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


	public String getOtroMedicamento() {
		return otroMedicamento;
	}


	public void setOtroMedicamento(String otroMedicamento) {
		this.otroMedicamento = otroMedicamento;
	}


	public Paciente getPaciente() {
		return paciente;
	}


	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
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


	public int getRecibioTratamiento() {
		return recibioTratamiento;
	}


	public void setRecibioTratamiento(int recibioTratamiento) {
		this.recibioTratamiento = recibioTratamiento;
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


	public HashMap getSintomas() {
		return sintomas;
	}


	public void setSintomas(HashMap sintomas) {
		this.sintomas = sintomas;
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


	public String getVolumenDiario() {
		return volumenDiario;
	}


	public void setVolumenDiario(String volumenDiario) {
		this.volumenDiario = volumenDiario;
	}


	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}


	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
	}


	public HashMap getLesiones() {
		return lesiones;
	}


	public void setLesiones(HashMap lesiones) {
		this.lesiones = lesiones;
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
