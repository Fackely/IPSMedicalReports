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
import com.sysmedica.dao.FichaAcciOfidicoDao;

public class FichaAcciOfidico {
	
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
    
    private int codigoFichaAcciOfidico;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
	
	private String fechaAccidente;
	private String nombreVereda;
	private int actividadAccidente;
	private String cualActividad;
	private int tipoAtencionInicial;
	private String cualAtencion;
	private int practicasNoMedicas;
	private String cualPractica;
	private int localizacionMordedura;
	private int huellasColmillos;
	private int serpienteIdentificada;
	private int serpienteCapturada;
	private int generoAgenteAgresor;
	private String cualAgente;
	private int nombreAgenteAgresor;
	private String cualLocal;
	private String cualComplicacion;
	private String cualSistemica;
	private int severidadAccidente;
	private int empleoSuero;
	private int diasTranscurridos;
	private int horasTranscurridas;
	private int tipoSueroAntiofidico;
	private String cualSuero;
	private int dosisSuero;
	private int horasSuero;
	private int minutosSuero;
	private int tratamientoQuirurgico;
	private int tipoTratamiento;
	

	private HashMap manifestacionesLocales;
	private HashMap manifestacionesSistemicas;
	private HashMap complicacionesLocales;
	private HashMap complicacionesSistemicas;
    
	  
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
    
    
    FichaAcciOfidicoDao fichaAcciOfidicoDao;
    
