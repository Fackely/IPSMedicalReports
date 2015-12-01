/*
 * Creado en 14-jul-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
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
import com.sysmedica.dao.FichaSarampionDao;
import com.sysmedica.mundo.saludpublica.Paciente;


/**
 * @author santiago
 *
 */
public class FichaSarampion {

	private Logger logger = Logger.getLogger(FichaSarampion.class);
	
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
    private int codigoFichaSarampion;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estado;
    
    private String nombrePadre;
    private String ocupacionPadre;
    private String direccionTrabajoPadre;
    private String fechaVisita1;
    private int fuenteNotificacion;
    private int vacunaSarampion;
    private int numeroDosisSarampion;
    private String fechaUltimaDosisSarampion;
    private int fuenteDatosSarampion;
    private int vacunaRubeola;
    private int numeroDosisRubeola;
    private String fechaUltimaDosisRubeola;
    private int fuenteDatosRubeola;
    
    private String fechaVisitaDomiciliaria;
    private int fiebre;
    private String fechaInicioFiebre;
    private int tipoErupcion;
    private String fechaInicioErupcion;
    private int duracionErupcion;
    private int tos;
    private int coriza;
    private int conjuntivitis;
    private int adenopatia;
    private int artralgia;
    private int embarazada;
    private int numeroSemanas;
    private String lugarParto;
    private String municipioParto;
    private String departamentoParto;
    
    private int huboContacto;
    private int huboCasoConfirmado;
    private int huboViaje;
    private String lugarViaje;
    private String municipioViaje;
    private String departamentoViaje;
    private int huboContactoEmbarazada;
    private int diagnosticoFinal;
    private String nombreInvestigador;
    private String telefonoInvestigador;
    
    private HashMap datosLaboratorio;
    
    private String pais;
    private int areaProcedencia;
    
    /*
    private String primerNombreUsuario;
    private String segundoNombreUsuario;
    private String primerApellidoUsuario;
    private String segundoApellidoUsuario;
    private String identificacionUsuario;
    */
    private Paciente paciente;
    
    private FichaSarampionDao fichaSarampionDao;
    
    public static int codigoGlobal;
    
    private boolean activa;
    
