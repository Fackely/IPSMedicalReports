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
import com.sysmedica.dao.FichaDifteriaDao;

public class FichaDifteria {

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
    
    private int codigoFichaDifteria;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
	
    private String nombrePadre;
    private String fechaInvestigacion;
    private int casoIdentificadoPor;
    private int contactoCasoConfirmado;
    private int carneVacunacion;
    private int dosisAplicadas;
    private int tipoVacuna;
    private String cualVacuna;
    private String fechaUltimaDosis;
    private int fiebre;
    private int amigdalitis;
    private int faringitis;
    private int laringitis;
    private int membranas;
    private int complicaciones;
    private int tipoComplicacion;
    private int tratAntibiotico;
    private String tipoAntibiotico;
    private String duracionTratamiento;
    private int antitoxina;
    private String dosisAntitoxina;
    private String fechaAplicacionAntitox;
    private int investigacionCampo;
    private String fechaOperacionBarrido;
    private int numeroContactos;
    private int quimioprofilaxis;
    private int poblacionGrupo1;
    private int poblacionGrupo2;
    private int poblacionGrupo3;
    private int dosisDpt1Grupo1;
    private int dosisDpt1Grupo2;
    private int dosisDpt1Grupo3;
    private int dosisDpt2Grupo1;
    private int dosisDpt2Grupo2;
    private int dosisDpt2Grupo3;  
    private int dosisDpt3Grupo1;
    private int dosisDpt3Grupo2;
    private int dosisDpt3Grupo3;
    private int dosisRef1Grupo1;
    private int dosisRef1Grupo2;
    private int dosisRef1Grupo3;
    private int dosisRef2Grupo1;
    private int dosisRef2Grupo2;
    private int dosisRef2Grupo3;
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
    
    FichaDifteriaDao fichaDifteriaDao;
    
