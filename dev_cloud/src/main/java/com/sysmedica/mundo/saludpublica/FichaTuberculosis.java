package com.sysmedica.mundo.saludpublica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.mundo.saludpublica.Paciente;
import com.sysmedica.dao.FichaTuberculosisDao;

public class FichaTuberculosis {

	FichaTuberculosisDao fichaTuberculosisDao;
	
	private boolean activa;
	
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
    
    private Paciente paciente;
    
    //**************************************
    // DATOS CARA B :
    
    private int codigoFichaTuberculosis;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
    private boolean baciloscopia;
    private boolean cultivo;
    private boolean histopatologia;
    private boolean clinicaPaciente;
    private boolean nexoEpidemiologico;
    private boolean radiologico;
    private boolean tuberculina;
    private boolean ada;
    private int resultadoBk;
    private String resultadoAda;
    private String resultadoTuberculina;
    private int tipoTuberculosis;
    private boolean tieneCicatriz;
    private String fuenteContagio;
    private int metodoHallazgo;
    private String otroCual;
    private int asociadoVih;
    private boolean asesoriaVih;
    private String observaciones;
    private boolean otroDx;
    private int realizoCultivo;
    private String otroTipoTuberculosis;
    private int fuenteContagio2;
    private String pais;
    private int areaProcedencia;
    
    
    private String codCiudadResidencia;
    private String codDepResidencia;
    private String nombreMunProcedencia;
    private String nombreDepProcedencia;
    private String nombreMunNotifica;
    private String nombreDepNotifica;
    private String nombreInstitucionAtendio;
    private String nombreEnfermedadNotificable;
    
