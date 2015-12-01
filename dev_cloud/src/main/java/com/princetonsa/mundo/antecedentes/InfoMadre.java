/*
* @(#)InfoMadre.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/

package com.princetonsa.mundo.antecedentes;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import util.InfoDatos;
import util.TipoNumeroId;
import util.UtilidadFecha;

import com.princetonsa.dao.AntecedentePediatricoDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;

/**
* Clase para el manejo de toda la informaci�n de la madre presente
* en antecedentes pediatricos
*
* @version 1.0, Jul 23, 2003
* @author <a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>
*/
public class InfoMadre implements Serializable
{
 /**
  * 
  */
 private static final long serialVersionUID = 1L;
	/** El DAO usado por el objeto <code>InfoMadre</code> para acceder a la fuente de datos. */
	private AntecedentePediatricoDao antecedentePediatricoDao = null;

	/**Indica si existe o no  informacion en el antecedente*/
	private boolean vacio;
	/** Numero de semanas de gestacion en el embarazo */
	private float semanasGestacion;

	/** Tipo de sangre de la madre */
	private InfoDatos tipoSangre;

	/** Edad de la madre al momento del embarazo */
	private int edad;

	/** el c�digo del paciente hijo */
	private int ii_codigoHijo;

	/** C�digo del m�todo de semanas de gestaci�n del embarazo */
	private int ii_codigoMetodo;
	
	/** En cazo de que se seleccione otro metodo de getacion */
	private String ii_otroMetodo;

	/** Indicador de confiabilidad de las semanas de gestaci�n */
	private String is_confiableSemanas;

	/** Fecha PP de la madre */
	private String is_fpp;

	/** Fecha UR de la madre */
	private String is_fur;

	/** Informacion A de los embarazos de la madre */
	private String is_infoEmbarazoA;

	/** Informacion C de los embarazos de la madre */
	private String is_infoEmbarazoC;

	/** Informacion G de los embarazos de la madre */
	private String is_infoEmbarazoG;

	/** Informacion M de los embarazos de la madre */
	private String is_infoEmbarazoM;

	/** Informacion P de los embarazos de la madre */
	private String is_infoEmbarazoP;

	/** Informacion V de los embarazos de la madre */
	private String is_infoEmbarazoV;

	/** Primer apellido de la madre */
	private String is_primerApellido;

	/** Primer nombre de la madre */
	private String is_primerNombre;

	/** Segundo apellido de la madre */
	private String is_segundoApellido;

	/** Segundo nombre de la madre */
	private String is_segundoNombre;

	/** Descripci�n de patolog�as durante el embarazo, parto o puerperio */
	private String obsPatologias;

	/** Descripcion de ex�menes practicados y medicamentos ingeridos durante la gestaci�n */
	private String obsExamenesMedicamentos;

	/** Observaciones generales de la madre durante el embarazo. */
	private String observaciones;

	/** Identificaci�n de la madre */
	private TipoNumeroId itni_identificacion;


	/** Constructor vac�o para InfoMadre */
	public InfoMadre()
	{
		init(System.getProperty("TIPOBD") );
		clean();
	}

