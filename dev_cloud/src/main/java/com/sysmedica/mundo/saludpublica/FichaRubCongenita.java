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
import com.sysmedica.dao.FichaRubCongenitaDao;

public class FichaRubCongenita {

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
    
    private int codigoFichaRubCongenita;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
	private int clasificacionInicial;
	private String nombreTutor;
	private String lugarNacimientoPaciente;
	private int fuenteNotificacion;
	private String nombreMadre;
	private String edadMadre;
	private int embarazos;
	private int carneVacunacion;
	private int vacunaRubeola;
	private String numeroDosis;
	private String fechaUltimaDosis;
	private int rubeolaConfirmada;
	private String semanasEmbarazo;
	private int similarRubeola;
	private String semanasEmbarazo2;
	private int expuestaRubeola;
	private String semanasEmbarazo3;
	private String donde;
	private int viajes;
	private String semanasEmbarazo4;
	private String dondeViajo;
	private String apgar;
	private int bajoPesoNacimiento;
	private String peso;
	private int pequenoEdadGesta;
	private String semanasEdad;
	private int cataratas;
	private int glaucoma;
	private int retinopatia;
	private int otrosOjo;
	private int arterioso;
	private int estenosis;
	private int otrosCorazon;
	private int sordera;
	private int otrosOido;
	private int microCefalia;
	private int sicomotor;
	private int purpura;
	private int hepatomegalia;
	private int ictericia;
	private int esplenomegalia;
	private int osteopatia;
	private int meningoencefalitis;
	private int otrosGeneral;
	private int examenesEspeciales;
	private String examen;
	private int anatomoPatologico;
	private String examen2;
	private int compatibleSrc;
	private int dxFinal;
	private String nombreInvestigador;
	private String telefonoInvestigador;
	
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
    
    FichaRubCongenitaDao fichaRubCongenitaDao;
    
    
    public FichaRubCongenita()
    {
    	reset();
        init(System.getProperty("TIPOBD"));
    }
    
