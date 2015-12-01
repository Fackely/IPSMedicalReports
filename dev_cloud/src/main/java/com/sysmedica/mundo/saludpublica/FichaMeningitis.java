package com.sysmedica.mundo.saludpublica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.FichaMeningitisDao;

public class FichaMeningitis {


	private Logger logger = Logger.getLogger(FichaMeningitis.class);
	
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
    
    private int codigoFichaMeningitis;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
	
    private int vacunaAntihib;
    private int dosis;
    private String fechaPrimeraDosis;
    private String fechaUltimaDosis;
    private int tieneCarne;
    private int vacunaAntimenin;
    private int dosis2;
    private String fechaPrimeraDosis2;
    private String fechaUltimaDosis2;
    private int tieneCarne2;
    private int vacunaAntineumo;
    private int dosis3;
    private String fechaPrimeraDosis3;
    private String fechaUltimaDosis3;
    private int tieneCarne3;
    private int fiebre;
    private int rigidez;
    private int irritacion;
    private int rash;
    private int abombamiento;
    private int alteracion;
    private int usoAntibioticos;
    private String fechaUltimaDosis4;
    private String observaciones;
    
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
    
    FichaMeningitisDao fichaMeningitisDao;
    
    public void reset()
    {
    	vacunaAntihib = 0;
        dosis = 0;
        fechaPrimeraDosis = "";
        fechaUltimaDosis = "";
        tieneCarne = 0;
        vacunaAntimenin = 0;
        dosis2 = 0;
        fechaPrimeraDosis2 = "";
        fechaUltimaDosis2 = "";
        tieneCarne2 = 0;
        vacunaAntineumo = 0;
        dosis3 = 0;
        fechaPrimeraDosis3 = "";
        fechaUltimaDosis3 = "";
        tieneCarne3 = 0;
        fiebre = 0;
        rigidez = 0;
        irritacion = 0;
        rash = 0;
        abombamiento = 0;
        alteracion = 0;
        usoAntibioticos = 0;
        fechaUltimaDosis4 = "";
        observaciones = "";
        
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
			fichaMeningitisDao = myFactory.getFichaMeningitisDao();
			wasInited = (fichaMeningitisDao != null);
		}
		return wasInited;
	}
    
    public FichaMeningitis()
    {
    	reset();
        init(System.getProperty("TIPOBD"));
    }
    
    
    

    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaMeningitisDao.insertarFichaCompleta(con,
											    		numeroSolicitud,
														loginUsuario,
														codigoPaciente,
														codigoDiagnostico,
														estadoFicha,
														codigoConvenio,
														nombreProfesional,
													    
													    sire,
														notificar,
														
														codigoFichaMeningitis,										    
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
													    
													    vacunaAntihib,
													    dosis,
													    fechaPrimeraDosis,
													    fechaUltimaDosis,
													    tieneCarne,
													    vacunaAntimenin,
													    dosis2,
													    fechaPrimeraDosis2,
													    fechaUltimaDosis2,
													    tieneCarne2,
													    vacunaAntineumo,
													    dosis3,
													    fechaPrimeraDosis3,
													    fechaUltimaDosis3,
													    tieneCarne3,
													    fiebre,
													    rigidez,
													    irritacion,
													    rash,
													    abombamiento,
													    alteracion,
													    usoAntibioticos,
													    fechaUltimaDosis4,
													    observaciones,
													    
													    activa,
													    pais,
													    areaProcedencia);
    }
    
    
    

    public int modificarFicha(Connection con)
    {
    	return fichaMeningitisDao.modificarFicha(con,
													sire,
													notificar,
												    loginUsuario,
												    codigoFichaMeningitis,
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
									
												    vacunaAntihib,
												    dosis,
												    fechaPrimeraDosis,
												    fechaUltimaDosis,
												    tieneCarne,
												    vacunaAntimenin,
												    dosis2,
												    fechaPrimeraDosis2,
												    fechaUltimaDosis2,
												    tieneCarne2,
												    vacunaAntineumo,
												    dosis3,
												    fechaPrimeraDosis3,
												    fechaUltimaDosis3,
												    tieneCarne3,
												    fiebre,
												    rigidez,
												    irritacion,
												    rash,
												    abombamiento,
												    alteracion,
												    usoAntibioticos,
												    fechaUltimaDosis4,
												    observaciones,
												    
													pais,
												    areaProcedencia);
    }

    
    
    
    

    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaMeningitisDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    	
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
    	this.codigoFichaMeningitis = codigo;
    	
    	ResultSet rs = fichaMeningitisDao.consultaTodoFicha(con,codigo);
    	
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
    
			    this.setVacunaAntihib(rs.getInt("vacunaAntihib"));
			    this.setDosis(rs.getInt("dosis"));
			    this.setFechaPrimeraDosis(rs.getString("fechaPrimeraDosis"));
			    this.setFechaUltimaDosis(rs.getString("fechaUltimaDosis"));
			    this.setTieneCarne(rs.getInt("tieneCarne"));
			    this.setVacunaAntimenin(rs.getInt("vacunaAntimenin"));
			    this.setDosis2(rs.getInt("dosis2"));
			    this.setFechaPrimeraDosis2(rs.getString("fechaPrimeraDosis2"));
			    this.setFechaUltimaDosis2(rs.getString("fechaUltimaDosis2"));
			    this.setTieneCarne2(rs.getInt("tieneCarne2"));
			    this.setVacunaAntineumo(rs.getInt("vacunaAntineumo"));
			    this.setDosis3(rs.getInt("dosis3"));
			    this.setFechaPrimeraDosis3(rs.getString("fechaPrimeraDosis3"));
			    this.setFechaUltimaDosis3(rs.getString("fechaUltimaDosis3"));
			    this.setTieneCarne3(rs.getInt("tieneCarne3"));
			    this.setFiebre(rs.getInt("fiebre"));
			    this.setRigidez(rs.getInt("rigidez"));
			    this.setIrritacion(rs.getInt("irritacion"));
			    this.setRash(rs.getInt("rash"));
			    this.setAbombamiento(rs.getInt("abombamiento"));
			    this.setAlteracion(rs.getInt("alteracion"));
			    this.setUsoAntibioticos(rs.getInt("usoAntibioticos"));
			    this.setFechaUltimaDosis4(rs.getString("fechaUltimaDosis4"));
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
			    
    		}
    	}
    	catch (SQLException sqle) {
    		sqle.printStackTrace();
    	}
    }



	public int getAbombamiento() {
		return abombamiento;
	}



	public void setAbombamiento(int abombamiento) {
		this.abombamiento = abombamiento;
	}



	public boolean isActiva() {
		return activa;
	}



	public void setActiva(boolean activa) {
		this.activa = activa;
	}



	public int getAlteracion() {
		return alteracion;
	}



	public void setAlteracion(int alteracion) {
		this.alteracion = alteracion;
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



	public int getCodigoFichaMeningitis() {
		return codigoFichaMeningitis;
	}



	public void setCodigoFichaMeningitis(int codigoFichaMeningitis) {
		this.codigoFichaMeningitis = codigoFichaMeningitis;
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



	public int getDosis() {
		return dosis;
	}



	public void setDosis(int dosis) {
		this.dosis = dosis;
	}



	public int getDosis2() {
		return dosis2;
	}



	public void setDosis2(int dosis2) {
		this.dosis2 = dosis2;
	}



	public int getDosis3() {
		return dosis3;
	}



	public void setDosis3(int dosis3) {
		this.dosis3 = dosis3;
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



	public String getFechaPrimeraDosis() {
		return fechaPrimeraDosis;
	}



	public void setFechaPrimeraDosis(String fechaPrimeraDosis) {
		this.fechaPrimeraDosis = fechaPrimeraDosis;
	}



	public String getFechaPrimeraDosis2() {
		return fechaPrimeraDosis2;
	}



	public void setFechaPrimeraDosis2(String fechaPrimeraDosis2) {
		this.fechaPrimeraDosis2 = fechaPrimeraDosis2;
	}



	public String getFechaPrimeraDosis3() {
		return fechaPrimeraDosis3;
	}



	public void setFechaPrimeraDosis3(String fechaPrimeraDosis3) {
		this.fechaPrimeraDosis3 = fechaPrimeraDosis3;
	}



	public String getFechaUltimaDosis() {
		return fechaUltimaDosis;
	}



	public void setFechaUltimaDosis(String fechaUltimaDosis) {
		this.fechaUltimaDosis = fechaUltimaDosis;
	}



	public String getFechaUltimaDosis2() {
		return fechaUltimaDosis2;
	}



	public void setFechaUltimaDosis2(String fechaUltimaDosis2) {
		this.fechaUltimaDosis2 = fechaUltimaDosis2;
	}



	public String getFechaUltimaDosis3() {
		return fechaUltimaDosis3;
	}



	public void setFechaUltimaDosis3(String fechaUltimaDosis3) {
		this.fechaUltimaDosis3 = fechaUltimaDosis3;
	}



	public String getFechaUltimaDosis4() {
		return fechaUltimaDosis4;
	}



	public void setFechaUltimaDosis4(String fechaUltimaDosis4) {
		this.fechaUltimaDosis4 = fechaUltimaDosis4;
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



	public int getIrritacion() {
		return irritacion;
	}



	public void setIrritacion(int irritacion) {
		this.irritacion = irritacion;
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



	public int getRash() {
		return rash;
	}



	public void setRash(int rash) {
		this.rash = rash;
	}



	public int getRigidez() {
		return rigidez;
	}



	public void setRigidez(int rigidez) {
		this.rigidez = rigidez;
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



	public int getTieneCarne() {
		return tieneCarne;
	}



	public void setTieneCarne(int tieneCarne) {
		this.tieneCarne = tieneCarne;
	}



	public int getTieneCarne2() {
		return tieneCarne2;
	}



	public void setTieneCarne2(int tieneCarne2) {
		this.tieneCarne2 = tieneCarne2;
	}



	public int getTieneCarne3() {
		return tieneCarne3;
	}



	public void setTieneCarne3(int tieneCarne3) {
		this.tieneCarne3 = tieneCarne3;
	}



	public int getTipoCaso() {
		return tipoCaso;
	}



	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}



	public int getUsoAntibioticos() {
		return usoAntibioticos;
	}



	public void setUsoAntibioticos(int usoAntibioticos) {
		this.usoAntibioticos = usoAntibioticos;
	}



	public int getVacunaAntihib() {
		return vacunaAntihib;
	}



	public void setVacunaAntihib(int vacunaAntihib) {
		this.vacunaAntihib = vacunaAntihib;
	}



	public int getVacunaAntimenin() {
		return vacunaAntimenin;
	}



	public void setVacunaAntimenin(int vacunaAntimenin) {
		this.vacunaAntimenin = vacunaAntimenin;
	}



	public int getVacunaAntineumo() {
		return vacunaAntineumo;
	}



	public void setVacunaAntineumo(int vacunaAntineumo) {
		this.vacunaAntineumo = vacunaAntineumo;
	}



	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}



	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
	}
    
}
