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
import com.sysmedica.dao.FichaTosferinaDao;

public class FichaTosferina {

	private Logger logger = Logger.getLogger(FichaTosferina.class);
	
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
    
    private int codigoFichaTosferina;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
    private String nombrePadre;
    private String fechaInvestigacion;
    private int identificacionCaso;
    private int contactoCaso;
    private int carneVacunacion;
    private int dosisAplicadas;
    private int tipoVacuna;
    private String cualVacuna;
    private String fechaUltimaDosis;
    private int etapaEnfermedad;
    private int tos;
    private String duracionTos;
    private int tosParoxistica;
    private int estridor;
    private int apnea;
    private int fiebre;
    private int vomitoPostusivo;
    private int complicaciones;
    private int tipoComplicacion;
    private int tratamientoAntibiotico;
    private String tipoAntibiotico;
    private String duracionTratamiento;
    private int investigacionCampo;
    private String fechaOperacionBarrido;
    private String numeroContactos;
    private int quimioprofilaxis;
    private int totalPoblacion1;
    private int totalPoblacion2;
    private int totalPoblacion3;
    private int totalPoblacion4;
    private int dosisDpt1Grupo1;
    private int dosisDpt1Grupo2;
    private int dosisDpt1Grupo3;
    private int dosisDpt1Grupo4;
    private int dosisDpt2Grupo1;
    private int dosisDpt2Grupo2;
    private int dosisDpt2Grupo3;
    private int dosisDpt2Grupo4;
    private int dosisDpt3Grupo1;
    private int dosisDpt3Grupo2;
    private int dosisDpt3Grupo3;
    private int dosisDpt3Grupo4;
    private int dosisRef1Grupo1;
    private int dosisRef1Grupo2;
    private int dosisRef1Grupo3;
    private int dosisRef1Grupo4;
    private int dosisRef2Grupo1;
    private int dosisRef2Grupo2;
    private int dosisRef2Grupo3;
    private int dosisRef2Grupo4;
    private String municipiosVacunados;
    
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
    
    FichaTosferinaDao fichaTosferinaDao;
    
