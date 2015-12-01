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
import com.sysmedica.dao.FichaEtasDao;

public class FichaEtas {

	private Logger logger = Logger.getLogger(FichaEasv.class);
	
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
    
    private int codigoFichaEtas;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
	
    private String otroSintoma;
    private String horaInicioSintomas;
    private String minutoInicioSintomas;
    private String nombreAlimento1;
    private String nombreAlimento2;
    private String nombreAlimento3;
    private String nombreAlimento4;
    private String nombreAlimento5;
    private String nombreAlimento6;
    private String nombreAlimento7;
    private String nombreAlimento8;
    private String nombreAlimento9;
    private String lugarConsumo1;
    private String lugarConsumo2;
    private String lugarConsumo3;
    private String lugarConsumo4;
    private String lugarConsumo5;
    private String lugarConsumo6;
    private String lugarConsumo7;
    private String lugarConsumo8;
    private String lugarConsumo9;
    private String horaConsumo1;
    private String horaConsumo2;
    private String horaConsumo3;
    private String horaConsumo4;
    private String horaConsumo5;
    private String horaConsumo6;
    private String horaConsumo7;
    private String horaConsumo8;
    private String horaConsumo9;
    private String minutoConsumo1;
    private String minutoConsumo2;
    private String minutoConsumo3;
    private String minutoConsumo4;
    private String minutoConsumo5;
    private String minutoConsumo6;
    private String minutoConsumo7;
    private String minutoConsumo8;
    private String minutoConsumo9;
    private int asociadoBrote;
    private int captadoPor;
    private int relacionExposicion;
    private int tomoMuestra;
    private int tipoMuestra;
    private String cualMuestra;
    private int agente1;
    private int agente2;
    private int agente3;
    private int agente4;
    
    private HashMap sintomas;
    private HashMap alimentos;
    

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
    
    FichaEtasDao fichaEtasDao;
    
