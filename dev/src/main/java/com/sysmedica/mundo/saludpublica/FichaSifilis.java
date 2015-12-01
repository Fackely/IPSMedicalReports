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
import com.sysmedica.dao.FichaSifilisDao;

public class FichaSifilis {

	private Logger logger = Logger.getLogger(FichaSifilis.class);
	
	private boolean activa;
	
	FichaSifilisDao fichaSifilisDao;
	
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
    
    private int codigoFichaSifilis;
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
    
	private boolean controlPrenatal;
	private int edadGestacional;
	private int numeroControles;
	private int edadGestacionalSero1;
	private int edadGestacionalDiag1;
	private int edadGestacionalTrat;
	private int edadGestacionalParto;
	private int estadoNacimiento;
	private int recienNacido;
	private int lugarAtencionParto;
	private boolean recibioTratamiento;
	private String medicamentoAdmin;
	private int dosisAplicadas;
	private boolean tratamientoHospitalario;
	private boolean tratamientoAmbulatorio;
	private boolean diagnosticoContactos;
	private boolean tratamientoContactos;
	private boolean diagnosticoIts;
	private String cualesIts;
	private String observaciones;
	private int condicionMomentoDx;
	private int tipoTratamiento;
	private int otrasIts;
	private int esquemaCompleto;
	private int alergiaPenicilina;
	private int desensibilizaPenicilina;
	private String pais;
    private int areaProcedencia;
	
	private HashMap signosRecienNacido;
	private HashMap datosLaboratorioSifilis;
	
	private Paciente paciente;
	
	public static int codigoGlobal;
	
	private int estadoAnterior;
	
