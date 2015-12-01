package com.sysmedica.mundo.saludpublica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.FichaInfeccionesDao;

public class FichaInfecciones {

	private Logger logger = Logger.getLogger(FichaInfecciones.class);
	
	private boolean activa;
	
	FichaInfeccionesDao fichaInfeccionesDao;
	
	//*************************************
    // DATOS CARA A :
    
    // Informacion general
    private String sire;
    private String municipioNotifica;
    private String departamentoNotifica;
    private String lugarNotifica;
    private String fechaNotificacion;
    
    // Informacion del paciente
    private int codigoConvenio;
    
    // Notificacion
    private String fechaConsultaGeneral;
    private String codigoMunProcedencia;
    private String codigoDepProcedencia;
    private String lugarProcedencia;
    private String fechaInicioSint;
    private String nombreProfesional;
	
	
	private int institucionAtendio;
    private int tipoCaso;
    private boolean hospitalizado;
    private String fechaHospitalizacion;
    private boolean estaVivo;
    private String fechaDefuncion;
    
    private int codigoFichaInfecciones;
    private int codigoFichaPaciente;
    private String codigoDiagnostico;
    private int numeroSolicitud;
    private String fechaDiligenciamiento;
    private String horaDiligenciamiento;
    
    private int estadoFicha;
    private String loginUsuario;
    private Paciente paciente;
    
    private int codigoPaciente;
    private int codigoNotificacion;
    private boolean notificar;
    
    public static int codigoGlobal;
    
    
    //*************************************
    // DATOS CARA B :
    
    private int numeroCama;
    private int servicio;
    private String fechaIngreso;
    private String fechaDxIh;
    private String fechaEgreso;
    private String fechaDxHisto;
    private String dxHospital;
    private String dxIh;
    private String dxEgreso;
    private String dxHisto;
    private int generoMicro;
    private String especieMicro;
    private String bioTipoMicro;
    private int tipoMuestra1;
    private int locAnatomica1;
    private String fechaToma1;
    private String fechaRemision1;
    private String medIdentificacion1;
    private String pruAdicionales1;
    private int tipoMuestra2;
    private int locAnatomica2;
    private String fechaToma2;
    private String fechaRemision2;
    private String medIdentificacion2;
    private String pruAdicionales2;
    private int antibiotico1;
    private String sensibilidad1;
    private String teDosis1;
    private String fechaInicioAntibiotico1;
    private String fechaFinAntibiotico1;
    private int antibiotico2;
    private String sensibilidad2;
    private String teDosis2;
    private String fechaInicioAntibiotico2;
    private String fechaFinAntibiotico2;
    private int antibiotico3;
    private String sensibilidad3;
    private String teDosis3;
    private String fechaInicioAntibiotico3;
    private String fechaFinAntibiotico3;
    private int clasificacionCaso;
    private int localizacionAnatomica;
    private String medicosTratantes;
    private String servicio2;
    
    private HashMap microorganismos;
    
    private HashMap factoresRiesgo;
    
