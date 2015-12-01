package com.sysmedica.mundo.saludpublica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.FichaLepraDao;

public class FichaLepra {

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
    
    private int codigoFichaLepra;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
	private int criterioClinico;
	private String indiceBacilar;
	private int clasificacion;
	private int resultadosBiopsia;
	private int ojoDerecho;
	private int ojoIzquierdo;
	private int manoDerecha;
	private int manoIzquierda;
	private int pieDerecho;
	private int pieIzquierdo;
	private int tipoCasoLepra;
	private int tieneCicatriz;
	private int fuenteContagio;
	private int metodoCaptacion;
	private String fechaInvestigacion;
	private int tieneConvivientes;
	private String totalConvivientes;
	private String totalExaminados;
	private String sanosConCicatriz;
	private String sanosSinCicatriz;
	private String sintomaticosConCicatriz;
	private String sintomaticosSinCicatriz;
	private String vacunadosBcg;
	private String motivoNoAplicacion;
	private String investigadoPor;
	private String telefonoInvestigador;
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
    
    FichaLepraDao fichaLepraDao;
    
    
    public void reset() {
    	
    	criterioClinico = 0;
    	indiceBacilar = "";
    	clasificacion = 0;
    	resultadosBiopsia = 0;
    	ojoDerecho = 0;
    	ojoIzquierdo = 0;
    	manoDerecha = 0;
    	manoIzquierda = 0;
    	pieDerecho = 0;
    	pieIzquierdo = 0;
    	tipoCasoLepra = 0;
    	tieneCicatriz = 0;
    	fuenteContagio = 0;
    	metodoCaptacion = 0;
    	fechaInvestigacion = "";
    	tieneConvivientes = 0;
    	totalConvivientes = "";
    	totalExaminados = "";
    	sanosConCicatriz = "";
    	sanosSinCicatriz = "";
    	sintomaticosConCicatriz = "";
    	sintomaticosSinCicatriz = "";
    	vacunadosBcg = "";
    	motivoNoAplicacion = "";
    	investigadoPor = "";
    	telefonoInvestigador = "";
    	observaciones = "";
    	
    	paciente = new Paciente();
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
			fichaLepraDao = myFactory.getFichaLepraDao();
			wasInited = (fichaLepraDao != null);
		}
		return wasInited;
	}
    
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaLepraDao.insertarFichaCompleta(con,
										    		numeroSolicitud,
													loginUsuario,
													codigoPaciente,
													codigoDiagnostico,
													estadoFicha,
													codigoConvenio,
													nombreProfesional,
												    
												    sire,
													notificar,
													
													codigoFichaLepra,										    
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
												    
												    criterioClinico,
													indiceBacilar,
													clasificacion,
													resultadosBiopsia,
													ojoDerecho,
													ojoIzquierdo,
													manoDerecha,
													manoIzquierda,
													pieDerecho,
													pieIzquierdo,
													tipoCasoLepra,
													tieneCicatriz,
													fuenteContagio,
													metodoCaptacion,
													fechaInvestigacion,
													tieneConvivientes,
													totalConvivientes,
													totalExaminados,
													sanosConCicatriz,
													sanosSinCicatriz,
													sintomaticosConCicatriz,
													sintomaticosSinCicatriz,
													vacunadosBcg,
													motivoNoAplicacion,
													investigadoPor,
													telefonoInvestigador,
													observaciones,
												    
												    activa,
												    pais,
												    areaProcedencia);
    }
    
    
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaLepraDao.modificarFicha(con,
											sire,
											notificar,
										    loginUsuario,
										    codigoFichaLepra,
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
							
										    criterioClinico,
											indiceBacilar,
											clasificacion,
											resultadosBiopsia,
											ojoDerecho,
											ojoIzquierdo,
											manoDerecha,
											manoIzquierda,
											pieDerecho,
											pieIzquierdo,
											tipoCasoLepra,
											tieneCicatriz,
											fuenteContagio,
											metodoCaptacion,
											fechaInvestigacion,
											tieneConvivientes,
											totalConvivientes,
											totalExaminados,
											sanosConCicatriz,
											sanosSinCicatriz,
											sintomaticosConCicatriz,
											sintomaticosSinCicatriz,
											vacunadosBcg,
											motivoNoAplicacion,
											investigadoPor,
											telefonoInvestigador,
											observaciones,
											pais,
										    areaProcedencia);
    }
    
    
    
    

    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaLepraDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    	
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
    	this.codigoFichaLepra = codigo;
    	
    	ResultSet rs = fichaLepraDao.consultaTodoFicha(con,codigo);
    	
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
			    
			    this.setCriterioClinico(rs.getInt("criterioClinico"));
			    this.setIndiceBacilar(rs.getString("indiceBacilar"));
			    this.setClasificacion(rs.getInt("clasificacion"));
			    this.setResultadosBiopsia(rs.getInt("resultadosBiopsia"));
			    this.setOjoDerecho(rs.getInt("ojoDerecho"));
			    this.setOjoIzquierdo(rs.getInt("ojoIzquierdo"));
			    this.setManoDerecha(rs.getInt("manoDerecha"));
			    this.setManoIzquierda(rs.getInt("manoIzquierda"));
			    this.setPieDerecho(rs.getInt("pieDerecho"));
			    this.setPieIzquierdo(rs.getInt("pieIzquierdo"));
			    this.setTipoCasoLepra(rs.getInt("tipoCasoLepra"));
			    this.setTieneCicatriz(rs.getInt("tieneCicatriz"));
			    this.setFuenteContagio(rs.getInt("fuenteContagio"));
			    this.setMetodoCaptacion(rs.getInt("metodoCaptacion"));
			    this.setFechaInvestigacion(rs.getString("fechaInvestigacion"));
			    this.setTieneConvivientes(rs.getInt("tieneConvivientes"));
			    this.setTotalConvivientes(rs.getString("totalConvivientes"));
			    this.setTotalExaminados(rs.getString("totalExaminados"));
			    this.setSanosConCicatriz(rs.getString("sanosConCicatriz"));
			    this.setSanosSinCicatriz(rs.getString("sanosSinCicatriz"));
			    this.setSintomaticosConCicatriz(rs.getString("sintomaticosConCicatriz"));
			    this.setSintomaticosSinCicatriz(rs.getString("sintomaticosSinCicatriz"));
			    this.setVacunadosBcg(rs.getString("vacunadosBcg"));
			    this.setMotivoNoAplicacion(rs.getString("motivoNoAplicacion"));
			    this.setInvestigadoPor(rs.getString("investigadoPor"));
			    this.setTelefonoInvestigador(rs.getString("telefonoInvestigador"));
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

    
    

    public FichaLepra()
    {
    	reset();
        init(System.getProperty("TIPOBD"));
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


	public int getClasificacion() {
		return clasificacion;
	}


	public void setClasificacion(int clasificacion) {
		this.clasificacion = clasificacion;
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


	public int getCodigoFichaLepra() {
		return codigoFichaLepra;
	}


	public void setCodigoFichaLepra(int codigoFichaLepra) {
		this.codigoFichaLepra = codigoFichaLepra;
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


	public int getCriterioClinico() {
		return criterioClinico;
	}


	public void setCriterioClinico(int criterioClinico) {
		this.criterioClinico = criterioClinico;
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


	public String getFechaInvestigacion() {
		return fechaInvestigacion;
	}


	public void setFechaInvestigacion(String fechaInvestigacion) {
		this.fechaInvestigacion = fechaInvestigacion;
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


	public int getFuenteContagio() {
		return fuenteContagio;
	}


	public void setFuenteContagio(int fuenteContagio) {
		this.fuenteContagio = fuenteContagio;
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


	public String getIndiceBacilar() {
		return indiceBacilar;
	}


	public void setIndiceBacilar(String indiceBacilar) {
		this.indiceBacilar = indiceBacilar;
	}


	public int getInstitucionAtendio() {
		return institucionAtendio;
	}


	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
	}


	public String getInvestigadoPor() {
		return investigadoPor;
	}


	public void setInvestigadoPor(String investigadoPor) {
		this.investigadoPor = investigadoPor;
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


	public int getManoDerecha() {
		return manoDerecha;
	}


	public void setManoDerecha(int manoDerecha) {
		this.manoDerecha = manoDerecha;
	}


	public int getManoIzquierda() {
		return manoIzquierda;
	}


	public void setManoIzquierda(int manoIzquierda) {
		this.manoIzquierda = manoIzquierda;
	}


	public int getMetodoCaptacion() {
		return metodoCaptacion;
	}


	public void setMetodoCaptacion(int metodoCaptacion) {
		this.metodoCaptacion = metodoCaptacion;
	}


	public String getMotivoNoAplicacion() {
		return motivoNoAplicacion;
	}


	public void setMotivoNoAplicacion(String motivoNoAplicacion) {
		this.motivoNoAplicacion = motivoNoAplicacion;
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


	public int getOjoDerecho() {
		return ojoDerecho;
	}


	public void setOjoDerecho(int ojoDerecho) {
		this.ojoDerecho = ojoDerecho;
	}


	public int getOjoIzquierdo() {
		return ojoIzquierdo;
	}


	public void setOjoIzquierdo(int ojoIzquierdo) {
		this.ojoIzquierdo = ojoIzquierdo;
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


	public int getPieDerecho() {
		return pieDerecho;
	}


	public void setPieDerecho(int pieDerecho) {
		this.pieDerecho = pieDerecho;
	}


	public int getPieIzquierdo() {
		return pieIzquierdo;
	}


	public void setPieIzquierdo(int pieIzquierdo) {
		this.pieIzquierdo = pieIzquierdo;
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


	public int getResultadosBiopsia() {
		return resultadosBiopsia;
	}


	public void setResultadosBiopsia(int resultadosBiopsia) {
		this.resultadosBiopsia = resultadosBiopsia;
	}


	public String getSanosConCicatriz() {
		return sanosConCicatriz;
	}


	public void setSanosConCicatriz(String sanosConCicatriz) {
		this.sanosConCicatriz = sanosConCicatriz;
	}


	public String getSanosSinCicatriz() {
		return sanosSinCicatriz;
	}


	public void setSanosSinCicatriz(String sanosSinCicatriz) {
		this.sanosSinCicatriz = sanosSinCicatriz;
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


	public String getSintomaticosConCicatriz() {
		return sintomaticosConCicatriz;
	}


	public void setSintomaticosConCicatriz(String sintomaticosConCicatriz) {
		this.sintomaticosConCicatriz = sintomaticosConCicatriz;
	}


	public String getSintomaticosSinCicatriz() {
		return sintomaticosSinCicatriz;
	}


	public void setSintomaticosSinCicatriz(String sintomaticosSinCicatriz) {
		this.sintomaticosSinCicatriz = sintomaticosSinCicatriz;
	}


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
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


	public int getTieneCicatriz() {
		return tieneCicatriz;
	}


	public void setTieneCicatriz(int tieneCicatriz) {
		this.tieneCicatriz = tieneCicatriz;
	}


	public int getTieneConvivientes() {
		return tieneConvivientes;
	}


	public void setTieneConvivientes(int tieneConvivientes) {
		this.tieneConvivientes = tieneConvivientes;
	}


	public int getTipoCaso() {
		return tipoCaso;
	}


	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}


	public int getTipoCasoLepra() {
		return tipoCasoLepra;
	}


	public void setTipoCasoLepra(int tipoCasoLepra) {
		this.tipoCasoLepra = tipoCasoLepra;
	}


	public String getTotalConvivientes() {
		return totalConvivientes;
	}


	public void setTotalConvivientes(String totalConvivientes) {
		this.totalConvivientes = totalConvivientes;
	}


	public String getTotalExaminados() {
		return totalExaminados;
	}


	public void setTotalExaminados(String totalExaminados) {
		this.totalExaminados = totalExaminados;
	}


	public String getVacunadosBcg() {
		return vacunadosBcg;
	}


	public void setVacunadosBcg(String vacunadosBcg) {
		this.vacunadosBcg = vacunadosBcg;
	}
    
}
