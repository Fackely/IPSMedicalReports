/*
 * Creado en Sep 21, 2005
 */
package com.princetonsa.mundo.hojaOftalmologica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import java.util.HashMap;
import org.apache.log4j.Logger;

import util.UtilidadCadena;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.HojaOftalmologicaDao;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class HojaOftalmologica
{
	/**
	 * Para hacer logs de esta funcionalidad.
	*/
		private Logger logger = Logger.getLogger(HojaOftalmologica.class);
		
  //---------------------------------------------------------------SECCION DE ESTRABISMO----------------------------------------------------------------//
	/**
	 * Ppm
	 */
	private String ppm;
	
	/**
	 * Cover Test de Cerca sin corrección
	 */
	private String coverTestCercaSc;
	
	/**
	 * Cover Test de Cerca con corrección
	 */
	private String coverTestCercaCc;
	
	/**
	 * Cover Test de Lejos sin corrección
	 */
	private String coverTestLejosSc;
	
	/**
	 * Cover Test de Lejos con corrección
	 */
	private String coverTestLejosCc;
	
	/**
	 * Ojo fijador, si está en true es derecho, false es izquierdo, en null no se seleccionó
	 */
	private int ojoFijador;
	
	/**
	 * Ppc por institución
	 */
	private int ppcInstitucion;
	
	/**
	 * Prisma con corrección de lejos 
	 */
	private String prismaCcLejos;
	
	/**
	 *  Prisma sin corrección de lejos
	 */
	private String prismaScLejos;
	
	/**
	 * Ducciones y versiones 
	 */
	private String duccionesVersiones;
	
	/**
	 * Test de visión binocular 
	 */
	private String testVisionBinocular;
	
	/**
	 * Estereopsis 
	 */
	private String estereopsis;
	
	/**
	 * Amplitud de fusión cerca mas 
	 */
	private String amplitudFusionCercaMas;
	
	/**
	 * Amplitud de fusión cerca menos 
	 */
	private String amplitudFusionCercaMenos;
	
	/**
	 * Amplitud de fusión lejos mas 
	 */
	private String amplitudFusionLejosMas;
	
	/**
	 * Amplitud de fusión lejos menoes 
	 */
	private String amplitudFusionLejosMenos;
	
	/**
	 * Prisma compensador de lejos 
	 */
	private String prismaCompensadorLejos;
	
	/**
	 * Prisma compensador de cerca 
	 */
	private String prismaCompensadorCerca;
	
	/**
	 * Observaciones de la seccion Estrabismo
	 */
	private String observacionEstrabismo;
	
	 /**
	   * Campo que guarda los prismas de Cerca, segmento anterior, orbita y anexos
	   */
	  private HashMap mapa;
	  
	 /**
	  * Campo para guardar los datos del médico cuando se realiza la consulta de algún histórico de la
	  * sección Estrabismo
	  */
	  private String datosMedico;
	  
	  /**
	    * Fecha de grabación del histórico de la sección Estrabismo
	    */
		private String fechaEstrabismo;
	  
	//-----------------------------------------SECCION DE SEGMENTO ANTERIOR-----------------------------------------------------------------//
	/**
	 * Observaciones de la seccion segmento anterior
	 */
	  private String observacionSegmentoAnt;
      
      /**
       * Nombre imagen ojo derecho segmento anterior
       */
      private String imagenSegmentoAnteriorOD;

      /**
       * Nombre imagen ojo izquierdo segmento anterior
       */
      private String imagenSegmentoAnteriorOS;

//	-----------------------------------------SECCION DE RETINA Y VITREO-----------------------------------------------------------------//
	/**
	 * Observaciones de la seccion retina y vitreo
	 */
	  private String observacionRetinaVitreo;
		  
      /**
       * Nombre imagen ojo derecho retina
       */
      private String imagenRetinaOD;

      /**
       * Nombre imagen ojo izquierdo retina
       */
      private String imagenRetinaOS;

      /**
       * Nombre imagen ojo derecho vitreo
       */
      private String imagenVitreoOD;

      /**
       * Nombre imagen ojo izquierdo vitreo
       */
      private String imagenVitreoOS;
      //-----------------------------------------SECCION DE ORBITA Y ANEXOS-----------------------------------------------------------------//
	/**
	 * Observaciones de la seccion orbita y anexos
	 */
	  private String observacionOrbitaAnexos;
	  
	  //------------------------------------------------ Datos Valoracion -----------------------------------------------------------------------//
	  /**
	   * Utilizado mpara relacionar la valoracion oftalmológica con la hoja oftalmológica
	   */
	  private int numeroSolicitud;
	  
  //---------------------------------------------------Fin de declaración de atributos-----------------------------------------------------------//
	  
	  /**
		 * Interfaz para acceder a la fuente de datos
		 */
		private HojaOftalmologicaDao hojaOftalmologicaDao = null;
	  
	  /**
	   * Constructor de la clase, inicializa en vacío todos los atributos
	   */
	  public HojaOftalmologica ()
	  {
	  	reset();
	  	this.init(System.getProperty("TIPOBD"));
	  }
	  
		/**
		 * Este método inicializa los atributos de la clase con valores vacíos
		 */
	  public void reset()
	  {
	  	//Seccion de Estrabismo
	  	this.ppm = "";
	  	this.coverTestCercaSc = "";
	  	this.coverTestCercaCc = "";
	  	this.coverTestLejosSc = "";
	  	this.coverTestLejosCc = "";
	  	this.ojoFijador = -1;
	  	this.ppcInstitucion=0;
	  	this.prismaCcLejos = "";
	  	this.prismaScLejos = "";
	  	this.duccionesVersiones = "";
	  	this.testVisionBinocular = "";
	  	this.estereopsis = "";
	  	this.amplitudFusionCercaMas = "";
	  	this.amplitudFusionCercaMenos = "";
	  	this.amplitudFusionLejosMas = "";
	  	this.amplitudFusionLejosMenos = "";
	  	this.prismaCompensadorCerca = "";
	  	this.prismaCompensadorLejos = "";
	  	this.observacionEstrabismo = "";
	  	this.datosMedico="";
	  	this.fechaEstrabismo = "";
		this.mapa=new HashMap();

		//Secciòn segmento anterior
		this.observacionSegmentoAnt = "";
        this.imagenSegmentoAnteriorOD = "";
        this.imagenSegmentoAnteriorOS = "";
		
		//Sección retina y vitreo
		this.observacionRetinaVitreo = "";
        this.imagenRetinaOD="";
        this.imagenRetinaOS="";
        this.imagenVitreoOD="";
        this.imagenVitreoOS="";
		
		//Sección orbita y anexos
		this.observacionOrbitaAnexos = "";
	  		  	
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
				hojaOftalmologicaDao = myFactory.getHojaOftalmologicaDao();
				wasInited = (hojaOftalmologicaDao != null);
			}
			return wasInited;
		}
		
