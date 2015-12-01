/*
 * @(#)AdmisionUrgencias.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import util.TipoNumeroId;
import util.UtilidadFecha;

import com.princetonsa.dao.AdmisionUrgenciasDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.historiaClinica.Valoraciones;

/**
 * Esta clase encapsula los atributos y funcionalidades necesarios para cargar,
 * ingresar y modificar una admisión de urgencias. Implementa la
 * <i>interface</i> <code>AccesoBD</code> con las operaciones estándar de
 * persistencia (insertar, cargar, modificar).
 *
 * @version 1.0, Mar 6, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>, <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class AdmisionUrgencias extends Admision {
	

	
	private boolean modificacionCamaHabilitada;
	private boolean mostrarDatosCama;

	/**
	 * Anio en que se creo la admision, no depende del año de la fecha de la
	 * admisión
	 */
	private int anio;

	/**
	 * Fecha en la que fue puesto en observación un paciente, en formato yyyy-MM-dd.
	 */
	private String fechaObservacion;

	/**
	 * Hora en la que fue puesto en observación un paciente, formato HH:mm.
	 */
	private String horaObservacion;
	
	/**
	 * Fecha en la que fue egresado de observación un paciente, en formato yyyy-MM-dd.
	 */
	private String fechaEgresoObservacion;

	/**
	 * Hora en la que fue egresado de observación un paciente, formato HH:mm.
	 */
	private String horaEgresoObservacion;

	/**
	 * Nombre de la cama (el código con el que se la denomina en la clínica).
	 */
	private String nombreCama;
	
	/**
	 * Piso de la cama
	 */
	private String pisoCama;
	
	/**
	 * Tipo habitacion de la cama
	 */
	private String tipoHabitacionCama;

	/**
	 * Habitacion de la cama
	 */
	private String habitacionCamaStr;
	
	/**
	 * Código de la cama (el código interno con el que referenciamos la cama).
	 */
	private int codigoCama;

	/**
	 * Tipo de usuario que usa la cama
	 */
	private String tipoUsuarioCama;

	/**
	 * Descripción de la cama
	 */
	private String descripcionCama;
	
	/**
	 * Nombre del centro de costo
	 */
	private String nombreCentroCostoCama;
	
	/**
	 * consecutivo triage
	 */
	private String consecutivoTriage;
	
	/**
	 * consecutivoFechaTriage
	 */
	private String consecutivoFechaTriage;
	
	/**
	 * El DAO usado por el objeto <code>AdmisionUrgencias</code> para acceder a la fuente de datos.
	 */
	private AdmisionUrgenciasDao admisionUrgenciasDao;
	private static Logger logger = Logger.getLogger(AdmisionUrgencias.class);

	/**
	 * Constructora vacía de esta clase, necesaria para poder usar esta clase como un JavaBean. Inicializa los
	 * atributos de esta clase en valores vacíos, y lee una Property del System que indica con qué tipo de BD
	 * se van a realizar las operaciones de persistencia de este objeto. El tipo de BD puede modificarse
	 * dinámicamente, si es necesario, mediante posteriores llamadas a init().
	 */
	public AdmisionUrgencias() {
		clean();
		init(System.getProperty("TIPOBD"));
	}

	/**
	 * Retorna el nombre de la cama (el código con el que se la denomina en la clínica).
	 * @return el nombre de la cama
	 */
	public String getNombreCama() {
		return nombreCama;
	}

	/**
	 * Retorna el código de la cama (el código interno con el que referenciamos la cama).
	 * @return el código de la cama
	 */
	public int getCodigoCama() {
		return codigoCama;
	}

	/**
	 * Retorna la fecha de entrada a observación.
	 * @return la fecha de entrada a observación
	 */
	public String getFechaObservacion() {
		return fechaObservacion;
	}

	/**
	 * Retorna la hora de entrada a observación.
	 * @return la hora de entrada a observación
	 */
	public String getHoraObservacion() {
		return horaObservacion;
	}

	/**
	 * Returns the anio.
	 * @return int
	 */
	public int getAnio() {
		return anio;
	}

	/**
	 * Returns the descripcionCama.
	 * @return String
	 */
	public String getDescripcionCama() {
		return descripcionCama;
	}

	/**
	 * Returns the tipoUsuarioCama.
	 * @return String
	 */
	public String getTipoUsuarioCama() {
		return tipoUsuarioCama;
	}

	/**
	 * Establece el nombre de la cama (el código con el que se la denomina en la clínica).
	 * @param nombreCama el nombre de la cama a establecer
	 */
	public void setNombreCama(String nombreCama) {
		this.nombreCama = (nombreCama != null) ? nombreCama.trim() : "";		
	}
	
    /**
     * @return Returns the habitacionCamaStr.
     */
    public String getHabitacionCamaStr() 
    {
        if((habitacionCamaStr+"").trim().equals("null") || habitacionCamaStr==null)
            habitacionCamaStr="";
        return habitacionCamaStr;
    }
    /**
     * @param habitacionCamaStr The habitacionCamaStr to set.
     */
    public void setHabitacionCamaStr(String habitacionCamaStr) {
        this.habitacionCamaStr = (habitacionCamaStr != null) ? habitacionCamaStr.trim() : "";
    }
	/**
	 * Establece el código de la cama (el código interno con el que referenciamos la cama).
	 * @param codigoCama el código de la cama a establecer
	 */
	public void setCodigoCama(int codigoCama) {
		this.codigoCama = codigoCama;
	}

	/**
	 * Establece el código de la cama (el código interno con el que referenciamos la cama).
	 * @param codigoCama el código de la cama a establecer
	 */
	public void setCodigoCama(String codigoCama) {
		this.codigoCama = Integer.parseInt(codigoCama);
	}

	/**
	 * Establece la fecha de entrada a observación.
	 * @param fechaObservacion la fecha de entrada a establecer
	 */
	public void setFechaObservacion(String fechaObservacion) {
		this.fechaObservacion = (fechaObservacion != null) ? fechaObservacion.trim() : null;
	}

	/**
	 * Establece la hora de entrada a observación.
	 * @param horaObservacion la hora de entrada a establecer
	 */
	public void setHoraObservacion(String horaObservacion) {
		this.horaObservacion = (horaObservacion != null) ? horaObservacion.trim() : null;
	}	

	/**
	 * @return Returns the consecutivoFechaTriage.
	 */
	public String getConsecutivoFechaTriage() {
		return consecutivoFechaTriage;
	}

	/**
	 * @param consecutivoFechaTriage The consecutivoFechaTriage to set.
	 */
	public void setConsecutivoFechaTriage(String consecutivoFechaTriage) {
		this.consecutivoFechaTriage = (consecutivoFechaTriage != null) ? consecutivoFechaTriage.trim() : null;
	}

	/**
	 * @return Returns the consecutivoTriage.
	 */
	public String getConsecutivoTriage() {
		return consecutivoTriage;
	}

	/**
	 * @param consecutivoTriage The consecutivoTriage to set.
	 */
	public void setConsecutivoTriage(String consecutivoTriage) {
		this.consecutivoTriage = (consecutivoTriage != null) ? consecutivoTriage.trim() : null;
	}

	/**
	 * Sets the anio.
	 * @param anio The anio to set
	 */
	public void setAnio(int anio) {
		this.anio = anio;
	}

	/**
	 * Sets the descripcionCama.
	 * @param descripcionCama The descripcionCama to set
	 */
	public void setDescripcionCama(String descripcionCama) {
		this.descripcionCama = descripcionCama;
	}

	/**
	 * Sets the tipoUsuarioCama.
	 * @param tipoUsuarioCama The tipoUsuarioCama to set
	 */
	public void setTipoUsuarioCama(String tipoUsuarioCama) {
		this.tipoUsuarioCama = tipoUsuarioCama;
	}

	/**
	 * Este método inicializa en valores vacíos, mas no-nulos los atributos de este objeto.
	 */
	public void clean() {

		super.clean();
		anio = -1;
		descripcionCama = "";
		fechaObservacion = "";
		horaObservacion = "";
		fechaEgresoObservacion = "";
		horaEgresoObservacion = "";		
		nombreCama = "";
		this.pisoCama = "";
		this.tipoHabitacionCama = "";
		this.habitacionCamaStr="";
		this.nombreCentroCostoCama = "";
		codigoCama = -1;
		tipoUsuarioCama = "";
		this.modificacionCamaHabilitada = false;
		this.mostrarDatosCama = false;
		this.consecutivoFechaTriage="";
		this.consecutivoTriage="";
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) {

		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null) {
			admisionUrgenciasDao = myFactory.getAdmisionUrgenciasDao();
			wasInited = (admisionUrgenciasDao != null);
		}

		return wasInited;

	}
	
	public static AdmisionUrgenciasDao admisionUrgencias()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAdmisionUrgenciasDao();
	}
	/**
	 * Inserta una nueva admisión de urgencias en una fuente de datos, reutilizando una conexión existente
	 * Se manejan las transacciones dependiendo del valor que se le pase en el
	 * parametro estado, que puede ser: empezar, continuar, o finalizar
	 * @param con una conexión abierta con una fuente de datos
	 * @return 0 si no se pudo realizar la inserción; el código de la admisión si sí se pudo insertar.
	 */
	public int insertarTransaccional(Connection con, String estado,int institucion)  
	{
		try
		{
			// Cambiandole el formato a la fecha de 'dd/MM/yyyy' a 'yyyy-MM-dd'
			String laFecha = UtilidadFecha.conversionFormatoFechaABD(super.getFecha());
			String laFechaObservacion = UtilidadFecha.conversionFormatoFechaABD(this.getFechaObservacion());			
					
			int codigo = admisionUrgenciasDao.insertarAdmisionUrgencias(
				con, getCodigoOrigen(), laFecha, getHora(), getMedico().getCodigoPersona(),
				/*getNumeroAutorizacion(),*/ getCodigoCausaExterna(),
				laFechaObservacion, horaObservacion, codigoCama, getLoginUsuario(), getIdCuenta(), 
				estado, getConsecutivoTriage(), getConsecutivoFechaTriage(), getFechaEgresoObservacion(),getHoraEgresoObservacion(),institucion
			);		
			setCodigo(codigo);
			return codigo;
		}
		catch(SQLException e)
		{
			logger.error("Error al ingresar una admision de urgencias: "+e);
			return 0;
		}
	}
	
	
	
	/**
	 * Metodo que permite inicializar todos los atributos del objeto segun la cuenta
	 */
	public void cargar(Connection con, int codigoCuenta) throws SQLException {

		clean();
		try
		{
			ResultSetDecorator rs = admisionUrgenciasDao.cargarAdmisionUrgencias(con, codigoCuenta);						
	
			if (rs.next())	{
				super.setCodigo(rs.getInt("codigo"));
				this.setAnio(rs.getInt("anio"));
				super.setIdCuenta(rs.getInt("cuenta"));
				super.setCodigoOrigen(rs.getInt("origen_admision_urgencias"));
				String fechaFormatoy_m_d = rs.getString("fecha_admision");
				super.setFecha(cambiarFormatoFechaAd_m_y(fechaFormatoy_m_d));
				super.setHora(UtilidadFecha.convertirHoraACincoCaracteres(rs.getString("hora_admision")));
	
				//Creando el usuario de sesion, con el login que obtenemos en la consulta
				UsuarioBasico usuarioBasicoSesion = new UsuarioBasico();
				usuarioBasicoSesion.init(System.getProperty("TIPOBD"));
				usuarioBasicoSesion.cargarUsuarioBasico(con, rs.getString("login_usuario"));
				super.setLoginUsuario(usuarioBasicoSesion);
				//this.setNumeroAutorizacion(rs.getString("numero_autorizacion"));
				super.setCodigoCausaExterna(rs.getInt("causa_externa"));
				fechaFormatoy_m_d = rs.getString("fecha_ingreso_observacion");			
				this.setFechaObservacion(cambiarFormatoFechaAd_m_y(fechaFormatoy_m_d));
				String hora_ingreso_observacion = rs.getString("hora_ingreso_observacion");
				if(hora_ingreso_observacion != null && !hora_ingreso_observacion.equals("")) {
					this.setHoraObservacion(hora_ingreso_observacion.substring(0,hora_ingreso_observacion.lastIndexOf(':')));
				}
				//sanmoy 2003-17-03
				//Cargando la fecha de egreso de observación.
				fechaFormatoy_m_d = rs.getString("fecha_egreso_observacion");			
				this.setFechaEgresoObservacion(cambiarFormatoFechaAd_m_y(fechaFormatoy_m_d));
				String hora_egreso_observacion = rs.getString("hora_egreso_observacion");
				if(hora_egreso_observacion != null && !hora_egreso_observacion.equals("")) {
					this.setHoraEgresoObservacion(hora_egreso_observacion);
				}
	//			//Si no tiene fecha y hora de egreso de observación, se pone como sugerida la 
	//			//fecha y hora de la evolución que dá orden de salida en caso de que exista.
	//			if(this.fechaEgresoObservacion.equals("")){
	//				ResultSetDecorator rs_fechaEgresoObsSugerida = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEvolucionDao().consultarFechaEvolucionSalida (con, super.getIdCuenta());
	//				if(rs_fechaEgresoObsSugerida.next()){
	//					this.setFechaEgresoObservacion(cambiarFormatoFechaAd_m_y(rs_fechaEgresoObsSugerida.getString("fecha_evolucion")));
	//					this.setHoraEgresoObservacion(rs_fechaEgresoObsSugerida.getString("hora_evolucion"));						
	//				}								
	//			}			
				
				//sanmoy 2003-06-03
				// Si no se tiene fecha y hora de ingreso a observación, se pone como sugerida la 
				// fecha y hora de grabación de la valoración de urgencias, en caso de que exista.
				if(this.fechaObservacion.equals(""))
				{
					String[] fechaHora = Valoraciones.obtenerFechahoraGrabacionValoracionUrgencias(con, super.getIdCuenta()+"");
					this.setFechaObservacion(fechaHora[0]);
					this.setHoraObservacion(fechaHora[1]);								
				}			
				
				//Ahora los nombres que viene de otras tablas
				this.setCausaExterna(rs.getString("nombre_causa_externa"));
				this.setOrigen(rs.getString("nom_origen_admision_urgencias"));		
				
				//Creando el medico
				
				if (rs.getString("codigo_medico")!=null)
				{
					PersonaBasica medicoBasico=new PersonaBasica();
					medicoBasico.cargar(con, rs.getInt("codigo_medico"));
					super.setMedico(medicoBasico);
				}
				
				// Cargando la cama
				String cama_observacion = rs.getString("cama_observacion");
				if(cama_observacion != null && !cama_observacion.equals("")){
					Camas1 camas1= new Camas1();
					camas1.setCodigo(Integer.parseInt(cama_observacion));
					camas1.detalleCama1(con);
					
					this.setCodigoCama(cama_observacion);
					this.setPisoCama(camas1.getNombrePiso());
					this.setTipoHabitacionCama(camas1.getTipoHabitacion());
					this.setHabitacionCamaStr(camas1.getNombreHabitacion());
					this.setNombreCama(camas1.getNumeroCama());
					this.setTipoUsuarioCama(camas1.getTipoUsuarioCama().getNombre());
					this.setDescripcionCama(camas1.getDescripcionCama());
					this.setNombreCentroCostoCama(camas1.getCentroCosto().getNombre());
					
				}			
			}
	
			this.mostrarDatosCama = this.mostrarDatosCama(con);
			if(this.mostrarDatosCama)		
				this.modificacionCamaHabilitada = this.isModificacionCamaHabilitada(con);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

	}
	
	
	/**
	 * Metodo que permite inicializar todos los atributos del objeto segun la bd
	 */
	public void cargar(Connection con, TipoNumeroId id) throws SQLException {
		int codigoAdmisionUrgencias = new Integer(id.getNumeroId()).intValue();
		int anio = new Integer(id.getTipoId()).intValue();		

		clean();
		ResultSetDecorator rs = admisionUrgenciasDao.cargarAdmisionUrgencias(con, codigoAdmisionUrgencias, anio);						

		if (rs.next())	{
			super.setCodigo(rs.getInt("codigo"));
			this.setAnio(rs.getInt("anio"));
			super.setIdCuenta(rs.getInt("cuenta"));
			super.setCodigoOrigen(rs.getInt("origen_admision_urgencias"));
			String fechaFormatoy_m_d = rs.getString("fecha_admision");
			super.setFecha(cambiarFormatoFechaAd_m_y(fechaFormatoy_m_d));
			String hora=rs.getString("hora_admision").substring(0, 5);
			if(hora.endsWith(":"))
			{
				hora=hora.substring(0, 4);
			}
			super.setHora(hora);

			//Creando el usuario de sesion, con el login que obtenemos en la consulta
			UsuarioBasico usuarioBasicoSesion = new UsuarioBasico();
			usuarioBasicoSesion.init(System.getProperty("TIPOBD"));
			usuarioBasicoSesion.cargarUsuarioBasico(con, rs.getString("login_usuario"));
			super.setLoginUsuario(usuarioBasicoSesion);
			//this.setNumeroAutorizacion(rs.getString("numero_autorizacion"));
			super.setCodigoCausaExterna(rs.getInt("causa_externa"));
			fechaFormatoy_m_d = rs.getString("fecha_ingreso_observacion");			
			this.setFechaObservacion(cambiarFormatoFechaAd_m_y(fechaFormatoy_m_d));
			String hora_ingreso_observacion = rs.getString("hora_ingreso_observacion");
			if(hora_ingreso_observacion != null && !hora_ingreso_observacion.equals("")) {
				this.setHoraObservacion(hora_ingreso_observacion.substring(0,hora_ingreso_observacion.lastIndexOf(':')));
			}
			//sanmoy 2003-17-03
			//Cargando la fecha de egreso de observación.
			fechaFormatoy_m_d = rs.getString("fecha_egreso_observacion");			
			this.setFechaEgresoObservacion(cambiarFormatoFechaAd_m_y(fechaFormatoy_m_d));
			String hora_egreso_observacion = rs.getString("hora_egreso_observacion");
			if(hora_egreso_observacion != null && !hora_egreso_observacion.equals("")) {
				this.setHoraEgresoObservacion(hora_egreso_observacion);
			}
//			//Si no tiene fecha y hora de egreso de observación, se pone como sugerida la 
//			//fecha y hora de la evolución que dá orden de salida en caso de que exista.
//			if(this.fechaEgresoObservacion.equals("")){
//				ResultSetDecorator rs_fechaEgresoObsSugerida = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEvolucionDao().consultarFechaEvolucionSalida (con, super.getIdCuenta());
//				if(rs_fechaEgresoObsSugerida.next()){
//					this.setFechaEgresoObservacion(cambiarFormatoFechaAd_m_y(rs_fechaEgresoObsSugerida.getString("fecha_evolucion")));
//					this.setHoraEgresoObservacion(rs_fechaEgresoObsSugerida.getString("hora_evolucion"));						
//				}								
//			}			
			
			//sanmoy 2003-06-03
			// Si no se tiene fecha y hora de ingreso a observación, se pone como sugerida la 
			// fecha y hora de grabación de la valoración de urgencias, en caso de que exista.
			if(this.fechaObservacion.equals(""))
			{
				String[] fechaHora = Valoraciones.obtenerFechahoraGrabacionValoracionUrgencias(con, super.getIdCuenta()+"");
				this.setFechaObservacion(fechaHora[0]);
				this.setHoraObservacion(fechaHora[1]);								
			}			
			
			//Ahora los nombres que viene de otras tablas
			this.setCausaExterna(rs.getString("nombre_causa_externa"));
			this.setOrigen(rs.getString("nom_origen_admision_urgencias"));		
			
			//Creando el medico
			
			if (rs.getString("codigo_medico")!=null)
			{
				PersonaBasica medicoBasico=new PersonaBasica();
				medicoBasico.cargar(con, rs.getInt("codigo_medico"));
				super.setMedico(medicoBasico);
			}
			
			// Cargando la cama
			String cama_observacion = rs.getString("cama_observacion");
			if(cama_observacion != null && !cama_observacion.equals("")){
				Camas1 camas1= new Camas1();
				camas1.setCodigo(Integer.parseInt(cama_observacion));
				camas1.detalleCama1(con);
				this.setCodigoCama(cama_observacion);	
				this.setPisoCama(camas1.getNombrePiso());
				this.setTipoHabitacionCama(camas1.getTipoHabitacion());
				this.setHabitacionCamaStr(camas1.getNombreHabitacion());
				this.setNombreCama(camas1.getNumeroCama());
				this.setTipoUsuarioCama(camas1.getTipoUsuarioCama().getNombre());
				this.setDescripcionCama(camas1.getDescripcionCama());															
			}			
		}

		this.mostrarDatosCama = this.mostrarDatosCama(con);
		if(this.mostrarDatosCama)		
			this.modificacionCamaHabilitada = this.isModificacionCamaHabilitada(con);

	}

	/**
	 * Este método actualiza la admisión de urgencias de acuerdo a las reglas
	 * que imperan el egreso. Estas reglas son:
	 * 1. Se debe pasar la cama a desinfección
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoAdmision Código de la admisión de urgencias a actualizar
	 * @param anioAdmision Año de la admisión de urgencias a actualizar
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int actualizarPorEgresoTransaccional (Connection con, int codigoAdmision, int anioAdmision, String estado, int institucion) throws SQLException
	{
		this.setCodigo(codigoAdmision);
		return admisionUrgenciasDao.pasarCamaADesinfeccionTransaccional(con, codigoAdmision, anioAdmision, estado, institucion);
	}

	/**
	 * Este método actualiza la admisión de urgencias de acuerdo a las reglas
	 * que imperan la orden de salida. Estas reglas son:
	 * 1. Asignar fecha y hora de observación
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoAdmision Código de la admisión de urgencias a actualizar
	 * @param anioAdmision Año de la admisión de urgencias a actualizar
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int actualizarPorOrdenSalidaTransaccional (Connection con, int codigoAdmision, int anioAdmision, String fechaObservacion, String horaObservacion, String estado) throws SQLException
	{
		this.setCodigo(codigoAdmision);
		return admisionUrgenciasDao.asignarFechaObservacionTransaccional(con, codigoAdmision, anioAdmision, fechaObservacion, horaObservacion, estado);
	}
	

	/**
	 * Cambia una cadena fecha con formato 'dd/MM/yyyy'	a formato 'yyyy-MM-dd'
	 * Se supone que la fecha viene en el formato predefinido
	 * @param fecha_d_m_y fecha con formato 'dd/MM/yyyy'
	 * @return fecha con formato 'yyyy-MM-dd'
	 */
	
	private static String cambiarFormatoFechaAd_m_y(String fecha_y_m_d) {
		logger.info("fecha_y_m_d: "+fecha_y_m_d);
		if(fecha_y_m_d != null && !fecha_y_m_d.equals("")) {		
			StringTokenizer fechaTokenizer = new StringTokenizer(fecha_y_m_d, "-");
			String anio = fechaTokenizer.nextToken();
			String mes = fechaTokenizer.nextToken();
			String dia = fechaTokenizer.nextToken();
			return dia + "/" + mes + "/" + anio;
		}
		return "";
	}
	/**
	 * Cambia una cadena fecha con formato 'dd/MM/yyyy'	a formato 'yyyy-MM-dd'
	 * Se supone que la fecha viene en el formato predefinido
	 * @param fecha_d_m_y fecha con formato 'dd/MM/yyyy'
	 * @return fecha con formato 'yyyy-MM-dd'
	 */
	private static String cambiarFormatoFechaAy_m_d(String fecha_d_m_y) {
		if(fecha_d_m_y != null && !fecha_d_m_y.equals("")) {
			StringTokenizer fechaTokenizer = new StringTokenizer(fecha_d_m_y, "/");
			String dia = fechaTokenizer.nextToken();
			String mes = fechaTokenizer.nextToken();
			String anio = fechaTokenizer.nextToken();
			return anio + "-" + mes + "-" + dia;
		}
		return "";
	}
	
	/**
	 * Metodo modificar que permite el manejo de la transaccionalidad
	 * dependiendo del estado: comenzar, continuar o finalizar
	 * @param con
	 * @return int
	 * @throws SQLException
	 */
	public int modificarTransaccional(Connection con, String estado) throws SQLException {
		String laFecha = cambiarFormatoFechaAy_m_d(super.getFecha());
		String laFechaObservacion = cambiarFormatoFechaAy_m_d(this.getFechaObservacion());
		String laFechaEgresoObservacion = cambiarFormatoFechaAy_m_d(this.getFechaEgresoObservacion());					
		return admisionUrgenciasDao.modificarAdmisionUrgencias(con, super.getCodigo(), this.getAnio(), super.getCodigoOrigen(), laFecha, super.getHora(), super.getMedico().getCodigoPersona(), /*this.getNumeroAutorizacion(), */super.getCodigoCausaExterna(), laFechaObservacion, this.getHoraObservacion(), this.getCodigoCama(), laFechaEgresoObservacion, this.getHoraEgresoObservacion(), super.getLoginUsuario(), estado);
	}
	
	/**
	 * Modificar que solo recibe la conexión
	 * @param con
	 * @return int
	 * @throws SQLException
	 */
	public int modificar(Connection con) throws SQLException {
		return modificar (con, null);
	}

	/**
	 * Modifica la admisión con los datos que tiene actualmente este objeto.
	 * El TipoNumeroId no lo usa, pues usa los datos del objeto
	 */

	public int modificar(Connection con, TipoNumeroId id) throws SQLException {
		if (id == null); // NOP para evitar el warning 'The argument id is never used'		
		// Cambiandole el formato a la fecha de 'dd/MM/yyyy' a 'yyyy-MM-dd'
		String laFecha = cambiarFormatoFechaAy_m_d(super.getFecha());
		String laFechaObservacion = cambiarFormatoFechaAy_m_d(this.getFechaObservacion());
		String laFechaEgresoObservacion = cambiarFormatoFechaAy_m_d(this.getFechaEgresoObservacion());					
		return admisionUrgenciasDao.modificarAdmisionUrgencias(con, super.getCodigo(), this.getAnio(), super.getCodigoOrigen(), laFecha, super.getHora(), super.getMedico().getCodigoPersona(), /*this.getNumeroAutorizacion(), */super.getCodigoCausaExterna(), laFechaObservacion, this.getHoraObservacion(), this.getCodigoCama(), laFechaEgresoObservacion, this.getHoraEgresoObservacion(), super.getLoginUsuario());				
	}

	/**
	 * Para identificar si al modificar la admision se puede asignar una cama de observacion o
	 * modificarla en caso de que ya la tenga asignada
	 * Esto es si hay un egreso con medico no nulo y los datos de cama de observacion ya estan llenos
	 * @return
	 */	
	public boolean isModificacionCamaHabilitada(Connection con) throws SQLException{
		 return modificacionCamaHabilitada = admisionUrgenciasDao.isModificacionCamaHabilitada(con, super.getCodigo(), super.getIdCuenta());
	}

	/**
	 * Para identificar si al modificar la admision se puede asignar una cama de observacion o
	 * modificarla en caso de que ya la tenga asignada
	 * Esto es si la conducta a seguir en la valoracion es 'Cama Observacion Urgencias'
	 * @return
	 */
	public boolean mostrarDatosCama(Connection con) throws SQLException{
		 return mostrarDatosCama = admisionUrgenciasDao.mostrarDatosCama(con, super.getCodigo());
	}

	/**
	 * @param b
	 */
	public void setModificacionCamaHabilitada(boolean b) {
		modificacionCamaHabilitada = b;
	}

	/**
	 * @return
	 */
	public boolean isModificacionCamaHabilitada() {
		return modificacionCamaHabilitada;
	}

	public int borrarDatosObservacion(Connection con, int numeroCuenta, int institucion) throws SQLException
	{
		return admisionUrgenciasDao.borrarDatosObservacion(con, numeroCuenta, institucion);
	}

	public static boolean estaAsignadaCamaObservacion(Connection con, int numeroCuenta) 
	{
		return admisionUrgencias().estaAsignadaCamaObservacion(con, numeroCuenta);
	}
	/**
	 * @return
	 */
	public String getFechaEgresoObservacion() {
		return fechaEgresoObservacion;
	}

	/**
	 * @return
	 */
	public String getHoraEgresoObservacion() {
		return horaEgresoObservacion;
	}

	/**
	 * @param string
	 */
	public void setFechaEgresoObservacion(String string) {
		fechaEgresoObservacion = string;
	}

	/**
	 * @param string
	 */
	public void setHoraEgresoObservacion(String string) {
		horaEgresoObservacion = string;
	}

	public int actualizarPorReversionEgresoTransaccional(Connection con, String estado) throws SQLException
	{
		return this.admisionUrgenciasDao.reversarEgresoYAdmisionTransaccional(con, this.getIdCuenta(), this.getCodigoCama(), estado);
	}
	
	/**
	 * Método para actualizar la fecha/hora ingreso a observacion de la admision de urgencias
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean actualizarFechaHoraIngresoObservacion(Connection con,String fechaIngresoObservacion,String horaIngresoObservacion,int idCuenta)
	{
		HashMap campos = new HashMap();
		campos.put("fechaIngresoObservacion",fechaIngresoObservacion);
		campos.put("horaIngresoObservacion",horaIngresoObservacion);
		campos.put("idCuenta",idCuenta);
		return admisionUrgencias().actualizarFechaHoraIngresoObservacion(con, campos);
		
	}
	/**
	 * @return
	 */
	public boolean isMostrarDatosCama() {
		return mostrarDatosCama;
	}

	/**
	 * @param b
	 */
	public void setMostrarDatosCama(boolean b) {
		mostrarDatosCama = b;
	}
	/**
	 * 
	 * @param con
	 * @param codigoAdmision
	 * @return
	 */
	public static String[] getCama(Connection con, int codigoAdmision)
	{
		try
		{		
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAdmisionUrgenciasDao().getCama(con, codigoAdmision);
		}
		catch (SQLException sql)
		{
			logger.warn("Error consultando el nombre de la cama en admision urgencias.\n"+sql);
			String[] a={"-1","-1","-1"};
			return a;
		}
	}

	public String[] getUltimaCama(Connection con, int codigoAdmision)
	{
		try
		{		
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAdmisionUrgenciasDao().getUltimaCama(con, codigoAdmision);
		}
		catch (SQLException sql)
		{
			logger.warn("Error consultando el nombre de la cama en admision urgencias.\n"+sql);
			String[] a={"-1","-1","-1"};
			return a;
		}
	}

	/**
	 * @return Returns the nombreCentroCostoCama.
	 */
	public String getNombreCentroCostoCama() {
		return nombreCentroCostoCama;
	}

	/**
	 * @param nombreCentroCostoCama The nombreCentroCostoCama to set.
	 */
	public void setNombreCentroCostoCama(String nombreCentroCostoCama) {
		this.nombreCentroCostoCama = nombreCentroCostoCama;
	}

	/**
	 * @return the pisoCama
	 */
	public String getPisoCama() {
		return pisoCama;
	}

	/**
	 * @param pisoCama the pisoCama to set
	 */
	public void setPisoCama(String pisoCama) {
		this.pisoCama = pisoCama;
	}

	/**
	 * @return the tipoHabitacionCama
	 */
	public String getTipoHabitacionCama() {
		return tipoHabitacionCama;
	}

	/**
	 * @param tipoHabitacionCama the tipoHabitacionCama to set
	 */
	public void setTipoHabitacionCama(String tipoHabitacionCama) {
		this.tipoHabitacionCama = tipoHabitacionCama;
	}

	@Override
	public int insertar(Connection con) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	/**
	 * Consulta la cuenta de una admision que tuvo asignada una cama en particular
	 * 
	 * @param con conexion 
	 * @param codigoCama
	 * @return Codigo de la cuenta
	 */
	public static String obtenerCuentaUltimaAdmisionXCama(Connection con,int codigoCama) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAdmisionUrgenciasDao().obtenerCuentaUltimaAdmisionXCama(con, codigoCama);
	}
}	