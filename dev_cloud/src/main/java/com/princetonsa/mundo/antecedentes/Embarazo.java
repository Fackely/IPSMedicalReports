/*
 * @(#)Embarazo.java
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
import java.util.Vector;

import util.InfoDatos;
import util.UtilidadFecha;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.EmbarazoDao;

/**
 * Clase para el manejo de todos los datos de los antecedentes de un embarazo
 *
 * @version 1.0, Abril 3, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class Embarazo 
{
	
	/**
	 * Código del embarazo
	 */
	private int codigo=0; 
	
	/**
	 * Fecha de terminación del embarazo. En formato "dd/mm/aaaa"
	 */
 	private String fechaTerminacion = "";
  
  	/**
  	 * Cadena con otra posible complicación que se presentó en el embarazo, si
  	 * no existe dentro de las opciones dadas.
  	 */
  	private Vector otraComplicacion = new Vector();
  
  	/** 
  	 * Meses de gestación del embarazo
  	 */
	private float mesesGestacion = 0;
    
  	/**
  	 * Complicacion durante el embarazo, pareja de datos de tipo (codigo,
  	 * nombre)
  	 */
  	private int[] complicacion;
  	
  	/**
  	 * Guardar los nombres de las complicaciones de cada embarazo
  	 */
  	private Vector nombresComplicaciones=new Vector();
    
    /**
     * Información sobre el trabajo de parto, pareja de datos de tipo (codigo,
     * nombre)
     */	
	private InfoDatos trabajoParto;
	
	/**
	 * Descripción del trabajo de parto si el valor de este es otros/otro
	 */
	private String otroTrabajoParto="";

  	/**
  	 * Lista con los hijos de este embarazo.
  	 */
  	private ArrayList hijos;
  	
  	/**
  	 * Manejo de la duración del trabajo de parto
  	 */
  	private String duracion;
  	
  	/**
  	 * Tiempo de la ruptura de membranas
  	 */
  	private String tiempoRupturaMembranas;
  	
  	/**
  	 * Legrado embarazo
  	 */
  	private String legrado;

	/**
	 * El DAO usado por el objeto
	 * <code>Embarazo</code> para acceder a la fuente de datos.
	 */
  	private static EmbarazoDao embarazoDao=null;

  	public void reset()
  	{
  		this.codigo=0;
  		this.fechaTerminacion="";
  		this.otraComplicacion=new Vector();
  		this.hijos=new ArrayList();
  		this.nombresComplicaciones=new Vector();
  		this.trabajoParto=new InfoDatos();
  		this.otroTrabajoParto="";
  		this.mesesGestacion=0;
  		this.complicacion=new int[0];
  	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init (String tipoBD) 
	{

		if (embarazoDao  == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			embarazoDao = myFactory.getEmbarazoDao();
		}

	}
  	
	/**
	 * Constructor del Embarazo.
	 */
	public Embarazo()
	{
		init(System.getProperty("TIPOBD"));
		hijos=new ArrayList();
	}

	/**
	 * Constructor del Embarazo que recibe todos los datos que componen este
	 * objeto
	 */
	public Embarazo(String fechaTerminacion, Vector otraComplicacion, int mesesGestacion, int[] complicacion, ArrayList hijos, InfoDatos trabajoParto, String otroTrabajoParto)
	{
		init(System.getProperty("TIPOBD"));

		this.fechaTerminacion=fechaTerminacion;
		this.otraComplicacion=otraComplicacion;
		this.mesesGestacion=mesesGestacion;
		this.complicacion=complicacion;
		this.hijos=hijos;
		this.trabajoParto=trabajoParto;
		this.otroTrabajoParto=otroTrabajoParto;
	}

	/**
	 * Funcionalidad para insertar el embarazo de esta paciente junto a todos
	 * sus hijos. Aunque este método no diga Transaccional, (Porque la
	 * funcionalidad es atómica), SI las maneja para su átomo (Es decir, si un
	 * hijo no puede ser insertado, no se inserta el embarazo)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoPaciente Código de la paciente a la que se le va a ingresar
	 * información de un embarazo (antecedentes gineco-obstetricos)
	 * @return int Retorna el número del embarazo
	 * @throws SQLException
	 */
	public int insertar(Connection con, int codigoPaciente) throws SQLException
	{
		this.codigo = embarazoDao.insertarTransaccional(con, codigoPaciente, this.mesesGestacion, this.reestructurarFecha(this.fechaTerminacion), this.complicacion, this.otraComplicacion, this.trabajoParto.getCodigo(), this.otroTrabajoParto, duracion, tiempoRupturaMembranas, legrado, "empezar");
		for (int i=0;i<this.hijos.size();i++)
		{
			HijoBasico hijoAInsertar=(HijoBasico)(hijos.get(i));
			if (i==hijos.size()-1)
			{
				//Es la última vez que entre al for
				//y como este insertar NO es transaccional
				//debe finalizar esta (la transaccion)
				hijoAInsertar.insertarTransaccional(con, codigoPaciente, this.codigo, "finalizar");
			}
			else
			{
				hijoAInsertar.insertarTransaccional(con, codigoPaciente, this.codigo, "continuar");
			}
		}
		
		return this.codigo;
	}
	
	/**
	 * Funcionalidad para insertar el embarazo de esta paciente junto a todos
	 * sus hijos. Soporta Transaccionalidad definida por el usuario a través del
	 * parámetro "estado"
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoPaciente Código  de la paciente a la que se le va a ingresar
	 * información de un embarazo (antecedentes gineco-obstetricos)
  	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return int Retorna el número del embarazo
	 * @throws SQLException
	 */
	public int insertarTransaccional(Connection con, int codigoPaciente, String estado) throws SQLException
	{

		if (estado.equals("finalizar")&&this.hijos.size()==0)
		{
			this.codigo=embarazoDao.insertarTransaccional(con, codigoPaciente, this.mesesGestacion, this.reestructurarFecha(this.fechaTerminacion), this.complicacion, this.otraComplicacion, this.trabajoParto.getCodigo(), this.otroTrabajoParto, duracion, tiempoRupturaMembranas, legrado, estado);
		}
		else 
		if (estado.equals("finalizar"))
		{
			//Aún no termina la transacción, solamente cuando termine de insertar el último hijo
			this.codigo=embarazoDao.insertarTransaccional(con, codigoPaciente, this.mesesGestacion, this.reestructurarFecha(this.fechaTerminacion), this.complicacion, this.otraComplicacion, this.trabajoParto.getCodigo(), this.otroTrabajoParto, duracion, tiempoRupturaMembranas, legrado, "continuar");
		}
		else
		{
			//Tanto si empezamos como si continuamos, el estado nos sirve
			this.codigo=embarazoDao.insertarTransaccional(con, codigoPaciente, this.mesesGestacion, this.reestructurarFecha(this.fechaTerminacion), this.complicacion, this.otraComplicacion, this.trabajoParto.getCodigo(), this.otroTrabajoParto, duracion, tiempoRupturaMembranas, legrado, estado);
		}
		
		for (int i=0;i<this.hijos.size();i++)
		{
			HijoBasico hijoAInsertar=(HijoBasico)(hijos.get(i));
			//Si empezamos la transaccion con el embarazo NO la tenemos que empezar
			//de nuevo, luego la ponemos en "continuar". Este caso se pone por claridad
			//basta ponerlo en el else y no tenemos mayor problema
			if (estado.equals("empezar"))
			{
				hijoAInsertar.insertarTransaccional(con, codigoPaciente, this.codigo, "continuar");
			}
			//Si por el contrario vamos a finalizar, en la última iteracion del for debemos finalizarla
			else if (  estado.equals("finalizar")&&(i==hijos.size()-1) )
			{
				//Es la última vez que entre al for
				//debe finalizar esta (la transaccion)
				hijoAInsertar.insertarTransaccional(con, codigoPaciente, this.codigo, "finalizar");
			}
			//Si no es ninguno de los casos anteriores debo "continuar"
			else
			{
				hijoAInsertar.insertarTransaccional(con, codigoPaciente, this.codigo, "continuar");
			}
		}
		
		
		return this.codigo;
		
	}

	/**
	 * Funcionalidad para modificar el embarazo de esta paciente junto a todos
	 * sus hijos. Aunque este método no diga Transaccional, (Porque la
	 * funcionalidad es atómica), SI las maneja para su átomo (Es decir, si un
	 * hijo no puede ser insertado, no se inserta el embarazo)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoPaciente Código de la paciente a la que se le va a modificar
	 * información de un embarazo (antecedentes gineco-obstetricos)
	 * @return int Retorna 0 si no se pudo modificar 1 de lo contrario
	 * @throws SQLException
	 */
	
	public int modificar(Connection con, int codigoPaciente) throws SQLException
	{
		int resp0=0;
		
		//Si no existe embarazo, es una inserción tradicional
		if ( !embarazoDao.existeEmbarazo(con, codigoPaciente, this.codigo) )
		{
			this.insertar(con, codigoPaciente);
		}
		else
		{

			embarazoDao.modificarTransaccional(con, codigoPaciente, this.codigo, this.mesesGestacion, this.reestructurarFecha(this.fechaTerminacion), this.complicacion, this.otraComplicacion, this.trabajoParto.getCodigo(), this.otroTrabajoParto, this.duracion, this.tiempoRupturaMembranas, this.legrado, "empezar");

			for (int i=0;i<this.hijos.size();i++)
			{
				HijoBasico hijoAModificar=(HijoBasico)(hijos.get(i));
				if (i==hijos.size()-1)
				{
					//Es la última vez que entre al for
					//y como este insertar NO es transaccional
					//debe finalizar esta (la transaccion)
					hijoAModificar.modificarTransaccional(con, codigoPaciente, this.codigo, "finalizar");
				}
				else
				{
					hijoAModificar.modificarTransaccional(con, codigoPaciente, this.codigo, "continuar");
				}
			}

		}
		
		return resp0;
	}
	
	/**
	 * Funcionalidad para modificar el embarazo de esta paciente junto a todos
	 * sus hijos. Soporta Transaccionalidad definida por el usuario a través del
	 * parámetro "estado"
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoPaciente Código  de la paciente a la que se le va a
	 * modificar información de un embarazo (antecedentes gineco-obstetricos)
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return int Retorna 0 si no se pudo modificar 1 de lo contrario
	 * @throws SQLException
	 */
	public int modificarTransaccional(Connection con, int codigoPaciente, String estado) throws SQLException
	{
		//Si no existe embarazo, es una inserción tradicional
		if ( !embarazoDao.existeEmbarazo(con, codigoPaciente, this.codigo) )
		{
			this.insertarTransaccional(con, codigoPaciente, estado);
		}
		else
		{
			if (estado.equals("finalizar"))
			{
				//Aún no termina la transacción, solamente cuando termine de modificar el último hijo
				embarazoDao.modificarTransaccional(con, codigoPaciente, this.codigo, this.mesesGestacion, this.reestructurarFecha(this.fechaTerminacion), this.complicacion, this.otraComplicacion, this.trabajoParto.getCodigo(), this.otroTrabajoParto, this.duracion, this.tiempoRupturaMembranas, this.legrado, "continuar");
			}
			else
			{
				//Tanto si empezamos como si continuamos, el estado nos sirve
				embarazoDao.modificarTransaccional(con, codigoPaciente,this.codigo, this.mesesGestacion, this.reestructurarFecha(this.fechaTerminacion), this.complicacion, this.otraComplicacion, this.trabajoParto.getCodigo(), this.otroTrabajoParto, this.duracion, this.tiempoRupturaMembranas, this.legrado, estado);
			}
			
			for (int i=0;i<this.hijos.size();i++)
			{
				HijoBasico hijoAModificar=(HijoBasico)(hijos.get(i));
				//Si empezamos la transaccion con el embarazo NO la tenemos que empezar
				//de nuevo, luego la ponemos en "continuar". Este caso se pone por claridad
				//basta ponerlo en el else y no tenemos mayor problema
				if (estado.equals("empezar"))
				{
					hijoAModificar.modificarTransaccional(con, codigoPaciente, this.codigo, "continuar");
				}
				//Si por el contrario vamos a finalizar, en la última iteracion del for debemos finalizarla
				else if (  estado.equals("finalizar")&&(i==hijos.size()-1) )
				{
					//Es la última vez que entre al for
					//debe finalizar esta (la transaccion)
					hijoAModificar.modificarTransaccional(con, codigoPaciente, this.codigo, "finalizar");
				}
				//Si no es ninguno de los casos anteriores debo "continuar"
				else
				{
					hijoAModificar.modificarTransaccional(con, codigoPaciente, this.codigo, "continuar");
				}
				
			}
		
		}
		
		return this.codigo;
		
	}
	
	/**
	 * Método que cambia el formato de la fecha manejado por el usuario (dd-mm-
	 * aaaa ->estándar Latinoamericano) al formato manejado por la fuente de
	 * datos (yyyy-mm-dd -> estándar Norteamericano)
	 * 
	 * @param fecha Fecha en el formato manejado por el usuario
	 * @return String Fecha en el formato manejado por la fuente de datos
	 */
	public String reestructurarFecha(String fecha)
	{
	/*	if( fecha != null )
		{ 
			String[] arregloFecha = (fecha).split("/");
			if( arregloFecha.length == 3 )
			{
				fecha = new String();
				fecha = arregloFecha[2]+"/"+arregloFecha[1]+"/"+arregloFecha[0];
			}					
		}*/
		return UtilidadFecha.conversionFormatoFechaABD(fecha);		
	}

	/**
	 * Dado un embarazo, este método me carga todos los hijos de este embarazo
	 * 
	 * @param con
	 * @param codigoPaciente Código de la paciente
	 * @throws SQLException
	 */	
	public void cargarHijos (Connection con, int codigoPaciente) throws SQLException
	{
		ResultSetDecorator rsACargar=embarazoDao.cargarHijos(con, codigoPaciente, this.codigo);
		HijoBasico hijoTemporal= new HijoBasico ();
		
		//Limpiamos la lista de hijos
		
		hijos=new ArrayList();
		
		while (rsACargar.next())
		{
			hijoTemporal= new HijoBasico ();
			hijoTemporal.setNumeroHijoEmbarazo(rsACargar.getInt("numeroHijoEmbarazo"));
			hijoTemporal.setVivo(rsACargar.getBoolean("vivo"));
			hijoTemporal.setOtraFormaNacimientoVaginal(rsACargar.getString("otroTipoPartoVaginal"));		
			hijoTemporal.setCesarea(rsACargar.getBoolean("cesarea"));
			hijoTemporal.setAborto(rsACargar.getBoolean("aborto"));
			hijoTemporal.setOtroTipoParto(rsACargar.getString("otroTipoParto"));
			hijoTemporal.setPeso(rsACargar.getString("peso"));
			hijoTemporal.setSexo(rsACargar.getInt("sexo"));
			hijoTemporal.setLugar(rsACargar.getString("lugar"));
			
			hijoTemporal.cargarTiposPartoVaginal(con, codigoPaciente, this.codigo);
			
			hijos.add(hijoTemporal);
		}
	}

	/**
	 * Retorna la fecha de terminación del embarazo
	 * @return 			String, fecha formato "dd/mm/aaaa"
	 */
	public String getFechaTerminacion() 
	{
		return fechaTerminacion;
	}

	/**
	 * Asigna la fecha de terminación del embarazo
	 * @param 		String, fechaTerminacion formato "dd/mm/aaaa"
	 */
	public void setFechaTerminacion(String fechaTerminacion) 
	{
		this.fechaTerminacion = fechaTerminacion;
	}



	/**
	 * Retorna la complicación que se presentó durante el embarazo.
	 * @return 			String, complicacion
	 */
	public Vector getOtraComplicacion() 
	{
		return otraComplicacion;
	}

	/**
	 * Asigna  la  complicación que se presentó durante el embarazo.
	 * @param 		String, otraComplicacion
	 */
	public void setOtraComplicacion(Vector otraComplicacion)
	{
		this.otraComplicacion = otraComplicacion;
	}



	/**
	 * Retorna el número de meses de gestación
	 * @return 			int, meses de gestación
	 */
	public float getMesesGestacion() 
	{
		return mesesGestacion;
	}

	/**
	 * Asigna el número de meses de gestación
	 * @param 		int, mesesGestacion
	 */
	public void setMesesGestacion(float mesesGestacion) 
	{
		this.mesesGestacion = mesesGestacion;
	}

	/**
	 * Retorna la complicacion durante el embarazo, pareja (codigo, nombre)
	 * @return 			InfoDatos, compliacion
	 */
	public int[] getComplicacion() 
	{
		return complicacion;
	}

	/**
	 * Asigna la complicacion durante el embarazo, pareja (codigo, nombre)
	 * @param 		InfoDatos, complicacion
	 */
	public void setComplicacion(int[] complicacion) 
	{
		this.complicacion = complicacion;
	}

	/**
	 * Retorna la lista de hijos (HijoBasico) de este embarazo
	 * @return 			ArrayList, lista de HijoBasico
	 */
	public ArrayList getHijos() 
	{
		return hijos;
	}

	/**
	 * Asigna la lista de hijos (HijoBasico) de este embarazo
	 * @param 		ArrayList, hijos
	 */
	public void setHijos(ArrayList hijos) 
	{
		this.hijos = hijos;
	}
	
	/**
	 * Adiciona un nuevo hijo de este embarazo
	 * @param			HijoBasico, hijo
	 */
	public void addHijo(HijoBasico hijo)
	{
		if (hijos==null)
		{
			hijos=new ArrayList();
		}
		
		this.hijos.add(hijo);
	}
 
	/**
	 * Retorna el codigo de este embarazo (El que identifica este embarazo del
	 * resto de la paciente).
	 * 
	 * @return int
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * Establece el codigo de este embarazo (El que identifica este embarazo del
	 * resto de la paciente).
	 * 
	 * @param codigo El codigo a establecer
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * Retorna el otro trabajo parto.
	 * @return String
	 */
	public String getOtroTrabajoParto() {
		return otroTrabajoParto;
	}

	/**
	 * Retorna el trabajo de parto.
	 * @return InfoDatos
	 */
	public InfoDatos getTrabajoParto() {
		return trabajoParto;
	}

	/**
	 * Establece el otro trabajo de parto.
	 * @param otroTrabajoParto El otro trabajo parto a establecer
	 */
	public void setOtroTrabajoParto(String otroTrabajoParto) {
		this.otroTrabajoParto = otroTrabajoParto;
	}

	/**
	 * Establece el trabajo de parto.
	 * @param trabajoParto El trabajo de parto a establecer
	 */
	public void setTrabajoParto(InfoDatos trabajoParto) {
		this.trabajoParto = trabajoParto;
	}

	/**
	 * @return Retorna nombresComplicaciones.
	 */
	public Vector getNombresComplicaciones()
	{
		return nombresComplicaciones;
	}
	/**
	 * @param nombresComplicaciones Asigna nombresComplicaciones.
	 */
	public void setNombresComplicaciones(Vector nombresComplicaciones)
	{
		this.nombresComplicaciones = nombresComplicaciones;
	}
	
	/**
	 * Método para ingresar complicaciones
	 * @param con
	 * @param secuencia
	 * @param codigoEmbarazo
	 * @param codigoPaciente
	 * @param complicaciones
	 * @param complicacionesOtras
	 * @return numero de inserciones hechas en la BD
	 */
	public static int ingresarComplicaciones(Connection con, int codigoEmbarazo, int codigoPaciente, int[] complicaciones, Vector complicacionesOtras)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmbarazoDao().ingresarComplicaciones(con, codigoEmbarazo, codigoPaciente, complicaciones, complicacionesOtras);
	}

	/**
	 * @return Retorna duracion.
	 */
	public String getDuracion()
	{
		return duracion;
	}
	/**
	 * @param duracion Asigna duracion.
	 */
	public void setDuracion(String duracion)
	{
		this.duracion = duracion;
	}
	/**
	 * @return Retorna legrado.
	 */
	public String getLegrado()
	{
		return legrado;
	}
	/**
	 * @param legrado Asigna legrado.
	 */
	public void setLegrado(String legrado)
	{
		this.legrado = legrado;
	}
	/**
	 * @return Retorna tiempoRupturaMembranas.
	 */
	public String getTiempoRupturaMembranas()
	{
		return tiempoRupturaMembranas;
	}
	/**
	 * @param tiempoRupturaMembranas Asigna tiempoRupturaMembranas.
	 */
	public void setTiempoRupturaMembranas(String tiempoRupturaMembranas)
	{
		this.tiempoRupturaMembranas = tiempoRupturaMembranas;
	}
}