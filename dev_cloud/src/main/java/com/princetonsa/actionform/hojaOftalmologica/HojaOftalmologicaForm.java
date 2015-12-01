/*
 * Creado en Sep 21, 2005
 */
package com.princetonsa.actionform.hojaOftalmologica;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class HojaOftalmologicaForm extends ValidatorForm implements Cloneable
{
	 /**
	 * Manejo de estados para el flujo de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Guarda el estado anterior 
	 */
	private String estadoAnterior;
	
	 //SECCION DE ESTRABISMO
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
	   * Campo para registrar las observaciones de estrabismo nuevas 
	   */
	 private String observacionEstrabismoNueva;
	
	 /**
	   * Campo que guarda los prismas de Cerca, segmento anterior, orbita y anexos
	   */
	  private HashMap mapa;
	  
	  /**
	   * Coleccion para traer los tipos de ppc parametrizados por institución   
	   */
	  private Collection listadoTiposPpc;
	  
	  /**
	   * Coleccion para traer las fechas históricas y el código histórico de estrabismo   
	   */
	  private Collection listadoHistoEstrabismo;
	  
	    /**
	     * Indide para el recorrido de los historicos de la sección estrabismo 
	     */
	    private int posIndEstrabismo;
	    
	    /**
	     * Código histórico de la sección estrabismo para realizar la carga de datos cuando dan click en una
	     * de las fechas históricas
	     */
	   private int codHistoEstrabismo;
	   
		 /**
		  * Campo para guardar los datos del médico cuando se realiza la consulta de algún histórico de la
		  * sección Estrabismo
		  */
		  private String datosMedico;
		  
		/**
		 * Campo para guardar el estado de la sección Estrabismo ya sea actual o histórico, para de esta forma 
		 * saber cuando se debe clonar el formulario
		 */
		  private String estadoEstrabismo;
		  
	   /**
	    * Fecha de grabación del histórico de la sección Estrabismo
	    */
		private String fechaEstrabismo;
	  
//	-----------------------------------------SECCION DE SEGMENTO ANTERIOR-----------------------------------------------------------------//
      /**
       * Pixeles imagen ojo derecho segmento anterior
       */
      private String imagenSegmentoAnteriorOD;
      
      /**
       * Ancho de la imagen
       */
      private int anchoImagenSegmentoAnteriorOD;
      
      /**
       * Alto de la imagen
       */
      private int altoImagenSegmentoAnteriorOD;
    
      /**
       * Pixeles imagen ojo izquierdo segmento anterior
       */
      private String imagenSegmentoAnteriorOS;

      /**
       * Ancho de la imagen
       */
      private int anchoImagenSegmentoAnteriorOS;
      
      /**
       * Alto de la imagen
       */
      private int altoImagenSegmentoAnteriorOS;
	/**
	 * Observaciones de la seccion segmento anterior
	 */
	  private String observacionSegmentoAnt;
		  
	/**
	 * Observaciones de la seccion segmento anterior
	 */
	  private String observacionSegmentoAntNueva;
	  
	  /**
	   * Coleccion para traer los tipos de segmento anterior por institución   
	   */
	  private Collection listadoTiposSegmentoAnt;
	  
	  /**
	   * Coleccion para traer el histórico de la sección Segmento Anterior
	   */
	  private Collection listadoHistoSegmentoAnt;
	  
        /**
         * Indide para el recorrido de los historicos de la sección Segmento Anterior 
         */
        private int posIndSegmentoAnt;

        /**
       * Coleccion para traer el histórico de la sección Retina Vitreo
       */
      private Collection listadoHistoRetinaVitreo;
      
	    /**
	     * Indide para el recorrido de los historicos de la sección Retina Vitreo 
	     */
	    private int posIndRetinaVitreo;
	    
	 /**
	  * Campo para guardar los datos del médico cuando se realiza la consulta de algún histórico de la
	  * sección Segmento Anterior para poder mostrarlo en la impresión
	  */
	  private String medicoSegmentoAnt;
	  
	   /**
	    * Fecha de grabación del histórico de la sección Segmento Anterior
	    */
		  private String fechaSegmentoAnt;

//-----------------------------------------SECCION DE RETINA Y VITREO-----------------------------------------------------------------//
      /**
       * Pixeles imagen ojo derecho retina
       */
      private String imagenRetinaOD;
    
      /**
       * Ancho de la imagen
       */
      private int anchoImagenRetinaOD;
      
      /**
       * Alto de la imagen
       */
      private int altoImagenRetinaOD;
      /**
       * Pixeles imagen ojo izquierdo retina
       */
      private String imagenRetinaOS;
    
      /**
       * Ancho de la imagen
       */
      private int anchoImagenRetinaOS;
      
      /**
       * Alto de la imagen
       */
      private int altoImagenRetinaOS;
      /**
       * Pixeles imagen ojo derecho vitreo
       */
      private String imagenVitreoOD;
    
      /**
       * Ancho de la imagen
       */
      private int anchoImagenVitreoOD;
      
      /**
       * Alto de la imagen
       */
      private int altoImagenVitreoOD;
      /**
       * Pixeles imagen ojo izquierdo vitreo
       */
      private String imagenVitreoOS;

      /**
       * Ancho de la imagen
       */
      private int anchoImagenVitreoOS;
      
      /**
       * Alto de la imagen
       */
      private int altoImagenVitreoOS;
      
      /**
       * Observaciones de la seccion retina y vitreo
	   */
	  private String observacionRetinaVitreo;
	  
	/**
	 * Observaciones nuevas de la seccion retina y vitreo
	 */
	  private String observacionRetinaVitreoNueva;
	  
  /**
	  * Campo para guardar los datos del médico cuando se realiza la consulta de algún histórico de la
	  * sección Retina y Vítreo para poder mostrarlo en la impresión
	  */
	  private String medicoRetinaVitreo;
	  
	   /**
	    * Fecha de grabación del histórico de la sección Retina y Vítreo
	    */
		  private String fechaRetinaVitreo;
			  
//	-----------------------------------------SECCION DE ORBITA Y ANEXOS-----------------------------------------------------------------//
	/**
	 * Observaciones de la seccion orbita y anexos
	 */
	  private String observacionOrbitaAnexos;
	  
  /**
	 * Observaciones nuevas de la seccion orbita y anexos
	 */
	  private String observacionOrbitaAnexosNueva;
	  
	  /**
	   * Coleccion para traer los tipos de orbita y anexos por institución   
	   */
	  private Collection listadoTiposOrbitaAnexos;
	  
	  /**
	   * Coleccion para traer el histórico de la sección Orbita y Anexos
	   */
	  private Collection listadoHistoOrbitaAnexos;
	  
	    /**
	     * Indide para el recorrido de los historicos de la sección  Orbita y Anexos
	     */
	    private int posIndOrbitaAnexo;
	    
	    /**
		  * Campo para guardar los datos del médico cuando se realiza la consulta de algún histórico de la
		  * sección Orbita y Anexos para poder mostrarlo en la impresión
		  */
		  private String medicoOrbitaAnexos;
		  
	   /**
	    * Fecha de grabación del histórico de la sección Orbita y Anexos
	    */
		  private String fechaOrbitaAnexos;
		  
		  //--------------------------------Otros atributos diferente de la sección----------------------------------------------//
	  /**
		 * Campo para identificar que sección  de la hoja oftalmológica se va ha mostrar
		 */
		private int mostrarSeccion;
		
		/**
		 * Variable para indicar si la página de la hoja oftalmológica se debe abrir de tipo consulta
		 */
		private int esConsulta;
		
		/**
		 * Variable para indicar si se debe abrir la sección estrabismo
		 */
		private boolean abrirSeccionEstrabismo;
		
		/**
		 * Utilizado para la valoración de oftalmología
		 */
		private int numeroSolicitud;
//	---------------------------------------------------Fin de declaración de atributos-----------------------------------------------------------//
	    
		/**
		 * Funcion Para poder pasar todos los valores de un Objeto Form a Otro 
		 */
		public Object clone()
		{
			HojaOftalmologicaForm obj=null;
			try
			{
				obj=(HojaOftalmologicaForm)super.clone();
			}
			catch(CloneNotSupportedException ex)
			{
				ex.printStackTrace();
			}
			return obj;
		}
	  
	  /**
		 * Este método inicializa los atributos de la clase con valores vacíos
		 * @param alSalir => boolean
		 */
	  public void reset(boolean alSalir)
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
	  	this.datosMedico = "";
	  	this.medicoSegmentoAnt = "";
	  	this.medicoRetinaVitreo = "";
	  	this.medicoOrbitaAnexos = "";
	  	this.fechaEstrabismo=UtilidadFecha.getFechaActual();
	  	this.fechaSegmentoAnt="";
	  	this.fechaRetinaVitreo="";
	  	this.fechaOrbitaAnexos="";
        //Sección segmento anterior
        this.imagenSegmentoAnteriorOD = "";
        this.altoImagenSegmentoAnteriorOD=-1;
        this.anchoImagenSegmentoAnteriorOD=-1;
        this.imagenSegmentoAnteriorOS = "";
        this.altoImagenSegmentoAnteriorOS=-1;
        this.anchoImagenSegmentoAnteriorOS=-1;
        //Sección retina y vítreo
        this.imagenRetinaOD="";
        this.altoImagenRetinaOD=-1;
        this.anchoImagenRetinaOD=-1;
        this.imagenRetinaOS="";
        this.altoImagenRetinaOS=-1;
        this.anchoImagenRetinaOS=-1;
        this.imagenVitreoOD="";
        this.altoImagenVitreoOD=-1;
        this.anchoImagenVitreoOD=-1;
        this.imagenVitreoOS="";
        this.altoImagenVitreoOS=-1;
        this.anchoImagenVitreoOS=-1;
	  	
	  	if(!alSalir)
		{
	  	this.observacionEstrabismo = "";
	  	this.observacionEstrabismoNueva = "";
		this.observacionSegmentoAntNueva = "";
	  	this.observacionSegmentoAnt = "";
		this.observacionRetinaVitreoNueva = "";
		this.observacionRetinaVitreo = "";
	  	//Sección orbita y anexos
		this.observacionOrbitaAnexosNueva = "";
		this.observacionOrbitaAnexos="";
		this.posIndEstrabismo=0;
	  	this.posIndSegmentoAnt=0;
        this.posIndRetinaVitreo=0;
        this.posIndOrbitaAnexo=0;
	  	this.mostrarSeccion=0;
		this.listadoHistoEstrabismo=null;
	  	this.listadoHistoSegmentoAnt=null;
	  	this.listadoHistoRetinaVitreo=null;
	  	this.listadoHistoOrbitaAnexos=null;
	  	this.estadoAnterior="";
	  	}
	  	this.mapa=new HashMap();
	  }
      
	  /**
	   * Validate the properties that have been set from this HTTP request, and
	   * return an <code>ActionErrors</code> object that encapsulates any
	   * validation errors that have been found.  If no errors are found, return
	   * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	   * error messages.
	   *
	   * @param mapping The mapping used to select this instance
	   * @param request The servlet request we are processing
	  */
	  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	  {
	  	ActionErrors errores= new ActionErrors();
	  	
	  	//HttpSession session=request.getSession();	
	  	//PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
	  	
	  	if(estado.equals("salir"))
	  	{
	  		
	  	}//Tiene errores
	  		  	
	  	if(!errores.isEmpty())
	  	{
	  		if(estado.equals("salir"))
	  			this.setEstado("empezar");
	  	}
	  	
	  	return errores;
	  	
	  }
	  