    private int estadoAnterior;
    
    
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
			fichaInfeccionesDao = myFactory.getFichaInfeccionesDao();
			wasInited = (fichaInfeccionesDao != null);
		}
		return wasInited;
	}
    
    public void reset()
    {
    	sire="";
    	institucionAtendio = 0;
    	fechaInicioSint = "";
    	tipoCaso = 0;
    	hospitalizado = false;
    	fechaHospitalizacion = "";
    	estaVivo = false;
    	fechaDefuncion = "";
    	paciente = new Paciente();
    	
    	
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
    	
    	numeroCama = 0;
        servicio = 0;
        fechaIngreso = "";
        fechaDxIh = "";
        fechaEgreso = "";
        fechaDxHisto = "";
        dxHospital = "";
        dxIh = "";
        dxEgreso = "";
        dxHisto = "";
        generoMicro = 0;
        especieMicro = "";
        bioTipoMicro = "";
        tipoMuestra1 = 0;
        locAnatomica1 = 0;
        fechaToma1 = "";
        fechaRemision1 = "";
        medIdentificacion1 = "";
        pruAdicionales1 = "";
        tipoMuestra2 = 0;
        locAnatomica2 = 0;
        fechaToma2 = "";
        fechaRemision2 = "";
        medIdentificacion2 = "";
        pruAdicionales2 = "";
        antibiotico1 = 0;
        sensibilidad1 = "";
        teDosis1 = "";
        fechaInicioAntibiotico1 = "";
        fechaFinAntibiotico1 = "";
        antibiotico2 = 0;
        sensibilidad2 = "";
        teDosis2 = "";
        fechaInicioAntibiotico2 = "";
        fechaFinAntibiotico2 = "";
        antibiotico3 = 0;
        sensibilidad3 = "";
        teDosis3 = "";
        fechaInicioAntibiotico3 = "";
        fechaFinAntibiotico3 = "";
        clasificacionCaso = 0;
        localizacionAnatomica = 0;
        medicosTratantes = "";
        servicio2 = "";
        
        factoresRiesgo = new HashMap();
    }
    
    
    
    public FichaInfecciones() {
    	
    	reset();
        init(System.getProperty("TIPOBD"));
    }
    
    
    
    public int insertarFicha(Connection con) {
    	
    	return fichaInfeccionesDao.insertarFicha(con,
											notificar,
										    loginUsuario,
										    codigoFichaInfecciones,
										    codigoPaciente,
										    codigoDiagnostico,
										    codigoNotificacion,
										    numeroSolicitud,
										    estadoFicha,
										    codigoConvenio,
											nombreProfesional);
    }
    
    
    
    public void cargarDatos(Connection con, int codigo) {
    	
    	this.codigoFichaInfecciones = codigo;
    	
    	ResultSet rs = fichaInfeccionesDao.consultaTodoFicha(con,codigo);
    	
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
			    paciente.setDesplazado(rs.getInt("desplazado"));
			    paciente.setTipoId(rs.getString("tipoId"));
			    paciente.setPaisExpedicion(rs.getString("codigo_pais_id"));
			    paciente.setPaisNacimiento(rs.getString("codigo_pais_nacimiento"));
			    paciente.setPaisResidencia(rs.getString("codigo_pais_vivienda"));
			    
    			this.setEstadoFicha(rs.getInt("estado"));
    			this.setNumeroCama(rs.getInt("numeroCama"));
    			
    			this.setServicio(rs.getInt("servicio"));
    			this.setFechaIngreso(rs.getString("fechaIngreso"));
    			this.setFechaDxIh(rs.getString("fechaDxIh"));
    			this.setFechaEgreso(rs.getString("fechaEgreso"));
    			this.setFechaDxHisto(rs.getString("fechaDxHisto"));
    			this.setDxHospital(rs.getString("dxHospital"));
    			this.setDxIh(rs.getString("dxIh"));
    			
    			this.setDxEgreso(rs.getString("dxEgreso"));
    			this.setDxHisto(rs.getString("dxHisto"));
    			this.setGeneroMicro(rs.getInt("generoMicro"));
    			this.setEspecieMicro(rs.getString("especieMicro"));
    			this.setBioTipoMicro(rs.getString("bioTipoMicro"));
    			this.setTipoMuestra1(rs.getInt("tipoMuestra1"));
    			this.setLocAnatomica1(rs.getInt("locAnatomica1"));
    			this.setFechaToma1(rs.getString("fechaToma1"));
    			this.setFechaRemision1(rs.getString("fechaRemision1"));
    			this.setMedIdentificacion1(rs.getString("mIdentificacion1"));
    			this.setPruAdicionales1(rs.getString("pruAdicionales1"));
    			this.setTipoMuestra2(rs.getInt("tipoMuestra2"));
    			this.setLocAnatomica2(rs.getInt("locAnatomica2"));
    			this.setFechaToma2(rs.getString("fechaToma2"));
    			
    			this.setFechaRemision2(rs.getString("fechaRemision2"));
    			this.setMedIdentificacion2(rs.getString("mIdentificacion2"));
    			this.setPruAdicionales2(rs.getString("pruAdicionales2"));
    			this.setAntibiotico1(rs.getInt("antibiotico1"));
    			this.setSensibilidad1(rs.getString("sensibilidad1"));
    			this.setTeDosis1(rs.getString("tDosis1"));
    			this.setFechaInicioAntibiotico1(rs.getString("fechaInicioAntibiotico1"));
    			this.setFechaFinAntibiotico1(rs.getString("fechaFinAntibiotico1"));
    			this.setAntibiotico2(rs.getInt("antibiotico2"));
    			this.setSensibilidad2(rs.getString("sensibilidad2"));
    			this.setTeDosis2(rs.getString("tDosis2"));
    			this.setFechaInicioAntibiotico2(rs.getString("fechaInicioAntibiotico2"));
    			this.setFechaFinAntibiotico2(rs.getString("fechaFinAntibiotico2"));
    			this.setAntibiotico3(rs.getInt("antibiotico3"));
    			this.setSensibilidad3(rs.getString("sensibilidad3"));
    			this.setTeDosis3(rs.getString("tDosis3"));
    			this.setFechaInicioAntibiotico3(rs.getString("fechaInicioAntibiotico3"));
    			this.setFechaFinAntibiotico3(rs.getString("fechaFinAntibiotico3"));
    			this.setClasificacionCaso(rs.getInt("clasificacionCaso"));
    			this.setFechaNotificacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaDiligenciamiento")));
    			this.setNombreProfesional(rs.getString("nombreProfesional"));
    			this.setInstitucionAtendio(rs.getInt("nombreUnidad"));
    			this.setLocalizacionAnatomica(rs.getInt("localizacionAnatomica"));
    			this.setEstaVivo(rs.getBoolean("estaVivo"));
    			this.setMedicosTratantes(rs.getString("medicos"));
    			this.setServicio2(rs.getString("servicio2"));
    			
    			
    			/*
    			ResultSet rs2 = fichaInfeccionesDao.consultarFactoresRiesgo(con,codigo);
			    
			    HashMap factoresRiesgo = new HashMap();
			    
			    while (rs2.next()) {
			    	
			    	factoresRiesgo.put("factor_"+rs2.getInt("codigofactor"),"true");
			    }
			    
			    this.setFactoresRiesgo(factoresRiesgo);
			    */
			    
    			ResultSet rs2 = fichaInfeccionesDao.consultarFactoresRiesgo(con, codigo);
			    
			    HashMap factoresRiesgo = new HashMap();
			    
			    int j=0;
			    
			    while (rs2.next()) {
			    	
			    	factoresRiesgo.put("factor_"+Integer.toString(j), rs2.getString("factor"));
			    	factoresRiesgo.put("clasificacion_"+Integer.toString(j), rs2.getString("clasificacion"));
			    				    	
			    	j++;
			    }
			    
			    this.setFactoresRiesgo(factoresRiesgo);
    			
    			
    			
			    
			    ResultSet rs3 = fichaInfeccionesDao.consultarMicros(con, codigo);
			    
			    HashMap micros = new HashMap();
			    
			    int i=1;
			    
			    while (rs3.next()) {
			    	
			    	micros.put("micro_"+Integer.toString(i), rs3.getString("codigomicro"));
			    	micros.put("especie_"+Integer.toString(i), rs3.getString("especie"));
			    	micros.put("biotipo_"+Integer.toString(i), rs3.getString("biotipo"));
			    	
			    	i++;
			    }
			    
			    this.setMicroorganismos(micros);
			    
			    
			    
    		}
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("Error cargando los datos de la ficha de infecciones : "+sqle.getMessage());
    	}
    }
    
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaInfeccionesDao.modificarFicha(con,
													sire,
													notificar,
												    loginUsuario,
												    codigoFichaInfecciones,
												    codigoPaciente,
												    codigoDiagnostico,
												    codigoNotificacion,
												    numeroSolicitud,
												    estadoFicha,
												    
												    numeroCama,
												    servicio,
												    fechaIngreso,
												    fechaDxIh,
												    fechaEgreso,
												    fechaDxHisto,
												    dxHospital,
												    dxIh,
												    dxEgreso,
												    dxHisto,
												    localizacionAnatomica,
												    generoMicro,
												    especieMicro,
												    bioTipoMicro,
												    tipoMuestra1,
												    locAnatomica1,
												    fechaToma1,
												    fechaRemision1,
												    medIdentificacion1,
												    pruAdicionales1,
												    tipoMuestra2,
												    locAnatomica2,
												    fechaToma2,
												    fechaRemision2,
												    medIdentificacion2,
												    pruAdicionales2,
												    antibiotico1,
												    sensibilidad1,
												    teDosis1,
												    fechaInicioAntibiotico1,
												    fechaFinAntibiotico1,
												    antibiotico2,
												    sensibilidad2,
												    teDosis2,
												    fechaInicioAntibiotico2,
												    fechaFinAntibiotico2,
												    antibiotico3,
												    sensibilidad3,
												    teDosis3,
												    fechaInicioAntibiotico3,
												    fechaFinAntibiotico3,
												    clasificacionCaso,
												    factoresRiesgo,
												    estadoAnterior,
												    institucionAtendio,
												    medicosTratantes,
												    servicio2,
												    microorganismos);
    }
    
    
    
    public int insertarFichaCompleta(Connection con)
    {
    	return fichaInfeccionesDao.insertarFichaCompleta(con,
															numeroSolicitud,
															loginUsuario,
															codigoPaciente,
															codigoDiagnostico,
															estadoFicha,
															codigoConvenio,
															nombreProfesional,
														    
														    numeroCama,
														    servicio,
														    fechaIngreso,
														    fechaDxIh,
														    fechaEgreso,
														    fechaDxHisto,
														    dxHospital,
														    dxIh,
														    dxEgreso,
														    dxHisto,
														    localizacionAnatomica,
														    generoMicro,
														    especieMicro,
														    bioTipoMicro,
														    tipoMuestra1,
														    locAnatomica1,
														    fechaToma1,
														    fechaRemision1,
														    medIdentificacion1,
														    pruAdicionales1,
														    tipoMuestra2,
														    locAnatomica2,
														    fechaToma2,
														    fechaRemision2,
														    medIdentificacion2,
														    pruAdicionales2,
														    antibiotico1,
														    sensibilidad1,
														    teDosis1,
														    fechaInicioAntibiotico1,
														    fechaFinAntibiotico1,
														    antibiotico2,
														    sensibilidad2,
														    teDosis2,
														    fechaInicioAntibiotico2,
														    fechaFinAntibiotico2,
														    antibiotico3,
														    sensibilidad3,
														    teDosis3,
														    fechaInicioAntibiotico3,
														    fechaFinAntibiotico3,
														    clasificacionCaso,
														    factoresRiesgo,
														    estadoAnterior,
														    institucionAtendio,
														    medicosTratantes,
														    activa,
														    servicio2,
														    microorganismos);
    }
    
    
    
    public void cargarPaciente(Connection con, int codigo, boolean empezarnuevo) {
    	
    	ResultSet rs = fichaInfeccionesDao.consultaDatosPaciente(con,codigo,empezarnuevo);
    	
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

    
    
	public static int getCodigoGlobal() {
		return codigoGlobal;
	}
	public static void setCodigoGlobal(int codigoGlobal) {
		FichaInfecciones.codigoGlobal = codigoGlobal;
	}
	public int getAntibiotico1() {
		return antibiotico1;
	}
	public void setAntibiotico1(int antibiotico1) {
		this.antibiotico1 = antibiotico1;
	}
	public int getAntibiotico2() {
		return antibiotico2;
	}
	public void setAntibiotico2(int antibiotico2) {
		this.antibiotico2 = antibiotico2;
	}
	public int getAntibiotico3() {
		return antibiotico3;
	}
	public void setAntibiotico3(int antibiotico3) {
		this.antibiotico3 = antibiotico3;
	}
	public String getBioTipoMicro() {
		return bioTipoMicro;
	}
	public void setBioTipoMicro(String bioTipoMicro) {
		this.bioTipoMicro = bioTipoMicro;
	}
	public int getClasificacionCaso() {
		return clasificacionCaso;
	}
	public void setClasificacionCaso(int clasificacionCaso) {
		this.clasificacionCaso = clasificacionCaso;
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
	public int getCodigoFichaInfecciones() {
		return codigoFichaInfecciones;
	}
	public void setCodigoFichaInfecciones(int codigoFichaInfecciones) {
		this.codigoFichaInfecciones = codigoFichaInfecciones;
	}
	public int getCodigoFichaPaciente() {
		return codigoFichaPaciente;
	}
	public void setCodigoFichaPaciente(int codigoFichaPaciente) {
		this.codigoFichaPaciente = codigoFichaPaciente;
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
	public String getDepartamentoNotifica() {
		return departamentoNotifica;
	}
	public void setDepartamentoNotifica(String departamentoNotifica) {
		this.departamentoNotifica = departamentoNotifica;
	}
	public String getDxEgreso() {
		return dxEgreso;
	}
	public void setDxEgreso(String dxEgreso) {
		this.dxEgreso = dxEgreso;
	}
	public String getDxHisto() {
		return dxHisto;
	}
	public void setDxHisto(String dxHisto) {
		this.dxHisto = dxHisto;
	}
	public String getDxHospital() {
		return dxHospital;
	}
	public void setDxHospital(String dxHospital) {
		this.dxHospital = dxHospital;
	}
	public String getDxIh() {
		return dxIh;
	}
	public void setDxIh(String dxIh) {
		this.dxIh = dxIh;
	}
	public String getEspecieMicro() {
		return especieMicro;
	}
	public void setEspecieMicro(String especieMicro) {
		this.especieMicro = especieMicro;
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
	public String getFechaDiligenciamiento() {
		return fechaDiligenciamiento;
	}
	public void setFechaDiligenciamiento(String fechaDiligenciamiento) {
		this.fechaDiligenciamiento = fechaDiligenciamiento;
	}
	public String getFechaDxHisto() {
		return fechaDxHisto;
	}
	public void setFechaDxHisto(String fechaDxHisto) {
		this.fechaDxHisto = fechaDxHisto;
	}
	public String getFechaDxIh() {
		return fechaDxIh;
	}
	public void setFechaDxIh(String fechaDxIh) {
		this.fechaDxIh = fechaDxIh;
	}
	public String getFechaEgreso() {
		return fechaEgreso;
	}
	public void setFechaEgreso(String fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}
	public String getFechaFinAntibiotico1() {
		return fechaFinAntibiotico1;
	}
	public void setFechaFinAntibiotico1(String fechaFinAntibiotico1) {
		this.fechaFinAntibiotico1 = fechaFinAntibiotico1;
	}
	public String getFechaFinAntibiotico2() {
		return fechaFinAntibiotico2;
	}
	public void setFechaFinAntibiotico2(String fechaFinAntibiotico2) {
		this.fechaFinAntibiotico2 = fechaFinAntibiotico2;
	}
	public String getFechaFinAntibiotico3() {
		return fechaFinAntibiotico3;
	}
	public void setFechaFinAntibiotico3(String fechaFinAntibiotico3) {
		this.fechaFinAntibiotico3 = fechaFinAntibiotico3;
	}
	public String getFechaHospitalizacion() {
		return fechaHospitalizacion;
	}
	public void setFechaHospitalizacion(String fechaHospitalizacion) {
		this.fechaHospitalizacion = fechaHospitalizacion;
	}
	public String getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public String getFechaInicioAntibiotico1() {
		return fechaInicioAntibiotico1;
	}
	public void setFechaInicioAntibiotico1(String fechaInicioAntibiotico1) {
		this.fechaInicioAntibiotico1 = fechaInicioAntibiotico1;
	}
	public String getFechaInicioAntibiotico2() {
		return fechaInicioAntibiotico2;
	}
	public void setFechaInicioAntibiotico2(String fechaInicioAntibiotico2) {
		this.fechaInicioAntibiotico2 = fechaInicioAntibiotico2;
	}
	public String getFechaInicioAntibiotico3() {
		return fechaInicioAntibiotico3;
	}
	public void setFechaInicioAntibiotico3(String fechaInicioAntibiotico3) {
		this.fechaInicioAntibiotico3 = fechaInicioAntibiotico3;
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
	public String getFechaRemision1() {
		return fechaRemision1;
	}
	public void setFechaRemision1(String fechaRemision1) {
		this.fechaRemision1 = fechaRemision1;
	}
	public String getFechaRemision2() {
		return fechaRemision2;
	}
	public void setFechaRemision2(String fechaRemision2) {
		this.fechaRemision2 = fechaRemision2;
	}
	public String getFechaToma1() {
		return fechaToma1;
	}
	public void setFechaToma1(String fechaToma1) {
		this.fechaToma1 = fechaToma1;
	}
	public String getFechaToma2() {
		return fechaToma2;
	}
	public void setFechaToma2(String fechaToma2) {
		this.fechaToma2 = fechaToma2;
	}
	public FichaInfeccionesDao getFichaInfeccionesDao() {
		return fichaInfeccionesDao;
	}
	public void setFichaInfeccionesDao(FichaInfeccionesDao fichaInfeccionesDao) {
		this.fichaInfeccionesDao = fichaInfeccionesDao;
	}
	public int getGeneroMicro() {
		return generoMicro;
	}
	public void setGeneroMicro(int generoMicro) {
		this.generoMicro = generoMicro;
	}
	public String getHoraDiligenciamiento() {
		return horaDiligenciamiento;
	}
	public void setHoraDiligenciamiento(String horaDiligenciamiento) {
		this.horaDiligenciamiento = horaDiligenciamiento;
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
	public int getLocAnatomica1() {
		return locAnatomica1;
	}
	public void setLocAnatomica1(int locAnatomica1) {
		this.locAnatomica1 = locAnatomica1;
	}
	public int getLocAnatomica2() {
		return locAnatomica2;
	}
	public void setLocAnatomica2(int locAnatomica2) {
		this.locAnatomica2 = locAnatomica2;
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
	public String getMedIdentificacion1() {
		return medIdentificacion1;
	}
	public void setMedIdentificacion1(String identificacion1) {
		medIdentificacion1 = identificacion1;
	}
	public String getMedIdentificacion2() {
		return medIdentificacion2;
	}
	public void setMedIdentificacion2(String identificacion2) {
		medIdentificacion2 = identificacion2;
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
	public int getNumeroCama() {
		return numeroCama;
	}
	public void setNumeroCama(int numeroCama) {
		this.numeroCama = numeroCama;
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
	public String getPruAdicionales1() {
		return pruAdicionales1;
	}
	public void setPruAdicionales1(String pruAdicionales1) {
		this.pruAdicionales1 = pruAdicionales1;
	}
	public String getPruAdicionales2() {
		return pruAdicionales2;
	}
	public void setPruAdicionales2(String pruAdicionales2) {
		this.pruAdicionales2 = pruAdicionales2;
	}
	public String getSensibilidad1() {
		return sensibilidad1;
	}
	public void setSensibilidad1(String sensibilidad1) {
		this.sensibilidad1 = sensibilidad1;
	}
	public String getSensibilidad2() {
		return sensibilidad2;
	}
	public void setSensibilidad2(String sensibilidad2) {
		this.sensibilidad2 = sensibilidad2;
	}
	public String getSensibilidad3() {
		return sensibilidad3;
	}
	public void setSensibilidad3(String sensibilidad3) {
		this.sensibilidad3 = sensibilidad3;
	}
	public int getServicio() {
		return servicio;
	}
	public void setServicio(int servicio) {
		this.servicio = servicio;
	}
	public String getSire() {
		return sire;
	}
	public void setSire(String sire) {
		this.sire = sire;
	}
	public String getTeDosis1() {
		return teDosis1;
	}
	public void setTeDosis1(String dosis1) {
		teDosis1 = dosis1;
	}
	public String getTeDosis2() {
		return teDosis2;
	}
	public void setTeDosis2(String dosis2) {
		teDosis2 = dosis2;
	}
	public String getTeDosis3() {
		return teDosis3;
	}
	public void setTeDosis3(String dosis3) {
		teDosis3 = dosis3;
	}
	public int getTipoCaso() {
		return tipoCaso;
	}
	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}
	public int getTipoMuestra1() {
		return tipoMuestra1;
	}
	public void setTipoMuestra1(int tipoMuestra1) {
		this.tipoMuestra1 = tipoMuestra1;
	}
	public int getTipoMuestra2() {
		return tipoMuestra2;
	}
	public void setTipoMuestra2(int tipoMuestra2) {
		this.tipoMuestra2 = tipoMuestra2;
	}

	public HashMap getFactoresRiesgo() {
		return factoresRiesgo;
	}

	public void setFactoresRiesgo(HashMap factoresRiesgo) {
		this.factoresRiesgo = factoresRiesgo;
	}

	public int getEstadoAnterior() {
		return estadoAnterior;
	}

	public void setEstadoAnterior(int estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}

	public int getLocalizacionAnatomica() {
		return localizacionAnatomica;
	}

	public void setLocalizacionAnatomica(int localizacionAnatomica) {
		this.localizacionAnatomica = localizacionAnatomica;
	}

	public String getMedicosTratantes() {
		return medicosTratantes;
	}

	public void setMedicosTratantes(String medicosTratantes) {
		this.medicosTratantes = medicosTratantes;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public String getServicio2() {
		return servicio2;
	}

	public void setServicio2(String servicio2) {
		this.servicio2 = servicio2;
	}

	public HashMap getMicroorganismos() {
		return microorganismos;
	}

	public void setMicroorganismos(HashMap microorganismos) {
		this.microorganismos = microorganismos;
	}
}
