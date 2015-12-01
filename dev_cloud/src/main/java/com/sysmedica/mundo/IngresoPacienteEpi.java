package com.sysmedica.mundo;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

import util.ConstantesIntegridadDominio;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.IngresoPacienteEpiDao;

public class IngresoPacienteEpi {

	private String tipoIdentificacion;
	private String numeroIdentificacion;
	
	private String primerApellido;
	private String segundoApellido;
	private String primerNombre;
	private String segundoNombre;
	private String fechaNacimiento;
	private int genero;
	private String municipioResidencia;
	private String departamentoResidencia;
	private String lugarResidencia;
	private int codigoBarrioResidencia;
	private String direccion;
	private String zonaDomicilio;
	private String telefono;
	private int ocupacion;
	private String tipoRegimen;
	private int aseguradora;
	private int etnia;
	private boolean desplazado;
	private int codigoEnfermedadNotificable;
	private int codigoPaciente;
	private String nuevoNumeroIdentificacion;
	private String nuevoTipoIdentificacion;
	private String estadoCivil;
	private String municipioNacimiento;
	private String departamentoNacimiento;
	private String lugarNacimiento;
	private boolean estaVivo;
	private String codigoDiagnostico;
	
	private String municipioIdentifica;
	private String departamentoIdentifica;
	private String lugarIdentifica;
	
	private int tipoSangre;
	private String grupoPoblacional;
	
	private int codigoInstitucion;
	
	private IngresoPacienteEpiDao ingresoPacienteEpiDao;
	

	private String paisExpedicion;
	private String paisNacimiento;
	private String paisResidencia;
	
	
	public void reset()
    {
		tipoIdentificacion = "";
		numeroIdentificacion = "";
		primerApellido = "";
		segundoApellido = "";
		primerNombre = "";
		segundoNombre = "";
		fechaNacimiento = "";
		direccion = "";
		zonaDomicilio = "";
		telefono = "";
		tipoRegimen = "";
	//	municipioResidencia = 0;
	//	departamentoResidencia = 0;
		codigoBarrioResidencia = -1;
	//	lugarResidencia = "21-11";
		ocupacion = 0;
		aseguradora = 0;
		etnia = 1;
		estaVivo = true;
	//	municipioNacimiento = 0;
	//	departamentoNacimiento = 0;
	//	lugarNacimiento = "21-11";
		estadoCivil = "";
		tipoSangre = 9;
		grupoPoblacional = ConstantesIntegridadDominio.acronimoOtrosGruposPoblacionales;
		
		codigoPaciente = 0;
		
		paisExpedicion = "";
		paisNacimiento = "";
		paisResidencia = "";
    }
	