//-------------------------------------------------------------SETS Y GETS----------------------------------------------------------------//
	/**
	 * @return Returns the amplitudFusionCercaMas.
	 */
	public String getAmplitudFusionCercaMas()
	{
		return amplitudFusionCercaMas;
	}
	/**
	 * @param amplitudFusionCercaMas The amplitudFusionCercaMas to set.
	 */
	public void setAmplitudFusionCercaMas(String amplitudFusionCercaMas)
	{
		this.amplitudFusionCercaMas = amplitudFusionCercaMas;
	}
	/**
	 * @return Returns the amplitudFusionCercaMenos.
	 */
	public String getAmplitudFusionCercaMenos()
	{
		return amplitudFusionCercaMenos;
	}
	/**
	 * @param amplitudFusionCercaMenos The amplitudFusionCercaMenos to set.
	 */
	public void setAmplitudFusionCercaMenos(String amplitudFusionCercaMenos)
	{
		this.amplitudFusionCercaMenos = amplitudFusionCercaMenos;
	}
	/**
	 * @return Returns the amplitudFusionLejosMas.
	 */
	public String getAmplitudFusionLejosMas()
	{
		return amplitudFusionLejosMas;
	}
	/**
	 * @param amplitudFusionLejosMas The amplitudFusionLejosMas to set.
	 */
	public void setAmplitudFusionLejosMas(String amplitudFusionLejosMas)
	{
		this.amplitudFusionLejosMas = amplitudFusionLejosMas;
	}
	/**
	 * @return Returns the amplitudFusionLejosMenos.
	 */
	public String getAmplitudFusionLejosMenos()
	{
		return amplitudFusionLejosMenos;
	}
	/**
	 * @param amplitudFusionLejosMenos The amplitudFusionLejosMenos to set.
	 */
	public void setAmplitudFusionLejosMenos(String amplitudFusionLejosMenos)
	{
		this.amplitudFusionLejosMenos = amplitudFusionLejosMenos;
	}
	/**
	 * @return Returns the coverTestCercaCc.
	 */
	public String getCoverTestCercaCc()
	{
		return coverTestCercaCc;
	}
	/**
	 * @param coverTestCercaCc The coverTestCercaCc to set.
	 */
	public void setCoverTestCercaCc(String coverTestCercaCc)
	{
		this.coverTestCercaCc = coverTestCercaCc;
	}
	/**
	 * @return Returns the coverTestCercaSc.
	 */
	public String getCoverTestCercaSc()
	{
		return coverTestCercaSc;
	}
	/**
	 * @param coverTestCercaSc The coverTestCercaSc to set.
	 */
	public void setCoverTestCercaSc(String coverTestCercaSc)
	{
		this.coverTestCercaSc = coverTestCercaSc;
	}
	/**
	 * @return Returns the coverTestLejosCc.
	 */
	public String getCoverTestLejosCc()
	{
		return coverTestLejosCc;
	}
	/**
	 * @param coverTestLejosCc The coverTestLejosCc to set.
	 */
	public void setCoverTestLejosCc(String coverTestLejosCc)
	{
		this.coverTestLejosCc = coverTestLejosCc;
	}
	/**
	 * @return Returns the coverTestLejosSc.
	 */
	public String getCoverTestLejosSc()
	{
		return coverTestLejosSc;
	}
	/**
	 * @param coverTestLejosSc The coverTestLejosSc to set.
	 */
	public void setCoverTestLejosSc(String coverTestLejosSc)
	{
		this.coverTestLejosSc = coverTestLejosSc;
	}
	/**
	 * @return Returns the duccionesVersiones.
	 */
	public String getDuccionesVersiones()
	{
		return duccionesVersiones;
	}
	/**
	 * @param duccionesVersiones The duccionesVersiones to set.
	 */
	public void setDuccionesVersiones(String duccionesVersiones)
	{
		this.duccionesVersiones = duccionesVersiones;
	}
	/**
	 * @return Returns the estereopsis.
	 */
	public String getEstereopsis()
	{
		return estereopsis;
	}
	/**
	 * @param estereopsis The estereopsis to set.
	 */
	public void setEstereopsis(String estereopsis)
	{
		this.estereopsis = estereopsis;
	}
	/**
	 * @return Returns the observacionEstrabismo.
	 */
	public String getObservacionEstrabismo()
	{
		return observacionEstrabismo;
	}
	/**
	 * @param observacionEstrabismo The observacionEstrabismo to set.
	 */
	public void setObservacionEstrabismo(String observacionEstrabismo)
	{
		this.observacionEstrabismo = observacionEstrabismo;
	}
	/**
	 * @return Returns the ojoFijador.
	 */
	public int getOjoFijador()
	{
		return ojoFijador;
	}
	/**
	 * @param ojoFijador The ojoFijador to set.
	 */
	public void setOjoFijador(int ojoFijador)
	{
		this.ojoFijador = ojoFijador;
	}