    public void reset()
    {
    	otroSintoma = "";
        horaInicioSintomas = "";
        minutoInicioSintomas = "";
        nombreAlimento1 = "";
        nombreAlimento2 = "";
        nombreAlimento3 = "";
        nombreAlimento4 = "";
        nombreAlimento5 = "";
        nombreAlimento6 = "";
        nombreAlimento7 = "";
        nombreAlimento8 = "";
        nombreAlimento9 = "";
        lugarConsumo1 = "";
        lugarConsumo2 = "";
        lugarConsumo3 = "";
        lugarConsumo4 = "";
        lugarConsumo5 = "";
        lugarConsumo6 = "";
        lugarConsumo7 = "";
        lugarConsumo8 = "";
        lugarConsumo9 = "";
        horaConsumo1 = "";
        horaConsumo2 = "";
        horaConsumo3 = "";
        horaConsumo4 = "";
        horaConsumo5 = "";
        horaConsumo6 = "";
        horaConsumo7 = "";
        horaConsumo8 = "";
        horaConsumo9 = "";
        minutoConsumo1 = "";
        minutoConsumo2 = "";
        minutoConsumo3 = "";
        minutoConsumo4 = "";
        minutoConsumo5 = "";
        minutoConsumo6 = "";
        minutoConsumo7 = "";
        minutoConsumo8 = "";
        minutoConsumo9 = "";
        asociadoBrote = 0;
        captadoPor = 0;
        relacionExposicion = 0;
        tomoMuestra = 0;
        tipoMuestra = 0;
        cualMuestra = "";
        agente1 = 0;
        agente2 = 0;
        agente3 = 0;
        agente4 = 0;
        
        paciente = new Paciente();
        
        sintomas = new HashMap();
        alimentos = new HashMap();
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
			fichaEtasDao = myFactory.getFichaEtasDao();
			wasInited = (fichaEtasDao != null);
		}
		return wasInited;
	}
    
    
    public FichaEtas()
    {
    	reset();
        init(System.getProperty("TIPOBD"));
    }
    
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaEtasDao.insertarFichaCompleta(con,
										    		numeroSolicitud,
													loginUsuario,
													codigoPaciente,
													codigoDiagnostico,
													estadoFicha,
													codigoConvenio,
													nombreProfesional,
												    
												    sire,
													notificar,
													
													codigoFichaEtas,										    
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
												    
												    otroSintoma,
												    horaInicioSintomas,
												    minutoInicioSintomas,
												    nombreAlimento1,
												    nombreAlimento2,
												    nombreAlimento3,
												    nombreAlimento4,
												    nombreAlimento5,
												    nombreAlimento6,
												    nombreAlimento7,
												    nombreAlimento8,
												    nombreAlimento9,
												    lugarConsumo1,
												    lugarConsumo2,
												    lugarConsumo3,
												    lugarConsumo4,
												    lugarConsumo5,
												    lugarConsumo6,
												    lugarConsumo7,
												    lugarConsumo8,
												    lugarConsumo9,
												    horaConsumo1,
												    horaConsumo2,
												    horaConsumo3,
												    horaConsumo4,
												    horaConsumo5,
												    horaConsumo6,
												    horaConsumo7,
												    horaConsumo8,
												    horaConsumo9,
												    minutoConsumo1,
												    minutoConsumo2,
												    minutoConsumo3,
												    minutoConsumo4,
												    minutoConsumo5,
												    minutoConsumo6,
												    minutoConsumo7,
												    minutoConsumo8,
												    minutoConsumo9,
												    asociadoBrote,
												    captadoPor,
												    relacionExposicion,
												    tomoMuestra,
												    tipoMuestra,
												    cualMuestra,
												    agente1,
												    agente2,
												    agente3,
												    agente4,
												    
												    activa,
												    pais,
												    areaProcedencia,
												    
												    sintomas,
												    alimentos);
    }
    
    
    
    
    
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaEtasDao.modificarFicha(con,
											sire,
											notificar,
										    loginUsuario,
										    codigoFichaEtas,
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
							
										    otroSintoma,
										    horaInicioSintomas,
										    minutoInicioSintomas,
										    nombreAlimento1,
										    nombreAlimento2,
										    nombreAlimento3,
										    nombreAlimento4,
										    nombreAlimento5,
										    nombreAlimento6,
										    nombreAlimento7,
										    nombreAlimento8,
										    nombreAlimento9,
										    lugarConsumo1,
										    lugarConsumo2,
										    lugarConsumo3,
										    lugarConsumo4,
										    lugarConsumo5,
										    lugarConsumo6,
										    lugarConsumo7,
										    lugarConsumo8,
										    lugarConsumo9,
										    horaConsumo1,
										    horaConsumo2,
										    horaConsumo3,
										    horaConsumo4,
										    horaConsumo5,
										    horaConsumo6,
										    horaConsumo7,
										    horaConsumo8,
										    horaConsumo9,
										    minutoConsumo1,
										    minutoConsumo2,
										    minutoConsumo3,
										    minutoConsumo4,
										    minutoConsumo5,
										    minutoConsumo6,
										    minutoConsumo7,
										    minutoConsumo8,
										    minutoConsumo9,
										    asociadoBrote,
										    captadoPor,
										    relacionExposicion,
										    tomoMuestra,
										    tipoMuestra,
										    cualMuestra,
										    agente1,
										    agente2,
										    agente3,
										    agente4,
											pais,
										    areaProcedencia,
										    sintomas,
										    alimentos);
    }

    
    
    

    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaEtasDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    	
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
    	this.codigoFichaEtas = codigo;
    	
    	ResultSet rs = fichaEtasDao.consultaTodoFicha(con,codigo);
    	
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
    
			    this.setOtroSintoma(rs.getString("otroSintoma"));
			    this.setHoraInicioSintomas(rs.getString("horaInicioSintomas"));
			    
			    this.setAsociadoBrote(rs.getInt("asociadoBrote"));
			    this.setCaptadoPor(rs.getInt("captadoPor"));
			    this.setRelacionExposicion(rs.getInt("relacionExposicion"));
			    this.setTomoMuestra(rs.getInt("tomoMuestra"));
			    this.setTipoMuestra(rs.getInt("tipoMuestra"));
			    this.setCualMuestra(rs.getString("cualMuestra"));
			    this.setAgente1(rs.getInt("agente1"));
			    this.setAgente2(rs.getInt("agente2"));
			    this.setAgente3(rs.getInt("agente3"));
			    this.setAgente4(rs.getInt("agente4"));
			    
			    
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
			    

			    ResultSet rs2 = fichaEtasDao.consultarSintomas(con,codigo);
			    
			    HashMap sint = new HashMap();
			    
			    while (rs2.next()) {
			    	
			    	sint.put("sintoma_"+rs2.getInt("codigo"),"true");
			    }
			    
			    this.setSintomas(sint);
			    
			    

			    ResultSet rs3 = fichaEtasDao.consultarAlimentos(con,codigo);
			    
			    HashMap alimentosIngeridos = new HashMap();
			    
			    int i=0;
			    
			    while (rs3.next()) {
			    	
			    	alimentosIngeridos.put("nombre_"+Integer.toString(i),rs3.getString("nombre"));
			    	alimentosIngeridos.put("hora_"+Integer.toString(i),rs3.getString("hora"));
			    	alimentosIngeridos.put("lugar_"+Integer.toString(i),rs3.getString("lugar"));
			    	
			    	i++;
			    }
			    
			    this.setAlimentos(alimentosIngeridos);
			    
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



	public int getAgente1() {
		return agente1;
	}



	public void setAgente1(int agente1) {
		this.agente1 = agente1;
	}



	public int getAgente2() {
		return agente2;
	}



	public void setAgente2(int agente2) {
		this.agente2 = agente2;
	}



	public int getAgente3() {
		return agente3;
	}



	public void setAgente3(int agente3) {
		this.agente3 = agente3;
	}



	public int getAgente4() {
		return agente4;
	}



	public void setAgente4(int agente4) {
		this.agente4 = agente4;
	}



	public int getAreaProcedencia() {
		return areaProcedencia;
	}



	public void setAreaProcedencia(int areaProcedencia) {
		this.areaProcedencia = areaProcedencia;
	}



	public int getAsociadoBrote() {
		return asociadoBrote;
	}



	public void setAsociadoBrote(int asociadoBrote) {
		this.asociadoBrote = asociadoBrote;
	}



	public int getCaptadoPor() {
		return captadoPor;
	}



	public void setCaptadoPor(int captadoPor) {
		this.captadoPor = captadoPor;
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



	public int getCodigoFichaEtas() {
		return codigoFichaEtas;
	}



	public void setCodigoFichaEtas(int codigoFichaEtas) {
		this.codigoFichaEtas = codigoFichaEtas;
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



	public String getCualMuestra() {
		return cualMuestra;
	}



	public void setCualMuestra(String cualMuestra) {
		this.cualMuestra = cualMuestra;
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



	public String getGenero() {
		return genero;
	}



	public void setGenero(String genero) {
		this.genero = genero;
	}



	public String getHoraConsumo1() {
		return horaConsumo1;
	}



	public void setHoraConsumo1(String horaConsumo1) {
		this.horaConsumo1 = horaConsumo1;
	}



	public String getHoraConsumo2() {
		return horaConsumo2;
	}



	public void setHoraConsumo2(String horaConsumo2) {
		this.horaConsumo2 = horaConsumo2;
	}



	public String getHoraConsumo3() {
		return horaConsumo3;
	}



	public void setHoraConsumo3(String horaConsumo3) {
		this.horaConsumo3 = horaConsumo3;
	}



	public String getHoraConsumo4() {
		return horaConsumo4;
	}



	public void setHoraConsumo4(String horaConsumo4) {
		this.horaConsumo4 = horaConsumo4;
	}



	public String getHoraConsumo5() {
		return horaConsumo5;
	}



	public void setHoraConsumo5(String horaConsumo5) {
		this.horaConsumo5 = horaConsumo5;
	}



	public String getHoraConsumo6() {
		return horaConsumo6;
	}



	public void setHoraConsumo6(String horaConsumo6) {
		this.horaConsumo6 = horaConsumo6;
	}



	public String getHoraConsumo7() {
		return horaConsumo7;
	}



	public void setHoraConsumo7(String horaConsumo7) {
		this.horaConsumo7 = horaConsumo7;
	}



	public String getHoraConsumo8() {
		return horaConsumo8;
	}



	public void setHoraConsumo8(String horaConsumo8) {
		this.horaConsumo8 = horaConsumo8;
	}



	public String getHoraConsumo9() {
		return horaConsumo9;
	}



	public void setHoraConsumo9(String horaConsumo9) {
		this.horaConsumo9 = horaConsumo9;
	}



	public String getHoraInicioSintomas() {
		return horaInicioSintomas;
	}



	public void setHoraInicioSintomas(String horaInicioSintomas) {
		this.horaInicioSintomas = horaInicioSintomas;
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



	public String getLugarConsumo1() {
		return lugarConsumo1;
	}



	public void setLugarConsumo1(String lugarConsumo1) {
		this.lugarConsumo1 = lugarConsumo1;
	}



	public String getLugarConsumo2() {
		return lugarConsumo2;
	}



	public void setLugarConsumo2(String lugarConsumo2) {
		this.lugarConsumo2 = lugarConsumo2;
	}



	public String getLugarConsumo3() {
		return lugarConsumo3;
	}



	public void setLugarConsumo3(String lugarConsumo3) {
		this.lugarConsumo3 = lugarConsumo3;
	}



	public String getLugarConsumo4() {
		return lugarConsumo4;
	}



	public void setLugarConsumo4(String lugarConsumo4) {
		this.lugarConsumo4 = lugarConsumo4;
	}



	public String getLugarConsumo5() {
		return lugarConsumo5;
	}



	public void setLugarConsumo5(String lugarConsumo5) {
		this.lugarConsumo5 = lugarConsumo5;
	}



	public String getLugarConsumo6() {
		return lugarConsumo6;
	}



	public void setLugarConsumo6(String lugarConsumo6) {
		this.lugarConsumo6 = lugarConsumo6;
	}



	public String getLugarConsumo7() {
		return lugarConsumo7;
	}



	public void setLugarConsumo7(String lugarConsumo7) {
		this.lugarConsumo7 = lugarConsumo7;
	}



	public String getLugarConsumo8() {
		return lugarConsumo8;
	}



	public void setLugarConsumo8(String lugarConsumo8) {
		this.lugarConsumo8 = lugarConsumo8;
	}



	public String getLugarConsumo9() {
		return lugarConsumo9;
	}



	public void setLugarConsumo9(String lugarConsumo9) {
		this.lugarConsumo9 = lugarConsumo9;
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



	public String getMinutoConsumo1() {
		return minutoConsumo1;
	}



	public void setMinutoConsumo1(String minutoConsumo1) {
		this.minutoConsumo1 = minutoConsumo1;
	}



	public String getMinutoConsumo2() {
		return minutoConsumo2;
	}



	public void setMinutoConsumo2(String minutoConsumo2) {
		this.minutoConsumo2 = minutoConsumo2;
	}



	public String getMinutoConsumo3() {
		return minutoConsumo3;
	}



	public void setMinutoConsumo3(String minutoConsumo3) {
		this.minutoConsumo3 = minutoConsumo3;
	}



	public String getMinutoConsumo4() {
		return minutoConsumo4;
	}



	public void setMinutoConsumo4(String minutoConsumo4) {
		this.minutoConsumo4 = minutoConsumo4;
	}



	public String getMinutoConsumo5() {
		return minutoConsumo5;
	}



	public void setMinutoConsumo5(String minutoConsumo5) {
		this.minutoConsumo5 = minutoConsumo5;
	}



	public String getMinutoConsumo6() {
		return minutoConsumo6;
	}



	public void setMinutoConsumo6(String minutoConsumo6) {
		this.minutoConsumo6 = minutoConsumo6;
	}



	public String getMinutoConsumo7() {
		return minutoConsumo7;
	}



	public void setMinutoConsumo7(String minutoConsumo7) {
		this.minutoConsumo7 = minutoConsumo7;
	}



	public String getMinutoConsumo8() {
		return minutoConsumo8;
	}



	public void setMinutoConsumo8(String minutoConsumo8) {
		this.minutoConsumo8 = minutoConsumo8;
	}



	public String getMinutoConsumo9() {
		return minutoConsumo9;
	}



	public void setMinutoConsumo9(String minutoConsumo9) {
		this.minutoConsumo9 = minutoConsumo9;
	}



	public String getMinutoInicioSintomas() {
		return minutoInicioSintomas;
	}



	public void setMinutoInicioSintomas(String minutoInicioSintomas) {
		this.minutoInicioSintomas = minutoInicioSintomas;
	}



	public String getMunicipioNotifica() {
		return municipioNotifica;
	}



	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}



	public String getNombreAlimento1() {
		return nombreAlimento1;
	}



	public void setNombreAlimento1(String nombreAlimento1) {
		this.nombreAlimento1 = nombreAlimento1;
	}



	public String getNombreAlimento2() {
		return nombreAlimento2;
	}



	public void setNombreAlimento2(String nombreAlimento2) {
		this.nombreAlimento2 = nombreAlimento2;
	}



	public String getNombreAlimento3() {
		return nombreAlimento3;
	}



	public void setNombreAlimento3(String nombreAlimento3) {
		this.nombreAlimento3 = nombreAlimento3;
	}



	public String getNombreAlimento4() {
		return nombreAlimento4;
	}



	public void setNombreAlimento4(String nombreAlimento4) {
		this.nombreAlimento4 = nombreAlimento4;
	}



	public String getNombreAlimento5() {
		return nombreAlimento5;
	}



	public void setNombreAlimento5(String nombreAlimento5) {
		this.nombreAlimento5 = nombreAlimento5;
	}



	public String getNombreAlimento6() {
		return nombreAlimento6;
	}



	public void setNombreAlimento6(String nombreAlimento6) {
		this.nombreAlimento6 = nombreAlimento6;
	}



	public String getNombreAlimento7() {
		return nombreAlimento7;
	}



	public void setNombreAlimento7(String nombreAlimento7) {
		this.nombreAlimento7 = nombreAlimento7;
	}



	public String getNombreAlimento8() {
		return nombreAlimento8;
	}



	public void setNombreAlimento8(String nombreAlimento8) {
		this.nombreAlimento8 = nombreAlimento8;
	}



	public String getNombreAlimento9() {
		return nombreAlimento9;
	}



	public void setNombreAlimento9(String nombreAlimento9) {
		this.nombreAlimento9 = nombreAlimento9;
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



	public String getOtroSintoma() {
		return otroSintoma;
	}



	public void setOtroSintoma(String otroSintoma) {
		this.otroSintoma = otroSintoma;
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



	public int getRelacionExposicion() {
		return relacionExposicion;
	}



	public void setRelacionExposicion(int relacionExposicion) {
		this.relacionExposicion = relacionExposicion;
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



	public HashMap getSintomas() {
		return sintomas;
	}



	public void setSintomas(HashMap sintomas) {
		this.sintomas = sintomas;
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



	public int getTipoMuestra() {
		return tipoMuestra;
	}



	public void setTipoMuestra(int tipoMuestra) {
		this.tipoMuestra = tipoMuestra;
	}



	public int getTomoMuestra() {
		return tomoMuestra;
	}



	public void setTomoMuestra(int tomoMuestra) {
		this.tomoMuestra = tomoMuestra;
	}



	public HashMap getAlimentos() {
		return alimentos;
	}



	public void setAlimentos(HashMap alimentos) {
		this.alimentos = alimentos;
	}
    
}
