/*
 * Creado en 14-jul-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.mundo.saludpublica;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.FichaVIHDao;
import com.sysmedica.dao.FichaVIHDao;
import com.sysmedica.mundo.saludpublica.Paciente;


/**
 * @author santiago
 *
 */
public class FichaVIH {

	private Logger logger = Logger.getLogger(FichaVIH.class);
	
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
    
    private int codigoFichaVIH;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
        
    private HashMap mecanismosTransmision;
    private int tipoMuestra;
    private int tipoPrueba;
    private int resultado;
    private String fechaResultado;
    private String valor;
    private int estadioClinico;
    private int numeroHijos;
    private int numeroHijas;
    private int embarazo;
    private int numeroSemanas;
    private HashMap enfermedadesAsociadas;
    private String pais;
    private int areaProcedencia;
    
    private Paciente paciente;
    
    private FichaVIHDao fichaVIHDao;
    
    public static int codigoGlobal;
    
    private int estadoAnterior;
    
    private boolean activa;
    
    public FichaVIH() {
    	
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
				fichaVIHDao = myFactory.getFichaVIHDao();
				wasInited = (fichaVIHDao != null);
			}
			return wasInited;
	}
    
    
    public void reset() {
    	
    	sire="";
    	mecanismosTransmision = new HashMap();
    	tipoMuestra = 0;
        tipoPrueba = 0;
        resultado = 0;
        fechaResultado = "";
        valor = "";
        estadioClinico = 0;
        numeroHijos = 0;
        numeroHijas = 0;
        embarazo = 0;
        numeroSemanas = 0;
        enfermedadesAsociadas = new HashMap();
        
        paciente = new Paciente();
    }

    
    public int insertarFicha(Connection con) {
    	
    	return fichaVIHDao.insertarFicha(con,
											notificar,
										    loginUsuario,
										    codigoFichaVIH,
										    codigoPaciente,
										    codigoDiagnostico,
										    codigoNotificacion,
										    numeroSolicitud,
										    estadoFicha,
										    codigoConvenio,
											nombreProfesional);
    }
    
    
    public void cargarDatos(Connection con, int codigo) {
    	
    	this.codigoFichaVIH = codigo;
    	
    	ResultSet rs = fichaVIHDao.consultaTodoFicha(con,codigo);
    	
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
			    this.setTipoMuestra(rs.getInt("tipoMuestra"));
			    this.setTipoPrueba(rs.getInt("tipoPrueba"));
			    this.setResultado(rs.getInt("resultado"));
			    this.setFechaResultado(rs.getString("fechaResultado"));
			    this.setValor(rs.getString("valor"));
			    this.setEstadioClinico(rs.getInt("estadioClinico"));
			    this.setNumeroHijas(rs.getInt("numeroHijas"));
			    this.setNumeroHijos(rs.getInt("numeroHijos"));
			    this.setEmbarazo(rs.getInt("embarazo"));
			    this.setNumeroSemanas(rs.getInt("numeroSemanas"));
			    
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
			    this.setPais(rs.getString("pais"));
			    this.setAreaProcedencia(rs.getInt("areaProcedencia"));
			    
			    ResultSet rs2 = fichaVIHDao.consultarMecanismosTransmision(con,codigo);
			    
			    HashMap mecTransmision = new HashMap();
			    
			    while (rs2.next()) {
			    	
			    	mecTransmision.put("mecanismo_"+rs2.getInt("codigomecanismo"),"true");
			    }
			    
			    this.setMecanismosTransmision(mecTransmision);
			    
			    ResultSet rs3 = fichaVIHDao.consultarEnfermedadesAsociadas(con,codigo);
			    
			    HashMap enfAsociadas = new HashMap();
			    
			    while (rs3.next()) {
			    	
			    	enfAsociadas.put("enfermedad_"+rs3.getInt("codigoenfermedad"),"true");
			    }
			    
			    this.setEnfermedadesAsociadas(enfAsociadas);
    		}
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("Error consultando los datos de la ficha de VIH : "+sqle.getMessage());
    	}
    }
    
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaVIHDao.modificarFicha(con,
    										sire,
											notificar,
										    loginUsuario,
										    codigoFichaVIH,
										    codigoPaciente,
										    codigoDiagnostico,
										    codigoNotificacion,
										    numeroSolicitud,
										    estadoFicha,
										    mecanismosTransmision,
										    tipoMuestra,
										    tipoPrueba,
										    resultado,
										    fechaResultado,
										    valor,
										    estadioClinico,
										    numeroHijos,
										    numeroHijas,
										    embarazo,
										    numeroSemanas,
										    enfermedadesAsociadas,
										    
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
										    areaProcedencia);
    }
    
    
    
    public void cargarPaciente(Connection con, int codigo, boolean empezarnuevo) {
    	
    	ResultSet rs = fichaVIHDao.consultaDatosPaciente(con,codigo,empezarnuevo);
    	
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
    		Log4JManager.error(sqle.getMessage());
        }
    }
    
    
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaVIHDao.insertarFichaCompleta(con,
									    		numeroSolicitud,
												loginUsuario,
												codigoPaciente,
												codigoDiagnostico,
												estadoFicha,
												codigoConvenio,
												nombreProfesional,
											    
											    sire,
												notificar,
												
												codigoFichaVIH,										    
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
											    
											    mecanismosTransmision,
											    tipoMuestra,
											    tipoPrueba,
											    resultado,
											    fechaResultado,
											    valor,
											    estadioClinico,
											    numeroHijos,
											    numeroHijas,
											    embarazo,
											    numeroSemanas,
											    enfermedadesAsociadas,
											    estadoAnterior,
											    activa,
											    pais,
											    areaProcedencia);
    }
    
    
	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}

	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}

	public int getCodigoFichaVIH() {
		return codigoFichaVIH;
	}

	public void setCodigoFichaVIH(int codigoFichaVIH) {
		this.codigoFichaVIH = codigoFichaVIH;
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

	public int getEmbarazo() {
		return embarazo;
	}

	public void setEmbarazo(int embarazo) {
		this.embarazo = embarazo;
	}

	public HashMap getEnfermedadesAsociadas() {
		return enfermedadesAsociadas;
	}

	public void setEnfermedadesAsociadas(HashMap enfermedadesAsociadas) {
		this.enfermedadesAsociadas = enfermedadesAsociadas;
	}

	public int getEstadioClinico() {
		return estadioClinico;
	}

	public void setEstadioClinico(int estadioClinico) {
		this.estadioClinico = estadioClinico;
	}

	public int getEstadoFicha() {
		return estadoFicha;
	}

	public void setEstadoFicha(int estadoFicha) {
		this.estadoFicha = estadoFicha;
	}

	public String getFechaResultado() {
		return fechaResultado;
	}

	public void setFechaResultado(String fechaResultado) {
		this.fechaResultado = fechaResultado;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public HashMap getMecanismosTransmision() {
		return mecanismosTransmision;
	}

	public void setMecanismosTransmision(HashMap mecanismosTransmision) {
		this.mecanismosTransmision = mecanismosTransmision;
	}

	public boolean isNotificar() {
		return notificar;
	}

	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}

	public int getNumeroHijas() {
		return numeroHijas;
	}

	public void setNumeroHijas(int numeroHijas) {
		this.numeroHijas = numeroHijas;
	}

	public int getNumeroHijos() {
		return numeroHijos;
	}

	public void setNumeroHijos(int numeroHijos) {
		this.numeroHijos = numeroHijos;
	}

	public int getNumeroSemanas() {
		return numeroSemanas;
	}

	public void setNumeroSemanas(int numeroSemanas) {
		this.numeroSemanas = numeroSemanas;
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

	public int getResultado() {
		return resultado;
	}

	public void setResultado(int resultado) {
		this.resultado = resultado;
	}

	public int getTipoMuestra() {
		return tipoMuestra;
	}

	public void setTipoMuestra(int tipoMuestra) {
		this.tipoMuestra = tipoMuestra;
	}

	public int getTipoPrueba() {
		return tipoPrueba;
	}

	public void setTipoPrueba(int tipoPrueba) {
		this.tipoPrueba = tipoPrueba;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
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

	public FichaVIHDao getFichaVIHDao() {
		return fichaVIHDao;
	}

	public void setFichaVIHDao(FichaVIHDao fichaVIHDao) {
		this.fichaVIHDao = fichaVIHDao;
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