/**
 * @return Returns the ppm.
 */
public String getPpm()
{
	return ppm;
}
/**
 * @param ppm The ppm to set.
 */
public void setPpm(String ppm)
{
	this.ppm = ppm;
}
	/**
	 * @return Returns the ppcInstitucion.
	 */
	public int getPpcInstitucion()
	{
		return ppcInstitucion;
	}
	/**
	 * @param ppcInstitucion The ppcInstitucion to set.
	 */
	public void setPpcInstitucion(int ppcInstitucion)
	{
		this.ppcInstitucion = ppcInstitucion;
	}
	/**
	 * @return Returns the prismaCcLejos.
	 */
	public String getPrismaCcLejos()
	{
		return prismaCcLejos;
	}
	/**
	 * @param prismaCcLejos The prismaCcLejos to set.
	 */
	public void setPrismaCcLejos(String prismaCcLejos)
	{
		this.prismaCcLejos = prismaCcLejos;
	}
	/**
	 * @return Returns the prismaCompensadorCerca.
	 */
	public String getPrismaCompensadorCerca()
	{
		return prismaCompensadorCerca;
	}
	/**
	 * @param prismaCompensadorCerca The prismaCompensadorCerca to set.
	 */
	public void setPrismaCompensadorCerca(String prismaCompensadorCerca)
	{
		this.prismaCompensadorCerca = prismaCompensadorCerca;
	}
	/**
	 * @return Returns the prismaCompensadorLejos.
	 */
	public String getPrismaCompensadorLejos()
	{
		return prismaCompensadorLejos;
	}
	/**
	 * @param prismaCompensadorLejos The prismaCompensadorLejos to set.
	 */
	public void setPrismaCompensadorLejos(String prismaCompensadorLejos)
	{
		this.prismaCompensadorLejos = prismaCompensadorLejos;
	}
	/**
	 * @return Returns the prismaScLejos.
	 */
	public String getPrismaScLejos()
	{
		return prismaScLejos;
	}
	/**
	 * @param prismaScLejos The prismaScLejos to set.
	 */
	public void setPrismaScLejos(String prismaScLejos)
	{
		this.prismaScLejos = prismaScLejos;
	}
	/**
	 * @return Returns the testVisionBinocular.
	 */
	public String getTestVisionBinocular()
	{
		return testVisionBinocular;
	}
	/**
	 * @param testVisionBinocular The testVisionBinocular to set.
	 */
	public void setTestVisionBinocular(String testVisionBinocular)
	{
		this.testVisionBinocular = testVisionBinocular;
	}
	