    public FichaSarampion() {
    	
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
				fichaSarampionDao = myFactory.getFichaSarampionDao();
				wasInited = (fichaSarampionDao != null);
			}
			return wasInited;
	}
    
    public void reset() {
    	
    	sire="";
    	nombrePadre = "";
        ocupacionPadre = "";
        direccionTrabajoPadre = "";
        fechaVisita1 = "";
        fuenteNotificacion = 0;
        vacunaSarampion = 0;
        numeroDosisSarampion = 0;
        fechaUltimaDosisSarampion = "";
        fuenteDatosSarampion = 0;
        vacunaRubeola = 0;
        numeroDosisRubeola = 0;
        fechaUltimaDosisRubeola = "";
        fuenteDatosRubeola = 0;
        
        fechaVisitaDomiciliaria = "";
        fiebre = 1;
        fechaInicioFiebre = "";
        tipoErupcion = 0;
        fechaInicioErupcion = "";
        duracionErupcion = 0;
        tos = 0;
        coriza = 0;
        conjuntivitis = 0;
        adenopatia = 0;
        artralgia = 0;
        embarazada = 0;
        numeroSemanas = 0;
        lugarParto = "";
        
        huboContacto = 0;
        huboCasoConfirmado = 0;
        huboViaje = 0;
        lugarViaje = "";
        huboContactoEmbarazada = 0;
        diagnosticoFinal = 0;
        nombreInvestigador = "";
        telefonoInvestigador = "";
        
        datosLaboratorio = new HashMap();
        paciente = new Paciente();
        
        fechaInicioSint = "";
        tipoCaso = 0;
        estaVivo = false;
    }
    
    
    public int insertarFicha(Connection con) {
    	
    	return fichaSarampionDao.insertarFicha(con,
												notificar,
											    loginUsuario,
											    codigoFichaSarampion,
											    codigoPaciente,
											    codigoDiagnostico,
											    codigoNotificacion,
											    numeroSolicitud,
											    estado,
											    codigoConvenio,
												nombreProfesional,
											    
											    nombrePadre,
											    ocupacionPadre,
											    direccionTrabajoPadre,
											    fechaVisita1,
											    fuenteNotificacion,
											    vacunaSarampion,
											    numeroDosisSarampion,
											    fechaUltimaDosisSarampion,
											    fuenteDatosSarampion,
											    vacunaRubeola,
											    numeroDosisRubeola,
											    fechaUltimaDosisRubeola,
											    fuenteDatosRubeola,
											    
											    fechaVisitaDomiciliaria,
											    fiebre,
											    fechaInicioFiebre,
											    tipoErupcion,
											    fechaInicioErupcion,
											    duracionErupcion,
											    tos,
											    coriza,
											    conjuntivitis,
											    adenopatia,
											    artralgia,
											    embarazada,
											    numeroSemanas,
											    lugarParto,
											    
											    huboContacto,
											    huboCasoConfirmado,
											    huboViaje,
											    lugarViaje,
											    huboContactoEmbarazada,
											    diagnosticoFinal,
											    
											    datosLaboratorio);
    }
    
    
    public void cargarDatos(Connection con, int codigo) {
        
        this.codigoFichaSarampion = codigo;
        
        ResultSet rs = fichaSarampionDao.consultaTodoFicha(con,codigo);
        
        try {
			if (rs.next()) {
				/*
				this.setPrimerNombreUsuario(rs.getString("primerNombreUsuario"));
			    this.setSegundoNombreUsuario(rs.getString("segundoNombreUsuario"));
			    this.setPrimerApellidoUsuario(rs.getString("primerApellidoUsuario"));
			    this.setSegundoApellidoUsuario(rs.getString("segundoApellidoUsuario"));
			    this.setIdentificacionUsuario(rs.getString("identificacionUsuario"));
			    */
			    this.setSire(rs.getString("sire"));
			    this.setEstado(rs.getInt("estado"));
			    this.setAdenopatia(rs.getInt("adenopatia"));
			    this.setArtralgia(rs.getInt("artralgia"));
			    this.setConjuntivitis(rs.getInt("conjuntivitis"));
			    this.setCoriza(rs.getInt("coriza"));
			    this.setDiagnosticoFinal(rs.getInt("diagnosticoFinal"));
			    this.setDireccionTrabajoPadre(rs.getString("direccionTrabajoPadre"));
			    this.setDuracionErupcion(rs.getInt("duracionErupcion"));
			    this.setEmbarazada(rs.getInt("embarazada"));
			    this.setFechaInicioErupcion(rs.getString("fechaInicioErupcion"));
			    this.setFechaInicioFiebre(rs.getString("fechaInicioFiebre"));
			    this.setFechaUltimaDosisRubeola(rs.getString("fechaUltimaDosisRubeola"));
			    this.setFechaUltimaDosisSarampion(rs.getString("fechaUltimaDosisSarampion"));
			    this.setFechaVisita1(rs.getString("fechaVisita1"));
			    this.setFechaVisitaDomiciliaria(rs.getString("fechaVisitaDomiciliaria"));
			    this.setFuenteDatosRubeola(rs.getInt("fuenteDatosRubeola"));
			    this.setFuenteDatosSarampion(rs.getInt("fuenteDatosSarampion"));
			    this.setFuenteNotificacion(rs.getInt("fuenteNotificacion"));
			    this.setHuboCasoConfirmado(rs.getInt("huboCasoConfirmado"));
			    this.setHuboContacto(rs.getInt("huboContacto"));
			    this.setHuboContactoEmbarazada(rs.getInt("huboContactoEmbarazada"));
			    this.setHuboViaje(rs.getInt("huboViaje"));
			    this.setMunicipioParto(rs.getString("municipioParto"));
			    this.setDepartamentoParto(rs.getString("departamentoParto"));
			    this.setMunicipioViaje(rs.getString("municipioViaje"));
			    this.setDepartamentoViaje(rs.getString("departamentoViaje"));
			    this.setNombrePadre(rs.getString("nombrePadre"));
			    this.setNumeroDosisRubeola(rs.getInt("numeroDosisRubeola"));
			    this.setNumeroDosisSarampion(rs.getInt("numeroDosisSarampion"));
			    this.setNumeroSemanas(rs.getInt("numeroSemanas"));
			    this.setOcupacionPadre(rs.getString("ocupacionPadre"));
			    this.setTipoErupcion(rs.getInt("tipoErupcion"));
			    this.setTos(rs.getInt("tos"));
			    this.setVacunaRubeola(rs.getInt("vacunaRubeola"));
			    this.setVacunaSarampion(rs.getInt("vacunaSarampion"));
			    
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
			    this.setFiebre(rs.getInt("fiebre"));
			    this.setNombreInvestigador(rs.getString("nombreInvestigador"));
			    this.setTelefonoInvestigador(rs.getString("telefonoInvestigador"));
			    this.setPais(rs.getString("pais"));
			    this.setAreaProcedencia(rs.getInt("areaProcedencia"));
			    
			    
			    this.setFechaNotificacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaDiligenciamiento")));
			    
			    
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
			    
			    ResultSet rs2 = fichaSarampionDao.consultarDatosLaboratorio(con,codigo);
			    
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
        }
    }
 
    
    
    
    public int modificarFicha(Connection con) {
    	
    	return fichaSarampionDao.modificarFicha(con,
    											sire,
												notificar,
											    loginUsuario,
											    codigoFichaSarampion,
											    codigoPaciente,
											    codigoDiagnostico,
											    codigoNotificacion,
											    numeroSolicitud,
											    estado,
											    
											    nombrePadre,
											    ocupacionPadre,
											    direccionTrabajoPadre,
											    fechaVisita1,
											    fuenteNotificacion,
											    vacunaSarampion,
											    numeroDosisSarampion,
											    fechaUltimaDosisSarampion,
											    fuenteDatosSarampion,
											    vacunaRubeola,
											    numeroDosisRubeola,
											    fechaUltimaDosisRubeola,
											    fuenteDatosRubeola,
											    
											    fechaVisitaDomiciliaria,
											    fiebre,
											    fechaInicioFiebre,
											    tipoErupcion,
											    fechaInicioErupcion,
											    duracionErupcion,
											    tos,
											    coriza,
											    conjuntivitis,
											    adenopatia,
											    artralgia,
											    embarazada,
											    numeroSemanas,
											    lugarParto,
											    
											    huboContacto,
											    huboCasoConfirmado,
											    huboViaje,
											    lugarViaje,
											    huboContactoEmbarazada,
											    diagnosticoFinal,
											    
											    lugarProcedencia,
											    fechaConsultaGeneral,
											    fechaInicioSint,
											    tipoCaso,
											    hospitalizado,
											    fechaHospitalizacion,
											    estaVivo,
											    fechaDefuncion,
											    lugarNotifica,
											    nombreInvestigador,
											    telefonoInvestigador,
											    institucionAtendio,
											    											    
											    datosLaboratorio,
											    pais,
											    areaProcedencia);
    }
    
    
    
    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaSarampionDao.consultaDatosPaciente(con,codigo,empezarnuevo);
    	
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
			//    paciente.setDesplazado(rs.getInt("desplazado"));
			    
			    String grupoPoblacional = rs.getString("grupoPoblacional");
			    int desplazado = 0;
			    
			    if (grupoPoblacional.equals(ConstantesIntegridadDominio.acronimoDesplazados)) {
			    	desplazado = 1;
			    }
			    
			    paciente.setDesplazado(desplazado);
			    
			    paciente.setTipoId(rs.getString("tipoId"));
			    paciente.setPaisExpedicion(rs.getString("codigo_pais_id"));
			    paciente.setPaisNacimiento(rs.getString("codigo_pais_nacimiento"));
			    paciente.setPaisResidencia(rs.getString("codigo_pais_vivienda"));
			    
			    if (empezarnuevo==false) {
				    paciente.setAseguradora(rs.getString("aseguradora"));
				    paciente.setRegimenSalud(rs.getString("regimenSalud"));
			    }
			    
			    paciente.setGrupoPoblacional(ValoresPorDefecto.getIntegridadDominio(grupoPoblacional).toString());
			    
    		}
    	}
    	catch (SQLException sqle) {
            
    		sqle.printStackTrace();
        }
    }
    
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaSarampionDao.insertarFichaCompleta(con,
											    		numeroSolicitud,
														loginUsuario,
														codigoPaciente,
														codigoDiagnostico,
														estado,
														codigoConvenio,
														nombreProfesional,
														datosLaboratorio,
													    
													    sire,
														notificar,
													    loginUsuario,
													    
													    nombrePadre,
													    ocupacionPadre,
													    direccionTrabajoPadre,
													    fechaVisita1,
													    fuenteNotificacion,
													    vacunaSarampion,
													    numeroDosisSarampion,
													    fechaUltimaDosisSarampion,
													    fuenteDatosSarampion,
													    vacunaRubeola,
													    numeroDosisRubeola,
													    fechaUltimaDosisRubeola,
													    fuenteDatosRubeola,
													    
													    fechaVisitaDomiciliaria,
													    fiebre,
													    fechaInicioFiebre,
													    tipoErupcion,
													    fechaInicioErupcion,
													    duracionErupcion,
													    tos,
													    coriza,
													    conjuntivitis,
													    adenopatia,
													    artralgia,
													    embarazada,
													    numeroSemanas,
													    lugarParto,
													    
													    huboContacto,
													    huboCasoConfirmado,
													    huboViaje,
													    lugarViaje,
													    huboContactoEmbarazada,
													    
													    lugarProcedencia,
													    fechaConsultaGeneral,
													    fechaInicioSint,
													    tipoCaso,
													    hospitalizado,
													    fechaHospitalizacion,
													    estaVivo,
													    fechaDefuncion,
													    lugarNotifica,
													    nombreInvestigador,
													    telefonoInvestigador,
													    institucionAtendio,
													    activa,
													    diagnosticoFinal,
													    pais,
													    areaProcedencia);
    }
    
    
    
    public int terminarFicha(Connection con)
    {
    	return fichaSarampionDao.terminarFicha(con,codigoFichaSarampion);    	
    }
    
    
    
	public int getAdenopatia() {
		return adenopatia;
	}
	public void setAdenopatia(int adenopatia) {
		this.adenopatia = adenopatia;
	}
	public int getArtralgia() {
		return artralgia;
	}
	public void setArtralgia(int artralgia) {
		this.artralgia = artralgia;
	}
	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}
	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}
	public int getCodigoFichaSarampion() {
		return codigoFichaSarampion;
	}
	public void setCodigoFichaSarampion(int codigoFichaSarampion) {
		this.codigoFichaSarampion = codigoFichaSarampion;
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
	public int getConjuntivitis() {
		return conjuntivitis;
	}
	public void setConjuntivitis(int conjuntivitis) {
		this.conjuntivitis = conjuntivitis;
	}
	public int getCoriza() {
		return coriza;
	}
	public void setCoriza(int coriza) {
		this.coriza = coriza;
	}
	public int getDiagnosticoFinal() {
		return diagnosticoFinal;
	}
	public void setDiagnosticoFinal(int diagnosticoFinal) {
		this.diagnosticoFinal = diagnosticoFinal;
	}
	public String getDireccionTrabajoPadre() {
		return direccionTrabajoPadre;
	}
	public void setDireccionTrabajoPadre(String direccionTrabajoPadre) {
		this.direccionTrabajoPadre = direccionTrabajoPadre;
	}
	public int getDuracionErupcion() {
		return duracionErupcion;
	}
	public void setDuracionErupcion(int duracionErupcion) {
		this.duracionErupcion = duracionErupcion;
	}
	public int getEmbarazada() {
		return embarazada;
	}
	public void setEmbarazada(int embarazada) {
		this.embarazada = embarazada;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public String getFechaInicioErupcion() {
		return fechaInicioErupcion;
	}
	public void setFechaInicioErupcion(String fechaInicioErupcion) {
		this.fechaInicioErupcion = fechaInicioErupcion;
	}
	public String getFechaInicioFiebre() {
		return fechaInicioFiebre;
	}
	public void setFechaInicioFiebre(String fechaInicioFiebre) {
		this.fechaInicioFiebre = fechaInicioFiebre;
	}
	public String getFechaUltimaDosisRubeola() {
		return fechaUltimaDosisRubeola;
	}
	public void setFechaUltimaDosisRubeola(String fechaUltimaDosisRubeola) {
		this.fechaUltimaDosisRubeola = fechaUltimaDosisRubeola;
	}
	public String getFechaUltimaDosisSarampion() {
		return fechaUltimaDosisSarampion;
	}
	public void setFechaUltimaDosisSarampion(String fechaUltimaDosisSarampion) {
		this.fechaUltimaDosisSarampion = fechaUltimaDosisSarampion;
	}
	public String getFechaVisita1() {
		return fechaVisita1;
	}
	public void setFechaVisita1(String fechaVisita1) {
		this.fechaVisita1 = fechaVisita1;
	}
	public String getFechaVisitaDomiciliaria() {
		return fechaVisitaDomiciliaria;
	}
	public void setFechaVisitaDomiciliaria(String fechaVisitaDomiciliaria) {
		this.fechaVisitaDomiciliaria = fechaVisitaDomiciliaria;
	}
	public FichaSarampionDao getFichaSarampionDao() {
		return fichaSarampionDao;
	}
	public void setFichaSarampionDao(FichaSarampionDao fichaSarampionDao) {
		this.fichaSarampionDao = fichaSarampionDao;
	}
	public int getFiebre() {
		return fiebre;
	}
	public void setFiebre(int fiebre) {
		this.fiebre = fiebre;
	}
	public int getFuenteDatosRubeola() {
		return fuenteDatosRubeola;
	}
	public void setFuenteDatosRubeola(int fuenteDatosRubeola) {
		this.fuenteDatosRubeola = fuenteDatosRubeola;
	}
	public int getFuenteDatosSarampion() {
		return fuenteDatosSarampion;
	}
	public void setFuenteDatosSarampion(int fuenteDatosSarampion) {
		this.fuenteDatosSarampion = fuenteDatosSarampion;
	}
	public int getFuenteNotificacion() {
		return fuenteNotificacion;
	}
	public void setFuenteNotificacion(int fuenteNotificacion) {
		this.fuenteNotificacion = fuenteNotificacion;
	}
	public int getHuboCasoConfirmado() {
		return huboCasoConfirmado;
	}
	public void setHuboCasoConfirmado(int huboCasoConfirmado) {
		this.huboCasoConfirmado = huboCasoConfirmado;
	}
	public int getHuboContacto() {
		return huboContacto;
	}
	public void setHuboContacto(int huboContacto) {
		this.huboContacto = huboContacto;
	}
	public int getHuboContactoEmbarazada() {
		return huboContactoEmbarazada;
	}
	public void setHuboContactoEmbarazada(int huboContactoEmbarazada) {
		this.huboContactoEmbarazada = huboContactoEmbarazada;
	}
	public int getHuboViaje() {
		return huboViaje;
	}
	public void setHuboViaje(int huboViaje) {
		this.huboViaje = huboViaje;
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
	public String getLugarParto() {
		return lugarParto;
	}
	public void setLugarParto(String lugarParto) {
		this.lugarParto = lugarParto;
	}
	public String getLugarViaje() {
		return lugarViaje;
	}
	public void setLugarViaje(String lugarViaje) {
		this.lugarViaje = lugarViaje;
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
	public int getNumeroDosisRubeola() {
		return numeroDosisRubeola;
	}
	public void setNumeroDosisRubeola(int numeroDosisRubeola) {
		this.numeroDosisRubeola = numeroDosisRubeola;
	}
	public int getNumeroDosisSarampion() {
		return numeroDosisSarampion;
	}
	public void setNumeroDosisSarampion(int numeroDosisSarampion) {
		this.numeroDosisSarampion = numeroDosisSarampion;
	}
	public int getNumeroSemanas() {
		return numeroSemanas;
	}
	public void setNumeroSemanas(int numeroSemanas) {
		this.numeroSemanas = numeroSemanas;
	}
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	public String getOcupacionPadre() {
		return ocupacionPadre;
	}
	public void setOcupacionPadre(String ocupacionPadre) {
		this.ocupacionPadre = ocupacionPadre;
	}
	public int getTipoErupcion() {
		return tipoErupcion;
	}
	public void setTipoErupcion(int tipoErupcion) {
		this.tipoErupcion = tipoErupcion;
	}
	public int getTos() {
		return tos;
	}
	public void setTos(int tos) {
		this.tos = tos;
	}
	public int getVacunaRubeola() {
		return vacunaRubeola;
	}
	public void setVacunaRubeola(int vacunaRubeola) {
		this.vacunaRubeola = vacunaRubeola;
	}
	public int getVacunaSarampion() {
		return vacunaSarampion;
	}
	public void setVacunaSarampion(int vacunaSarampion) {
		this.vacunaSarampion = vacunaSarampion;
	}
	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}
	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
	}



	public Paciente getPaciente() {
		return paciente;
	}


	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public String getSire() {
		return sire;
	}

	public void setSire(String sire) {
		this.sire = sire;
	}

	public String getDepartamentoParto() {
		return departamentoParto;
	}

	public void setDepartamentoParto(String departamentoParto) {
		this.departamentoParto = departamentoParto;
	}

	public String getDepartamentoViaje() {
		return departamentoViaje;
	}

	public void setDepartamentoViaje(String departamentoViaje) {
		this.departamentoViaje = departamentoViaje;
	}

	public String getMunicipioParto() {
		return municipioParto;
	}

	public void setMunicipioParto(String municipioParto) {
		this.municipioParto = municipioParto;
	}

	public String getMunicipioViaje() {
		return municipioViaje;
	}

	public void setMunicipioViaje(String municipioViaje) {
		this.municipioViaje = municipioViaje;
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

	public String getNombreInvestigador() {
		return nombreInvestigador;
	}

	public void setNombreInvestigador(String nombreInvestigador) {
		this.nombreInvestigador = nombreInvestigador;
	}

	public String getTelefonoInvestigador() {
		return telefonoInvestigador;
	}

	public void setTelefonoInvestigador(String telefonoInvestigador) {
		this.telefonoInvestigador = telefonoInvestigador;
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
