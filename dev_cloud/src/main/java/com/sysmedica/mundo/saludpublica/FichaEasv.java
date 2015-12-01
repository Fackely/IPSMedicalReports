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
import com.sysmedica.dao.FichaDifteriaDao;
import com.sysmedica.dao.FichaEasvDao;

public class FichaEasv {

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
    
    private int codigoFichaEasv;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
	
    private int vacuna1;
    private int vacuna2;
    private int vacuna3;
    private int vacuna4;
    private int dosis1;
    private int dosis2;
    private int dosis3;
    private int dosis4;
    private int via1;
    private int via2;
    private int via3;
    private int via4;
    private int sitio1;
    private int sitio2;
    private int sitio3;
    private int sitio4;
    private String fechaVacunacion1;
    private String fechaVacunacion2;
    private String fechaVacunacion3;
    private String fechaVacunacion4;
    private String fabricante1;
    private String fabricante2;
    private String fabricante3;
    private String fabricante4;
    private String lote1;
    private String lote2;
    private String lote3;
    private String lote4;
    private String otroHallazgo;
    private String tiempo;
    private int unidadTiempo;
    private String lugarVacunacion;
    private String codDepVacunacion;
    private String codMunVacunacion;
    private int estadoSalud;
    private int recibiaMedicamentos;
    private String medicamentos;
    private int antPatologicos;
    private String cualesAntPatologicos;
    private int antAlergicos;
    private String cualesAntAlergicos;
    private int antEasv;
    private String cualesAntEasv;
    private int biologico1;
    private String fabricanteMuestra1;
    private String loteMuestra1;
    private String cantidadMuestra1;
    private String fechaEnvioMuestra1;
    private int biologico2;
    private String fabricanteMuestra2;
    private String loteMuestra2;
    private String cantidadMuestra2;
    private String fechaEnvioMuestra2;
    private int estadoFinal;
    private String telefonoContacto;
    
    private String lugarVac;
    
    private HashMap hallazgos;
    private HashMap vacunas;
    

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
    
    FichaEasvDao fichaEasvDao;
    