//	----------Funcion del hashMap----------------
	/**
	 * @return Returna la propiedad del mapa mapa.
	 */
	public Object getMapa(String key)
	{
		return mapa.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapa
	 */
	public void setMapa(String key, Object value)
	{
		this.mapa.put(key, value);
	}

	/**
	 * @return Retorna el mapa completo con toda la información
	 */
	public HashMap getMapaCompleto() {
		return this.mapa;
	}
	/**
	 * @param Asigna el mapa completo con toda la información
	 */
	public void setMapaCompleto(HashMap mapa) {
		this.mapa = mapa;
	}
//	-------------------------------------------------------------------------------------------------------//

	/**
	 * @return Returns the observacionSegmentoAnt.
	 */
	public String getObservacionSegmentoAnt()
	{
		return observacionSegmentoAnt;
	}
	/**
	 * @param observacionSegmentoAnt The observacionSegmentoAnt to set.
	 */
	public void setObservacionSegmentoAnt(String observacionSegmentoAnt)
	{
		this.observacionSegmentoAnt = observacionSegmentoAnt;
	}
    
    public String getImagenSegmentoAntOD()
    {
        return this.imagenSegmentoAnteriorOD;
    }
    
    public void setImagenSegmentoAntOD(String imagenSegmentoAntOD)
    {
        this.imagenSegmentoAnteriorOD = imagenSegmentoAntOD;
    }
    
    public String getImagenSegmentoAntOS()
    {
        return this.imagenSegmentoAnteriorOS;
    }

    public void setImagenSegmentoAntOS(String imagenSegmentoAntOS)
    {
        this.imagenSegmentoAnteriorOS = imagenSegmentoAntOS;
    }
    
    /**
     * @return Returns the observacionRetinaVitreo.
     */
    public String getObservacionRetinaVitreo()
    {
    	return observacionRetinaVitreo;
    }
    /**
     * @param observacionRetinaVitreo The observacionRetinaVitreo to set.
     */
    public void setObservacionRetinaVitreo(String observacionRetinaVitreo)
    {
    	this.observacionRetinaVitreo = observacionRetinaVitreo;
    }

    public String getImagenRetinaOD()
    {
        return this.imagenRetinaOD;
    }
    
    public void setImagenRetinaOD(String imagenRetinaOD)
    {
        this.imagenRetinaOD = imagenRetinaOD;
    }
    
    public String getImagenRetinaOS()
    {
        return this.imagenRetinaOS;
    }
    
    public void setImagenRetinaOS(String imagenRetinaOS)
    {
        this.imagenRetinaOS = imagenRetinaOS;
    }

    public String getImagenVitreoOD()
    {
        return this.imagenVitreoOD;
    }
    
    public void setImagenVitreoOD(String imagenVitreoOD)
    {
        this.imagenVitreoOD = imagenVitreoOD;
    }
    
    public String getImagenVitreoOS()
    {
        return this.imagenVitreoOS;
    }
    
    public void setImagenVitreoOS(String imagenVitreoOS)
    {
        this.imagenVitreoOS = imagenVitreoOS;
    }

/**
 * @return Returns the observacionOrbitaAnexos.
 */
public String getObservacionOrbitaAnexos()
{
	return observacionOrbitaAnexos;
}
/**
 * @param observacionOrbitaAnexos The observacionOrbitaAnexos to set.
 */
public void setObservacionOrbitaAnexos(String observacionOrbitaAnexos)
{
	this.observacionOrbitaAnexos = observacionOrbitaAnexos;
}
	/**
	 * @return Returns the datosMedico.
	 */
	public String getDatosMedico()
	{
		return datosMedico;
	}
	/**
	 * @param datosMedico The datosMedico to set.
	 */
	public void setDatosMedico(String datosMedico)
	{
		this.datosMedico = datosMedico;
	}
	/**
	 * @return Returns the fechaEstrabismo.
	 */
	public String getFechaEstrabismo()
	{
		return fechaEstrabismo;
	}
	/**
	 * @param fechaEstrabismo The fechaEstrabismo to set.
	 */
	public void setFechaEstrabismo(String fechaEstrabismo)
	{
		this.fechaEstrabismo = fechaEstrabismo;
	}
	/**
	 * @return Returns the hojaOftalmologicaDao.
	 */
	public HojaOftalmologicaDao getHojaOftalmologicaDao()
	{
		return hojaOftalmologicaDao;
	}
	/**
	 * @param hojaOftalmologicaDao The hojaOftalmologicaDao to set.
	 */
	public void setHojaOftalmologicaDao(
			HojaOftalmologicaDao hojaOftalmologicaDao)
	{
		this.hojaOftalmologicaDao = hojaOftalmologicaDao;
	}
	
//-------------------------------------------------------FIN DE SETS Y GETS-------------------------------------------------------------//
	
	/**
	 * Método para insertar una hoja oftalmológica
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente
	 * @return codigoHojaOftalmologica
	 * @throws SQLException
	 */
	
	public int insertarHojaOftalmologica (Connection con, int codigoPaciente) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaOftalmologicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaOftalmologicaDao - insertarHojaOftalmologica )");
		}
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
			
		resp1=hojaOftalmologicaDao.insertarHojaOftalmologica(con, codigoPaciente,  this.observacionEstrabismo, this.observacionSegmentoAnt,  this.observacionRetinaVitreo, this.observacionOrbitaAnexos);
		
		
		if (!inicioTrans||resp1<1)
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
	 * Metodo para insertar el encabezado historico de la hoja oftalmológica  
	 * @param con
	 * @param codHojaOftalmologica
	 * @param datosMedico
	 * @return codEncabezadoHisto
	 * @throws SQLException
	 */
	public int insertarEncabezadoHistoHojaOftalmologica(Connection con, int codHojaOftalmologica, String datosMedico) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaOftalmologicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaOftalmologicaDao - insertarEncabezadoHistoHojaOftalmologica )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
			
		resp1=hojaOftalmologicaDao.insertarEncabezadoHistoHojaOftalmologica(con, codHojaOftalmologica, datosMedico, this.numeroSolicitud);
		
		if (!inicioTrans||resp1<1)
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
	 * Metodo para insertar la sección estrabismo  
	 * @param con
	 * @param codEncabezadoHisto
	 * @return codEncaEstrabismo
	 * @throws SQLException
	 */
	public int insertarEstrabismo (Connection con, int codEncabezadoHisto) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaOftalmologicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaOftalmologicaDao - insertarEstrabismo )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
			
		resp1=hojaOftalmologicaDao.insertarEstrabismo(con, codEncabezadoHisto, this.ppm, 	this.coverTestCercaSc, this.coverTestCercaCc, this.coverTestLejosSc, this.coverTestLejosCc,this.ojoFijador, this.ppcInstitucion, this.prismaCcLejos, this.prismaScLejos, 
																									this.duccionesVersiones, this.testVisionBinocular, this.estereopsis, this.amplitudFusionCercaMas, this.amplitudFusionCercaMenos, this.amplitudFusionLejosMas, this.amplitudFusionLejosMenos, this.prismaCompensadorLejos, this.prismaCompensadorCerca);
		
		if (!inicioTrans||resp1<1)
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
	 * Metodo para insertar el prisma cerca en la sección estrabismo  
	 * @param con
	 * @param encaHistoEstrabismo
	 * @return encaHistoEstrabismo
	 * @throws SQLException
	 */
	public int insertarPrismaCerca (Connection con, int encaHistoEstrabismo) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaOftalmologicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaOftalmologicaDao - insertarPrismaCerca )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		//Inserción del prisma cerca sin corrección y con corrección
		for(int seccion=1; seccion<=9;seccion++)
		{
			String valorPrismaCercaSc= (String)this.getMapa("prismaCercaSc_"+seccion);
			if(UtilidadCadena.noEsVacio(valorPrismaCercaSc))
				resp1=hojaOftalmologicaDao.insertarPrismaCerca(con, encaHistoEstrabismo, seccion, false, valorPrismaCercaSc);
			
			String valorPrismaCercaCc= (String)this.getMapa("prismaCercaCc_"+seccion);
			if(UtilidadCadena.noEsVacio(valorPrismaCercaCc))
				resp1=hojaOftalmologicaDao.insertarPrismaCerca(con, encaHistoEstrabismo, seccion, true, valorPrismaCercaCc);
			
			if (!inicioTrans||resp1<1)
			{
			    myFactory.abortTransaction(con);
				return -1;
			}
			else
			{
			    myFactory.endTransaction(con);
			}
		}
		
		return resp1;
	}
	
	/**
	 * Metodo para insertar el detalle del segmento anterior  
	 * @param con
	 * @param codHistoSegmentoAnt
	 * @return codHistoSegmentoAnt
	 * @throws SQLException
	 */
	public int insertarDetalleSegmentoAnt (Connection con, int codHistoSegmentoAnt) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaOftalmologicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaOftalmologicaDao - insertarDetalleSegmentoAnt )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if(this.getMapa("codigosSegmentoAnt") != null)
		{
			Vector codigos=(Vector) this.getMapa("codigosSegmentoAnt");
			for (int i=0; i<codigos.size(); i++)
			{
				int segmentoAntInst=Integer.parseInt(codigos.elementAt(i)+"");
				if (this.getMapa("segmentoAntOd_"+segmentoAntInst) != null || this.getMapa("segmentoAntOs_"+segmentoAntInst) != null)
				{
					//Valor del ojo derecho
					String valorOd=(String)this.getMapa("segmentoAntOd_"+segmentoAntInst);
					//Valor del ojo izquierdo
					String valorOs=(String)this.getMapa("segmentoAntOs_"+segmentoAntInst);
					
					//Si alguno de los dos valores es diferente de vacío se realiza la inserción
					if(UtilidadCadena.noEsVacio(valorOd) || UtilidadCadena.noEsVacio(valorOs))
					{
						resp1=hojaOftalmologicaDao.insertarDetalleSegmentoAnt(con, codHistoSegmentoAnt, segmentoAntInst, valorOd, valorOs);
						if (!inicioTrans||resp1<1)
						{
						    myFactory.abortTransaction(con);
							return -1;
						}
						else
						{
						    myFactory.endTransaction(con);
						}
					}
				
				}//if
				
			}//for
		}//if codigosSegmentoAnt != null
		return resp1;
	}
	
	/**
	 * Metodo para insertar orbita y anexos  
	 * @param con
	 * @param codigoHistorico
	 * @return codHistoOrbitaAnexos
	 * @throws SQLException
	 */
	public int insertarOrbitaAnexos (Connection con, int codigoHistorico) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaOftalmologicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaOftalmologicaDao - insertarOrbitaAnexos )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if(this.getMapa("codigosOrbitaAnexos") != null)
		{
			Vector codigos=(Vector) this.getMapa("codigosOrbitaAnexos");
			for (int i=0; i<codigos.size(); i++)
			{
				int orbitaAnexoInst=Integer.parseInt(codigos.elementAt(i)+"");
				if (this.getMapa("orbitaAnexoOd_"+orbitaAnexoInst) != null || this.getMapa("orbitaAnexoOs_"+orbitaAnexoInst) != null)
				{
					//Valor del ojo derecho
					String valorOd=(String)this.getMapa("orbitaAnexoOd_"+orbitaAnexoInst);
					//Valor del ojo izquierdo
					String valorOs=(String)this.getMapa("orbitaAnexoOs_"+orbitaAnexoInst);
					
					//Si alguno de los dos valores es diferente de vacío se realiza la inserción
					if(UtilidadCadena.noEsVacio(valorOd) || UtilidadCadena.noEsVacio(valorOs))
					{
						resp1=hojaOftalmologicaDao.insertarOrbitaAnexos(con, codigoHistorico, orbitaAnexoInst, valorOd, valorOs);
						if (!inicioTrans||resp1<1)
						{
						    myFactory.abortTransaction(con);
							return -1;
						}
						else
						{
						    myFactory.endTransaction(con);
						}
					}
				}//if
				
			}//for
		}//if codigosOrbitaAnexos != null
		return resp1;
	}
	
	/**
	 * Metodo para insertar el Segmento anterior  
	 * @param con
	 * @param codHistorico
	 * @return codHistorico
	 * @throws SQLException
	 */
	public int insertarSegmentoAnterior (Connection con, int codHistorico) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (hojaOftalmologicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaOftalmologicaDao - insertarSegmentoAnterior )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
			
		resp1=hojaOftalmologicaDao.insertarSegmentoAnterior(con, codHistorico, this.imagenSegmentoAnteriorOD, this.imagenSegmentoAnteriorOS);
		
		if (!inicioTrans||resp1<1)
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
	
    public int insertarRetinaVitreo (Connection con, int codHistorico) throws SQLException
    {
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

        int  resp1=0;
            
        if (hojaOftalmologicaDao==null)
        {
            throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (HojaOftalmologicaDao - insertarSegmentoAnterior )");
        }
        //Iniciamos la transacción, si el estado es empezar
        boolean inicioTrans;
        
        inicioTrans=myFactory.beginTransaction(con);
            
        resp1=hojaOftalmologicaDao.insertarRetinaVitreo(con, codHistorico, 
                this.imagenRetinaOD, this.imagenRetinaOS, this.imagenVitreoOD, this.imagenVitreoOS);
        
        if (!inicioTrans||resp1<1)
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
	 * Método para consultar los tipos aplicables para la hoja oftalmológica según la institución
	 * @param con
	 * @param institucion
	 * @param tipo
	 * @return Collection
	 */
	
	public Collection consultarTipoParametrizado (Connection con, int institucion, int nroConsulta)
	{
		HojaOftalmologicaDao hojaOftalmologicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaOftalmologicaDao();
		Collection coleccion=null;
		try
		{	
			coleccion = hojaOftalmologicaDao.consultarTipoParametrizado(con, institucion, nroConsulta);
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar una tabla de parametrización por institución en la Hoja Oftalmologica"+"Consulta: "+nroConsulta +" " +e.toString());
		  coleccion=null;
		}
		return coleccion;	
	}
	
	/**
	 * Método para consultar los históricos de cada una de las secciones  de acuerdo al parámetro mandado
	 * @param con
	 * @param codigoPaciente
	 * @param nroConsulta.  1=>Historico Estrabismo, 2=> Histórico Segmento Anterior
	 * 							   				  3=>Histórico Retina y Vítreo, 4=> Histórico Orbita y anexos
	 * @return Collection
	 */
	
	public Collection consultarTipoHistorico (Connection con, int codigoPaciente, int nroConsulta)
	{
		HojaOftalmologicaDao hojaOftalmologicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaOftalmologicaDao();
		Collection coleccion=null;
		try
		{	
			coleccion = hojaOftalmologicaDao.consultarTipoHistorico(con, codigoPaciente, nroConsulta);
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar alguno de los históricos de las secciones de la Hoja Oftalmologica"+"Consulta: "+nroConsulta +" " +e.toString());
		  coleccion=null;
		}
		return coleccion;	
	}
    
	 /**
     * Metodo para consultar la hoja oftalmológica del paciente cargado 
     * @param con
     * @param codigoPaciente
     * @return true si existe una hoja oftalmológica para el paciente sino retorna false
     */
	public boolean cargarHojaOftalmologica (Connection con, int codigoPaciente)
	{
		HojaOftalmologicaDao hojaOftalmologicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaOftalmologicaDao();
		Collection coleccion=null;
				
		try
		{	
			coleccion = hojaOftalmologicaDao.cargarHojaOftalmologica (con, codigoPaciente);
			
			Iterator ite=coleccion.iterator();
			
			if(ite.hasNext())
				{
				HashMap col=(HashMap) ite.next();
					this.observacionEstrabismo = (col.get("observ_estrabismo")+"").equals("null")  ? "" : (col.get("observ_estrabismo")+"");
					this.observacionSegmentoAnt = (col.get("observ_segmento_ant")+"").equals("null")  ? "" : (col.get("observ_segmento_ant")+"");
					this.observacionRetinaVitreo = (col.get("observ_retina_vitreo")+"").equals("null")  ? "" : (col.get("observ_retina_vitreo")+"");
					this.observacionOrbitaAnexos = (col.get("observ_orbita_anexos")+"").equals("null")  ? "" : (col.get("observ_orbita_anexos")+"");
					
					return true;
				}
				else
				{
				 return false;	
				}
		}//try
	catch(Exception e)
		{
		  logger.warn("Error al Consultar la Hoja Oftalmológica " +e.toString());
		  coleccion=null;
		}
	 return false;		
	}
	
	 /**
     * Metodo para cargar la información del histórico de la sección Estrabismo, cuando seleccionan la 
     * fecha histórica
     * @param con
     * @param codigoHistorico
     * @return true si existe el histórico sino retorna false
     */
	public boolean cargarHistoricoEstrabismo (Connection con, int codigoHistorico)
	{
		HojaOftalmologicaDao hojaOftalmologicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaOftalmologicaDao();
		Collection coleccion=null;
				
		try
		{	
			coleccion = hojaOftalmologicaDao.cargarHistoricoEstrabismo (con, codigoHistorico);
			
			Iterator ite=coleccion.iterator();
			
			if(ite.hasNext())
				{
				HashMap col=(HashMap) ite.next();
					this.ppm = (col.get("ppm")+"").equals("null")  ? "" : (col.get("ppm")+"");
					this.coverTestCercaCc = (col.get("cover_test_cerca_cc")+"").equals("null")  ? "" : (col.get("cover_test_cerca_cc")+"");
					this.coverTestCercaSc = (col.get("cover_test_cerca_sc")+"").equals("null")  ? "" : (col.get("cover_test_cerca_sc")+"");
					this.coverTestLejosCc= (col.get("cover_test_lejos_cc")+"").equals("null")  ? "" : (col.get("cover_test_lejos_cc")+"");
					this.coverTestLejosSc= (col.get("cover_test_lejos_sc")+"").equals("null")  ? "" : (col.get("cover_test_lejos_sc")+"");
					
					if ((col.get("ojo_fijador")+"").equals(ValoresPorDefecto.getValorTrueParaConsultas()))
					{
						this.ojoFijador=1;
					}
					else
					{
						if((col.get("ojo_fijador")+"").equals(ValoresPorDefecto.getValorFalseParaConsultas()))
						{
							this.ojoFijador=0;
						}
						else
						{
							this.ojoFijador=-1;
						}
					}
										
					this.ppcInstitucion = Integer.parseInt(  (col.get("ppc_institucion")+"").equals("null") ? "0" : col.get("ppc_institucion")+"" );
					this.prismaCcLejos=(col.get("prisma_cc_lejos")+"").equals("null")  ? "" : (col.get("prisma_cc_lejos")+"");
					this.prismaScLejos=(col.get("prisma_sc_lejos")+"").equals("null")  ? "" : (col.get("prisma_sc_lejos")+"");
					this.duccionesVersiones=(col.get("ducciones_versiones")+"").equals("null")  ? "" : (col.get("ducciones_versiones")+"");
					this.testVisionBinocular=(col.get("vision_binocular")+"").equals("null")  ? "" : (col.get("vision_binocular")+"");
					this.estereopsis=(col.get("estereopsis")+"").equals("null")  ? "" : (col.get("estereopsis")+"");
					this.amplitudFusionCercaMas=(col.get("amp_fusion_cerca_mas")+"").equals("null")  ? "" : (col.get("amp_fusion_cerca_mas")+"");
					this.amplitudFusionCercaMenos=(col.get("amp_fusion_cerca_menos")+"").equals("null")  ? "" : (col.get("amp_fusion_cerca_menos")+"");
					this.amplitudFusionLejosMas=(col.get("amp_fusion_lejos_mas")+"").equals("null")  ? "" : (col.get("amp_fusion_lejos_mas")+"");
					this.amplitudFusionLejosMenos=(col.get("amp_fusion_lejos_menos")+"").equals("null")  ? "" : (col.get("amp_fusion_lejos_menos")+"");
					this.prismaCompensadorLejos=(col.get("prisma_compe_lejos")+"").equals("null")  ? "" : (col.get("prisma_compe_lejos")+"");
					this.prismaCompensadorCerca=(col.get("prisma_compe_cerca")+"").equals("null")  ? "" : (col.get("prisma_compe_cerca")+"");
					this.datosMedico = (col.get("datos_medico")+"").equals("null")  ? "" : (col.get("datos_medico")+"");
					this.fechaEstrabismo = (col.get("fecha_grabacion")+"").equals("null")  ? "" : (col.get("fecha_grabacion")+"");
					
					return true;
				}
				else
				{
				 return false;	
				}
		}//try
	catch(Exception e)
		{
		  logger.warn("Error al Consultar el Histórico de la sección Estrabismo " +e.toString());
		  coleccion=null;
		}
	 return false;		
	}
	
	 /**
     * Metodo para cargar la información histórica del prisma cerca en la sección Estrabismo cuando seleccionan una 
     * fecha histórica
     * @param con
     * @param codigoHistorico
     * @return true si existe el histórico sino retorna false
     */
	public boolean cargarHistoricoPrismaCerca (Connection con, int codigoHistorico)
	{
		HojaOftalmologicaDao hojaOftalmologicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaOftalmologicaDao();
		Collection coleccion=null;
		int seccion=0;
		String valor="";
		boolean hayDatos=false;
				
		try
		{	
			coleccion = hojaOftalmologicaDao.cargarHistoricoPrismaCerca (con, codigoHistorico);
			Iterator ite=coleccion.iterator();
			
			while(ite.hasNext())
				{
				HashMap col=(HashMap) ite.next();
										
					seccion = Integer.parseInt(  (col.get("seccion")+"").equals("null") ? "0" : col.get("seccion")+"" );
					valor=(col.get("valor")+"").equals("null")  ? "" : (col.get("valor")+"");
					
					if ((col.get("correccion")+"").equals(ValoresPorDefecto.getValorTrueParaConsultas()))
					{
						this.setMapa("prismaCercaCc_"+seccion, valor);
					}
					else
					{
						this.setMapa("prismaCercaSc_"+seccion, valor);
					}
					
					hayDatos=true;
				}
			}//try
		catch(Exception e)
		{
		  logger.warn("Error al Consultar el Histórico del prisma cerca en la sección Estrabismo " +e.toString());
		  coleccion=null;
		}
		return hayDatos;
	}

	/**
	 * @return Retorna numeroSolicitud.
	 */
	public int getNumeroSolicitud()
	{
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud Asigna numeroSolicitud.
	 */
	public void setNumeroSolicitud(int numeroSolicitud)
	{
		this.numeroSolicitud = numeroSolicitud;
	}
	
	/**
	 * Método para consultar un histórico específico
	 * @param con
	 * @param numeroConsulta
	 * @param codigoEncabezado
	 * @return
	 */
	public Collection consultarHistoricoEspecifico(Connection con, int numeroConsulta, int codigoEncabezado)
	{
		HojaOftalmologicaDao hojaOftalmologicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaOftalmologicaDao();
		return hojaOftalmologicaDao.consultarHistoricoEspecifico(con, numeroConsulta, codigoEncabezado);
	}

	/**
	 * Método que consulta el codigo del encabezado de una valoración específica para cada seccion
	 * @param con
	 * @param valoracion
	 * @return
	 */
	public int consultarHistoricoHojaOftal(Connection con, int valoracion, int tipoConsulta)
	{
		HojaOftalmologicaDao hojaOftalmologicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHojaOftalmologicaDao();
		return hojaOftalmologicaDao.consultarHistoricoHojaOftal(con, valoracion, tipoConsulta);
	}

}
