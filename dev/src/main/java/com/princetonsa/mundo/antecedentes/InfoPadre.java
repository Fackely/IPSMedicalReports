/*
* @(#)InfoPadre.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/

package com.princetonsa.mundo.antecedentes;

import java.io.Serializable;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import com.princetonsa.dao.AntecedentePediatricoDao;
import com.princetonsa.dao.DaoFactory;

import util.InfoDatos;
import util.TipoNumeroId;

/**
* Clase para el manejo de toda la información del padre presente
* en antecedentes pediatricos
*
* @version 1.0, Nov 25, 2003
* @author <a href="mailto:edgar@princetonsa.com">Edgar Leonardo Prieto Perilla</a>
*/
public class InfoPadre implements Serializable
{
	/**
	* El DAO usado por el objeto <code>InfoPadre</code> para acceder a la
	* fuente de datos.
	*/
	private AntecedentePediatricoDao iapd_dao;

	/** Código del paciente */
	private boolean vacio;
	/** Código del paciente */
	private int ii_codigoHijo;

	/** Edad del padre */
	private int ii_edad;

	/** Tipo de sangre del padre */
	private InfoDatos iid_tipoSangre;

	/** Consanguinidad del padre con la madre */
	private String is_consanguinidad;

	/** Primer apellido del padre */
	private String is_primerApellido;

	/** Primer nombre del padre */
	private String is_primerNombre;

	/** Segundo apellido del padre */
	private String is_segundoApellido;

	/** Segundo nombre del padre */
	private String is_segundoNombre;

	/** Identificación del padre */
	private TipoNumeroId itni_identificacion;

	/** Constructor vacío para InfoPadre */
	public InfoPadre()
	{
		clean();
		init(System.getProperty("TIPOBD") );
	}

	/**
	* Constructor que recibe todos los elementos que
	* componen InfoPadre y los guarda en el objeto
	*
	* @param as_tipoIdentificacion
	* @param as_numeroIdentificacion
	* @param as_tipoIdentificacionHijo
	* @param as_numeroIdentificacionHijo
	* @param ai_edad
	* @param aid_tipoSangre
	* @param as_primerApellido
	* @param as_primerNombre
	* @param as_segundoApellido
	* @param as_segundoNombre
	*/
	public InfoPadre
	(
		String		as_tipoIdentificacion,
		String		as_numeroIdentificacion,
		int			ai_codigoHijo,
		int			ai_edad,
		String		as_consanguinidad,
		String		as_primerApellido,
		String		as_primerNombre,
		String		as_segundoApellido,
		String		as_segundoNombre,
		InfoDatos	aid_tipoSangre
	)
	{
		iapd_dao				= null;
		ii_codigoHijo			= ai_codigoHijo;
		ii_edad					= ai_edad;
		iid_tipoSangre			= aid_tipoSangre;
		is_consanguinidad		= as_consanguinidad;
		is_primerApellido		= as_primerApellido;
		is_primerNombre			= as_primerNombre;
		is_segundoApellido		= as_segundoApellido;
		is_segundoNombre		= as_segundoNombre;
		itni_identificacion		= new TipoNumeroId(as_tipoIdentificacion, as_numeroIdentificacion);

		init(System.getProperty("TIPOBD") );
		this.vacio=true;
	}

	/**
	* @param con
	* @param numeroIdentificacionHijo
	* @param codigoTipoIdentificacionHijo
	* @return
	* @throws SQLException
	*/
	public boolean cargar(Connection ac_con, int ai_codigoHijo) throws SQLException
	{
		ResultSetDecorator rs = iapd_dao.consultarInfoPadre(ac_con, ai_codigoHijo);

		if(rs.next() )
		{
			ii_codigoHijo		= ai_codigoHijo;
			ii_edad				= rs.getInt("edad");
			is_consanguinidad	= rs.getString("consanguinidad");
			is_primerApellido	= rs.getString("primer_apellido");
			is_primerNombre		= rs.getString("primer_nombre");
			is_segundoApellido	= rs.getString("segundo_apellido");
			is_segundoNombre	= rs.getString("segundo_nombre");
			itni_identificacion =
				new TipoNumeroId(
					rs.getString("tipo_identificacion"),
					rs.getString("numero_identificacion")
				);

				/* Lectura vacia para determinar si el parámetro tipo_sabngre es o nó nulo */
			rs.getInt("tipo_sangre");

			iid_tipoSangre =
				rs.wasNull() ? null: new InfoDatos(rs.getString("codigo"), rs.getString("nombre") );
			this.vacio=false;
			return true;
		}

		return false;
	}

	public void clean()
	{
		iapd_dao				= null;
		ii_codigoHijo			= -1;
		ii_edad					= 0;
		is_consanguinidad		= "";
		is_primerApellido		= "";
		is_primerNombre			= "";
		is_segundoApellido		= "";
		is_segundoNombre		= "";
		itni_identificacion		= new TipoNumeroId();
		iid_tipoSangre			= null;
		this.vacio=true;
	}