    public void reset() {
    	
    	clasificacionInicial = 0;
    	nombreTutor = "";
    	lugarNacimientoPaciente = "";
    	fuenteNotificacion = 0;
    	nombreMadre = "";
    	edadMadre = "";
    	embarazos = 0;
    	carneVacunacion = 0;
    	vacunaRubeola = 0;
    	numeroDosis = "";
    	fechaUltimaDosis = "";
    	rubeolaConfirmada = 0;
    	semanasEmbarazo = "";
    	similarRubeola = 0;
    	semanasEmbarazo2 = "";
    	expuestaRubeola = 0;
    	semanasEmbarazo3 = "";
    	donde = "";
    	viajes = 0;
    	semanasEmbarazo4 = "";
    	dondeViajo = "";
    	apgar = "";
    	bajoPesoNacimiento = 0;
    	peso = "";
    	pequenoEdadGesta = 0;
    	semanasEdad = "";
    	cataratas = 0;
    	glaucoma = 0;
    	retinopatia = 0;
    	otrosOjo = 0;
    	arterioso = 0;
    	estenosis = 0;
    	otrosCorazon = 0;
    	sordera = 0;
    	otrosOido = 0;
    	microCefalia = 0;
    	sicomotor = 0;
    	purpura = 0;
    	hepatomegalia = 0;
    	ictericia = 0;
    	esplenomegalia = 0;
    	osteopatia = 0;
    	meningoencefalitis = 0;
    	otrosGeneral = 0;
    	examenesEspeciales = 0;
    	examen = "";
    	anatomoPatologico = 0;
    	examen2 = "";
    	compatibleSrc = 0;
    	dxFinal = 0;
    	nombreInvestigador = "";
    	telefonoInvestigador = "";
    	
    	paciente = new Paciente();
    	
    	datosLaboratorio = new HashMap();
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
			fichaRubCongenitaDao = myFactory.getFichaRubCongenitaDao();
			wasInited = (fichaRubCongenitaDao != null);
		}
		return wasInited;
	}
    
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaRubCongenitaDao.insertarFichaCompleta(con,
												    		numeroSolicitud,
															loginUsuario,
															codigoPaciente,
															codigoDiagnostico,
															estadoFicha,
															codigoConvenio,
															nombreProfesional,
														    
														    sire,
															notificar,
															
															codigoFichaRubCongenita,										    
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
															nombreTutor,
															lugarNacimientoPaciente,
															fuenteNotificacion,
															nombreMadre,
															edadMadre,
															embarazos,
															carneVacunacion,
															vacunaRubeola,
															numeroDosis,
															fechaUltimaDosis,
															rubeolaConfirmada,
															semanasEmbarazo,
															similarRubeola,
															semanasEmbarazo2,
															expuestaRubeola,
															semanasEmbarazo3,
															donde,
															viajes,
															semanasEmbarazo4,
															dondeViajo,
															apgar,
															bajoPesoNacimiento,
															peso,
															pequenoEdadGesta,
															semanasEdad,
															cataratas,
															glaucoma,
															retinopatia,
															otrosOjo,
															arterioso,
															estenosis,
															otrosCorazon,
															sordera,
															otrosOido,
															microCefalia,
															sicomotor,
															purpura,
															hepatomegalia,
															ictericia,
															esplenomegalia,
															osteopatia,
															meningoencefalitis,
															otrosGeneral,
															examenesEspeciales,
															examen,
															anatomoPatologico,
															examen2,
															compatibleSrc,
															dxFinal,
															nombreInvestigador,
															telefonoInvestigador,
														    
														    activa,
														    pais,
														    areaProcedencia);
    }
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaRubCongenitaDao.modificarFicha(con,
													sire,
													notificar,
												    loginUsuario,
												    codigoFichaRubCongenita,
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
													nombreTutor,
													lugarNacimientoPaciente,
													fuenteNotificacion,
													nombreMadre,
													edadMadre,
													embarazos,
													carneVacunacion,
													vacunaRubeola,
													numeroDosis,
													fechaUltimaDosis,
													rubeolaConfirmada,
													semanasEmbarazo,
													similarRubeola,
													semanasEmbarazo2,
													expuestaRubeola,
													semanasEmbarazo3,
													donde,
													viajes,
													semanasEmbarazo4,
													dondeViajo,
													apgar,
													bajoPesoNacimiento,
													peso,
													pequenoEdadGesta,
													semanasEdad,
													cataratas,
													glaucoma,
													retinopatia,
													otrosOjo,
													arterioso,
													estenosis,
													otrosCorazon,
													sordera,
													otrosOido,
													microCefalia,
													sicomotor,
													purpura,
													hepatomegalia,
													ictericia,
													esplenomegalia,
													osteopatia,
													meningoencefalitis,
													otrosGeneral,
													examenesEspeciales,
													examen,
													anatomoPatologico,
													examen2,
													compatibleSrc,
													dxFinal,
													nombreInvestigador,
													telefonoInvestigador,
												    pais,
												    areaProcedencia);
    }
    
    
    
    
    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaRubCongenitaDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    	
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
    	this.codigoFichaRubCongenita = codigo;
    	
    	ResultSet rs = fichaRubCongenitaDao.consultaTodoFicha(con,codigo);
    	
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
			    this.setNombreTutor(rs.getString("nombreTutor"));
			    this.setLugarNacimientoPaciente(rs.getString("lugarNacimientoPaciente"));
			    this.setFuenteNotificacion(rs.getInt("fuenteNotificacion"));
			    this.setNombreMadre(rs.getString("nombreMadre"));
			    this.setEdadMadre(rs.getString("edadMadre"));
			    this.setEmbarazos(rs.getInt("embarazos"));
			    this.setCarneVacunacion(rs.getInt("carneVacunacion"));
			    this.setVacunaRubeola(rs.getInt("vacunaRubeola"));
			    this.setNumeroDosis(rs.getString("numeroDosis"));
			    this.setFechaUltimaDosis(rs.getString("fechaUltimaDosis"));
			    this.setRubeolaConfirmada(rs.getInt("rubeolaConfirmada"));
			    this.setSemanasEmbarazo(rs.getString("semanasEmbarazo"));
			    this.setSimilarRubeola(rs.getInt("similarRubeola"));
			    this.setSemanasEmbarazo2(rs.getString("semanasEmbarazo2"));
			    this.setExpuestaRubeola(rs.getInt("expuestaRubeola"));
			    this.setSemanasEmbarazo3(rs.getString("semanasEmbarazo3"));
			    this.setDonde(rs.getString("donde"));
			    this.setViajes(rs.getInt("viajes"));
			    this.setSemanasEmbarazo4(rs.getString("semanasEmbarazo4"));
			    this.setDondeViajo(rs.getString("dondeViajo"));
			    this.setApgar(rs.getString("apgar"));
			    this.setBajoPesoNacimiento(rs.getInt("bajoPesoNacimiento"));
			    this.setPeso(rs.getString("peso"));
			    this.setPequenoEdadGesta(rs.getInt("pequenoEdadGesta"));
			    this.setSemanasEdad(rs.getString("semanasEdad"));
			    this.setCataratas(rs.getInt("cataratas"));
			    this.setGlaucoma(rs.getInt("glaucoma"));
			    this.setRetinopatia(rs.getInt("retinopatia"));
			    this.setOtrosOjo(rs.getInt("otrosOjo"));
			    this.setArterioso(rs.getInt("arterioso"));
			    this.setEstenosis(rs.getInt("estenosis"));
			    this.setOtrosCorazon(rs.getInt("otrosCorazon"));
			    this.setSordera(rs.getInt("sordera"));
			    this.setOtrosOido(rs.getInt("otrosOido"));
			    this.setMicroCefalia(rs.getInt("microCefalia"));
			    this.setSicomotor(rs.getInt("sicomotor"));
			    this.setPurpura(rs.getInt("purpura"));
			    this.setHepatomegalia(rs.getInt("hepatomegalia"));
			    this.setIctericia(rs.getInt("ictericia"));
			    this.setEsplenomegalia(rs.getInt("esplenomegalia"));
			    this.setOsteopatia(rs.getInt("osteopatia"));
			    this.setMeningoencefalitis(rs.getInt("meningoencefalitis"));
			    this.setOtrosGeneral(rs.getInt("otrosGeneral"));
			    this.setExamenesEspeciales(rs.getInt("examenesEspeciales"));
			    this.setExamen(rs.getString("examen"));
			    this.setAnatomoPatologico(rs.getInt("anatomoPatologico"));
			    this.setExamen2(rs.getString("examen2"));
			    this.setCompatibleSrc(rs.getInt("compatibleSrc"));
			    this.setDxFinal(rs.getInt("dxFinal"));
			    this.setNombreInvestigador(rs.getString("nombreInvestigador"));
			    this.setTelefonoInvestigador(rs.getString("telefonoInvestigador"));
			    
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
			    
			    ResultSet rs2 = fichaRubCongenitaDao.consultarDatosLaboratorio(con,codigo);
			    
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

	public int getAnatomoPatologico() {
		return anatomoPatologico;
	}

	public void setAnatomoPatologico(int anatomoPatologico) {
		this.anatomoPatologico = anatomoPatologico;
	}

	public String getApgar() {
		return apgar;
	}

	public void setApgar(String apgar) {
		this.apgar = apgar;
	}

	public int getAreaProcedencia() {
		return areaProcedencia;
	}

	public void setAreaProcedencia(int areaProcedencia) {
		this.areaProcedencia = areaProcedencia;
	}

	public int getArterioso() {
		return arterioso;
	}

	public void setArterioso(int arterioso) {
		this.arterioso = arterioso;
	}

	public int getBajoPesoNacimiento() {
		return bajoPesoNacimiento;
	}

	public void setBajoPesoNacimiento(int bajoPesoNacimiento) {
		this.bajoPesoNacimiento = bajoPesoNacimiento;
	}

	public int getCarneVacunacion() {
		return carneVacunacion;
	}

	public void setCarneVacunacion(int carneVacunacion) {
		this.carneVacunacion = carneVacunacion;
	}

	public int getCataratas() {
		return cataratas;
	}

	public void setCataratas(int cataratas) {
		this.cataratas = cataratas;
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

	public int getCodigoFichaRubCongenita() {
		return codigoFichaRubCongenita;
	}

	public void setCodigoFichaRubCongenita(int codigoFichaRubCongenita) {
		this.codigoFichaRubCongenita = codigoFichaRubCongenita;
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

	public int getCompatibleSrc() {
		return compatibleSrc;
	}

	public void setCompatibleSrc(int compatibleSrc) {
		this.compatibleSrc = compatibleSrc;
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

	public String getDonde() {
		return donde;
	}

	public void setDonde(String donde) {
		this.donde = donde;
	}

	public String getDondeViajo() {
		return dondeViajo;
	}

	public void setDondeViajo(String dondeViajo) {
		this.dondeViajo = dondeViajo;
	}

	public int getDxFinal() {
		return dxFinal;
	}

	public void setDxFinal(int dxFinal) {
		this.dxFinal = dxFinal;
	}

	public String getEdad() {
		return edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}

	public String getEdadMadre() {
		return edadMadre;
	}

	public void setEdadMadre(String edadMadre) {
		this.edadMadre = edadMadre;
	}

	public int getEmbarazos() {
		return embarazos;
	}

	public void setEmbarazos(int embarazos) {
		this.embarazos = embarazos;
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

	public int getEstenosis() {
		return estenosis;
	}

	public void setEstenosis(int estenosis) {
		this.estenosis = estenosis;
	}

	public String getExamen() {
		return examen;
	}

	public void setExamen(String examen) {
		this.examen = examen;
	}

	public String getExamen2() {
		return examen2;
	}

	public void setExamen2(String examen2) {
		this.examen2 = examen2;
	}

	public int getExamenesEspeciales() {
		return examenesEspeciales;
	}

	public void setExamenesEspeciales(int examenesEspeciales) {
		this.examenesEspeciales = examenesEspeciales;
	}

	public int getExpuestaRubeola() {
		return expuestaRubeola;
	}

	public void setExpuestaRubeola(int expuestaRubeola) {
		this.expuestaRubeola = expuestaRubeola;
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

	public int getGlaucoma() {
		return glaucoma;
	}

	public void setGlaucoma(int glaucoma) {
		this.glaucoma = glaucoma;
	}

	public int getHepatomegalia() {
		return hepatomegalia;
	}

	public void setHepatomegalia(int hepatomegalia) {
		this.hepatomegalia = hepatomegalia;
	}

	public boolean isHospitalizado() {
		return hospitalizado;
	}

	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}

	public int getIctericia() {
		return ictericia;
	}

	public void setIctericia(int ictericia) {
		this.ictericia = ictericia;
	}

	public int getInstitucionAtendio() {
		return institucionAtendio;
	}

	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
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

	public String getLugarNacimientoPaciente() {
		return lugarNacimientoPaciente;
	}

	public void setLugarNacimientoPaciente(String lugarNacimientoPaciente) {
		this.lugarNacimientoPaciente = lugarNacimientoPaciente;
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

	public int getMeningoencefalitis() {
		return meningoencefalitis;
	}

	public void setMeningoencefalitis(int meningoencefalitis) {
		this.meningoencefalitis = meningoencefalitis;
	}

	public int getMicroCefalia() {
		return microCefalia;
	}

	public void setMicroCefalia(int microCefalia) {
		this.microCefalia = microCefalia;
	}

	public String getMunicipioNotifica() {
		return municipioNotifica;
	}

	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}

	public String getNombreInvestigador() {
		return nombreInvestigador;
	}

	public void setNombreInvestigador(String nombreInvestigador) {
		this.nombreInvestigador = nombreInvestigador;
	}

	public String getNombreMadre() {
		return nombreMadre;
	}

	public void setNombreMadre(String nombreMadre) {
		this.nombreMadre = nombreMadre;
	}

	public String getNombreProfesional() {
		return nombreProfesional;
	}

	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}

	public String getNombreTutor() {
		return nombreTutor;
	}

	public void setNombreTutor(String nombreTutor) {
		this.nombreTutor = nombreTutor;
	}

	public boolean isNotificar() {
		return notificar;
	}

	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}

	public String getNumeroDosis() {
		return numeroDosis;
	}

	public void setNumeroDosis(String numeroDosis) {
		this.numeroDosis = numeroDosis;
	}

	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public int getOsteopatia() {
		return osteopatia;
	}

	public void setOsteopatia(int osteopatia) {
		this.osteopatia = osteopatia;
	}

	public int getOtrosCorazon() {
		return otrosCorazon;
	}

	public void setOtrosCorazon(int otrosCorazon) {
		this.otrosCorazon = otrosCorazon;
	}

	public int getOtrosGeneral() {
		return otrosGeneral;
	}

	public void setOtrosGeneral(int otrosGeneral) {
		this.otrosGeneral = otrosGeneral;
	}

	public int getOtrosOido() {
		return otrosOido;
	}

	public void setOtrosOido(int otrosOido) {
		this.otrosOido = otrosOido;
	}

	public int getOtrosOjo() {
		return otrosOjo;
	}

	public void setOtrosOjo(int otrosOjo) {
		this.otrosOjo = otrosOjo;
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

	public int getPequenoEdadGesta() {
		return pequenoEdadGesta;
	}

	public void setPequenoEdadGesta(int pequenoEdadGesta) {
		this.pequenoEdadGesta = pequenoEdadGesta;
	}

	public String getPeso() {
		return peso;
	}

	public void setPeso(String peso) {
		this.peso = peso;
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

	public int getPurpura() {
		return purpura;
	}

	public void setPurpura(int purpura) {
		this.purpura = purpura;
	}

	public int getRetinopatia() {
		return retinopatia;
	}

	public void setRetinopatia(int retinopatia) {
		this.retinopatia = retinopatia;
	}

	public int getRubeolaConfirmada() {
		return rubeolaConfirmada;
	}

	public void setRubeolaConfirmada(int rubeolaConfirmada) {
		this.rubeolaConfirmada = rubeolaConfirmada;
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

	public String getSemanasEdad() {
		return semanasEdad;
	}

	public void setSemanasEdad(String semanasEdad) {
		this.semanasEdad = semanasEdad;
	}

	public String getSemanasEmbarazo() {
		return semanasEmbarazo;
	}

	public void setSemanasEmbarazo(String semanasEmbarazo) {
		this.semanasEmbarazo = semanasEmbarazo;
	}

	public String getSemanasEmbarazo2() {
		return semanasEmbarazo2;
	}

	public void setSemanasEmbarazo2(String semanasEmbarazo2) {
		this.semanasEmbarazo2 = semanasEmbarazo2;
	}

	public String getSemanasEmbarazo3() {
		return semanasEmbarazo3;
	}

	public void setSemanasEmbarazo3(String semanasEmbarazo3) {
		this.semanasEmbarazo3 = semanasEmbarazo3;
	}

	public String getSemanasEmbarazo4() {
		return semanasEmbarazo4;
	}

	public void setSemanasEmbarazo4(String semanasEmbarazo4) {
		this.semanasEmbarazo4 = semanasEmbarazo4;
	}

	public int getSicomotor() {
		return sicomotor;
	}

	public void setSicomotor(int sicomotor) {
		this.sicomotor = sicomotor;
	}

	public int getSimilarRubeola() {
		return similarRubeola;
	}

	public void setSimilarRubeola(int similarRubeola) {
		this.similarRubeola = similarRubeola;
	}

	public String getSire() {
		return sire;
	}

	public void setSire(String sire) {
		this.sire = sire;
	}

	public int getSordera() {
		return sordera;
	}

	public void setSordera(int sordera) {
		this.sordera = sordera;
	}

	public String getTelefonoInvestigador() {
		return telefonoInvestigador;
	}

	public void setTelefonoInvestigador(String telefonoInvestigador) {
		this.telefonoInvestigador = telefonoInvestigador;
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

	public int getVacunaRubeola() {
		return vacunaRubeola;
	}

	public void setVacunaRubeola(int vacunaRubeola) {
		this.vacunaRubeola = vacunaRubeola;
	}

	public int getViajes() {
		return viajes;
	}

	public void setViajes(int viajes) {
		this.viajes = viajes;
	}

	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}

	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
	}
}
