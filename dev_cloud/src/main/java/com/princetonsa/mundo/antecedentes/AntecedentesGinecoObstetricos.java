/*
 * @(#)AntecedentesGinecoObstetricos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
 
package com.princetonsa.mundo.antecedentes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SimpleTimeZone;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.InfoDatos;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.AntecedentesGinecoObstetricosForm;
import com.princetonsa.dao.AntecedentesGinecoObstetricosDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para el manejo de todos los datos de antecedentes gineco-obstétricos.
 * incluyendo embarazos, antecedentes gineco obstetricos históricos y métodos
 * anticonceptivos
 *
 * @version 1.0, Abril 3, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
@SuppressWarnings({"rawtypes","deprecation"})
public class AntecedentesGinecoObstetricos 
{
	/**
	 * Objeto Log, para manejar el log de cambios para
	 * AntecedentesGinecoObstetricos
	 */
	private Logger logger = Logger.getLogger(AntecedentesGinecoObstetricos.class);
	
	/**
	 * Paciente para el cual se graban los antecedentes.
	 */
	private PersonaBasica paciente;
		
	/**
	 * Edad en la que se inicia la primera menstruación, solo si la edad no está
	 * incluida en ninguno de los rangos proporcionados.
	 */
	private String otroEdadMenarquia = "";
  
  	/**
  	 * Edad en la que se inicia la primera menstruación, es una pareja de datos
  	 * de tipo (codigo, nombre(rango)).
  	 */
  	private InfoDatos rangoEdadMenarquia;
  
  	/**
  	 * Edad en que se finaliza la menstruación, solo si la edad no está incluída
  	 * en ninguno de los rangos proporcionados.
  	 */
  	private String otroEdadMenopausia = "";
  	
  	/**
  	 * Edad en que se finaliza la menstruacion, es una pareja de datos de tipo
  	 * (codigo, nombre(rango)).
  	 */
	private InfoDatos rangoEdadMenopausia;
  
  	/**
  	 * Observaciones generales de todos los antecedentes gineco-obstétricos.
  	 */
  	private String observaciones = "";
  	
  	/**
  	 * Edad en años del inicio de la vida sexual
  	 */
  	private int inicioVidaSexual;
  	
  	/**
  	 * Edad en años del inicio de la vida obstetrica
  	 */
  	private int inicioVidaObstetrica;
  	
  	/**
  	 * Vector con la información de los embarazos de la paciente. 
  	 */
	private ArrayList embarazos;
  	
	/**
	 * Conjunto de antecedentes que manejan datos historicos
	 */	
	private ArrayList antecedentesHistoricos;  	
  
  	/**
  	 * Conjunto de Métodos Anticonceptivos usados por la paciente
  	 */
  	private ArrayList metodosAnticonceptivos;
  	
  	/**
  	 * Fecha en la cual se graban los datos.
  	 */
  	private String fecha = "";
  
  	/**
  	 * Hora en la cual se graban los datos.
  	 */
  	private String hora = "";
  
  	/**
  	 * login del usuario que graba los datos.
  	 */
  	private String loginUsuario = "";
  
	/**
	 * El DAO usado por el objeto <code>AntecedenteGinecoObstetrico</code> para acceder a la fuente de datos.
	 */
	private static  AntecedentesGinecoObstetricosDao antecedenteGinecoObstetricoDao=null;


	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init (String tipoBD) 
	{

		if (antecedenteGinecoObstetricoDao  == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			antecedenteGinecoObstetricoDao = myFactory.getAntecedentesGinecoObstetricosDao();
		}

	}

	/**
	 * Constructor que recibe todos los datos de AntecedentesGinecoObstetricos.
	 */
	public AntecedentesGinecoObstetricos() 
	{
		init(System.getProperty("TIPOBD"));
		this.antecedentesHistoricos= new ArrayList();
		this.embarazos= new ArrayList();
		this.metodosAnticonceptivos= new ArrayList();
		this.rangoEdadMenarquia = new InfoDatos("-1", "");
		this.rangoEdadMenopausia = new InfoDatos("-1", "");
	}
	
	/**
	 * Constructor que recibe todos los datos de un AntecedenteGinecoObstetrico
	 * con excepción de los métodos anticonceptivos
	 * 
	 * @param paciente Objeto PersonaBasica que  tiene toda la información
	 * necesaria sobre un paciente que en algún momento pueda necesitar un
	 * AntecedenteGinecoObstetrico
	 * @param otroEdadMenarquia Si la edad de menarquía se presentó en una edad
	 * o rango fuera de los que el médico puede seleccionar (presente en la
	 * fuente de datos)
	 * @param rangoEdadMenarquia Código de un rango predefinido en la base de
	 * datos para la edad de menarquia
	 * @param otroEdadMenopausia Si la edad de menopausia se presentó en una edad
	 * o rango fuera de los que el médico puede seleccionar (presente en la
	 * fuente de datos)
	 * @param rangoEdadMenopausia Código de un rango predefinido en la base de
	 * datos para la edad de menopausia
	 * @param observaciones Observaciones hechas por el médico
	 * @param embarazos ArrayList con todos los Embarazos de esta paciente
	 * @param antecedentesHistoricos ArrayList con todos los nuevos antecedentes
	 * historicos de esta paciente
	 * @param fecha Fecha de creación de este AntecedenteGinecoObstetrico
	 * @param hora Hora de creación de este AntecedenteGinecoObstetrico
	 * @param loginUsuario Login del médico que crea este
	 * AntecedenteGinecoObstetrico
	 */
	public AntecedentesGinecoObstetricos(PersonaBasica paciente, String otroEdadMenarquia, InfoDatos rangoEdadMenarquia, String otroEdadMenopausia, InfoDatos rangoEdadMenopausia, String observaciones, ArrayList embarazos, ArrayList antecedentesHistoricos, String fecha, String hora, String loginUsuario) 
	{
		init(System.getProperty("TIPOBD"));
		
		this.paciente=paciente;
		this.otroEdadMenarquia=otroEdadMenarquia;
		this.rangoEdadMenarquia=rangoEdadMenarquia;
		this.otroEdadMenopausia=otroEdadMenopausia;
		this.rangoEdadMenopausia=rangoEdadMenopausia;
		this.embarazos=embarazos;
		this.antecedentesHistoricos=antecedentesHistoricos;
		this.fecha=fecha;
		this.hora=hora;
		this.loginUsuario=loginUsuario;
		this.observaciones=observaciones;
		this.metodosAnticonceptivos= new ArrayList();
	}
	
	/**
	 * Constructor que recibe todos los datos de un AntecedenteGinecoObstetrico
	 * 
	 * @param paciente Objeto PersonaBasica que  tiene toda la información
	 * necesaria sobre un paciente que en algún momento pueda necesitar un
	 * AntecedenteGinecoObstetrico
	 * @param otroEdadMenarquia Si la edad de menarquía se presentó en una edad
	 * o rango fuera de los que el médico puede seleccionar (presente en la
	 * fuente de datos)
	 * @param rangoEdadMenarquia Código de un rango predefinido en la base de
	 * datos para la edad de menarquia
	 * @param otroEdadMenopausia Si la edad de menopausia se presentó en una edad
	 * o rango fuera de los que el médico puede seleccionar (presente en la
	 * fuente de datos)
	 * @param rangoEdadMenopausia Código de un rango predefinido en la base de
	 * datos para la edad de menopausia
	 * @param observaciones Observaciones hechas por el médico
	 * @param embarazos ArrayList con todos los Embarazos de esta paciente
	 * @param antecedentesHistoricos ArrayList con todos los nuevos antecedentes
	 * historicos de esta paciente
	 * @param metodosAnticonceptivos ArrayList con la información de todos los
	 * métodos anticonceptivos usados alguna vez por esta paciente
	 * @param fecha Fecha de creación de este AntecedenteGinecoObstetrico
	 * @param hora Hora de creación de este AntecedenteGinecoObstetrico
	 * @param loginUsuario Login del médico que crea este
	 * AntecedenteGinecoObstetrico
	 */
	public AntecedentesGinecoObstetricos(PersonaBasica paciente, String otroEdadMenarquia, InfoDatos rangoEdadMenarquia, String otroEdadMenopausia, InfoDatos rangoEdadMenopausia, String observaciones, ArrayList embarazos, ArrayList antecedentesHistoricos, ArrayList metodosAnticonceptivos, String fecha, String hora, String loginUsuario) 
	{
		init(System.getProperty("TIPOBD"));
		
		this.paciente=paciente;
		this.otroEdadMenarquia=otroEdadMenarquia;
		this.rangoEdadMenarquia=rangoEdadMenarquia;
		this.otroEdadMenopausia=otroEdadMenopausia;
		this.rangoEdadMenopausia=rangoEdadMenopausia;
		this.embarazos=embarazos;
		this.antecedentesHistoricos=antecedentesHistoricos;
		this.fecha=fecha;
		this.hora=hora;
		this.loginUsuario=loginUsuario;
		this.observaciones=observaciones;
		this.metodosAnticonceptivos= metodosAnticonceptivos;
		
	}


	/**
	 * Método que inserta un AntecedenteGinecoObstetrico y todo lo que tiene que
	 * ver con el (embarazos, métodos anticonceptivos, etc). Aunque el nombre no
	 * diga transaccional, por dentro lo es, evitando que quede información
	 * incompleta (el método transaccional se refiere a su interacción con otros
	 * objetos, Ej si quisieramos meter en una misma transacción la cuenta y el
	 * AntecedenteGinecoObstetrico)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param selecDolorMenstruacion
	 * @return int 1 se se pudo insertar, 0 si no
	 * @throws SQLException
	 */
	public int insertar(Connection con) throws SQLException
	{
		int i=0, resp0=0;
		
		//Si no hay ni antecedentes Historicos GO ni embarazos
		//debemos insertarlo sin transacción
		if (antecedentesHistoricos==null||embarazos==null||antecedentesHistoricos.size()<1||embarazos.size()<1)
		{
			resp0=antecedenteGinecoObstetricoDao.insertar(con, this.paciente.getCodigoPersona(), this.rangoEdadMenarquia.getCodigo(), this.otroEdadMenarquia, this.rangoEdadMenopausia.getCodigo(), this.otroEdadMenopausia, this.observaciones, this.inicioVidaSexual, this.inicioVidaObstetrica, this.loginUsuario, metodosAnticonceptivos);
			
			if( resp0 > 0 )
			{
				if( logger.isInfoEnabled() )
					logger.info("["+this.getDateAndTime()+"] El usuario ' "+this.getLoginUsuario()+ " ' insertó un nuevo registro de información de antecedentes ginecoobstétricos (solo datos generales, sin información de historicos o embarazos), para el paciente con identificación : "+this.getPaciente().getCodigoTipoIdentificacionPersona()+" "+this.getPaciente().getNumeroIdentificacionPersona());
			}
		}
		else
		{
			//Si hay al menos alg?n elemento de las listas, empezamos una
			//transacción
			resp0=antecedenteGinecoObstetricoDao.insertarTransaccional(con, this.paciente.getCodigoPersona(), this.rangoEdadMenarquia.getCodigo(), this.otroEdadMenarquia, this.rangoEdadMenopausia.getCodigo(), this.otroEdadMenopausia, this.observaciones, this.inicioVidaSexual, this.inicioVidaObstetrica, this.loginUsuario, metodosAnticonceptivos, "empezar");
			
			if( resp0 > 0 )
			{
				if( logger.isInfoEnabled() )
					logger.info("["+this.getDateAndTime()+"] El usuario ' "+this.getLoginUsuario()+ " ' insertó un nuevo registro de información de antecedentes ginecoobstétricos, para el paciente con identificación :  "+this.getPaciente().getCodigoTipoIdentificacionPersona()+" "+this.getPaciente().getNumeroIdentificacionPersona());
			}			
		}
		
		if (antecedentesHistoricos!=null)
		{
			
			AntecedentesGinecoObstetricosHistorico antecedenteHistorico;
			for (i=0;i<antecedentesHistoricos.size();i++)
			{
				antecedenteHistorico= (AntecedentesGinecoObstetricosHistorico )antecedentesHistoricos.get(i);
				//Si es el último elemento y no hay embarazos terminamos la transaccion
				//en caso contrario continuamos
				if (i==antecedentesHistoricos.size()-1&&( embarazos==null||embarazos.size()<1 ) )
				{
					antecedenteHistorico.insertarTransaccional(con, this.paciente.getCodigoPersona(),"finalizar");
				}
				else
				{
					antecedenteHistorico.insertarTransaccional(con, this.paciente.getCodigoPersona(), "continuar");
				}
			}
		}
		if (embarazos!=null)
		{
			Embarazo embarazo;
			for(i=0;i<embarazos.size();i++)
			{
				embarazo=(Embarazo)embarazos.get(i);
				if (i==embarazos.size()-1)
				{
					embarazo.insertarTransaccional(con, this.paciente.getCodigoPersona(), "finalizar");
				}
				else
				{
					embarazo.insertarTransaccional(con, this.paciente.getCodigoPersona(), "continuar");
				}
			}
		}
		
		return resp0;
	}
	
	

	/**
	 * Método que inserta un AntecedenteGinecoObstetrico y todo lo que tiene que
	 * ver con el (embarazos, métodos anticonceptivos, etc). Como es
	 * transaccional recibe un paràmetro donde se especifíca en que punto queda
	 * esta inserción dentro de la transacción (la empieza?, esta en el medio o
	 * la termina)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de la transacción (empezar, continuar, finalizar)
	 * @return int 1 se se pudo insertar, 0 si no
	 * @throws SQLException
	 */

