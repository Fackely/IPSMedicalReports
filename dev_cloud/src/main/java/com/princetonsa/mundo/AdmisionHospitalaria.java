package com.princetonsa.mundo;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import util.TipoNumeroId;
import util.UtilidadBD;

import com.princetonsa.dao.AdmisionHospitalariaDao;
import com.princetonsa.dao.DaoFactory;
import org.apache.log4j.Logger;

/**
 * 
 *
 * @version 1.0, Mar 4, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class AdmisionHospitalaria extends Admision
{
	private int codigoAdmisionHospitalaria;
	private int codigoCama;
	private String nombreCama = "";
	private int estadoCama;
	private String habitacion;
	private String tipoHabitacion;
	private String piso;
	private String nombreCentroCostoCama = "";
	private String descripcionCama="";
	private String nombreTipoUsuarioCama;
	private int codigoEstado;
	private String nombreEstado = "";
	private String codigoDiagnostico = "";
	private String codigoCIEDiagnostico = "";
	private String nombreDiagnostico = "";
	
	
	/**
	 * Habitación de la cama
	 */
	
	
	private AdmisionHospitalariaDao admisionHospitalariaDao = null;
	private static Logger logger = Logger.getLogger(AdmisionHospitalaria.class);
	
	/**
	 *	 Colección que guarda los datos de la cama actual en la que está el paciente
	 */
	private Collection datosCamaActual;
	
	public AdmisionHospitalaria()	{
		init(System.getProperty("TIPOBD"));
	}

	public AdmisionHospitalaria(int origen, int codigoPersona, String numeroIdMedico, String tipoIdMedico, int codigoCama, String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna, /*String numeroAutorizacion, */UsuarioBasico usuario, String cuenta, String hora, String fecha)
	{	  
		PersonaBasica medico = new PersonaBasica(numeroIdMedico, tipoIdMedico);
		medico.setCodigoPersona(codigoPersona);
		this.setCodigoOrigen(origen);
		this.setMedico(medico);
		this.codigoCama = codigoCama;
		this.codigoDiagnostico = codigoDiagnostico;
		this.codigoCIEDiagnostico = codigoCIEDiagnostico;
		this.setCodigoCausaExterna(codigoCausaExterna);
		//this.setNumeroAutorizacion(numeroAutorizacion);
		this.setLoginUsuario(usuario);	
		this.setIdCuenta(cuenta);
		this.setHora(hora);
		this.setFecha(fecha);
		init(System.getProperty("TIPOBD"));
	}
	
	public AdmisionHospitalaria(int origen, String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna/*, String numeroAutorizacion*/)
	{
		this.setCodigoOrigen(origen);
		this.codigoDiagnostico = codigoDiagnostico;
		this.codigoCIEDiagnostico = codigoCIEDiagnostico;
		this.setCodigoCausaExterna(codigoCausaExterna);
		//this.setNumeroAutorizacion(numeroAutorizacion);
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( admisionHospitalariaDao == null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			admisionHospitalariaDao = myFactory.getAdmisionHospitalariaDao();
			if( admisionHospitalariaDao != null )
				return true;
		}
		
		return false;
	}

	/**
	 * Inserta los datos del objeto en una fuente de datos, reutilizando una conexión existente,
	 * con la información presente en los atributos del objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @return número de filas insertadas (1 o 0)
	 */
	public int insertar(Connection con) throws SQLException
	{
		int posibleCodigo=this.admisionHospitalariaDao.insertar(con, this.getCodigoOrigen(), this.getMedico().getCodigoPersona(), this.codigoCama, this.codigoDiagnostico, this.codigoCIEDiagnostico, this.getCodigoCausaExterna(), /*this.getNumeroAutorizacion(), */this.getLoginUsuario(), this.getIdCuenta(), this.getHora(), this.getFecha());
		if (posibleCodigo>0)
		{
			codigoAdmisionHospitalaria=posibleCodigo;
		}
		else
		{
			codigoAdmisionHospitalaria=0;
		}
		return codigoAdmisionHospitalaria;
	}

	/**
	 * Inserta los datos del objeto en una fuente de datos, reutilizando una conexión existente,
	 * con la información presente en los atributos del objeto.
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return número de filas insertadas (1 o 0)
	 */
	public int insertarTransaccional(Connection con, String estado,int institucion) 
	{
		//int posibleCodigo=this.admisionHospitalariaDao.insertarTransaccional(con, this.getCodigoOrigen(), this.getMedico().getNumeroIdentificacionPersona(), this.getMedico().getCodigoTipoIdentificacionPersona(), this.codigoCama, this.codigoDiagnostico, this.codigoCIEDiagnostico, this.getCodigoCausaExterna(), this.getNumeroAutorizacion(), this.getLoginUsuario(), this.getIdCuenta(), this.getHora(), this.getFecha(), estado);
		int posibleCodigo = 0;
		try 
		{
			posibleCodigo = this.admisionHospitalariaDao.insertarTransaccional(con, this.getCodigoOrigen(), this.getMedico().getCodigoPersona(), this.codigoCama, this.codigoDiagnostico, this.codigoCIEDiagnostico, this.getCodigoCausaExterna(), /*this.getNumeroAutorizacion(), */this.getLoginUsuario(), this.getIdCuenta(), this.getHora(), this.getFecha(), estado, institucion);
		} catch (SQLException e) 
		{
			logger.error("error en insertarTransaccional: "+e);
		}
		if (posibleCodigo>0)
		{
			codigoAdmisionHospitalaria=posibleCodigo;
		}
		else
		{
			codigoAdmisionHospitalaria=0;
		}
		return codigoAdmisionHospitalaria;
	}
	
	/**
	 * Carga un objeto desde una fuente de datos, buscando por
	 * el id suministrado. Establece los atributos del objeto con
	 * los datos cargados.
	 * @param con una conexion abierta con una fuente de datos 
	 * @param id pareja tipo/numero, correspondiente a la PK en una tabla
	 * necesaria0.+ 0  para identificar de manera única el objeto que se desea
	 * cargar. Si la PK sólo depende de un atributo, por convención éste se
	 * denomina 'tipo'.
	 * @param tipoBD El tipo de la BD (necesario para inicializar el usuario)
	 */
	public void cargar(Connection con)
	{
		try
		{
		
			if(admisionHospitalariaDao == null ) init(System.getProperty("TIPOBD"));
			logger.info("CODIGO DE LA ADMISION HOSPITALARIA: "+this.codigoAdmisionHospitalaria);
			ResultSetDecorator rs=admisionHospitalariaDao.cargar(con, this.codigoAdmisionHospitalaria );
			if (rs.next())
			{
				codigoAdmisionHospitalaria=rs.getInt("codigoAdmisionHospitalaria");
				super.setCodigo(codigoAdmisionHospitalaria);
				super.setIdCuenta(rs.getInt("codigoCuenta"));
				codigoCama=rs.getInt("codigoCama");
				nombreCama=rs.getString("nombreCama");
				estadoCama = rs.getInt("estadoCama");
				nombreCentroCostoCama=rs.getString("nombreCentroCostoCama");
				descripcionCama=rs.getString("descripcionCama");
				nombreTipoUsuarioCama=rs.getString("tipoUsuarioCama");
				codigoEstado=rs.getInt("codigoEstado");
				nombreEstado=rs.getString("nombreEstado");
				codigoDiagnostico=rs.getString("codigoDiagnostico");
				codigoCIEDiagnostico=rs.getString("codigoCIEDiagnostico");
				nombreDiagnostico=rs.getString("nombreDiagnostico");
				setCodigoOrigen(rs.getInt("codigoOrigenAdmision"));
				this.setCodigoCausaExterna(rs.getInt("codigoCausaExterna"));
				this.setCausaExterna(rs.getString("causaExterna"));
				this.setFecha(rs.getString("fechaAdmision"));
				this.setHora(rs.getString("horaAdmision"));
				this.setOrigen(rs.getString("origenAdmision"));
				this.setCodigoOrigen(rs.getInt("codigoOrigenAdmision"));
				//this.setNumeroAutorizacion(rs.getString("numeroAutorizacion"));
				UsuarioBasico usuario=new UsuarioBasico();
				usuario.init(System.getProperty("TIPOBD"));
				usuario.cargarUsuarioBasico(con, rs.getString("loginUsuario"));		
				PersonaBasica medico = new PersonaBasica(rs.getString("numeroIdentificacionMedico"), rs.getString("tipoIdentificacionMedico"));
				medico.setNombrePersona(rs.getString("primerNombreMedico")+" "+rs.getString("segundoNombreMedico")+" "+rs.getString("primerApellidoMedico")+" "+rs.getString("segundoApellidoMedico"));
				this.setMedico(medico);
				this.setLoginUsuario(usuario);
				//this.setNumeroAutorizacion(rs.getString("numeroAutorizacion"));
				this.setHabitacion(rs.getString("habitacion"));
				this.setTipoHabitacion(rs.getString("tipo_habitacion"));
				this.setPiso(rs.getString("piso"));
			}
			else
			{
				logger.warn("No existe la admisión hospitalaria solicitada");
				//UtilidadBD.closeConnection(con);
			}
		}
		catch(SQLException e)
		{
			logger.error("No se cargo la admision hospitalaria: " +e);
		}
		
	}

	/**
	 * Modifica un objeto almacenado en una fuente de datos, reutilizando una conexion existente,
	 * con los datos presentes en los atributos del objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @param id pareja tipo/numero, correspondiente a la PK en una tabla necesaria para
	 * identificar de manera única el objeto que se desea modificar. Si la PK sólo depende de
	 * un atributo, por convención éste se denomina 'tipo'.
	 * @return número de filas modificadas (1 o 0)
	 */
	public int modificar(Connection con, TipoNumeroId id) throws SQLException
	{
		return this.admisionHospitalariaDao.modificar(con,this.getCodigoOrigen(), this.codigoDiagnostico, this.codigoCIEDiagnostico, this.getCodigoCausaExterna(),/*this.getNumeroAutorizacion(), */Integer.parseInt(id.getNumeroId()) );
	}
	
	/**
	 * Este método actualiza la admisión hospitalaria de acuerdo a las reglas
	 * que imperan el egreso. Estas reglas son:
	 * 1. Pasar la cama usada a estado de desinfección.
	 * 2. Cambiar el estado de la admisión a egresada
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoAdmision Código de la admisión que se va a afectar
	 * con el egreso
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int actualizarPorEgresoTransaccional (Connection con, int codigoAdmision, String estado, int institucion) throws SQLException
	{
		this.codigoAdmisionHospitalaria=codigoAdmision;
		this.setCodigo(codigoAdmision);
		return this.admisionHospitalariaDao.pasarAdmisionAEgresoTransaccional(con, codigoAdmisionHospitalaria, estado, institucion);
	}

	/**
	 * Este método actualiza la admisión hospitalaria de acuerdo a las reglas
	 * que imperan la reversión del egreso. Estas reglas son:
	 * 
	 * 1. Pasar la cama seleccionada a estado de ocupada.
	 * 2. Cambiar el estado de la admisión a hospitalizada
	 * 3. Actualizar la cama en la admisión
	 * 
	 * Todo esto debe ser en modo transaccional
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */	
	public int actualizarPorReversionEgresoTransaccional(Connection con, String estado) throws SQLException
	{
		return this.admisionHospitalariaDao.reversarEgresoYAdmisionTransaccional(con, this.getIdCuenta(), this.getCodigoCama(), estado);
	}

	/**
	 * Returns the codigoDiagnostico.
	 * @return String
	 */
	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}

	
	/**
	 * Returns the nombreCama.
	 * @return String
	 */
	public String getNombreCama() {
		return nombreCama;
	}

	/**
	 * Returns the nombreCentroCostoCama.
	 * @return String
	 */
	public String getNombreCentroCostoCama() {
		return nombreCentroCostoCama;
	}

	/**
	 * Returns the nombreDiagnostico.
	 * @return String
	 */
	public String getNombreDiagnostico() {
		return nombreDiagnostico;
	}

	/**
	 * Returns the nombreEstado.
	 * @return String
	 */
	public String getNombreEstado() {
		return nombreEstado;
	}

	
	/**
	 * Sets the codigoDiagnostico.
	 * @param codigoDiagnostico The codigoDiagnostico to set
	 */
	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}

	
	/**
	 * Sets the nombreCama.
	 * @param nombreCama The nombreCama to set
	 */
	public void setNombreCama(String nombreCama) {
		this.nombreCama = nombreCama;
	}

	/**
	 * Sets the nombreCentroCostoCama.
	 * @param nombreCentroCostoCama The nombreCentroCostoCama to set
	 */
	public void setNombreCentroCostoCama(String nombreCentroCostoCama) {
		this.nombreCentroCostoCama = nombreCentroCostoCama;
	}

	/**
	 * Sets the nombreDiagnostico.
	 * @param nombreDiagnostico The nombreDiagnostico to set
	 */
	public void setNombreDiagnostico(String nombreDiagnostico) {
		this.nombreDiagnostico = nombreDiagnostico;
	}

	/**
	 * Sets the nombreEstado.
	 * @param nombreEstado The nombreEstado to set
	 */
	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	/**
	 * Returns the codigoCIEDiagnostico.
	 * @return String
	 */
	public String getCodigoCIEDiagnostico() {
		return codigoCIEDiagnostico;
	}

	/**
	 * Sets the codigoAdmisionHospitalaria.
	 * @param codigoAdmisionHospitalaria The codigoAdmisionHospitalaria to set
	 */
	public void setCodigoAdmisionHospitalaria(int codigoAdmisionHospitalaria) {
		this.codigoAdmisionHospitalaria = codigoAdmisionHospitalaria;
	}

	/**
	 * Sets the codigoCama.
	 * @param codigoCama The codigoCama to set
	 */
	public void setCodigoCama(int codigoCama) {
		this.codigoCama = codigoCama;
	}

	/**
	 * Sets the codigoCIEDiagnostico.
	 * @param codigoCIEDiagnostico The codigoCIEDiagnostico to set
	 */
	public void setCodigoCIEDiagnostico(String codigoCIEDiagnostico) {
		this.codigoCIEDiagnostico = codigoCIEDiagnostico;
	}

	/**
	 * Sets the codigoEstado.
	 * @param codigoEstado The codigoEstado to set
	 */
	public void setCodigoEstado(int codigoEstado) {
		this.codigoEstado = codigoEstado;
	}

	/**
	 * Returns the codigoAdmisionHospitalaria.
	 * @return int
	 */
	public int getCodigoAdmisionHospitalaria() {
		return codigoAdmisionHospitalaria;
	}

	/**
	 * Returns the codigoCama.
	 * @return int
	 */
	public int getCodigoCama() {
		return codigoCama;
	}

	/**
	 * Returns the codigoEstado.
	 * @return int
	 */
	public int getCodigoEstado() {
		return codigoEstado;
	}

	/**
	 * Returns the descripcionCama.
	 * @return String
	 */
	public String getDescripcionCama() {
		return descripcionCama;
	}

	/**
	 * Returns the nombreTipoUsuarioCama.
	 * @return String
	 */
	public String getNombreTipoUsuarioCama() {
		return nombreTipoUsuarioCama;
	}

	/**
	 * Sets the descripcionCama.
	 * @param descripcionCama The descripcionCama to set
	 */
	public void setDescripcionCama(String descripcionCama) {
		this.descripcionCama = descripcionCama;
	}

	/**
	 * Sets the nombreTipoUsuarioCama.
	 * @param nombreTipoUsuarioCama The nombreTipoUsuarioCama to set
	 */
	public void setNombreTipoUsuarioCama(String nombreTipoUsuarioCama) {
		this.nombreTipoUsuarioCama = nombreTipoUsuarioCama;
	}

	/**
	 * Returns the estadoCama.
	 * @return String
	 */
	public int getEstadoCama()
	{
		return estadoCama;
	}

	/**
	 * Sets the estadoCama.
	 * @param estadoCama The estadoCama to set
	 */
	public void setEstadoCama(int estadoCama)
	{
		this.estadoCama = estadoCama;
	}

	/**
	 * @return Returns the habitacion.
	 */
	public String getHabitacion()
	{
		return habitacion;
	}
	/**
	 * @param habitacion The habitacion to set.
	 */
	public void setHabitacion(String habitacion)
	{
		this.habitacion = habitacion;
	}
	/**
	 * @return Returns the datosCamaActual.
	 */
	public Collection getDatosCamaActual()
	{
		return datosCamaActual;
	}
	/**
	 * @param datosCamaActual The datosCamaActual to set.
	 */
	public void setDatosCamaActual(Collection datosCamaActual)
	{
		this.datosCamaActual = datosCamaActual;
	}
	public int cambiarCama(Connection con, int cama, int cuenta)
	{
		return this.admisionHospitalariaDao.cambiarCama(con, cama, cuenta);
	}
	
	public int actualizarMedico(Connection con, int medico, int cuenta)
	{
		return this.admisionHospitalariaDao.actualizarMedico(con, medico, cuenta);
	}
	
	public String cargarUltimaFechaHoraRegistroCama(Connection con, int codigoPaciente) throws SQLException
	{
		return this.admisionHospitalariaDao.cargarUltimaFechaHoraRegistroCama(con, codigoPaciente);
	}
	
	/**
	 * Método que obtiene la información de la cama actual en la que está el paciente
	 * @param con
	 * @param codigoAdmision
	 * @param codigoPaciente
	 * @return
	 */
	public static String[] getCama(Connection con, int codigoAdmision, int codigoPaciente)
	{
		try
		{		
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAdmisionHospitalariaDao().getCama(con, codigoAdmision, codigoPaciente);
		}
		catch (SQLException sql)
		{
			logger.warn("Error consultando el nombre de la cama en admision hospitalaria.\n"+sql);
			String[] a={"-1","-1","-1"};
			return a;
		}
	}
	
	public void cargarUltimaAdmision(Connection con, int codigoPaciente) throws SQLException
	{
		ResultSetDecorator resultado = this.admisionHospitalariaDao.cargarUltimaAdmision(con, codigoPaciente);
		
		if( resultado.next() )
		{
			codigoAdmisionHospitalaria=resultado.getInt("codigoAdmisionHospitalaria");
			super.setCodigo(codigoAdmisionHospitalaria);
			setCodigoCama(resultado.getInt("codigoCama"));
			setNombreCama(resultado.getString("nombreCama"));
			setEstadoCama(resultado.getInt("estadoCama"));
			setCodigoEstado(resultado.getInt("codigoEstado"));
			setFecha(resultado.getString("fechaAdmision"));
			setHora(resultado.getString("horaAdmision"));
			//setNumeroAutorizacion(resultado.getString("numeroautorizacion"));
		}
	}

	public void cargar(Connection con, TipoNumeroId id) throws SQLException
	{
		this.codigoAdmisionHospitalaria=Integer.parseInt(id.getNumeroId());
		this.cargar(con);
	}	
	
	/**
	 * Método para obtener los datos de la cama actual en la que está el paciente 
	 * @param con
	 * @param codigoPaciente
	 * @return Collection -> Con los datos de la cama actual del paciente
	 */
	public Collection consultarDatosCamaActual(Connection con, int codigoPaciente)
	{
		AdmisionHospitalariaDao admisionHospitalariaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAdmisionHospitalariaDao();
		Collection coleccion=null;
		try
		{	
			coleccion = admisionHospitalariaDao.consultarDatosCamaActual(con, codigoPaciente);
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar la información de la cama actual del paciente (AdmisionHospitalaria)"+" " +e.toString());
		  coleccion=null;
		}
		return coleccion;	
	}

	/**
	 * @return the piso
	 */
	public String getPiso() {
		return piso;
	}

	/**
	 * @param piso the piso to set
	 */
	public void setPiso(String piso) {
		this.piso = piso;
	}

	/**
	 * @return the tipoHabitacion
	 */
	public String getTipoHabitacion() {
		return tipoHabitacion;
	}

	/**
	 * @param tipoHabitacion the tipoHabitacion to set
	 */
	public void setTipoHabitacion(String tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}
}
