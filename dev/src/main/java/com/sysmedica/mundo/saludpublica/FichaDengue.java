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
import com.sysmedica.dao.FichaDengueDao;
import com.sysmedica.dao.sqlbase.SqlBaseFichaDengueDao;

public class FichaDengue {

	private Logger logger = Logger.getLogger(FichaDengue.class);
	
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
    
    private int codigoFichaDengue;
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
    
	private int vacunaFiebreAmarilla;
	private String fechaAplicacionVacunaFiebre;
	private int vacunaHepatitisBDosis1;
	private int vacunaHepatitisBDosis2;
	private int vacunaHepatitisBDosis3;
	private String fechaVacunaHepaDosis1;
	private String fechaVacunaHepaDosis2;
	private String fechaVacunaHepaDosis3;
	private int vacunaHepatitisADosis1;
	private String fechaVacunaHepatADosis1;
	private String observaciones;
	private boolean desplazamiento;
	private String fechaDesplazamiento;
	private String lugarDesplazamiento;
	private String codigoMunicipio;
	private String codigoDepartamento;
	private int casoFiebreAmarilla;
	private int casoEpizootia;
	private String direccionSitio;
	private int presenciaAedes;
	private HashMap hallazgosSemiologicos;
	private HashMap datosLaboratorio;
	private String pais;
	private int areaProcedencia;
	
	
	
	private Paciente paciente;
	
	private FichaDengueDao fichaDengueDao;
	
	public static int codigoGlobal;
	
	private int estadoAnterior;
	
	private boolean activa;
	