	public FichaSifilis() {
		
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
				fichaSifilisDao = myFactory.getFichaSifilisDao();
				wasInited = (fichaSifilisDao != null);
			}
			return wasInited;
	}
    
    
    public void reset() {
    	
    	sire="";
    	controlPrenatal = false;
    	estadoNacimiento = 0;
    	recienNacido = 0;
    	lugarAtencionParto = 0;
    	medicamentoAdmin = "";
    	cualesIts = "";
    	observaciones = "";
    	
    	signosRecienNacido = new HashMap();
    	datosLaboratorioSifilis = new HashMap();
    	
    	paciente = new Paciente();
    	
    	edadGestacional = 0;
    	numeroControles = 0;
    	edadGestacionalSero1 = 0;
    	edadGestacionalDiag1 = 0;
    	edadGestacionalTrat = 0;
    	edadGestacionalParto = 0;
    	estadoNacimiento = 0;
    	recienNacido = 0;
    	lugarAtencionParto = 0;
    	recibioTratamiento = false;
    	medicamentoAdmin = "";
    	dosisAplicadas = 0;
    	tratamientoHospitalario = false;
    	tratamientoAmbulatorio = false;
    	diagnosticoContactos = false;
    	tratamientoContactos = false;
    	diagnosticoIts = false;
    	cualesIts = "";
    	observaciones = "";
    	
    	condicionMomentoDx = 0;
    	tipoTratamiento = 0;
    	otrasIts = 0;
    	esquemaCompleto = 0;
    	alergiaPenicilina = 0;
    	desensibilizaPenicilina = 0;
    }
    
    
    
    public int insertarFicha(Connection con) 
    {
    	return fichaSifilisDao.insertarFicha(con,
												notificar,
											    loginUsuario,
											    codigoFichaSifilis,
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
    	return fichaSifilisDao.modificarFicha(con,
												sire,
												loginUsuario,
											    codigoFichaSifilis,
											    estadoFicha,
											    controlPrenatal,
												edadGestacional,
												numeroControles,
												edadGestacionalSero1,
												edadGestacionalDiag1,
												edadGestacionalTrat,
												edadGestacionalParto,
												estadoNacimiento,
												recienNacido,
												lugarAtencionParto,
												recibioTratamiento,
												medicamentoAdmin,
												dosisAplicadas,
												tratamientoHospitalario,
												tratamientoAmbulatorio,
												diagnosticoContactos,
												tratamientoContactos,
												diagnosticoIts,
												cualesIts,
												observaciones,
												
												signosRecienNacido,
												datosLaboratorioSifilis,
												
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
											    condicionMomentoDx,
										    	tipoTratamiento,
										    	otrasIts,
										    	esquemaCompleto,
										    	alergiaPenicilina,
										    	desensibilizaPenicilina,
										    	pais,
											    areaProcedencia
												);
    }
    
    
    
    public void cargarDatos(Connection con, int codigo) 
    {
    	this.codigoFichaSifilis = codigo;
    	
    	ResultSet rs = fichaSifilisDao.consultaTodoFicha(con,codigo);
    	
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
		    	this.setControlPrenatal(rs.getBoolean("controlPrenatal"));
		    	this.setEdadGestacional(rs.getInt("edadGestacional"));
		    	this.setNumeroControles(rs.getInt("numeroControles"));
		    	this.setEdadGestacionalSero1(rs.getInt("edadGestacionalSero1"));
		    	this.setEdadGestacionalDiag1(rs.getInt("edadGestacionalDiag1"));
		    	this.setEdadGestacionalTrat(rs.getInt("edadGestacionalTrat"));
		    	this.setEdadGestacionalParto(rs.getInt("edadGestacionalParto"));
		    	this.setEstadoNacimiento(rs.getInt("estadoNacimiento"));
		    	this.setRecienNacido(rs.getInt("recienNacido"));
		    	this.setLugarAtencionParto(rs.getInt("lugarAtencionParto"));
		    	this.setRecibioTratamiento(rs.getBoolean("recibioTratamiento"));
		    	this.setMedicamentoAdmin(rs.getString("medicamentoAdmin"));
		    	this.setDosisAplicadas(rs.getInt("dosisAplicadas"));
		    	this.setTratamientoHospitalario(rs.getBoolean("tratamientoHospitalario"));
		    	this.setTratamientoAmbulatorio(rs.getBoolean("tratamientoAmbulatorio"));
		    	this.setDiagnosticoContactos(rs.getBoolean("diagnosticoContactos"));
		    	this.setTratamientoContactos(rs.getBoolean("tratamientoContactos"));
		    	this.setDiagnosticoIts(rs.getBoolean("diagnosticoIts"));
		    	this.setCualesIts(rs.getString("cualesIts"));
		    	this.setObservaciones(rs.getString("observaciones"));
		    	this.setFechaNotificacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaDiligenciamiento")));
		    	
		    	ResultSet rs2 = fichaSifilisDao.consultarSignos(con,codigo);
			    
			    HashMap signos = new HashMap();
			    
			    while (rs2.next()) {
			    	
			    	signos.put("signo_"+rs2.getInt("codigosigno"),"true");
			    }
			    
			    this.setSignosRecienNacido(signos);
			    
			    
			    ResultSet rs3 = fichaSifilisDao.consultarDatosLaboratorio(con,codigo);
			    
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
			    
			    this.setDatosLaboratorioSifilis(datosLab);
			    
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
    
    
    
    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaSifilisDao.consultaDatosPaciente(con,codigo,empezarnuevo);
    	
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
    	return fichaSifilisDao.insertarFichaCompleta(con,
														numeroSolicitud,
														loginUsuario,
														codigoPaciente,
														codigoDiagnostico,
														estadoFicha,
														codigoConvenio,
														nombreProfesional,
													    
													    sire,
													    controlPrenatal,
														edadGestacional,
														numeroControles,
														edadGestacionalSero1,
														edadGestacionalDiag1,
														edadGestacionalTrat,
														edadGestacionalParto,
														estadoNacimiento,
														recienNacido,
														lugarAtencionParto,
														recibioTratamiento,
														medicamentoAdmin,
														dosisAplicadas,
														tratamientoHospitalario,
														tratamientoAmbulatorio,
														diagnosticoContactos,
														tratamientoContactos,
														diagnosticoIts,
														cualesIts,
														observaciones,
														
														signosRecienNacido,
														datosLaboratorioSifilis,
														
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
													    condicionMomentoDx,
												    	tipoTratamiento,
												    	otrasIts,
												    	esquemaCompleto,
												    	alergiaPenicilina,
												    	desensibilizaPenicilina,
												    	pais,
													    areaProcedencia);
    }
    
    
    
    public int terminarFicha(Connection con)
	{
		return fichaSifilisDao.terminarFicha(con,codigoFichaSifilis); 
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


	public int getCodigoFichaSifilis() {
		return codigoFichaSifilis;
	}


	public void setCodigoFichaSifilis(int codigoFichaSifilis) {
		this.codigoFichaSifilis = codigoFichaSifilis;
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


	public boolean isControlPrenatal() {
		return controlPrenatal;
	}


	public void setControlPrenatal(boolean controlPrenatal) {
		this.controlPrenatal = controlPrenatal;
	}


	public String getCualesIts() {
		return cualesIts;
	}


	public void setCualesIts(String cualesIts) {
		this.cualesIts = cualesIts;
	}


	public HashMap getDatosLaboratorioSifilis() {
		return datosLaboratorioSifilis;
	}


	public void setDatosLaboratorioSifilis(HashMap datosLaboratorioSifilis) {
		this.datosLaboratorioSifilis = datosLaboratorioSifilis;
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


	public boolean isDiagnosticoContactos() {
		return diagnosticoContactos;
	}


	public void setDiagnosticoContactos(boolean diagnosticoContactos) {
		this.diagnosticoContactos = diagnosticoContactos;
	}


	public boolean isDiagnosticoIts() {
		return diagnosticoIts;
	}


	public void setDiagnosticoIts(boolean diagnosticoIts) {
		this.diagnosticoIts = diagnosticoIts;
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


	public String getEdad() {
		return edad;
	}


	public void setEdad(String edad) {
		this.edad = edad;
	}


	public int getEdadGestacional() {
		return edadGestacional;
	}


	public void setEdadGestacional(int edadGestacional) {
		this.edadGestacional = edadGestacional;
	}


	public int getEdadGestacionalDiag1() {
		return edadGestacionalDiag1;
	}


	public void setEdadGestacionalDiag1(int edadGestacionalDiag1) {
		this.edadGestacionalDiag1 = edadGestacionalDiag1;
	}


	public int getEdadGestacionalParto() {
		return edadGestacionalParto;
	}


	public void setEdadGestacionalParto(int edadGestacionalParto) {
		this.edadGestacionalParto = edadGestacionalParto;
	}


	public int getEdadGestacionalSero1() {
		return edadGestacionalSero1;
	}


	public void setEdadGestacionalSero1(int edadGestacionalSero1) {
		this.edadGestacionalSero1 = edadGestacionalSero1;
	}


	public int getEdadGestacionalTrat() {
		return edadGestacionalTrat;
	}


	public void setEdadGestacionalTrat(int edadGestacionalTrat) {
		this.edadGestacionalTrat = edadGestacionalTrat;
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


	public int getEstadoNacimiento() {
		return estadoNacimiento;
	}


	public void setEstadoNacimiento(int estadoNacimiento) {
		this.estadoNacimiento = estadoNacimiento;
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


	public String getLoginUsuario() {
		return loginUsuario;
	}


	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}


	public int getLugarAtencionParto() {
		return lugarAtencionParto;
	}


	public void setLugarAtencionParto(int lugarAtencionParto) {
		this.lugarAtencionParto = lugarAtencionParto;
	}


	public String getMedicamentoAdmin() {
		return medicamentoAdmin;
	}


	public void setMedicamentoAdmin(String medicamentoAdmin) {
		this.medicamentoAdmin = medicamentoAdmin;
	}


	public boolean isNotificar() {
		return notificar;
	}


	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}


	public int getNumeroControles() {
		return numeroControles;
	}


	public void setNumeroControles(int numeroControles) {
		this.numeroControles = numeroControles;
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


	public Paciente getPaciente() {
		return paciente;
	}


	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
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


	public boolean isRecibioTratamiento() {
		return recibioTratamiento;
	}


	public void setRecibioTratamiento(boolean recibioTratamiento) {
		this.recibioTratamiento = recibioTratamiento;
	}


	public int getRecienNacido() {
		return recienNacido;
	}


	public void setRecienNacido(int recienNacido) {
		this.recienNacido = recienNacido;
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


	public HashMap getSignosRecienNacido() {
		return signosRecienNacido;
	}


	public void setSignosRecienNacido(HashMap signosRecienNacido) {
		this.signosRecienNacido = signosRecienNacido;
	}


	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}


	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}


	public boolean isTratamientoAmbulatorio() {
		return tratamientoAmbulatorio;
	}


	public void setTratamientoAmbulatorio(boolean tratamientoAmbulatorio) {
		this.tratamientoAmbulatorio = tratamientoAmbulatorio;
	}


	public boolean isTratamientoContactos() {
		return tratamientoContactos;
	}


	public void setTratamientoContactos(boolean tratamientoContactos) {
		this.tratamientoContactos = tratamientoContactos;
	}


	public boolean isTratamientoHospitalario() {
		return tratamientoHospitalario;
	}


	public void setTratamientoHospitalario(boolean tratamientoHospitalario) {
		this.tratamientoHospitalario = tratamientoHospitalario;
	}


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
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


	public FichaSifilisDao getFichaSifilisDao() {
		return fichaSifilisDao;
	}


	public void setFichaSifilisDao(FichaSifilisDao fichaSifilisDao) {
		this.fichaSifilisDao = fichaSifilisDao;
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


	public int getAlergiaPenicilina() {
		return alergiaPenicilina;
	}


	public void setAlergiaPenicilina(int alergiaPenicilina) {
		this.alergiaPenicilina = alergiaPenicilina;
	}


	public int getCondicionMomentoDx() {
		return condicionMomentoDx;
	}


	public void setCondicionMomentoDx(int condicionMomentoDx) {
		this.condicionMomentoDx = condicionMomentoDx;
	}


	public int getDesensibilizaPenicilina() {
		return desensibilizaPenicilina;
	}


	public void setDesensibilizaPenicilina(int desensibilizaPenicilina) {
		this.desensibilizaPenicilina = desensibilizaPenicilina;
	}


	public int getEsquemaCompleto() {
		return esquemaCompleto;
	}


	public void setEsquemaCompleto(int esquemaCompleto) {
		this.esquemaCompleto = esquemaCompleto;
	}


	public int getOtrasIts() {
		return otrasIts;
	}


	public void setOtrasIts(int otrasIts) {
		this.otrasIts = otrasIts;
	}


	public int getTipoTratamiento() {
		return tipoTratamiento;
	}


	public void setTipoTratamiento(int tipoTratamiento) {
		this.tipoTratamiento = tipoTratamiento;
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
