/*
 * Creado el 21-nov-2005
 * por Julian Montoya
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import java.util.HashMap;

import util.UtilidadCadena;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ProgramacionCirugiaDao;



/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class ProgramacionCirugia {
	
	/**
	 * Fecha de la Peticion 
	 */	
	private String fechaPeticion;
	
	/**
	 * Hora de la peticion 
	 * */
	private String horaPeticion;
	
	/**
	 *  Fecha estimada de Cirugia
	 */
	private String fechaEstimadaCirugia;
	
	/**
	 *  Nombre del Medico que solicita 
	 */
	private String solicitante;
	
	/**
	 * Duracion Aproximada de la cirugia
	 */
	private String duracion;
	
	/**
	 * Numero del Servicio inicial (consecutivo)
	 */
	private int nroIniServicio;

	/**
	 * Numero del Servicio Final (consecutivo)
	 */
	private int nroFinServicio;
	
	
	/**
	 * Fecha inicial peticion del servicio
	 */
	private String fechaIniPeticion;
	
	/**
	 * Fecha final de peticion del servicio 
	 */
	private String fechaFinPeticion;
	
	/**
	 * Fecha Inicial de Cirugia 
	 */
	private String fechaIniCirugia;

	/**
	 * Fecha Inicial de Cirugia 
	 */
	private String fechaFinCirugia;
	
	/**
	 * Medico que solicita la peticion 
	 */
	private int profesional;

	/**
	 * Fecha para manejar la navegacion
	 */
	private String fechaProgramacion;
	
	/**
	 * Para Almacenar la Hora de Inicio de Programacion
	 */
	private String horaInicioProgramacion;

	/**
	 * Para Almacenar la Hora Final de Programacion
	 */
	private String horaFinProgramacion;
	
	/**
	 * Variable para almacenar el codigo
	 * de la sala donde se va a programar 
	 */
	private int nroSala;
	
	/**
	 * Variable para almacenar el centro de atencion 
	 */
	private int centroAtencion;
	
	/**
	 * DAO para el manejo de Peticiones de Cirugía
	 */
	private ProgramacionCirugiaDao programacionCirugiaDao = null;
	
	
	/**
	 * String para almacenar el estado de la  peticion en la busqueda avanzada 
	 * de reprogramacion de cirugias.  
	 */
	private String estadoPeticion;
	
	
	/**
	 * Para Postular el tipo de anestesia registrado en la preanestesia.
	 */
	private int tipoAnestesiaPreanestesia;

	
	/**
	 * Metodo para retornar la información de peticion de cirugia 
	 * @param con
	 * @param nroPeticion
	 * @return
	 */
   public int cargarCodigoPersona(Connection con, int nroPeticion)
   {
   	 int codigo = 0;
   	 Collection lista = null; 
   	 
   	lista = programacionCirugiaDao.cargarCodigoPersona(con, nroPeticion);
	Iterator ite=lista.iterator();
	
		if (ite.hasNext())
		 {
			HashMap col=(HashMap) ite.next();
			codigo = (col.get("codigo")+"").equals("null") ? 0 : Integer.parseInt(col.get("codigo")+"");
		 }
	
		return codigo;
   }

	
	/**
	 * Metodo para retornar la información de peticion de cirugia 
	 * @param con
	 * @param nroPeticion
	 * @return
	 */
   public Collection cargarInformacionPeticion(Connection con, int nroPeticion)
   {
   	 Collection lista = null;
   	
   	 lista = programacionCirugiaDao.cargarInformacionPeticion(con, nroPeticion, 1);
     Iterator ite=lista.iterator();
    	
	if (ite.hasNext())
	 {
		HashMap col=(HashMap) ite.next();
		this.tipoAnestesiaPreanestesia = UtilidadTexto.isEmpty(col.get("anestesia")+"") ? 0 : Integer.parseInt(col.get("anestesia")+"");
	 }
	 else
	 {
		this.tipoAnestesiaPreanestesia = 0;
   	 }	

   	 
   	 
   	//----La información ....  
   	lista = programacionCirugiaDao.cargarInformacionPeticion(con, nroPeticion, 0);
	ite=lista.iterator();
	
	if (ite.hasNext())
	 {
		HashMap col=(HashMap) ite.next();
		this.fechaPeticion = (col.get("fecha_peticion")+"").equals("null")  ? "" : (col.get("fecha_peticion")+"");
		this.horaPeticion = (col.get("hora_peticion")+"").equals("null")  ? "" : (col.get("hora_peticion")+"");		
		this.estadoPeticion = (col.get("estado")+"").equals("null")  ? "" : (col.get("estado")+"");		
		this.fechaEstimadaCirugia = (col.get("fecha_cirugia")+"").equals("null")  ? "" : (col.get("fecha_cirugia")+"");
		this.solicitante = (col.get("solicitante")+"").equals("null")  ? "" : (col.get("solicitante")+"");
		this.duracion = (col.get("duracion")+"").equals("null")  ? "" : (col.get("duracion")+"");
	 }
	 else
	 {
		this.fechaPeticion = "";
		this.fechaEstimadaCirugia = "";
		this.solicitante = "";
		this.estadoPeticion = "";
		this.horaPeticion = "";
	 }	

	
	return lista;
   	 //-Retornar la coleccion con las descripciones de las cirugias 	
    // return programacionCirugiaDao.cargarInformacionCirugias(con, nroPeticion);
   }
	    
   /**
	 * Metodo para retornar la información de las salas 
	 * @param con
	 * @return
	 */
	 public Collection cargarSalas(Connection con)
	  {
	  	return programacionCirugiaDao.cargarSalas(con);
	  }

	/**
	 * Metodo para listar las peticiones por paciente especifico con estado pendiente
	 * @param con
	 * @param codigoPersona
	 * @param centroAtencion 
	 * @param codigoCuenta 
	 * @return
	 */
  	 public Collection cargarListadoPeticionesPaciente(Connection con, int codigoPersona, int centroAtencion, int codigoCuenta)
	 {
		return programacionCirugiaDao.cargarListadoPeticionesPaciente(con, codigoPersona, centroAtencion, codigoCuenta);
	 }

  	 
  	 
  	/**
  	 * Metodo para  Consultar los servicios asociadas a las peticiones...
 	 * @param con
 	 * @param codigoPersona
  	 * @param centroAtencion 
  	 * @param codigoCuenta 
 	 * @return
 	 */
 	public Collection cargarInformacionServiciosPeticion(Connection con, int codigoPersona, int centroAtencion, int codigoCuenta)
 	{
		return programacionCirugiaDao.cargarInformacionServiciosPeticion(con, codigoPersona, centroAtencion, codigoCuenta);
 	}

 	/**
 	 * Metodo para listar las peticiones de acuerdo a unos parametros de busqueda
	 * @param con
	 * @return
	 */
	public Collection cargarListadoPeticionesBusqueda(Connection con)
	{
		return programacionCirugiaDao.cargarListadoPeticionesBusqueda(con, this.nroIniServicio, this.nroFinServicio, this.fechaIniPeticion,
																		   this.fechaFinPeticion, this.fechaIniCirugia, this.fechaFinCirugia,
																		   this.profesional);
	}

 	/**
 	 * Metodo para listar las peticiones de acuerdo a unos parametros de busqueda. Para la Funcionalidad de Consulta de Cirugias Programadas.  
	 * @param con
	 * @return
	 */
	public HashMap  cargarListadoConsultaPeticionesBusqueda(Connection con)
	{
		HashMap mp = new HashMap();
		mp =  programacionCirugiaDao.cargarListadoConsultaPeticionesBusqueda(con, this.nroIniServicio, this.nroFinServicio, this.fechaIniPeticion,
																				  this.fechaFinPeticion, this.fechaIniCirugia, this.fechaFinCirugia,
																		     	  this.profesional, this.centroAtencion);
		
		//-Establecer de una vez las cantidad de cirujanos, anestesiologos y observaciones de los servicios
		//-para cada una de las peticiones.
		int nroReg = 0;
		if ( UtilidadCadena.noEsVacio( mp.get("numRegistros") +"" ) )
		{
			nroReg = Integer.parseInt( mp.get("numRegistros") +"" );
		}
		
		for (int i=0; i<nroReg; i++)
		{
			String codPet = mp.get("codigo_peticion_" + i)+"", nombreC = "";
			int nroRgC = 0;

			//-Colocar los cirujanos
			if ( UtilidadCadena.noEsVacio( mp.get("numRegCirujanos") +"" ) ) { nroRgC = Integer.parseInt( mp.get("numRegCirujanos") +"" );	}
			for (int j=0; j<nroRgC; j++)
			{
				if ( (mp.get("codigo_peticion_cirujano_"+ j)+"").equals(codPet) )
				{
					if (nombreC.equals("")) { nombreC = mp.get("cirujano_" + j)+""; }
					else { nombreC += ",  " + mp.get("cirujano_"+ j)+""; }
				}
			}
			mp.put("cirujanof_"+ i, nombreC);

			//------Colocar los anestesiologos.
			nroRgC = 0;	nombreC = "";
			if ( UtilidadCadena.noEsVacio( mp.get("numRegAnestesiologos") +"" ) ) { nroRgC = Integer.parseInt( mp.get("numRegAnestesiologos") +"" );	}
			for (int j=0; j<nroRgC; j++)
			{

				if ( (mp.get("codigo_peticion_anestesiologo_"+ j)+"").equals(codPet) )
				{
					if (nombreC.equals("")) { nombreC = mp.get("anestesiologo_"+ j)+""; }
					else { nombreC += ",  " +  mp.get("anestesiologo_"+ j)+""; }
				}
			}
			mp.put("anestesiologof_"+ i, nombreC);
			

			//------Colocar las Observaciones de los servicios.
			nroRgC = 0;	nombreC = "";
			if ( UtilidadCadena.noEsVacio( mp.get("numRegObser") +"" ) ) { nroRgC = Integer.parseInt( mp.get("numRegObser") +"" );	}
			for (int j=0; j<nroRgC; j++)
			{
				if ( (mp.get("codigo_peticion_obser_"+ j)+"").equals(codPet) )
				{
					if (UtilidadCadena.noEsVacio(mp.get("observaciones_"+j)+""))
					{
						if (nombreC.equals("")) { nombreC = mp.get("observaciones_"+j)+""; }
						else { nombreC +=  "  +  " +  mp.get("observaciones_"+j)+""; }
					}	
				}
			}
			mp.put("observacionesf_"+ i, nombreC);
		}
		
		return mp;
	}

	
 	/**
 	 * Metodo para listar las peticiones de acuerdo a unos parametros de busqueda pero solo las de estados programadas y reprogramadas 
	 * @param con
	 * @return
	 */
	public Collection accionResultadoBusquedaAvanzadaProgramadas(Connection con)
	{
		return programacionCirugiaDao.accionResultadoBusquedaAvanzadaProgramadas(con, this.nroIniServicio, this.nroFinServicio, this.fechaIniPeticion,
																		   this.fechaFinPeticion, this.fechaIniCirugia, this.fechaFinCirugia,
																		   this.profesional);
	}

	
 	/**
 	 * Metodo para listar los servicios de las peticiones que se consulten de acuerdo a unos parametros de busqueda
	 * @param con
	 * @return
	 */
	public Collection cargarInformacionServiciosBusqueda(Connection con)
	{
		return programacionCirugiaDao.cargarInformacionServiciosBusqueda(con, this.nroIniServicio, this.nroFinServicio, this.fechaIniPeticion,
																			  this.fechaFinPeticion, this.fechaIniCirugia, this.fechaFinCirugia,
																		      this.profesional);
	}

 	/**
 	 * Metodo para listar los servicios de las peticiones. Para la busqueda avanzada de la funcionalidad de consulta de cirugias programadas
	 * @param con
 	 * @param mapaPeticion 
	 * @return
	 */
	public HashMap cargarListadoConsultaServiciosBusqueda(Connection con, HashMap mapaPeticion)
	{
		HashMap mp = programacionCirugiaDao.cargarListadoConsultaServiciosBusqueda(con, this.nroIniServicio, this.nroFinServicio, this.fechaIniPeticion,
																						this.fechaFinPeticion, this.fechaIniCirugia, this.fechaFinCirugia,
																						this.profesional);

		int nroReg = 0;
		if ( UtilidadCadena.noEsVacio( mapaPeticion.get("numRegistros") +"" ) )
		{
			nroReg = Integer.parseInt( mapaPeticion.get("numRegistros") +"" );
		}

		int nroRgSer = 0;
		//-Colocar los cirujanos
		if ( UtilidadCadena.noEsVacio( mp.get("numRegistros") +"" ) ) { nroRgSer = Integer.parseInt( mp.get("numRegistros") +"" );	}
		
		//-Establecer los servicios para cada peticion. 
		for (int i=0; i<nroReg; i++)
		{
			String codPet = mapaPeticion.get("codigo_peticion_" + i)+"", nombreC = "";
			for (int j=0; j<nroRgSer; j++)
			{
				if ( (mp.get("codigo_peticion_servicio_"+ j)+"").equals(codPet) )
				{			  	
					if (nombreC.equals("")) { nombreC = mp.get("servicio_" + j)+""; }
					else { nombreC += "  +  " + mp.get("servicio_"+ j)+""; }
				}
			}
			mp.put("serviciof_"+ i, nombreC);
		}
		

		
		return mp;

		
	}
	

 	/**
 	 * Metodo para listar los servicios de las peticiones que se consulten de acuerdo a unos parametros de busqueda
	 * @param con
	 * @return
	 */
	public Collection cargarInformacionServiciosBusquedaProgramadas(Connection con)
	{
		return programacionCirugiaDao.cargarInformacionServiciosBusquedaProgramadas(con, this.nroIniServicio, this.nroFinServicio, this.fechaIniPeticion,
																			  this.fechaFinPeticion, this.fechaIniCirugia, this.fechaFinCirugia,
																		      this.profesional);
	}
	
	
	/**
 	 * Metodo para listar las peticiones de acuerdo a unos parametros de busqueda en la Reprogramacion de Cirugias 
	 * @param con
	 * @return
	 */
	public Collection cargarListadoPeticionesBusquedaReprogramacion(Connection con)
	{
		return programacionCirugiaDao.cargarListadoPeticionesBusquedaReprogramacion(con, this.nroIniServicio, this.nroFinServicio, this.estadoPeticion,
																						 this.profesional, this.fechaIniCirugia, this.fechaFinCirugia);
	}

	/**
	 * Metodo para consultar la informacion de los servicios de la busqueda avanzada de reprogramacion 
	 * @param con
	 * @return
	 */
	public Collection cargarInformacionServiciosPeticionReprogramacion(Connection con)
	{
		return programacionCirugiaDao.cargarInformacionServiciosPeticionReprogramacion(con, this.nroIniServicio, this.nroFinServicio, this.estadoPeticion,
																							this.profesional, this.fechaIniCirugia, this.fechaFinCirugia);
	}
	
	/**
	 * Metodo para cargar la programacion regsitrada de cada sala
	 * @param con
	 * @param fechaCirugia
	 * @return
	 */
	public Collection cargarProgramacionSalas(Connection con, String fechaCirugia)
	{
		return programacionCirugiaDao.cargarProgramacionSalas(con, fechaCirugia);
	}

	
	/**
	 * Metodo para cargar listado de las peticiones de programadas y reprogramadas
	 * @param con
	 * @param codigoPersona 
	 * @param nroPeticion
	 * @return
	 */
	public Collection cargarListadoPeticionesProgramadas(Connection con, int centroAtencion)
	{
		return programacionCirugiaDao.cargarListadoPeticionesProgramadas(con, centroAtencion);
	}

	/**
	 * Metodo para cargar listado de las peticiones para la funcionalidad Consulta de programacaion de Cirugias.
	 * @param con
	 * @param codigoPersona 
	 * @param codigoPersona 
	 * @param nroPeticion
	 * @return
	 */
	public HashMap cargarListadoPeticiones(Connection con, int codigoPersona)
	{
		HashMap mp = new HashMap();
		mp = programacionCirugiaDao.cargarListadoPeticiones(con, codigoPersona);
		
		//-Establecer de una vez las cantidad de cirujanos, anestesiologos y observaciones de los servicios
		//-para cada una de las peticiones.
		int nroReg = 0;
		if ( UtilidadCadena.noEsVacio( mp.get("numRegistros") +"" ) )
		{
			nroReg = Integer.parseInt( mp.get("numRegistros") +"" );
		}
		
		for (int i=0; i<nroReg; i++)
		{
			String codPet = mp.get("codigo_peticion_" + i)+"", nombreC = "";
			int nroRgC = 0;

			//-Colocar los cirujanos
			if ( UtilidadCadena.noEsVacio( mp.get("numRegCirujanos") +"" ) ) { nroRgC = Integer.parseInt( mp.get("numRegCirujanos") +"" );	}
			for (int j=0; j<nroRgC; j++)
			{
				if ( (mp.get("codigo_peticion_cirujano_"+ j)+"").equals(codPet) )
				{
					if (nombreC.equals("")) { nombreC = mp.get("cirujano_" + j)+""; }
					else { nombreC += ",  " + mp.get("cirujano_"+ j)+""; }
				}
			}
			mp.put("cirujanof_"+ i, nombreC);

			//------Colocar los anestesiologos.
			nroRgC = 0;	nombreC = "";
			if ( UtilidadCadena.noEsVacio( mp.get("numRegAnestesiologos") +"" ) ) { nroRgC = Integer.parseInt( mp.get("numRegAnestesiologos") +"" );	}
			for (int j=0; j<nroRgC; j++)
			{

				if ( (mp.get("codigo_peticion_anestesiologo_"+ j)+"").equals(codPet) )
				{
					if (nombreC.equals("")) { nombreC = mp.get("anestesiologo_"+ j)+""; }
					else { nombreC += ",  " +  mp.get("anestesiologo_"+ j)+""; }
				}
			}
			mp.put("anestesiologof_"+ i, nombreC);
			

			//------Colocar las Observaciones de los servicios.
			nroRgC = 0;	nombreC = "";
			if ( UtilidadCadena.noEsVacio( mp.get("numRegObser") +"" ) ) { nroRgC = Integer.parseInt( mp.get("numRegObser") +"" );	}
			for (int j=0; j<nroRgC; j++)
			{
				if ( (mp.get("codigo_peticion_obser_"+ j)+"").equals(codPet) )
				{
					if (UtilidadCadena.noEsVacio(mp.get("observaciones_"+j)+""))
					{
						if (nombreC.equals("")) { nombreC = mp.get("observaciones_"+j)+""; }
						else { nombreC +=  "  +  " +  mp.get("observaciones_"+j)+""; }
					}	
				}
			}
			mp.put("observacionesf_"+ i, nombreC);
			
			
		}
		
		return mp;
	}

	
	/**
	 * Cargar Servicios Peticion. Para la funcionalidad de Consulta de Cirugias 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap cargarServiciosPeticion(Connection con, int codigoPersona, HashMap mapaPeticion) 
	{
		//-----Mapa de los servicios con asociados a las peticiones.	
		HashMap mp = programacionCirugiaDao.cargarServiciosPeticion(con, codigoPersona); 

		int nroReg = 0;
		if ( UtilidadCadena.noEsVacio( mapaPeticion.get("numRegistros") +"" ) )
		{
			nroReg = Integer.parseInt( mapaPeticion.get("numRegistros") +"" );
		}

		int nroRgSer = 0;
		//-Colocar los cirujanos
		if ( UtilidadCadena.noEsVacio( mp.get("numRegistros") +"" ) ) { nroRgSer = Integer.parseInt( mp.get("numRegistros") +"" );	}
		
		//-Establecer los servicios para cada peticion. 
		for (int i=0; i<nroReg; i++)
		{
			String codPet = mapaPeticion.get("codigo_peticion_" + i)+"", nombreC = "";
			for (int j=0; j<nroRgSer; j++)
			{
				if ( (mp.get("codigo_peticion_servicio_"+ j)+"").equals(codPet) )
				{
					if (nombreC.equals("")) { nombreC = mp.get("servicio_" + j)+""; }
					else { nombreC += "  +  " + mp.get("servicio_"+ j)+""; }
				}
			}
			mp.put("serviciof_"+ i, nombreC);
		}
		

		
		return mp;
		
	}
	
	/**
	 * Metodo para insertar la programacion de una sala a una hora determinada  
	 * @param con
	 * @param loginUsuario
	 * @param numeroPeticion 
	 * @param operacion : 0 para programar 1 para reprogramar
	 */
	public int insertarProgamacion(Connection con, String loginUsuario, int numeroPeticion, int operacion)
	{
	 return programacionCirugiaDao.insertarProgamacion(con, numeroPeticion, this.fechaProgramacion, this.nroSala, this.horaInicioProgramacion, this.horaFinProgramacion, loginUsuario, operacion);
	}
	
	/**
	 * Metodo para dejar disponible la peticion de cirugia de nuevo y eliminar la programacion de la misma
	 * @param con
	 * @param numeroPeticion
	 */
	public int eliminarProgramacion(Connection con, int numeroPeticion)
	{
		 return programacionCirugiaDao.eliminarProgramacion(con, numeroPeticion);
	}
	
	
	public HashMap consultaProgramacionSalasQx(Connection con, int numeroPeticion)
	{
		return programacionCirugiaDao.consultaProgramacionSalasQx(con, numeroPeticion);
	}
	
	public int insertarCancelacionProgramacionSalasQx(Connection con, HashMap rep)
	{
		return programacionCirugiaDao.insertarCancelacionProgramacionSalasQx(con,rep);
	}
	
	
	
	
	
	
	/**
	 * Constructor
	 *
	 */
	public ProgramacionCirugia()
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (programacionCirugiaDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			programacionCirugiaDao = myFactory.getProgramacionCirugiaDao();
		}	
	}
	
	
	
	/**
	 * Metodo para iniciar 
	 * las variables del Objeto
	 */
	private void reset()
	{
		
	}

	/**
	 * @return Retorna fechaEstimadaCirugia.
	 */
	public String getFechaEstimadaCirugia() {
		return fechaEstimadaCirugia;
	}
	/**
	 * @param Asigna fechaEstimadaCirugia.
	 */
	public void setFechaEstimadaCirugia(String fechaEstimadaCirugia) {
		this.fechaEstimadaCirugia = fechaEstimadaCirugia;
	}
	/**
	 * @return Retorna fechaPeticion.
	 */
	public String getFechaPeticion() {
		return fechaPeticion;
	}
	/**
	 * @param Asigna fechaPeticion.
	 */
	public void setFechaPeticion(String fechaPeticion) {
		this.fechaPeticion = fechaPeticion;
	}
	/**
	 * @return Retorna programacionCirugiaDao.
	 */
	public ProgramacionCirugiaDao getProgramacionCirugiaDao() {
		return programacionCirugiaDao;
	}
	/**
	 * @param Asigna programacionCirugiaDao.
	 */
	public void setProgramacionCirugiaDao(
			ProgramacionCirugiaDao programacionCirugiaDao) {
		this.programacionCirugiaDao = programacionCirugiaDao;
	}
	/**
	 * @return Retorna solicitante.
	 */
	public String getSolicitante() {
		return solicitante;
	}
	/**
	 * @param Asigna solicitante.
	 */
	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}
	/**
	 * @return Retorna duracion.
	 */
	public String getDuracion() {
		return duracion;
	}
	/**
	 * @param Asigna duracion.
	 */
	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	/**
	 * @return Retorna fechaFinCirugia.
	 */
	public String getFechaFinCirugia() {
		return fechaFinCirugia;
	}
	/**
	 * @param Asigna fechaFinCirugia.
	 */
	public void setFechaFinCirugia(String fechaFinCirugia) {
		this.fechaFinCirugia = fechaFinCirugia;
	}
	/**
	 * @return Retorna fechaFinPeticion.
	 */
	public String getFechaFinPeticion() {
		return fechaFinPeticion;
	}
	/**
	 * @param Asigna fechaFinPeticion.
	 */
	public void setFechaFinPeticion(String fechaFinPeticion) {
		this.fechaFinPeticion = fechaFinPeticion;
	}
	/**
	 * @return Retorna fechaIniCirugia.
	 */
	public String getFechaIniCirugia() {
		return fechaIniCirugia;
	}
	/**
	 * @param Asigna fechaIniCirugia.
	 */
	public void setFechaIniCirugia(String fechaIniCirugia) {
		this.fechaIniCirugia = fechaIniCirugia;
	}
	/**
	 * @return Retorna fechaIniPeticion.
	 */
	public String getFechaIniPeticion() {
		return fechaIniPeticion;
	}
	/**
	 * @param Asigna fechaIniPeticion.
	 */
	public void setFechaIniPeticion(String fechaIniPeticion) {
		this.fechaIniPeticion = fechaIniPeticion;
	}
	/**
	 * @return Retorna nroFinServicio.
	 */
	public int getNroFinServicio() {
		return nroFinServicio;
	}
	/**
	 * @param Asigna nroFinServicio.
	 */
	public void setNroFinServicio(int nroFinServicio) {
		this.nroFinServicio = nroFinServicio;
	}
	/**
	 * @return Retorna nroIniServicio.
	 */
	public int getNroIniServicio() {
		return nroIniServicio;
	}
	/**
	 * @param Asigna nroIniServicio.
	 */
	public void setNroIniServicio(int nroIniServicio) {
		this.nroIniServicio = nroIniServicio;
	}
	/**
	 * @return Retorna profesional.
	 */
	public int getProfesional() {
		return profesional;
	}
	/**
	 * @param Asigna profesional.
	 */
	public void setProfesional(int profesional) {
		this.profesional = profesional;
	}

	
	/**
	 * @return Retorna fechaProgramacion.
	 */
	public String getFechaProgramacion() {
		return fechaProgramacion;
	}
	/**
	 * @param Asigna fechaProgramacion.
	 */
	public void setFechaProgramacion(String fechaProgramacion) {
		this.fechaProgramacion = fechaProgramacion;
	}
	/**
	 * @return Retorna horaFinProgramacion.
	 */
	public String getHoraFinProgramacion() {
		return horaFinProgramacion;
	}
	/**
	 * @param Asigna horaFinProgramacion.
	 */
	public void setHoraFinProgramacion(String horaFinProgramacion) {
		this.horaFinProgramacion = horaFinProgramacion;
	}
	/**
	 * @return Retorna horaInicioProgramacion.
	 */
	public String getHoraInicioProgramacion() {
		return horaInicioProgramacion;
	}
	/**
	 * @param Asigna horaInicioProgramacion.
	 */
	public void setHoraInicioProgramacion(String horaInicioProgramacion) {
		this.horaInicioProgramacion = horaInicioProgramacion;
	}
	/**
	 * @return Retorna nroSala.
	 */
	public int getNroSala() {
		return nroSala;
	}
	/**
	 * @param Asigna nroSala.
	 */
	public void setNroSala(int nroSala) {
		this.nroSala = nroSala;
	}
	
	/**
	 * @return Retorna estadoPeticion.
	 */
	public String getEstadoPeticion() {
		return estadoPeticion;
	}

	/**
	 * @param Asigna estadoPeticion.
	 */
	public void setEstadoPeticion(String estadoPeticion) {
		this.estadoPeticion = estadoPeticion;
	}


	/**
	 * @return Retorna tipoAnestesiaPreanestesia.
	 */
	public int getTipoAnestesiaPreanestesia() {
		return tipoAnestesiaPreanestesia;
	}


	/**
	 * @param Asigna tipoAnestesiaPreanestesia.
	 */
	public void setTipoAnestesiaPreanestesia(int tipoAnestesiaPreanestesia) {
		this.tipoAnestesiaPreanestesia = tipoAnestesiaPreanestesia;
	}


	public int getCentroAtencion() {
		return centroAtencion;
	}


	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	/**
	 * @return the horaPeticion
	 */
	public String getHoraPeticion() {
		return horaPeticion;
	}


	/**
	 * @param horaPeticion the horaPeticion to set
	 */
	public void setHoraPeticion(String horaPeticion) {
		this.horaPeticion = horaPeticion;
	}


	
}