	public FichaDengue() {
		
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
				fichaDengueDao = myFactory.getFichaDengueDao();
				wasInited = (fichaDengueDao != null);
			}
			return wasInited;
	}
    
    
    public int insertarFicha(Connection con)  {
    	
    	return fichaDengueDao.insertarFicha(con,
											notificar,
										    loginUsuario,
										    codigoFichaDengue,
										    codigoPaciente,
										    codigoDiagnostico,
										    codigoNotificacion,
										    numeroSolicitud,
										    codigoConvenio,
											nombreProfesional,
										    estadoFicha);
    }
    
    
    public void cargarDatos(Connection con, int codigo) {
    
    	this.codigoFichaDengue = codigo;
    	
    	ResultSet rs = fichaDengueDao.consultaTodoFicha(con,codigo);
    	
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
			    this.setVacunaFiebreAmarilla(rs.getInt("vacunaFiebreAmarilla"));
			    this.setFechaAplicacionVacunaFiebre(rs.getString("fechaApliVacunaFiebre"));
			    this.setVacunaHepatitisADosis1(rs.getInt("vacunaHepatitisADosis1"));
			 
			    this.setVacunaHepatitisBDosis1(rs.getInt("vacunaHepatitisBDosis1"));
			    this.setVacunaHepatitisBDosis2(rs.getInt("vacunaHepatitisBDosis2"));
			    this.setVacunaHepatitisBDosis3(rs.getInt("vacunaHepatitisBDosis3"));
			    this.setFechaVacunaHepaDosis1(rs.getString("fechaVacunaHepaDosis1"));
			    this.setFechaVacunaHepaDosis2(rs.getString("fechaVacunaHepaDosis2"));
			    this.setFechaVacunaHepaDosis3(rs.getString("fechaVacunaHepaDosis3"));
			    this.setFechaVacunaHepatADosis1(rs.getString("fechaVacunaHepatADosis1"));
			    
			    this.setObservaciones(rs.getString("observaciones"));
			    this.setDesplazamiento(rs.getBoolean("desplazamiento"));
			    this.setFechaDesplazamiento(rs.getString("fechaDesplazamiento"));
			    
			//    this.setLugarDesplazamiento(rs.getInt("codigoMunicipio"));
			    this.setCodigoMunicipio(rs.getString("codigoMunicipio"));
			    this.setCodigoDepartamento(rs.getString("codigoDepartamento"));
			    this.setCasoFiebreAmarilla(rs.getInt("casoFiebreAmarilla"));
			    
			    this.setCasoEpizootia(rs.getInt("casoEpizootia"));
			    this.setDireccionSitio(rs.getString("direccionSitio"));
			    this.setPresenciaAedes(rs.getInt("presenciaAedes"));
			    
			    this.setFechaNotificacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaDiligenciamiento")));
			    
			    ResultSet rs2 = fichaDengueDao.consultarHallazgos(con,codigo);
			    
			    HashMap hallazgos = new HashMap();
			    
			    while (rs2.next()) {
			    	
			    	hallazgos.put("hallazgo_"+rs2.getInt("codigohallazgo"),"true");
			    }
			    
			    this.setHallazgosSemiologicos(hallazgos);
			    
			    
			    ResultSet rs3 = fichaDengueDao.consultarDatosLaboratorio(con,codigo);
			    
			    HashMap datosLab = new HashMap();
			    int i=1;
			    
			    while (rs3.next()) {
			    	
			    	datosLab.put("fechaToma"+i,rs3.getString("fechatoma"));
			    	datosLab.put("fechaRecepcion"+i,rs3.getString("fechaRecepcion"));
			    	datosLab.put("muestra"+i,Integer.toString(rs3.getInt("muestra")));
			    	datosLab.put("prueba"+i,Integer.toString(rs3.getInt("prueba")));
			    	datosLab.put("agente"+i,Integer.toString(rs3.getInt("agente")));
			    	datosLab.put("resultado"+i,Integer.toString(rs3.getInt("resultado")));
			    	datosLab.put("fechaResultado"+i,rs3.getString("fecharesultado"));
			    	datosLab.put("valor"+i,rs3.getString("valor"));
			    	datosLab.put("codigo"+i,rs3.getInt("codigofichalaboratorios"));
			    	
			    	i++;
			    }
			    
			    this.setDatosLaboratorio(datosLab);
			    
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
    		}
    	}
    	catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
    }
    
    
    public int modificarFicha(Connection con) {
    	
    	return fichaDengueDao.modificarFicha(con,
												sire,
												notificar,
											    loginUsuario,
											    codigoFichaDengue,
											    estadoFicha,
											    vacunaFiebreAmarilla,
												fechaAplicacionVacunaFiebre,
												vacunaHepatitisBDosis1,
												vacunaHepatitisBDosis2,
												vacunaHepatitisBDosis3,
												fechaVacunaHepaDosis1,
												fechaVacunaHepaDosis2,
												fechaVacunaHepaDosis3,
												vacunaHepatitisADosis1,
												fechaVacunaHepatADosis1,
												observaciones,
												desplazamiento,
												fechaDesplazamiento,
												lugarDesplazamiento,
												codigoMunicipio,
												codigoDepartamento,
												casoFiebreAmarilla,
												casoEpizootia,
												direccionSitio,
												presenciaAedes,
												hallazgosSemiologicos,
												datosLaboratorio,
												
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
											    estadoAnterior,
											    pais,
											    areaProcedencia
												);
    }
    
    
    
    
    public void cargarPaciente(Connection con, int codigo, boolean empezarnuevo) {
    	
    	ResultSet rs = fichaDengueDao.consultaDatosPaciente(con,codigo,empezarnuevo);
    	
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
    	return fichaDengueDao.insertarFichaCompleta(con,
													numeroSolicitud,
													loginUsuario,
													codigoPaciente,
													codigoDiagnostico,
													estadoFicha,
													codigoConvenio,
													nombreProfesional,
												    
												    sire,
												    
												    vacunaFiebreAmarilla,
													fechaAplicacionVacunaFiebre,
													vacunaHepatitisBDosis1,
													vacunaHepatitisBDosis2,
													vacunaHepatitisBDosis3,
													fechaVacunaHepaDosis1,
													fechaVacunaHepaDosis2,
													fechaVacunaHepaDosis3,
													vacunaHepatitisADosis1,
													fechaVacunaHepatADosis1,
													observaciones,
													desplazamiento,
													fechaDesplazamiento,
													lugarDesplazamiento,
													casoFiebreAmarilla,
													casoEpizootia,
													direccionSitio,
													presenciaAedes,
													
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
												    estadoAnterior,
												    activa,
												    hallazgosSemiologicos,
												    pais,
												    areaProcedencia);
    }
    
    
    
    
    public int terminarFicha(Connection con)
	{
		return fichaDengueDao.terminarFicha(con,codigoFichaDengue); 
	}
    
    
    
    
    public void reset() {
    	
    	sire="";
    	hallazgosSemiologicos = new HashMap();
    	datosLaboratorio = new HashMap();
    	paciente = new Paciente();
    	vacunaFiebreAmarilla = 0;
    	fechaAplicacionVacunaFiebre = "";
    	vacunaHepatitisBDosis1 = 0;
    	vacunaHepatitisBDosis2 = 0;
    	vacunaHepatitisBDosis3 = 0;
    	fechaVacunaHepaDosis1 = "";
    	fechaVacunaHepaDosis2 = "";
    	fechaVacunaHepaDosis3 = "";
    	vacunaHepatitisADosis1 = 0;
    	fechaVacunaHepatADosis1 = "";
    	observaciones = "";
    	desplazamiento = false;
    	fechaDesplazamiento = "";
    	lugarDesplazamiento = "";
    	casoFiebreAmarilla = 0;
    	casoEpizootia = 0;
    	direccionSitio = "";
    	presenciaAedes = 0;
    	
    	fechaConsultaGeneral = "";
        codigoMunProcedencia = "";
        codigoDepProcedencia = "";
        lugarProcedencia = "";
        fechaInicioSint = "";
        tipoCaso = 0;
        hospitalizado = false;
        fechaHospitalizacion = "";
        estaVivo = false;
        fechaDefuncion = "";
        nombreProfesional = "";
    }


	public int getCasoEpizootia() {
		return casoEpizootia;
	}


	public void setCasoEpizootia(int casoEpizootia) {
		this.casoEpizootia = casoEpizootia;
	}


	public int getCasoFiebreAmarilla() {
		return casoFiebreAmarilla;
	}


	public void setCasoFiebreAmarilla(int casoFiebreAmarilla) {
		this.casoFiebreAmarilla = casoFiebreAmarilla;
	}


	public String getCodigoDepartamento() {
		return codigoDepartamento;
	}


	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}


	public String getCodigoMunicipio() {
		return codigoMunicipio;
	}


	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}


	public boolean isDesplazamiento() {
		return desplazamiento;
	}


	public void setDesplazamiento(boolean desplazamiento) {
		this.desplazamiento = desplazamiento;
	}


	public String getDireccionSitio() {
		return direccionSitio;
	}


	public void setDireccionSitio(String direccionSitio) {
		this.direccionSitio = direccionSitio;
	}


	public String getFechaAplicacionVacunaFiebre() {
		return fechaAplicacionVacunaFiebre;
	}


	public void setFechaAplicacionVacunaFiebre(String fechaAplicacionVacunaFiebre) {
		this.fechaAplicacionVacunaFiebre = fechaAplicacionVacunaFiebre;
	}


	public String getFechaDesplazamiento() {
		return fechaDesplazamiento;
	}


	public void setFechaDesplazamiento(String fechaDesplazamiento) {
		this.fechaDesplazamiento = fechaDesplazamiento;
	}


	public String getFechaVacunaHepaDosis1() {
		return fechaVacunaHepaDosis1;
	}


	public void setFechaVacunaHepaDosis1(String fechaVacunaHepaDosis1) {
		this.fechaVacunaHepaDosis1 = fechaVacunaHepaDosis1;
	}


	public String getFechaVacunaHepaDosis2() {
		return fechaVacunaHepaDosis2;
	}


	public void setFechaVacunaHepaDosis2(String fechaVacunaHepaDosis2) {
		this.fechaVacunaHepaDosis2 = fechaVacunaHepaDosis2;
	}


	public String getFechaVacunaHepaDosis3() {
		return fechaVacunaHepaDosis3;
	}


	public void setFechaVacunaHepaDosis3(String fechaVacunaHepaDosis3) {
		this.fechaVacunaHepaDosis3 = fechaVacunaHepaDosis3;
	}


	public String getFechaVacunaHepatADosis1() {
		return fechaVacunaHepatADosis1;
	}


	public void setFechaVacunaHepatADosis1(String fechaVacunaHepatADosis1) {
		this.fechaVacunaHepatADosis1 = fechaVacunaHepatADosis1;
	}


	public FichaDengueDao getFichaDengueDao() {
		return fichaDengueDao;
	}


	public void setFichaDengueDao(FichaDengueDao fichaDengueDao) {
		this.fichaDengueDao = fichaDengueDao;
	}


	public HashMap getHallazgosSemiologicos() {
		return hallazgosSemiologicos;
	}


	public void setHallazgosSemiologicos(HashMap hallazgosSemiologicos) {
		this.hallazgosSemiologicos = hallazgosSemiologicos;
	}


	public Logger getLogger() {
		return logger;
	}


	public void setLogger(Logger logger) {
		this.logger = logger;
	}


	public String getLugarDesplazamiento() {
		return lugarDesplazamiento;
	}


	public void setLugarDesplazamiento(String lugarDesplazamiento) {
		this.lugarDesplazamiento = lugarDesplazamiento;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public Paciente getPaciente() {
		return paciente;
	}


	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}


	public int getPresenciaAedes() {
		return presenciaAedes;
	}


	public void setPresenciaAedes(int presenciaAedes) {
		this.presenciaAedes = presenciaAedes;
	}


	public int getVacunaFiebreAmarilla() {
		return vacunaFiebreAmarilla;
	}


	public void setVacunaFiebreAmarilla(int vacunaFiebreAmarilla) {
		this.vacunaFiebreAmarilla = vacunaFiebreAmarilla;
	}


	public int getVacunaHepatitisADosis1() {
		return vacunaHepatitisADosis1;
	}


	public void setVacunaHepatitisADosis1(int vacunaHepatitisADosis1) {
		this.vacunaHepatitisADosis1 = vacunaHepatitisADosis1;
	}


	public int getVacunaHepatitisBDosis1() {
		return vacunaHepatitisBDosis1;
	}


	public void setVacunaHepatitisBDosis1(int vacunaHepatitisBDosis1) {
		this.vacunaHepatitisBDosis1 = vacunaHepatitisBDosis1;
	}


	public int getVacunaHepatitisBDosis2() {
		return vacunaHepatitisBDosis2;
	}


	public void setVacunaHepatitisBDosis2(int vacunaHepatitisBDosis2) {
		this.vacunaHepatitisBDosis2 = vacunaHepatitisBDosis2;
	}


	public int getVacunaHepatitisBDosis3() {
		return vacunaHepatitisBDosis3;
	}


	public void setVacunaHepatitisBDosis3(int vacunaHepatitisBDosis3) {
		this.vacunaHepatitisBDosis3 = vacunaHepatitisBDosis3;
	}


	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}


	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}


	public int getCodigoFichaDengue() {
		return codigoFichaDengue;
	}


	public void setCodigoFichaDengue(int codigoFichaDengue) {
		this.codigoFichaDengue = codigoFichaDengue;
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


	public int getEstadoFicha() {
		return estadoFicha;
	}


	public void setEstadoFicha(int estadoFicha) {
		this.estadoFicha = estadoFicha;
	}


	public String getLoginUsuario() {
		return loginUsuario;
	}


	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
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


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
	}


	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}


	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
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


	public String getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
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


	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}


	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
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


	public int getEstadoAnterior() {
		return estadoAnterior;
	}


	public void setEstadoAnterior(int estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
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