    public void reset()
    {
    	identificacionCaso = 0;
        contactoCaso = 0;
        carneVacunacion = 0;
        dosisAplicadas = 0;
        tipoVacuna = 0;
        cualVacuna = "";
        fechaUltimaDosis = "";
        etapaEnfermedad = 0;
        tos = 0;
        duracionTos = "";
        tosParoxistica = 0;
        estridor = 0;
        apnea = 0;
        fiebre = 0;
        vomitoPostusivo = 0;
        complicaciones = 0;
        tipoComplicacion = 0;
        tratamientoAntibiotico = 0;
        tipoAntibiotico = "";
        duracionTratamiento = "";
        investigacionCampo = 0;
        fechaOperacionBarrido = "";
        numeroContactos = "";
        quimioprofilaxis = 0;
        totalPoblacion1 = 0;
        totalPoblacion2 = 0;
        totalPoblacion3 = 0;
        totalPoblacion4 = 0;
        dosisDpt1Grupo1 = 0;
        dosisDpt1Grupo2 = 0;
        dosisDpt1Grupo3 = 0;
        dosisDpt1Grupo4 = 0;
        dosisDpt2Grupo1 = 0;
        dosisDpt2Grupo2 = 0;
        dosisDpt2Grupo3 = 0;
        dosisDpt2Grupo4 = 0;
        dosisDpt3Grupo1 = 0;
        dosisDpt3Grupo2 = 0;
        dosisDpt3Grupo3 = 0;
        dosisDpt3Grupo4 = 0;
        dosisRef1Grupo1 = 0;
        dosisRef1Grupo2 = 0;
        dosisRef1Grupo3 = 0;
        dosisRef1Grupo4 = 0;
        dosisRef2Grupo1 = 0;
        dosisRef2Grupo2 = 0;
        dosisRef2Grupo3 = 0;
        dosisRef2Grupo4 = 0;
        municipiosVacunados = "";
        
        datosLaboratorio = new HashMap();
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
			fichaTosferinaDao = myFactory.getFichaTosferinaDao();
			wasInited = (fichaTosferinaDao != null);
		}
		return wasInited;
	}
    
    
    public FichaTosferina()
    {
    	reset();
        init(System.getProperty("TIPOBD"));
    }
    
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaTosferinaDao.insertarFichaCompleta(con,
											    		numeroSolicitud,
														loginUsuario,
														codigoPaciente,
														codigoDiagnostico,
														estadoFicha,
														codigoConvenio,
														nombreProfesional,
													    
													    sire,
														notificar,
														
														codigoFichaTosferina,										    
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
													    
													    nombrePadre,
													    fechaInvestigacion,
													    identificacionCaso,
													    contactoCaso,
													    carneVacunacion,
													    dosisAplicadas,
													    tipoVacuna,
													    cualVacuna,
													    fechaUltimaDosis,
													    etapaEnfermedad,
													    tos,
													    duracionTos,
													    tosParoxistica,
													    estridor,
													    apnea,
													    fiebre,
													    vomitoPostusivo,
													    complicaciones,
													    tipoComplicacion,
													    tratamientoAntibiotico,
													    tipoAntibiotico,
													    duracionTratamiento,
													    investigacionCampo,
													    fechaOperacionBarrido,
													    numeroContactos,
													    quimioprofilaxis,
													    totalPoblacion1,
													    totalPoblacion2,
													    totalPoblacion3,
													    totalPoblacion4,
													    dosisDpt1Grupo1,
													    dosisDpt1Grupo2,
													    dosisDpt1Grupo3,
													    dosisDpt1Grupo4,
													    dosisDpt2Grupo1,
													    dosisDpt2Grupo2,
													    dosisDpt2Grupo3,
													    dosisDpt2Grupo4,
													    dosisDpt3Grupo1,
													    dosisDpt3Grupo2,
													    dosisDpt3Grupo3,
													    dosisDpt3Grupo4,
													    dosisRef1Grupo1,
													    dosisRef1Grupo2,
													    dosisRef1Grupo3,
													    dosisRef1Grupo4,
													    dosisRef2Grupo1,
													    dosisRef2Grupo2,
													    dosisRef2Grupo3,
													    dosisRef2Grupo4,
													    municipiosVacunados,
													    
													    activa,
													    pais,
													    areaProcedencia);
    }
    
    
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaTosferinaDao.modificarFicha(con,
												sire,
												notificar,
											    loginUsuario,
											    codigoFichaTosferina,
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
								
											    nombrePadre,
											    fechaInvestigacion,
											    identificacionCaso,
											    contactoCaso,
											    carneVacunacion,
											    dosisAplicadas,
											    tipoVacuna,
											    cualVacuna,
											    fechaUltimaDosis,
											    etapaEnfermedad,
											    tos,
											    duracionTos,
											    tosParoxistica,
											    estridor,
											    apnea,
											    fiebre,
											    vomitoPostusivo,
											    complicaciones,
											    tipoComplicacion,
											    tratamientoAntibiotico,
											    tipoAntibiotico,
											    duracionTratamiento,
											    investigacionCampo,
											    fechaOperacionBarrido,
											    numeroContactos,
											    quimioprofilaxis,
											    totalPoblacion1,
											    totalPoblacion2,
											    totalPoblacion3,
											    totalPoblacion4,
											    dosisDpt1Grupo1,
											    dosisDpt1Grupo2,
											    dosisDpt1Grupo3,
											    dosisDpt1Grupo4,
											    dosisDpt2Grupo1,
											    dosisDpt2Grupo2,
											    dosisDpt2Grupo3,
											    dosisDpt2Grupo4,
											    dosisDpt3Grupo1,
											    dosisDpt3Grupo2,
											    dosisDpt3Grupo3,
											    dosisDpt3Grupo4,
											    dosisRef1Grupo1,
											    dosisRef1Grupo2,
											    dosisRef1Grupo3,
											    dosisRef1Grupo4,
											    dosisRef2Grupo1,
											    dosisRef2Grupo2,
											    dosisRef2Grupo3,
											    dosisRef2Grupo4,
											    municipiosVacunados,
												pais,
											    areaProcedencia);
    }
    
    
    
    
    

    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaTosferinaDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    	
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
    	this.codigoFichaTosferina = codigo;
    	
    	ResultSet rs = fichaTosferinaDao.consultaTodoFicha(con,codigo);
    	
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
			    
			    this.setIdentificacionCaso(rs.getInt("identificacionCaso"));
			    this.setContactoCaso(rs.getInt("contactoCaso"));
			    this.setCarneVacunacion(rs.getInt("carneVacunacion"));
			    this.setDosisAplicadas(rs.getInt("dosisAplicadas"));
			    this.setTipoVacuna(rs.getInt("tipoVacuna"));
			    this.setCualVacuna(rs.getString("cualVacuna"));
			    this.setFechaUltimaDosis(rs.getString("fechaUltimaDosis"));
			    this.setEtapaEnfermedad(rs.getInt("etapaEnfermedad"));
			    this.setTos(rs.getInt("tos"));
			    this.setDuracionTos(rs.getString("duracionTos"));
			    this.setTosParoxistica(rs.getInt("tosParoxistica"));
			    this.setEstridor(rs.getInt("estridor"));
			    this.setApnea(rs.getInt("apnea"));
			    this.setFiebre(rs.getInt("fiebre"));
			    this.setVomitoPostusivo(rs.getInt("vomitoPostusivo"));
			    this.setComplicaciones(rs.getInt("complicaciones"));
			    this.setTipoComplicacion(rs.getInt("tipoComplicacion"));
			    this.setTratamientoAntibiotico(rs.getInt("tratamientoAntibiotico"));
			    this.setTipoAntibiotico(rs.getString("tipoAntibiotico"));
			    this.setDuracionTratamiento(rs.getString("duracionTratamiento"));
			    this.setInvestigacionCampo(rs.getInt("investigacionCampo"));
			    this.setFechaOperacionBarrido(rs.getString("fechaOperacionBarrido"));
			    this.setNumeroContactos(rs.getString("numeroContactos"));
			    this.setQuimioprofilaxis(rs.getInt("quimioprofilaxis"));
			    this.setTotalPoblacion1(rs.getInt("totalPoblacion1"));
			    this.setTotalPoblacion2(rs.getInt("totalPoblacion2"));
			    this.setTotalPoblacion3(rs.getInt("totalPoblacion3"));
			    this.setTotalPoblacion4(rs.getInt("totalPoblacion4"));
			    this.setDosisDpt1Grupo1(rs.getInt("dosisDpt1Grupo1"));
			    this.setDosisDpt1Grupo2(rs.getInt("dosisDpt1Grupo2"));
			    this.setDosisDpt1Grupo3(rs.getInt("dosisDpt1Grupo3"));
			    this.setDosisDpt1Grupo4(rs.getInt("dosisDpt1Grupo4"));
			    this.setDosisDpt2Grupo1(rs.getInt("dosisDpt2Grupo1"));
			    this.setDosisDpt2Grupo2(rs.getInt("dosisDpt2Grupo2"));
			    this.setDosisDpt2Grupo3(rs.getInt("dosisDpt2Grupo3"));
			    this.setDosisDpt2Grupo4(rs.getInt("dosisDpt2Grupo4"));
			    this.setDosisDpt3Grupo1(rs.getInt("dosisDpt3Grupo1"));
			    this.setDosisDpt3Grupo2(rs.getInt("dosisDpt3Grupo2"));
			    this.setDosisDpt3Grupo3(rs.getInt("dosisDpt3Grupo3"));
			    this.setDosisDpt3Grupo4(rs.getInt("dosisDpt3Grupo4"));
			    this.setDosisRef1Grupo1(rs.getInt("dosisRef1Grupo1"));
			    this.setDosisRef1Grupo2(rs.getInt("dosisRef1Grupo2"));
			    this.setDosisRef1Grupo3(rs.getInt("dosisRef1Grupo3"));
			    this.setDosisRef1Grupo4(rs.getInt("dosisRef1Grupo4"));
			    this.setDosisRef2Grupo1(rs.getInt("dosisRef2Grupo1"));
			    this.setDosisRef2Grupo2(rs.getInt("dosisRef2Grupo2"));
			    this.setDosisRef2Grupo3(rs.getInt("dosisRef2Grupo3"));
			    this.setDosisRef2Grupo4(rs.getInt("dosisRef2Grupo4"));
			    this.setMunicipiosVacunados(rs.getString("municipiosVacunados"));
			    
			    
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



	public int getApnea() {
		return apnea;
	}



	public void setApnea(int apnea) {
		this.apnea = apnea;
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



	public int getCodigoFichaTosferina() {
		return codigoFichaTosferina;
	}



	public void setCodigoFichaTosferina(int codigoFichaTosferina) {
		this.codigoFichaTosferina = codigoFichaTosferina;
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



	public int getComplicaciones() {
		return complicaciones;
	}



	public void setComplicaciones(int complicaciones) {
		this.complicaciones = complicaciones;
	}



	public int getContactoCaso() {
		return contactoCaso;
	}



	public void setContactoCaso(int contactoCaso) {
		this.contactoCaso = contactoCaso;
	}



	public String getCualVacuna() {
		return cualVacuna;
	}



	public void setCualVacuna(String cualVacuna) {
		this.cualVacuna = cualVacuna;
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



	public int getDosisAplicadas() {
		return dosisAplicadas;
	}



	public void setDosisAplicadas(int dosisAplicadas) {
		this.dosisAplicadas = dosisAplicadas;
	}



	public int getDosisDpt1Grupo1() {
		return dosisDpt1Grupo1;
	}



	public void setDosisDpt1Grupo1(int dosisDpt1Grupo1) {
		this.dosisDpt1Grupo1 = dosisDpt1Grupo1;
	}



	public int getDosisDpt1Grupo2() {
		return dosisDpt1Grupo2;
	}



	public void setDosisDpt1Grupo2(int dosisDpt1Grupo2) {
		this.dosisDpt1Grupo2 = dosisDpt1Grupo2;
	}



	public int getDosisDpt1Grupo3() {
		return dosisDpt1Grupo3;
	}



	public void setDosisDpt1Grupo3(int dosisDpt1Grupo3) {
		this.dosisDpt1Grupo3 = dosisDpt1Grupo3;
	}



	public int getDosisDpt1Grupo4() {
		return dosisDpt1Grupo4;
	}



	public void setDosisDpt1Grupo4(int dosisDpt1Grupo4) {
		this.dosisDpt1Grupo4 = dosisDpt1Grupo4;
	}



	public int getDosisDpt2Grupo1() {
		return dosisDpt2Grupo1;
	}



	public void setDosisDpt2Grupo1(int dosisDpt2Grupo1) {
		this.dosisDpt2Grupo1 = dosisDpt2Grupo1;
	}



	public int getDosisDpt2Grupo2() {
		return dosisDpt2Grupo2;
	}



	public void setDosisDpt2Grupo2(int dosisDpt2Grupo2) {
		this.dosisDpt2Grupo2 = dosisDpt2Grupo2;
	}



	public int getDosisDpt2Grupo3() {
		return dosisDpt2Grupo3;
	}



	public void setDosisDpt2Grupo3(int dosisDpt2Grupo3) {
		this.dosisDpt2Grupo3 = dosisDpt2Grupo3;
	}



	public int getDosisDpt2Grupo4() {
		return dosisDpt2Grupo4;
	}



	public void setDosisDpt2Grupo4(int dosisDpt2Grupo4) {
		this.dosisDpt2Grupo4 = dosisDpt2Grupo4;
	}



	public int getDosisDpt3Grupo1() {
		return dosisDpt3Grupo1;
	}



	public void setDosisDpt3Grupo1(int dosisDpt3Grupo1) {
		this.dosisDpt3Grupo1 = dosisDpt3Grupo1;
	}



	public int getDosisDpt3Grupo2() {
		return dosisDpt3Grupo2;
	}



	public void setDosisDpt3Grupo2(int dosisDpt3Grupo2) {
		this.dosisDpt3Grupo2 = dosisDpt3Grupo2;
	}



	public int getDosisDpt3Grupo3() {
		return dosisDpt3Grupo3;
	}



	public void setDosisDpt3Grupo3(int dosisDpt3Grupo3) {
		this.dosisDpt3Grupo3 = dosisDpt3Grupo3;
	}



	public int getDosisDpt3Grupo4() {
		return dosisDpt3Grupo4;
	}



	public void setDosisDpt3Grupo4(int dosisDpt3Grupo4) {
		this.dosisDpt3Grupo4 = dosisDpt3Grupo4;
	}



	public int getDosisRef1Grupo1() {
		return dosisRef1Grupo1;
	}



	public void setDosisRef1Grupo1(int dosisRef1Grupo1) {
		this.dosisRef1Grupo1 = dosisRef1Grupo1;
	}



	public int getDosisRef1Grupo2() {
		return dosisRef1Grupo2;
	}



	public void setDosisRef1Grupo2(int dosisRef1Grupo2) {
		this.dosisRef1Grupo2 = dosisRef1Grupo2;
	}



	public int getDosisRef1Grupo3() {
		return dosisRef1Grupo3;
	}



	public void setDosisRef1Grupo3(int dosisRef1Grupo3) {
		this.dosisRef1Grupo3 = dosisRef1Grupo3;
	}



	public int getDosisRef1Grupo4() {
		return dosisRef1Grupo4;
	}



	public void setDosisRef1Grupo4(int dosisRef1Grupo4) {
		this.dosisRef1Grupo4 = dosisRef1Grupo4;
	}



	public int getDosisRef2Grupo1() {
		return dosisRef2Grupo1;
	}



	public void setDosisRef2Grupo1(int dosisRef2Grupo1) {
		this.dosisRef2Grupo1 = dosisRef2Grupo1;
	}



	public int getDosisRef2Grupo2() {
		return dosisRef2Grupo2;
	}



	public void setDosisRef2Grupo2(int dosisRef2Grupo2) {
		this.dosisRef2Grupo2 = dosisRef2Grupo2;
	}



	public int getDosisRef2Grupo3() {
		return dosisRef2Grupo3;
	}



	public void setDosisRef2Grupo3(int dosisRef2Grupo3) {
		this.dosisRef2Grupo3 = dosisRef2Grupo3;
	}



	public int getDosisRef2Grupo4() {
		return dosisRef2Grupo4;
	}



	public void setDosisRef2Grupo4(int dosisRef2Grupo4) {
		this.dosisRef2Grupo4 = dosisRef2Grupo4;
	}



	public String getDuracionTos() {
		return duracionTos;
	}



	public void setDuracionTos(String duracionTos) {
		this.duracionTos = duracionTos;
	}



	public String getDuracionTratamiento() {
		return duracionTratamiento;
	}



	public void setDuracionTratamiento(String duracionTratamiento) {
		this.duracionTratamiento = duracionTratamiento;
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



	public int getEstridor() {
		return estridor;
	}



	public void setEstridor(int estridor) {
		this.estridor = estridor;
	}



	public int getEtapaEnfermedad() {
		return etapaEnfermedad;
	}



	public void setEtapaEnfermedad(int etapaEnfermedad) {
		this.etapaEnfermedad = etapaEnfermedad;
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



	public String getFechaOperacionBarrido() {
		return fechaOperacionBarrido;
	}



	public void setFechaOperacionBarrido(String fechaOperacionBarrido) {
		this.fechaOperacionBarrido = fechaOperacionBarrido;
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



	public String getGenero() {
		return genero;
	}



	public void setGenero(String genero) {
		this.genero = genero;
	}



	public boolean isHospitalizado() {
		return hospitalizado;
	}



	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}



	public int getIdentificacionCaso() {
		return identificacionCaso;
	}



	public void setIdentificacionCaso(int identificacionCaso) {
		this.identificacionCaso = identificacionCaso;
	}



	public int getInstitucionAtendio() {
		return institucionAtendio;
	}



	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
	}



	public int getInvestigacionCampo() {
		return investigacionCampo;
	}



	public void setInvestigacionCampo(int investigacionCampo) {
		this.investigacionCampo = investigacionCampo;
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



	public String getMunicipioNotifica() {
		return municipioNotifica;
	}



	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}



	public String getMunicipiosVacunados() {
		return municipiosVacunados;
	}



	public void setMunicipiosVacunados(String municipiosVacunados) {
		this.municipiosVacunados = municipiosVacunados;
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



	public String getNumeroContactos() {
		return numeroContactos;
	}



	public void setNumeroContactos(String numeroContactos) {
		this.numeroContactos = numeroContactos;
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



	public String getPais() {
		return pais;
	}



	public void setPais(String pais) {
		this.pais = pais;
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



	public int getQuimioprofilaxis() {
		return quimioprofilaxis;
	}



	public void setQuimioprofilaxis(int quimioprofilaxis) {
		this.quimioprofilaxis = quimioprofilaxis;
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



	public String getTipoAntibiotico() {
		return tipoAntibiotico;
	}



	public void setTipoAntibiotico(String tipoAntibiotico) {
		this.tipoAntibiotico = tipoAntibiotico;
	}



	public int getTipoCaso() {
		return tipoCaso;
	}



	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}



	public int getTipoComplicacion() {
		return tipoComplicacion;
	}



	public void setTipoComplicacion(int tipoComplicacion) {
		this.tipoComplicacion = tipoComplicacion;
	}



	public int getTipoVacuna() {
		return tipoVacuna;
	}



	public void setTipoVacuna(int tipoVacuna) {
		this.tipoVacuna = tipoVacuna;
	}



	public int getTos() {
		return tos;
	}



	public void setTos(int tos) {
		this.tos = tos;
	}



	public int getTosParoxistica() {
		return tosParoxistica;
	}



	public void setTosParoxistica(int tosParoxistica) {
		this.tosParoxistica = tosParoxistica;
	}



	public int getTotalPoblacion1() {
		return totalPoblacion1;
	}



	public void setTotalPoblacion1(int totalPoblacion1) {
		this.totalPoblacion1 = totalPoblacion1;
	}



	public int getTotalPoblacion2() {
		return totalPoblacion2;
	}



	public void setTotalPoblacion2(int totalPoblacion2) {
		this.totalPoblacion2 = totalPoblacion2;
	}



	public int getTotalPoblacion3() {
		return totalPoblacion3;
	}



	public void setTotalPoblacion3(int totalPoblacion3) {
		this.totalPoblacion3 = totalPoblacion3;
	}



	public int getTotalPoblacion4() {
		return totalPoblacion4;
	}



	public void setTotalPoblacion4(int totalPoblacion4) {
		this.totalPoblacion4 = totalPoblacion4;
	}



	public int getTratamientoAntibiotico() {
		return tratamientoAntibiotico;
	}



	public void setTratamientoAntibiotico(int tratamientoAntibiotico) {
		this.tratamientoAntibiotico = tratamientoAntibiotico;
	}



	public int getVomitoPostusivo() {
		return vomitoPostusivo;
	}



	public void setVomitoPostusivo(int vomitoPostusivo) {
		this.vomitoPostusivo = vomitoPostusivo;
	}



	public String getFechaInvestigacion() {
		return fechaInvestigacion;
	}



	public void setFechaInvestigacion(String fechaInvestigacion) {
		this.fechaInvestigacion = fechaInvestigacion;
	}



	public String getNombrePadre() {
		return nombrePadre;
	}



	public void setNombrePadre(String nombrePadre) {
		this.nombrePadre = nombrePadre;
	}



	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}



	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
	}
    
    
    
}
