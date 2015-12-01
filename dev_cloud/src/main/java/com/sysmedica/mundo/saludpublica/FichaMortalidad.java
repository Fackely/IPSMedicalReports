package com.sysmedica.mundo.saludpublica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.mundo.saludpublica.Paciente;
import com.sysmedica.dao.FichaMortalidadDao;

public class FichaMortalidad {

	FichaMortalidadDao fichaMortalidadDao;
	
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
    
    private int codigoFichaMortalidad;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
    private int sitioDefuncion;
	private String descripcionSitio;
	private int convivencia;
	private String otroConvivencia;
	private int escolaridad;
	private int fecundidad;
	private String gestaciones;
	private String partos;
	private String cesareas;
	private String abortos;
	private String sustanciasSico;
	private String trastornoMental;
	private String infecciones;
	private String factoresRiesgo;
	private int controlPrenatal;
	private String cuantosControles;
	private int trimInicio;
	private int controlesRealizadosPor;
	private int nivelAtencion;
	private int clasificacionRiesgo;
	private int remisionesOportunas;
	private HashMap complicaciones;
	private int momentoFallecimiento;
	private String semanasGestacion;
	private int tipoParto;
	private int atendidoPor;
	private int nivelAtencion2;
	private int momentoMuerteRelacion;
	private String edadGestacional;
	private String pesoNacimiento;
	private String tallaNacimiento;
	private int sexo;
	private String apgarNacimiento1;
	private String apgarNacimiento5;
	private String apgarNacimiento15;
	private int nivelAtencion3;
	private int adaptacionNeonatal;
	private String causaDirectaDefuncion;
	private String causaBasicaDefuncion;
	private int muerteDemora;
	private int causaMuerteDet;
	private String complicacionesAntecedentes;
	private String muertos;
	private String vivos;
	private int semanaInicioCpn;
	private int quienClasificoRiesgo;
	private int remisionOportunaComplica;
	private String muerteDemora1;
	private String muerteDemora2;
	private String muerteDemora3;
	private String muerteDemora4;
	private HashMap antecedentes;
	private String pais;
    private int areaProcedencia;
	
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
				fichaMortalidadDao = myFactory.getFichaMortalidadDao();
				wasInited = (fichaMortalidadDao != null);
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
        
