
/*
 * Created on Apr 23, 2003
 */
package com.princetonsa.mundo.antecedentes;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.TipoNumeroId;

import com.princetonsa.dao.AntecedentesVariosDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.AccesoBD;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Esta es la clase encargada de hacer el manejo de los antecedentes varios
 * @author &lt;a href="mailto:dramirez@princetonsa.com" &gt;Diego Andr&eacute; Ram&iacute;rez Arag&oacute;n &lt;/a&gt;
 */
public final class AntecedenteVario implements AccesoBD{

	/**
	 * C&oacute;digo del antededente
	 */
	private int codigoAntecedente;
	/**
	 * Identificador del tipo de antededente
	 */
	private int codigoTipoAntecedente;
	
	/**
	 * Nombre del tipo de antecedente
	 */
	private String tipoAntecedente;
	
	/**
	 * Descripci&oacute;n del antedente
	 */
	private String descripcionAntecedente;
	
	/**
	 * Fecha en la que se inserta el antecedente
	 */
	private String fecha;
	/**
	 * Hora en la que se inserta el antecedente
	 */
	private String hora;
	/**
	 * Usuario que inserta el antecedente (Es usuario debe ser profesional de la salud)
	 */
	private String usuario;
	
	/**
	 * Paciente al que se le esta realizando el antecedente
	 */
	private PersonaBasica paciente;
	
	/**
	 * Objeto que se va a comunicar con la base de datos 
	 */
	private AntecedentesVariosDao antecedenteVarioDao = null;

