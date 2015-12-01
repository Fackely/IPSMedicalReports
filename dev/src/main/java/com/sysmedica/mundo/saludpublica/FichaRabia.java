/*
 * Creado en 14-jul-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.mundo.saludpublica;

import org.apache.log4j.Logger;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import java.util.HashMap;
import java.sql.*;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.FichaRabiaDao;

/**
 * @author santiago
 *
 */
public class FichaRabia {

    private Logger logger = Logger.getLogger(FichaRabia.class);
    
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

/*  private String primerNombreUsuario;
    private String segundoNombreUsuario;
    private String primerApellidoUsuario;
    private String segundoApellidoUsuario;
    private String identificacionUsuario;
*/    
    
    //**************************************
    // DATOS CARA B :
    private int codigoFichaRabia;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    
    private int estado;    
    private int sueroAntirrabico;
    private int tipoSuero;
    private int cantidadAplicada;
    private String fechaAplicacion;
    private int tipoVacunaSeguimiento;
    private int vacunaAntirrabica;
    private int tipoVacuna;
    private int dosisAplicadas;
    private String fechaUltimaDosis;
    private int tipoAgresion;
    private boolean provocada;
    private int tipoLesion;
    private boolean cabeza;
    private boolean cara;
    private boolean cuello;
    private boolean manos;
    private boolean tronco;
    private boolean extsuperiores;
    private boolean extinferiores;
    private HashMap localizacionAnatomica;
    private int tipoExposicion;
    private String observacion;
    private String codigoMunicipioAgresion;
    private String codigoDepartamentoAgresion;
    private String procedencia;
    private String direccion;
    private String fechaAgresion;
    private int profundidadLesion;
    private int confDiagnosticaCasoRabia;
    private String fechaMuestraCasoRabia;
    private String pais;
    private int areaProcedencia;
    
    private String fechaDiligenciamiento;
    private String horaDiligenciamiento;
    
    private TratamientoAntirrabico tratamientoAntirrabico;
    private Animal animal;
    private Paciente paciente;
    
    private FichaRabiaDao fichaRabiaDao;
    
    public static int codigoGlobal;
    
    private boolean activa;
    