/*	public int insertarTransaccional(Connection con, String estado) throws SQLException
	{
		int i=0, resp0=0;
		

		if (estado.equals("empezar"))
		{
			resp0=antecedenteGinecoObstetricoDao.insertarTransaccional(con, this.paciente.getCodigoPersona(), this.rangoEdadMenarquia.getCodigo(), this.otroEdadMenarquia, this.rangoEdadMenopausia.getCodigo(), this.otroEdadMenopausia, this.observaciones, this.inicioVidaSexual, this.inicioVidaObstetrica, this.loginUsuario, metodosAnticonceptivos, "empezar");
		}
		else if (antecedentesHistoricos==null||embarazos==null||antecedentesHistoricos.size()<1||embarazos.size()<1)
		{
			//Si no hay ni antecedentes Historicos GO ni embarazos
			//y el estado es finalizar, debemos ponerlo en el antecedenteGinecoObstetrico
			if (estado.equals("finalizar"))
			{
				resp0=antecedenteGinecoObstetricoDao.insertarTransaccional(con, this.paciente.getCodigoPersona(), this.rangoEdadMenarquia.getCodigo(), this.otroEdadMenarquia, this.rangoEdadMenopausia.getCodigo(), this.otroEdadMenopausia, this.observaciones, this.inicioVidaSexual, this.inicioVidaObstetrica, this.loginUsuario, metodosAnticonceptivos, "finalizar");
			}
			else
			{
				resp0=antecedenteGinecoObstetricoDao.insertarTransaccional(con, this.paciente.getCodigoPersona(), this.rangoEdadMenarquia.getCodigo(), this.otroEdadMenarquia, this.rangoEdadMenopausia.getCodigo(), this.otroEdadMenopausia, this.observaciones, this.inicioVidaSexual, this.inicioVidaObstetrica, this.loginUsuario, metodosAnticonceptivos, "continuar");
			}
		}
		else
		{
			//En cualquier otro caso debemos continuar...
			resp0=antecedenteGinecoObstetricoDao.insertarTransaccional(con, this.paciente.getCodigoPersona(), this.rangoEdadMenarquia.getCodigo(), this.otroEdadMenarquia, this.rangoEdadMenopausia.getCodigo(), this.otroEdadMenopausia, this.observaciones, this.inicioVidaSexual, this.inicioVidaObstetrica, this.loginUsuario, metodosAnticonceptivos, "continuar");
		}
		
		if (antecedentesHistoricos!=null)
		{
			AntecedentesGinecoObstetricosHistorico antecedenteHistorico;
			for (i=0;i<antecedentesHistoricos.size();i++)
			{
				antecedenteHistorico= (AntecedentesGinecoObstetricosHistorico )antecedentesHistoricos.get(i);
				//Si es el último elemento, el estado es finalizar y no hay embarazos terminamos 
				//la transaccion en caso contrario continuamos
				if (i==antecedentesHistoricos.size()-1&&( embarazos==null||embarazos.size()<1 ) && estado.equals("finalizar"))
				{
					antecedenteHistorico.insertarTransaccional(con, this.paciente.getCodigoPersona(), "finalizar");
				}
				else
				{
					//En cualquier otro caso hay que continuar
					antecedenteHistorico.insertarTransaccional(con, this.paciente.getCodigoPersona(), "continuar");
				}
			}
		}
		if (embarazos!=null)
		{
			Embarazo embarazo;
			for(i=0;i<embarazos.size();i++)
			{
				embarazo=(Embarazo)embarazos.get(i);
				//Si es el último elementoy  el estado es finalizar terminamos 
				//la transaccion en caso contrario continuamos
				if (i==embarazos.size()-1&&estado.equals("finalizar"))
				{
					embarazo.insertarTransaccional(con, this.paciente.getCodigoPersona(), "finalizar");
				}
				else
				{
					embarazo.insertarTransaccional(con, this.paciente.getCodigoPersona(), "continuar");
				}
			}
		}
		
		return resp0;
	}

*/


	/**
	 * Método que modifica un AntecedenteGinecoObstetrico y todo lo que tiene
	 * que ver con el (embarazos, métodos anticonceptivos, etc). Aunque el
	 * nombre no diga transaccional, por dentro lo es, evitando que quede
	 * información incompleta (el método transaccional se refiere a su
	 * interacción con otros objetos, Ej si quisieramos meter en una misma
	 * transacción la cuenta y la modificación del AntecedenteGinecoObstetrico)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return int 1 se se pudo insertar, 0 si no
	 * @throws SQLException
	 */

	//
	///----------ARMANDO
	public int modificar(Connection con) throws SQLException
	{
		int i=0, resp0=0;

		
		//System.out.print("\n\n\n Entro a Modificar \n\n\n");
		//Si no hay ni antecedentes Historicos GO ni embarazos
		//debemos modificarlo sin transacción
		
	 if (!antecedenteGinecoObstetricoDao.existeAntecedente(con, this.paciente.getCodigoPersona()))
		{
			if(!this.otroEdadMenarquia.equals("") || this.rangoEdadMenarquia.getCodigo() !=-1 || !this.otroEdadMenopausia.equals("") || this.rangoEdadMenopausia.getCodigo() != -1 || !this.observaciones.equals("") || this.inicioVidaSexual !=0 || this.inicioVidaObstetrica !=0 || !this.embarazos.isEmpty() || !this.metodosAnticonceptivos.isEmpty() || !this.antecedentesHistoricos.isEmpty())
			{
				return this.insertar(con);	
			}
			
		}
		else if (antecedentesHistoricos==null && embarazos==null && antecedentesHistoricos.size()<1 && embarazos.size()<1)
		{
			
			resp0=antecedenteGinecoObstetricoDao.modificar(con, this.paciente.getCodigoPersona(), this.rangoEdadMenarquia.getCodigo(), this.otroEdadMenarquia, this.rangoEdadMenopausia.getCodigo(), this.otroEdadMenopausia, this.observaciones, this.inicioVidaSexual, this.inicioVidaObstetrica, this.loginUsuario, metodosAnticonceptivos);

			if( resp0 > 0 )
			{
				if( logger.isInfoEnabled() )
					logger.info("["+this.getDateAndTime()+"] El usuario ' "+this.getLoginUsuario()+ " ' insertó un nuevo registro de actualización de información de antecedentes ginecoobstétricos (solo datos generales, sin información de historicos o embarazos), para el paciente con identificación :  "+this.getPaciente().getCodigoTipoIdentificacionPersona()+" "+this.getPaciente().getNumeroIdentificacionPersona());
			}						
		}
		else
		{
			//Si hay al menos algún elemento de las listas, empezamos una
			//transacción
			
			resp0=antecedenteGinecoObstetricoDao.modificarTransaccional(con, this.paciente.getCodigoPersona(), this.rangoEdadMenarquia.getCodigo(), this.otroEdadMenarquia, this.rangoEdadMenopausia.getCodigo(), this.otroEdadMenopausia, this.observaciones, this.inicioVidaSexual, this.inicioVidaObstetrica, this.loginUsuario, metodosAnticonceptivos, "empezar");

			if( resp0 > 0 )
			{
				if( logger.isInfoEnabled() )
					logger.info("["+this.getDateAndTime()+"] El usuario ' "+this.getLoginUsuario()+ " ' insertó un nuevo registro de actualización de información de antecedentes ginecoobstétricos, para el paciente con identificación :  "+this.getPaciente().getCodigoTipoIdentificacionPersona()+" "+this.getPaciente().getNumeroIdentificacionPersona());
			}									
		}
		
		//A pesar de ser modificar, los antecedentes historicos van por consulta
		//es decir no se pueden modificar, luego todos se insertan
		if(this.otroEdadMenarquia==null)
		{
			this.otroEdadMenarquia="";
		}
		if(this.otroEdadMenopausia==null)
		{
			this.otroEdadMenopausia="";
		}
		
		if(!this.otroEdadMenarquia.equals("") || this.rangoEdadMenarquia.getCodigo() !=-1 || !this.otroEdadMenopausia.equals("") || this.rangoEdadMenopausia.getCodigo() != -1 || !this.observaciones.equals("") || this.inicioVidaSexual !=0 || this.inicioVidaObstetrica !=0 || !this.embarazos.isEmpty() || !this.metodosAnticonceptivos.isEmpty() || !this.antecedentesHistoricos.isEmpty())
		{
		
			if (antecedentesHistoricos!=null)
			{
				AntecedentesGinecoObstetricosHistorico antecedenteHistorico;
				for (i=0;i<antecedentesHistoricos.size();i++)
				{
					antecedenteHistorico= (AntecedentesGinecoObstetricosHistorico )antecedentesHistoricos.get(i);
								
					//Si es el último elemento y no hay embarazos terminamos la transaccion
					//en caso contrario continuamos
					if (i==antecedentesHistoricos.size()-1&&( embarazos==null||embarazos.size()<1 ) )
					{
						antecedenteHistorico.insertarTransaccional(con, this.paciente.getCodigoPersona(), "finalizar");
					}
					else
					{
						antecedenteHistorico.insertarTransaccional(con, this.paciente.getCodigoPersona(), "continuar");
					}
				}
			}
		}
		if (embarazos!=null)
		{
			Embarazo embarazo;
			boolean embFinalizado=false;
			for(i=0;i<embarazos.size();i++)
			{
				embarazo=(Embarazo)embarazos.get(i);
				if(Utilidades.esEmbarazoSinTerminar(con, this.paciente.getCodigoPersona(), embarazo.getCodigo()) || embFinalizado)
				{
					embarazo.setCodigo(embarazo.getCodigo()+1);
					embFinalizado=true;
				}
				
				if (i==embarazos.size()-1)
				{
					embarazo.modificarTransaccional(con, this.paciente.getCodigoPersona(), "finalizar");
				}
				else
				{
					embarazo.modificarTransaccional(con, this.paciente.getCodigoPersona(), "continuar");
				}
			}
		}
		
		return resp0;
	}
	
	/**
	 * Método que modifica un AntecedenteGinecoObstetrico y todo lo que tiene
	 * que ver con el (embarazos, métodos anticonceptivos, etc). Como es
	 * transaccional recibe un paràmetro donde se especifíca en que punto queda
	 * esta inserción dentro de la transacción (la empieza?, esta en el medio o
	 * la termina)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de la transacción (empezar, continuar, finalizar)
	 * @return int 1 se se pudo insertar, 0 si no
	 * @throws SQLException
	 */

	/*
	 public int modificarTransaccional(Connection con, String estado) throws SQLException
	{
		int i=0, resp0=0;
		
		//Si no hay ni antecedentes Historicos GO ni embarazos
		//debemos modificarlo transaccionalmente
		
		if (!antecedenteGinecoObstetricoDao.existeAntecedente(con, this.paciente.getCodigoPersona()))
		{
			return this.insertarTransaccional(con, estado);
		}
		else if (antecedentesHistoricos==null||embarazos==null||antecedentesHistoricos.size()<1||embarazos.size()<1)
		{
			//Si no hay ningún elemento más, podemos modificarlo con el estado que nos dan
			resp0=antecedenteGinecoObstetricoDao.modificarTransaccional(con, this.paciente.getCodigoPersona(), this.rangoEdadMenarquia.getCodigo(), this.otroEdadMenarquia, this.rangoEdadMenopausia.getCodigo(), this.otroEdadMenopausia, this.observaciones, this.inicioVidaSexual, this.inicioVidaObstetrica, this.loginUsuario, metodosAnticonceptivos, estado);
		}
		else
		{
			//Si hay al menos algún elemento de las listas, NO podemos finalizar la 
			//transacción, dejamos el estado en continuar. En cualquiera de los otros 
			//dos estados no hay problema
			if (estado.equals("finalizar"))
			{
				resp0=antecedenteGinecoObstetricoDao.modificarTransaccional(con, this.paciente.getCodigoPersona(), this.rangoEdadMenarquia.getCodigo(), this.otroEdadMenarquia, this.rangoEdadMenopausia.getCodigo(), this.otroEdadMenopausia, this.observaciones, this.inicioVidaSexual, this.inicioVidaObstetrica, this.loginUsuario, metodosAnticonceptivos, "continuar");
			}
			else
			{
				resp0=antecedenteGinecoObstetricoDao.modificarTransaccional(con, this.paciente.getCodigoPersona(), this.rangoEdadMenarquia.getCodigo(), this.otroEdadMenarquia, this.rangoEdadMenopausia.getCodigo(), this.otroEdadMenopausia, this.observaciones, this.inicioVidaSexual, this.inicioVidaObstetrica, this.loginUsuario, metodosAnticonceptivos, estado);
			}
		}
		
		//A pesar de ser modificar, los antecedentes historicos van por consulta
		//es decir no se pueden modificar, luego todos se insertan
		if (antecedentesHistoricos!=null)
		{
			AntecedentesGinecoObstetricosHistorico antecedenteHistorico;
			for (i=0;i<antecedentesHistoricos.size();i++)
			{
				antecedenteHistorico= (AntecedentesGinecoObstetricosHistorico )antecedentesHistoricos.get(i);
				//Si es el último elemento, no hay embarazos y el estado es finalizar, terminamos 
				//la transaccion, en caso contrario continuamos
				if (i==k.size()-1&&( embarazos==null||embarazos.size()<1 )&& estado.equals("finalizar"))
				{
					antecedenteHistorico.insertarTransaccional(con, this.paciente.getCodigoPersona(), "finalizar");
				}
				else
				{
					antecedenteHistorico.insertarTransaccional(con, this.paciente.getCodigoPersona(), "continuar");
				}
			}
		}
		if (embarazos!=null)
		{
			Embarazo embarazo;
			for(i=0;i<embarazos.size();i++)
			{
				embarazo=(Embarazo)embarazos.get(i);
				//Si es el último elemento y el estado es finalizar, debemos
				//terminar la transacción
				if (i==embarazos.size()-1&& estado.equals("finalizar"))
				{
					embarazo.modificarTransaccional(con, this.paciente.getCodigoPersona(), "finalizar");
				}
				else
				{
					//En cualquiera de los otros dos estados (empezar y continuar),
					//debemos poner continuar pues si la transacción se empezó,
					//el estado empezar ya se le puso a el primer elemento (AntecedenteGinecoObstetrico)
					embarazo.modificarTransaccional(con, this.paciente.getCodigoPersona(), "continuar");
				}
			}
		}
		
		return resp0;
	}
*/

	/**
	 * Sobre carga al método cargar que simplemente toma los datos del paciente
	 * del objeto y los pasa al cargar que recibe estos datos
	 * @param con Conexión abierta con la fuente de datos
	 * @param numeroSolicitud
	 * 
	 * @throws SQLException
	 */
	public void cargar(Connection con, int numeroSolicitud) throws SQLException
	{
		this.cargar(con, this.paciente.getCodigoPersona(), numeroSolicitud);
	}

	/**
	 * Metodo para consultar la informacion desde Resumen de Atenciones  
	 * @param con
	 * @param mapa
	 * @throws SQLException
	 */
	public void cargar(Connection con, HashMap mapa) throws SQLException
	{
		this.cargarHistoriaAtenciones(con, mapa);
	}
	
	/**
	 * Método que trae toda la información de un AntecedenteGinecoObstetrico y
	 * todo lo que tiene que ver con el (embarazos, métodos anticonceptivos,
	 * etc) de una fuente de datos. 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud
	 * @param codigoTipoIdentificacion Código del tipo de identificación de la
	 * paciente
	 * @param numeroIdentificacion Número de identificación de la paciente
	 * 
	 * @throws SQLException
	 */

	@SuppressWarnings("unchecked")
	public void cargar (Connection con, int codigoPaciente, int numeroSolicitud) throws SQLException
	{
		
		boolean hayResultados=false;
		ResultSetDecorator rsACargar=antecedenteGinecoObstetricoDao.cargar(con, codigoPaciente);
		if (rsACargar.next())
		{
			this.rangoEdadMenarquia=new InfoDatos(rsACargar.getString("codigoEdadMenarquia"), rsACargar.getString("edadMenarquia"));
			this.otroEdadMenarquia=rsACargar.getString("otraEdadMenarquia");
			this.rangoEdadMenopausia=new InfoDatos(rsACargar.getString("codigoEdadMenopausia"), rsACargar.getString("edadMenopausia"));
			this.otroEdadMenopausia=rsACargar.getString("otraEdadMenopausia");
			this.observaciones=rsACargar.getString("observaciones");
			this.fecha=rsACargar.getString("fecha");
			this.hora=rsACargar.getString("hora");
			this.loginUsuario=rsACargar.getString("usuario");
			this.inicioVidaSexual = rsACargar.getInt("inicioVidaSexual");
			this.inicioVidaObstetrica = rsACargar.getInt("inicioVidaObstetrica");
			hayResultados=true;
		}
		rsACargar.close();
		//Antes de intentar cargar tanto historicos como embarazos,
		//revisamos que haya antecedente
		
		//Limpiamos las dos listas
		this.antecedentesHistoricos= new ArrayList();
		this.embarazos= new ArrayList();
		this.metodosAnticonceptivos= new ArrayList();
		
		
		if (hayResultados)
		{
			AntecedentesGinecoObstetricosHistorico antecedenteHistorico=new AntecedentesGinecoObstetricosHistorico();
			rsACargar=antecedenteGinecoObstetricoDao.cargarHistorico(con, codigoPaciente, numeroSolicitud);
			
			while (rsACargar.next())
			{
					
				antecedenteHistorico= new AntecedentesGinecoObstetricosHistorico();
				antecedenteHistorico.setDuracionMenstruacion(rsACargar.getInt("duracionMenstruacion"));
				antecedenteHistorico.setDolorMenstruacion(rsACargar.getString("dolorMenstruacion"));
				
				String FUR = rsACargar.getString("fechaUltimaRegla");
				if( FUR != null )
				{ 
					String[] fur = (FUR).split("-");
					if( fur.length == 3 )
					{
						FUR = new String();
						FUR = fur[2]+"/"+fur[1]+"/"+fur[0];
					}					
					antecedenteHistorico.setFechaUltimaRegla(FUR);
				}
				
				antecedenteHistorico.setConceptoMenstruacion(new InfoDatos(rsACargar.getString("codigoConceptoMenstruacion"), rsACargar.getString("conceptoMenstruacion")) );
				antecedenteHistorico.setFechaUltimaMamografia(rsACargar.getString("fechaUltimaMamografia"));
				antecedenteHistorico.setDescripcionUltimaMamografia(rsACargar.getString("descUltimaMamografia"));
				antecedenteHistorico.setFechaUltimaEcografia(rsACargar.getString("fechaUltimaEcografia"));
				antecedenteHistorico.setDescripcionUltimaEcografia(rsACargar.getString("descUltimaEcografia"));
				antecedenteHistorico.setFechaUltimaCitologia(rsACargar.getString("fechaUltimaCitologia"));
				antecedenteHistorico.setDescripcionUltimaCitologia(rsACargar.getString("descUltimaCitologia"));
				antecedenteHistorico.setDescripcionProcedimientosGinecologicos(rsACargar.getString("descUltimoProcGin"));
				antecedenteHistorico.setFechaUltimaDensimetriaOsea(rsACargar.getString("fechaUltDensimetriaOsea"));
				antecedenteHistorico.setDescUltimaDensimetriaOsea(rsACargar.getString("descUltDensimentriaOsea"));
				
				antecedenteHistorico.setObservacionesMenstruacion(rsACargar.getString("obsMenstruacion"));
				antecedenteHistorico.setCicloMenstrual(rsACargar.getInt("cicloMenstrual"));
				antecedenteHistorico.setFechaCreacion(rsACargar.getString("fechaCreacion"));
				antecedenteHistorico.setHoraCreacion(rsACargar.getString("horaCreacion"));
				
				//if ((rsACargar.getString("gInfoEmbarazos")+"").equals("") || (rsACargar.getString("gInfoEmbarazos")+"").equals("null"))
				
				if ( !(rsACargar.getString("gInfoEmbarazos")+"").equals("null") && !(rsACargar.getString("gInfoEmbarazos")+"").equals("") )
				{
					antecedenteHistorico.setGInfoEmbarazos(rsACargar.getString("gInfoEmbarazos"));
				}
				else
				{
					antecedenteHistorico.setGInfoEmbarazos("");
				}
				
				if ( !(rsACargar.getString("pInfoEmbarazos")+"").equals("null") && !(rsACargar.getString("pInfoEmbarazos")+"").equals("") )
				{
					antecedenteHistorico.setPInfoEmbarazos(rsACargar.getString("pInfoEmbarazos"));	
				}
				else
				{
					antecedenteHistorico.setPInfoEmbarazos("");					
				}
				
				if ( !(rsACargar.getString("aInfoEmbarazos")+"").equals("null") && !(rsACargar.getString("aInfoEmbarazos")+"").equals("") )
				{				
					antecedenteHistorico.setAInfoEmbarazos(rsACargar.getString("aInfoEmbarazos"));
				}
				else
				{
					antecedenteHistorico.setAInfoEmbarazos("");
				}
				
				if ( !(rsACargar.getString("cInfoEmbarazos")+"").equals("null") && !(rsACargar.getString("cInfoEmbarazos")+"").equals("") )
				{	
					antecedenteHistorico.setCInfoEmbarazos(rsACargar.getString("cInfoEmbarazos"));
				}
				else
				{
					antecedenteHistorico.setCInfoEmbarazos("");
				}
					
				
				if ( !(rsACargar.getString("vInfoEmbarazos")+"").equals("null") && !(rsACargar.getString("vInfoEmbarazos")+"").equals("") )
				{	
				  antecedenteHistorico.setVInfoEmbarazos(rsACargar.getString("vInfoEmbarazos"));
				}
				else
				{
				  antecedenteHistorico.setVInfoEmbarazos("");
				}
				
				if ( !(rsACargar.getString("mInfoEmbarazos")+"").equals("null") && !(rsACargar.getString("mInfoEmbarazos")+"").equals("") )
				{
					antecedenteHistorico.setMInfoEmbarazos(rsACargar.getString("mInfoEmbarazos"));	
				}
				else
				{
					antecedenteHistorico.setMInfoEmbarazos("");						
				}				
				
				antecedenteHistorico.setP2500( UtilidadTexto.getBoolean(rsACargar.getBoolean("p2500")+"")+"" );
				antecedenteHistorico.setP4000( UtilidadTexto.getBoolean(rsACargar.getBoolean("p4000") +"")+"" );	
				antecedenteHistorico.setMayorA2( UtilidadTexto.getBoolean(rsACargar.getBoolean("mayora2")+"")+"" );
				
				if ( !(rsACargar.getString("finembarazoanterior")+"").equals("null") && !(rsACargar.getString("finembarazoanterior")+"").equals("") )
				{
					antecedenteHistorico.setFinEmbarazoAnterior(rsACargar.getString("finembarazoanterior"));	
				}
				else
				{
					antecedenteHistorico.setFinEmbarazoAnterior("");											
				}				

				if ( !(rsACargar.getString("prematuros")+"").equals("null") && !(rsACargar.getString("prematuros")+"").equals("") )
				{
					antecedenteHistorico.setPrematuros(rsACargar.getString("prematuros"));	
				}
				else
				{
					antecedenteHistorico.setPrematuros("");					
				}
				
				if ( !(rsACargar.getString("ectropicos")+"").equals("null") && !(rsACargar.getString("ectropicos")+"").equals("") )
				{
					antecedenteHistorico.setEctropicos(rsACargar.getString("ectropicos"));
				}
				else
				{
					antecedenteHistorico.setEctropicos("");					
				}

				if ( !(rsACargar.getString("multiples")+"").equals("null") && !(rsACargar.getString("multiples")+"").equals("") )
				{
					antecedenteHistorico.setMultiples(rsACargar.getString("multiples"));
				}
				else
				{
					antecedenteHistorico.setMultiples("");
				}
				
				
				antecedenteHistorico.setFinEmbarazoMayor1o5(rsACargar.getString("finembarazomayor1o5"));
				
				
				//--------------------------------------------------------
				//--------------------------------------------------------
				//-- Cargar los nuevos campos de los antecedentes Gineco.
				antecedenteHistorico.setVag( UtilidadCadena.vInt(rsACargar.getString("vag")+"") );	
				
				antecedenteHistorico.setTipoEmbarazo(rsACargar.getString("tipoembarazo"));
				antecedenteHistorico.setMuertosAntes1Semana(rsACargar.getString("muertosantes1semana")+"") ;
				antecedenteHistorico.setMuertosDespues1Semana(rsACargar.getString("muertosdespues1semana")+"");
				antecedenteHistorico.setVivosActualmente(rsACargar.getString("vivosactualmente"));
				
				antecedenteHistorico.setRetencionPlacentaria( UtilidadCadena.vString(rsACargar.getString("retencion_placentaria")+"") );	
				antecedenteHistorico.setInfeccionPostparto( UtilidadCadena.vString(rsACargar.getString("infeccion_postparto")+"") );
				antecedenteHistorico.setMalformacion( UtilidadCadena.vString(rsACargar.getString("malformacion")+"") );	
				antecedenteHistorico.setMuertePerinatal( UtilidadCadena.vString(rsACargar.getString("muerte_perinatal")+"") );	
				
				//-Estos son String requeridos en la insercion
				antecedenteHistorico.setSangradoAnormal( rsACargar.getString("sangrado_anormal"));
				antecedenteHistorico.setFlujoVaginal(rsACargar.getString("flujo_vaginal"));	
				antecedenteHistorico.setEnferTransSexual(rsACargar.getString("enfer_trans_sexual"));	
				antecedenteHistorico.setCualEnferTransSex(UtilidadCadena.vString(rsACargar.getString("cual_enfer_trans_sex")));	
				antecedenteHistorico.setCirugiaGineco(rsACargar.getString("cirugia_gineco"));	
				antecedenteHistorico.setCualCirugiaGineco(UtilidadCadena.vString(rsACargar.getString("cual_cirugia_gineco")+""));	
				antecedenteHistorico.setHistoriaInfertilidad(rsACargar.getString("historia_infertilidad"));	
				antecedenteHistorico.setCualHistoInfertilidad(UtilidadCadena.vString(rsACargar.getString("cual_histo_infertilidad")+""));	
				
				this.antecedentesHistoricos.add(antecedenteHistorico);
			}
			rsACargar.close();
			
			//System.out.print ("\n\n Despues de Cerrar  El p2500 Que Hay  >"+ al + "<\n\n");
			//antecedenteHistorico.setP2500(al);
			
			Embarazo emb=new Embarazo();
			rsACargar=antecedenteGinecoObstetricoDao.cargarEmbarazos(con, codigoPaciente);

			while (rsACargar.next())
			{
				emb=new Embarazo();
				emb.setCodigo(rsACargar.getInt("codigo"));
				emb.setMesesGestacion(rsACargar.getFloat("mesesGestacion"));
				
				String fechaTerminacion = rsACargar.getString("fechaTerminacion");
				if( fechaTerminacion != null )
				{ 
					String[] fTerminacion = (fechaTerminacion).split("-");
					if( fTerminacion.length == 3 )
					{
						fechaTerminacion = new String();
						fechaTerminacion = fTerminacion[2]+"/"+fTerminacion[1]+"/"+fTerminacion[0];
					}					
					emb.setFechaTerminacion(fechaTerminacion);
				}
				
				Collection complicaciones=antecedenteGinecoObstetricoDao.cargarComplicaciones(con, codigoPaciente, emb.getCodigo());
				
				Iterator iterador=complicaciones.iterator();
				int[] compliTempo=new int[complicaciones.size()];
				int i=0;
				Vector nombresComplicaciones=new Vector();
				while(iterador.hasNext())
				{
					compliTempo[i]=0;
					HashMap fila=(HashMap)iterador.next();
					compliTempo[i]=Integer.parseInt(fila.get("codigocomplicacion")+"");
					String tempoNombreComplicacion=(String)fila.get("nombrecomplicacion");
					nombresComplicaciones.add(tempoNombreComplicacion);
					i++;
				}
				emb.setComplicacion(compliTempo);
				emb.setNombresComplicaciones(nombresComplicaciones);
				
				complicaciones=antecedenteGinecoObstetricoDao.cargarComplicacionesOtras(con, codigoPaciente, emb.getCodigo());
				iterador=complicaciones.iterator();
				Vector complicacionesOtras=new Vector();
				while(iterador.hasNext())
				{
					HashMap fila=(HashMap)iterador.next();
					complicacionesOtras.add(fila.get("complicacion")+"");
				}
				emb.setOtraComplicacion(complicacionesOtras);
				
				emb.setTrabajoParto(new InfoDatos(rsACargar.getString("codigoTrabajoParto"), rsACargar.getString("trabajoParto")));
				emb.setOtroTrabajoParto(rsACargar.getString("otroTrabajoParto"));
				emb.setDuracion(rsACargar.getString("duracion"));
				emb.setTiempoRupturaMembranas(rsACargar.getString("ruptura"));
				emb.setLegrado(rsACargar.getString("legrado"));
				emb.cargarHijos(con, codigoPaciente);
				embarazos.add(emb);
			}
			rsACargar.close();
			InfoDatos metodoAnticonceptivo;
			rsACargar=antecedenteGinecoObstetricoDao.cargarMetodosAnticonceptivos(con, codigoPaciente);
			while (rsACargar.next())
			{
				//codigoMetodoAnticon, metodoAnticonceptivo
				metodoAnticonceptivo=new InfoDatos(rsACargar.getString("codigoMetodoAnticon"), rsACargar.getString("metodoAnticonceptivo"));
				metodoAnticonceptivo.setDescripcion(rsACargar.getString("descripcion"));
				metodosAnticonceptivos.add(metodoAnticonceptivo);
			}
			
		}
	}

	/**
	 * Metodo para llamar desde resumen de atenciones.
	 * @param con
	 * @param mapa
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public void cargarHistoriaAtenciones (Connection con, HashMap mapa) throws SQLException
	{
		int codigoPaciente = UtilidadCadena.vInt(mapa.get("paciente") + "");
		
		boolean hayResultados=false;
		//ResultSetDecorator rsACargar=antecedenteGinecoObstetricoDao.cargar(con, codigoPaciente);
		ResultSetDecorator rsACargar=antecedenteGinecoObstetricoDao.cargar(con, mapa);
		if (rsACargar.next())
		{
			this.rangoEdadMenarquia=new InfoDatos(rsACargar.getString("codigoEdadMenarquia"), rsACargar.getString("edadMenarquia"));
			this.otroEdadMenarquia=rsACargar.getString("otraEdadMenarquia");
			this.rangoEdadMenopausia=new InfoDatos(rsACargar.getString("codigoEdadMenopausia"), rsACargar.getString("edadMenopausia"));
			this.otroEdadMenopausia=rsACargar.getString("otraEdadMenopausia");
			this.observaciones=rsACargar.getString("observaciones");
			this.fecha=rsACargar.getString("fecha");
			this.hora=rsACargar.getString("hora");
			this.loginUsuario=rsACargar.getString("usuario");
			this.inicioVidaSexual = rsACargar.getInt("inicioVidaSexual");
			this.inicioVidaObstetrica = rsACargar.getInt("inicioVidaObstetrica");
			hayResultados=true;
		}
		rsACargar.close();
		//Antes de intentar cargar tanto historicos como embarazos,
		//revisamos que haya antecedente
		
		//Limpiamos las dos listas
		this.antecedentesHistoricos= new ArrayList();
		this.embarazos= new ArrayList();
		this.metodosAnticonceptivos= new ArrayList();
		
		
		if (hayResultados)
		{
			AntecedentesGinecoObstetricosHistorico antecedenteHistorico=new AntecedentesGinecoObstetricosHistorico();
			//rsACargar=antecedenteGinecoObstetricoDao.cargarHistorico(con, codigoPaciente, numeroSolicitud);
			rsACargar=antecedenteGinecoObstetricoDao.cargarHistorico(con, mapa);
			
			while (rsACargar.next())
			{
					
				antecedenteHistorico= new AntecedentesGinecoObstetricosHistorico();
				antecedenteHistorico.setDuracionMenstruacion(rsACargar.getInt("duracionMenstruacion"));
				antecedenteHistorico.setDolorMenstruacion(rsACargar.getString("dolorMenstruacion"));
				
				String FUR = rsACargar.getString("fechaUltimaRegla");
				if( FUR != null )
				{ 
					String[] fur = (FUR).split("-");
					if( fur.length == 3 )
					{
						FUR = new String();
						FUR = fur[2]+"/"+fur[1]+"/"+fur[0];
					}					
					antecedenteHistorico.setFechaUltimaRegla(FUR);
				}
				
				antecedenteHistorico.setConceptoMenstruacion(new InfoDatos(rsACargar.getString("codigoConceptoMenstruacion"), rsACargar.getString("conceptoMenstruacion")) );
				antecedenteHistorico.setFechaUltimaMamografia(rsACargar.getString("fechaUltimaMamografia"));
				antecedenteHistorico.setDescripcionUltimaMamografia(rsACargar.getString("descUltimaMamografia"));
				antecedenteHistorico.setFechaUltimaEcografia(rsACargar.getString("fechaUltimaEcografia"));
				antecedenteHistorico.setDescripcionUltimaEcografia(rsACargar.getString("descUltimaEcografia"));
				antecedenteHistorico.setFechaUltimaCitologia(rsACargar.getString("fechaUltimaCitologia"));
				antecedenteHistorico.setDescripcionUltimaCitologia(rsACargar.getString("descUltimaCitologia"));
				antecedenteHistorico.setDescripcionProcedimientosGinecologicos(rsACargar.getString("descUltimoProcGin"));
				antecedenteHistorico.setFechaUltimaDensimetriaOsea(rsACargar.getString("fechaUltDensimetriaOsea"));
				antecedenteHistorico.setDescUltimaDensimetriaOsea(rsACargar.getString("descUltDensimentriaOsea"));
				
				antecedenteHistorico.setObservacionesMenstruacion(rsACargar.getString("obsMenstruacion"));
				antecedenteHistorico.setCicloMenstrual(rsACargar.getInt("cicloMenstrual"));
				antecedenteHistorico.setFechaCreacion(rsACargar.getString("fechaCreacion"));
				antecedenteHistorico.setHoraCreacion(rsACargar.getString("horaCreacion"));
				
				//if ((rsACargar.getString("gInfoEmbarazos")+"").equals("") || (rsACargar.getString("gInfoEmbarazos")+"").equals("null"))
				
				if ( !(rsACargar.getString("gInfoEmbarazos")+"").equals("null") && !(rsACargar.getString("gInfoEmbarazos")+"").equals("") )
				{
					antecedenteHistorico.setGInfoEmbarazos(rsACargar.getString("gInfoEmbarazos"));
				}
				else
				{
					antecedenteHistorico.setGInfoEmbarazos("");
				}
				
				if ( !(rsACargar.getString("pInfoEmbarazos")+"").equals("null") && !(rsACargar.getString("pInfoEmbarazos")+"").equals("") )
				{
					antecedenteHistorico.setPInfoEmbarazos(rsACargar.getString("pInfoEmbarazos"));	
				}
				else
				{
					antecedenteHistorico.setPInfoEmbarazos("");					
				}
				
				if ( !(rsACargar.getString("aInfoEmbarazos")+"").equals("null") && !(rsACargar.getString("aInfoEmbarazos")+"").equals("") )
				{				
					antecedenteHistorico.setAInfoEmbarazos(rsACargar.getString("aInfoEmbarazos"));
				}
				else
				{
					antecedenteHistorico.setAInfoEmbarazos("");
				}
				
				if ( !(rsACargar.getString("cInfoEmbarazos")+"").equals("null") && !(rsACargar.getString("cInfoEmbarazos")+"").equals("") )
				{	
					antecedenteHistorico.setCInfoEmbarazos(rsACargar.getString("cInfoEmbarazos"));
				}
				else
				{
					antecedenteHistorico.setCInfoEmbarazos("");
				}
					
				
				if ( !(rsACargar.getString("vInfoEmbarazos")+"").equals("null") && !(rsACargar.getString("vInfoEmbarazos")+"").equals("") )
				{	
				  antecedenteHistorico.setVInfoEmbarazos(rsACargar.getString("vInfoEmbarazos"));
				}
				else
				{
				  antecedenteHistorico.setVInfoEmbarazos("");
				}
				
				if ( !(rsACargar.getString("mInfoEmbarazos")+"").equals("null") && !(rsACargar.getString("mInfoEmbarazos")+"").equals("") )
				{
					antecedenteHistorico.setMInfoEmbarazos(rsACargar.getString("mInfoEmbarazos"));	
				}
				else
				{
					antecedenteHistorico.setMInfoEmbarazos("");						
				}				

				
				antecedenteHistorico.setP2500(UtilidadTexto.getBoolean(rsACargar.getBoolean("p2500")+"")+"");
				antecedenteHistorico.setP4000(UtilidadTexto.getBoolean(rsACargar.getBoolean("p4000") +"")+"");	
				antecedenteHistorico.setMayorA2(UtilidadTexto.getBoolean(rsACargar.getBoolean("mayora2")+"")+"");
				

				antecedenteHistorico.setTipoEmbarazo(rsACargar.getString("tipoembarazo"));
				antecedenteHistorico.setMuertosAntes1Semana(rsACargar.getString("muertosantes1semana")+"") ;
				antecedenteHistorico.setMuertosDespues1Semana(rsACargar.getString("muertosdespues1semana")+"");
				
				antecedenteHistorico.setVivosActualmente(rsACargar.getString("vivosactualmente")+"");
				
				if ( !(rsACargar.getString("finembarazoanterior")+"").equals("null") && !(rsACargar.getString("finembarazoanterior")+"").equals("") )
				{
					antecedenteHistorico.setFinEmbarazoAnterior(rsACargar.getString("finembarazoanterior"));	
				}
				else
				{
					antecedenteHistorico.setFinEmbarazoAnterior("");											
				}				

				if ( !(rsACargar.getString("prematuros")+"").equals("null") && !(rsACargar.getString("prematuros")+"").equals("") )
				{
					antecedenteHistorico.setPrematuros(rsACargar.getString("prematuros"));	
				}
				else
				{
					antecedenteHistorico.setPrematuros("");					
				}
				
				if ( !(rsACargar.getString("ectropicos")+"").equals("null") && !(rsACargar.getString("ectropicos")+"").equals("") )
				{
					antecedenteHistorico.setEctropicos(rsACargar.getString("ectropicos"));
				}
				else
				{
					antecedenteHistorico.setEctropicos("");					
				}

				if ( !(rsACargar.getString("multiples")+"").equals("null") && !(rsACargar.getString("multiples")+"").equals("") )
				{
					antecedenteHistorico.setMultiples(rsACargar.getString("multiples"));
				}
				else
				{
					antecedenteHistorico.setMultiples("");
				}
				
				
				antecedenteHistorico.setFinEmbarazoMayor1o5(rsACargar.getString("finembarazomayor1o5"));
				
				this.antecedentesHistoricos.add(antecedenteHistorico);
			}
			rsACargar.close();
			
			//System.out.print ("\n\n Despues de Cerrar  El p2500 Que Hay  >"+ al + "<\n\n");
			//antecedenteHistorico.setP2500(al);
			
			Embarazo emb=new Embarazo();
			//rsACargar=antecedenteGinecoObstetricoDao.cargarEmbarazos(con, codigoPaciente);
			rsACargar=antecedenteGinecoObstetricoDao.cargarEmbarazos(con, mapa);
			

			while (rsACargar.next())
			{
				emb=new Embarazo();
				emb.setCodigo(rsACargar.getInt("codigo"));
				emb.setMesesGestacion(rsACargar.getFloat("mesesGestacion"));
				
				String fechaTerminacion = rsACargar.getString("fechaTerminacion");
				if( fechaTerminacion != null )
				{ 
					String[] fTerminacion = (fechaTerminacion).split("-");
					if( fTerminacion.length == 3 )
					{
						fechaTerminacion = new String();
						fechaTerminacion = fTerminacion[2]+"/"+fTerminacion[1]+"/"+fTerminacion[0];
					}					
					emb.setFechaTerminacion(fechaTerminacion);
				}
				
				mapa.put("embarazo",emb.getCodigo()+"");
				Collection complicaciones=antecedenteGinecoObstetricoDao.cargarComplicaciones(con, mapa);
				//Collection complicaciones=antecedenteGinecoObstetricoDao.cargarComplicaciones(con, codigoPaciente, emb.getCodigo());
				
				Iterator iterador=complicaciones.iterator();
				int[] compliTempo=new int[complicaciones.size()];
				int i=0;
				Vector nombresComplicaciones=new Vector();
				while(iterador.hasNext())
				{
					compliTempo[i]=0;
					HashMap fila=(HashMap)iterador.next();
					compliTempo[i]=Integer.parseInt(fila.get("codigocomplicacion")+"");
					String tempoNombreComplicacion=(String)fila.get("nombrecomplicacion");
					nombresComplicaciones.add(tempoNombreComplicacion);
					i++;
				}
				emb.setComplicacion(compliTempo);
				emb.setNombresComplicaciones(nombresComplicaciones);
				
				mapa.put("antecedente",emb.getCodigo()+"");
				complicaciones=antecedenteGinecoObstetricoDao.cargarComplicacionesOtras(con, mapa);
				//complicaciones=antecedenteGinecoObstetricoDao.cargarComplicacionesOtras(con, codigoPaciente, emb.getCodigo());
				
				iterador=complicaciones.iterator();
				Vector complicacionesOtras=new Vector();
				while(iterador.hasNext())
				{
					HashMap fila=(HashMap)iterador.next();
					complicacionesOtras.add(fila.get("complicacion")+"");
				}
				emb.setOtraComplicacion(complicacionesOtras);
				
				emb.setTrabajoParto(new InfoDatos(rsACargar.getString("codigoTrabajoParto"), rsACargar.getString("trabajoParto")));
				emb.setOtroTrabajoParto(rsACargar.getString("otroTrabajoParto"));
				emb.setDuracion(rsACargar.getString("duracion"));
				emb.setTiempoRupturaMembranas(rsACargar.getString("ruptura"));
				emb.setLegrado(rsACargar.getString("legrado"));
				emb.cargarHijos(con, codigoPaciente);
				embarazos.add(emb);
			}
			rsACargar.close();
			InfoDatos metodoAnticonceptivo;
			rsACargar=antecedenteGinecoObstetricoDao.cargarMetodosAnticonceptivos(con, mapa);
			//rsACargar=antecedenteGinecoObstetricoDao.cargarMetodosAnticonceptivos(con, codigoPaciente);
			while (rsACargar.next())
			{
				//codigoMetodoAnticon, metodoAnticonceptivo
				metodoAnticonceptivo=new InfoDatos(rsACargar.getString("codigoMetodoAnticon"), rsACargar.getString("metodoAnticonceptivo"));
				metodoAnticonceptivo.setDescripcion(rsACargar.getString("descripcion"));
				metodosAnticonceptivos.add(metodoAnticonceptivo);
			}
			
		}
	}
	
	

	/**
	 * Retorna la paciente asociada con estos antecedentes
	 * @return 		PersonaBasica, paciente de los antecedentes.
	 */
	public PersonaBasica getPaciente() 
	{
		return paciente;
	}

	/**
	 * Asigna la paciente asociada con estos antecedentes
	 * @param 	PersonaBasica. paciente de los antecedentes.
	 */
	public void setPaciente(PersonaBasica paciente) 
	{
		this.paciente = paciente;
	}

	/**
	 * Retorna la edad de la primera menstruación, en caso que no haya
	 * seleccionado ninguna del rango de edades de la menarquia.
	 * @return 		String, edad especificada de la menarquia
	 */
	public String getOtroEdadMenarquia() 
	{
		return otroEdadMenarquia;
	}

	/**
	 * Asigna la edad de la primera menstruación, en caso que no haya
	 * seleccionado ninguna del rango de edades de la menarquia.
	 * @param 	String, edad especificada de la menarquia
	 */
	public void setOtroEdadMenarquia(String otroEdadMenarquia) 
	{
		this.otroEdadMenarquia = otroEdadMenarquia;
	}

	/**
	 * Retorna la pareja (Codigo, nombre) del rango de la primera menstruación
	 * seleccionado.
	 * @return 		InfoDatos, rangoEdadMenarquia 
	 */
	public InfoDatos getRangoEdadMenarquia() 
	{
		return rangoEdadMenarquia;
	}

	/**
	 * Asigna la pareja (Codigo, nombre) del rango de la primera menstruación
	 * seleccionado.
	 * @param 	InfoDatos, rangoEdadMenarquia
	 */
	public void setRangoEdadMenarquia(InfoDatos rangoEdadMenarquia) 
	{
		this.rangoEdadMenarquia = rangoEdadMenarquia;
	}

	/**
	 * Retorna la edad de la ultima menstruación, en caso que no haya
	 * seleccionado ninguna del rango de edades de la menopausia.
	 * @return 		String, edad especificada de la menopausia
	 */
	public String getOtroEdadMenopausia() 
	{
		return otroEdadMenopausia;
	}

	/**
	 * Asigna la edad de la ultima menstruación, en caso que no haya
	 * seleccionado ninguna del rango de edades de la menstruación.
	 * @param 	String, edad especificada de la menstruación
	 */
	public void setOtroEdadMenopausia(String otroEdadMenopausia) 
	{
		this.otroEdadMenopausia = otroEdadMenopausia;
	}

	/**
	 * Retorna la pareja (Codigo, nombre) del rango de la última menstruación
	 * seleccionada.
	 * @return 		InfoDatos, rangoEdadMenopausia 
	 */
	public InfoDatos getRangoEdadMenopausia()
	{
		return rangoEdadMenopausia;
	}

	/**
	 * Asigna la pareja (Codigo, nombre) del rango de la última menstruación
	 * seleccionada.
	 * @param 	InfoDatos, rangoEdadMenopausia
	 */
	public void setRangoEdadMenopausia(InfoDatos rangoEdadMenopausia) 
	{
		this.rangoEdadMenopausia = rangoEdadMenopausia;
	}

	/**
	 * Retorna las observaciones generales del antecedente
	 * @return 		String, observaciones generales de Antecedentes GO
	 */
	public String getObservaciones() 
	{
		return observaciones;
	}

	/**
	 * Asigna las observaciones generales del antecedente
	 * @param 	String, observaciones.
	 */
	public void setObservaciones(String observaciones) 
	{
		this.observaciones = observaciones;
	}

	/**
	 * Retorna la fecha en la que se grabaron los datos.
	 * @return 		String, fecha en formato "dd/mm/aaaa"
	 */
	public String getFecha() 
	{
		return fecha;
	}

	/**
	 * Asigna la fecha en la que se grabaron los datos.
	 * @param 	String, fecha en formato "dd/mm/aaaa"
	 */
	public void setFecha(String fecha) 
	{
		this.fecha = fecha;
	}

	/**
	 * Retorna la hora  en la que se grabaron los datos.
	 * @return 		String, hora en formato "hh:mm"
	 */
	public String getHora() 
	{
		return hora;
	}

	/**
	 * Asigna la hora en la que se grabaron los datos.
	 * @param 	String, hora en formato "hh:mm"
	 */
	public void setHora(String hora) 
	{
		this.hora = hora;
	}

	/**
	 * Retorna el login del usuario que grabó los datos.
	 * @return 		String, login del usuario del sistema
	 */
	public String getLoginUsuario() 
	{
		return loginUsuario;
	}

	/**
	 * Asigna el login del usuario que grabó los datos.
	 * @param 	String, loginUsuario
	 */
	public void setLoginUsuario(String loginUsuario) 
	{
		this.loginUsuario = loginUsuario;
	}

	/**
	 * Retorna la información de todos los embarazos de la paciente. La
	 * información de cada embarazo esta dada en el objeto "Embarazo"
	 * @return 		ArrayList, información embarazos de la paciente.
	 */
	public ArrayList getEmbarazos() 
	{
		return embarazos;
	}

	/**
	 * Asigna la información de todos los embarazos de la paciente. La
	 * información de cada embarazo esta dada en el objeto "Embarazo"
	 * @param 	ArrayList, embarazos
	 */
	public void setEmbarazos(ArrayList embarazos) 
	{
		this.embarazos = embarazos;
	}

	/**
	 * Adiciona la información de un embarazo nuevo a la lista de embarazos.
	 * @param		Embarazo, nuevo embarazo a adicionar.
	 */
	@SuppressWarnings("unchecked")
	public void addEmbarazo(Embarazo embarazo)
	{
		this.embarazos.add(embarazo);
	}
	
	/**
	 * Retorna la lista con la informacion de los antecedentes que tienen
	 * historico, está ordenada por fecha
	 * @return 			ArrayList, antecedentes historicos
	 */
	public ArrayList getAntecedentesHistoricos() 
	{
		return antecedentesHistoricos;
	}

	/**
	 * Asigna los antecedentes que tienen historico, el historico esta dado por
	 * los datos grabados por fecha
	 * @param 		ArrayList, antecedentesHistoricos
	 */
	public void setAntecedentesHistoricos(ArrayList antecedentesHistoricos) 
	{
		this.antecedentesHistoricos = antecedentesHistoricos;
	}
	
	/**
	 * Adiciona un nuevo historico
	 * @param			AntecedentesGinecoObstetricosHistorico, con la info del
	 * 						historico
 	 */
	@SuppressWarnings("unchecked")
	public void addHistorico(AntecedentesGinecoObstetricosHistorico historico)
	{
		this.antecedentesHistoricos.add(historico);
	}

	/**
	 * Returna el ArrayList con los metodos anticonceptivos usados en algún
	 * momento por la paciente.
	 * 
	 * @return ArrayList
	 */
	public ArrayList getMetodosAnticonceptivos() 
	{
		return metodosAnticonceptivos;
	}

	/**
	 *  Establece el ArrayList con los metodos anticonceptivos usados en algún
	 * momento por la paciente.
	 * 
	 * @param metodosAnticonceptivos El ArrayList con los metodos
	 * anticonceptivos usados en algún momento por la paciente a establecer
	 */
	public void setMetodosAnticonceptivos(ArrayList metodosAnticonceptivos) 
	{
		this.metodosAnticonceptivos = metodosAnticonceptivos;
	}

	@SuppressWarnings("unchecked")
	public void addMetodoAnticonceptivo(InfoDatos metodoAnticonceptivo)
	{
		this.metodosAnticonceptivos.add(metodoAnticonceptivo);
	}
	
	/**
	 * Retorna la fecha con la hora actual, en fomato dd/mm/aaaa (hh:mm)
	 * @return		Cadena con la fecha y la hora actual
	 */
	public String getDateAndTime()
	{
		String dateTime = new String();
			
		GregorianCalendar calendar = new GregorianCalendar(new SimpleTimeZone(-18000000, "America/Bogota"));

		int anioAct = calendar.get(Calendar.YEAR);
		int mesAct  = calendar.get(Calendar.MONTH)+1;
		String mesAct2 = (new Integer(mesAct)).toString();	
		mesAct2 = (mesAct2.length() < 2) ? "0"+mesAct2 : mesAct2;	
		int diaAct  = calendar.get(Calendar.DAY_OF_MONTH);
		String diaAct2 = (new Integer(diaAct)).toString();
		diaAct2 = (diaAct2.length() < 2) ? "0"+diaAct2 : diaAct2;
		
		String minute = calendar.get(Calendar.MINUTE) + "";
		minute = (minute.length() < 2) ? "0"+minute : minute;		
		String hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
		hour = (hour.length() < 2) ? "0"+hour : hour;
		String horaAct = hour + ":" + minute;
		
		dateTime = diaAct2+"/"+mesAct2+"/"+anioAct+"("+horaAct+")";
		
		return dateTime;	
	}
	/**
	 * Retorna la edad en años del inicio de la vida sexual
	 * @return int
	 */
	public int getInicioVidaSexual()
	{
		return inicioVidaSexual;
	}

	/**
	 * Asigna la edad en años del inicio de la vida sexual
	 * @param inicioVidaSexual The inicioVidaSexual to set
	 */
	public void setInicioVidaSexual(int inicioVidaSexual)
	{
		this.inicioVidaSexual = inicioVidaSexual;
	}

	/**
	 * Retorna la edad en años del inicio de la vida obstetrica
	 * @return int
	 */
	public int getInicioVidaObstetrica()
	{
		return inicioVidaObstetrica;
	}

	/**
	 * Asigna la edad en años del inicio de la vida obstetrica
	 * @param inicioVidaObstetrica The inicioVidaObstetrica to set
	 */
	public void setInicioVidaObstetrica(int inicioVidaObstetrica)
	{
		this.inicioVidaObstetrica = inicioVidaObstetrica;
	}

	public AntecedentesGinecoObstetricosHistorico cargarUltimoRegInfoEmbarazos(Connection con)  throws SQLException
	{
		ResultSetDecorator resultado = antecedenteGinecoObstetricoDao.cargarUltInformacionEmbarazos(con, this.paciente.getCodigoPersona());
		
		AntecedentesGinecoObstetricosHistorico ant = new AntecedentesGinecoObstetricosHistorico();
		
		if( resultado.next() )
		{
			ant.setGInfoEmbarazos(resultado.getString("gInfoEmbarazos"));
			ant.setPInfoEmbarazos(resultado.getString("pInfoEmbarazos"));
			ant.setAInfoEmbarazos(resultado.getString("aInfoEmbarazos"));
			ant.setCInfoEmbarazos(resultado.getString("cInfoEmbarazos"));
			ant.setVInfoEmbarazos(resultado.getString("vInfoEmbarazos"));
			ant.setMInfoEmbarazos(resultado.getString("mInfoEmbarazos"));			
			
			
			ant.setP2500( UtilidadTexto.getBoolean(resultado.getBoolean("p2500")+"") +"");
			ant.setP4000( UtilidadTexto.getBoolean(resultado.getBoolean("p4000") +"")+"");
			ant.setMayorA2( UtilidadTexto.getBoolean(resultado.getBoolean("mayora2")+"")+"");
			
			//--- seccion "Informacion embarazos nuevos"
			ant.setVag( UtilidadCadena.vInt(resultado.getInt("vag")+""));
			ant.setTipoEmbarazo(UtilidadCadena.vString(resultado.getString("tipoembarazo")+"") );
			ant.setVivosActualmente(UtilidadCadena.vString(resultado.getString("vivosactualmente")+"") );
			ant.setMuertosAntes1Semana(UtilidadCadena.vString(resultado.getString("muertosantes1semana")+"") );
			ant.setMuertosDespues1Semana(UtilidadCadena.vString(resultado.getString("muertosdespues1semana")+"") );
			ant.setRetencionPlacentaria( UtilidadCadena.vString(resultado.getString("retencion_placentaria")+"") );
			ant.setInfeccionPostparto( UtilidadCadena.vString(resultado.getString("infeccion_postparto")+"") );
			ant.setMalformacion( UtilidadCadena.vString(resultado.getString("malformacion")+"") );
			ant.setMuertePerinatal( UtilidadCadena.vString(resultado.getString("muerte_perinatal")+"") );
				
			
			if ( !(resultado.getString("finembarazoanterior")+"").equals("null") && !(resultado.getString("finembarazoanterior")+"").equals("") )
			{
				ant.setFinEmbarazoAnterior(resultado.getString("finembarazoanterior"));
			}
			else
			{
				ant.setFinEmbarazoAnterior("");
			}
			

			if ( !(resultado.getString("finembarazomayor1o5")+"").equals("null") && !(resultado.getString("finembarazomayor1o5")+"").equals("") )
			{
				ant.setFinEmbarazoMayor1o5(resultado.getString("finembarazomayor1o5"));
			}
			else
			{
				ant.setFinEmbarazoMayor1o5("");
			}

			if ( !(resultado.getString("prematuros")+"").equals("null") && !(resultado.getString("prematuros")+"").equals("") )
			{
				ant.setPrematuros(resultado.getString("prematuros"));
			}
			else
			{
				ant.setPrematuros("");
			}

			if ( !(resultado.getString("ectropicos")+"").equals("null") && !(resultado.getString("ectropicos")+"").equals("") )
			{
				ant.setEctropicos(resultado.getString("ectropicos"));
			}
			else
			{
				ant.setEctropicos("");
			}
			
			if ( !(resultado.getString("multiples")+"").equals("null") && !(resultado.getString("multiples")+"").equals("") )
			{
				ant.setMultiples(resultado.getString("multiples"));
			}
			else
			{
				ant.setMultiples("");
			}			
		}

		return ant;
	}

	/**
	 * Método que inserta los datos propios de la valoración gineco-obstetrica
	 * @param con
	 * @param edadMenarquia
	 * @param edadMenopausia
	 * @param otraEdadMenarquia
	 * @param otraEdadMenopausia
	 * @param cilcoMenstrual
	 * @param duracionMenstruacion
	 * @param furAnt
	 * @param dolorMenstruacion
	 * @param conceptoMenstruacion
	 * @param observacionesMenstruacion
	 * @param usuario
	 * @param codigoPersona
	 * @param numeroSolicitud
	 */
	public static int inseretarDatosHistMenstrual(Connection con, int edadMenarquia, int edadMenopausia, String otraEdadMenarquia, String otraEdadMenopausia, String cilcoMenstrual, String duracionMenstruacion, String furAnt, String dolorMenstruacion, int conceptoMenstruacion, String observacionesMenstruacion, UsuarioBasico usuario, int codigoPersona, int numeroSolicitud)
	{
		String tempoObservaciones="";
		if(!observacionesMenstruacion.equals(""))
		{
			tempoObservaciones=UtilidadTexto.agregarTextoAObservacion(null, observacionesMenstruacion, usuario, false);
		}
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAntecedentesGinecoObstetricosDao().inseretarDatosHistMenstrual(con, edadMenarquia, edadMenopausia, otraEdadMenarquia, otraEdadMenopausia, cilcoMenstrual, duracionMenstruacion, furAnt, dolorMenstruacion, conceptoMenstruacion, tempoObservaciones, codigoPersona, numeroSolicitud);
	}
	
	/**
	 * Método para insertar las características de la menstruación
	 * @param con
	 */
	
	public void insertarCaracteristicasMenstruacion(Connection con)
	{
		if (antecedentesHistoricos!=null)
		{
			AntecedentesGinecoObstetricosHistorico antecedenteHistorico=new AntecedentesGinecoObstetricosHistorico();
			for (int i=0;i<antecedentesHistoricos.size();i++)
			{
				antecedenteHistorico= (AntecedentesGinecoObstetricosHistorico )antecedentesHistoricos.get(i);
				//Si es el último elemento y no hay embarazos terminamos la transaccion
				//en caso contrario continuamos
				if (i==antecedentesHistoricos.size()-1&&( embarazos==null||embarazos.size()<1 ) )
				{
					try
					{
						antecedenteHistorico.insertarTransaccional(con, this.paciente.getCodigoPersona(),"finalizar");
					} catch (SQLException e)
					{
						logger.warn(e);
					}
				}
				else
				{
					try
					{
						antecedenteHistorico.insertarTransaccional(con, this.paciente.getCodigoPersona(), "continuar");
					} catch (SQLException e)
					{
						logger.warn(e);
					}
				}
			}
		}
	}

	/**
	 * @param con
	 * @param paciente2
	 * @param antecedentesBean
	 * @throws SQLException
	 */
	public void cargarDatosEmbarazo(Connection con, PersonaBasica paciente2, AntecedentesGinecoObstetricosForm antecedentesBean) throws SQLException
	{
		ResultSetDecorator rsACargar=antecedenteGinecoObstetricoDao.cargarHistorico(con, paciente2.getCodigoPersona(), 0);

		if (rsACargar.next())
		{
			antecedentesBean.setP2500(UtilidadTexto.getBoolean(rsACargar.getBoolean("p2500")+"")+"");
			antecedentesBean.setP4000(UtilidadTexto.getBoolean(rsACargar.getBoolean("p4000")+"")+"");
			antecedentesBean.setMayorA2(UtilidadTexto.getBoolean(rsACargar.getBoolean("mayora2")+"")+"");
			antecedentesBean.setFinEmbarazoAnterior(rsACargar.getString("finembarazoanterior"));
			antecedentesBean.setFinEmbarazoMayor1o5(rsACargar.getString("finembarazomayor1o5"));
			antecedentesBean.setPrematuros(rsACargar.getString("prematuros"));
			antecedentesBean.setEctropicos(rsACargar.getString("ectropicos"));
			antecedentesBean.setMultiples(rsACargar.getString("multiples"));
			
		}
		rsACargar.close();
	}

}