	/**
	* Constructor que recibe todos los elementos que componen InfoMadre y los
	* guarda en el objeto
	*
	* @param numeroIdentificacionHijo
	* @param codigoTipoIdentificacionHijo
	* @param edad
	* @param tipoSangre
	* @param semanasGestacion
	* @param obsPatologias
	* @param obsExamenesMedicamentos
	* @param observaciones
	*/
	public InfoMadre
	(
		int			ai_codigoHijo,
		int			edad,
		InfoDatos	tipoSangre,
		float		semanasGestacion,
		String		obsPatologias,
		String		obsExamenesMedicamentos,
		String		observaciones,
		int			ai_codigoMetodo,
		String		as_confiableSemanas,
		String		as_fpp,
		String		as_fur,
		String		as_infoEmbarazoA,
		String		as_infoEmbarazoC,
		String		as_infoEmbarazoG,
		String		as_infoEmbarazoM,
		String		as_infoEmbarazoP,
		String		as_infoEmbarazoV,
		String		as_numeroIdentificacion,
		String		as_primerApellido,
		String		as_primerNombre,
		String		as_segundoApellido,
		String		as_segundoNombre,
		String		as_tipoIdentificacion,
		String		ai_otroMetodo
	)
	{
		ii_codigoHijo					= ai_codigoHijo;
		this.edad						= edad;
		this.tipoSangre					= tipoSangre;
		this.semanasGestacion			= semanasGestacion;
		this.obsPatologias				= obsPatologias;
		this.obsExamenesMedicamentos	= obsExamenesMedicamentos;
		this.observaciones				= observaciones;

		ii_codigoMetodo		= ai_codigoMetodo;
		ii_otroMetodo= ai_otroMetodo;
		is_confiableSemanas	= as_confiableSemanas;
		is_fpp				= as_fpp;
		is_fur				= as_fur;
		is_infoEmbarazoA	= as_infoEmbarazoA;
		is_infoEmbarazoC	= as_infoEmbarazoC;
		is_infoEmbarazoG	= as_infoEmbarazoG;
		is_infoEmbarazoM	= as_infoEmbarazoM;
		is_infoEmbarazoP	= as_infoEmbarazoP;
		is_infoEmbarazoV	= as_infoEmbarazoV;
		is_primerApellido	= as_primerApellido;
		is_primerNombre		= as_primerNombre;
		is_segundoApellido	= as_segundoApellido;
		is_segundoNombre	= as_segundoNombre;
		itni_identificacion	= new TipoNumeroId(as_tipoIdentificacion, as_numeroIdentificacion);

		init(System.getProperty("TIPOBD") );
		this.vacio=false;
	}

	public boolean cargar(Connection con, int ai_codigoHijo)throws SQLException
	{
		ResultSetDecorator rs = antecedentePediatricoDao.consultarInfoMadre(con, ai_codigoHijo);

		if(rs.next())
		{
			edad					= rs.getInt("edad");
			ii_codigoHijo			= ai_codigoHijo;
			ii_codigoMetodo			= rs.getInt("metodo_semanas_gestacion");
			ii_otroMetodo			= rs.getString("detalle_otro_metodo");
			is_confiableSemanas		= rs.getString("confiable_semanas_gestacion");
			is_fpp					= UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fpp") );
			is_fur					= UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fur") );
			is_infoEmbarazoA		= rs.getString("info_embarazos_a");
			is_infoEmbarazoC		= rs.getString("info_embarazos_c");
			is_infoEmbarazoG		= rs.getString("info_embarazos_g");
			is_infoEmbarazoM		= rs.getString("info_embarazos_m");
			is_infoEmbarazoP		= rs.getString("info_embarazos_p");
			is_infoEmbarazoV		= rs.getString("info_embarazos_v");
			is_primerApellido		= rs.getString("primer_apellido");
			is_primerNombre			= rs.getString("primer_nombre");
			is_segundoApellido		= rs.getString("segundo_apellido");
			is_segundoNombre		= rs.getString("segundo_nombre");
			itni_identificacion		= new TipoNumeroId(
										rs.getString("tipo_identificacion"),
										rs.getString("numero_identificacion") );
			obsPatologias			= rs.getString("obs_patologias");
			obsExamenesMedicamentos	= rs.getString("obs_examenes_medicamentos");
			observaciones			= rs.getString("observaciones");
			semanasGestacion		= rs.getFloat("semanas_gestacion");

			/* Lectura vacia para determinar si el par�metro tipo_sabngre es o n� nulo */
			rs.getInt("tipo_sangre");

			
			tipoSangre = 	rs.wasNull() ? null: new InfoDatos(rs.getString("codigo"), rs.getString("nombre") );
			
			
			this.vacio=false;
			return true;
		}

		return false;
	}

	
	public void clean()
	{
		edad					= 0;
		ii_codigoHijo 			= -1;
		ii_codigoMetodo			= 0;
		ii_otroMetodo			= "";
		is_confiableSemanas		= "";
		is_fpp					= "";
		is_fur					= "";
		is_infoEmbarazoA		= "";
		is_infoEmbarazoC		= "";
		is_infoEmbarazoG		= "";
		is_infoEmbarazoM		= "";
		is_infoEmbarazoP		= "";
		is_infoEmbarazoV		= "";
		is_primerApellido		= "";
		is_primerNombre			= "";
		is_segundoApellido		= "";
		is_segundoNombre		= "";
		itni_identificacion		= new TipoNumeroId();
		obsPatologias			= "";
		obsExamenesMedicamentos	= "";
		observaciones			= "";
		semanasGestacion		= 0;
		tipoSangre				= null;
		this.vacio=true;
	}