    /**
     * Constructor
     *
     */
    public FichaRabia() {
        reset();
        init(System.getProperty("TIPOBD"));
    }
    
    
    /**
     * Metodo para resetear los atributos 
     *
     */
    public void reset() {
        
    	sire="";
        sueroAntirrabico = 0;
        tipoSuero = 0;
        cantidadAplicada = 0;
        fechaAplicacion = "";
        tipoVacunaSeguimiento = 0;
        vacunaAntirrabica = 0;
        tipoVacuna = 0;
        dosisAplicadas = 0;
        fechaUltimaDosis = "";
        tipoAgresion = 0;
        tipoLesion = 0;
        tipoExposicion = 0;
        observacion = "";
        codigoMunicipioAgresion = "21";
        codigoDepartamentoAgresion = "11";
        procedencia = "";
        direccion = "";
        fechaAgresion = "";  
        fechaInicioSint = "";
        hospitalizado = false;
        provocada = false;
        localizacionAnatomica = new HashMap();
        localizacionAnatomica.clear();
        
        estaVivo = false;
        
        tratamientoAntirrabico = new TratamientoAntirrabico();
        animal = new Animal();
        paciente = new Paciente();
        
        cabeza = false;
        manos = false;
        tronco = false;
        extsuperiores = false;
        extinferiores = false;
        
        fechaHospitalizacion = "";
        fechaDefuncion = "";
        nombreProfesional = "";
        
        
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
				fichaRabiaDao = myFactory.getFichaRabiaDao();
				wasInited = (fichaRabiaDao != null);
			}
			return wasInited;
	}
    
    /**
     * Metodo para insertar una ficha de rabia
     * @param con
     * @return
     */
    public int insertarFichaRabia(Connection con) 
    {
    	return fichaRabiaDao.insertarFicha(con,
    										sire,
											codigoPaciente,
											codigoFichaRabia,
											numeroSolicitud,
											codigoDiagnostico,
											loginUsuario,
											estado,
											codigoConvenio,
											nombreProfesional,
											sueroAntirrabico,
											tipoSuero,
										    cantidadAplicada,
										    fechaAplicacion,
										    vacunaAntirrabica,
										    tipoVacuna,
										    dosisAplicadas,
										    fechaUltimaDosis,
										    tipoAgresion,
										    provocada,
										    tipoLesion,
										    tipoExposicion,
										    cabeza,
										    cara,
										    cuello,
										    manos,
										    tronco,
										    extsuperiores,
										    extinferiores,
										    fechaAgresion,
										    
											// Elementos del Animal Agresor        															    						   
										    animal.getEspecie(),
										    animal.getFechaInicioSintomas(),
										    animal.getFechaMuerte(),
										    animal.getFechaTomaMuestra(),
										    animal.getFuenteInformacionLaboratorio(),
										    animal.getVacunado(),
										    animal.getFechaUltimaDosisAnimal(),
										    
										    
										    // Elementos del Tratamiento Antirrabico
										    tratamientoAntirrabico.isLavadoHerida(),
										    tratamientoAntirrabico.isSuturaHerida(),
										    tratamientoAntirrabico.isAplicacionSuero(),
										    tratamientoAntirrabico.getFechaAplicacionSuero(),
										    tratamientoAntirrabico.getTipoSueroTratamiento(),
										    tratamientoAntirrabico.getCantidadSueroGlutea(),
										    tratamientoAntirrabico.getCantidadSueroHerida(),
										    tratamientoAntirrabico.getNumeroLote(),
										    tratamientoAntirrabico.getLaboratorioProductor(),
										    tratamientoAntirrabico.isAplicarVacuna(),
										    tratamientoAntirrabico.getNumeroDosisTratamiento(),
										    tratamientoAntirrabico.getTipoVacunaTratamiento(),
										    tratamientoAntirrabico.getFechaVacunaDosis1(),
										    tratamientoAntirrabico.getFechaVacunaDosis2(),
										    tratamientoAntirrabico.getFechaVacunaDosis3(),
										    tratamientoAntirrabico.getFechaVacunaDosis4(),
										    tratamientoAntirrabico.getFechaVacunaDosis5(),
										    tratamientoAntirrabico.isSuspensionTratamiento(),
										    tratamientoAntirrabico.getRazonSuspension(),
										    tratamientoAntirrabico.getFechaTomaMuestraMuerte(),
										    tratamientoAntirrabico.isConfirmacionDiagnostica(),
										    tratamientoAntirrabico.getPruebasLaboratorio());
    }
    
    
    
    /**
     * Metodo para modificar la ficha de vigilancia epidemiologica de accidente rabico
     * @param con
     * @return
     */
    public int modificarFichaRabia(Connection con)
    {
        return fichaRabiaDao.modificarFicha(con,
        										sire,
												codigoPaciente,
												codigoFichaRabia,
												loginUsuario,
												estado,
												sueroAntirrabico,
												tipoSuero,
											    cantidadAplicada,
											    fechaAplicacion,
											    vacunaAntirrabica,
											    tipoVacuna,
											    dosisAplicadas,
											    fechaUltimaDosis,
											    tipoAgresion,
											    provocada,
											    tipoLesion,
											    tipoExposicion,
											    cabeza,
											    cara,
											    cuello,
											    manos,
											    tronco,
											    extsuperiores,
											    extinferiores,
											    fechaAgresion,
											    confDiagnosticaCasoRabia,
											    fechaMuestraCasoRabia,
											    pais,
											    areaProcedencia,
											    
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
												
												// Elementos del Animal Agresor        															    						   
											    animal.getEspecie(),
											    animal.getFechaInicioSintomas(),
											    animal.getFechaMuerte(),
											    animal.getFechaTomaMuestra(),
											    animal.getFuenteInformacionLaboratorio(),
											    animal.getVacunado(),
											    animal.getFechaUltimaDosisAnimal(),
											    animal.getNombrePropietario(),
											    animal.getDireccionPropietario(),
											    animal.getEstadoMomentoAgresion(),
											    animal.getUbicacionAnimal(),
											    animal.getNumeroDiasObserva(),
											    animal.getLugarObservacion(),
											    animal.getEstadoAnimalObserva(),
											    animal.getConfirmacionDiagnosticaAnimal(),
											    
											    
											    // Elementos del Tratamiento Antirrabico
											    tratamientoAntirrabico.isLavadoHerida(),
											    tratamientoAntirrabico.isSuturaHerida(),
											    tratamientoAntirrabico.isAplicacionSuero(),
											    tratamientoAntirrabico.getFechaAplicacionSuero(),
											    tratamientoAntirrabico.getTipoSueroTratamiento(),
											    tratamientoAntirrabico.getCantidadSueroGlutea(),
											    tratamientoAntirrabico.getCantidadSueroHerida(),
											    tratamientoAntirrabico.getNumeroLote(),
											    tratamientoAntirrabico.getLaboratorioProductor(),
											    tratamientoAntirrabico.isAplicarVacuna(),
											    tratamientoAntirrabico.getNumeroDosisTratamiento(),
											    tratamientoAntirrabico.getTipoVacunaTratamiento(),
											    tratamientoAntirrabico.getFechaVacunaDosis1(),
											    tratamientoAntirrabico.getFechaVacunaDosis2(),
											    tratamientoAntirrabico.getFechaVacunaDosis3(),
											    tratamientoAntirrabico.getFechaVacunaDosis4(),
											    tratamientoAntirrabico.getFechaVacunaDosis5(),
											    tratamientoAntirrabico.isSuspensionTratamiento(),
											    tratamientoAntirrabico.getRazonSuspension(),
											    tratamientoAntirrabico.getFechaTomaMuestraMuerte(),
											    tratamientoAntirrabico.isConfirmacionDiagnostica(),
											    tratamientoAntirrabico.getPruebasLaboratorio(),
											    profundidadLesion,
											    tratamientoAntirrabico.getReaccionesVacunaSuero(),
											    tratamientoAntirrabico.getEvolucionPaciente()
										   );
    }
    
    
    
    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaRabiaDao.consultaDatosPaciente(con,codigo,empezarnuevo);
    	
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
			    
			    
			    String grupoPoblacional = rs.getString("grupoPoblacional");
			    int desplazado = 0;
			    
			    if (grupoPoblacional.equals(ConstantesIntegridadDominio.acronimoDesplazados)) {
			    	desplazado = 1;
			    }
			    
			    paciente.setDesplazado(desplazado);
			    
			    if (empezarnuevo==false) {
				    paciente.setAseguradora(rs.getString("aseguradora"));
				    paciente.setRegimenSalud(rs.getString("regimenSalud"));
			    }
			    
			    paciente.setGrupoPoblacional(ValoresPorDefecto.getIntegridadDominio(grupoPoblacional).toString());
    		}
    	}
    	catch (SQLException sqle) {
            
        }
    }
    
    
    /**
     * Metodo para cargar los datos (provenientes de la BD) en el mundo
     * @param con
     * @param codigo
     */
    public void cargarDatos(Connection con, int codigo) {
        
        this.codigoFichaRabia = codigo;
        
        ResultSet rs = fichaRabiaDao.consultaTodoFicha(con,codigo);
        
        try {
			
			if (rs.next()) {
			 /*   
				this.setPrimerNombreUsuario(rs.getString("primerNombreUsuario"));
			    this.setSegundoNombreUsuario(rs.getString("segundoNombreUsuario"));
			    this.setPrimerApellidoUsuario(rs.getString("primerApellidoUsuario"));
			    this.setSegundoApellidoUsuario(rs.getString("segundoApellidoUsuario"));
			    this.setIdentificacionUsuario(rs.getString("identificacionUsuario"));
			 */   
			    this.setSire(rs.getString("sire"));
			    this.setEstado(rs.getInt("estado"));
			    this.setCantidadAplicada(rs.getInt("cantidadAplicada"));
			    this.setDosisAplicadas(rs.getInt("dosisAplicadas"));
			    this.setFechaAgresion(rs.getString("fechaAgresion"));
			    this.setFechaAplicacion(rs.getString("fechaAplicacion"));
			    this.setFechaUltimaDosis(rs.getString("fechaUltimaDosis"));
			    this.setSueroAntirrabico(rs.getInt("sueroAntirrabico"));
			    this.setTipoAgresion(rs.getInt("tipoAgresion"));
			    this.setProvocada(rs.getBoolean("provocada"));
			    this.setTipoExposicion(rs.getInt("tipoExposicion"));
			    this.setTipoLesion(rs.getInt("tipoLesion"));
			    this.setCabeza(rs.getBoolean("cabeza"));
			    this.setManos(rs.getBoolean("manos"));
			    this.setTronco(rs.getBoolean("tronco"));
			    this.setExtsuperiores(rs.getBoolean("extresuperiores"));
			    this.setExtinferiores(rs.getBoolean("extreinferiores"));
			    this.setTipoSuero(rs.getInt("tipoSuero"));
			    this.setTipoVacuna(rs.getInt("tipoVacuna"));
			    this.setVacunaAntirrabica(rs.getInt("vacunaAntirrabica"));
			    this.setFechaDiligenciamiento(rs.getString("fechaDiligenciamiento"));
			    this.setHoraDiligenciamiento(rs.getString("horaDiligenciamiento"));
			    this.setProfundidadLesion(rs.getInt("profundidadLesion"));
			    this.setConfDiagnosticaCasoRabia(rs.getInt("confDiagnosticaCasoRabia"));
			    this.setFechaMuestraCasoRabia(rs.getString("fechaMuestraCasoRabia"));
			    
			    this.setFechaNotificacion(UtilidadFecha.conversionFormatoFechaAAp(this.fechaDiligenciamiento));
			    
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
			//    paciente.setDesplazado(rs.getInt("desplazado"));
			    paciente.setTipoId(rs.getString("tipoId"));
			    paciente.setPaisExpedicion(rs.getString("codigo_pais_id"));
			    paciente.setPaisNacimiento(rs.getString("codigo_pais_nacimiento"));
			    paciente.setPaisResidencia(rs.getString("codigo_pais_vivienda"));
			    
			    String grupoPoblacional = rs.getString("grupoPoblacional");
			    int desplazado = 0;
			    
			    if (grupoPoblacional.equals(ConstantesIntegridadDominio.acronimoDesplazados)) {
			    	desplazado = 1;
			    }
			    
			    paciente.setDesplazado(desplazado);
			    paciente.setGrupoPoblacional(ValoresPorDefecto.getIntegridadDominio(grupoPoblacional).toString());
			    
			    animal.setEspecie(rs.getInt("especie"));
			    animal.setFechaInicioSintomas(rs.getString("fechainiciosintomas"));
			    animal.setFechaMuerte(rs.getString("fechamuerte"));
			    animal.setFechaTomaMuestra(rs.getString("fechatomamuestra"));
			    animal.setFuenteInformacionLaboratorio(rs.getInt("informacionlaboratorio"));
			    animal.setVacunado(rs.getBoolean("vacunado"));
			    animal.setFechaUltimaDosisAnimal(rs.getString("fechaultimadosisanimal"));
			    animal.setNombrePropietario(rs.getString("nombrepropietario"));
			    animal.setDireccionPropietario(rs.getString("direccionpropietario"));
			    animal.setEstadoMomentoAgresion(rs.getInt("estadomomentoagresion"));
			    animal.setUbicacionAnimal(rs.getInt("ubicacionanimal"));
			    animal.setNumeroDiasObserva(rs.getInt("numerodiasobserva"));
			    animal.setLugarObservacion(rs.getInt("lugarobservacion"));
			    animal.setEstadoAnimalObserva(rs.getInt("estadoanimalobserva"));
			    animal.setConfirmacionDiagnosticaAnimal(rs.getInt("confirmacionanimal"));
			    
			    tratamientoAntirrabico.setLavadoHerida(rs.getBoolean("lavadoherida"));
			    tratamientoAntirrabico.setSuturaHerida(rs.getBoolean("suturaherida"));
			    tratamientoAntirrabico.setAplicacionSuero(rs.getBoolean("aplicacionsuero"));
			    tratamientoAntirrabico.setFechaAplicacionSuero(rs.getString("fechaaplicacionsuero"));
			    tratamientoAntirrabico.setTipoSueroTratamiento(rs.getInt("tiposuerotratamiento"));
			    tratamientoAntirrabico.setCantidadSueroGlutea(rs.getInt("cantidadsueroglutea"));
			    tratamientoAntirrabico.setCantidadSueroHerida(rs.getInt("cantidadsueroherida"));
			    tratamientoAntirrabico.setNumeroLote(rs.getString("numerolote"));
			    tratamientoAntirrabico.setLaboratorioProductor(rs.getString("laboratorioproductor"));
			    tratamientoAntirrabico.setAplicarVacuna(rs.getBoolean("aplicarvacuna"));
			    tratamientoAntirrabico.setNumeroDosisTratamiento(rs.getInt("numerodosistratamiento"));
			    tratamientoAntirrabico.setTipoVacunaTratamiento(rs.getInt("tipovacunatratamiento"));
			    tratamientoAntirrabico.setFechaVacunaDosis1(rs.getString("fechavacunadosis1"));
			    tratamientoAntirrabico.setFechaVacunaDosis2(rs.getString("fechavacunadosis2"));
			    tratamientoAntirrabico.setFechaVacunaDosis3(rs.getString("fechavacunadosis3"));
			    tratamientoAntirrabico.setFechaVacunaDosis4(rs.getString("fechavacunadosis4"));
			    tratamientoAntirrabico.setFechaVacunaDosis5(rs.getString("fechavacunadosis5"));
			    tratamientoAntirrabico.setSuspensionTratamiento(rs.getBoolean("suspensiontratamiento"));
			    tratamientoAntirrabico.setRazonSuspension(rs.getInt("razonsuspension"));
			    tratamientoAntirrabico.setFechaTomaMuestraMuerte(rs.getString("fechatomamuestramuerte"));
			    tratamientoAntirrabico.setConfirmacionDiagnostica(rs.getBoolean("confirmaciondiagnostica"));
			    tratamientoAntirrabico.setPruebasLaboratorio(rs.getInt("pruebaslaboratorio"));
			    tratamientoAntirrabico.setReaccionesVacunaSuero(rs.getInt("reaccionesvacunasuero"));
			    tratamientoAntirrabico.setEvolucionPaciente(rs.getInt("evolucionpaciente"));
			}
        }
        catch (SQLException sqle) {
            
        	logger.error("Error obteniendo los datos de la ficha de accidente rabico : "+sqle.getMessage());
        }
    }
    
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaRabiaDao.insertarFichaCompleta(con,
										    		loginUsuario,
													codigoPaciente,
													codigoDiagnostico,
													estado,
													codigoConvenio,
													nombreProfesional,
												    								    
												    sire,
													sueroAntirrabico,
													tipoSuero,
												    cantidadAplicada,
												    fechaAplicacion,
												    vacunaAntirrabica,
												    tipoVacuna,
												    dosisAplicadas,
												    fechaUltimaDosis,
												    tipoAgresion,
												    provocada,
												    tipoLesion,
												    tipoExposicion,
												    cabeza,
												    cara,
												    cuello,
												    manos,
												    tronco,
												    extsuperiores,
												    extinferiores,
												    fechaAgresion,
												    confDiagnosticaCasoRabia,
												    fechaMuestraCasoRabia,
												    pais,
												    areaProcedencia,
												    
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
												    
													// Elementos del Animal Agresor        															    						   
												    animal.getEspecie(),
												    animal.getFechaInicioSintomas(),
												    animal.getFechaMuerte(),
												    animal.getFechaTomaMuestra(),
												    animal.getFuenteInformacionLaboratorio(),
												    animal.getVacunado(),
												    animal.getFechaUltimaDosisAnimal(),
												    animal.getNombrePropietario(),
												    animal.getDireccionPropietario(),
												    animal.getEstadoMomentoAgresion(),
												    animal.getUbicacionAnimal(),
												    animal.getNumeroDiasObserva(),
												    animal.getLugarObservacion(),
												    animal.getEstadoAnimalObserva(),
												    animal.getConfirmacionDiagnosticaAnimal(),
												    
												    
												    // Elementos del Tratamiento Antirrabico
												    tratamientoAntirrabico.isLavadoHerida(),
												    tratamientoAntirrabico.isSuturaHerida(),
												    tratamientoAntirrabico.isAplicacionSuero(),
												    tratamientoAntirrabico.getFechaAplicacionSuero(),
												    tratamientoAntirrabico.getTipoSueroTratamiento(),
												    tratamientoAntirrabico.getCantidadSueroGlutea(),
												    tratamientoAntirrabico.getCantidadSueroHerida(),
												    tratamientoAntirrabico.getNumeroLote(),
												    tratamientoAntirrabico.getLaboratorioProductor(),
												    tratamientoAntirrabico.isAplicarVacuna(),
												    tratamientoAntirrabico.getNumeroDosisTratamiento(),
												    tratamientoAntirrabico.getTipoVacunaTratamiento(),
												    tratamientoAntirrabico.getFechaVacunaDosis1(),
												    tratamientoAntirrabico.getFechaVacunaDosis2(),
												    tratamientoAntirrabico.getFechaVacunaDosis3(),
												    tratamientoAntirrabico.getFechaVacunaDosis4(),
												    tratamientoAntirrabico.getFechaVacunaDosis5(),
												    tratamientoAntirrabico.isSuspensionTratamiento(),
												    tratamientoAntirrabico.getRazonSuspension(),
												    tratamientoAntirrabico.getFechaTomaMuestraMuerte(),
												    tratamientoAntirrabico.isConfirmacionDiagnostica(),
												    tratamientoAntirrabico.getPruebasLaboratorio(),
												    activa,
												    profundidadLesion,
												    tratamientoAntirrabico.getReaccionesVacunaSuero(),
												    tratamientoAntirrabico.getEvolucionPaciente());
    }
    
    
    
    public int terminarFicha(Connection con)
	{
		return fichaRabiaDao.terminarFicha(con,codigoFichaRabia); 
	}
    
    
    /**
     * @return Returns the animal.
     */
    public Animal getAnimal() {
        return animal;
    }
    /**
     * @param animal The animal to set.
     */
    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
    /**
     * @return Returns the cantidadAplicada.
     */
    public int getCantidadAplicada() {
        return cantidadAplicada;
    }
    /**
     * @param cantidadAplicada The cantidadAplicada to set.
     */
    public void setCantidadAplicada(int cantidadAplicada) {
        this.cantidadAplicada = cantidadAplicada;
    }
    /**
     * @return Returns the codigoDepartamentoAgresion.
     */
    public String getCodigoDepartamentoAgresion() {
        return codigoDepartamentoAgresion;
    }
    /**
     * @param codigoDepartamentoAgresion The codigoDepartamentoAgresion to set.
     */
    public void setCodigoDepartamentoAgresion(String codigoDepartamentoAgresion) {
        this.codigoDepartamentoAgresion = codigoDepartamentoAgresion;
    }
    /**
     * @return Returns the codigoDiagnostico.
     */
    public String getCodigoDiagnostico() {
        return codigoDiagnostico;
    }
    /**
     * @param codigoDiagnostico The codigoDiagnostico to set.
     */
    public void setCodigoDiagnostico(String codigoDiagnostico) {
        this.codigoDiagnostico = codigoDiagnostico;
    }
    /**
     * @return Returns the codigoFichaRabia.
     */
    public int getCodigoFichaRabia() {
        return codigoFichaRabia;
    }
    /**
     * @param codigoFichaRabia The codigoFichaRabia to set.
     */
    public void setCodigoFichaRabia(int codigoFichaRabia) {
        this.codigoFichaRabia = codigoFichaRabia;
    }
    /**
     * @return Returns the codigoMunicipioAgresion.
     */
    public String getCodigoMunicipioAgresion() {
        return codigoMunicipioAgresion;
    }
    /**
     * @param codigoMunicipioAgresion The codigoMunicipioAgresion to set.
     */
    public void setCodigoMunicipioAgresion(String codigoMunicipioAgresion) {
        this.codigoMunicipioAgresion = codigoMunicipioAgresion;
    }
    /**
     * @return Returns the codigoPaciente.
     */
    public int getCodigoPaciente() {
        return codigoPaciente;
    }
    /**
     * @param codigoPaciente The codigoPaciente to set.
     */
    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }
    /**
     * @return Returns the direccion.
     */
    public String getDireccion() {
        return direccion;
    }
    /**
     * @param direccion The direccion to set.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    /**
     * @return Returns the dosisAplicadas.
     */
    public int getDosisAplicadas() {
        return dosisAplicadas;
    }
    /**
     * @param dosisAplicadas The dosisAplicadas to set.
     */
    public void setDosisAplicadas(int dosisAplicadas) {
        this.dosisAplicadas = dosisAplicadas;
    }
    /**
     * @return Returns the fechaAgresion.
     */
    public String getFechaAgresion() {
        return fechaAgresion;
    }
    /**
     * @param fechaAgresion The fechaAgresion to set.
     */
    public void setFechaAgresion(String fechaAgresion) {
        this.fechaAgresion = fechaAgresion;
    }
    /**
     * @return Returns the fechaAplicacion.
     */
    public String getFechaAplicacion() {
        return fechaAplicacion;
    }
    /**
     * @param fechaAplicacion The fechaAplicacion to set.
     */
    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }
    /**
     * @return Returns the fechaUltimaDosis.
     */
    public String getFechaUltimaDosis() {
        return fechaUltimaDosis;
    }
    /**
     * @param fechaUltimaDosis The fechaUltimaDosis to set.
     */
    public void setFechaUltimaDosis(String fechaUltimaDosis) {
        this.fechaUltimaDosis = fechaUltimaDosis;
    }
    /**
     * @return Returns the fichaRabiaDao.
     */
    public FichaRabiaDao getFichaRabiaDao() {
        return fichaRabiaDao;
    }
    /**
     * @param fichaRabiaDao The fichaRabiaDao to set.
     */
    public void setFichaRabiaDao(FichaRabiaDao fichaRabiaDao) {
        this.fichaRabiaDao = fichaRabiaDao;
    }
    /**
     * @return Returns the localizacionAnatomica.
     */
    public HashMap getLocalizacionAnatomica() {
        return localizacionAnatomica;
    }
    /**
     * @param localizacionAnatomica The localizacionAnatomica to set.
     */
    public void setLocalizacionAnatomica(HashMap localizacionAnatomica) {
        this.localizacionAnatomica = localizacionAnatomica;
    }
    /**
     * @return Returns the logger.
     */
    public Logger getLogger() {
        return logger;
    }
    /**
     * @param logger The logger to set.
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    /**
     * @return Returns the observacion.
     */
    public String getObservacion() {
        return observacion;
    }
    /**
     * @param observacion The observacion to set.
     */
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    /**
     * @return Returns the procedencia.
     */
    public String getProcedencia() {
        return procedencia;
    }
    /**
     * @param procedencia The procedencia to set.
     */
    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }
    /**
     * @return Returns the sueroAntirrabico.
     */
    public int getSueroAntirrabico() {
        return sueroAntirrabico;
    }
    /**
     * @param sueroAntirrabico The sueroAntirrabico to set.
     */
    public void setSueroAntirrabico(int sueroAntirrabico) {
        this.sueroAntirrabico = sueroAntirrabico;
    }
    /**
     * @return Returns the tipoAgresion.
     */
    public int getTipoAgresion() {
        return tipoAgresion;
    }
    /**
     * @param tipoAgresion The tipoAgresion to set.
     */
    public void setTipoAgresion(int tipoAgresion) {
        this.tipoAgresion = tipoAgresion;
    }
    /**
     * @return Returns the tipoExposicion.
     */
    public int getTipoExposicion() {
        return tipoExposicion;
    }
    /**
     * @param tipoExposicion The tipoExposicion to set.
     */
    public void setTipoExposicion(int tipoExposicion) {
        this.tipoExposicion = tipoExposicion;
    }
    /**
     * @return Returns the tipoLesion.
     */
    public int getTipoLesion() {
        return tipoLesion;
    }
    /**
     * @param tipoLesion The tipoLesion to set.
     */
    public void setTipoLesion(int tipoLesion) {
        this.tipoLesion = tipoLesion;
    }
    /**
     * @return Returns the tipoSuero.
     */
    public int getTipoSuero() {
        return tipoSuero;
    }
    /**
     * @param tipoSuero The tipoSuero to set.
     */
    public void setTipoSuero(int tipoSuero) {
        this.tipoSuero = tipoSuero;
    }
    /**
     * @return Returns the tipoVacuna.
     */
    public int getTipoVacuna() {
        return tipoVacuna;
    }
    /**
     * @param tipoVacuna The tipoVacuna to set.
     */
    public void setTipoVacuna(int tipoVacuna) {
        this.tipoVacuna = tipoVacuna;
    }
    /**
     * @return Returns the tipoVacunaSeguimiento.
     */
    public int getTipoVacunaSeguimiento() {
        return tipoVacunaSeguimiento;
    }
    /**
     * @param tipoVacunaSeguimiento The tipoVacunaSeguimiento to set.
     */
    public void setTipoVacunaSeguimiento(int tipoVacunaSeguimiento) {
        this.tipoVacunaSeguimiento = tipoVacunaSeguimiento;
    }
    /**
     * @return Returns the tratamientoAntirrabico.
     */
    public TratamientoAntirrabico getTratamientoAntirrabico() {
        return tratamientoAntirrabico;
    }
    /**
     * @param tratamientoAntirrabico The tratamientoAntirrabico to set.
     */
    public void setTratamientoAntirrabico(
            TratamientoAntirrabico tratamientoAntirrabico) {
        this.tratamientoAntirrabico = tratamientoAntirrabico;
    }
    /**
     * @return Returns the vacunaAntirrabica.
     */
    public int getVacunaAntirrabica() {
        return vacunaAntirrabica;
    }
    /**
     * @param vacunaAntirrabica The vacunaAntirrabica to set.
     */
    public void setVacunaAntirrabica(int vacunaAntirrabica) {
        this.vacunaAntirrabica = vacunaAntirrabica;
    }
    /**
     * @return Returns the estado.
     */
    public int getEstado() {
        return estado;
    }
    /**
     * @param estado The estado to set.
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }
    /**
     * @return Returns the codigoNotificacion.
     */
    public int getCodigoNotificacion() {
        return codigoNotificacion;
    }
    /**
     * @param codigoNotificacion The codigoNotificacion to set.
     */
    public void setCodigoNotificacion(int codigoNotificacion) {
        this.codigoNotificacion = codigoNotificacion;
    }
    /**
     * @return Returns the notificar.
     */
    public boolean isNotificar() {
        return notificar;
    }
    /**
     * @param notificar The notificar to set.
     */
    public void setNotificar(boolean notificar) {
        this.notificar = notificar;
    }
    /**
     * @return Returns the numeroSolicitud.
     */
    public int getNumeroSolicitud() {
        return numeroSolicitud;
    }
    /**
     * @param numeroSolicitud The numeroSolicitud to set.
     */
    public void setNumeroSolicitud(int numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }
    /**
     * @return Returns the loginUsuario.
     */
    public String getLoginUsuario() {
        return loginUsuario;
    }
    /**
     * @param loginUsuario The loginUsuario to set.
     */
    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }
    /**
     * @return Returns the fechaDiligenciamiento.
     */
    public String getFechaDiligenciamiento() {
        return fechaDiligenciamiento;
    }
    /**
     * @param fechaDiligenciamiento The fechaDiligenciamiento to set.
     */
    public void setFechaDiligenciamiento(String fechaDiligenciamiento) {
        this.fechaDiligenciamiento = fechaDiligenciamiento;
    }
    /**
     * @return Returns the horaDiligenciamiento.
     */
    public String getHoraDiligenciamiento() {
        return horaDiligenciamiento;
    }
    /**
     * @param horaDiligenciamiento The horaDiligenciamiento to set.
     */
    public void setHoraDiligenciamiento(String horaDiligenciamiento) {
        this.horaDiligenciamiento = horaDiligenciamiento;
    }
    /**
     * @return Returns the paciente.
     */
    public Paciente getPaciente() {
        return paciente;
    }
    /**
     * @param paciente The paciente to set.
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    


	public boolean isCabeza() {
		return cabeza;
	}


	public void setCabeza(boolean cabeza) {
		this.cabeza = cabeza;
	}


	public boolean isCara() {
		return cara;
	}


	public void setCara(boolean cara) {
		this.cara = cara;
	}


	public boolean isCuello() {
		return cuello;
	}


	public void setCuello(boolean cuello) {
		this.cuello = cuello;
	}


	public boolean isExtinferiores() {
		return extinferiores;
	}


	public void setExtinferiores(boolean extinferiores) {
		this.extinferiores = extinferiores;
	}


	public boolean isExtsuperiores() {
		return extsuperiores;
	}


	public void setExtsuperiores(boolean extsuperiores) {
		this.extsuperiores = extsuperiores;
	}


	public boolean isManos() {
		return manos;
	}


	public void setManos(boolean manos) {
		this.manos = manos;
	}


	public boolean isTronco() {
		return tronco;
	}


	public void setTronco(boolean tronco) {
		this.tronco = tronco;
	}


	public boolean isProvocada() {
		return provocada;
	}


	public void setProvocada(boolean provocada) {
		this.provocada = provocada;
	}


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
	}


	public String getDepartamentoNotifica() {
		return departamentoNotifica;
	}


	public void setDepartamentoNotifica(String departamentoNotifica) {
		this.departamentoNotifica = departamentoNotifica;
	}


	public String getFechaNotificacion() {
		return fechaNotificacion;
	}


	public void setFechaNotificacion(String fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}


	public int getInstitucionAtendio() {
		return institucionAtendio;
	}


	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
	}


	public String getMunicipioNotifica() {
		return municipioNotifica;
	}


	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
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


	public boolean isHospitalizado() {
		return hospitalizado;
	}


	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}


	public int getTipoCaso() {
		return tipoCaso;
	}


	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}


	public String getNombreProfesional() {
		return nombreProfesional;
	}


	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
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


	public int getCodigoConvenio() {
		return codigoConvenio;
	}


	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}


	public boolean isActiva() {
		return activa;
	}


	public void setActiva(boolean activa) {
		this.activa = activa;
	}


	public int getProfundidadLesion() {
		return profundidadLesion;
	}


	public void setProfundidadLesion(int profundidadLesion) {
		this.profundidadLesion = profundidadLesion;
	}


	public int getConfDiagnosticaCasoRabia() {
		return confDiagnosticaCasoRabia;
	}


	public void setConfDiagnosticaCasoRabia(int confDiagnosticaCasoRabia) {
		this.confDiagnosticaCasoRabia = confDiagnosticaCasoRabia;
	}


	public String getFechaMuestraCasoRabia() {
		return fechaMuestraCasoRabia;
	}


	public void setFechaMuestraCasoRabia(String fechaMuestraCasoRabia) {
		this.fechaMuestraCasoRabia = fechaMuestraCasoRabia;
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