/*
 * @(#)HijoBasico.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.antecedentes;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;

import util.InfoDatos;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.HijoBasicoDao;

/**
 * Clase para el manejo de toda la información básica de un hijo, desde su
 * nacimiento.
 *
 * @version 1.0, Abril 4, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class HijoBasico 
{
	/**
	 * Bandera que indica si el bebe nació vivo (true) o muerto (false)
	 */
	private boolean vivo = true;
	
	/**
	 * Bandera que indica si el bebe nacio por cesarea o no.
	 */
	private boolean cesarea = false;
	
  	/**
  	 * Bandera que indica si fue un aborto o no.
  	 */
  	private boolean aborto = false;
  
  	/**
  	 * El numero de hijo dentro del embarazo (Si es solo 1 bebe su numero es 1,
  	 * pero si es mas de 1 este numero indica el orden de nacimiento)
  	 */
  	private int numeroHijoEmbarazo = 0;
  
  	/**
  	 * Lista de las formas de parto en las que nacio el bebe. Ej. Forceps,
  	 * Espatula ....
  	 */
  	private ArrayList formasNacimientoVaginal;
  	
  	/**
  	 * Cadena con la forma en la que nacio el bebe si no existe dentro de las
  	 * predefinidas.
  	 */
  	private String otraFormaNacimientoVaginal = "";
  	
  	/**
  	 * Si no nacio ni por cesarea ni por parto vaginal, ni fue un aborto, se
  	 * especifica en una cadena adicional.
  	 */
  	private String otroTipoParto = "";

	/**
	 * El DAO usado por el objeto
	 * <code>HijoBasico</code> para acceder a la fuente de datos.
	 */
  	private HijoBasicoDao hijoBasicoDao = null;
  	
  	/**
  	 * Peso del hijo
  	 */
  	private String peso="";
  	
  	/**
  	 * Sexo del niño
  	 */
  	private int sexo=0;
  	
  	/**
  	 * Lugar de nacimiento
  	 */
  	private String lugar="";
  	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init (String tipoBD) 
	{

		if (hijoBasicoDao  == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			hijoBasicoDao = myFactory.getHijoBasicoDao();
		}

	}
  	
	/**
	 * Constructor vacío para HijoBasico.
	 */
	public HijoBasico()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Constructor que recibe todos los elementos que componen HijoBasico y los
	 * guarda en el objeto
	 * @param vivo boolean que dice si el hijo nacio vivo o muerto
	 * @param cesarea boolean que dice si el hijo nacio por cesarea o no
	 * @param aborto boolean que dice si el hijo nacio en medio de un aborto o
	 * no
	 * @param numeroHijoEmbarazo Orden de nacimiento de este hijo en el embarazo
	 * (Ej. La señora tuvo trillizos y este objeto corresponde al tercer niño)
	 * @param formasNacimientoVaginal ArrayList con todas las formas de
	 * nacimiento vaginal que aplican al nacimiento de este hijo
	 * @param otraFormaNacimientoVaginal String con la descripción de todas las
	 * formas de nacimiento vaginal que aplican al nacimiento de este hijo y que
	 * no se encuentren en la fuente de datos
	 * @param otroTipoParto String con todas las descripciones del tipo tipo de
	 * parto de este hijo que no se encuentren en la fuente de datos
	 * @param peso String Peso del hijo
	 * @param sexo int Sexo del Hijo
	 * @param lugar String Lugar de nacimiento
	 */
	public HijoBasico (boolean vivo , boolean cesarea, boolean aborto, int numeroHijoEmbarazo, ArrayList formasNacimientoVaginal, String otraFormaNacimientoVaginal, String otroTipoParto, String peso, int sexo, String lugar)
	{
		
		this.vivo=vivo;
		this.cesarea=cesarea;
		this.aborto=aborto;
		this.numeroHijoEmbarazo=numeroHijoEmbarazo;
		this.formasNacimientoVaginal=formasNacimientoVaginal;
		this.otraFormaNacimientoVaginal=otraFormaNacimientoVaginal;
		this.otroTipoParto=otroTipoParto;
		this.peso=peso;
		this.sexo=sexo;
		this.lugar=lugar;
		
		init(System.getProperty("TIPOBD"));

	}

	/**
	 * Método para insertar este HijoBasico 
	 * 
	 * @param con conexión con la fuente de datos
	 * @param codigoTipoIdentificacionPaciente Código del tipo de Identificación
	 * de la paciente a la que se le va a ingresar información de un hijo
	 * (antecedentes gineco-obstetricos)
	 * @param numeroIdentificacionPaciente Número de Identificación de la
	 * paciente a la que se le va a ingresar información de un hijo
	 * (antecedentes gineco- obstetricos)
	 * @param numeroEmbarazo El número de embarazo cronologico (Ej. Es el
	 * primer embarazo de la paciente)
	 * @return int Número de Hijos Insertados (1 Inserción exitosa, 0 problemas
	 * inserción)
	 * @throws SQLException
	 */
	public int insertar (Connection con, int codigoPaciente, int numeroEmbarazo) throws SQLException
	{
		return hijoBasicoDao.insertar(	con, 
															codigoPaciente, 
															numeroEmbarazo, 
															numeroHijoEmbarazo, 
															vivo, 
															otraFormaNacimientoVaginal, 
															cesarea, 
															aborto, 
															otroTipoParto, 
															formasNacimientoVaginal,
															peso,
															sexo,
															lugar);
	}

	/**
	 * Método para insertar este HijoBasico, con definición de Transaccionalidad
	 * especificando el parámetro estado
	 * 
	 * @param con conexión con la fuente de datos
	 * @param codigoTipoIdentificacionPaciente Código del tipo de Identificación
	 * de la paciente a la que se le va a ingresar información de un hijo
	 * (antecedentes gineco-obstetricos)
	 * @param numeroIdentificacionPaciente Número de Identificación de la
	 * paciente a la que se le va a ingresar información de un hijo
	 * (antecedentes gineco- obstetricos)
	 * @param numeroEmbarazo El número de embarazo cronologico (Ej. Es el
	 * primer embarazo de la paciente)
  	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return int Número de Hijos Insertados (1 Inserción exitosa, 0 problemas
	 * inserción)
	 * @throws SQLException
	 */
	public int insertarTransaccional (Connection con, int codigoPaciente, int numeroEmbarazo, String estado) throws SQLException
	{
		return hijoBasicoDao.insertarTransaccional(con, 
																				codigoPaciente, 
																				numeroEmbarazo, 
																				numeroHijoEmbarazo, 
																				vivo, 
																				otraFormaNacimientoVaginal, 
																				cesarea, 
																				aborto, 
																				otroTipoParto, 
																				formasNacimientoVaginal, 
																				estado,
																				peso,
																				sexo,
																				lugar);
	}
	
	/**
	 * Método para modificar este HijoBasico 
	 * 
	 * @param con conexión con la fuente de datos
	 * @param codigoTipoIdentificacionPaciente Código del tipo de Identificación
	 * de la paciente a la que se le va a modificar información de un hijo
	 * (antecedentes gineco-obstetricos)
	 * @param numeroIdentificacionPaciente Número de Identificación de la
	 * paciente a la que se le va a modificar información de un hijo
	 * (antecedentes gineco- obstetricos)
	 * @param numeroEmbarazo El número de embarazo cronologico (Ej. Es el
	 * primer embarazo de la paciente)
	 * @return int Número de Hijos Modificados (1 Modificación exitosa, 0
	 * problemas modificación)
	 * @throws SQLException
	 */
	public int modificar (Connection con, int codigoPaciente, int numeroEmbarazo) throws SQLException
	{
		if (hijoBasicoDao.existeHijo(con, codigoPaciente, numeroEmbarazo, numeroHijoEmbarazo))
		{
			return hijoBasicoDao.modificar(con, 
																codigoPaciente, 
																numeroEmbarazo, 
																numeroHijoEmbarazo, 
																vivo, 
																otraFormaNacimientoVaginal, 
																cesarea, 
																aborto, 
																otroTipoParto, 
																formasNacimientoVaginal, peso, sexo, lugar);
		}
		else
		{
			//Si el hijo no existe lo debo insertar,
			//como esta no es transaccional utilizo el método NO transaccional
			
			return insertar(con, codigoPaciente, numeroEmbarazo);
		}
	}

	/**
	 * Método para modificar este HijoBasico, con definición de
	 * Transaccionalidad especificando el parámetro estado
	 * 
	 * @param con conexión con la fuente de datos
	 * @param codigoTipoIdentificacionPaciente Código del tipo de Identificación
	 * de la paciente a la que se le va a modificar información de un hijo
	 * (antecedentes gineco-obstetricos)
	 * @param numeroIdentificacionPaciente Número de Identificación de la
	 * paciente a la que se le va a modificar información de un hijo
	 * (antecedentes gineco- obstetricos)
	 * @param numeroEmbarazo El número de embarazo cronologico (Ej. Es el
	 * primer embarazo de la paciente)
  	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return int Número de Hijos Modificados (1 Modificación exitosa, 0
	 * problemas modificación)
	 * @throws SQLException
	 */
	public int modificarTransaccional (Connection con, int codigoPaciente, int numeroEmbarazo, String estado) throws SQLException
	{
		if (hijoBasicoDao.existeHijo(con, codigoPaciente, numeroEmbarazo, numeroHijoEmbarazo))
		{
			return hijoBasicoDao.modificarTransaccional(con, 
																						codigoPaciente, 
																						numeroEmbarazo, 
																						numeroHijoEmbarazo, 
																						vivo, 
																						otraFormaNacimientoVaginal, 
																						cesarea, 
																						aborto, 
																						otroTipoParto, 
																						formasNacimientoVaginal, 
																						estado,
																						peso,
																						sexo,
																						lugar);
		}
		else
		{
			//Si el hijo no existe lo debo insertar,
			return insertarTransaccional(con, codigoPaciente, numeroEmbarazo, estado);
		}
	}

	/**
	 * Método que carga todos los partos vaginales que aplican al nacimiento de
	 * este hijo
	 * 
	 * @param con conexión con la fuente de datos
	 * @param codigoTipoIdentificacionPaciente Código del tipo de Identificación
	 * de la paciente a la que se le va a ingresar información de un hijo
	 * (antecedentes gineco-obstetricos)
	 * @param numeroIdentificacionPaciente Número de Identificación de la
	 * paciente a la que se le va a ingresar información de un hijo
	 * (antecedentes gineco- obstetricos)
	 * @param numeroEmbarazo Número del embarazo en el que nacio este hijo (Ej.
	 * Es el primer hijo del segundo embarazo de la señora)
	 * @throws SQLException
	 */
	public void cargarTiposPartoVaginal (Connection con, int codigoPaciente, int numeroEmbarazo) throws SQLException
	{
		ResultSetDecorator rsACargar=hijoBasicoDao.cargarTiposPartoVaginal(con, codigoPaciente, numeroEmbarazo, this.numeroHijoEmbarazo);
		
		//Limpiamos la lista de tipos de parto vaginal
		
		formasNacimientoVaginal=new ArrayList();
		
		while (rsACargar.next())
		{
			formasNacimientoVaginal.add(new InfoDatos ( rsACargar.getString("codigoTipoParto"),  rsACargar.getString("tipoParto") )  );
		}
	}
	
	/**
	 * Retorna la bandera que indica si el bebe nacio vivo o no
	 * @return 			boolean, true si nació vivo, false si no.
	 */
	public boolean isVivo() 
	{
		return vivo;
	}

	/**
	 * Asigna la bandera que indica si el bebe nacio vivo o no
	 * @param 		boolean, vivo
	 */
	public void setVivo(boolean vivo) 
	{
		this.vivo = vivo;
	}

	/**
	 * Retorna la bandera que indica si el bebe nacio por cesarea o no
	 * @return 			boolean, true si nacio por cesárea, false si no.
	 */
	public boolean isCesarea() 
	{
		return cesarea;
	}

	/**
	 * Asigna la bandera que indica si el bebe nacio por cesarea o no
	 * @param 		boolean, cesarea
	 */
	public void setCesarea(boolean cesarea) 
	{
		this.cesarea = cesarea;
	}

	/**
	 * Retorna la bandera que indica si fue un aborto o no
	 * @return 			boolean, true si fue un aborto, false si no
	 */
	public boolean isAborto() 
	{
		return aborto;
	}

	/**
	 * Asigna la bandera que indica si fue un aborto o no
	 * @param 		boolean, aborto
	 */
	public void setAborto(boolean aborto) 
	{
		this.aborto = aborto;
	}

	/**
	 * Retorna el numero (posicion) del bebe dentro del embarazo
	 * @return 			int, numero de bebe del embarazo
	 */
	public int getNumeroHijoEmbarazo() 
	{
		return numeroHijoEmbarazo;
	}

	/**
	 * Asigna el numero (posicion) del bebe dentro del embarazo
	 * @param 		int, numeroHijoEmbarazo
	 */
	public void setNumeroHijoEmbarazo(int numeroHijoEmbarazo) 
	{
		this.numeroHijoEmbarazo = numeroHijoEmbarazo;
	}

	/**
	 * Retorna la cadena con la especificacion de la forma de nacimiento vaginal
	 * ingresada
	 * @return 			String, forma de nacimiento vaginal no existente
	 */
	public String getOtraFormaNacimientoVaginal() 
	{
		return otraFormaNacimientoVaginal;
	}

	/**
	 * Asigna la cadena con la especificacion de la forma de nacimiento vaginal
	 * ingresada
	 * @param 		String, otraFormaNacimientoVaginal
	 */
	public void setOtraFormaNacimientoVaginal(String otraFormaNacimientoVaginal) 
	{
		this.otraFormaNacimientoVaginal = otraFormaNacimientoVaginal;
	}

	/**
	 * Retorna la cadena con la especificacion del tipo de parto ingresado
	 * manualmente
	 * @return 			String, tipo de parto no existente en la bd
	 */
	public String getOtroTipoParto() 
	{
		return otroTipoParto;
	}

	/**
	 * Asigna la cadena con la especificacion del tipo de parto ingresado
	 * manualmente
	 * @param 		String, otroTipoParto
	 */
	public void setOtroTipoParto(String otroTipoParto) 
	{
		this.otroTipoParto = otroTipoParto;
	}
	/**
	 * Retorna la lista con las formas de nacimiento vaginal que presento el
	 * nacimiento del bebe.
	 * @return 			ArrayList, formas de nacimiento vaginal (tipo parto
	 * 						vaginal)
	 */
	public ArrayList getFormasNacimientoVaginal() 
	{
		return formasNacimientoVaginal;
	}

	/**
	 * Asigna la lista con las formas de nacimiento vaginal que presento el
	 * nacimiento del bebe.
	 * @param 		ArrayList, formasNacimientoVaginal
	 */
	public void setFormasNacimientoVaginal(ArrayList formasNacimientoVaginal) 
	{
		this.formasNacimientoVaginal = formasNacimientoVaginal;
	}

	/**
	 * Adiciona una nueva forma de nacimiento vaginal que presento este bebe, la
	 * información de esta forma está dada por una pareja de datos tipo (código,
	 * nombre)
	 * @param		 	formaNacimiento, información de la forma de nacimiento
	 * 						vaginal del bebe, es una pareja (InfoDatos) de tipo
	 * 						codigo, nombre.
	 */
	public void addFormaNacimientoVaginal(InfoDatos formaNacimiento)
	{
		if (formasNacimientoVaginal==null)
		{
			formasNacimientoVaginal= new ArrayList();
		}
		this.formasNacimientoVaginal.add(formaNacimiento);
	}


	/**
	 * @return Retorna lugar.
	 */
	public String getLugar()
	{
		return lugar;
	}
	/**
	 * @param lugar Asigna lugar.
	 */
	public void setLugar(String lugar)
	{
		this.lugar = lugar;
	}
	/**
	 * @return Retorna peso.
	 */
	public String getPeso()
	{
		return peso;
	}
	/**
	 * @param peso Asigna peso.
	 */
	public void setPeso(String peso)
	{
		this.peso = peso;
	}
	/**
	 * @return Retorna sexo.
	 */
	public int getSexo()
	{
		return sexo;
	}
	/**
	 * @param sexo Asigna sexo.
	 */
	public void setSexo(int sexo)
	{
		this.sexo = sexo;
	}
}