    public void reset()
    {
    	nombrePadre = "";
        fechaInvestigacion = "";
        casoIdentificadoPor = 0;
        contactoCasoConfirmado = 0;
        carneVacunacion = 0;
        dosisAplicadas = 0;
        tipoVacuna = 0;
        cualVacuna = "";
        fechaUltimaDosis = "";
        fiebre = 0;
        amigdalitis = 0;
        faringitis = 0;
        laringitis = 0;
        membranas = 0;
        complicaciones = 0;
        tipoComplicacion = 0;
        tratAntibiotico = 0;
        tipoAntibiotico = "";
        duracionTratamiento = "";
        antitoxina = 0;
        dosisAntitoxina = "";
        fechaAplicacionAntitox = "";
        investigacionCampo = 0;
        fechaOperacionBarrido = "";
        numeroContactos = 0;
        quimioprofilaxis = 0;
        poblacionGrupo1 = 0;
        poblacionGrupo2 = 0;
        poblacionGrupo3 = 0;
        dosisDpt1Grupo1 = 0;
        dosisDpt1Grupo2 = 0;
        dosisDpt1Grupo3 = 0;
        dosisDpt2Grupo1 = 0;
        dosisDpt2Grupo2 = 0;
        dosisDpt2Grupo3 = 0;  
        dosisDpt3Grupo1 = 0;
        dosisDpt3Grupo2 = 0;
        dosisDpt3Grupo3 = 0;
        dosisRef1Grupo1 = 0;
        dosisRef1Grupo2 = 0;
        dosisRef1Grupo3 = 0;
        dosisRef2Grupo1 = 0;
        dosisRef2Grupo2 = 0;
        dosisRef2Grupo3 = 0;
        municipiosVacunados = "";
        
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
			fichaDifteriaDao = myFactory.getFichaDifteriaDao();
			wasInited = (fichaDifteriaDao != null);
		}
		return wasInited;
	}
    
    
    public FichaDifteria()
    {
    	reset();
        init(System.getProperty("TIPOBD"));
    }
    
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaDifteriaDao.insertarFichaCompleta(con,
											    		numeroSolicitud,
														loginUsuario,
														codigoPaciente,
														codigoDiagnostico,
														estadoFicha,
														codigoConvenio,
														nombreProfesional,
													    
													    sire,
														notificar,
														
														codigoFichaDifteria,										    
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
													    casoIdentificadoPor,
													    contactoCasoConfirmado,
													    carneVacunacion,
													    dosisAplicadas,
													    tipoVacuna,
													    cualVacuna,
													    fechaUltimaDosis,
													    fiebre,
													    amigdalitis,
													    faringitis,
													    laringitis,
													    membranas,
													    complicaciones,
													    tipoComplicacion,
													    tratAntibiotico,
													    tipoAntibiotico,
													    duracionTratamiento,
													    antitoxina,
													    dosisAntitoxina,
													    fechaAplicacionAntitox,
													    investigacionCampo,
													    fechaOperacionBarrido,
													    numeroContactos,
													    quimioprofilaxis,
													    poblacionGrupo1,
													    poblacionGrupo2,
													    poblacionGrupo3,
													    dosisDpt1Grupo1,
													    dosisDpt1Grupo2,
													    dosisDpt1Grupo3,
													    dosisDpt2Grupo1,
													    dosisDpt2Grupo2,
													    dosisDpt2Grupo3,  
													    dosisDpt3Grupo1,
													    dosisDpt3Grupo2,
													    dosisDpt3Grupo3,
													    dosisRef1Grupo1,
													    dosisRef1Grupo2,
													    dosisRef1Grupo3,
													    dosisRef2Grupo1,
													    dosisRef2Grupo2,
													    dosisRef2Grupo3,
													    municipiosVacunados,
													    
													    activa,
													    pais,
													    areaProcedencia);
    }
    
    
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaDifteriaDao.modificarFicha(con,
												sire,
												notificar,
											    loginUsuario,
											    codigoFichaDifteria,
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
											    casoIdentificadoPor,
											    contactoCasoConfirmado,
											    carneVacunacion,
											    dosisAplicadas,
											    tipoVacuna,
											    cualVacuna,
											    fechaUltimaDosis,
											    fiebre,
											    amigdalitis,
											    faringitis,
											    laringitis,
											    membranas,
											    complicaciones,
											    tipoComplicacion,
											    tratAntibiotico,
											    tipoAntibiotico,
											    duracionTratamiento,
											    antitoxina,
											    dosisAntitoxina,
											    fechaAplicacionAntitox,
											    investigacionCampo,
											    fechaOperacionBarrido,
											    numeroContactos,
											    quimioprofilaxis,
											    poblacionGrupo1,
											    poblacionGrupo2,
											    poblacionGrupo3,
											    dosisDpt1Grupo1,
											    dosisDpt1Grupo2,
											    dosisDpt1Grupo3,
											    dosisDpt2Grupo1,
											    dosisDpt2Grupo2,
											    dosisDpt2Grupo3,  
											    dosisDpt3Grupo1,
											    dosisDpt3Grupo2,
											    dosisDpt3Grupo3,
											    dosisRef1Grupo1,
											    dosisRef1Grupo2,
											    dosisRef1Grupo3,
											    dosisRef2Grupo1,
											    dosisRef2Grupo2,
											    dosisRef2Grupo3,
											    municipiosVacunados,
												pais,
											    areaProcedencia);
    }
    
    
    

    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaDifteriaDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    	
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
    	this.codigoFichaDifteria = codigo;
    	
    	ResultSet rs = fichaDifteriaDao.consultaTodoFicha(con,codigo);
    	
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
    
			    this.setNombrePadre(rs.getString("nombrePadre"));
			    this.setFechaInvestigacion(rs.getString("fechaInvestigacion"));
			    this.setCasoIdentificadoPor(rs.getInt("casoIdentificadoPor"));
			    this.setContactoCasoConfirmado(rs.getInt("contactoCasoConfirmado"));
			    this.setCarneVacunacion(rs.getInt("carneVacunacion"));
			    this.setDosisAplicadas(rs.getInt("dosisAplicadas"));
			    this.setTipoVacuna(rs.getInt("tipoVacuna"));
			    this.setCualVacuna(rs.getString("cualVacuna"));
			    this.setFechaUltimaDosis(rs.getString("fechaUltimaDosis"));
			    this.setFiebre(rs.getInt("fiebre"));
			    this.setAmigdalitis(rs.getInt("amigdalitis"));
			    this.setFaringitis(rs.getInt("faringitis"));
			    this.setLaringitis(rs.getInt("laringitis"));
			    this.setMembranas(rs.getInt("membranas"));
			    this.setComplicaciones(rs.getInt("complicaciones"));
			    this.setTipoComplicacion(rs.getInt("tipoComplicacion"));
			    this.setTratAntibiotico(rs.getInt("tratAntibiotico"));
			    this.setTipoAntibiotico(rs.getString("tipoAntibiotico"));
			    this.setDuracionTratamiento(rs.getString("duracionTratamiento"));
			    this.setAntitoxina(rs.getInt("antitoxina"));
			    this.setDosisAntitoxina(rs.getString("dosisAntitoxina"));
			    this.setFechaAplicacionAntitox(rs.getString("fechaAplicacionAntitox"));
			    this.setInvestigacionCampo(rs.getInt("investigacionCampo"));
			    this.setFechaOperacionBarrido(rs.getString("fechaOperacionBarrido"));
			    this.setNumeroContactos(rs.getInt("numeroContactos"));
			    this.setQuimioprofilaxis(rs.getInt("quimioprofilaxis"));
			    this.setPoblacionGrupo1(rs.getInt("poblacionGrupo1"));
			    this.setPoblacionGrupo2(rs.getInt("poblacionGrupo2"));
			    this.setPoblacionGrupo3(rs.getInt("poblacionGrupo3"));
			    this.setDosisDpt1Grupo1(rs.getInt("dosisDpt1Grupo1"));
			    this.setDosisDpt1Grupo2(rs.getInt("dosisDpt1Grupo2"));
			    this.setDosisDpt1Grupo3(rs.getInt("dosisDpt1Grupo3"));
			    this.setDosisDpt2Grupo1(rs.getInt("dosisDpt2Grupo1"));
			    this.setDosisDpt2Grupo2(rs.getInt("dosisDpt2Grupo2"));
			    this.setDosisDpt2Grupo3(rs.getInt("dosisDpt2Grupo3"));  
			    this.setDosisDpt3Grupo1(rs.getInt("dosisDpt3Grupo1"));
			    this.setDosisDpt3Grupo2(rs.getInt("dosisDpt3Grupo2"));
			    this.setDosisDpt3Grupo3(rs.getInt("dosisDpt3Grupo3"));
			    this.setDosisRef1Grupo1(rs.getInt("dosisRef1Grupo1"));
			    this.setDosisRef1Grupo2(rs.getInt("dosisRef1Grupo2"));
			    this.setDosisRef1Grupo3(rs.getInt("dosisRef1Grupo3"));
			    this.setDosisRef2Grupo1(rs.getInt("dosisRef2Grupo1"));
			    this.setDosisRef2Grupo2(rs.getInt("dosisRef2Grupo2"));
			    this.setDosisRef2Grupo3(rs.getInt("dosisRef2Grupo3"));
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
			    

			    ResultSet rs2 = fichaDifteriaDao.consultarDatosLaboratorio(con,codigo);
			    
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
	public int getAmigdalitis() {
		return amigdalitis;
	}
	public void setAmigdalitis(int amigdalitis) {
		this.amigdalitis = amigdalitis;
	}
	public int getAntitoxina() {
		return antitoxina;
	}
	public void setAntitoxina(int antitoxina) {
		this.antitoxina = antitoxina;
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
	public int getCasoIdentificadoPor() {
		return casoIdentificadoPor;
	}
	public void setCasoIdentificadoPor(int casoIdentificadoPor) {
		this.casoIdentificadoPor = casoIdentificadoPor;
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
	public int getCodigoFichaDifteria() {
		return codigoFichaDifteria;
	}
	public void setCodigoFichaDifteria(int codigoFichaDifteria) {
		this.codigoFichaDifteria = codigoFichaDifteria;
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
	public int getContactoCasoConfirmado() {
		return contactoCasoConfirmado;
	}
	public void setContactoCasoConfirmado(int contactoCasoConfirmado) {
		this.contactoCasoConfirmado = contactoCasoConfirmado;
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
	public String getDosisAntitoxina() {
		return dosisAntitoxina;
	}
	public void setDosisAntitoxina(String dosisAntitoxina) {
		this.dosisAntitoxina = dosisAntitoxina;
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
	public int getFaringitis() {
		return faringitis;
	}
	public void setFaringitis(int faringitis) {
		this.faringitis = faringitis;
	}
	public String getFechaAplicacionAntitox() {
		return fechaAplicacionAntitox;
	}
	public void setFechaAplicacionAntitox(String fechaAplicacionAntitox) {
		this.fechaAplicacionAntitox = fechaAplicacionAntitox;
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
	public int getLaringitis() {
		return laringitis;
	}
	public void setLaringitis(int laringitis) {
		this.laringitis = laringitis;
	}
	public Logger getLogger() {
		return logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
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
	public int getMembranas() {
		return membranas;
	}
	public void setMembranas(int membranas) {
		this.membranas = membranas;
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
	public String getNombrePadre() {
		return nombrePadre;
	}
	public void setNombrePadre(String nombrePadre) {
		this.nombrePadre = nombrePadre;
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
	public int getNumeroContactos() {
		return numeroContactos;
	}
	public void setNumeroContactos(int numeroContactos) {
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
	public int getPoblacionGrupo1() {
		return poblacionGrupo1;
	}
	public void setPoblacionGrupo1(int poblacionGrupo1) {
		this.poblacionGrupo1 = poblacionGrupo1;
	}
	public int getPoblacionGrupo2() {
		return poblacionGrupo2;
	}
	public void setPoblacionGrupo2(int poblacionGrupo2) {
		this.poblacionGrupo2 = poblacionGrupo2;
	}
	public int getPoblacionGrupo3() {
		return poblacionGrupo3;
	}
	public void setPoblacionGrupo3(int poblacionGrupo3) {
		this.poblacionGrupo3 = poblacionGrupo3;
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
	public int getTratAntibiotico() {
		return tratAntibiotico;
	}
	public void setTratAntibiotico(int tratAntibiotico) {
		this.tratAntibiotico = tratAntibiotico;
	}



	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}



	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
	}
    
}