	/**
	 * Inicializa el acceso a base de datos de este objeto
	 * @param tipoBD
	 * @return
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			ingresoPacienteEpiDao = myFactory.getIngresoPacienteEpiDao();
			wasInited = (ingresoPacienteEpiDao != null);
		}
		return wasInited;
	}
	
	
	public ResultSet consultaPaciente(Connection con)
	{
		ResultSet rs = ingresoPacienteEpiDao.consultaPaciente(con,numeroIdentificacion,tipoIdentificacion);
		
		try {
			
			if (rs.next()) {
				this.setCodigoPaciente(rs.getInt("codigo"));
				this.setPrimerApellido(rs.getString("primer_apellido"));
				this.setSegundoApellido(rs.getString("segundo_apellido"));
				this.setPrimerNombre(rs.getString("primer_nombre"));
				this.setSegundoNombre(rs.getString("segundo_nombre"));
				this.setFechaNacimiento(rs.getString("fecha_nacimiento"));
				this.setGenero(rs.getInt("sexo"));
				this.setMunicipioResidencia(rs.getString("ciu_vivienda"));
				this.setDepartamentoResidencia(rs.getString("dep_vivienda"));
				this.setMunicipioNacimiento(rs.getString("ciu_nacimiento"));
				this.setDepartamentoNacimiento(rs.getString("dep_nacimiento"));
				this.setCodigoBarrioResidencia(rs.getInt("barrio"));
				this.setDireccion(rs.getString("direccion_paciente"));
				this.setTelefono(rs.getString("telefono_paciente"));
				this.setOcupacion(rs.getInt("ocupacion"));
				this.setEtnia(rs.getInt("etnia"));
				this.setNumeroIdentificacion(rs.getString("numero_identificacion"));
				this.setTipoIdentificacion(rs.getString("tipoId"));
				this.setMunicipioIdentifica(rs.getString("ciuIdentifica"));
				this.setDepartamentoIdentifica(rs.getString("depIdentifica"));
				this.setTipoSangre(rs.getInt("tipoSangre"));
				this.setGrupoPoblacional(rs.getString("grupoPoblacional"));
				this.setEstaVivo(rs.getBoolean("estaVivo"));
				
				this.setPaisExpedicion(rs.getString("codigo_pais_id"));
				this.setPaisNacimiento(rs.getString("codigo_pais_nacimiento"));
				this.setPaisResidencia(rs.getString("codigo_pais_vivienda"));
			}
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		ResultSet rs2 = ingresoPacienteEpiDao.consultarConvenioPaciente(con,codigoPaciente);
		
		this.setAseguradora(0);
		
		try {
			
			if (rs2.next()) {
				this.setAseguradora(rs2.getInt("convenio_por_defecto"));
				
			}
		}
		catch (SQLException sqle) {
			
			sqle.printStackTrace();
		}
				
		return rs;
	}
	
	public int ingresarPaciente(Connection con)
	{
		return ingresoPacienteEpiDao.ingresarPaciente(con,
														primerApellido,
														segundoApellido,
														primerNombre,
														segundoNombre,
														fechaNacimiento,
														genero,
														municipioResidencia,
														departamentoResidencia,
														lugarResidencia,
														codigoBarrioResidencia,
														municipioNacimiento,
														departamentoNacimiento,
														lugarNacimiento,
														estadoCivil,
														estaVivo,
														direccion,
														zonaDomicilio,
														telefono,
														ocupacion,
														tipoRegimen,
														aseguradora,
														etnia,
														numeroIdentificacion,
														tipoIdentificacion,
														municipioIdentifica,
														departamentoIdentifica,
														lugarIdentifica,
														tipoSangre,
														grupoPoblacional,
														codigoInstitucion,
														paisExpedicion,
														paisNacimiento,
														paisResidencia);
	}
	
	
	
	public int modificarPaciente(Connection con)
	{
		return ingresoPacienteEpiDao.modificarPaciente(con,
														codigoPaciente,
														primerApellido,
														segundoApellido,
														primerNombre,
														segundoNombre,
														fechaNacimiento,
														genero,
														municipioResidencia,
														departamentoResidencia,
														lugarResidencia,
														codigoBarrioResidencia,
														municipioNacimiento,
														departamentoNacimiento,
														lugarNacimiento,
														estadoCivil,
														estaVivo,
														direccion,
														zonaDomicilio,
														telefono,
														ocupacion,
														tipoRegimen,
														aseguradora,
														etnia,
														numeroIdentificacion,
														tipoIdentificacion,
														nuevoNumeroIdentificacion,
														nuevoTipoIdentificacion,
														municipioIdentifica,
														departamentoIdentifica,
														lugarIdentifica,
														tipoSangre,
														grupoPoblacional,
														paisExpedicion,
														paisNacimiento,
														paisResidencia);
	}
	
	
	public ResultSet consultarConvenioPaciente(Connection con, int codigoPaciente)
	{
		return ingresoPacienteEpiDao.consultarConvenioPaciente(con,codigoPaciente);
	}
	
	
	public IngresoPacienteEpi()
	{
	    reset ();
		this.init (System.getProperty("TIPOBD"));
	}

	public int getAseguradora() {
		return aseguradora;
	}

	public void setAseguradora(int aseguradora) {
		this.aseguradora = aseguradora;
	}

	public int getCodigoBarrioResidencia() {
		return codigoBarrioResidencia;
	}

	public void setCodigoBarrioResidencia(int codigoBarrioResidencia) {
		this.codigoBarrioResidencia = codigoBarrioResidencia;
	}

	public int getCodigoEnfermedadNotificable() {
		return codigoEnfermedadNotificable;
	}

	public void setCodigoEnfermedadNotificable(int codigoEnfermedadNotificable) {
		this.codigoEnfermedadNotificable = codigoEnfermedadNotificable;
	}

	public String getDepartamentoResidencia() {
		return departamentoResidencia;
	}

	public void setDepartamentoResidencia(String departamentoResidencia) {
		this.departamentoResidencia = departamentoResidencia;
	}

	public boolean isDesplazado() {
		return desplazado;
	}

	public void setDesplazado(boolean desplazado) {
		this.desplazado = desplazado;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public int getEtnia() {
		return etnia;
	}

	public void setEtnia(int etnia) {
		this.etnia = etnia;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public int getGenero() {
		return genero;
	}

	public void setGenero(int genero) {
		this.genero = genero;
	}

	public IngresoPacienteEpiDao getIngresoPacienteEpiDao() {
		return ingresoPacienteEpiDao;
	}

	public void setIngresoPacienteEpiDao(IngresoPacienteEpiDao ingresoPacienteEpiDao) {
		this.ingresoPacienteEpiDao = ingresoPacienteEpiDao;
	}

	public String getMunicipioResidencia() {
		return municipioResidencia;
	}

	public void setMunicipioResidencia(String municipioResidencia) {
		this.municipioResidencia = municipioResidencia;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public int getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(int ocupacion) {
		this.ocupacion = ocupacion;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getTipoRegimen() {
		return tipoRegimen;
	}

	public void setTipoRegimen(String tipoRegimen) {
		this.tipoRegimen = tipoRegimen;
	}

	public String getZonaDomicilio() {
		return zonaDomicilio;
	}

	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}

	public String getLugarResidencia() {
		return lugarResidencia;
	}

	public void setLugarResidencia(String lugarResidencia) {
		this.lugarResidencia = lugarResidencia;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public String getNuevoNumeroIdentificacion() {
		return nuevoNumeroIdentificacion;
	}

	public void setNuevoNumeroIdentificacion(String nuevoNumeroIdentificacion) {
		this.nuevoNumeroIdentificacion = nuevoNumeroIdentificacion;
	}

	public String getNuevoTipoIdentificacion() {
		return nuevoTipoIdentificacion;
	}

	public void setNuevoTipoIdentificacion(String nuevoTipoIdentificacion) {
		this.nuevoTipoIdentificacion = nuevoTipoIdentificacion;
	}

	public String getDepartamentoNacimiento() {
		return departamentoNacimiento;
	}

	public void setDepartamentoNacimiento(String departamentoNacimiento) {
		this.departamentoNacimiento = departamentoNacimiento;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getLugarNacimiento() {
		return lugarNacimiento;
	}

	public void setLugarNacimiento(String lugarNacimiento) {
		this.lugarNacimiento = lugarNacimiento;
	}

	public String getMunicipioNacimiento() {
		return municipioNacimiento;
	}

	public void setMunicipioNacimiento(String municipioNacimiento) {
		this.municipioNacimiento = municipioNacimiento;
	}

	public boolean isEstaVivo() {
		return estaVivo;
	}

	public void setEstaVivo(boolean estaVivo) {
		this.estaVivo = estaVivo;
	}

	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}

	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}

	public String getDepartamentoIdentifica() {
		return departamentoIdentifica;
	}

	public void setDepartamentoIdentifica(String departamentoIdentifica) {
		this.departamentoIdentifica = departamentoIdentifica;
	}

	public String getLugarIdentifica() {
		return lugarIdentifica;
	}

	public void setLugarIdentifica(String lugarIdentifica) {
		this.lugarIdentifica = lugarIdentifica;
	}

	public String getMunicipioIdentifica() {
		return municipioIdentifica;
	}

	public void setMunicipioIdentifica(String municipioIdentifica) {
		this.municipioIdentifica = municipioIdentifica;
	}

	public int getTipoSangre() {
		return tipoSangre;
	}

	public void setTipoSangre(int tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public String getGrupoPoblacional() {
		return grupoPoblacional;
	}

	public void setGrupoPoblacional(String grupoPoblacional) {
		this.grupoPoblacional = grupoPoblacional;
	}

	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	public String getPaisExpedicion() {
		return paisExpedicion;
	}

	public void setPaisExpedicion(String paisExpedicion) {
		this.paisExpedicion = paisExpedicion;
	}

	public String getPaisNacimiento() {
		return paisNacimiento;
	}

	public void setPaisNacimiento(String paisNacimiento) {
		this.paisNacimiento = paisNacimiento;
	}

	public String getPaisResidencia() {
		return paisResidencia;
	}

	public void setPaisResidencia(String paisResidencia) {
		this.paisResidencia = paisResidencia;
	}
}