    public static int codigoGlobal;
    
    
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
				fichaTuberculosisDao = myFactory.getFichaTuberculosisDao();
				wasInited = (fichaTuberculosisDao != null);
			}
			return wasInited;
	}
    
    
    public void reset()
    {
    	sire = "";
        municipioNotifica = "";
        departamentoNotifica = "";
        lugarNotifica = "";
        institucionAtendio = 0;
        fechaNotificacion = "";
        
        fechaConsultaGeneral = "";
        codigoMunProcedencia = "";
        codigoDepProcedencia = "";
        lugarProcedencia = "";
        fechaInicioSint = "";
        tipoCaso = 0;
        hospitalizado = false;
        fechaHospitalizacion = "";
        estaVivo = true;
        fechaDefuncion = "";
        nombreProfesional = "";
        
        baciloscopia = false;
        cultivo = false;
        histopatologia = false;
        clinicaPaciente = false;
        nexoEpidemiologico = false;
        radiologico = false;
        tuberculina = false;
        ada = false;
        resultadoBk = 0;
        resultadoAda = "";
        resultadoTuberculina = "";
        tipoTuberculosis = 0;
        tieneCicatriz = false;
        fuenteContagio = "";
        metodoHallazgo = 0;
        otroCual = "";
        asociadoVih = 0;
        asesoriaVih = false;
        observaciones = "";
        
        paciente = new Paciente();
    }
    
    public FichaTuberculosis()
    {
    	reset();
		init(System.getProperty("TIPOBD"));
    }
    
    
    public int insertarFicha(Connection con) 
    {

    	return fichaTuberculosisDao.insertarFicha(con,
												notificar,
											    loginUsuario,
											    codigoFichaTuberculosis,
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
    	return fichaTuberculosisDao.modificarFicha(con,
													sire,
													loginUsuario,
												    codigoFichaTuberculosis,
												    estadoFicha,
												    baciloscopia,
												    cultivo,
												    histopatologia,
												    clinicaPaciente,
												    nexoEpidemiologico,
												    radiologico,
												    tuberculina,
												    ada,
												    otroDx,
												    resultadoBk,
												    resultadoAda,
												    resultadoTuberculina,
												    tipoTuberculosis,
												    tieneCicatriz,
												    fuenteContagio,
												    metodoHallazgo,
												    otroCual,
												    asociadoVih,
												    asesoriaVih,
												    observaciones,
													
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
											        realizoCultivo,
											        otroTipoTuberculosis,
											        fuenteContagio2,
											        pais,
												    areaProcedencia);
    }
    
    
    public void cargarDatos(Connection con, int codigo) 
    {
    	this.codigoFichaTuberculosis = codigo;
    	
    	ResultSet rs = fichaTuberculosisDao.consultaTodoFicha(con,codigo);
    	
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
			    this.setBaciloscopia(rs.getBoolean("baciloscopia"));
			    this.setCultivo(rs.getBoolean("cultivo"));
			    this.setHistopatologia(rs.getBoolean("histopatologia"));
			    this.setClinicaPaciente(rs.getBoolean("clinicaPaciente"));
			    this.setNexoEpidemiologico(rs.getBoolean("nexoepidemiologico"));
			    this.setRadiologico(rs.getBoolean("radiologico"));
			    this.setTuberculina(rs.getBoolean("tuberculina"));
			    this.setAda(rs.getBoolean("ada"));
			    this.setResultadoBk(rs.getInt("bk"));
			    this.setResultadoAda(rs.getString("valorada"));
			    this.setResultadoTuberculina(rs.getString("valortuberculina"));
			    this.setTipoTuberculosis(rs.getInt("tipotuberculosis"));
			    this.setTieneCicatriz(rs.getBoolean("cicatrizvacuna"));
			    this.setFuenteContagio(rs.getString("fuentecontagio"));
			    this.setMetodoHallazgo(rs.getInt("metodohallazgo"));
			    this.setOtroCual(rs.getString("otrometodo"));
			    this.setAsociadoVih(rs.getInt("asociacionVih"));
			    this.setAsesoriaVih(rs.getBoolean("asesoriavih"));
			    this.setObservaciones(rs.getString("observaciones"));
			    this.setPais(rs.getString("pais"));
			    this.setAreaProcedencia(rs.getInt("areaProcedencia"));
			    
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
			    this.setFechaNotificacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaDiligenciamiento")));
			    this.setOtroDx(rs.getBoolean("otroDx"));
			    this.setRealizoCultivo(rs.getInt("realizoCultivo"));
			    this.setOtroTipoTuberculosis(rs.getString("otroTipoTuberculosis"));
			    this.setFuenteContagio2(rs.getInt("fuenteContagio2"));
    		}
    	}
    	catch (SQLException sqle) {
    		
    		sqle.printStackTrace();
    	}
    }
    
    
    
    public void cargarPaciente(Connection con, int codigo, boolean empezarNuevo) {
    	
    	ResultSet rs = fichaTuberculosisDao.consultaDatosPaciente(con,codigo,empezarNuevo);
    	
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
			    
			    if (empezarNuevo==false) {
				    paciente.setAseguradora(rs.getString("aseguradora"));
				    paciente.setRegimenSalud(rs.getString("regimenSalud"));
			    }
    		}
    	}
    	catch (SQLException sqle) {
            
    		Log4JManager.error(sqle.getMessage());
        }
    }
    

    
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaTuberculosisDao.insertarFichaCompleta(con,
															numeroSolicitud,
															loginUsuario,
															codigoPaciente,
															codigoDiagnostico,
															estadoFicha,
															codigoConvenio,
															nombreProfesional,
														    
														    sire,
														    baciloscopia,
														    cultivo,
														    histopatologia,
														    clinicaPaciente,
														    nexoEpidemiologico,
														    radiologico,
														    tuberculina,
														    ada,
														    otroDx,
														    resultadoBk,
														    resultadoAda,
														    resultadoTuberculina,
														    tipoTuberculosis,
														    tieneCicatriz,
														    fuenteContagio,
														    metodoHallazgo,
														    otroCual,
														    asociadoVih,
														    asesoriaVih,
														    observaciones,
															
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
														    activa,
														    realizoCultivo,
													        otroTipoTuberculosis,
													        fuenteContagio2,
													        pais,
														    areaProcedencia);
    }


	public boolean isAda() {
		return ada;
	}


	public void setAda(boolean ada) {
		this.ada = ada;
	}


	public boolean isAsesoriaVih() {
		return asesoriaVih;
	}


	public void setAsesoriaVih(boolean asesoriaVih) {
		this.asesoriaVih = asesoriaVih;
	}


	public int getAsociadoVih() {
		return asociadoVih;
	}


	public void setAsociadoVih(int asociadoVih) {
		this.asociadoVih = asociadoVih;
	}


	public boolean isBaciloscopia() {
		return baciloscopia;
	}


	public void setBaciloscopia(boolean baciloscopia) {
		this.baciloscopia = baciloscopia;
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


	public boolean isClinicaPaciente() {
		return clinicaPaciente;
	}


	public void setClinicaPaciente(boolean clinicaPaciente) {
		this.clinicaPaciente = clinicaPaciente;
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


	public boolean isCultivo() {
		return cultivo;
	}


	public void setCultivo(boolean cultivo) {
		this.cultivo = cultivo;
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


	public FichaTuberculosisDao getFichaTuberculosisDao() {
		return fichaTuberculosisDao;
	}


	public void setFichaTuberculosisDao(FichaTuberculosisDao fichaTuberculosisDao) {
		this.fichaTuberculosisDao = fichaTuberculosisDao;
	}


	public String getFuenteContagio() {
		return fuenteContagio;
	}


	public void setFuenteContagio(String fuenteContagio) {
		this.fuenteContagio = fuenteContagio;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public boolean isHistopatologia() {
		return histopatologia;
	}


	public void setHistopatologia(boolean histopatologia) {
		this.histopatologia = histopatologia;
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


	public int getMetodoHallazgo() {
		return metodoHallazgo;
	}


	public void setMetodoHallazgo(int metodoHallazgo) {
		this.metodoHallazgo = metodoHallazgo;
	}


	public String getMunicipioNotifica() {
		return municipioNotifica;
	}


	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}


	public boolean isNexoEpidemiologico() {
		return nexoEpidemiologico;
	}


	public void setNexoEpidemiologico(boolean nexoEpidemiologico) {
		this.nexoEpidemiologico = nexoEpidemiologico;
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


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public String getOtroCual() {
		return otroCual;
	}


	public void setOtroCual(String otroCual) {
		this.otroCual = otroCual;
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


	public boolean isRadiologico() {
		return radiologico;
	}


	public void setRadiologico(boolean radiologico) {
		this.radiologico = radiologico;
	}


	public String getResultadoAda() {
		return resultadoAda;
	}


	public void setResultadoAda(String resultadoAda) {
		this.resultadoAda = resultadoAda;
	}


	public int getResultadoBk() {
		return resultadoBk;
	}


	public void setResultadoBk(int resultadoBk) {
		this.resultadoBk = resultadoBk;
	}


	public String getResultadoTuberculina() {
		return resultadoTuberculina;
	}


	public void setResultadoTuberculina(String resultadoTuberculina) {
		this.resultadoTuberculina = resultadoTuberculina;
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


	public boolean isTieneCicatriz() {
		return tieneCicatriz;
	}


	public void setTieneCicatriz(boolean tieneCicatriz) {
		this.tieneCicatriz = tieneCicatriz;
	}


	public int getTipoCaso() {
		return tipoCaso;
	}


	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}


	public int getTipoTuberculosis() {
		return tipoTuberculosis;
	}


	public void setTipoTuberculosis(int tipoTuberculosis) {
		this.tipoTuberculosis = tipoTuberculosis;
	}


	public boolean isTuberculina() {
		return tuberculina;
	}


	public void setTuberculina(boolean tuberculina) {
		this.tuberculina = tuberculina;
	}


	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}


	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}


	public int getCodigoFichaTuberculosis() {
		return codigoFichaTuberculosis;
	}


	public void setCodigoFichaTuberculosis(int codigoFichaTuberculosis) {
		this.codigoFichaTuberculosis = codigoFichaTuberculosis;
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


	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}


	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	public String getCodCiudadResidencia() {
		return codCiudadResidencia;
	}


	public void setCodCiudadResidencia(String codCiudadResidencia) {
		this.codCiudadResidencia = codCiudadResidencia;
	}


	public String getCodDepResidencia() {
		return codDepResidencia;
	}


	public void setCodDepResidencia(String codDepResidencia) {
		this.codDepResidencia = codDepResidencia;
	}


	public String getNombreDepProcedencia() {
		return nombreDepProcedencia;
	}


	public void setNombreDepProcedencia(String nombreDepProcedencia) {
		this.nombreDepProcedencia = nombreDepProcedencia;
	}


	public String getNombreEnfermedadNotificable() {
		return nombreEnfermedadNotificable;
	}


	public void setNombreEnfermedadNotificable(String nombreEnfermedadNotificable) {
		this.nombreEnfermedadNotificable = nombreEnfermedadNotificable;
	}


	public String getNombreInstitucionAtendio() {
		return nombreInstitucionAtendio;
	}


	public void setNombreInstitucionAtendio(String nombreInstitucionAtendio) {
		this.nombreInstitucionAtendio = nombreInstitucionAtendio;
	}


	public String getNombreMunProcedencia() {
		return nombreMunProcedencia;
	}


	public void setNombreMunProcedencia(String nombreMunProcedencia) {
		this.nombreMunProcedencia = nombreMunProcedencia;
	}


	public String getNombreDepNotifica() {
		return nombreDepNotifica;
	}


	public void setNombreDepNotifica(String nombreDepNotifica) {
		this.nombreDepNotifica = nombreDepNotifica;
	}


	public String getNombreMunNotifica() {
		return nombreMunNotifica;
	}


	public void setNombreMunNotifica(String nombreMunNotifica) {
		this.nombreMunNotifica = nombreMunNotifica;
	}


	public boolean isActiva() {
		return activa;
	}


	public void setActiva(boolean activa) {
		this.activa = activa;
	}


	public int getFuenteContagio2() {
		return fuenteContagio2;
	}


	public void setFuenteContagio2(int fuenteContagio2) {
		this.fuenteContagio2 = fuenteContagio2;
	}


	public boolean isOtroDx() {
		return otroDx;
	}


	public void setOtroDx(boolean otroDx) {
		this.otroDx = otroDx;
	}


	public String getOtroTipoTuberculosis() {
		return otroTipoTuberculosis;
	}


	public void setOtroTipoTuberculosis(String otroTipoTuberculosis) {
		this.otroTipoTuberculosis = otroTipoTuberculosis;
	}


	public int getRealizoCultivo() {
		return realizoCultivo;
	}


	public void setRealizoCultivo(int realizoCultivo) {
		this.realizoCultivo = realizoCultivo;
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