        sitioDefuncion = 0;
    	descripcionSitio = "";
    	convivencia = 0;
    	otroConvivencia = "";
    	escolaridad = 0;
    	fecundidad = 0;
    	gestaciones = "";
    	partos = "";
    	cesareas = "";
    	abortos = "";
    	sustanciasSico = "";
    	trastornoMental = "";
    	infecciones = "";
    	factoresRiesgo = "";
    	controlPrenatal = 0;
    	cuantosControles = "";
    	trimInicio = 0;
    	controlesRealizadosPor = 0;
    	nivelAtencion = 0;
    	clasificacionRiesgo = 0;
    	remisionesOportunas = 0;
    	complicaciones = new HashMap();
    	momentoFallecimiento = 0;
    	semanasGestacion = "";
    	tipoParto = 0;
    	atendidoPor = 0;
    	nivelAtencion2 = 0;
    	momentoMuerteRelacion = 0;
    	edadGestacional = "";
    	pesoNacimiento = "";
    	tallaNacimiento = "";
    	sexo = 0;
    	apgarNacimiento1 = "";
    	apgarNacimiento5 = "";
    	apgarNacimiento15 = "";
    	nivelAtencion3 = 0;
    	adaptacionNeonatal = 0;
    	causaDirectaDefuncion = "";
    	causaBasicaDefuncion = "";
    	muerteDemora = 0;
    	causaMuerteDet = 0;
    	paciente = new Paciente();
    }
    
    
    public int insertarFicha(Connection con) 
    {
    	return fichaMortalidadDao.insertarFicha(con,
												notificar,
											    loginUsuario,
											    codigoFichaMortalidad,
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
    	return fichaMortalidadDao.modificarFicha(con,
												sire,
												loginUsuario,
											    codigoFichaMortalidad,
											    estadoFicha,
											    
											    sitioDefuncion,
												descripcionSitio,
												convivencia,
												otroConvivencia,
												escolaridad,
												fecundidad,
												gestaciones,
												partos,
												cesareas,
												abortos,
												sustanciasSico,
												trastornoMental,
												infecciones,
												factoresRiesgo,
												controlPrenatal,
												cuantosControles,
												trimInicio,
												controlesRealizadosPor,
												nivelAtencion,
												clasificacionRiesgo,
												remisionesOportunas,
												complicaciones,
												momentoFallecimiento,
												semanasGestacion,
												tipoParto,
												atendidoPor,
												nivelAtencion2,
												momentoMuerteRelacion,
												edadGestacional,
												pesoNacimiento,
												tallaNacimiento,
												sexo,
												apgarNacimiento1,
												apgarNacimiento5,
												apgarNacimiento15,
												nivelAtencion3,
												adaptacionNeonatal,
												causaDirectaDefuncion,
												causaBasicaDefuncion,
												muerteDemora,
												causaMuerteDet,
											    
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
											    complicacionesAntecedentes,
											    muertos,
											    vivos,
											    semanaInicioCpn,
											    quienClasificoRiesgo,
											    remisionOportunaComplica,
											    muerteDemora1,
											    muerteDemora2,
											    muerteDemora3,
											    muerteDemora4,
											    antecedentes,
											    pais,
											    areaProcedencia);
    }
    
    
    public void cargarDatos(Connection con, int codigo) 
    {
    	this.codigoFichaMortalidad = codigo;
    	
    	ResultSet rs = fichaMortalidadDao.consultaTodoFicha(con,codigo);
    	
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
			    paciente.setGenero(rs.getString("sexoPaciente"));
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
			    this.setSitioDefuncion(rs.getInt("sitioDefuncion"));
			    this.setDescripcionSitio(rs.getString("descripcionSitio"));
			    this.setConvivencia(rs.getInt("convivencia"));
			    this.setOtroConvivencia(rs.getString("otroConvivencia"));
			    this.setEscolaridad(rs.getInt("escolaridad"));
			    this.setFecundidad(rs.getInt("fecundidad"));
			    this.setGestaciones(rs.getString("gestaciones"));
			    this.setPartos(rs.getString("partos"));
			    this.setCesareas(rs.getString("cesareas"));
			    this.setAbortos(rs.getString("abortos"));
			    this.setSustanciasSico(rs.getString("sustanciasSico"));
			    this.setTrastornoMental(rs.getString("trastornoMental"));
			    this.setInfecciones(rs.getString("infecciones"));
			    this.setFactoresRiesgo(rs.getString("factoresRiesgo"));
			    this.setControlPrenatal(rs.getInt("controlPrenatal"));
			    this.setCuantosControles(rs.getString("cuantosControles"));
			    this.setTrimInicio(rs.getInt("trimInicio"));
			    this.setControlesRealizadosPor(rs.getInt("controlesRealizadosPor"));
			    this.setNivelAtencion(rs.getInt("nivelAtencion"));
			    this.setClasificacionRiesgo(rs.getInt("clasificacionRiesgo"));
			    this.setRemisionesOportunas(rs.getInt("remisionesOportunas"));
			    this.setComplicacionesAntecedentes(rs.getString("complicaciones"));
			//    this.setComplicaciones(rs.getString("complicaciones"));
			    this.setMomentoFallecimiento(rs.getInt("momentoFallecimiento"));
			    this.setSemanasGestacion(rs.getString("semanasGestacion"));
			    this.setTipoParto(rs.getInt("tipoParto"));
			    this.setAtendidoPor(rs.getInt("atendidoPor"));
			    this.setNivelAtencion2(rs.getInt("nivelAtencion2"));
			    this.setMomentoMuerteRelacion(rs.getInt("momentoMuerteRelacion"));
			    this.setEdadGestacional(rs.getString("edadGestacional"));
			    this.setPesoNacimiento(rs.getString("pesoNacimiento"));
			    this.setTallaNacimiento(rs.getString("tallaNacimiento"));
			    this.setSexo(rs.getInt("sexo"));
			    this.setApgarNacimiento1(rs.getString("apgarNacimiento1"));
			    this.setApgarNacimiento5(rs.getString("apgarNacimiento5"));
			    this.setApgarNacimiento15(rs.getString("apgarNacimiento15"));
			    this.setNivelAtencion3(rs.getInt("nivelAtencion3"));
			    this.setAdaptacionNeonatal(rs.getInt("adaptacionNeonatal"));
			    this.setCausaDirectaDefuncion(rs.getString("causaDirectaDefuncion"));
			    this.setCausaBasicaDefuncion(rs.getString("causaBasicaDefuncion"));
			    this.setMuerteDemora(rs.getInt("muerteDemora"));
			    this.setCausaMuerteDet(rs.getInt("causaMuerteDet"));
			    
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
			    this.setMuertos(rs.getString("muertos"));
			    this.setVivos(rs.getString("vivos"));
			    this.setSemanaInicioCpn(rs.getInt("semanaInicioCpn"));
			    this.setQuienClasificoRiesgo(rs.getInt("quienClasificoRiesgo"));
			    this.setRemisionOportunaComplica(rs.getInt("remisionOportunaComplica"));
			    this.setMuerteDemora1(rs.getString("muerteDemora1"));
			    this.setMuerteDemora2(rs.getString("muerteDemora2"));
			    this.setMuerteDemora3(rs.getString("muerteDemora3"));
			    this.setMuerteDemora4(rs.getString("muerteDemora4"));
			    this.setPais(rs.getString("pais"));
			    this.setAreaProcedencia(rs.getInt("areaProcedencia"));
			    
			    ResultSet rs2 = fichaMortalidadDao.consultarComplicaciones(con,codigo);
			    
			    HashMap comp = new HashMap();
			    
			    while (rs2.next()) {
			    	
			    	comp.put("complicacion_"+rs2.getInt("codigocomplicacion"),"true");
			    }
			    
			    
			    ResultSet rs3 = fichaMortalidadDao.consultarAntecedentes(con,codigo);
			    
			    HashMap ante = new HashMap();
			    
			    while (rs3.next()) {
			    	
			    	ante.put("antecedente_"+rs3.getInt("codigoantecedente"),"true");
			    }
			    
			    this.setComplicaciones(comp);
    		}
    	}
    	catch (SQLException sqle) {
    		
    		sqle.printStackTrace();
    	}
    }
    
    
    public FichaMortalidad()
    {
    	reset();
		init(System.getProperty("TIPOBD"));
    }
    
    
    
    
    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaMortalidadDao.consultaDatosPaciente(con,codigo,empezarnuevo);
    	
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
    	return fichaMortalidadDao.insertarFichaCompleta(con,
															numeroSolicitud,
															loginUsuario,
															codigoPaciente,
															codigoDiagnostico,
															estadoFicha,
															codigoConvenio,
															nombreProfesional,
														    
														    sire,
														    sitioDefuncion,
															descripcionSitio,
															convivencia,
															otroConvivencia,
															escolaridad,
															fecundidad,
															gestaciones,
															partos,
															cesareas,
															abortos,
															sustanciasSico,
															trastornoMental,
															infecciones,
															factoresRiesgo,
															controlPrenatal,
															cuantosControles,
															trimInicio,
															controlesRealizadosPor,
															nivelAtencion,
															clasificacionRiesgo,
															remisionesOportunas,
															complicaciones,
															complicacionesAntecedentes,
															momentoFallecimiento,
															semanasGestacion,
															tipoParto,
															atendidoPor,
															nivelAtencion2,
															momentoMuerteRelacion,
															edadGestacional,
															pesoNacimiento,
															tallaNacimiento,
															sexo,
															apgarNacimiento1,
															apgarNacimiento5,
															apgarNacimiento15,
															nivelAtencion3,
															adaptacionNeonatal,
															causaDirectaDefuncion,
															causaBasicaDefuncion,
															muerteDemora,
															causaMuerteDet,
														    
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
														    muertos,
														    vivos,
														    semanaInicioCpn,
														    quienClasificoRiesgo,
														    remisionOportunaComplica,
														    muerteDemora1,
														    muerteDemora2,
														    muerteDemora3,
														    muerteDemora4,
														    antecedentes,
														    pais,
														    areaProcedencia);
    }


	public String getAbortos() {
		return abortos;
	}


	public void setAbortos(String abortos) {
		this.abortos = abortos;
	}


	public int getAdaptacionNeonatal() {
		return adaptacionNeonatal;
	}


	public void setAdaptacionNeonatal(int adaptacionNeonatal) {
		this.adaptacionNeonatal = adaptacionNeonatal;
	}


	public String getApgarNacimiento1() {
		return apgarNacimiento1;
	}


	public void setApgarNacimiento1(String apgarNacimiento1) {
		this.apgarNacimiento1 = apgarNacimiento1;
	}


	public String getApgarNacimiento15() {
		return apgarNacimiento15;
	}


	public void setApgarNacimiento15(String apgarNacimiento15) {
		this.apgarNacimiento15 = apgarNacimiento15;
	}


	public String getApgarNacimiento5() {
		return apgarNacimiento5;
	}


	public void setApgarNacimiento5(String apgarNacimiento5) {
		this.apgarNacimiento5 = apgarNacimiento5;
	}


	public int getAtendidoPor() {
		return atendidoPor;
	}


	public void setAtendidoPor(int atendidoPor) {
		this.atendidoPor = atendidoPor;
	}


	public String getCausaBasicaDefuncion() {
		return causaBasicaDefuncion;
	}


	public void setCausaBasicaDefuncion(String causaBasicaDefuncion) {
		this.causaBasicaDefuncion = causaBasicaDefuncion;
	}


	public String getCausaDirectaDefuncion() {
		return causaDirectaDefuncion;
	}


	public void setCausaDirectaDefuncion(String causaDirectaDefuncion) {
		this.causaDirectaDefuncion = causaDirectaDefuncion;
	}


	public int getCausaMuerteDet() {
		return causaMuerteDet;
	}


	public void setCausaMuerteDet(int causaMuerteDet) {
		this.causaMuerteDet = causaMuerteDet;
	}


	public String getCesareas() {
		return cesareas;
	}


	public void setCesareas(String cesareas) {
		this.cesareas = cesareas;
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


	public int getClasificacionRiesgo() {
		return clasificacionRiesgo;
	}


	public void setClasificacionRiesgo(int clasificacionRiesgo) {
		this.clasificacionRiesgo = clasificacionRiesgo;
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


	public HashMap getComplicaciones() {
		return complicaciones;
	}


	public void setComplicaciones(HashMap complicaciones) {
		this.complicaciones = complicaciones;
	}


	public int getControlesRealizadosPor() {
		return controlesRealizadosPor;
	}


	public void setControlesRealizadosPor(int controlesRealizadosPor) {
		this.controlesRealizadosPor = controlesRealizadosPor;
	}


	public int getControlPrenatal() {
		return controlPrenatal;
	}


	public void setControlPrenatal(int controlPrenatal) {
		this.controlPrenatal = controlPrenatal;
	}


	public int getConvivencia() {
		return convivencia;
	}


	public void setConvivencia(int convivencia) {
		this.convivencia = convivencia;
	}


	public String getCuantosControles() {
		return cuantosControles;
	}


	public void setCuantosControles(String cuantosControles) {
		this.cuantosControles = cuantosControles;
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


	public String getDescripcionSitio() {
		return descripcionSitio;
	}


	public void setDescripcionSitio(String descripcionSitio) {
		this.descripcionSitio = descripcionSitio;
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


	public String getEdadGestacional() {
		return edadGestacional;
	}


	public void setEdadGestacional(String edadGestacional) {
		this.edadGestacional = edadGestacional;
	}


	public int getEscolaridad() {
		return escolaridad;
	}


	public void setEscolaridad(int escolaridad) {
		this.escolaridad = escolaridad;
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


	public String getFactoresRiesgo() {
		return factoresRiesgo;
	}


	public void setFactoresRiesgo(String factoresRiesgo) {
		this.factoresRiesgo = factoresRiesgo;
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


	public int getFecundidad() {
		return fecundidad;
	}


	public void setFecundidad(int fecundidad) {
		this.fecundidad = fecundidad;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public String getGestaciones() {
		return gestaciones;
	}


	public void setGestaciones(String gestaciones) {
		this.gestaciones = gestaciones;
	}


	public boolean isHospitalizado() {
		return hospitalizado;
	}


	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}


	public String getInfecciones() {
		return infecciones;
	}


	public void setInfecciones(String infecciones) {
		this.infecciones = infecciones;
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


	public int getMomentoFallecimiento() {
		return momentoFallecimiento;
	}


	public void setMomentoFallecimiento(int momentoFallecimiento) {
		this.momentoFallecimiento = momentoFallecimiento;
	}


	public int getMomentoMuerteRelacion() {
		return momentoMuerteRelacion;
	}


	public void setMomentoMuerteRelacion(int momentoMuerteRelacion) {
		this.momentoMuerteRelacion = momentoMuerteRelacion;
	}


	public int getMuerteDemora() {
		return muerteDemora;
	}


	public void setMuerteDemora(int muerteDemora) {
		this.muerteDemora = muerteDemora;
	}


	public String getMunicipioNotifica() {
		return municipioNotifica;
	}


	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}


	public int getNivelAtencion() {
		return nivelAtencion;
	}


	public void setNivelAtencion(int nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}


	public int getNivelAtencion2() {
		return nivelAtencion2;
	}


	public void setNivelAtencion2(int nivelAtencion2) {
		this.nivelAtencion2 = nivelAtencion2;
	}


	public int getNivelAtencion3() {
		return nivelAtencion3;
	}


	public void setNivelAtencion3(int nivelAtencion3) {
		this.nivelAtencion3 = nivelAtencion3;
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


	public String getOtroConvivencia() {
		return otroConvivencia;
	}


	public void setOtroConvivencia(String otroConvivencia) {
		this.otroConvivencia = otroConvivencia;
	}


	public Paciente getPaciente() {
		return paciente;
	}


	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}


	public String getPartos() {
		return partos;
	}


	public void setPartos(String partos) {
		this.partos = partos;
	}


	public String getPesoNacimiento() {
		return pesoNacimiento;
	}


	public void setPesoNacimiento(String pesoNacimiento) {
		this.pesoNacimiento = pesoNacimiento;
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


	public int getRemisionesOportunas() {
		return remisionesOportunas;
	}


	public void setRemisionesOportunas(int remisionesOportunas) {
		this.remisionesOportunas = remisionesOportunas;
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


	public String getSemanasGestacion() {
		return semanasGestacion;
	}


	public void setSemanasGestacion(String semanasGestacion) {
		this.semanasGestacion = semanasGestacion;
	}


	public int getSexo() {
		return sexo;
	}


	public void setSexo(int sexo) {
		this.sexo = sexo;
	}


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
	}


	public int getSitioDefuncion() {
		return sitioDefuncion;
	}


	public void setSitioDefuncion(int sitioDefuncion) {
		this.sitioDefuncion = sitioDefuncion;
	}


	public String getSustanciasSico() {
		return sustanciasSico;
	}


	public void setSustanciasSico(String sustanciasSico) {
		this.sustanciasSico = sustanciasSico;
	}


	public String getTallaNacimiento() {
		return tallaNacimiento;
	}


	public void setTallaNacimiento(String tallaNacimiento) {
		this.tallaNacimiento = tallaNacimiento;
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


	public int getTipoParto() {
		return tipoParto;
	}


	public void setTipoParto(int tipoParto) {
		this.tipoParto = tipoParto;
	}


	public String getTrastornoMental() {
		return trastornoMental;
	}


	public void setTrastornoMental(String trastornoMental) {
		this.trastornoMental = trastornoMental;
	}


	public int getTrimInicio() {
		return trimInicio;
	}


	public void setTrimInicio(int trimInicio) {
		this.trimInicio = trimInicio;
	}


	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}


	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}


	public int getCodigoFichaMortalidad() {
		return codigoFichaMortalidad;
	}


	public void setCodigoFichaMortalidad(int codigoFichaMortalidad) {
		this.codigoFichaMortalidad = codigoFichaMortalidad;
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


	public boolean isActiva() {
		return activa;
	}


	public void setActiva(boolean activa) {
		this.activa = activa;
	}


	public String getComplicacionesAntecedentes() {
		return complicacionesAntecedentes;
	}


	public void setComplicacionesAntecedentes(String complicacionesAntecedentes) {
		this.complicacionesAntecedentes = complicacionesAntecedentes;
	}


	public HashMap getAntecedentes() {
		return antecedentes;
	}


	public void setAntecedentes(HashMap antecedentes) {
		this.antecedentes = antecedentes;
	}


	public String getMuerteDemora1() {
		return muerteDemora1;
	}


	public void setMuerteDemora1(String muerteDemora1) {
		this.muerteDemora1 = muerteDemora1;
	}


	public String getMuerteDemora2() {
		return muerteDemora2;
	}


	public void setMuerteDemora2(String muerteDemora2) {
		this.muerteDemora2 = muerteDemora2;
	}


	public String getMuerteDemora3() {
		return muerteDemora3;
	}


	public void setMuerteDemora3(String muerteDemora3) {
		this.muerteDemora3 = muerteDemora3;
	}


	public String getMuerteDemora4() {
		return muerteDemora4;
	}


	public void setMuerteDemora4(String muerteDemora4) {
		this.muerteDemora4 = muerteDemora4;
	}


	public String getMuertos() {
		return muertos;
	}


	public void setMuertos(String muertos) {
		this.muertos = muertos;
	}


	public int getQuienClasificoRiesgo() {
		return quienClasificoRiesgo;
	}


	public void setQuienClasificoRiesgo(int quienClasificoRiesgo) {
		this.quienClasificoRiesgo = quienClasificoRiesgo;
	}


	public int getRemisionOportunaComplica() {
		return remisionOportunaComplica;
	}


	public void setRemisionOportunaComplica(int remisionOportunaComplica) {
		this.remisionOportunaComplica = remisionOportunaComplica;
	}


	public int getSemanaInicioCpn() {
		return semanaInicioCpn;
	}


	public void setSemanaInicioCpn(int semanaInicioCpn) {
		this.semanaInicioCpn = semanaInicioCpn;
	}


	public String getVivos() {
		return vivos;
	}


	public void setVivos(String vivos) {
		this.vivos = vivos;
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