	/** Obtiene el c�digo del m�todo de semanas de gestaci�n */
	public int getCodigoMetodoSemanasGestacion()
	{
		return ii_codigoMetodo;
	};

	/** Obtiene el indicador de confiabilidad de las semanas de gestaci�n */
	public String getConfiableSemanasGestacion()
	{
		return is_confiableSemanas;
	};

	/** Retorna la edad de la madre durante el embarazo */
	public int getEdad()
	{
		return edad;
	}

	/** Obtiene la fecha PP de la madre */
	public String getFpp()
	{
		return is_fpp;
	}

	/** Obtiene la fehca UR de la madre */
	public String getFur()
	{
		return is_fur;
	}

	/** Obtiene la identificaci�n de la madre */
	public TipoNumeroId getIdentificacion()
	{
		return itni_identificacion;
	}

	/** Obtiene el c�digo del hijo */
	public int getCodigoHijo()
	{
		return ii_codigoHijo;
	}

	/** Obtiene la infomaci�n A de los embarazos de la madre */
	public String getInfoEmbarazoA()
	{
		return is_infoEmbarazoA;
	}

	/** Obtiene la infomaci�n C de los embarazos de la madre */
	public String getInfoEmbarazoC()
	{
		return is_infoEmbarazoC;
	}

	/** Obtiene la infomaci�n G de los embarazos de la madre */
	public String getInfoEmbarazoG()
	{
		return is_infoEmbarazoG;
	}

	/** Obtiene la infomaci�n M de los embarazos de la madre */
	public String getInfoEmbarazoM()
	{
		return is_infoEmbarazoM;
	}

	/** Obtiene la infomaci�n P de los embarazos de la madre */
	public String getInfoEmbarazoP()
	{
		return is_infoEmbarazoP;
	}

	/** Obtiene la infomaci�n V de los embarazos de la madre */
	public String getInfoEmbarazoV()
	{
		return is_infoEmbarazoV;
	}

	/** Retorna las observaciones generales del embarazo de la madre */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	* Retorna la descripcion de examenes practicados
	* y medicamentos ingeridos durante la gestacion
	*/
	public String getObsExamenesMedicamentos()
	{
		return obsExamenesMedicamentos;
	}

	/**
	* Retorna la descripcion de las patologias
	* durante el embarazo, parto o puerperio
	*/
	public String getObsPatologias()
	{
		return obsPatologias;
	}

	/** Obtiene el primer apellido de la madre */
	public String getPrimerApellido()
	{
		return is_primerApellido;
	}

	/** Obtiene el primer nombre de la madre */
	public String getPrimerNombre()
	{
		return is_primerNombre;
	}

	/** Obtiene el segundo apellido de la madre */
	public String getSegundoApellido()
	{
		return is_segundoApellido;
	}

	/** Obtiene el segundo nombre de la madre */
	public String getSegundoNombre()
	{
		return is_segundoNombre;
	}

	/** Retorna el numero de semanas de gestaci�n */
	public float getSemanasGestacion()
	{
		return semanasGestacion;
	}

	/**
	* Retorna un objeto infoDatos que contiene el codigo
	* y nombre del tipo de sangre de la madre
	*/
	public InfoDatos getTipoSangre()
	{
		return tipoSangre;
	}