    public void reset()
    {
    	vacuna1 = 0;
        vacuna2 = 0;
        vacuna3 = 0;
        vacuna4 = 0;
        dosis1 = 0;
        dosis2 = 0;
        dosis3 = 0;
        dosis4 = 0;
        via1 = 0;
        via2 = 0;
        via3 = 0;
        via4 = 0;
        sitio1 = 0;
        sitio2 = 0;
        sitio3 = 0;
        sitio4 = 0;
        fechaVacunacion1 = "";
        fechaVacunacion2 = "";
        fechaVacunacion3 = "";
        fechaVacunacion4 = "";
        fabricante1 = "";
        fabricante2 = "";
        fabricante3 = "";
        fabricante4 = "";
        lote1 = "";
        lote2 = "";
        lote3 = "";
        lote4 = "";
        otroHallazgo = "";
        tiempo = "";
        unidadTiempo = 0;
        lugarVacunacion = "";
        codDepVacunacion = "";
        codMunVacunacion = "";
        estadoSalud = 0;
        recibiaMedicamentos = 0;
        medicamentos = "";
        antPatologicos = 0;
        cualesAntPatologicos = "";
        antAlergicos = 0;
        cualesAntAlergicos = "";
        antEasv = 0;
        cualesAntEasv = "";
        biologico1 = 0;
        fabricanteMuestra1 = "";
        loteMuestra1 = "";
        cantidadMuestra1 = "";
        fechaEnvioMuestra1 = "";
        biologico2 = 0;
        fabricanteMuestra2 = "";
        loteMuestra2 = "";
        cantidadMuestra2 = "";
        fechaEnvioMuestra2 = "";
        estadoFinal = 0;
        telefonoContacto = "";
        hallazgos = new HashMap();
        vacunas = new HashMap();
        
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
			fichaEasvDao = myFactory.getFichaEasvDao();
			wasInited = (fichaEasvDao != null);
		}
		return wasInited;
	}
    
    public FichaEasv()
    {
    	reset();
        init(System.getProperty("TIPOBD"));
    }
    
    
    
    
    public int insertarFichaCompleta(Connection con) 
    {
    	return fichaEasvDao.insertarFichaCompleta(con,
										    		numeroSolicitud,
													loginUsuario,
													codigoPaciente,
													codigoDiagnostico,
													estadoFicha,
													codigoConvenio,
													nombreProfesional,
												    
												    sire,
													notificar,
													
													codigoFichaEasv,										    
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
												    
												    vacuna1,
												    vacuna2,
												    vacuna3,
												    vacuna4,
												    dosis1,
												    dosis2,
												    dosis3,
												    dosis4,
												    via1,
												    via2,
												    via3,
												    via4,
												    sitio1,
												    sitio2,
												    sitio3,
												    sitio4,
												    fechaVacunacion1,
												    fechaVacunacion2,
												    fechaVacunacion3,
												    fechaVacunacion4,
												    fabricante1,
												    fabricante2,
												    fabricante3,
												    fabricante4,
												    lote1,
												    lote2,
												    lote3,
												    lote4,
												    otroHallazgo,
												    tiempo,
												    unidadTiempo,
												    lugarVacunacion,
												    codDepVacunacion,
												    codMunVacunacion,
												    estadoSalud,
												    recibiaMedicamentos,
												    medicamentos,
												    antPatologicos,
												    cualesAntPatologicos,
												    antAlergicos,
												    cualesAntAlergicos,
												    antEasv,
												    cualesAntEasv,
												    biologico1,
												    fabricanteMuestra1,
												    loteMuestra1,
												    cantidadMuestra1,
												    fechaEnvioMuestra1,
												    biologico2,
												    fabricanteMuestra2,
												    loteMuestra2,
												    cantidadMuestra2,
												    fechaEnvioMuestra2,
												    estadoFinal,
												    telefonoContacto,
												    
												    activa,
												    pais,
												    areaProcedencia,
												    
												    hallazgos,
												    vacunas,
												    lugarVac);
    }
    
    
    
    

    public int modificarFicha(Connection con)
    {
    	return fichaEasvDao.modificarFicha(con,
											sire,
											notificar,
										    loginUsuario,
										    codigoFichaEasv,
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
							
										    vacuna1,
										    vacuna2,
										    vacuna3,
										    vacuna4,
										    dosis1,
										    dosis2,
										    dosis3,
										    dosis4,
										    via1,
										    via2,
										    via3,
										    via4,
										    sitio1,
										    sitio2,
										    sitio3,
										    sitio4,
										    fechaVacunacion1,
										    fechaVacunacion2,
										    fechaVacunacion3,
										    fechaVacunacion4,
										    fabricante1,
										    fabricante2,
										    fabricante3,
										    fabricante4,
										    lote1,
										    lote2,
										    lote3,
										    lote4,
										    otroHallazgo,
										    tiempo,
										    unidadTiempo,
										    lugarVacunacion,
										    codDepVacunacion,
										    codMunVacunacion,
										    estadoSalud,
										    recibiaMedicamentos,
										    medicamentos,
										    antPatologicos,
										    cualesAntPatologicos,
										    antAlergicos,
										    cualesAntAlergicos,
										    antEasv,
										    cualesAntEasv,
										    biologico1,
										    fabricanteMuestra1,
										    loteMuestra1,
										    cantidadMuestra1,
										    fechaEnvioMuestra1,
										    biologico2,
										    fabricanteMuestra2,
										    loteMuestra2,
										    cantidadMuestra2,
										    fechaEnvioMuestra2,
										    estadoFinal,
										    telefonoContacto,
											pais,
										    areaProcedencia,
										    hallazgos,
										    vacunas,
										    lugarVac);
    }
    
    
    

    public void cargarPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	ResultSet rs = fichaEasvDao.consultarDatosPaciente(con,codigo,empezarnuevo);
    	
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
    	this.codigoFichaEasv = codigo;
    	
    	ResultSet rs = fichaEasvDao.consultaTodoFicha(con,codigo);
    	
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
			    
			    this.setOtroHallazgo(rs.getString("otroHallazgo"));
			    this.setTiempo(rs.getString("tiempo"));
			    this.setUnidadTiempo(rs.getInt("unidadTiempo"));
			    this.setLugarVacunacion(rs.getString("lugarVacunacion"));
			    this.setCodDepVacunacion(rs.getString("codDepVacunacion"));
			    this.setCodMunVacunacion(rs.getString("codMunVacunacion"));
			    this.setEstadoSalud(rs.getInt("estadoSalud"));
			    this.setRecibiaMedicamentos(rs.getInt("recibiaMedicamentos"));
			    this.setMedicamentos(rs.getString("medicamentos"));
			    this.setAntPatologicos(rs.getInt("antPatologicos"));
			    this.setCualesAntPatologicos(rs.getString("cualesAntPatologicos"));
			    this.setAntAlergicos(rs.getInt("antAlergicos"));
			    this.setCualesAntAlergicos(rs.getString("cualesAntAlergicos"));
			    this.setAntEasv(rs.getInt("antEasv"));
			    this.setCualesAntEasv(rs.getString("cualesAntEasv"));
			    this.setBiologico1(rs.getInt("biologico1"));
			    this.setFabricanteMuestra1(rs.getString("fabricanteMuestra1"));
			    this.setLoteMuestra1(rs.getString("loteMuestra1"));
			    this.setCantidadMuestra1(rs.getString("cantidadMuestra1"));
			    this.setFechaEnvioMuestra1(rs.getString("fechaEnvioMuestra1"));
			    this.setBiologico2(rs.getInt("biologico2"));
			    this.setFabricanteMuestra2(rs.getString("fabricanteMuestra2"));
			    this.setLoteMuestra2(rs.getString("loteMuestra2"));
			    this.setCantidadMuestra2(rs.getString("cantidadMuestra2"));
			    this.setFechaEnvioMuestra2(rs.getString("fechaEnvioMuestra2"));
			    this.setEstadoFinal(rs.getInt("estadoFinal"));
			    this.setTelefonoContacto(rs.getString("telefonoContacto"));
			    
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
			    
			    ResultSet rs2 = fichaEasvDao.consultarHallazgos(con,codigo);
			    
			    HashMap hallazgosSemi = new HashMap();
			    
			    while (rs2.next()) {
			    	
			    	hallazgosSemi.put("hallazgo_"+rs2.getInt("codigoHallazgo"),"true");
			    }
			    
			    this.setHallazgos(hallazgosSemi);
			    
			    
			    
			    
			    ResultSet rs3 = fichaEasvDao.consultarVacunas(con,codigo);
			    
			    HashMap vacunasImplicadas = new HashMap();
			    
			    int i=0;
			    
			    while (rs3.next()) {
			    	
			    	vacunasImplicadas.put("vacuna_"+Integer.toString(i),rs3.getString("vacuna"));
			    	vacunasImplicadas.put("dosis_"+Integer.toString(i),rs3.getString("dosis"));
			    	vacunasImplicadas.put("via_"+Integer.toString(i),rs3.getString("via"));
			    	vacunasImplicadas.put("sitio_"+Integer.toString(i),rs3.getString("sitio"));
			    	vacunasImplicadas.put("fecha_"+Integer.toString(i),rs3.getString("fechavacunacion"));
			    	vacunasImplicadas.put("fabricante_"+Integer.toString(i),rs3.getString("fabricante"));
			    	vacunasImplicadas.put("lote_"+Integer.toString(i),rs3.getString("lote"));
			    	
			    	i++;
			    }
			    
			    this.setVacunas(vacunasImplicadas);
			    
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


	public int getAntAlergicos() {
		return antAlergicos;
	}


	public void setAntAlergicos(int antAlergicos) {
		this.antAlergicos = antAlergicos;
	}


	public int getAntEasv() {
		return antEasv;
	}


	public void setAntEasv(int antEasv) {
		this.antEasv = antEasv;
	}


	public int getAntPatologicos() {
		return antPatologicos;
	}


	public void setAntPatologicos(int antPatologicos) {
		this.antPatologicos = antPatologicos;
	}


	public int getAreaProcedencia() {
		return areaProcedencia;
	}


	public void setAreaProcedencia(int areaProcedencia) {
		this.areaProcedencia = areaProcedencia;
	}


	public int getBiologico1() {
		return biologico1;
	}


	public void setBiologico1(int biologico1) {
		this.biologico1 = biologico1;
	}


	public int getBiologico2() {
		return biologico2;
	}


	public void setBiologico2(int biologico2) {
		this.biologico2 = biologico2;
	}


	public String getCantidadMuestra1() {
		return cantidadMuestra1;
	}


	public void setCantidadMuestra1(String cantidadMuestra1) {
		this.cantidadMuestra1 = cantidadMuestra1;
	}


	public String getCantidadMuestra2() {
		return cantidadMuestra2;
	}


	public void setCantidadMuestra2(String cantidadMuestra2) {
		this.cantidadMuestra2 = cantidadMuestra2;
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


	public String getCodDepVacunacion() {
		return codDepVacunacion;
	}


	public void setCodDepVacunacion(String codDepVacunacion) {
		this.codDepVacunacion = codDepVacunacion;
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


	public int getCodigoFichaEasv() {
		return codigoFichaEasv;
	}


	public void setCodigoFichaEasv(int codigoFichaEasv) {
		this.codigoFichaEasv = codigoFichaEasv;
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


	public String getCodMunVacunacion() {
		return codMunVacunacion;
	}


	public void setCodMunVacunacion(String codMunVacunacion) {
		this.codMunVacunacion = codMunVacunacion;
	}


	public String getCualesAntAlergicos() {
		return cualesAntAlergicos;
	}


	public void setCualesAntAlergicos(String cualesAntAlergicos) {
		this.cualesAntAlergicos = cualesAntAlergicos;
	}


	public String getCualesAntEasv() {
		return cualesAntEasv;
	}


	public void setCualesAntEasv(String cualesAntEasv) {
		this.cualesAntEasv = cualesAntEasv;
	}


	public String getCualesAntPatologicos() {
		return cualesAntPatologicos;
	}


	public void setCualesAntPatologicos(String cualesAntPatologicos) {
		this.cualesAntPatologicos = cualesAntPatologicos;
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


	public int getDosis1() {
		return dosis1;
	}


	public void setDosis1(int dosis1) {
		this.dosis1 = dosis1;
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


	public int getDosis4() {
		return dosis4;
	}


	public void setDosis4(int dosis4) {
		this.dosis4 = dosis4;
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


	public int getEstadoFinal() {
		return estadoFinal;
	}


	public void setEstadoFinal(int estadoFinal) {
		this.estadoFinal = estadoFinal;
	}


	public int getEstadoSalud() {
		return estadoSalud;
	}


	public void setEstadoSalud(int estadoSalud) {
		this.estadoSalud = estadoSalud;
	}


	public boolean isEstaVivo() {
		return estaVivo;
	}


	public void setEstaVivo(boolean estaVivo) {
		this.estaVivo = estaVivo;
	}


	public String getFabricante1() {
		return fabricante1;
	}


	public void setFabricante1(String fabricante1) {
		this.fabricante1 = fabricante1;
	}


	public String getFabricante2() {
		return fabricante2;
	}


	public void setFabricante2(String fabricante2) {
		this.fabricante2 = fabricante2;
	}


	public String getFabricante3() {
		return fabricante3;
	}


	public void setFabricante3(String fabricante3) {
		this.fabricante3 = fabricante3;
	}


	public String getFabricante4() {
		return fabricante4;
	}


	public void setFabricante4(String fabricante4) {
		this.fabricante4 = fabricante4;
	}


	public String getFabricanteMuestra1() {
		return fabricanteMuestra1;
	}


	public void setFabricanteMuestra1(String fabricanteMuestra1) {
		this.fabricanteMuestra1 = fabricanteMuestra1;
	}


	public String getFabricanteMuestra2() {
		return fabricanteMuestra2;
	}


	public void setFabricanteMuestra2(String fabricanteMuestra2) {
		this.fabricanteMuestra2 = fabricanteMuestra2;
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


	public String getFechaEnvioMuestra1() {
		return fechaEnvioMuestra1;
	}


	public void setFechaEnvioMuestra1(String fechaEnvioMuestra1) {
		this.fechaEnvioMuestra1 = fechaEnvioMuestra1;
	}


	public String getFechaEnvioMuestra2() {
		return fechaEnvioMuestra2;
	}


	public void setFechaEnvioMuestra2(String fechaEnvioMuestra2) {
		this.fechaEnvioMuestra2 = fechaEnvioMuestra2;
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


	public String getFechaVacunacion1() {
		return fechaVacunacion1;
	}


	public void setFechaVacunacion1(String fechaVacunacion1) {
		this.fechaVacunacion1 = fechaVacunacion1;
	}


	public String getFechaVacunacion2() {
		return fechaVacunacion2;
	}


	public void setFechaVacunacion2(String fechaVacunacion2) {
		this.fechaVacunacion2 = fechaVacunacion2;
	}


	public String getFechaVacunacion3() {
		return fechaVacunacion3;
	}


	public void setFechaVacunacion3(String fechaVacunacion3) {
		this.fechaVacunacion3 = fechaVacunacion3;
	}


	public String getFechaVacunacion4() {
		return fechaVacunacion4;
	}


	public void setFechaVacunacion4(String fechaVacunacion4) {
		this.fechaVacunacion4 = fechaVacunacion4;
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


	public String getLoginUsuario() {
		return loginUsuario;
	}


	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}


	public String getLote1() {
		return lote1;
	}


	public void setLote1(String lote1) {
		this.lote1 = lote1;
	}


	public String getLote2() {
		return lote2;
	}


	public void setLote2(String lote2) {
		this.lote2 = lote2;
	}


	public String getLote3() {
		return lote3;
	}


	public void setLote3(String lote3) {
		this.lote3 = lote3;
	}


	public String getLote4() {
		return lote4;
	}


	public void setLote4(String lote4) {
		this.lote4 = lote4;
	}


	public String getLoteMuestra1() {
		return loteMuestra1;
	}


	public void setLoteMuestra1(String loteMuestra1) {
		this.loteMuestra1 = loteMuestra1;
	}


	public String getLoteMuestra2() {
		return loteMuestra2;
	}


	public void setLoteMuestra2(String loteMuestra2) {
		this.loteMuestra2 = loteMuestra2;
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


	public String getLugarVacunacion() {
		return lugarVacunacion;
	}


	public void setLugarVacunacion(String lugarVacunacion) {
		this.lugarVacunacion = lugarVacunacion;
	}


	public String getMedicamentos() {
		return medicamentos;
	}


	public void setMedicamentos(String medicamentos) {
		this.medicamentos = medicamentos;
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


	public String getOtroHallazgo() {
		return otroHallazgo;
	}


	public void setOtroHallazgo(String otroHallazgo) {
		this.otroHallazgo = otroHallazgo;
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


	public int getRecibiaMedicamentos() {
		return recibiaMedicamentos;
	}


	public void setRecibiaMedicamentos(int recibiaMedicamentos) {
		this.recibiaMedicamentos = recibiaMedicamentos;
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


	public int getSitio1() {
		return sitio1;
	}


	public void setSitio1(int sitio1) {
		this.sitio1 = sitio1;
	}


	public int getSitio2() {
		return sitio2;
	}


	public void setSitio2(int sitio2) {
		this.sitio2 = sitio2;
	}


	public int getSitio3() {
		return sitio3;
	}


	public void setSitio3(int sitio3) {
		this.sitio3 = sitio3;
	}


	public int getSitio4() {
		return sitio4;
	}


	public void setSitio4(int sitio4) {
		this.sitio4 = sitio4;
	}


	public String getTelefonoContacto() {
		return telefonoContacto;
	}


	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}


	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}


	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}


	public String getTiempo() {
		return tiempo;
	}


	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}


	public int getTipoCaso() {
		return tipoCaso;
	}


	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}


	public int getUnidadTiempo() {
		return unidadTiempo;
	}


	public void setUnidadTiempo(int unidadTiempo) {
		this.unidadTiempo = unidadTiempo;
	}


	public int getVacuna1() {
		return vacuna1;
	}


	public void setVacuna1(int vacuna1) {
		this.vacuna1 = vacuna1;
	}


	public int getVacuna2() {
		return vacuna2;
	}


	public void setVacuna2(int vacuna2) {
		this.vacuna2 = vacuna2;
	}


	public int getVacuna3() {
		return vacuna3;
	}


	public void setVacuna3(int vacuna3) {
		this.vacuna3 = vacuna3;
	}


	public int getVacuna4() {
		return vacuna4;
	}


	public void setVacuna4(int vacuna4) {
		this.vacuna4 = vacuna4;
	}


	public int getVia1() {
		return via1;
	}


	public void setVia1(int via1) {
		this.via1 = via1;
	}


	public int getVia2() {
		return via2;
	}


	public void setVia2(int via2) {
		this.via2 = via2;
	}


	public int getVia3() {
		return via3;
	}


	public void setVia3(int via3) {
		this.via3 = via3;
	}


	public int getVia4() {
		return via4;
	}


	public void setVia4(int via4) {
		this.via4 = via4;
	}


	public HashMap getHallazgos() {
		return hallazgos;
	}


	public void setHallazgos(HashMap hallazgos) {
		this.hallazgos = hallazgos;
	}


	public HashMap getVacunas() {
		return vacunas;
	}


	public void setVacunas(HashMap vacunas) {
		this.vacunas = vacunas;
	}


	public String getLugarVac() {
		return lugarVac;
	}


	public void setLugarVac(String lugarVac) {
		this.lugarVac = lugarVac;
	}
    
}