//-----------------------------------------------------SETS Y GETS-----------------------------------------------------------------------//
	
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
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	/**
	 * @return Returns the estadoAnterior.
	 */
	public String getEstadoAnterior()
	{
		return estadoAnterior;
	}
	/**
	 * @param estadoAnterior The estadoAnterior to set.
	 */
	public void setEstadoAnterior(String estadoAnterior)
	{
		this.estadoAnterior = estadoAnterior;
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
	 * @return Returns the observacionEstrabismoNueva.
	 */
	public String getObservacionEstrabismoNueva()
	{
		return observacionEstrabismoNueva;
	}
	/**
	 * @param observacionEstrabismoNueva The observacionEstrabismoNueva to set.
	 */
	public void setObservacionEstrabismoNueva(
			String observacionEstrabismoNueva)
	{
		this.observacionEstrabismoNueva = observacionEstrabismoNueva;
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
    public String getImagenSegmentoAntOD()
    {
        return this.imagenSegmentoAnteriorOD;
    }
    
    public void setImagenSegmentoAntOD(String imagenSegmentoAntOD)
    {
        this.imagenSegmentoAnteriorOD = imagenSegmentoAntOD;
    }
    
    public int getAltoImagenSegmentoAntOD()
    {
        return this.altoImagenSegmentoAnteriorOD;
    }
    
    public void setAltoImagenSegmentoAntOD(int altoImagenSegmentoAnteriorOD)
    {
        this.altoImagenSegmentoAnteriorOD = altoImagenSegmentoAnteriorOD;
    }
    
    public int getAnchoImagenSegmentoAntOD()
    {
        return this.anchoImagenSegmentoAnteriorOD;
    }
    
    public void setAnchoImagenSegmentoAntOD(int anchoImagenSegmentoAnteriorOD)
    {
        this.anchoImagenSegmentoAnteriorOD = anchoImagenSegmentoAnteriorOD;
    }
    
    public String getImagenSegmentoAntOS()
    {
        return this.imagenSegmentoAnteriorOS;
    }

    public void setImagenSegmentoAntOS(String imagenSegmentoAntOS)
    {
        this.imagenSegmentoAnteriorOS = imagenSegmentoAntOS;
    }

    public int getAltoImagenSegmentoAntOS()
    {
        return this.altoImagenSegmentoAnteriorOS;
    }

    public void setAltoImagenSegmentoAntOS(int altoImagenSegmentoAnteriorOS)
    {
        this.altoImagenSegmentoAnteriorOS = altoImagenSegmentoAnteriorOS;
    }

    public int getAnchoImagenSegmentoAntOS()
    {
        return this.anchoImagenSegmentoAnteriorOS;
    }

    public void setAnchoImagenSegmentoAntOS(int anchoImagenSegmentoAnteriorOS)
    {
        this.anchoImagenSegmentoAnteriorOS = anchoImagenSegmentoAnteriorOS;
    }

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
	/**
	 * @return Returns the observacionSegmentoAntNueva.
	 */
	public String getObservacionSegmentoAntNueva()
	{
		return observacionSegmentoAntNueva;
	}
	/**
	 * @param observacionSegmentoAntNueva The observacionSegmentoAntNueva to set.
	 */
	public void setObservacionSegmentoAntNueva(
			String observacionSegmentoAntNueva)
	{
		this.observacionSegmentoAntNueva = observacionSegmentoAntNueva;
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
 * @return Returns the observacionOrbitaAnexosNueva.
 */
public String getObservacionOrbitaAnexosNueva()
{
	return observacionOrbitaAnexosNueva;
}
/**
 * @param observacionOrbitaAnexosNueva The observacionOrbitaAnexosNueva to set.
 */
public void setObservacionOrbitaAnexosNueva(String observacionOrbitaAnexosNueva)
{
	this.observacionOrbitaAnexosNueva = observacionOrbitaAnexosNueva;
}

    public String getImagenRetinaOD()
    {
        return this.imagenRetinaOD;
    }
    
    public void setImagenRetinaOD(String imagenRetinaOD)
    {
        this.imagenRetinaOD = imagenRetinaOD;
    }
    
    public int getAltoImagenRetinaOD()
    {
        return this.altoImagenRetinaOD;
    }
    
    public void setAltoImagenRetinaOD(int altoImagenRetinaOD)
    {
        this.altoImagenRetinaOD = altoImagenRetinaOD;
    }
    
    public int getAnchoImagenRetinaOD()
    {
        return this.anchoImagenRetinaOD;
    }
    
    public void setAnchoImagenRetinaOD(int anchoImagenRetinaOD)
    {
        this.anchoImagenRetinaOD = anchoImagenRetinaOD;
    }
    
    public String getImagenRetinaOS()
    {
        return this.imagenRetinaOS;
    }
    
    public void setImagenRetinaOS(String imagenRetinaOS)
    {
        this.imagenRetinaOS = imagenRetinaOS;
    }
    
    public int getAltoImagenRetinaOS()
    {
        return this.altoImagenRetinaOS;
    }
    
    public void setAltoImagenRetinaOS(int altoImagenRetinaOS)
    {
        this.altoImagenRetinaOS = altoImagenRetinaOS;
    }

    public int getAnchoImagenRetinaOS()
    {
        return this.anchoImagenRetinaOS;
    }
    
    public void setAnchoImagenRetinaOS(int anchoImagenRetinaOS)
    {
        this.anchoImagenRetinaOS = anchoImagenRetinaOS;
    }

    public String getImagenVitreoOD()
    {
        return this.imagenVitreoOD;
    }
    
    public void setImagenVitreoOD(String imagenVitreoOD)
    {
        this.imagenVitreoOD = imagenVitreoOD;
    }
    
    public int getAltoImagenVitreoOD()
    {
        return this.altoImagenVitreoOD;
    }
    
    public void setAltoImagenVitreoOD(int altoImagenVitreoOD)
    {
        this.altoImagenVitreoOD = altoImagenVitreoOD;
    }
    
    public int getAnchoImagenVitreoOD()
    {
        return this.anchoImagenVitreoOD;
    }
    
    public void setAnchoImagenVitreoOD(int anchoImagenVitreoOD)
    {
        this.anchoImagenVitreoOD = anchoImagenVitreoOD;
    }
    
    public String getImagenVitreoOS()
    {
        return this.imagenVitreoOS;
    }
    
    public void setImagenVitreoOS(String imagenVitreoOS)
    {
        this.imagenVitreoOS = imagenVitreoOS;
    }
    
    public int getAltoImagenVitreoOS()
    {
        return this.altoImagenVitreoOS;
    }
    
    public void setAltoImagenVitreoOS(int altoImagenVitreoOS)
    {
        this.altoImagenVitreoOS = altoImagenVitreoOS;
    }
    
    public int getAnchoImagenVitreoOS()
    {
        return this.anchoImagenVitreoOS;
    }
    
    public void setAnchoImagenVitreoOS(int anchoImagenVitreoOS)
    {
        this.anchoImagenVitreoOS = anchoImagenVitreoOS;
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
	/**
	 * @return Returns the observacionRetinaVitreoNueva.
	 */
	public String getObservacionRetinaVitreoNueva()
	{
		return observacionRetinaVitreoNueva;
	}
	/**
	 * @param observacionRetinaVitreoNueva The observacionRetinaVitreoNueva to set.
	 */
	public void setObservacionRetinaVitreoNueva(
			String observacionRetinaVitreoNueva)
	{
		this.observacionRetinaVitreoNueva = observacionRetinaVitreoNueva;
	}
	/**
	 * @return Returns the listadoTiposPpc.
	 */
	public Collection getListadoTiposPpc()
	{
		return listadoTiposPpc;
	}
	/**
	 * @param listadoTiposPpc The listadoTiposPpc to set.
	 */
	public void setListadoTiposPpc(Collection listadoTiposPpc)
	{
		this.listadoTiposPpc = listadoTiposPpc;
	}
	/**
	 * @return Returns the listadoTiposSegmentoAnt.
	 */
	public Collection getListadoTiposSegmentoAnt()
	{
		return listadoTiposSegmentoAnt;
	}
	/**
	 * @param listadoTiposSegmentoAnt The listadoTiposSegmentoAnt to set.
	 */
	public void setListadoTiposSegmentoAnt(Collection listadoTiposSegmentoAnt)
	{
		this.listadoTiposSegmentoAnt = listadoTiposSegmentoAnt;
	}
	/**
	 * @return Returns the listadoTiposOrbitaAnexos.
	 */
	public Collection getListadoTiposOrbitaAnexos()
	{
		return listadoTiposOrbitaAnexos;
	}
	/**
	 * @param listadoTiposOrbitaAnexos The listadoTiposOrbitaAnexos to set.
	 */
	public void setListadoTiposOrbitaAnexos(Collection listadoTiposOrbitaAnexos)
	{
		this.listadoTiposOrbitaAnexos = listadoTiposOrbitaAnexos;
	}
	/**
	 * @return Returns the listadoHistoEstrabismo.
	 */
	public Collection getListadoHistoEstrabismo()
	{
		return listadoHistoEstrabismo;
	}
	/**
	 * @param listadoHistoEstrabismo The listadoHistoEstrabismo to set.
	 */
	public void setListadoHistoEstrabismo(Collection listadoHistoEstrabismo)
	{
		this.listadoHistoEstrabismo = listadoHistoEstrabismo;
	}
	/**
	 * @return Returns the listadoHistoSegmentoAnt.
	 */
	public Collection getListadoHistoSegmentoAnt()
	{
		return listadoHistoSegmentoAnt;
	}
	/**
	 * @param listadoHistoSegmentoAnt The listadoHistoSegmentoAnt to set.
	 */
	public void setListadoHistoSegmentoAnt(Collection listadoHistoSegmentoAnt)
	{
		this.listadoHistoSegmentoAnt = listadoHistoSegmentoAnt;
	}
    /**
     * @return Returns the listadoHistoRetinaVitreo.
     */
    public Collection getListadoHistoRetinaVitreo()
    {
        return listadoHistoRetinaVitreo;
    }
    /**
     * @param listadoHistoSegmentoAnt The listadoHistoSegmentoAnt to set.
     */
    public void setListadoHistoRetinaVitreo(Collection listadoHistoRetinaVitreo)
    {
        this.listadoHistoRetinaVitreo = listadoHistoRetinaVitreo;
    }
	/**
	 * @return Returns the listadoHistoOrbitaAnexos.
	 */
	public Collection getListadoHistoOrbitaAnexos()
	{
		return listadoHistoOrbitaAnexos;
	}
	/**
	 * @param listadoHistoOrbitaAnexos The listadoHistoOrbitaAnexos to set.
	 */
	public void setListadoHistoOrbitaAnexos(Collection listadoHistoOrbitaAnexos)
	{
		this.listadoHistoOrbitaAnexos = listadoHistoOrbitaAnexos;
	}
		/**
		 * @return Returns the posIndEstrabismo.
		 */
		public int getPosIndEstrabismo()
		{
			return posIndEstrabismo;
		}
		/**
		 * @param posIndEstrabismo The posIndEstrabismo to set.
		 */
		public void setPosIndEstrabismo(int posIndEstrabismo)
		{
			this.posIndEstrabismo = posIndEstrabismo;
		}
		/**
		 * @return Returns the posIndOrbitaAnexo.
		 */
		public int getPosIndOrbitaAnexo()
		{
			return posIndOrbitaAnexo;
		}
		/**
		 * @param posIndOrbitaAnexo The posIndOrbitaAnexo to set.
		 */
		public void setPosIndOrbitaAnexo(int posIndOrbitaAnexo)
		{
			this.posIndOrbitaAnexo = posIndOrbitaAnexo;
		}
		/**
		 * @return Returns the posIndSegmentoAnt.
		 */
		public int getPosIndSegmentoAnt()
		{
			return posIndSegmentoAnt;
		}
		/**
		 * @param posIndSegmentoAnt The posIndSegmentoAnt to set.
		 */
		public void setPosIndSegmentoAnt(int posIndSegmentoAnt)
		{
			this.posIndSegmentoAnt = posIndSegmentoAnt;
		}
        /**
         * @return Returns the posIndRetinaVitreo.
         */
        public int getPosIndRetinaVitreo()
        {
            return posIndRetinaVitreo;
        }
        /**
         * @param posIndRetinaVitreo The posIndRetinaVitreo to set.
         */
        public void setPosIndRetinaVitreo(int posIndRetinaVitreo)
        {
            this.posIndRetinaVitreo = posIndRetinaVitreo;
        }
		/**
		 * @return Returns the codHistoEstrabismo.
		 */
		public int getCodHistoEstrabismo()
		{
			return codHistoEstrabismo;
		}
		/**
		 * @param codHistoEstrabismo The codHistoEstrabismo to set.
		 */
		public void setCodHistoEstrabismo(int codHistoEstrabismo)
		{
			this.codHistoEstrabismo = codHistoEstrabismo;
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
		 * @return Returns the estadoEstrabismo.
		 */
		public String getEstadoEstrabismo()
		{
			return estadoEstrabismo;
		}
		/**
		 * @param estadoEstrabismo The estadoEstrabismo to set.
		 */
		public void setEstadoEstrabismo(String estadoEstrabismo)
		{
			this.estadoEstrabismo = estadoEstrabismo;
		}
		/**
		 * @return Returns the medicoOrbitaAnexos.
		 */
		public String getMedicoOrbitaAnexos()
		{
			return medicoOrbitaAnexos;
		}
		/**
		 * @param medicoOrbitaAnexos The medicoOrbitaAnexos to set.
		 */
		public void setMedicoOrbitaAnexos(String medicoOrbitaAnexos)
		{
			this.medicoOrbitaAnexos = medicoOrbitaAnexos;
		}
/**
 * @return Returns the medicoRetinaVitreo.
 */
public String getMedicoRetinaVitreo()
{
	return medicoRetinaVitreo;
}
/**
 * @param medicoRetinaVitreo The medicoRetinaVitreo to set.
 */
public void setMedicoRetinaVitreo(String medicoRetinaVitreo)
{
	this.medicoRetinaVitreo = medicoRetinaVitreo;
}
	/**
	 * @return Returns the medicoSegmentoAnt.
	 */
	public String getMedicoSegmentoAnt()
	{
		return medicoSegmentoAnt;
	}
	/**
	 * @param medicoSegmentoAnt The medicoSegmentoAnt to set.
	 */
	public void setMedicoSegmentoAnt(String medicoSegmentoAnt)
	{
		this.medicoSegmentoAnt = medicoSegmentoAnt;
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
	 * @return Returns the fechaOrbitaAnexos.
	 */
	public String getFechaOrbitaAnexos()
	{
		return fechaOrbitaAnexos;
	}
	/**
	 * @param fechaOrbitaAnexos The fechaOrbitaAnexos to set.
	 */
	public void setFechaOrbitaAnexos(String fechaOrbitaAnexos)
	{
		this.fechaOrbitaAnexos = fechaOrbitaAnexos;
	}
	/**
	 * @return Returns the fechaRetinaVitreo.
	 */
	public String getFechaRetinaVitreo()
	{
		return fechaRetinaVitreo;
	}
	/**
	 * @param fechaRetinaVitreo The fechaRetinaVitreo to set.
	 */
	public void setFechaRetinaVitreo(String fechaRetinaVitreo)
	{
		this.fechaRetinaVitreo = fechaRetinaVitreo;
	}
	/**
	 * @return Returns the fechaSegmentoAnt.
	 */
	public String getFechaSegmentoAnt()
	{
		return fechaSegmentoAnt;
	}
	/**
	 * @param fechaSegmentoAnt The fechaSegmentoAnt to set.
	 */
	public void setFechaSegmentoAnt(String fechaSegmentoAnt)
	{
		this.fechaSegmentoAnt = fechaSegmentoAnt;
	}
		/**
		 * @return Returns the mostrarSeccion.
		 */
		public int getMostrarSeccion()
		{
			return mostrarSeccion;
		}
		/**
		 * @param mostrarSeccion The mostrarSeccion to set.
		 */
		public void setMostrarSeccion(int mostrarSeccion)
		{
			this.mostrarSeccion = mostrarSeccion;
		}
		/**
		 * @return Returns the esConsulta.
		 */
		public int getEsConsulta()
		{
			return esConsulta;
		}
		/**
		 * @param esConsulta The esConsulta to set.
		 */
		public void setEsConsulta(int esConsulta)
		{
			this.esConsulta = esConsulta;
		}
		/**
		 * @return Returns the abrirSeccionEstrabismo.
		 */
		public boolean isAbrirSeccionEstrabismo()
		{
			return abrirSeccionEstrabismo;
		}
		/**
		 * @param abrirSeccionEstrabismo The abrirSeccionEstrabismo to set.
		 */
		public void setAbrirSeccionEstrabismo(boolean abrirSeccionEstrabismo)
		{
			this.abrirSeccionEstrabismo = abrirSeccionEstrabismo;
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
}