	/**
	* Inicializa el acceso a bases de datos de este objeto, obteniendo su
	* respectivo DAO.
	* @param tipoBD el tipo de base de datos que va a usar este objeto
	* (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	* son los nombres y constantes definidos en <code>DaoFactory</code>.
	* @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
	*/
	public boolean init(String tipoBD)
	{
		boolean		wasInited;
		DaoFactory	myFactory;

		wasInited	= false;
		myFactory	= DaoFactory.getDaoFactory(tipoBD);

		if(myFactory != null)
		{
			antecedentePediatricoDao = myFactory.getAntecedentePediatricoDao();
			wasInited = (antecedentePediatricoDao != null);
		}

		return wasInited;
	}

	/**
	* M�todo para insertar esta informacion del embarazo de la madre en un antecedentePediatrico
	*
	* @param con conexi�n con la fuente de datos
	* @throws SQLException
	*/
	public int insertar(Connection con)throws SQLException
	{
		if(ii_codigoHijo > -1)
			return
				antecedentePediatricoDao.insertarInfoMadre
				(
					con,
					ii_codigoHijo,
					edad,
					tipoSangre.getCodigo(),
					semanasGestacion,
					obsPatologias,
					obsExamenesMedicamentos,
					observaciones,
					ii_codigoMetodo,
					is_confiableSemanas,
					is_fpp,
					is_fur,
					is_infoEmbarazoA,
					is_infoEmbarazoC,
					is_infoEmbarazoG,
					is_infoEmbarazoM,
					is_infoEmbarazoP,
					is_infoEmbarazoV,
					is_primerApellido,
					is_primerNombre,
					is_segundoApellido,
					is_segundoNombre,
					itni_identificacion.getTipoId(),
					itni_identificacion.getNumeroId(),
					ii_otroMetodo
				);

		return 0;
	}

	/**
	* M�todo para modificar la informacion del embarazo de la madre en una antecedente pediatrico
	*
	* @param con conexi�n con la fuente de datos
	* @throws SQLException
	*/
	public int modificar(Connection con) throws SQLException
	{
		return
			antecedentePediatricoDao.modificarInfoMadre
			(
				con,
				ii_codigoHijo,
				edad,
				tipoSangre.getCodigo(),
				semanasGestacion,
				obsPatologias,
				obsExamenesMedicamentos,
				observaciones,
				ii_codigoMetodo,
				is_confiableSemanas,
				is_fpp,
				is_fur,
				is_infoEmbarazoA,
				is_infoEmbarazoC,
				is_infoEmbarazoG,
				is_infoEmbarazoM,
				is_infoEmbarazoP,
				is_infoEmbarazoV,
				is_primerApellido,
				is_primerNombre,
				is_segundoApellido,
				is_segundoNombre,
				itni_identificacion.getTipoId(),
				itni_identificacion.getNumeroId(),
				ii_otroMetodo
			);
	}

	/** Asigna el c�digo del m�todo de semanas de gestaci�n */
	public void setCodigoMetodoSemanasGestacion(int ai_codigoMetodo)
	{
		if(ai_codigoMetodo > 0)
		ii_codigoMetodo = ai_codigoMetodo;
	}

	/** Asigna el indicador de confiabilidad de las semanas de gestaci�n */
	public void setConfiableSemanasGestacion(String as_confiableSemanas)
	{
		if(as_confiableSemanas != null)
			is_confiableSemanas = as_confiableSemanas.trim();
	};

	/** Asigna el la edad de la madre al momento del embarazo */
	public void setEdad(int i)
	{
		edad = i;
	}

	/** Asigna la fecha PP de la madre */
	public void setFpp(String as_fpp)
	{
		if(as_fpp != null && UtilidadFecha.validarFecha(as_fpp = as_fpp.trim() ) )
			is_fpp = as_fpp;
	}

	/** Asigna la fecha UR de la madre */
	public void setFur(String as_fur)
	{
		if(as_fur != null && UtilidadFecha.validarFecha(as_fur = as_fur.trim() ) )
			is_fur = as_fur.trim();
	}