	/** Obtiene la consanguinidad del padre con la madre */
	public String getConsanguinidad()
	{
		return is_consanguinidad;
	}

	/** Retorna la edad del padre durante el embarazo */
	public int getEdad()
	{
		return ii_edad;
	}

	/** Obtiene la identificación del padre */
	public TipoNumeroId getIdentificacion()
	{
		return itni_identificacion;
	}

	/** Obtiene el código del hijo */
	public int getCodigoHijo()
	{
		return ii_codigoHijo;
	}

	/** Obtiene el primer apellido del padre */
	public String getPrimerApellido()
	{
		return is_primerApellido;
	}

	/** Obtiene el primer nombre del padre */
	public String getPrimerNombre()
	{
		return is_primerNombre;
	}

	/** Obtiene el segundo apellido del padre */
	public String getSegundoApellido()
	{
		return is_segundoApellido;
	}

	/** Obtiene el segundo nombre del padre */
	public String getSegundoNombre()
	{
		return is_segundoNombre;
	}

	/**
	* Retorna un objeto infoDatos que contiene el codigo
	* y nombre del tipo de sangre del padre
	*/
	public InfoDatos getTipoSangre()
	{
		return iid_tipoSangre;
	}

	/**
	* Inicializa el acceso a bases de datos de este objeto, obteniendo su
	* respectivo DAO.
	* @param tipoBD el tipo de base de datos que va a usar este objeto
	* (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	* son los nombres y constantes definidos en <code>DaoFactory</code>.
	* @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	*/
	public boolean init(String tipoBD)
	{
		DaoFactory	ldf_df;

		if( (ldf_df = DaoFactory.getDaoFactory(tipoBD) ) != null)
			iapd_dao = ldf_df.getAntecedentePediatricoDao();

		return (iapd_dao != null);
	}

	/**
	* Método para insertar esta informacion del embarazo del padre en un antecedentePediatrico
	*
	* @param con conexión con la fuente de datos
	* @throws SQLException
	*/
	public int insertar(Connection ac_con) throws SQLException
	{
		if(ii_codigoHijo > -1)
			return
				iapd_dao.insertarInfoPadre
				(
					ac_con,
					ii_codigoHijo,
					ii_edad,
					iid_tipoSangre.getCodigo(),
					is_consanguinidad,
					is_primerApellido,
					is_primerNombre,
					is_segundoApellido,
					is_segundoNombre,
					itni_identificacion.getTipoId(),
					itni_identificacion.getNumeroId()
				);

		return 0;
	}

	/**
	* Método para modificar la informacion del embarazo del padre en una antecedente pediatrico
	*
	* @param con conexión con la fuente de datos
	* @throws SQLException
	*/
	public int modificar(Connection ac_con) throws SQLException
	{
		return
			iapd_dao.modificarInfoPadre
			(
				ac_con,
				ii_codigoHijo,
				ii_edad,
				iid_tipoSangre.getCodigo(),
				is_consanguinidad,
				is_primerApellido,
				is_primerNombre,
				is_segundoApellido,
				is_segundoNombre,
				itni_identificacion.getTipoId(),
				itni_identificacion.getNumeroId()
			);

	}

	/** Asigna la consanguinidad del padre con la madre */
	public void setConsanguinidad(String as_consanguinidad)
	{
		if(as_consanguinidad != null)
			is_consanguinidad = as_consanguinidad.trim();
	}

	/** Asigna el la edad del padre */
	public void setEdad(int ai_edad)
	{
		ii_edad = ai_edad;
	}

	/** Asigna la identificación del padre */
	public void setIdentificacion(TipoNumeroId atni_identificacion)
	{
		if(atni_identificacion != null)
		itni_identificacion = atni_identificacion;
	}

	/** Asigna el código del hijo */
	public void setCodigoHijo(int ai_codigoHijo)
	{
		ii_codigoHijo = ai_codigoHijo;
	}

	/** Asigna el primer apellido del padre */
	public void setPrimerApellido(String as_primerApellido)
	{
		if(as_primerApellido != null)
			is_primerApellido = as_primerApellido.trim();
	}

	/** Asigna el primer nombre del padre */
	public void setPrimerNombre(String as_primerNombre)
	{
		if(as_primerNombre != null)
			is_primerNombre = as_primerNombre.trim();
	}

	/** Asigna el segundo apellido del padre */
	public void setSegundoApellido(String as_segundoApellido)
	{
		if(as_segundoApellido != null)
			is_segundoApellido = as_segundoApellido.trim();
	}

	/** Asigna el segundo nombre del padre */
	public void setSegundoNombre(String as_segundoNombre)
	{
		if(as_segundoNombre != null)
			is_segundoNombre = as_segundoNombre.trim();
	}

	/** Asigna el tipo de sangre del padre */
	public void setTipoSangre(InfoDatos datos)
	{
		iid_tipoSangre = datos;
	}
	/**
	 * @return
	 */
	public boolean isVacio() {
		return vacio;
	}

	/**
	 * @param b
	 */
	public void setVacio(boolean b) {
		vacio = b;
	}

}