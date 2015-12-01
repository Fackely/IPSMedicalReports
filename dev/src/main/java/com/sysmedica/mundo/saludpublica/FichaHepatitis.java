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
import com.sysmedica.dao.FichaHepatitisDao;


/**
 * @author santiago
 *
 */
public class FichaHepatitis {


	private Logger logger = Logger.getLogger(FichaHepatitis.class);
	
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
    
    private int codigoFichaHepatitis;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
	
    private int embarazada;
    private String edadGestacional;
    private int controlPrenatal;
    private int donanteSangre;
    private int poblacionRiesgo;
    private int modoTransmision;
    private int otrasIts;
    private int vacunaAntihepatitis;
    private String numeroDosis;
    private String fechaPrimeraDosis;
    private String fechaUltimaDosis;
    private int fuenteInformacion;
    private int tratamiento;
    private String cualTratamiento;
    private int complicacion;
    private String cualComplicacion;
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
    
    private HashMap sintomas;
    private HashMap poblaRiesgo;
    private HashMap datosLaboratorio;
    
    FichaHepatitisDao fichaHepatitisDao;
    
    public void reset()
    {
    	embarazada = 0;
        edadGestacional = "";
        controlPrenatal = 0;
        donanteSangre = 0;
        poblacionRiesgo = 0;
        modoTransmision = 0;
        otrasIts = 0;
        vacunaAntihepatitis = 0;
        numeroDosis = "";
        fechaPrimeraDosis = "";
        fechaUltimaDosis = "";
        fuenteInformacion = 0;
        tratamiento = 0;
        cualTratamiento = "";
        complicacion = 0;
        cualComplicacion = "";
        observaciones = "";
        
        paciente = new Paciente();
        sintomas = new HashMap();
        poblaRiesgo = new HashMap();
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
			fichaHepatitisDao = myFactory.getFichaHepatitisDao();
			wasInited = (fichaHepatitisDao != null);
		}
		return wasInited;
	}
    
    
    public FichaHepatitis()
    {
    	reset();
        init(System.getProperty("TIPOBD"));
    }

    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaHepatitisDao.insertarFichaCompleta(con,
											    		numeroSolicitud,
														loginUsuario,
														codigoPaciente,
														codigoDiagnostico,
														estadoFicha,
														codigoConvenio,
														nombreProfesional,
													    
													    sire,
														notificar,
														
														codigoFichaHepatitis,										    
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
													    
													    embarazada,
													    edadGestacional,
													    controlPrenatal,
													    donanteSangre,
													    poblacionRiesgo,
													    modoTransmision,
													    otrasIts,
													    vacunaAntihepatitis,
													    numeroDosis,
													    fechaPrimeraDosis,
													    fechaUltimaDosis,
													    fuenteInformacion,
													    tratamiento,
													    cualTratamiento,
													    complicacion,
													    cualComplicacion,
													    observaciones,
													    
													    activa,
													    pais,
													    areaProcedencia,
													    
													    sintomas,
													    poblaRiesgo);
    }
    
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaHepatitisDao.modificarFicha(con,
												sire,
												notificar,
											    loginUsuario,
											    codigoFichaHepatitis,
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
								
											    embarazada,
											    edadGestacional,
											    controlPrenatal,
											    donanteSangre,
											    poblacionRiesgo,
											    modoTransmision,
											    otrasIts,
											    vacunaAntihepatitis,
											    numeroDosis,
											    fechaPrimeraDosis,
											    fechaUltimaDosis,
											    fuenteInformacion,
											    tratamiento,
											    cualTratamiento,
											    complicacion,
											    cualComplicacion,
											    observaciones,
												pais,
											    areaProcedencia,
											    sintomas,
											    poblaRiesgo);
    }

    
    
    

    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaHepatitisDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    	
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
    	this.codigoFichaHepatitis = codigo;
    	
    	ResultSet rs = fichaHepatitisDao.consultaTodoFicha(con,codigo);
    	
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
    
			    this.setEmbarazada(rs.getInt("embarazada"));
			    this.setEdadGestacional(rs.getString("edadGestacional"));
			    this.setControlPrenatal(rs.getInt("controlPrenatal"));
			    this.setDonanteSangre(rs.getInt("donanteSangre"));
			    this.setPoblacionRiesgo(rs.getInt("poblacionRiesgo"));
			    this.setModoTransmision(rs.getInt("modoTransmision"));
			    this.setOtrasIts(rs.getInt("otrasIts"));
			    this.setVacunaAntihepatitis(rs.getInt("vacunaAntihepatitis"));
			    this.setNumeroDosis(rs.getString("numeroDosis"));
			    this.setFechaPrimeraDosis(rs.getString("fechaPrimeraDosis"));
			    this.setFechaUltimaDosis(rs.getString("fechaUltimaDosis"));
			    this.setFuenteInformacion(rs.getInt("fuenteInformacion"));
			    this.setTratamiento(rs.getInt("tratamiento"));
			    this.setCualTratamiento(rs.getString("cualTratamiento"));
			    this.setComplicacion(rs.getInt("complicacion"));
			    this.setCualComplicacion(rs.getString("cualComplicacion"));
			    this.setObservaciones(rs.getString("observaciones"));
			    			    
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
			    
			    ResultSet rs2 = fichaHepatitisDao.consultarSintomas(con,codigo);
			    
			    HashMap sint = new HashMap();
			    
			    while (rs2.next()) {
			    	
			    	sint.put("sintoma_"+rs2.getInt("codigo"),"true");
			    }
			    
			    this.setSintomas(sint);
			    
			    
			    
			    ResultSet rs3 = fichaHepatitisDao.consultarPoblacion(con,codigo);
			    
			    HashMap pobla = new HashMap();
			    
			    while (rs3.next()) {
			    	
			    	pobla.put("poblacion_"+rs3.getInt("codigo"),"true");
			    }
			    
			    this.setPoblaRiesgo(pobla);
			    
			    
			    
			    ResultSet rs4 = fichaHepatitisDao.consultarDatosLaboratorio(con,codigo);
			    
			    HashMap datosLab = new HashMap();
			    int i=1;
			    
			    while (rs4.next()) {
			    	
			    	datosLab.put("fechaToma"+i,rs4.getString("fechatoma"));
			    	datosLab.put("fechaRecepcion"+i,rs4.getString("fechaRecepcion"));
			    	datosLab.put("muestra"+i,Integer.toString(rs4.getInt("muestra")));
			    	datosLab.put("prueba"+i,Integer.toString(rs4.getInt("prueba")));
			    	datosLab.put("agente"+i,Integer.toString(rs4.getInt("agente")));
			    	datosLab.put("resultado"+i,Integer.toString(rs4.getInt("resultado")));
			    	datosLab.put("fechaResultado"+i,rs4.getString("fecharesultado"));
			    	datosLab.put("valor"+i,rs4.getString("valor"));
			    	datosLab.put("codigo"+i,rs4.getInt("codigofichalaboratorios"));
			    	
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



	public int getCodigoFichaHepatitis() {
		return codigoFichaHepatitis;
	}



	public void setCodigoFichaHepatitis(int codigoFichaHepatitis) {
		this.codigoFichaHepatitis = codigoFichaHepatitis;
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



	public int getComplicacion() {
		return complicacion;
	}



	public void setComplicacion(int complicacion) {
		this.complicacion = complicacion;
	}



	public int getControlPrenatal() {
		return controlPrenatal;
	}



	public void setControlPrenatal(int controlPrenatal) {
		this.controlPrenatal = controlPrenatal;
	}



	public String getCualComplicacion() {
		return cualComplicacion;
	}



	public void setCualComplicacion(String cualComplicacion) {
		this.cualComplicacion = cualComplicacion;
	}



	public String getCualTratamiento() {
		return cualTratamiento;
	}



	public void setCualTratamiento(String cualTratamiento) {
		this.cualTratamiento = cualTratamiento;
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



	public int getDonanteSangre() {
		return donanteSangre;
	}



	public void setDonanteSangre(int donanteSangre) {
		this.donanteSangre = donanteSangre;
	}



	public String getEdad() {
		return edad;
	}



	public void setEdad(String edad) {
		this.edad = edad;
	}



	public String getEdadGestacional() {
		return edadGestacional;
	}



	public void setEdadGestacional(String edadGestacional) {
		this.edadGestacional = edadGestacional;
	}



	public int getEmbarazada() {
		return embarazada;
	}



	public void setEmbarazada(int embarazada) {
		this.embarazada = embarazada;
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



	public String getFechaPrimeraDosis() {
		return fechaPrimeraDosis;
	}



	public void setFechaPrimeraDosis(String fechaPrimeraDosis) {
		this.fechaPrimeraDosis = fechaPrimeraDosis;
	}



	public String getFechaUltimaDosis() {
		return fechaUltimaDosis;
	}



	public void setFechaUltimaDosis(String fechaUltimaDosis) {
		this.fechaUltimaDosis = fechaUltimaDosis;
	}



	public int getFuenteInformacion() {
		return fuenteInformacion;
	}



	public void setFuenteInformacion(int fuenteInformacion) {
		this.fuenteInformacion = fuenteInformacion;
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



	public int getModoTransmision() {
		return modoTransmision;
	}



	public void setModoTransmision(int modoTransmision) {
		this.modoTransmision = modoTransmision;
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



	public String getObservaciones() {
		return observaciones;
	}



	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}



	public int getOtrasIts() {
		return otrasIts;
	}



	public void setOtrasIts(int otrasIts) {
		this.otrasIts = otrasIts;
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



	public int getPoblacionRiesgo() {
		return poblacionRiesgo;
	}



	public void setPoblacionRiesgo(int poblacionRiesgo) {
		this.poblacionRiesgo = poblacionRiesgo;
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



	public int getTipoCaso() {
		return tipoCaso;
	}



	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}



	public int getTratamiento() {
		return tratamiento;
	}



	public void setTratamiento(int tratamiento) {
		this.tratamiento = tratamiento;
	}



	public int getVacunaAntihepatitis() {
		return vacunaAntihepatitis;
	}



	public void setVacunaAntihepatitis(int vacunaAntihepatitis) {
		this.vacunaAntihepatitis = vacunaAntihepatitis;
	}



	public HashMap getSintomas() {
		return sintomas;
	}



	public void setSintomas(HashMap sintomas) {
		this.sintomas = sintomas;
	}



	public HashMap getPoblaRiesgo() {
		return poblaRiesgo;
	}



	public void setPoblaRiesgo(HashMap poblaRiesgo) {
		this.poblaRiesgo = poblaRiesgo;
	}



	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}



	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
	}
    
}
