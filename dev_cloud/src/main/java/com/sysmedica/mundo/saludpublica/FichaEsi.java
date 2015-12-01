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
import com.sysmedica.dao.FichaEasvDao;
import com.sysmedica.dao.FichaEsiDao;

public class FichaEsi {


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
    
    private int codigoFichaEsi;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
    
    private int clasificacionInicial;
    private int ocupacion;
    private String lugarTrabajo;
    private int vacunaEstacional;
    private int carneVacunacion;
    private int numeroDosis;
    private String fechaUltimaDosis;
    private int verificacion;
    private int fuenteNotificacion;
    private int viajo;
    private String codMunViajo;
    private String codDepViajo;
    private String lugarViajo;
    private int contactoAves;
    private int contactoPersona;
    private int casoEsporadico;
    private int casoEpidemico;
    private int fiebre;
    private int dolorGarganta;
    private int tos;
    private int dificultadRespiratoria;
    private int hipoxia;
    private int taquipnea;
    private int rinorrea;
    private int coriza;
    private int conjuntivitis;
    private int cefalea;
    private int mialgias;
    private int postracion;
    private int infiltrados;
    private int dolorAbdominal;
    
    private HashMap datosLaboratorio;
    

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
    
    FichaEsiDao fichaEsiDao;
    
    
    public void reset()
    {
    	clasificacionInicial = 0;
        ocupacion = 0;
        lugarTrabajo = "";
        vacunaEstacional = 0;
        carneVacunacion = 0;
        numeroDosis = 0;
        fechaUltimaDosis = "";
        verificacion = 0;
        fuenteNotificacion = 0;
        viajo = 0;
        codMunViajo = "";
        codDepViajo = "";
        contactoAves = 0;
        contactoPersona = 0;
        casoEsporadico = 0;
        casoEpidemico = 0;
        fiebre = 0;
        dolorGarganta = 0;
        tos = 0;
        dificultadRespiratoria = 0;
        hipoxia = 0;
        taquipnea = 0;
        rinorrea = 0;
        coriza = 0;
        conjuntivitis = 0;
        cefalea = 0;
        mialgias = 0;
        postracion = 0;
        infiltrados = 0;
        dolorAbdominal = 0;
        
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
			fichaEsiDao = myFactory.getFichaEsiDao();
			wasInited = (fichaEsiDao != null);
		}
		return wasInited;
	}
    
    
    public FichaEsi()
    {
    	reset();
        init(System.getProperty("TIPOBD"));
    }
    
    

    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaEsiDao.insertarFichaCompleta(con,
										    		numeroSolicitud,
													loginUsuario,
													codigoPaciente,
													codigoDiagnostico,
													estadoFicha,
													codigoConvenio,
													nombreProfesional,
												    
												    sire,
													notificar,
													
													codigoFichaEsi,										    
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
												    
												    clasificacionInicial,
												    ocupacion,
												    lugarTrabajo,
												    vacunaEstacional,
												    carneVacunacion,
												    numeroDosis,
												    fechaUltimaDosis,
												    verificacion,
												    fuenteNotificacion,
												    viajo,
												    codMunViajo,
												    codDepViajo,
												    lugarViajo,
												    contactoAves,
												    contactoPersona,
												    casoEsporadico,
												    casoEpidemico,
												    fiebre,
												    dolorGarganta,
												    tos,
												    dificultadRespiratoria,
												    hipoxia,
												    taquipnea,
												    rinorrea,
												    coriza,
												    conjuntivitis,
												    cefalea,
												    mialgias,
												    postracion,
												    infiltrados,
												    dolorAbdominal,
												    
												    activa,
												    pais,
												    areaProcedencia);
    }
    
    
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaEsiDao.modificarFicha(con,
											sire,
											notificar,
										    loginUsuario,
										    codigoFichaEsi,
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
							
										    clasificacionInicial,
										    ocupacion,
										    lugarTrabajo,
										    vacunaEstacional,
										    carneVacunacion,
										    numeroDosis,
										    fechaUltimaDosis,
										    verificacion,
										    fuenteNotificacion,
										    viajo,
										    codMunViajo,
										    codDepViajo,
										    lugarViajo,
										    contactoAves,
										    contactoPersona,
										    casoEsporadico,
										    casoEpidemico,
										    fiebre,
										    dolorGarganta,
										    tos,
										    dificultadRespiratoria,
										    hipoxia,
										    taquipnea,
										    rinorrea,
										    coriza,
										    conjuntivitis,
										    cefalea,
										    mialgias,
										    postracion,
										    infiltrados,
										    dolorAbdominal,
											pais,
										    areaProcedencia);
    }
    
    
    
    
    

    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaEsiDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    	
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
    	this.codigoFichaEsi = codigo;
    	
    	ResultSet rs = fichaEsiDao.consultaTodoFicha(con,codigo);
    	
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
    
			    this.setClasificacionInicial(rs.getInt("clasificacionInicial"));
			//    this.setOcupacion(rs.getInt("ocupacion"));
			    this.setLugarTrabajo(rs.getString("lugarTrabajo"));
			    this.setVacunaEstacional(rs.getInt("vacunaEstacional"));
			    this.setCarneVacunacion(rs.getInt("carneVacunacion"));
			    this.setNumeroDosis(rs.getInt("numeroDosis"));
			    this.setFechaUltimaDosis(rs.getString("fechaUltimaDosis"));
			    this.setVerificacion(rs.getInt("verificacion"));
			    this.setFuenteNotificacion(rs.getInt("fuenteNotificacion"));
			    this.setViajo(rs.getInt("viajo"));
			    this.setCodMunViajo(rs.getString("codMunViajo"));
			    this.setCodDepViajo(rs.getString("codDepViajo"));
			    this.setContactoAves(rs.getInt("contactoAves"));
			    this.setContactoPersona(rs.getInt("contactoPersona"));
			    this.setCasoEsporadico(rs.getInt("casoEsporadico"));
			    this.setCasoEpidemico(rs.getInt("casoEpidemico"));
			    this.setFiebre(rs.getInt("fiebre"));
			    this.setDolorGarganta(rs.getInt("dolorGarganta"));
			    this.setTos(rs.getInt("tos"));
			    this.setDificultadRespiratoria(rs.getInt("dificultadRespiratoria"));
			    this.setHipoxia(rs.getInt("hipoxia"));
			    this.setTaquipnea(rs.getInt("taquipnea"));
			    this.setRinorrea(rs.getInt("rinorrea"));
			    this.setCoriza(rs.getInt("coriza"));
			    this.setConjuntivitis(rs.getInt("conjuntivitis"));
			    this.setCefalea(rs.getInt("cefalea"));
			    this.setMialgias(rs.getInt("mialgias"));
			    this.setPostracion(rs.getInt("postracion"));
			    this.setInfiltrados(rs.getInt("infiltrados"));
			    this.setDolorAbdominal(rs.getInt("dolorAbdominal"));
			    			    
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
			    
			    
			    ResultSet rs2 = fichaEsiDao.consultarDatosLaboratorio(con,codigo);
			    
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

	public int getAreaProcedencia() {
		return areaProcedencia;
	}

	public void setAreaProcedencia(int areaProcedencia) {
		this.areaProcedencia = areaProcedencia;
	}

	public int getCarneVacunacion() {
		return carneVacunacion;
	}

	public void setCarneVacunacion(int carneVacunacion) {
		this.carneVacunacion = carneVacunacion;
	}

	public int getCasoEpidemico() {
		return casoEpidemico;
	}

	public void setCasoEpidemico(int casoEpidemico) {
		this.casoEpidemico = casoEpidemico;
	}

	public int getCasoEsporadico() {
		return casoEsporadico;
	}

	public void setCasoEsporadico(int casoEsporadico) {
		this.casoEsporadico = casoEsporadico;
	}

	public int getCefalea() {
		return cefalea;
	}

	public void setCefalea(int cefalea) {
		this.cefalea = cefalea;
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

	public String getCodDepViajo() {
		return codDepViajo;
	}

	public void setCodDepViajo(String codDepViajo) {
		this.codDepViajo = codDepViajo;
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

	public int getCodigoFichaEsi() {
		return codigoFichaEsi;
	}

	public void setCodigoFichaEsi(int codigoFichaEsi) {
		this.codigoFichaEsi = codigoFichaEsi;
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

	public String getCodMunViajo() {
		return codMunViajo;
	}

	public void setCodMunViajo(String codMunViajo) {
		this.codMunViajo = codMunViajo;
	}

	public int getConjuntivitis() {
		return conjuntivitis;
	}

	public void setConjuntivitis(int conjuntivitis) {
		this.conjuntivitis = conjuntivitis;
	}

	public int getContactoAves() {
		return contactoAves;
	}

	public void setContactoAves(int contactoAves) {
		this.contactoAves = contactoAves;
	}

	public int getContactoPersona() {
		return contactoPersona;
	}

	public void setContactoPersona(int contactoPersona) {
		this.contactoPersona = contactoPersona;
	}

	public int getCoriza() {
		return coriza;
	}

	public void setCoriza(int coriza) {
		this.coriza = coriza;
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

	public int getDificultadRespiratoria() {
		return dificultadRespiratoria;
	}

	public void setDificultadRespiratoria(int dificultadRespiratoria) {
		this.dificultadRespiratoria = dificultadRespiratoria;
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

	public int getDolorAbdominal() {
		return dolorAbdominal;
	}

	public void setDolorAbdominal(int dolorAbdominal) {
		this.dolorAbdominal = dolorAbdominal;
	}

	public int getDolorGarganta() {
		return dolorGarganta;
	}

	public void setDolorGarganta(int dolorGarganta) {
		this.dolorGarganta = dolorGarganta;
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

	public String getFechaUltimaDosis() {
		return fechaUltimaDosis;
	}

	public void setFechaUltimaDosis(String fechaUltimaDosis) {
		this.fechaUltimaDosis = fechaUltimaDosis;
	}

	public int getFiebre() {
		return fiebre;
	}

	public void setFiebre(int fiebre) {
		this.fiebre = fiebre;
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

	public int getHipoxia() {
		return hipoxia;
	}

	public void setHipoxia(int hipoxia) {
		this.hipoxia = hipoxia;
	}

	public boolean isHospitalizado() {
		return hospitalizado;
	}

	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}

	public int getInfiltrados() {
		return infiltrados;
	}

	public void setInfiltrados(int infiltrados) {
		this.infiltrados = infiltrados;
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

	public String getLugarTrabajo() {
		return lugarTrabajo;
	}

	public void setLugarTrabajo(String lugarTrabajo) {
		this.lugarTrabajo = lugarTrabajo;
	}

	public int getMialgias() {
		return mialgias;
	}

	public void setMialgias(int mialgias) {
		this.mialgias = mialgias;
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

	public int getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(int ocupacion) {
		this.ocupacion = ocupacion;
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

	public int getPostracion() {
		return postracion;
	}

	public void setPostracion(int postracion) {
		this.postracion = postracion;
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

	public String getSire() {
		return sire;
	}

	public void setSire(String sire) {
		this.sire = sire;
	}

	public int getTaquipnea() {
		return taquipnea;
	}

	public void setTaquipnea(int taquipnea) {
		this.taquipnea = taquipnea;
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

	public int getTos() {
		return tos;
	}

	public void setTos(int tos) {
		this.tos = tos;
	}

	public int getVacunaEstacional() {
		return vacunaEstacional;
	}

	public void setVacunaEstacional(int vacunaEstacional) {
		this.vacunaEstacional = vacunaEstacional;
	}

	public int getVerificacion() {
		return verificacion;
	}

	public void setVerificacion(int verificacion) {
		this.verificacion = verificacion;
	}

	public int getViajo() {
		return viajo;
	}

	public void setViajo(int viajo) {
		this.viajo = viajo;
	}

	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}

	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
	}

	public String getLugarViajo() {
		return lugarViajo;
	}

	public void setLugarViajo(String lugarViajo) {
		this.lugarViajo = lugarViajo;
	}
    
    
}
