/*
 * @(#)UsuarioBasico.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

import java.io.Serializable;
import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.MD5Hash;
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import util.UtilidadValidacion;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.UsuarioBasicoDao;

/**
 * Esta clase es una version reducida de <code>Usuario</code>, con los datos minimos para identificar
 * de manera unica un usuario y permitirle cambiar su password.
 *
 * @version 1.0, Oct 1, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class UsuarioBasico implements Observer, Serializable, HttpSessionBindingListener {

	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Manejador de logs de la clase
	 */
	private transient static Logger logger=Logger.getLogger(UsuarioBasico.class);
	/**
	 * Código de la persona
	 */
	private int codigoPersona;

	/**
	 * El nombre del usuario en el sistema
	 */
	private String nombreUsuario;

	/**
	 * El login del usuario en el sistema.
	 */
	private String loginUsuario;

	/**
	 * El password del usuario en el sistema.
	 */
	private String passwordUsuario;

	/**
	 * Numero de identificacion de la persona.
	 */
	private String numeroIdentificacion;

	/**
	 * Codigo del tipo de identificacion de la persona.
	 */
	private String codigoTipoIdentificacion;

	/**
	 * Numero registro Medico (Si existe, - si no. No tiene set pues solo al
	 * cargarlo se cambia)
	 */
	private String numeroRegistroMedico = "-";

	/**
	 * Arreglo con las especialidades médicas de este usuario (Si es médico.  No
	 * tiene set pues solo al cargarlo se cambia)
	 */
	private InfoDatosInt especialidades[];

	/**
	 * Codigo de la institución a la que pertenece el usuario
	 */
	private String codigoInstitucion;

	/**
	 * Nombre de la institución a la que pertenece el usuario
	 */
	private String institucion;
	
	/**
	 * Nit de la instituciòn en la que  se està trabajando
	 */
	private String nit;
	
	/**
	 * 
	 */
	private String digitoVerificacion;
	
	/**
	 * Nombre de la ocupación médica de este profesional de la salud.
	 */
	private String ocupacionMedica;

	/**
	 * Código de la ocupación médica de este profesional de la salud.
	 */
	private int codigoOcupacionMedica =0;

	/**
	 * Código del centro de salud al que pertenece el profesional.
	 */
	private int codigoCentroCosto = 0;

	/**
	 * Centro de costo al que pertenece.
	 */
	private String centroCosto;
	
	/**
	 * Centro de Atención
	 */
	private String centroAtencion;
	
	/**
	 * Código del centro de atencion
	 */
	private int codigoCentroAtencion;
	
	
	private int tipoReporte;
	
	/**
	 * Mapa donde se almacenan los centros de costo del usuario
	 */
	private HashMap centrosCosto;
	
	/**
	 * Número de registros que hay en el mapa de Centros de Costo
	 */
	private int numCentrosCosto;
	
	

	/**
	 * Este boolean indica si este usuario tiene como rol
	 * únicamente el de paciente
	 */
	private boolean esSoloPaciente=false;

	/**
	 * El DAO usado por el objeto <code>UsuarioBasico</code> para acceder a la fuente de datos.
	 */
	private static UsuarioBasicoDao usuarioBasicoDao = null;

	/**
	 * Observable al cual está registrado este Observer.
	 */
	private Observable observable;
	
	/**
	 * Variable que almacena el consecutivo de la caja a la cual se encuentra asociado un usuario.
	 */
	private int consecutivoCaja=ConstantesBD.codigoNuncaValido;
	
	/**
	 * Variable que almacena el codigo de la caja a la cual se encuentra asociado un usuario.
	 */
	private int codigoCaja=ConstantesBD.codigoNuncaValido;
	
	/**
	 * Variable para manejar la descripcion de la caja, a la que se encuentra relacionado el medico
	 */
	private String descripcionCaja="";
	
	/**
	 * Atributo vinculado con informacion de epidemiología
	 */
	private String codigoUPGD;

	/**
	 * nombre de la firma digital almacenada en /web/upload/firmasDigitales/ 
	 */
	private String firmaDigital;
	
	/**
	 * nombre del cargo que pose el usuario 
	 */
	private String nombreCargo;
	
	/**
	 * codigo del cargo del usuario
	 * */
	private int codigoCargo;
	
	/**
	 * esProfesionalSalud 
	 */
	private String esProfesionalSalud;
	
	/**
	 * Constructor vacio, necesario para poder usar esta clase como un JavaBean
	 */
	public UsuarioBasico() {
		clean();
		init(System.getProperty("TIPOBD"));
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) {

		if (usuarioBasicoDao == null) {
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			usuarioBasicoDao = myFactory.getUsuarioBasicoDao();
		}

	}

	/**
	 * Retorna el codigo de la institucion a la cual pertenece este usuario.
	 * @return String el codigo de la institucion
	 */
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Retorna el codigo de la institucion a la cual pertenece este usuario.
	 * @return int el codigo de la institucion
	 */
	public int getCodigoInstitucionInt() {
		try
		{
			return Integer.parseInt(codigoInstitucion);
		}
		catch(NumberFormatException e)
		{
			return 0;
		}
	}

	/**
	 * Retorna el codigo del tipo de identificacion.
	 * @return el codigo del tipo de identificacion
	 */
	public String getCodigoTipoIdentificacion() {
		return codigoTipoIdentificacion;
	}

	/**
	 * Retorna el login del usuario basico.
	 * @return el login del usuario basico
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * Retorna el numero de identificacion.
	 * @return el numero de identificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * Returns the especialidades.
	 * @return String[]
	 */
	public InfoDatosInt[] getEspecialidades() {
		return especialidades;
	}

	/**
	 * @return
	 */
	public String getOcupacionMedica() {
		return ocupacionMedica;
	}

	/**
	 * @return
	 */
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @return
	 */
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * Retorna el numero de registro del medico (Si no es un médico o no lo
	 * tiene retorna un -).
	 * @return String
	 */
	public String getNumeroRegistroMedico() {
		return numeroRegistroMedico;
	}

	/**
	 * Returns the nombreUsuario.
	 * @return String
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * @param string
	 */
	public void setOcupacionMedica(String string) {
		ocupacionMedica = string;
	}

	/**
	 * @param string
	 */
	public void setCentroCosto(String string) {
		centroCosto = string;
	}

	/**
	 * @param i
	 */
	public void setCodigoCentroCosto(int i) {
		codigoCentroCosto = i;
	}

	/**
	 * Sets the nombreUsuario.
	 * @param nombreUsuario The nombreUsuario to set
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * Observable al cual está registrado este Observer.
	 * @param observable observable de este observer
	 */
	public void setObservable(Observable observable) {
		this.observable = observable;
	}

	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos del usuario basico.
	 * Notese que es <i>private</i>, solamente se debe llamar desde esta clase.
	 */
	private void clean()
	{
		this.codigoPersona=0;
		this.loginUsuario = "";
		this.passwordUsuario = "";
		this.numeroIdentificacion = "";
		this.codigoTipoIdentificacion = "";
		this.codigoInstitucion = "";
		this.nit="";
		this.institucion="";
		this.digitoVerificacion="";
		this.numeroRegistroMedico = "-";
		this.ocupacionMedica = "";
		this.codigoOcupacionMedica=0;
		
		this.centroCosto = "";
		this.centroAtencion = "";
		this.codigoCentroAtencion = 0;
		this.codigoCentroCosto = 0;
		this.centrosCosto = new HashMap();
		this.numCentrosCosto = 0;
		
		this.especialidades = new InfoDatosInt[0];
		this.nombreUsuario = "";
		this.esSoloPaciente=false;

		this.codigoUPGD = "";
		
		this.esProfesionalSalud = ConstantesBD.acronimoNo;
	}

	/**
	 * Implementación de la interfaz Observer, sirve para avisarle
	 * a una instancia de este objeto que debe re-cargar sus datos
	 * desde la fuente de datos.
	 * @param o Objeto observado
	 * @param arg tipo y número de identificación del usuario básico
	 * que cambió
	 */
	public void update(Observable o, Object arg) {


		Integer tmp=new Integer(Utilidades.convertirAEntero(arg+""));
		int codigoCambiado=tmp.intValue();

		//if (id.getTipoId().equals(this.codigoTipoIdentificacion) && id.getNumeroId().equals(this.numeroIdentificacion)) {

			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

			try {

				Connection con = myFactory.getConnection();

				if (codigoCambiado==this.codigoPersona)
				{		
				    //CAMBIO CARGAR SOLO DESDE LOGIN
					//cargarUsuarioBasico(con, this.codigoPersona);
				    cargarUsuarioBasico(con, this.loginUsuario);
				}
				if (con!=null&&!con.isClosed())
				{
					UtilidadBD.closeConnection(con);
				}

			}	catch (SQLException e) {
			}

		//}

	}

	/**
	 * Listener que le indica a este objeto cuándo fue añadido a una sesión.
	 * @param event objeto que encapsula este evento
	 */
	public void valueBound(HttpSessionBindingEvent event) {
	}

	/**
	 * Listener que le indica a este objeto cuándo fue removido de una sesión.
	 * @param event objeto que encapsula este evento
	 */
	public void valueUnbound(HttpSessionBindingEvent event) {

		// Cuando me remueven de un session, bien sea explícitamente (session.removeAttribute()),
		// por un logout (session.invalidate()), o por timeout, me des-registro como Observer

		if (observable != null) {
			observable.deleteObserver(this);
		}

	}

	/**
	* Este metodo accede a una fuente de datos y, dado un código de persona, recupera los datos
	* básicos de un usuario, almacenándolos en una instancia esta clase.
	* @param ac_con				Conexión abierta con una fuente de datos
	* @param ai_codigoPersona	Código usuario que se desea cargar
	*/
	public void cargarUsuarioBasico(Connection ac_con, int ai_codigoPersona)throws SQLException
	{
		ResultSetDecorator	lrs_rs;
		String		ls_s;

		if(usuarioBasicoDao == null)
			usuarioBasicoDao =
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getUsuarioBasicoDao();

		lrs_rs = new ResultSetDecorator(usuarioBasicoDao.cargarUsuarioBasico(ac_con, ai_codigoPersona));

		if(lrs_rs.next() )
		{
			clean();

			ls_s = lrs_rs.getString("nombreCompleto");

			this.codigoInstitucion			= lrs_rs.getString("codigoInstitucion");
			this.codigoPersona				= lrs_rs.getInt("codigoPersona");
			this.codigoTipoIdentificacion	= lrs_rs.getString("codigoTipoIdentificacion");
			this.loginUsuario				= lrs_rs.getString("loginUsuario");
			this.nombreUsuario				= (ls_s != null ? ls_s.trim(): "");
			this.numeroIdentificacion		= lrs_rs.getString("numeroIdentificacion");
			this.passwordUsuario			= lrs_rs.getString("passwordUsuario");
			this.nombreCargo			    = lrs_rs.getString("nombre_cargo");
			this.codigoCargo			    = lrs_rs.getInt("codigo_cargo");

			this.esSoloPaciente = usuarioBasicoDao.esUsuarioSoloPaciente(ac_con, loginUsuario);

			//se cargan los centros de costo del usuario
			this.cargarCentrosCostoUsuario(ac_con);
			//**************************************
			cargarNumeroRegistroMedico(ac_con);
			cargarNombreInstitucion(ac_con);
			cargarCajaUsuario(ac_con);
			
			// ******************** ANTECEDENTES DE ALERTA ****************************************
			// VERIFICAR QUE EL USUARIO QUE ESTA LOGEADO SEA PROFESIONAL DE LA SALUD
			this.setEsProfesionalSalud(UtilidadValidacion.esProfesionalSalud(this.codigoPersona, 
					Utilidades.convertirAEntero(this.codigoInstitucion),
					this.numeroRegistroMedico)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			// ******************** FIN ANTECEDENTES DE ALERTA ************************************
		}
		UtilidadBD.cerrarObjetosPersistencia(null, lrs_rs, null);
	}

	/**
	 * Este metodo accede a una fuente de datos y, dado un login, recupera los datos basicos de
	 * un usuario, almacenándolos en una instancia esta clase.
	 * @param login login del usuario que se desea cargar
	 */
	public void cargarUsuarioBasico(String login) throws SQLException 
	{
		Connection con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getConnection();
		cargarUsuarioBasico(con, login);
		cargarNombreInstitucion(con);
		UtilidadBD.closeConnection(con);
	}
	
	/**
	 * Este metodo accede a una fuente de datos y, dado un login, recupera los datos basicos de
	 * un usuario, almacenándolos en una instancia esta clase.
	 * @param con una conexion abierta con una fuente de datos
	 * @param login login del usuario que se desea cargar
	 */
	public void cargarUsuarioBasico(Connection con, String login) throws SQLException {

		if(usuarioBasicoDao == null)
			usuarioBasicoDao =
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getUsuarioBasicoDao();

		ResultSetDecorator rs = null;
		try
		{
			rs = new ResultSetDecorator(usuarioBasicoDao.cargarUsuarioBasico(con, login));
			if (rs.next()) {
				clean();
				this.codigoPersona=rs.getInt("codigoPersona");
				this.loginUsuario = rs.getString("loginUsuario");
				this.passwordUsuario = rs.getString("passwordUsuario");
				this.numeroIdentificacion = rs.getString("numeroIdentificacion");
				this.codigoTipoIdentificacion = rs.getString("codigoTipoIdentificacion");
				this.codigoInstitucion = rs.getString("codigoInstitucion");
				logger.info("CODIGO DE LA INSTITUCION DEL USUARIO: "+this.codigoInstitucion);
				this.nombreUsuario = (rs.getString("nombreCompleto") != null) ? rs.getString("nombreCompleto").trim() : "";
				this.nombreCargo = rs.getString("nombre_cargo");
				this.codigoCargo = rs.getInt("codigo_cargo");
				
				//se cargan los centros de costo del usuario*******
				this.cargarCentrosCostoUsuario(con);
				//*************************************************
				
				if (usuarioBasicoDao.esUsuarioSoloPaciente(con, loginUsuario))
				{
					this.esSoloPaciente=true;
				}
				else
				{
					this.esSoloPaciente=false;
				}
				//Slogger.info("mapa centros costo: "+this.getCentrosCosto());
				//Se verifica si el usuario solo tiene un centro de costo
				if(this.getNumCentrosCosto()==1)
				{
					this.setCodigoCentroCosto(Integer.parseInt(this.getCentrosCosto("codigo_0").toString()));
					this.setCentroCosto(this.getCentrosCosto("nombre_0").toString());
					this.setCentroAtencion(this.getCentrosCosto("nombrecentroatencion_0").toString());
					this.setCodigoCentroAtencion(Integer.parseInt(this.getCentrosCosto("codigocentroatencion_0").toString()));
					this.setCodigoUPGD(Utilidades.getCodigoUPGDCentroAtencion(con, this.getCodigoCentroAtencion()));
					cargarCajaUsuario(con);
				}
			}
	
			cargarNumeroRegistroMedico(con);
			cargarNombreInstitucion(con);
			
			// ******************** ANTECEDENTES DE ALERTA ****************************************
			// VERIFICAR QUE EL USUARIO QUE ESTA LOGEADO SEA PROFESIONAL DE LA SALUD
			this.setEsProfesionalSalud(UtilidadValidacion.esProfesionalSalud(this.codigoPersona, 
					Utilidades.convertirAEntero(this.codigoInstitucion),
					this.numeroRegistroMedico)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		}catch (Exception e) {
			throw new SQLException(e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(null, rs, null);	
		}
		
		// ******************** FIN ANTECEDENTES DE ALERTA ************************************
	}

	/**
	 * Este metodo accede a una fuente de datos y, dado su tipo y número de
	 * identificación, recupera los datos basicos de
	 * un usuario, almacenándolos en una instancia esta clase.
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoId tipo de identificación del usuario
	 * @param numeroId número de identificación del usuario
	 */
	public void cargarUsuarioBasico(Connection con, String tipoId, String numeroId) throws SQLException {

		clean();

		ResultSetDecorator rs = new ResultSetDecorator(usuarioBasicoDao.cargarUsuarioBasico(con, tipoId, numeroId));

		if (rs.next())
		{
			this.codigoPersona=rs.getInt("codigoPersona");
			this.loginUsuario = rs.getString("loginUsuario");
			this.passwordUsuario = rs.getString("passwordUsuario");
			this.numeroIdentificacion = rs.getString("numeroIdentificacion");
			this.codigoTipoIdentificacion = rs.getString("codigoTipoIdentificacion");
			this.codigoInstitucion = rs.getString("codigoInstitucion");
			this.nombreUsuario = (rs.getString("nombreCompleto") != null) ? rs.getString("nombreCompleto").trim() : "";
			this.nombreCargo = rs.getString("nombre_cargo");
			this.codigoCargo = rs.getInt("codigo_cargo");
			
			//se cargan los centros de costo del usuario
			this.cargarCentrosCostoUsuario(con);
			//***************************************************
			
			if (usuarioBasicoDao.esUsuarioSoloPaciente(con, loginUsuario))
			{
				this.esSoloPaciente=true;
			}
			else
			{
				this.esSoloPaciente=false;
			}
			cargarCajaUsuario(con);
		}
		
		cargarNumeroRegistroMedico(con);
		cargarNombreInstitucion(con);

		// ******************** ANTECEDENTES DE ALERTA ****************************************
		// VERIFICAR QUE EL USUARIO QUE ESTA LOGEADO SEA PROFESIONAL DE LA SALUD
		this.setEsProfesionalSalud(UtilidadValidacion.esProfesionalSalud(this.codigoPersona, 
				Utilidades.convertirAEntero(this.codigoInstitucion),
				this.numeroRegistroMedico)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		// ******************** FIN ANTECEDENTES DE ALERTA ************************************
		UtilidadBD.cerrarObjetosPersistencia(null, rs, null);
	}

	/**
	 * Versión sobrecargada del método anterior, toma una conexión del pool y la devuelve al finalizar.
	 * Accede a una fuente de datos y, dado su tipo y número de identificación, recupera los datos basicos de
	 * un usuario, almacenándolos en una instancia esta clase.
	 * @param tipoId tipo de identificación del usuario
	 * @param numeroId número de identificación del usuario
	 */
	public void cargarUsuarioBasico(String tipoId, String numeroId) throws SQLException {
		Connection con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
		cargarUsuarioBasico(con, tipoId, numeroId);
		cargarNombreInstitucion(con);
		UtilidadBD.closeConnection(con);
	}
	
	/**
	 * Método implementado para cargar los centros de costo
	 * del usuario
	 * @param con
	 */
	private void cargarCentrosCostoUsuario(Connection con)
	{
		/**
		 * Las llaves del mapa son
		 * codigo_ : codigo del centro de costo
		 * nombre_ : nombre del centro de costo
		 * codigocentroatencion_ :  codigo del centro de atencion
		 * nombrecentroatencion_ :  nombre del centro de atencion
		 */
		this.setCentrosCosto(usuarioBasicoDao.cargarCentrosCostoUsuario(con,this.loginUsuario));
		//Utilidades.imprimirMapa(this.getCentrosCosto());
		
		this.setNumCentrosCosto(Integer.parseInt(this.getCentrosCosto("numRegistros").toString()));
	}
	
	public void cargarNombreInstitucion(Connection con)
	{
		if(this.codigoInstitucion!=null)
		{
			ResultSetDecorator resultado= null;
			try
			{
				resultado=new ResultSetDecorator(usuarioBasicoDao.cargarNombreInstitucion(con, codigoInstitucion));
				if(resultado.next())
				{
					institucion=resultado.getString(1);
					nit=resultado.getString(2);
					digitoVerificacion=resultado.getString(3);
				}
			}
			catch (SQLException e)
			{
				logger.error("Error cargando la institución del usuario "+e);
			}finally{
				UtilidadBD.cerrarObjetosPersistencia(null, resultado, null);
			}
		}
	}

	/**
	 * Carga el número de registro médico, si aplica.
	 * @param con Conexión abierta con la fuente de datos
	 */
	public void cargarNumeroRegistroMedico(Connection con) throws SQLException
	{
		//Como no se cuantas especialidades tenga, utilizo un arrayList para
		//guardarlas

		ArrayList arrayEspecialidades = new ArrayList();

		//Solo lo tengo que cargar cuando no tenga un valor
		if (numeroRegistroMedico.equals("-"))
		{
			ResultSetDecorator rs = new ResultSetDecorator(usuarioBasicoDao.cargarDatosMedico(con, this.codigoPersona));
			try {
				if (rs.next())
				{
					numeroRegistroMedico = rs.getString("numeroRegistroMedico");
					ocupacionMedica = rs.getString("ocupacionMedica");
					codigoOcupacionMedica= rs.getInt("codigoOcupacionMedica");
					
					firmaDigital=rs.getString("firmadigital");
					
					//EL CC SE MANTENDRA DEL USUARIO CARGADO DESDE EL LOGIN DEBIDO A QUE EL MEDICO
					//PODRA TENER VARIOS USER EN DIF CC.
					//codigoCentroCosto = rs.getInt("codigoCentroCosto");
					//centroCosto = rs.getString("centroCosto");
					//No se pide la especialidad para el primer registro

				}
				while (rs.next()) {
					InfoDatosInt sigElemento=new InfoDatosInt();
					sigElemento.setCodigo(rs.getInt("codigoEspecialidad"));
					sigElemento.setNombre(rs.getString("especialidad"));
					sigElemento.setActivo(rs.getBoolean("especialidadActiva"));
					arrayEspecialidades.add(sigElemento);
				}

				//Ahora recorremos el arrayList y lo guardamos
				//en el arreglo de especialidades

				if (arrayEspecialidades.size() > 0) {
					especialidades = new InfoDatosInt[arrayEspecialidades.size()];
					for (int i = 0; i < especialidades.length; i++)
					{
						especialidades[i]=(InfoDatosInt) arrayEspecialidades.get(i);
					}
				}
			} catch (Exception e) {
				
			}finally{
				if(rs!=null){
					try {
						rs.close();
						
					}catch(SQLException sqlException){
						logger.error("Error al cerrar el recurso", sqlException);
					}
				}
			}
		}

	}

	/**
	 * Cambia, en la fuente de datos, el password del usuario actual.
	 * @param oldPassword contraseña vieja del usuario
	 * @param newPassword contraseña nueva del usuario
	 * @return <b>true</b> si se pudo cambiar la contraseña, <b>false</b> si no
	 */
	public boolean cambiarPassword(String oldPassword, String newPassword,String usuActual) throws SQLException {

		if (!((MD5Hash.hashPassword(oldPassword)).equals(passwordUsuario))) {
			return false;
		}
		else {
			String newHashedPwd = usuarioBasicoDao.cambiarPassword(loginUsuario, newPassword,usuActual);
			if (newHashedPwd != null && !newHashedPwd.trim().equals("")) {
				this.passwordUsuario = newHashedPwd;
				return true;
			}
			else {
				return false;
			}
		}

	}

	/**
	 * Cambia el password de un usuario dado. Este método es llamado únicamente
	 * desde la funcionalidad "administrador - cambiar contraseña", ya que cambia el password de "cualquiera"
	 * que tenga el login dado.
	 * @param con conexión abierta con la base de datos
	 * @param login el login del usuario del cual se desea cambiar el password
	 * @param password el nuevo password del usuario
	 * @param codigoInstitucion código de la institución a la que pertenece el usuario (necesaria para validaciones)
	 * @return <code>RespuestaValidacion</code> indicando si se pudo cambiar el password, y un mensaje.
	 */
	public static RespuestaValidacion cambiarPasswordValidacion(Connection con, String login, String password, String codigoInstitucion,String usuActual) throws SQLException {

		RespuestaValidacion resp1 = UtilidadValidacion.validacionCambiarPasswordAdministrador(con, login, codigoInstitucion,password);

		if (resp1.puedoSeguir) {

			if (usuarioBasicoDao == null) {
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				usuarioBasicoDao = myFactory.getUsuarioBasicoDao();
			}

			if (usuarioBasicoDao.cambiarPassword(login, password, true,usuActual)) {
				return new RespuestaValidacion("El password fue cambiado exitosamente", true);
			}

			else {
				return new RespuestaValidacion("Problemas cambiando el password", false);
			}

		}

		else {
			return resp1;
		}

	}
	/**
	 * adición sebastián
	 * Método que lista todos los usuarios que hacen parte de un mismo 
	 * profesional
	 * @param con
	 * @param tipoId
	 * @param numeroId
	 * @return
	 */
	public Collection cargarUsuariosMismoProfesional(Connection con, String tipoId, String numeroId) 
	{
		return usuarioBasicoDao.cargarUsuariosMismoProfesional(con,tipoId,numeroId,"");
	}

	/**
	 * adición sebastián
	 * Método que lista todos los usuarios activos/inactivos que hacen parte de un mismo 
	 * profesional o usuarios del sistema 
	 * @param con
	 * @param tipoId
	 * @param numeroId
	 * @param activo (si es true sólo consultará los usuarios activos y
	 * 		si es false consultará los usuarios sin importar si son activos o no
	 * @return
	 */
	public Collection cargarUsuariosMismoProfesional(Connection con, String tipoId, String numeroId,boolean activo) 
	{
		String cadenaActivo="";
		//se verifica si sólo se desea consultar los activos
		if(activo)
			cadenaActivo="true";
		return usuarioBasicoDao.cargarUsuariosMismoProfesional(con,tipoId,numeroId,cadenaActivo);
	}
	
	/**
	 * adición sebastián
	 * Método que lista todos los usuarios activos/inactivos que hacen parte de un mismo 
	 * profesional o usuarios del sistema 
	 * @param con
	 * @param codigoMedico
	 * @param activo (si es true sólo consultará los usuarios activos y
	 * 		si es false consultará los usuarios sin importar si son activos o no
	 * @return
	 */
	public Collection cargarUsuariosMismoProfesional(Connection con, int codigoMedico,String activo) 
	{
		try
		{
			this.cargarUsuarioBasico(con,codigoMedico);
			return usuarioBasicoDao.cargarUsuariosMismoProfesional(con,this.codigoTipoIdentificacion,this.numeroIdentificacion,activo);
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarUsuariosMismoProfesional de UsuarioBasico: "+e);
			return new ArrayList();
		}
	}
	
	
	/**
	 * Método que devuelve un String con la información general
	 * del médico (si es médico)
	 * @return
	 */
	public String getInformacionGeneralPersonalSalud()
	{
	    String informacionGeneralStr = new String();
		InfoDatosInt especialidadesMedico[]=this.getEspecialidades();

		if (this.numeroRegistroMedico!=null&&!this.numeroRegistroMedico.equals(""))
		{
			if (this.nombreUsuario!=null)
			{
				informacionGeneralStr=nombreUsuario + " - R.M. " + numeroRegistroMedico ;
			}

			if (especialidadesMedico!=null&&especialidadesMedico.length>0)
			{
			    //Ya no aparece la palabra especialidades 
				//informacionGeneralStr= informacionGeneralStr+" Especialidades: ";

				for (int i=0;i<especialidadesMedico.length;i++)
				{
					if (i!=especialidadesMedico.length-1)
					{
						informacionGeneralStr+=" " + especialidadesMedico[i].getNombre()+ ",";
					}
					else
					{
						informacionGeneralStr+=" " + especialidadesMedico[i].getNombre();
					}
				}
			}
		}
		else
		{
			informacionGeneralStr="Este usuario no esta registrado en el sistema como profesional de la salud";
		}

		return informacionGeneralStr;
	}
	
	
	public String getNombreyRMPersonalSalud()
	{
	    String informacionGeneralStr = new String();
		InfoDatosInt especialidadesMedico[]=this.getEspecialidades();

		if (this.numeroRegistroMedico!=null&&!this.numeroRegistroMedico.equals(""))
		{
			if (this.nombreUsuario!=null)
			{
				informacionGeneralStr=nombreUsuario + "  -  R.M.  " + numeroRegistroMedico ;
			}
		}
		else
		{
			informacionGeneralStr="Este usuario no esta registrado en el sistema como profesional de la salud";
		}

		return informacionGeneralStr;
	}


	/**
	 * Método que dice si este usuario solo tiene el rol paciente o no
	 * @return boolean que dice si este usuario solo tiene el rol paciente o no
	 */
	public boolean isEsSoloPaciente() {
		return esSoloPaciente;
	}

	/**
	 * Método que dice si este usuario solo tiene el rol paciente o no
	 * @return boolean que dice si este usuario solo tiene el rol paciente o no
	 */
	public boolean getEsSoloPaciente() {
		return esSoloPaciente;
	}

	/**
	 * Este método devuelve un String con las especialidades del médico
	 * separadas por espacio
	 *
	 * @param medico
	 * @return
	 */
	public static String getEspecialidadesMedico(UsuarioBasico medico)
	{
		String especialidades = new String();
		InfoDatosInt especialidadesMedico[]=medico.getEspecialidades();

		if (especialidadesMedico!=null&&especialidadesMedico.length>0)
		{
			//especialidades=" Especialidades: ";

			for (int i=0;i<especialidadesMedico.length;i++)
			{
				if (i!=especialidadesMedico.length-1)
				{
					especialidades+=" " + especialidadesMedico[i].getNombre()+ ",";
				}
				else
				{
					especialidades+=" " + especialidadesMedico[i].getNombre();
				}
			}
		}

		return especialidades;
	}
	
	
	public static String getCodEspecialidadesMedico(UsuarioBasico medico)
	{
		String especialidades = new String();
		InfoDatosInt especialidadesMedico[]=medico.getEspecialidades();

		if (especialidadesMedico!=null&&especialidadesMedico.length>0)
		{
			//especialidades=" Especialidades: ";

			for (int i=0;i<especialidadesMedico.length;i++)
			{
				if (i!=especialidadesMedico.length-1)
				{
					especialidades+=" " + especialidadesMedico[i].getCodigo()+ ",";
				}
				else
				{
					especialidades+=" " + especialidadesMedico[i].getCodigo();
				}
			}
		}

		return especialidades;
	}

	/**
	 * Este método devuelve un String con las especialidades del médico
	 * separadas por espacio
	 *
	 * @param medico
	 * @return
	 */
	public String getEspecialidadesMedico()
	{
		return getEspecialidadesMedico(this);
	}

	
	public String getCodEspecialidadesMedico()
	{
		return getCodEspecialidadesMedico(this);
	}
	/**
	 * Este método revisa si un médico tiene una especialidad
	 * en particular, dado el código de la misma
	 *
	 * @param codigoEspecialidadBuscar Entero con el código
	 * de la especialidad a buscar
	 * @return
	 */
	public boolean tieneEspecialidad (int codigoEspecialidadBuscar)
	{
		int i;
		for (i=0;i<especialidades.length;i++)
		{
			if (codigoEspecialidadBuscar==especialidades[i].getCodigo()&&especialidades[i].getActivo())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Este método revisa si un médico tiene una especialidad
	 * en particular, dado el nombre de la misma
	 *
	 * @param nombreEspecialidadBuscar String con el nombre
	 * de la especialidad a buscar
	 * @return
	 */
	public boolean tieneEspecialidad (String nombreEspecialidadBuscar)
	{
		int i;

		//Si el nombre de la especialidad es nulo, de una vez retornamos
		//false
		if (nombreEspecialidadBuscar==null)
		{
			return false;
		}

		for (i=0;i<especialidades.length;i++)
		{
			if (especialidades[i].getNombre()!=null&&especialidades[i].getNombre().equals(nombreEspecialidadBuscar)&&especialidades[i].getActivo())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	public int getCodigoOcupacionMedica() {
		return codigoOcupacionMedica;
	}

	/**
	 * @return
	 */
	public int getCodigoPersona() {
		return codigoPersona;
	}

	/**
	 * @param i
	 */
	public void setCodigoPersona(int i) {
		codigoPersona = i;
	}

	/**
	 * @return Retorna nombre de la institución.
	 */
	
	public String getInstitucion()
	{
		return institucion;
	}
	
	/**
	 * @param institucion Asigna nombre de la institución.
	 */
	public void setInstitucion(String institucion)
	{
		this.institucion = institucion;
	}
	/**
	 * @return Returns the nit.
	 */
	public String getNit() {
		return nit;
	}
	
	/**
	 * Método que devuelve un String con el nombre y el número de registro médico
	 * (si es médico)
	 * @return
	 */
	public String getNombreRegistroMedico()
	{
	    String informacionStr = new String();
		
		if (this.numeroRegistroMedico!=null&&!this.numeroRegistroMedico.equals(""))
		{
			if (this.nombreUsuario!=null)
			{
				informacionStr=nombreUsuario + " - " + numeroRegistroMedico ;
			}

		}
		else
		{
			informacionStr="Este usuario no esta registrado en el sistema como profesional de la salud";
		}

		return informacionStr;
	}
	
	/**
	 * Metodo que carga la caja que tiene registrada un usuario, solo cuando el usuario esta asociado a una caja.
	 * en caso de tener mas de una caja el sistema no carga nada
	 *
	 */
	public void cargarCajaUsuario(Connection con)
	{
	    if(Utilidades.numCajasAsociadasUsuarioXcentroAtencion(this.loginUsuario, this.codigoCentroAtencion+"")==1)
	    {
	        ResultSetDecorator rs=new ResultSetDecorator(Utilidades.getConsecutivosCajaUsuario(con,this.loginUsuario, this.codigoCentroAtencion+""));
	        try
            {
	        	
                if(rs.next())
                {
                    this.consecutivoCaja=rs.getInt("consecutivocaja");
                }
                this.codigoCaja=Utilidades.getCodigoCaja(con,this.consecutivoCaja);
                this.descripcionCaja=Utilidades.getDescripcionCaja(con,this.consecutivoCaja);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }finally{
            	UtilidadBD.cerrarObjetosPersistencia(null, rs, null);
            }
	    }
	}
    /**
     * @return Returns the codigoCaja.
     */
    public int getCodigoCaja()
    {
        return codigoCaja;
    }
    /**
     * @param codigoCaja The codigoCaja to set.
     */
    public void setCodigoCaja(int codigoCaja)
    {
        this.codigoCaja = codigoCaja;
    }
    /**
     * @return Returns the consecutivoCaja.
     */
    public int getConsecutivoCaja()
    {
        return consecutivoCaja;
    }
    /**
     * @param consecutivoCaja The consecutivoCaja to set.
     */
    public void setConsecutivoCaja(int consecutivoCaja)
    {
        this.consecutivoCaja = consecutivoCaja;
    }
    /**
     * @return Returns the descripcionCaja.
     */
    public String getDescripcionCaja()
    {
        return descripcionCaja;
    }
    /**
     * @param descripcionCaja The descripcionCaja to set.
     */
    public void setDescripcionCaja(String descripcionCaja)
    {
        this.descripcionCaja = descripcionCaja;
    }

	/**
	 * @return Returns the centrosCosto.
	 */
	public HashMap getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto The centrosCosto to set.
	 */
	public void setCentrosCosto(HashMap centrosCosto) {
		this.centrosCosto = centrosCosto;
	}
	
	/**
	 * @return Retorna un elemento del mapa centrosCosto.
	 */
	public Object getCentrosCosto(String key) {
		return centrosCosto.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa centrosCosto.
	 */
	public void setCentrosCosto(String key,Object obj) {
		this.centrosCosto.put(key,obj);
	}

	/**
	 * @return Returns the numCentrosCosto.
	 */
	public int getNumCentrosCosto() {
		return numCentrosCosto;
	}

	/**
	 * @param numCentrosCosto The numCentrosCosto to set.
	 */
	public void setNumCentrosCosto(int numCentrosCosto) {
		this.numCentrosCosto = numCentrosCosto;
	}

	/**
	 * @return Returns the centroAtencion.
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return Returns the codigoCentroAtencion.
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion The codigoCentroAtencion to set.
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return the codigoUPGD
	 */
	public String getCodigoUPGD() {
		return codigoUPGD;
	}

	/**
	 * @param codigoUPGD the codigoUPGD to set
	 */
	public void setCodigoUPGD(String codigoUPGD) {
		this.codigoUPGD = codigoUPGD;
	}

	public int getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(int tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	
	/**
	 * @return the firmaDigital
	 */
	public String getFirmaDigital() 
	{
		//this.firmaDigital="firmaDigital.jpeg";
		return firmaDigital;
	}

	/**
	 * @return the firmaDigital
	 */
	public String getPathFirmaDigital() 
	{
		return ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+this.firmaDigital;
	}
	
	
	/**
	 * @param firmaDigital the firmaDigital to set
	 */
	public void setFirmaDigital(String firmaDigital) {
		this.firmaDigital = firmaDigital;
	}

	public String getDigitoVerificacion() {
		return digitoVerificacion;
	}

	public void setDigitoVerificacion(String digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}

	public String getNombreCargo() {
		return nombreCargo;
	}

	public void setNombreCargo(String nombreCargo) {
		this.nombreCargo = nombreCargo;
	}

	/**
	 * @return the codigoCargo
	 */
	public int getCodigoCargo() {
		return codigoCargo;
	}

	/**
	 * @param codigoCargo the codigoCargo to set
	 */
	public void setCodigoCargo(int codigoCargo) {
		this.codigoCargo = codigoCargo;
	}

	public void setEspecialidades(InfoDatosInt[] especialidades) {
		this.especialidades = especialidades;
	}

	public InfoDatosInt[] getEspecialidades1()
	{	 	
	 return especialidades;	
	}
	
	
	public int getNumEspecialidades()
	{
		return this.especialidades.length;
	}

	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * @return the passwordUsuario
	 */
	public String getPasswordUsuario() {
		return passwordUsuario;
	}

	/**
	 * @param passwordUsuario the passwordUsuario to set
	 */
	public void setPasswordUsuario(String passwordUsuario) {
		this.passwordUsuario = passwordUsuario;
	}

	/**
	 * @return the esProfesionalSalud
	 */
	public String getEsProfesionalSalud() {
		return esProfesionalSalud;
	}

	/**
	 * @param esProfesionalSalud the esProfesionalSalud to set
	 */
	public void setEsProfesionalSalud(String esProfesionalSalud) {
		this.esProfesionalSalud = esProfesionalSalud;
	}
	
	
	/**
	 * @param con
	 * @param loginUsuario
	 * @param codigoFuncionalidad
	 * @return Si tiene una funcionalidad dada
	 */
	public Boolean obtenerFuncionalidadPorusuario(Connection con,String loginUsuario, Integer codigoFuncionalidad)
	{
			return usuarioBasicoDao.consultarTieneRol(con, loginUsuario, codigoFuncionalidad);
	}
	
	
	/**
	 * Alberto Ovalle
	 * mt 5749
	 * Método que devuelve un String con la información general
	 * del médico (si es médico)
	 * @return
	 */
	public String getInformacionObservacionesGeneralPersonalSalud()
	{
	    String informacionGeneralStr = new String();
		try {
	    InfoDatosInt especialidadesMedico[]=this.getEspecialidades();

		if (this.numeroRegistroMedico!=null&&!this.numeroRegistroMedico.equals(""))
		{
			if (this.nombreUsuario!=null)
			{
				if(numeroRegistroMedico.trim().equals("-")) {
					informacionGeneralStr=nombreUsuario;
				} else {
				informacionGeneralStr=nombreUsuario + " " + numeroRegistroMedico;	
				} 	
			}

			if (especialidadesMedico!=null&&especialidadesMedico.length>0)
			{
			    //Ya no aparece la palabra especialidades 
				//informacionGeneralStr= informacionGeneralStr+" Especialidades: ";

				for (int i=0;i<especialidadesMedico.length;i++)
				{
					if (i!=especialidadesMedico.length-1)
					{
						informacionGeneralStr+=" " + especialidadesMedico[i].getNombre()+ ",";
					}
					else
					{
						informacionGeneralStr+=" " + especialidadesMedico[i].getNombre();
					}
				}
			}
		}
		else
		{
			informacionGeneralStr="Este usuario no esta registrado en el sistema como profesional de la salud";
		}
		
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return informacionGeneralStr;
	}
	
	/**
	 * Alberto Ovalle
	 * mt 5749
	 * Método valida si es motivo consulta 
		 * @return boolean
	 */
	public Boolean validarMotivoConsulta(String valor){
		boolean resul=false;
		if(valor.trim().equals(ConstantesIntegridadDominio.acronimoMotivoConsulta)) {
		resul=true;	
		}
		return resul;
	}
	
	
	
}