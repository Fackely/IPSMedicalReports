/*
 * @(#)Cama.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.Answer;
import util.Encoder;
import util.InfoDatosString;
import util.UtilidadTexto;

import com.princetonsa.dao.CamaDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Esta clase encapsula los atributos y la funcionalidad necesarias para manejar una cama de un hospital.
 *
 * @version 1.0, Oct 29, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class Cama {

	/**
	 * Código de la cama
	 */
	private String codigoCama;

	/**
	 * Número de la cama.
	 */
	private String numeroCama;
	
	/**
	 * habitacion de la cama
	 */
	private String habitacionCama;
	
	/**
	 * Método para obtener el codigo descriptivo de la habitacion
	 */
	private String codigoDescriptivoHabitacion;
	
	/**
	 * Piso de la campa
	 */
	private String piso;
	
	
	/**
	 * Tipo habitacion
	 */
	private String tipoHabitacion;

	/**
	 * Estado de la cama corresponde al codigo del estado
	 */
	private int estado;
	
	/**
	 * Nombre del estado de la cama. Ej. ocupada, desinfección, etc
	 */
	private String nombreEstado;

	/**
	 * Centro de costo al cual pertenece la cama.
	 */
	private String centroCostoCama;

	/**
	 * Código del centro de costo al cual pertenece la cama.
	 */
	private String codigoCentroCostoCama;

	/**
	 * Tipo de usuario de la cama (hombre, mujer, pensionado, etc.)
	 */
	private String tipoUsuarioCama;

	/**
	 * Código del tipo de usuario de la cama.
	 */
	private String codigoTipoUsuarioCama;
	
	/**
	 * Descripcion cama
	 */
	private String descripcionCama;

	/**
	 * El DAO usado por el objeto <code>Cama</code> para acceder a la fuente de datos.
	 */
	private static CamaDao camaDao = null;

	private CamaOcupada informacionCamaOcupada = null;
	/**
	 * Constructora de la clase <code>Cama</code>.
	 */
	public Cama () {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init (String tipoBD) {

		if (camaDao == null) {
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			camaDao = myFactory.getCamaDao();
		}

	}

	/**
	 * Retorna el centro de costo de la cama.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el centro de costo de la cama
	 */
	public String getCentroCostoCama(boolean encoded) {
		if (encoded) {
			return getCentroCostoCama();
		}
		else {
			return centroCostoCama;
		}
	}

	/**
	 * Retorna el centro de costo de la cama.
	 * @return el centro de costo de la cama
	 */
	public String getCentroCostoCama() {
		return centroCostoCama;
	}

	/**
	 * Retorna el código de la cama.
	 * @return el código de la cama
	 */
	public String getCodigoCama() {
		return codigoCama;
	}

	/**
	 * Retorna el codigo del centro de costo de la cama.
	 * @return el codigo del centro de costo de la cama
	 */
	public String getCodigoCentroCostoCama() {
		return codigoCentroCostoCama;
	}

	/**
	 * Retorna el codigo del tipo de usuario de la cama.
	 * @return el codigo del tipo de usuario de la cama
	 */
	public String getCodigoTipoUsuarioCama() {
		return codigoTipoUsuarioCama;
	}

	/**
	 * Retorna el estado de la cama.
	 * @return el estado de la cama
	 */
	public int getEstado() {
		return estado;
	}

	/**
	 * Retorna el numero de la cama.
	 * @return el numero de la cama
	 */
	public String getNumeroCama() {
		return numeroCama;
	}

	/**
	 * Retorna el tipo de usuario de la cama.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el tipo de usuario de la cama
	 */
	public String getTipoUsuarioCama(boolean encoded) {
		if (encoded) {
			return getTipoUsuarioCama();
		}
		else {
			return tipoUsuarioCama;
		}
	}

	/**
	 * Retorna el tipo de usuario de la cama.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;") .
	 * @return el tipo de usuario de la cama
	 */
	public String getTipoUsuarioCama() {
		return Encoder.encode(tipoUsuarioCama);
	}

	/**
	 * Establece el centro de costo de la cama.
	 * @param centroCostoCama el centro de costo de la cama a establecer
	 */
	public void setCentroCostoCama(String centroCostoCama) {
		this.centroCostoCama = centroCostoCama;
	}

	/**
	 * Establece el código de la cama.
	 * @param codigoCama el código de la cama a establecer
	 */
	public void setCodigoCama(String codigoCama) {
		this.codigoCama = codigoCama;
	}

	/**
	 * Establece el codigo del centro de costo de la cama.
	 * @param codigoCentroCostoCama el codigo del centro de costo de la cama a establecer
	 */
	public void setCodigoCentroCostoCama(String codigoCentroCostoCama) {
		this.codigoCentroCostoCama = codigoCentroCostoCama;
	}

	/**
	 * Establece el codigo del tipo de usuario de la cama.
	 * @param codigoTipoUsuarioCama el codigo del tipo de usuario de la cama a establecer
	 */
	public void setCodigoTipoUsuarioCama(String codigoTipoUsuarioCama) {
		this.codigoTipoUsuarioCama = codigoTipoUsuarioCama;
	}

	/**
	 * Establece el estado de la cama.
	 * @param estado el estado a establecer
	 */
	public void setEstado(int estado) {
		this.estado = estado;
	}

	/**
	 * Establece el numero de la cama.
	 * @param numeroCama el numero de la cama a establecer
	 */
	public void setNumeroCama(String numeroCama) {
		this.numeroCama = numeroCama;
	}

	/**
	 * Establece el tipo de usuario de la cama.
	 * @param tipoUsuarioCama el tipo de usuario de la cama a establecer
	 */
	public void setTipoUsuarioCama(String tipoUsuarioCama) {
		this.tipoUsuarioCama = tipoUsuarioCama;
	}

	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos de la cama.
	 */
	public void clean () {

		this.setNumeroCama("");
		this.setCodigoCama("");
		this.setHabitacionCama("");
		this.setPiso("");
		this.setTipoHabitacion("");
		this.setCentroCostoCama("");
		this.setCodigoCentroCostoCama("");
		this.setTipoUsuarioCama("");
		this.setCodigoTipoUsuarioCama("");
		this.setDescripcionCama("");
		this.setEstado(-1);
		this.habitacionCama="";
		this.codigoDescriptivoHabitacion = "";
	}

	/**
	 * Inicializa todas las variables de la cama que puedan venir en la forma "codigo-nombre".
	 */
	public void inicializarVariables () {

		String [] resultados;

		resultados = UtilidadTexto.separarNombresDeCodigos(numeroCama, 1);
		codigoCama = resultados[0];
		numeroCama = resultados[1];

		resultados = UtilidadTexto.separarNombresDeCodigos(centroCostoCama, 1);
		codigoCentroCostoCama = resultados[0];
		centroCostoCama = resultados[1];

		resultados = UtilidadTexto.separarNombresDeCodigos(tipoUsuarioCama, 1);
		codigoTipoUsuarioCama = resultados[0];
		tipoUsuarioCama = resultados[1];

	}

	/**
	 * Inserta una cama en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @return numero de camas insertadas
	 */
	public int insertarCama(Connection con) throws SQLException {
		return camaDao.insertarCama(con, numeroCama, estado, codigoCentroCostoCama,codigoTipoUsuarioCama);
	}

	/**
	 * Dada la identificacion de una cama, establece las propiedades de un objeto <code>Cama</code>
	 * en los valores correspondientes.
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoCama el código de la cama a insertar
	 */
	public void cargarCama(Connection con, String codigoCama) throws SQLException {
		Answer a=camaDao.cargarCama(con, codigoCama);
		ResultSetDecorator rs = a.getResultSet();
		this.clean();
		
		
		if(rs == null){
			return;
		}
		
		
		if (rs.next())
		{
			setNumeroCama(rs.getString("numeroCama"));
			setEstado(rs.getInt("estado"));
			setCodigoCentroCostoCama(rs.getString("codigoCentroCosto"));
			setCodigoTipoUsuarioCama(rs.getString("codigoTipoUsuarioCama"));
			this.setDescripcionCama(rs.getString("descripcion"));
			this.setTipoUsuarioCama(rs.getString("tipoUsuarioCama"));
			this.setCodigoDescriptivoHabitacion(rs.getString("descripcion_habitacion"));
		

		
		}
	}

	/**
	 * Modifica una cama en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoCama el código de la cama que desea modificar
	 * @return numero de camas modificadas
	 */
	public int modificarCama(Connection con, String codigoCama) throws SQLException {
		return camaDao.modificarCama(con, codigoCama, numeroCama, estado, codigoCentroCostoCama, codigoTipoUsuarioCama);
	}

	/**
	 * Returns the descripcionCama.
	 * @return String
	 */
	public String getDescripcionCama() {
		return descripcionCama;
	}

	/**
	 * Sets the descripcionCama.
	 * @param descripcionCama The descripcionCama to set
	 */
	public void setDescripcionCama(String descripcionCama) {
		this.descripcionCama = descripcionCama;
	}
	
	/**
	 * Cambia el estado de la cama en la base de datos.
	 * @param	estado, nuevo estado de la cama
	 * @param	codigoCama, código de la cama que desea modificar 
	 */
	public int cambiarEstadoCama(Connection con, String codigoCama, int estado) throws SQLException
	{
		return camaDao.cambiarEstadoCama(con, codigoCama, estado);
	}
		
	/**
	 * Returns the nombreEstado.
	 * @return String
	 */
	public String getNombreEstado()
	{
		return nombreEstado;
	}

	/**
	 * Sets the nombreEstado.
	 * @param nombreEstado The nombreEstado to set
	 */
	public void setNombreEstado(String nombreEstado)
	{
		this.nombreEstado = nombreEstado;
	}

	/**
	 * Returns the informacionCamaOcupada.
	 * @return CamaOcupada
	 */
	public CamaOcupada getInformacionCamaOcupada()
	{
		return informacionCamaOcupada;
	}

	/**
	 * Sets the informacionCamaOcupada.
	 * @param informacionCamaOcupada The informacionCamaOcupada to set
	 */
	public void setInformacionCamaOcupada(CamaOcupada informacionCamaOcupada)
	{
		this.informacionCamaOcupada = informacionCamaOcupada;
	}
	
	public String[] getFechaHoraUltimoUsoCama(Connection con, int codigoCama) throws SQLException
	{
		return camaDao.getFechaHoraUltimoUsoCama(con, codigoCama);
	}

    /**
     * @return Returns the habitacionCama.
     */
    public String getHabitacionCama() {
        return habitacionCama;
    }
    /**
     * @param habitacionCama The habitacionCama to set.
     */
    public void setHabitacionCama(String habitacionCama) {
        this.habitacionCama = habitacionCama;
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

	/**
	 * @return the codigoDescriptivoHabitacion
	 */
	public String getCodigoDescriptivoHabitacion() {
		return codigoDescriptivoHabitacion;
	}

	/**
	 * @param codigoDescriptivoHabitacion the codigoDescriptivoHabitacion to set
	 */
	public void setCodigoDescriptivoHabitacion(String codigoDescriptivoHabitacion) {
		this.codigoDescriptivoHabitacion = codigoDescriptivoHabitacion;
	}
}