    public void reset() {
    	
    	fechaAccidente = "";
    	nombreVereda = "";
    	actividadAccidente = 0;
    	cualActividad = "";
    	tipoAtencionInicial = 0;
    	cualAtencion = "";
    	practicasNoMedicas = 0;
    	cualPractica = "";
    	localizacionMordedura = 0;
    	huellasColmillos = 0;
    	serpienteIdentificada = 0;
    	serpienteCapturada = 0;
    	generoAgenteAgresor = 0;
    	cualAgente = "";
    	nombreAgenteAgresor = 0;
    	cualLocal = "";
    	cualComplicacion = "";
    	cualSistemica = "";
    	severidadAccidente = 0;
    	empleoSuero = 0;
    	diasTranscurridos = 0;
    	horasTranscurridas = 0;
    	tipoSueroAntiofidico = 0;
    	cualSuero = "";
    	dosisSuero = 0;
    	horasSuero = 0;
    	minutosSuero = 0;
    	tratamientoQuirurgico = 0;
    	tipoTratamiento = 0;
    	
    	paciente = new Paciente();
    	

    	manifestacionesLocales = new HashMap();
    	manifestacionesSistemicas = new HashMap();
    	complicacionesLocales = new HashMap();
    	complicacionesSistemicas = new HashMap();
        
    }
    
    
    public FichaAcciOfidico()
    {
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
			fichaAcciOfidicoDao = myFactory.getFichaAcciOfidicoDao();
			wasInited = (fichaAcciOfidicoDao != null);
		}
		return wasInited;
	}
    
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaAcciOfidicoDao.insertarFichaCompleta(con,
											    		numeroSolicitud,
														loginUsuario,
														codigoPaciente,
														codigoDiagnostico,
														estadoFicha,
														codigoConvenio,
														nombreProfesional,
													    
													    sire,
														notificar,
														
														codigoFichaAcciOfidico,										    
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
													    
													    fechaAccidente,
														nombreVereda,
														actividadAccidente,
														cualActividad,
														tipoAtencionInicial,
														cualAtencion,
														practicasNoMedicas,
														cualPractica,
														localizacionMordedura,
														huellasColmillos,
														serpienteIdentificada,
														serpienteCapturada,
														generoAgenteAgresor,
														cualAgente,
														nombreAgenteAgresor,
														cualLocal,
														cualComplicacion,
														cualSistemica,
														severidadAccidente,
														empleoSuero,
														diasTranscurridos,
														horasTranscurridas,
														tipoSueroAntiofidico,
														cualSuero,
														dosisSuero,
														horasSuero,
														minutosSuero,
														tratamientoQuirurgico,
														tipoTratamiento,
													    
													    activa,
													    pais,
													    areaProcedencia,
													    
													    manifestacionesLocales,
													    manifestacionesSistemicas,
													    complicacionesLocales,
													    complicacionesSistemicas);
    }
    
    
    

    public int modificarFicha(Connection con)
    {
    	return fichaAcciOfidicoDao.modificarFicha(con,
													sire,
													notificar,
												    loginUsuario,
												    codigoFichaAcciOfidico,
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
												    
												    fechaAccidente,
													nombreVereda,
													actividadAccidente,
													cualActividad,
													tipoAtencionInicial,
													cualAtencion,
													practicasNoMedicas,
													cualPractica,
													localizacionMordedura,
													huellasColmillos,
													serpienteIdentificada,
													serpienteCapturada,
													generoAgenteAgresor,
													cualAgente,
													nombreAgenteAgresor,
													cualLocal,
													cualComplicacion,
													cualSistemica,
													severidadAccidente,
													empleoSuero,
													diasTranscurridos,
													horasTranscurridas,
													tipoSueroAntiofidico,
													cualSuero,
													dosisSuero,
													horasSuero,
													minutosSuero,
													tratamientoQuirurgico,
													tipoTratamiento,
												    pais,
												    areaProcedencia,
												    manifestacionesLocales,
												    manifestacionesSistemicas,
												    complicacionesLocales,
												    complicacionesSistemicas);
    }
    
    
    

    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaAcciOfidicoDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    	
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
			    	
			    
			    paciente.setAseguradora(rs.getString("aseguradora"));
			    paciente.setRegimenSalud(rs.getString("regimenSalud"));
    		}
    	}
    	catch (SQLException sqle) {
            
        }
    }
    
    
    
    

    public void cargarDatos(Connection con, int codigo) 
    {
    	this.codigoFichaAcciOfidico = codigo;
    	
    	ResultSet rs = fichaAcciOfidicoDao.consultaTodoFicha(con,codigo);
    	
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
			    
			    this.setFechaAccidente(rs.getString("fechaAccidente"));
			    this.setNombreVereda(rs.getString("nombreVereda"));
			    this.setActividadAccidente(rs.getInt("actividadAccidente"));
			    this.setCualActividad(rs.getString("cualActividad"));
			    this.setTipoAtencionInicial(rs.getInt("tipoAtencionInicial"));
			    this.setCualAtencion(rs.getString("cualAtencion"));
			    this.setPracticasNoMedicas(rs.getInt("practicasNoMedicas"));
			    this.setCualPractica(rs.getString("cualPractica"));
			    this.setLocalizacionMordedura(rs.getInt("localizacionMordedura"));
			    this.setHuellasColmillos(rs.getInt("huellasColmillos"));
			    this.setSerpienteIdentificada(rs.getInt("serpienteIdentificada"));
			    this.setSerpienteCapturada(rs.getInt("serpienteCapturada"));
			    this.setGeneroAgenteAgresor(rs.getInt("generoAgenteAgresor"));
			    this.setCualAgente(rs.getString("cualAgente"));
			    this.setNombreAgenteAgresor(rs.getInt("nombreAgenteAgresor"));
			    this.setCualLocal(rs.getString("cualLocal"));
			    this.setCualComplicacion(rs.getString("cualComplicacion"));
			    this.setCualSistemica(rs.getString("cualSistemica"));
			    this.setSeveridadAccidente(rs.getInt("severidadAccidente"));
			    this.setEmpleoSuero(rs.getInt("empleoSuero"));
			    this.setDiasTranscurridos(rs.getInt("diasTranscurridos"));
			    this.setHorasTranscurridas(rs.getInt("horasTranscurridas"));
			    this.setTipoSueroAntiofidico(rs.getInt("tipoSueroAntiofidico"));
			    this.setCualSuero(rs.getString("cualSuero"));
			    this.setDosisSuero(rs.getInt("dosisSuero"));
			    this.setHorasSuero(rs.getInt("horasSuero"));
			    this.setMinutosSuero(rs.getInt("minutosSuero"));
			    this.setTratamientoQuirurgico(rs.getInt("tratamientoQuirurgico"));
			    this.setTipoTratamiento(rs.getInt("tipoTratamiento"));
			    
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
			    
			    
			    ResultSet rs2 = fichaAcciOfidicoDao.consultarManifestacionLocal(con,codigo);
			    
			    HashMap maniLocal = new HashMap();
			    
			    while (rs2.next()) {
			    	
			    	maniLocal.put("manilocal_"+rs2.getInt("codigomanifestacion"),"true");
			    }
			    
			    this.setManifestacionesLocales(maniLocal);
			    
			    

			    ResultSet rs3 = fichaAcciOfidicoDao.consultarManifestacionSistemica(con,codigo);
			    
			    HashMap maniSistemica = new HashMap();
			    
			    while (rs3.next()) {
			    	
			    	maniSistemica.put("manisistemica_"+rs3.getInt("codigomanifestacion"),"true");
			    }
			    
			    this.setManifestacionesSistemicas(maniSistemica);
			    
			    
			    
			    ResultSet rs4 = fichaAcciOfidicoDao.consultarComplicacionLocal(con,codigo);
			    
			    HashMap compliLocal = new HashMap();
			    
			    while (rs4.next()) {
			    	
			    	compliLocal.put("complilocal_"+rs4.getInt("codigocomplicacion"),"true");
			    }
			    
			    this.setComplicacionesLocales(compliLocal);
			    
			    

			    ResultSet rs5 = fichaAcciOfidicoDao.consultarComplicacionSistemica(con,codigo);
			    
			    HashMap compliSist = new HashMap();
			    
			    while (rs5.next()) {
			    	
			    	compliSist.put("complisistemica_"+rs5.getInt("codigocomplicacion"),"true");
			    }
			    
			    this.setComplicacionesSistemicas(compliSist);
			    
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



	public int getActividadAccidente() {
		return actividadAccidente;
	}



	public void setActividadAccidente(int actividadAccidente) {
		this.actividadAccidente = actividadAccidente;
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



	public String getCualActividad() {
		return cualActividad;
	}



	public void setCualActividad(String cualActividad) {
		this.cualActividad = cualActividad;
	}



	public String getCualAgente() {
		return cualAgente;
	}



	public void setCualAgente(String cualAgente) {
		this.cualAgente = cualAgente;
	}



	public String getCualAtencion() {
		return cualAtencion;
	}



	public void setCualAtencion(String cualAtencion) {
		this.cualAtencion = cualAtencion;
	}



	public String getCualComplicacion() {
		return cualComplicacion;
	}



	public void setCualComplicacion(String cualComplicacion) {
		this.cualComplicacion = cualComplicacion;
	}



	public String getCualLocal() {
		return cualLocal;
	}



	public void setCualLocal(String cualLocal) {
		this.cualLocal = cualLocal;
	}



	public String getCualPractica() {
		return cualPractica;
	}



	public void setCualPractica(String cualPractica) {
		this.cualPractica = cualPractica;
	}



	public String getCualSistemica() {
		return cualSistemica;
	}



	public void setCualSistemica(String cualSistemica) {
		this.cualSistemica = cualSistemica;
	}



	public String getCualSuero() {
		return cualSuero;
	}



	public void setCualSuero(String cualSuero) {
		this.cualSuero = cualSuero;
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



	public int getDiasTranscurridos() {
		return diasTranscurridos;
	}



	public void setDiasTranscurridos(int diasTranscurridos) {
		this.diasTranscurridos = diasTranscurridos;
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



	public int getDosisSuero() {
		return dosisSuero;
	}



	public void setDosisSuero(int dosisSuero) {
		this.dosisSuero = dosisSuero;
	}



	public String getEdad() {
		return edad;
	}



	public void setEdad(String edad) {
		this.edad = edad;
	}



	public int getEmpleoSuero() {
		return empleoSuero;
	}



	public void setEmpleoSuero(int empleoSuero) {
		this.empleoSuero = empleoSuero;
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



	public String getFechaAccidente() {
		return fechaAccidente;
	}



	public void setFechaAccidente(String fechaAccidente) {
		this.fechaAccidente = fechaAccidente;
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



	public String getGenero() {
		return genero;
	}



	public void setGenero(String genero) {
		this.genero = genero;
	}



	public int getGeneroAgenteAgresor() {
		return generoAgenteAgresor;
	}



	public void setGeneroAgenteAgresor(int generoAgenteAgresor) {
		this.generoAgenteAgresor = generoAgenteAgresor;
	}



	public int getHorasSuero() {
		return horasSuero;
	}



	public void setHorasSuero(int horasSuero) {
		this.horasSuero = horasSuero;
	}



	public int getHorasTranscurridas() {
		return horasTranscurridas;
	}



	public void setHorasTranscurridas(int horasTranscurridas) {
		this.horasTranscurridas = horasTranscurridas;
	}



	public boolean isHospitalizado() {
		return hospitalizado;
	}



	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}



	public int getHuellasColmillos() {
		return huellasColmillos;
	}



	public void setHuellasColmillos(int huellasColmillos) {
		this.huellasColmillos = huellasColmillos;
	}



	public int getInstitucionAtendio() {
		return institucionAtendio;
	}



	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
	}



	public int getLocalizacionMordedura() {
		return localizacionMordedura;
	}



	public void setLocalizacionMordedura(int localizacionMordedura) {
		this.localizacionMordedura = localizacionMordedura;
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



	public int getMinutosSuero() {
		return minutosSuero;
	}



	public void setMinutosSuero(int minutosSuero) {
		this.minutosSuero = minutosSuero;
	}



	public String getMunicipioNotifica() {
		return municipioNotifica;
	}



	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}



	public int getNombreAgenteAgresor() {
		return nombreAgenteAgresor;
	}



	public void setNombreAgenteAgresor(int nombreAgenteAgresor) {
		this.nombreAgenteAgresor = nombreAgenteAgresor;
	}



	public String getNombreProfesional() {
		return nombreProfesional;
	}



	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}



	public String getNombreVereda() {
		return nombreVereda;
	}



	public void setNombreVereda(String nombreVereda) {
		this.nombreVereda = nombreVereda;
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



	public int getPracticasNoMedicas() {
		return practicasNoMedicas;
	}



	public void setPracticasNoMedicas(int practicasNoMedicas) {
		this.practicasNoMedicas = practicasNoMedicas;
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



	public int getSerpienteCapturada() {
		return serpienteCapturada;
	}



	public void setSerpienteCapturada(int serpienteCapturada) {
		this.serpienteCapturada = serpienteCapturada;
	}



	public int getSerpienteIdentificada() {
		return serpienteIdentificada;
	}



	public void setSerpienteIdentificada(int serpienteIdentificada) {
		this.serpienteIdentificada = serpienteIdentificada;
	}



	public int getSeveridadAccidente() {
		return severidadAccidente;
	}



	public void setSeveridadAccidente(int severidadAccidente) {
		this.severidadAccidente = severidadAccidente;
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



	public int getTipoAtencionInicial() {
		return tipoAtencionInicial;
	}



	public void setTipoAtencionInicial(int tipoAtencionInicial) {
		this.tipoAtencionInicial = tipoAtencionInicial;
	}



	public int getTipoCaso() {
		return tipoCaso;
	}



	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}



	public int getTipoSueroAntiofidico() {
		return tipoSueroAntiofidico;
	}



	public void setTipoSueroAntiofidico(int tipoSueroAntiofidico) {
		this.tipoSueroAntiofidico = tipoSueroAntiofidico;
	}



	public int getTipoTratamiento() {
		return tipoTratamiento;
	}



	public void setTipoTratamiento(int tipoTratamiento) {
		this.tipoTratamiento = tipoTratamiento;
	}



	public int getTratamientoQuirurgico() {
		return tratamientoQuirurgico;
	}



	public void setTratamientoQuirurgico(int tratamientoQuirurgico) {
		this.tratamientoQuirurgico = tratamientoQuirurgico;
	}



	public int getCodigoFichaAcciOfidico() {
		return codigoFichaAcciOfidico;
	}



	public void setCodigoFichaAcciOfidico(int codigoFichaAcciOfidico) {
		this.codigoFichaAcciOfidico = codigoFichaAcciOfidico;
	}


	public HashMap getComplicacionesLocales() {
		return complicacionesLocales;
	}


	public void setComplicacionesLocales(HashMap complicacionesLocales) {
		this.complicacionesLocales = complicacionesLocales;
	}


	public HashMap getComplicacionesSistemicas() {
		return complicacionesSistemicas;
	}


	public void setComplicacionesSistemicas(HashMap complicacionesSistemicas) {
		this.complicacionesSistemicas = complicacionesSistemicas;
	}


	public HashMap getManifestacionesLocales() {
		return manifestacionesLocales;
	}


	public void setManifestacionesLocales(HashMap manifestacionesLocales) {
		this.manifestacionesLocales = manifestacionesLocales;
	}


	public HashMap getManifestacionesSistemicas() {
		return manifestacionesSistemicas;
	}


	public void setManifestacionesSistemicas(HashMap manifestacionesSistemicas) {
		this.manifestacionesSistemicas = manifestacionesSistemicas;
	}
    
    
}