	/** Asigna la identificaci�n de la madre */
	public void setIdentificacion(TipoNumeroId atni_id)
	{
		if(atni_id != null)
		itni_identificacion = atni_id;
	}

	/** Asigna el c�digo del hijo */
	public void setCodigoHijo(int ai_codigoHijo)
	{
		ii_codigoHijo = ai_codigoHijo;
	}

	/** Asigna la informaci�n A de los embarazos de la madre */
	public void setInfoEmbarazoA(String as_infoEmbarazoA)
	{
		if(as_infoEmbarazoA != null)
			is_infoEmbarazoA = as_infoEmbarazoA.trim();
	}

	/** Asigna la informaci�n C de los embarazos de la madre */
	public void setInfoEmbarazoC(String as_infoEmbarazoC)
	{
		if(as_infoEmbarazoC != null)
			is_infoEmbarazoC = as_infoEmbarazoC.trim();
	}

	/** Asigna la informaci�n G de los embarazos de la madre */
	public void setInfoEmbarazoG(String as_infoEmbarazoG)
	{
		if(as_infoEmbarazoG != null)
			is_infoEmbarazoG = as_infoEmbarazoG.trim();
	}

	/** Asigna la informaci�n M de los embarazos de la madre */
	public void setInfoEmbarazoM(String as_infoEmbarazoM)
	{
		if(as_infoEmbarazoM != null)
			is_infoEmbarazoM = as_infoEmbarazoM.trim();
	}

	/** Asigna la informaci�n P de los embarazos de la madre */
	public void setInfoEmbarazoP(String as_infoEmbarazoP)
	{
		if(as_infoEmbarazoP != null)
			is_infoEmbarazoP = as_infoEmbarazoP.trim();
	}

	/** Asigna la informaci�n V de los embarazos de la madre */
	public void setInfoEmbarazoV(String as_infoEmbarazoV)
	{
		if(as_infoEmbarazoV != null)
			is_infoEmbarazoV = as_infoEmbarazoV.trim();
	}

	/** Asigna las observaciones generales del embarazo */
	public void setObservaciones(String string)
	{
		observaciones = string;
	}

	/** Asigna los examenes y medicamentos ingeridos durante la gestacion */
	public void setObsExamenesMedicamentos(String string)
	{
		obsExamenesMedicamentos = string;
	}

	/** Asigna las patologias durante el embarazo, parto o puerperio */
	public void setObsPatologias(String string)
	{
		obsPatologias = string;
	}

	/** Asigna el primer apellido de la madre */
	public void setPrimerApellido(String as_primerApellido)
	{
		if(as_primerApellido != null)
			is_primerApellido = as_primerApellido.trim();
	}

	/** Asigna el primer nombre de la madre */
	public void setPrimerNombre(String as_primerNombre)
	{
		if(as_primerNombre != null)
			is_primerNombre = as_primerNombre.trim();
	}

	/** Asigna el segundo apellido de la madre */
	public void setSegundoApellido(String as_segundoApellido)
	{
		if(as_segundoApellido != null)
			is_segundoApellido = as_segundoApellido.trim();
	}

	/** Asigna el segundo nombre de la madre */
	public void setSegundoNombre(String as_segundoNombre)
	{
		if(as_segundoNombre != null)
			is_segundoNombre = as_segundoNombre.trim();
	}

	/** Asigna las semanas de gestacion del embarazo */
	public void setSemanasGestacion(float f)
	{
		semanasGestacion = f;
	}

	/** Asigna el tipo de sangre de la madre */
	public void setTipoSangre(InfoDatos datos)
	{
		tipoSangre = datos;
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

	/**
	 * @return the ii_otroMetodo
	 */
	public String getIi_otroMetodo() {
		return ii_otroMetodo;
	}

	/**
	 * @param ii_otroMetodo the ii_otroMetodo to set
	 */
	public void setIi_otroMetodo(String ii_otroMetodo) {
		this.ii_otroMetodo = ii_otroMetodo;
	}

}