	/**
	 * Constructor vacio de la clase
	 */
	public AntecedenteVario() {
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * Constructor con par&aacute;metros
	 * @param aCodigoTipoAntecedente Codigo del tipo de antecedente que se va a crear 
	 * @param aDescripcionAntecedente Descripcion del antecedente
	 * @param aUsuario Usuario que esta creando el antecedente
	 * @param paciente Paciente al que se va a realizar el antecedente
	 */
	public AntecedenteVario(int aCodigoTipoAntecedente,String aDescripcionAntecedente,String aUsuario,PersonaBasica paciente) {
		this();
    	this.codigoTipoAntecedente = aCodigoTipoAntecedente;
        this.descripcionAntecedente = aDescripcionAntecedente;
        this.usuario = aUsuario;
        this.paciente = paciente;
	}

    /**
     *  Permite modificar el codigo del antedecente
     * @param codigoAntecedente Nuevo c&oacute;digo del antecedente
     */
    public void setCodigoAntecedente(int codigoAntecedente) {
		this.codigoAntecedente = codigoAntecedente;
	}

	/**
	 * Permite obtener el c&oacute;digo del antecedente
	 * @return int el c&oacute; del antecedente
	 */
	public int getCodigoAntecedente() {
		return codigoAntecedente;
	}

	/**
	 * Pemite modificar el c&oacute;digo del tipo de antedecente
	 * @param codigoTipoAntecedente
	 */
	public void setCodigoTipoAntecedente(int codigoTipoAntecedente) {
		this.codigoTipoAntecedente = codigoTipoAntecedente;
	}

	/**
	 * Permit obtener el C&oacute;digo del tipo de antecedente al que pertenece el antecedente.
	 * @return El c&oacute;digo del tipo de antecedente
	 */
	public int getCodigoTipoAntecedente() {
		return codigoTipoAntecedente;
	}

	/**
	 * Permite asignar el nombre del tipo de antecedente al que pertenece el antecedente; Este nombre
	 * debe ser consistente con el c&oacute;digo del tipo de antecedente
	 * @param tipoAntecedente Nuevo tipo de antecedente
	 */
	public void setTipoAntecedente(String tipoAntecedente) {
		this.tipoAntecedente = tipoAntecedente;
	}

	/**
	 * Permite obtener el nombre del tipo de antecedente 
	 * @return El nombre del tipo de antecedente
	 */
	public String getTipoAntecedente() {
		return tipoAntecedente;
	}

	/**
	 * Permite asignar la descripci&oacute;n del antecedente
	 * @param descripcionAntecedente Nueva descripci&oacute;n del antecedente
	 */
	public void setDescripcionAntecedente(String descripcionAntecedente) {
		this.descripcionAntecedente = descripcionAntecedente;
	}

	/**
	 * Permite obtener la descripci&oacute;n del antencedente
	 * @return Descripci&oacute;n del antecedente
	 */
	public String getDescripcionAntecedente() {
		return descripcionAntecedente;
	}

	/**
	 * Pemite obtener la fecha de creaci&oacute; del antecedente
	 * @return La fecha de creaci&oacute;n del antecedente
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * Pemite obtener la hora de creaci&oacute;n del antecedente
	 * @return Fecha de creaci&oacute;n del antecedente
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * Permite asignar el usuario  responsable del ingreso del  antecendente
	 * @param usuario Login del usuario que crea el antecedente
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Permite obtener el login del usuario responsable del antecedente
	 * @return
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Permite asignar el paciente al que se le esta asiendo el antecedente
	 * @param paciente El paciente
	 */
	public void setPaciente(PersonaBasica paciente) {
		this.paciente = paciente;
	}

	/**
	 * Permite obtener la informaci&oacute;n del paciente al que pertenece el antecedente.
	 * @return El paciente
	 */
	public PersonaBasica getPaciente() {
		return paciente;
	}

	/**
	 * Implementa la inicializaci&oacute;n de la base de datos para el objeto antecedente vario
	 * @see com.princetonsa.mundo.AccesoBD#init(java.lang.String)
	 */
	public boolean init(String tipoBD) {
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
	
		if (myFactory != null) {
			this.antecedenteVarioDao = myFactory.getAntecedentesVariosDao();
			wasInited = (this.antecedenteVarioDao != null);
		}
		return wasInited;
	}

	/**
	 * Implementa la funcionalidad de inserci&oacute;n para los antecedentes varios
	 * @see com.princetonsa.mundo.AccesoBD#insertar(java.sql.Connection)
	 */
	public int insertar(Connection con) throws SQLException {
    	this.codigoAntecedente =this.antecedenteVarioDao.insertar(con,this.codigoTipoAntecedente,this.descripcionAntecedente,this.usuario,this.paciente);
		return this.codigoAntecedente;
	}

	/**
 	 * Implementa la funcionalidad de cargar para los antecedentes varios
	 * @see com.princetonsa.mundo.AccesoBD#cargar(java.sql.Connection, util.TipoNumeroId)
	 */
	public void cargar(Connection con, TipoNumeroId id) throws SQLException {
		int codigo = this.codigoAntecedente;
		if(id!=null && !id.getTipoId().equals("-1")){
			codigo = Integer.parseInt(id.getTipoId());
		}
		ResultSetDecorator rs = this.antecedenteVarioDao.cargar(con, codigo);
		if(rs.next()){
			this.codigoAntecedente = rs.getInt("codigo_i");
			this.codigoTipoAntecedente = rs.getInt("codigoTipo_i");
			this.tipoAntecedente = rs.getString("tipo_s");
			this.descripcionAntecedente = rs.getString("descripcion_s");
			this.fecha = rs.getDate("fecha_d").toString();
			this.hora = rs.getString("hora_t");
			this.hora = this.hora.substring(0,this.hora.lastIndexOf(":"));
			this.usuario = rs.getString("usuario_s");
		}

	}

	/**
	 * Implementa la funcionalidad de modificar para los antecedentes varios
	 * @see com.princetonsa.mundo.AccesoBD#modificar(java.sql.Connection, util.TipoNumeroId)
	 */
	public int modificar(Connection con, TipoNumeroId id) throws SQLException {
		int codigo = this.codigoAntecedente;
		if(id!=null && !id.getTipoId().equals("-1")){
			codigo = Integer.parseInt(id.getTipoId());
		}
		
		return this.antecedenteVarioDao.modificar(con,codigo,this.descripcionAntecedente,this.usuario,this.paciente);
	}

	/**
	 * Inicializa con valores los atributos de la clase
	 */
	public void clean(){
		this.codigoAntecedente = -1;
		this.codigoTipoAntecedente = -1;
		this.descripcionAntecedente="" ;
		this.fecha = "";
		this.hora ="";
		this.tipoAntecedente = "";
		this.usuario = "";
	}
}