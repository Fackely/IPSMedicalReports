/*
 * Creado en Jun 14, 2005
 */
package com.princetonsa.mundo.hojaObstetrica;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.HojaObstetricaDao;
import com.princetonsa.mundo.UsuarioBasico;


/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class HojaObstetrica
{
	/**
	 * Para hacer logs de esta funcionalidad.
	*/
		private Logger logger = Logger.getLogger(HojaObstetrica.class);
		
    //SECCION EMBARAZO
		
	private int numeroEmbarazo;

	//SECCION INFORMACIÓN DEL EMBARAZO
		
	/**
	  * Fecha de grabacion de la orden
	  */
		private String fechaGrabacion;
		
	/**
	 * Hora de grabación de la orden
	 */
		private String horaGrabacion;	
		
	/**
	  * Fecha de registro de la hoja obstétrica
	  */
	   private String fechaOrden;
	   
   /**
	  * Fecha de la última regla
	  */
	   private String fur;
	   
	   /**
	    * Fur temporal
	    */
		   private String furTemp;
	   
   /**
	  * Fecha Probable del Parto
	  */
	   private String fpp;
	   
   /**
	  * Fecha de realización del Ultrasonido
	  */
	   private String fechaUltrasonido;
	   
   /**
	  * Fecha Probable de Parto de acuerdo al ultrasonido
	  */
	   private String fppUltrasonido;
	   
   /**
	  * Edad Gestacional del bebé
	  */
	   private int edadGestacional;
	   
   /**
	  * Edad al parte o del paciente 
	  */
	   private int edadParto;
	   
   /**
	  * Finalización del embarazo 
	  */
	   private boolean finalizacionEmbarazo;
	   
   /**
	  * Fecha de terminación
	  */
	   private String fechaTerminacion;
	   
	   /**
	    * Motivo de finalización que postula la información
	    * ingresada en el campo tipos partode antecedentes
	    * gineco obstetricos
	    */
	   private int motivoFinalizacion;

	   /**
	    *  Descripcion del motivo finalización
	    */
	   private String nombreMotivoFinalizacion;
	   
	   /**
	    * Campo para guardar el otro motivo finalización, cuando seleccionan
	    * otro en el motivo de finalización
	    */
	   private String otroMotivoFinalizacion;
	   
	   /**
	    * Campo que postula la información ingresada en el campo
	    * semanas de gestación del último registro en antecedentes
	    * gineco-obstétricos 
	    */
	   
	   private int egFinalizar;
	       
	   //SECCION DE RESUMEN GESTACIONAL
	       
	   /**
		  * Edad Gestacional en el resumen
		  */
		   private int edadGestacionalResumen;
	   
	   /**
	    * Fecha del Resumen Gestacional
	    */
	   
	   private String fechaGestacional;
	   
	   /**
	    * Hora del Resumen Gestacional
	    */
	   
	   private String horaGestacional; 
	   
	    /**
	    * Campo para guardar las observaciones generales de la hoja obstétrica
	    */
	   private String observacionesGrales;
	   
	   //SECCION DE EXÁMENES DE LABORATORIO
	   /**
		  * Edad Gestacional en los exámenes de labaratorio
		  */
		   private int edadGestacionalExamen;
		   
			/**
			 * Fecha de los exámenes de laboratorio
			 */
			private String fechaExamen;
			
			/**
			 * Hora de los exámenes de laboratorio
			 */
			private String horaExamen;
	   
	   
	   //*******************FIN DE LA SECCION DE INFORMACIÓN DE EMBARAZO**************************//
	   
	   /**
		 * Interfaz para acceder la fuente de datos
		 */
		private HojaObstetricaDao hojaObstetricaDao = null;

   	    //-----------------------CAMPOS PARA LA BUSQUEDA AVANZADA----------------------------------	
		/**
		 * Listado de las mujeres embarazadas
		 */
		private Collection listado;

		/***
		 * Para tener los nombres del paciente
		 */
		private String nombre;
	   
		/***
		 * Para tener los apellidos del pacientes
		 */
		private String apellido;		

		/***
		 * Para tener la cedula del paciente
		 */
		private String id;
		/**
		 * Para tener edad del paciente 
		 */
		private int edad;
		/**
		 * Para tener el nombre del medico 
		 */
		private String nombreMedico;
		
		/**
		 * Duración embarazo
		 */
		private String duracion;
		
		/**
		 * Tiempo de ruptura de membranas
		 */
		private String tiempoRupturaMemebranas;
		
		/**
		 * Legrado embarazo
		 */
		private String legrado;
		
		//************************************SECCION DE ULTRASONIDOS*****************************************//
		
		/**
		 * Fecha del ultrasonido
		 */
		private String ultrasonidoFecha;
		
		/**
		 * Hora del ultrasonido
		 */
		private String ultrasonidoHora;
		
		//************************************SECCION PLAN DE MANEJO*****************************************//
		 /**
	     * Campo para indicar en la información del embarazo si es o no confiable
	     */
		 private String confiable;
		 
		 /**
		  * Campo de vigente antitetanica
		 */
		 private String vigenteAntitetanica;
		 
		 /**
		  * Campo de las dosis de antitetanica (select) 
		  */
		 private String dosisAntitetanica;
		 
		 /**
		  * Campo de los meses de gestacion de antitetanica
		  */
		 private String mesesGestacionAntitetanica;
		 
		 /**
		  * Campo para guardar la antirubeola (select)
		  */
		 private String antirubeola;
		 
		 /**
		  * Campo peso
		  */
		 private String peso;
		 
		 /**
		  * Campo talla
		  */
		 private String talla;
		 
		 /**
		  * Campo SI/NO embarazo deseado
		  */
		 private String embarazoDeseado;
		 
		 /**
		  * Campo SI/NO embarazo planeado
		  */
		 private String embarazoPlaneado;
		 
		 
		 /**
		  * Mapa para guardar la información que se ingresa en la sección plan manejo
		  */
		 private HashMap mapaPlanManejo;
		
		/**
		 * Este método inicializa en valores vacíos, -mas no nulos- los atributos de este objeto.
		 */
		public void reset() 
		{
			this.numeroEmbarazo=0;
			this.fechaGrabacion="";
			this.horaGrabacion="";
			this.fechaOrden="";
			this.fur="";
			this.fpp="";
			this.fechaUltrasonido="";
			this.fppUltrasonido="";
			this.edadGestacional=0;
			this.edadParto=0;
			this.finalizacionEmbarazo=false;
			this.fechaTerminacion="";
			this.motivoFinalizacion=0;
			this.egFinalizar=0;
			this.otroMotivoFinalizacion="";
			this.edadGestacionalResumen=0;
			this.edadGestacionalExamen=0;
			this.fechaExamen="";
			this.horaExamen="";
			this.ultrasonidoFecha="";
			this.ultrasonidoHora="";
			this.furTemp="";
			
			//---Seccion Plan de Manejo ---------//
			this.confiable=ConstantesBD.acronimoSi;
			this.vigenteAntitetanica=ConstantesBD.acronimoSi;
			this.dosisAntitetanica="";
			this.mesesGestacionAntitetanica="";
			this.antirubeola="";
			this.peso = "";
			this.talla = "";
			this.embarazoDeseado = "";
			this.embarazoPlaneado = "";
			
			this.mapaPlanManejo=new HashMap();
			this.mapaPlanManejo.put("numRegistros","0");
		}
		
		/**
		 * Constructor de la clase, inicializa en vacio todos los parámetros
		 */		
		public HojaObstetrica ()
		{
			reset();
			this.init (System.getProperty("TIPOBD"));
		}
		
		/**
		 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
		 * @param tipoBD el tipo de base de datos que va a usar este objeto
		 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
		 * son los nombres y constantes definidos en <code>DaoFactory</code>.
		 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
		 */
		public boolean  init(String tipoBD)
		{
			boolean wasInited = false;
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

			if (myFactory != null)
			{
				hojaObstetricaDao = myFactory.getHojaObstetricaDao();
				wasInited = (hojaObstetricaDao != null);
			}
			return wasInited;
		}
	   
	/**
	 * @return Returns the numeroEmbarazo.
	 */
	public int getNumeroEmbarazo()
	{
		return numeroEmbarazo;
	}
	/**
	 * @param numeroEmbarazo The numeroEmbarazo to set.
	 */
	public void setNumeroEmbarazo(int numeroEmbarazo)
	{
		this.numeroEmbarazo = numeroEmbarazo;
	}
/**
 * @return Returns the edadGestacional.
 */
public int getEdadGestacional()
{
	return edadGestacional;
}
/**
 * @param edadGestacional The edadGestacional to set.
 */
public void setEdadGestacional(int edadGestacional)
{
	this.edadGestacional = edadGestacional;
}
/**
 * @return Returns the edadParto.
 */
public int getEdadParto()
{
	return edadParto;
}
/**
 * @param edadParto The edadParto to set.
 */
public void setEdadParto(int edadParto)
{
	this.edadParto = edadParto;
}
	/**
	 * @return Returns the egFinalizar.
	 */
	public int getEgFinalizar()
	{
		return egFinalizar;
	}
	/**
	 * @param egFinalizar The egFinalizar to set.
	 */
	public void setEgFinalizar(int egFinalizar)
	{
		this.egFinalizar = egFinalizar;
	}
	/**
	 * @return Returns the fechaGrabacion.
	 */
	public String getFechaGrabacion()
	{
		return fechaGrabacion;
	}
	/**
	 * @param fechaGrabacion The fechaGrabacion to set.
	 */
	public void setFechaGrabacion(String fechaGrabacion)
	{
		this.fechaGrabacion = fechaGrabacion;
	}
	/**
	 * @return Returns the fechaOrden.
	 */
	public String getFechaOrden()
	{
		return fechaOrden;
	}
	/**
	 * @param fechaOrden The fechaOrden to set.
	 */
	public void setFechaOrden(String fechaOrden)
	{
		this.fechaOrden = fechaOrden;
	}
/**
 * @return Returns the fechaTerminacion.
 */
public String getFechaTerminacion()
{
	return fechaTerminacion;
}
/**
 * @param fechaTerminacion The fechaTerminacion to set.
 */
public void setFechaTerminacion(String fechaTerminacion)
{
	this.fechaTerminacion = fechaTerminacion;
}
/**
 * @return Returns the fechaUltrasonido.
 */
public String getFechaUltrasonido()
{
	return fechaUltrasonido;
}
/**
 * @param fechaUltrasonido The fechaUltrasonido to set.
 */
public void setFechaUltrasonido(String fechaUltrasonido)
{
	this.fechaUltrasonido = fechaUltrasonido;
}
/**
 * @return Returns the finalizacionEmbarazo.
 */
public boolean isFinalizacionEmbarazo()
{
	return finalizacionEmbarazo;
}
/**
 * @param finalizacionEmbarazo The finalizacionEmbarazo to set.
 */
public void setFinalizacionEmbarazo(boolean finalizacionEmbarazo)
{
	this.finalizacionEmbarazo = finalizacionEmbarazo;
}
/**
 * @return Returns the fpp.
 */
public String getFpp()
{
	return fpp;
}
/**
 * @param fpp The fpp to set.
 */
public void setFpp(String fpp)
{
	this.fpp = fpp;
}
/**
 * @return Returns the fppUltrasonido.
 */
public String getFppUltrasonido()
{
	return fppUltrasonido;
}
/**
 * @param fppUltrasonido The fppUltrasonido to set.
 */
public void setFppUltrasonido(String fppUltrasonido)
{
	this.fppUltrasonido = fppUltrasonido;
}
/**
 * @return Returns the fur.
 */
public String getFur()
{
	return fur;
}
/**
 * @param fur The fur to set.
 */
public void setFur(String fur)
{
	this.fur = fur;
}
	/**
	 * @return Returns the horaGrabacion.
	 */
	public String getHoraGrabacion()
	{
		return horaGrabacion;
	}
	/**
	 * @param horaGrabacion The horaGrabacion to set.
	 */
	public void setHoraGrabacion(String horaGrabacion)
	{
		this.horaGrabacion = horaGrabacion;
	}
	/**
	 * @return Returns the motivoFinalizacion.
	 */
	public int getMotivoFinalizacion()
	{
		return motivoFinalizacion;
	}
	/**
	 * @param motivoFinalizacion The motivoFinalizacion to set.
	 */
	public void setMotivoFinalizacion(int motivoFinalizacion)
	{
		this.motivoFinalizacion = motivoFinalizacion;
	}
	/**
	 * @return Returns the otroMotivoFinalizacion.
	 */
	public String getOtroMotivoFinalizacion()
	{
		return otroMotivoFinalizacion;
	}
	/**
	 * @param otroMotivoFinalizacion The otroMotivoFinalizacion to set.
	 */
	public void setOtroMotivoFinalizacion(String otroMotivoFinalizacion)
	{
		this.otroMotivoFinalizacion = otroMotivoFinalizacion;
	}
	/**
	 * @return Returns the hojaObstetricaDao.
	 */
	public HojaObstetricaDao getHojaObstetricaDao()
	{
		return hojaObstetricaDao;
	}
	/**
	 * @param hojaObstetricaDao The hojaObstetricaDao to set.
	 */
	public void setHojaObstetricaDao(HojaObstetricaDao hojaObstetricaDao)
	{
		this.hojaObstetricaDao = hojaObstetricaDao;
	}
	/**
	 * @return Returns the observacionesGrales.
	 */
	public String getObservacionesGrales()
	{
		return observacionesGrales;
	}
	/**
	 * @param observacionesGrales The observacionesGrales to set.
	 */
	public void setObservacionesGrales(String observacionesGrales)
	{
		this.observacionesGrales = observacionesGrales;
	}
	
	/**
	 * @return Returns the edadGestacionalResumen.
	 */
	public int getEdadGestacionalResumen()
	{
		return edadGestacionalResumen;
	}
	/**
	 * @param edadGestacionalResumen The edadGestacionalResumen to set.
	 */
	public void setEdadGestacionalResumen(int edadGestacionalResumen)
	{
		this.edadGestacionalResumen = edadGestacionalResumen;
	}
	/**
	 * @return Returns the fechaGestacional.
	 */
	public String getFechaGestacional()
	{
		return fechaGestacional;
	}
	/**
	 * @param fechaGestacional The fechaGestacional to set.
	 */
	public void setFechaGestacional(String fechaGestacional)
	{
		this.fechaGestacional = fechaGestacional;
	}
	/**
	 * @return Returns the horaGestacional.
	 */
	public String getHoraGestacional()
	{
		return horaGestacional;
	}
	/**
	 * @param horaGestacional The horaGestacional to set.
	 */
	public void setHoraGestacional(String horaGestacional)
	{
		this.horaGestacional = horaGestacional;
	}
	
	/**
	 * @return Returns the edadGestacionalExamen.
	 */
	public int getEdadGestacionalExamen()
	{
		return edadGestacionalExamen;
	}
	/**
	 * @param edadGestacionalExamen The edadGestacionalExamen to set.
	 */
	public void setEdadGestacionalExamen(int edadGestacionalExamen)
	{
		this.edadGestacionalExamen = edadGestacionalExamen;
	}
			/**
			 * @return Returns the fechaExamen.
			 */
			public String getFechaExamen()
			{
				return fechaExamen;
			}
			/**
			 * @param fechaExamen The fechaExamen to set.
			 */
			public void setFechaExamen(String fechaExamen)
			{
				this.fechaExamen = fechaExamen;
			}
			/**
			 * @return Returns the horaExamen.
			 */
			public String getHoraExamen()
			{
				return horaExamen;
			}
			/**
			 * @param horaExamen The horaExamen to set.
			 */
			public void setHoraExamen(String horaExamen)
			{
				this.horaExamen = horaExamen;
			}
	/**
	 * @return Retorna apellido.
	 */
	public String getApellido() {
		return apellido;
	}
	/**
	 * @param Asigna apellido.
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	/**
	 * @return Retorna edad.
	 */
	public int getEdad() {
		return edad;
	}
	/**
	 * @param Asigna edad.
	 */
	public void setEdad(int edad) {
		this.edad = edad;
	}
	/**
	 * @return Retorna id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param Asigna id.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Retorna listado.
	 */
	public Collection getListado() {
		return listado;
	}
	/**
	 * @param Asigna listado.
	 */
	public void setListado(Collection listado) {
		this.listado = listado;
	}
	/**
	 * @return Retorna nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param Asigna nombre.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return Retorna nombreMedico.
	 */
	public String getNombreMedico() {
		return nombreMedico;
	}
	/**
	 * @param Asigna nombreMedico.
	 */
	public void setNombreMedico(String nombreMedico) {
		this.nombreMedico = nombreMedico;
	}
	
	/**
	 * @return Retorna nombreMotivoFinalizacion.
	 */
	public String getNombreMotivoFinalizacion() {
		return nombreMotivoFinalizacion;
	}
	/**
	 * @param Asigna nombreMotivoFinalizacion.
	 */
	public void setNombreMotivoFinalizacion(String nombreMotivoFinalizacion) {
		this.nombreMotivoFinalizacion = nombreMotivoFinalizacion;
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
	 * @return Retorna tiempoRupturaMemebranas.
	 */
	public String getTiempoRupturaMemebranas()
	{
		return tiempoRupturaMemebranas;
	}
	/**
	 * @param tiempoRupturaMemebranas Asigna tiempoRupturaMemebranas.
	 */
	public void setTiempoRupturaMemebranas(String tiempoRupturaMemebranas)
	{
		this.tiempoRupturaMemebranas = tiempoRupturaMemebranas;
	}
	
		/**
		 * @return Returns the ultrasonidoFecha.
		 */
		public String getUltrasonidoFecha()
		{
			return ultrasonidoFecha;
		}
		/**
		 * @param ultrasonidoFecha The ultrasonidoFecha to set.
		 */
		public void setUltrasonidoFecha(String ultrasonidoFecha)
		{
			this.ultrasonidoFecha = ultrasonidoFecha;
		}
		/**
		 * @return Returns the ultrasonidoHora.
		 */
		public String getUltrasonidoHora()
		{
			return ultrasonidoHora;
		}
		/**
		 * @param ultrasonidoHora The ultrasonidoHora to set.
		 */
		public void setUltrasonidoHora(String ultrasonidoHora)
		{
			this.ultrasonidoHora = ultrasonidoHora;
		}
	/**
	 * @return Returns the furTemp.
	 */
	public String getFurTemp()
	{
		return furTemp;
	}
	/**
	 * @param furTemp The furTemp to set.
	 */
	public void setFurTemp(String furTemp)
	{
		this.furTemp = furTemp;
	}
	/**
	 * Método para insertar una hoja obstétrica
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente  => int
	 * @param login  => String
	 * @param institucion  => int
	 * @param datosMedico  => String
	 * @return codigoHojaObstétrica
	 * @throws SQLException
	 */
	
	public int insertarHojaObstetrica (Connection con, int codigoPaciente, String login, 
																	int institucion, 	String datosMedico) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao - insertarHojaObstetrica )");
		}
		
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		resp1=hojaObstetricaDao.insertarHojaObstetrica (	con, codigoPaciente, this.numeroEmbarazo, login, institucion, datosMedico, 
															this.fechaOrden, this.fur, this.fpp, this.fechaUltrasonido, this.fppUltrasonido, 
															this.edadGestacional, this.edadParto, this.finalizacionEmbarazo, this.observacionesGrales,
															this.confiable,this.vigenteAntitetanica,this.dosisAntitetanica,this.mesesGestacionAntitetanica,this.antirubeola,
															this.fechaTerminacion, this.nombreMotivoFinalizacion, this.egFinalizar,Integer.parseInt(this.peso),Integer.parseInt(this.talla),this.embarazoDeseado,this.embarazoPlaneado);
		
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		
		/*if(!UtilidadValidacion.existeAntecedenteGinecoHisto(con, codigoPaciente))
		{
			insertarAntecedenteGinecoHisto(con, codigoPaciente);
		}*/
		
		if(!this.fur.equals(this.furTemp))
		{
			insertarAntecedenteGinecoHisto(con, codigoPaciente);
		}
		
		if(this.finalizacionEmbarazo)
		{
			modificarAntecedenteGinecoEmbarazo(con, codigoPaciente);
		}
		return resp1;
	}
	
	/**
	 * Método para insertar el resumen gestacional de la hoja obstétrica
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoHojaObstetrica  => int
	  * @param datosMedico  => String
	 * @return codigoResumenGestacional
	 * @throws SQLException
	 */
	public int insertarResumenGestacional (Connection con, int codigoHojaObstetrica, String datosMedico) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao - insertarResumenGestacional )");
		}
		
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
				
			resp1=hojaObstetricaDao.insertarResumenGestacional (	con, codigoHojaObstetrica, this.edadGestacionalResumen, this.fechaGestacional, this.horaGestacional, datosMedico);
	
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}
	
	/**
	 * Método para insertar detalle del resumen gestacional
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoResumenGestacional => int
	 * @param codigoTipoResumenGest  => int
	 * @param valorTipoResumenGest  => String
	  *@return codigoDetalleResumenGest
	 * @throws SQLException
	 */
	public int insertarDetalleResumenGestacional (Connection con, int codigoResumenGestacional, int codigoTipoResumenGest, String valorTipoResumenGest) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao -insertarDetalleResumenGestacional )");
		}
		
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		resp1=hojaObstetricaDao.insertarDetalleResumenGestacional (	con, codigoResumenGestacional, codigoTipoResumenGest, valorTipoResumenGest);
	
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}
	
	/**
	 * Método para insertar el histórico del examen de laboratorio
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoHojaObstetrica  => int
	  * @param datosMedico  => String
	 * @return codigoHistoricoExamenLab
	 * @throws SQLException
	 */
	public int insertarHistoricoExamenLab (Connection con, int codigoHojaObstetrica, String datosMedico) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao -insertarHistoricoExamenLab )");
		}
		
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		resp1=hojaObstetricaDao.insertarHistoricoExamenLab (	con, codigoHojaObstetrica, datosMedico, this.edadGestacionalExamen, this.fechaExamen, this.horaExamen);
	
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}
		
	/**
	 * Método para insertar el detalle del exámen de laboratorio
	 * @param con
	 * @param codigoHistoricoExamenLab => int
	 * @param tipoExamen => int
	 * @param resultadoExamenLab => String
	 * @param observacionExamenLab => String
	 * @return 
	 * @throws SQLException

	 */
	
	public int insertarDetalleExamenLab(Connection con, int codigoHistoricoExamenLab, int tipoExamen, String resultadoExamenLab, String observacionExamenLab) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao -insertarDetalleExamenLab )");
		}
		
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		resp1=hojaObstetricaDao.insertarDetalleExamenLab (con, codigoHistoricoExamenLab, tipoExamen, resultadoExamenLab, observacionExamenLab);
	
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
		
	}
	
	/**
	 * Método para insertar el documento adjunto de un exámen de laboratorio
	 * @param con
	 * @param codigoDetalleExamenLab => int
	 * @param nombreOriginal => String
	 * @param nombreArchivo => String
	 * @return 
	 * @throws SQLException
	 */
	
	public int insertarAdjuntoExamenLab(Connection con, int codigoDetalleExamenLab, String nombreOriginal, String nombreArchivo) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao -insertarAdjuntoExamenLab )");
		}

		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		resp1=hojaObstetricaDao.insertarAdjuntoExamenLab (con, codigoDetalleExamenLab, nombreOriginal, nombreArchivo);
	
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
		
	}
	
	/**
	 * Método para insertar el histórico del ultrasonido
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoHojaObstetrica  => int
	  * @param datosMedico  => String
	 * @return codigoHistoricoUltrasonido
	 * @throws SQLException
	 */
	public int insertarHistoricoUltrasonido (Connection con, int codigoHojaObstetrica, String datosMedico) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao - insertarHistoricoUltrasonido )");
		}
		
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
				
			resp1=hojaObstetricaDao.insertarHistoricoUltrasonido (	con, codigoHojaObstetrica, this.ultrasonidoFecha, this.ultrasonidoHora, datosMedico);
	
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}
	
	/**
	 * Método para insertar el detalle del ultrasonidol
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoHistoUltrasonido => int
	 * @param codigoTipoUltrasonido  => int
	 * @param valorTipoUltrasonido  => String
	  *@return 1 si logró insertar sino retorna -1
	 * @throws SQLException
	 */
	public int insertarDetalleUltrasonido (Connection con, int codigoHistoUltrasonido, int codigoTipoUltrasonido, String valorTipoUltrasonido) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao -insertarDetalleUltrasonido )");
		}
		
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		resp1=hojaObstetricaDao.insertarDetalleUltrasonido (	con, codigoHistoUltrasonido, codigoTipoUltrasonido, valorTipoUltrasonido);
	
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}
	
	/**
	 * Método para insertar el documento adjunto a un ultrasonido
	 * @param con
	 * @param codigo_histo_ultrasonido => int
	 * @param nombreOriginal => String
	 * @param nombreArchivo => String
	 * @return 
	 * @throws SQLException
	 */
	
	public int insertarAdjuntoUltrasonido (Connection con, int codigo_histo_ultrasonido, String nombreOriginal, String nombreArchivo) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao -insertarAdjuntoUltrasonido )");
		}

		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		resp1=hojaObstetricaDao.insertarAdjuntoUltrasonido (con, codigo_histo_ultrasonido, nombreOriginal, nombreArchivo);
	
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
		
	}
	
	/**
	 * Funcion que retorna el resultado de una busqueda avanzada sobre mujeres embarazadas
	 * @param con
	 * @return
	 */
		public Collection resultadoBusquedaAvanzada(Connection con)
		{
			HojaObstetricaDao hojaObtetricaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaObstetricaDao();
			Collection coleccion=null;
			try
			{	
   			 coleccion = hojaObtetricaDao.busquedaAbanzada(con,this.apellido,this.nombre,this.id,this.edad, this.fpp, this.edadGestacional, this.nombreMedico);
			}
			catch(Exception e)
			{
			  logger.warn("Error mundo Documento " +e.toString());
			  coleccion=null;
			}
			return coleccion;		
		}
	/**
	 * Funcion para cargar el listado de mujeres embarazadas
	 * @param con
	 * @return
	 */
	public Collection cargarListado(Connection con)
	{
		HojaObstetricaDao hojaObstetricaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaObstetricaDao();
		return hojaObstetricaDao.cargarListado(con);
	}

	/**
	 * Ingresar Embarazo
	 * @param con
	 * @param numeroEmbarazo
	 * @param fur
	 * @param fpp
	 * @param edadGestacional
	 * @param codigoPaciente
	 * @param usuario
	 * @param duracion
	 * @param tiempoRupturaMemebranas
	 * @param legrado
	 * @param insertarEmbarazo
	 */
	public void ingresarEmbarazo(Connection con, int numeroEmbarazo, String fur, String fpp, String edadGestacional, int codigoPaciente, UsuarioBasico usuario, boolean insertarHoja, String duracion, String tiempoRupturaMemebranas, String legrado)
	{
		this.fur=fur;
		this.fpp=fpp;
		if(edadGestacional.equals(""))
		{
			this.edadGestacional=0;
		}
		else
		{
			this.edadGestacional=Integer.parseInt(edadGestacional);
		}
		this.numeroEmbarazo=numeroEmbarazo;
		hojaObstetricaDao.ingresarEmbarazo(con, this.fur, this.fpp, edadGestacional, codigoPaciente, usuario, this.numeroEmbarazo, insertarHoja, duracion, tiempoRupturaMemebranas, legrado);
	}

	/**
	 * Ingresar Antecedente gineco-obstetrico y embarazo
	 * @param con
	 * @param fur
	 * @param fpp
	 * @param edadGestacional
	 * @param usuario
	 * @param codigoPaciente
	 * @param insertarHoja
	 * @param duracion
	 * @param tiempoRupturaMemebranas
	 * @param legrado
	 */
	public void ingresarEmbarazoTotal(Connection con, String fur, String fpp, String edadGestacional, UsuarioBasico usuario, int codigoPaciente, boolean insertarHoja, String duracion, String tiempoRupturaMemebranas, String legrado)
	{
		DaoFactory myDaoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			myDaoFactory.beginTransaction(con);
			if(!UtilidadValidacion.existeAntecedenteGinecoObstetrico(con, codigoPaciente))
			{
				ingresarAntecedente(con, codigoPaciente, usuario);
			}
			ingresarEmbarazo(con, numeroEmbarazo, fur, fpp, edadGestacional, codigoPaciente, usuario, insertarHoja, duracion, tiempoRupturaMemebranas, legrado);
			myDaoFactory.endTransaction(con);
		}
		catch (SQLException e)
		{
			logger.error("Error en el manejo de las transacciones : "+e);
		}
	}
	
	/**
	 * Método para obtener los campos del la hoja obstétrica
	 * que generan log
	 * @param con
	 * @param codigoHojaObstetrica
	 * @return boolean
	 */
	public boolean cargarDatosLog(Connection con, int codigoHojaObstetrica)
	{
	  try
		{
			ResultSetDecorator rs=hojaObstetricaDao.cargarDatosLog(con,codigoHojaObstetrica);
			
			if (rs.next())
			{
				this.fur=UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fur"));
				this.fpp=UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fpp"));
				this.fppUltrasonido=UtilidadFecha.conversionFormatoFechaAAp(fppUltrasonido=rs.getString("fppUltrasonido"));
				this.edadParto=rs.getInt("edadParto");
				this.finalizacionEmbarazo=rs.getBoolean("finEmbarazo");
				this.edadGestacional=rs.getInt("edadGestacional");
								
				return true;
			}
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDatosLog de la Hoja Obstétrica: "+e);
			return false;
		}
	}

	/**
	 * Funcion para consultar  
	 * @param embarazo
	 */
	public boolean consultarEmbarazo(Connection con, int numeroEmbarazo, int codigoPaciente)
	{
		String aux;
		Collection coleccion = hojaObstetricaDao.consultarEmbarazo(con,numeroEmbarazo,codigoPaciente);
		
		Iterator ite=coleccion.iterator();
		if(ite.hasNext())
		{
			HashMap col=(HashMap) ite.next();
			
			//---Verificar que se no devuelva nulos 
			this.numeroEmbarazo = Utilidades.convertirAEntero( (col.get("numero_embarazo")+"").equals("null") ? "0" : col.get("numero_embarazo")+"");
			this.fechaOrden = (col.get("fecha_orden")+"").equals("null") ? "" : UtilidadFecha.conversionFormatoFechaAAp((col.get("fecha_orden")+"")) ;	
			this.fur = (col.get("fur")+"").equals("null") ? "" : UtilidadFecha.conversionFormatoFechaAAp((col.get("fur")+""));
			this.fpp = (col.get("fpp")+"").equals("null") ? "" : UtilidadFecha.conversionFormatoFechaAAp((col.get("fpp")+""));
			this.fechaUltrasonido = (col.get("fecha_ultrasonido")+"").equals("null") ? "" : UtilidadFecha.conversionFormatoFechaAAp((col.get("fecha_ultrasonido")+""));
			this.fppUltrasonido = (col.get("fpp_ultrasonido")+"").equals("null") ? "" : UtilidadFecha.conversionFormatoFechaAAp((col.get("fpp_ultrasonido")+""));			
			this.edadGestacional = Utilidades.convertirAEntero( (col.get("edad_gestacional")+"").equals("null") ? "0" : col.get("edad_gestacional")+"");
			this.edadParto = Utilidades.convertirAEntero(  (col.get("edad_parto")+"").equals("null") ? "0" : col.get("edad_parto")+"" );
			if(numeroEmbarazo!=-1)
				{
					this.finalizacionEmbarazo = UtilidadTexto.getBoolean( col.get("fin_embarazo")+"" );    
					this.fechaTerminacion = (col.get("fecha_terminacion")+"").equals("null") ? "" : UtilidadFecha.conversionFormatoFechaAAp((col.get("fecha_terminacion")+"")) ; 
					this.nombreMotivoFinalizacion = (col.get("motivo_finalizacion")+"").equals("null") ? "" : (col.get("motivo_finalizacion")+"");
					this.otroMotivoFinalizacion = (col.get("otro_motivo_finalizacion")+"").equals("null") ? "" : col.get("otro_motivo_finalizacion")+"";
					aux = ((col.get("eg_finalizar")+"").equals("null") ? "0" : (col.get("eg_finalizar")+"")).trim();
					this.egFinalizar = Utilidades.convertirAEntero(aux);
				}
			this.observacionesGrales = (col.get("observaciones_grales")+"").equals("null") ? "" : col.get("observaciones_grales")+"";
			this.finalizacionEmbarazo=UtilidadTexto.getBoolean((col.get("fin_embarazo")+"").equals("null") ? "false" :col.get("fin_embarazo")+"");
			this.confiable=col.get("confiable")+"";
			this.vigenteAntitetanica=col.get("vigente_antitetanica")+"";
			this.dosisAntitetanica=col.get("dosis_antitetanica")+"";
			this.mesesGestacionAntitetanica=!UtilidadCadena.noEsVacio(col.get("mes_gestacion_antitetanica")+"") ? "" :  col.get("mes_gestacion_antitetanica")+"";
			this.antirubeola=col.get("antirubeola")+"";
			this.peso = col.get("peso").toString();
			this.talla = col.get("talla").toString();
			this.embarazoDeseado = col.get("embarazo_deseado").toString();
			this.embarazoPlaneado = col.get("embarazo_planeado").toString();
			return true;
		}
		else
		{
		 return false;	
		}
	}

	/**
	 * @param con
	 * @param codigoPaciente
	 * @param usuario
	 */
	public static int ingresarAntecedente(Connection con, int codigoPaciente, UsuarioBasico usuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaObstetricaDao().ingresarAntecedente(con, codigoPaciente, usuario);
	}
	
	/**
	 * Modifica el detalle embarazo gineco-obstétrico, para ingresar los valores correpondientes
	 * a fecha terminación, motivo finalización y eg finalizar  
	 * @param con una conexion abierta con una fuente de datos
	 * @return 1 si logro modificar sino retorna 0
	 */
	public int modificarAntecedenteGinecoEmbarazo (Connection con, int paciente) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao - modificarAntecedenteGinecoEmbarazo )");
		}
		
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
						
		resp1=hojaObstetricaDao.modificarAntecedenteGinecoEmbarazo (con, this.numeroEmbarazo, paciente, this.fechaTerminacion, this.motivoFinalizacion, this.otroMotivoFinalizacion, this.egFinalizar, this.duracion, this.tiempoRupturaMemebranas, this.legrado);
			
		if (!inicioTrans || resp1==0  )
		{
		    myFactory.abortTransaction(con);
			return 0;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}
	
	/**
	 * Método para insertar el historico de antecedentes gineco-obstétrico con la fur
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente  => int
	  * @return 
	 * @throws SQLException
	 */
	public int insertarAntecedenteGinecoHisto (Connection con, int codigoPaciente) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao - insertarAntecedenteGinecoHisto )");
		}
		
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
				
		if(!this.fur.equals(""))
			resp1=hojaObstetricaDao.insertarAntecedenteGinecoHisto (	con, codigoPaciente, this.fur);
		else
			resp1=-1;
	
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}
	
	/**
	 * Funcion para Actualizar las Observaciones generales de un Embarazo finalizado
	 * @param con
	 * @param numeroEmbarazo
	 * @param codigoPaciente
	 * @param observaciones_grales
	 * @return
	 * @throws SQLException
	 */

	public int actualizarEmbarazo (Connection con, int numeroEmbarazo, int codigoPaciente, String observaciones_grales) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao ---> modificar Hoja Obstetrica )");
		}
		
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
						
		resp1=hojaObstetricaDao.actualizarEmbarazo(con, numeroEmbarazo, codigoPaciente, observaciones_grales);
			
		if (!inicioTrans || resp1==0  )
		{
		    myFactory.abortTransaction(con);
			return 0;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;  //-retorna el numero de registros actualizados+ 
	}
	
	/**
	 * Método para consultar el historico de los examenes de laboratorio
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @return Collection con los examenes de laboratorio realizados al embarazo pasado por parámetro
	 */
	public Collection consultarHistoricoExamenesLab(Connection con, int codigoPaciente, int codigoEmbarazo)
	{
		return hojaObstetricaDao.consultarHistoricoExamenesLab(con, codigoPaciente, codigoEmbarazo);
	}
	
	/**
	 * Método para consultar los dos últimos historicos de los examenes de laboratorio
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @return Collection con los dos últimos históricos de los examenes de laboratorio realizados al embarazo pasado por parámetro
	 */
	public Collection consultarUltimosHistoricosExamenesLab(Connection con, int codigoPaciente, int codigoEmbarazo)
	{
		return hojaObstetricaDao.consultarUltimosHistoricosExamenesLab(con, codigoPaciente, codigoEmbarazo);
	}
	
	/**
	 * Método para consultar el resumen gestacional de una hoja obstétrica
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @param orden -> true si es ascendente o false si es descendente
	 * @return Collection con el valor, fecha, hora, edadgestacional del resumen gestacional
	 */
	public Collection consultarResumenGestacional(Connection con, int codigoPaciente, int codigoEmbarazo, boolean orden)
		{
		 return hojaObstetricaDao.consultarResumenGestacional(con, codigoPaciente, codigoEmbarazo, orden);
		}
	
	/**
	 * Método para consultar los tipos de resumen gestacional parametrizados a la institucion
	 * @param con
	 * @param institucion
	 * @return Collection con codigo y nombre del resumen gestacional
	 */
	public Collection consultarTiposResumenGestacional (Connection con, int institucion)
	{
		return hojaObstetricaDao.consultarTiposResumenGestacional(con, institucion);
	}

	/**
	 * Método para consultar los exámenes de laboratorio de una hoja obstétrica
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @param orden -> boolean true si ordenamiento ascendente o false si es descendente
	 * @param consulta -> Identica la consulta a ejecutar 1=Consulta con todos los históricos
	 * 					2= Histórico de los exámenes parametrizados,  3=Histórico nuevos exámenes de laboratorio
	
	 * @return Collection con el resultado, observación, fecha, hora, edadgestacional de los exámenes de laboratorio
	 */
	public Collection consultarExamenesLaboratorio(Connection con, int codigoPaciente, int codigoEmbarazo, boolean orden, int consulta)
	{
		return hojaObstetricaDao.consultarExamenesLaboratorio(con, codigoPaciente, codigoEmbarazo, orden, consulta);
	}

	/**
	 * Método para consultar los tipos de exámenes de laboratorio
	 * @param con
	 * @param institucion
	 * @return Collection con codigo y nombre del exámen de laboratorio
	 */
	public Collection consultarTiposExamenLaboratorio (Connection con, int institucion)
	{
		return hojaObstetricaDao.consultarTiposExamenLaboratorio(con,institucion);
	}
	
	/**
	 * Método para consultar los tipos de ultrasonido parametrizados por institución
	 * @param con
	 * @param institucion
	 * @return Collection con codigo y nombre del tipo de ultrasonido
	 */
	public Collection consultarTiposUltrasonido (Connection con, int institucion)
	{
		return hojaObstetricaDao.consultarTiposUltrasonido(con, institucion);
	}
	
	/**
	 * Método para consultar el histórico de los ultrasonidos
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @param orden -> boolean true si ordenamiento ascendente o false si es descendente
	 * @return Collection con el tipo_ultrasonido, valor, fecha, hora, codigo_histo_ultrasonido 
	 */
	public Collection consultarHistoricoUltrasonidos(Connection con, int codigoPaciente, int codigoEmbarazo, boolean orden)
	{
		return hojaObstetricaDao.consultarHistoricoUltrasonidos(con, codigoPaciente, codigoEmbarazo, orden);
	}

	/**
	 * Método para actualizar la hoja Obstetrica desdfe la valoración
	 * @param con
	 * @param fur
	 * @param fpp
	 * @param edadGestacional
	 * @param usuario
	 * @param codigoPersona
	 */
	public void actualizarDatosValoracion(Connection con, String fur, String fpp, String edadGestacional, int codigoPersona)
	{
		hojaObstetricaDao.actualizarDatosValoracion(con, fur, fpp, edadGestacional, codigoPersona);
	}
	
	/**
	 * Método para insertar el detalle otro exámen de laboratorio
	 * @param con
	 * @param codigoHistoricoExamenLab => int
	 * @param descripcionOtro => String
	 * @param resultadoExamenLab => String
	 * @param observacionExamenLab => String
	 * @return 
	 * @throws SQLException

	 */
	
	public int insertarDetalleOtroExamenLab(Connection con, int codigoHistoricoExamenLab, String descripcionOtro, String resultadoExamenLab, String observacionExamenLab) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao -insertarDetalleOtroExamenLab )");
		}
		
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		resp1=hojaObstetricaDao.insertarDetalleOtroExamenLab (con, codigoHistoricoExamenLab, descripcionOtro, resultadoExamenLab, observacionExamenLab);
	
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
		
	}
	
	/**
	 * Método para insertar el documento adjunto de otro exámen de laboratorio
	 * @param con
	 * @param codigoDetalleOtroExamenLab => int
	 * @param nombreOriginal => String
	 * @param nombreArchivo => String
	 * @return 
	 * @throws SQLException
	 */
	
	public int insertarAdjuntoOtroExamenLab(Connection con, int codigoDetalleOtroExamenLab, String nombreOriginal, String nombreArchivo) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao -insertarAdjuntoOtroExamenLab )");
		}

		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		resp1=hojaObstetricaDao.insertarAdjuntoOtroExamenLab (con, codigoDetalleOtroExamenLab, nombreOriginal, nombreArchivo);
	
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
		
	}
	
	/**
	 * Método que consulta los tipos de plan manejo parametrizados para la institución, y también los
	 * otros planes de menejo de tipo medicamento que hayan sido ingresados por la opción ingresar otro
	 * @param con
	 * @param institucion
	 * @param codigoPersona
	 * @return
	 */
	public HashMap consultarTiposPlanManejoInstitucion(Connection con, int institucion, int codigoPaciente) 
		{
		return hojaObstetricaDao.consultarTiposPlanManejoInstitucion(con, institucion, codigoPaciente, this.numeroEmbarazo);
		}
	
	/**
	 * Metodo que consulta el historico de plan manejo tanto de los tipos parametrizados
	 * por institucion como los otros nuevos ingresados
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public HashMap consultaHistoricoPlanManejo(Connection con, int codigoPaciente) 
		{
		return hojaObstetricaDao.consultaHistoricoPlanManejo(con, codigoPaciente, this.numeroEmbarazo);
		}
	
	/**
	 * Metodo que inserta los datos ingresados en la sección plan de manejo
	 * @param con
	 * @param codigoHojaObstetrica
	 * @param datosMedico
	 * @param mapaTiposPlanManejo
	 */
	public boolean insertarPlanManejo(Connection con, int codigoHojaObstetrica, String datosMedico, HashMap mapaTiposPlanManejo) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		boolean error=false;
		int resp=0;
		int numRegTiposPlanManejo=0;
		//Variable para indicar que se ha insertado el historico de plan de manejo, y solo lo haga una vez
		boolean histoPlanManejoInsertado=false;
		int codHistoPlanManejo=0;
				
		if (hojaObstetricaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaObstetricaDao - insertarPlanManejo )");
		}
		
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if(UtilidadCadena.noEsVacio(mapaTiposPlanManejo.get("numRegistros")+""))
		{
		numRegTiposPlanManejo=Integer.parseInt(mapaTiposPlanManejo.get("numRegistros")+"");
		}
		
		//Se verifica si hay tipos de plan manejo
		if(numRegTiposPlanManejo>0)
			{
			for(int i=0; i<numRegTiposPlanManejo; i++)
				{
					
				//Codigo del tipo de plan de manejo por institucion o del otro que tiene la misma secuencia
				int codTipoPlanManejoIns=Integer.parseInt(mapaTiposPlanManejo.get("codigo_"+i)+"");
				
				//Valor del radio para saber si presenta o no
				String presenta=this.mapaPlanManejo.get("presenta_"+codTipoPlanManejoIns)+"";
				
				//Observaciones del tipo de plan de manejo
				String observacion=this.mapaPlanManejo.get("observaciones_"+codTipoPlanManejoIns)+"";
				
				//-Verificar que se haya ingresado alguna información en el tipo de plan de manejo
				if (UtilidadCadena.noEsVacio(presenta) || (UtilidadCadena.noEsVacio(observacion)) )
					{
						//Se verifica si ya fué insertado el historico de plan de manejo
						if(!histoPlanManejoInsertado)
							{
							codHistoPlanManejo=hojaObstetricaDao.insertarHistoricoPlanManejo(con, codigoHojaObstetrica, datosMedico);
							histoPlanManejoInsertado=true;
							}
					
						
						if(codHistoPlanManejo>0)
							{
								//Si es otro es igual a 0 es un tipo de plan de manejo parametrizado
								if((mapaTiposPlanManejo.get("es_otro_"+i)+"").equals("0"))
								{
									//Se inserta el detalle del tipo de plan manejo parametrizado
									resp=hojaObstetricaDao.insertarDetallePlanManejo(con, codHistoPlanManejo, codTipoPlanManejoIns, presenta, observacion, false);
								}
								else
								{
									//Se inserta el detalle del otro tipo de plan manejo 
									resp=hojaObstetricaDao.insertarDetallePlanManejo(con, codHistoPlanManejo, codTipoPlanManejoIns, presenta, observacion, true);	
								}
							}
						
						if (resp < 1)
						{
							error=true;
							break;
						}
					}//if noEsVacio presenta ni descripcion	
				}//for numRegTiposPlanManejo
			
			//Se inserta la información de los otros planes de manejo ingresados
			int nroOtros=Utilidades.convertirAEntero(this.mapaPlanManejo.get("numeroOtros")+"",true);
			int codOtroPlanManejo=0;
			String descripcionOtro="", presentaOtro="", observacionOtro="";
			
			for(int j=0; j<nroOtros; j++)
				{
				//-----Descripción del nuevo tipo de plan de manejo -------//
				descripcionOtro=this.mapaPlanManejo.get("descripcionOtro_"+j)+"";
				
				//----- Presenta del nuevo tipo de plan de manejo ----------//
				presentaOtro=this.mapaPlanManejo.get("presentaOtro_"+j)+"";
				
				//----- Observacion del nuevo tipo de plan de manejo ----------//
				observacionOtro=this.mapaPlanManejo.get("observacionesOtro_"+j)+"";
				
				if(UtilidadCadena.noEsVacio(presentaOtro) || UtilidadCadena.noEsVacio(observacionOtro))
					{
					//---Se inserta el nuevo tipo de plan de manejo---//
					codOtroPlanManejo=hojaObstetricaDao.insertarOtroPlanManejo(con, descripcionOtro);
					
					if(codOtroPlanManejo>0)
						{
						//Si no se ha insertado histórico entonces se inserta
						if(codHistoPlanManejo<=0)
							codHistoPlanManejo=hojaObstetricaDao.insertarHistoricoPlanManejo(con, codigoHojaObstetrica, datosMedico);
						//Se inserta el detalle del nuevo tipo de plan de manejo
						resp=hojaObstetricaDao.insertarDetallePlanManejo(con, codHistoPlanManejo, codOtroPlanManejo, presentaOtro, observacionOtro, true);
						}
					else
						{
							error=true;
							break;	
						}
					}//if noEsVacio
				}//for nroOtros
				
			}//if numRegTiposPlanManejo
		
		
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error)
		{
			myFactory.abortTransaction(con);
			return false;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		
		return true;
	}

	public String getConfiable() {
		return confiable;
	}

	public void setConfiable(String confiable) {
		this.confiable = confiable;
	}

	public String getAntirubeola() {
		return antirubeola;
	}

	public void setAntirubeola(String antirubeola) {
		this.antirubeola = antirubeola;
	}

	public String getDosisAntitetanica() {
		return dosisAntitetanica;
	}

	public void setDosisAntitetanica(String dosisAntitetanica) {
		this.dosisAntitetanica = dosisAntitetanica;
	}

	public String getMesesGestacionAntitetanica() {
		return mesesGestacionAntitetanica;
	}

	public void setMesesGestacionAntitetanica(String mesesGestacionAntitetanica) {
		this.mesesGestacionAntitetanica = mesesGestacionAntitetanica;
	}

	public String getVigenteAntitetanica() {
		return vigenteAntitetanica;
	}

	public void setVigenteAntitetanica(String vigenteAntitetanica) {
		this.vigenteAntitetanica = vigenteAntitetanica;
	}
	
	public HashMap getMapaPlanManejo() {
		return mapaPlanManejo;
	}

	public void setMapaPlanManejo(HashMap mapaPlanManejo) {
		this.mapaPlanManejo = mapaPlanManejo;
	}
	
	/**
	 * @return Retorna mapaPlanManejo.
	 */
	public Object getMapaPlanManejo(String key)
	{
		return mapaPlanManejo.get(key+"");
	}

	/**
	 * @param mapaPlanManejo Asigna mapaPlanManejo.
	 */
	public void setMapaPlanManejo(String key, String valor)
	{
		this.mapaPlanManejo.put(key,valor);
	}

	/**
	 * @return the embarazoDeseado
	 */
	public String getEmbarazoDeseado() {
		return embarazoDeseado;
	}

	/**
	 * @param embarazoDeseado the embarazoDeseado to set
	 */
	public void setEmbarazoDeseado(String embarazoDeseado) {
		this.embarazoDeseado = embarazoDeseado;
	}

	/**
	 * @return the embarazoPlaneado
	 */
	public String getEmbarazoPlaneado() {
		return embarazoPlaneado;
	}

	/**
	 * @param embarazoPlaneado the embarazoPlaneado to set
	 */
	public void setEmbarazoPlaneado(String embarazoPlaneado) {
		this.embarazoPlaneado = embarazoPlaneado;
	}

	/**
	 * @return the peso
	 */
	public String getPeso() {
		return peso;
	}

	/**
	 * @param peso the peso to set
	 */
	public void setPeso(String peso) {
		this.peso = peso;
	}

	/**
	 * @return the talla
	 */
	public String getTalla() {
		return talla;
	}

	/**
	 * @param talla the talla to set
	 */
	public void setTalla(String talla) {
		this.talla = talla;
	}

}
