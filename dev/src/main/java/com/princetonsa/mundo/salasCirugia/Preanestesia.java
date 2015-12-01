/*
 * Creado en Oct 26, 2005
 */
package com.princetonsa.mundo.salasCirugia;


import java.sql.SQLException;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import java.util.HashMap;
import org.apache.log4j.Logger;

import util.UtilidadCadena;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PreanestesiaDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para el manejo de la Preanestesia en la Hoja de Anestesia en la cirugía
 * 
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class Preanestesia
{
	/**
	 * Para hacer logs de esta funcionalidad.
	*/
		private Logger logger = Logger.getLogger(Preanestesia.class);
		
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private PreanestesiaDao preanestesiaDao = null;
	
//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	 /**
	  * Fecha de la Preanestesia
	  */
		private String fechaPreanestesia;
		
	/**
	 * Hora de la Preanestesia 
	 */
		private String horaPreanestesia;
	
	/**
	 * Observaciones Generales de la Preanestesia
	 */
	private String observacionesGrales;
	
	/**
	 * Mapa de la preanestesia
	 */
	private HashMap mapa = new HashMap();
	
	/**
	 * Mapa para almacenar los examenes de laboratorio 
	 */
	private HashMap mapaExamenFisico = new HashMap();
	
	/**
	 * Mapa para almacenar las conclusiones parametrizadas en la preanestesia 
	 */
	private HashMap mapaConclusion = new HashMap();
	
	//------------------------------Campos de información general de la petición--------------------------------------------//
	/**
	 * Número de la petición de cirugía
	 */
		private int peticionCirugia;
		
	/**
	 * Fecha de la petición de cirugía
	 */
		private String fechaPeticion;
		
	/**
	 * Hora de la petición de cirugía
	 */
		private String horaPeticion;
		
	/**
	 * Estado de la petición de cirugía
	 */
		private String nombreEstadoPeticion;
		
	/**
	 * Código de la petición de cirugía
	 */
		private int codigoEstadoPeticion;
		
	/**
	 * Fecha estimada de la cirugía
	 */
		private String fechaCirugia;
		
	/**
	 * Duración aproximada de la cirugía
	 */
		private String duracionCirugia;
		
	/**
	 * Nombre completo del profesional que solicita
	 */
		private String nombreSolicitante;
	
	/**
	 * Para almacenar el tipo de anestesia 
	 */
		private int tipoAnestesia;

		
//	---------------------------------------------------FIN DE LA DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	

	//*********CONSTRUCTORES E INICIALIZADORES*********************************
	
	  /**
	   * Constructor de la clase, inicializa en vacío todos los atributos
	   */
	  public Preanestesia ()
	  {
	  	reset();
	  	this.init(System.getProperty("TIPOBD"));
	  }
	  
	  /**
		 * Este método inicializa los atributos de la clase con valores vacíos
		 */
	  public void reset()
	  {
	  	this.fechaPreanestesia= "";
	  	this.horaPreanestesia = "";
	  	this.observacionesGrales = "";
	  	this.mapa = new HashMap();
	  	this.peticionCirugia = 0;
	  	this.fechaPeticion = "";
	  	this.horaPeticion = "";
	  	this.fechaCirugia = "";
	  	this.nombreEstadoPeticion = "";
	  	this.duracionCirugia = "";
	  	this.nombreSolicitante = "";
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
				preanestesiaDao = myFactory.getPreanestesiaDao();
				wasInited = (preanestesiaDao != null);
			}
			return wasInited;
		}
		
	/**
	 * Método para insertar la valoración de preanestesia
	 * @param con una conexion abierta con una fuente de datos
	 * @param login
	 * @param datosMedico
	 * @return peticionCirugia
	 * @throws SQLException
	 */
	public int insertarValoracionPreanestesia (Connection con, String login, String datosMedico) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (preanestesiaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (PreanestesiaDao - insertarValoracionPreanestesia )");
		}
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		resp1=preanestesiaDao.insertarValoracionPreanestesia(con, this.peticionCirugia,  this.fechaPreanestesia, this.horaPreanestesia,  datosMedico, this.tipoAnestesia,  this.observacionesGrales, login);
		
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
	 * Método para insertar los exámenes de laboratorio de preanestesia, que se utiliza para
	 * los exámes parametrizados por institución así como los ingresados.
	 * @param con una conexion abierta con una fuente de datos
	 * @param peticionQx
	 * @param datosMedico
	 * @return codEncaExamenLab
	 * @throws SQLException
	 */
	public int insertarExamenLabPreanestesia (Connection con, int peticionQx, String datosMedico) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  codValExamenLab=0, codAdjunto=0,examenLabPar=0, numDocsAdj=0;
		boolean error=false;
			
		if (preanestesiaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (PreanestesiaDao - insertarEncabezadoExamenLab )");
		}
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if(this.getMapa("codigosExamenesLab") != null)
		{
			Vector codigos=(Vector) this.getMapa("codigosExamenesLab");
			Vector vecAccion=(Vector) this.getMapa("actualizar");
			
			String codOtrosExamenesLab=this.getMapa("codOtrosExamenesLab")+"";
			if(codOtrosExamenesLab!=null && !codOtrosExamenesLab.equals("") && !codOtrosExamenesLab.equals("0"))
			{
				String[] vecOtrosExamenes=codOtrosExamenesLab.split("-");
				for(int z=0; z<vecOtrosExamenes.length; z++)
				{
					codigos.add(vecOtrosExamenes[z]);
					vecAccion.add("0");
					this.setMapa("esOtro_"+vecOtrosExamenes[z], "1");
					this.setMapa("tieneAdjunto_"+vecOtrosExamenes[z],"1");
				}
			}
			
			for (int i=0; i<codigos.size(); i++)
			{
				int examenLabPreInst=Integer.parseInt(codigos.elementAt(i)+"");
				int accion=Integer.parseInt(vecAccion.elementAt(i)+"");
				
				//Resultado del exámen de laboratorio
				String resultadoExamenLab=(String)this.getMapa("resultadoExamenLab_"+examenLabPreInst);
				//Observación del exámen de laboratorio
				String observacionExamenLab=(String)this.getMapa("observacionExamenLab_"+examenLabPreInst);
				
				String tieneAdjuntos=(String)this.getMapa("tieneAdjunto_"+examenLabPreInst);
				
				
				if(tieneAdjuntos.equals("1"))
					{
					//Número de documentos adjuntos
					numDocsAdj=Integer.parseInt((String)this.getMapa("numDocAdj_"+examenLabPreInst));
					}
				else
					{
						if(accion!=-1)
							numDocsAdj=1;
					}
				
				if(accion!=-1 || numDocsAdj>0)
				{
					if(!resultadoExamenLab.trim().equals("") || !observacionExamenLab.trim().equals("") || numDocsAdj>0)
					{
						String esOtro=(String)this.getMapa("esOtro_"+examenLabPreInst);
						//Si la accion es actualizar o no tiene nada en resultado y observaciones pero tiene archivos adjuntos
						if(accion==1 || (tieneAdjuntos.equals("0")))
						{
							//Si el examen de laboratorio es otro
							if (esOtro.equals("1"))
								{
								//Descripción de otro examen de laboratorio
								String descripcionOtro=(String)this.getMapa("descripcionOtro_"+examenLabPreInst);
								//Se modifica el encabezado del examen de laboratorio de preanestesia de otro
								codValExamenLab=preanestesiaDao.insertarEncabezadoExamenLab(con, peticionQx, resultadoExamenLab, observacionExamenLab, false, -1, descripcionOtro, datosMedico);
								if(codValExamenLab<1)
									error=true;
								}
							else
								{
								//Se modifica el encabezado del examen de laboratorio de preanestesia cuando es parametrizado
								codValExamenLab=preanestesiaDao.insertarEncabezadoExamenLab(con, peticionQx, resultadoExamenLab, observacionExamenLab, false, examenLabPreInst, "", datosMedico);
								if(codValExamenLab<1)
									error=true;
								}
						}//if accion es actualizar = 1
						else
						{
							//Si la accion es insertar encabezado de examen de laboratorio
							if(accion==0)
								{
									//Se inserta el encabezado del examen de laboratorio de preanestesia
									codValExamenLab=preanestesiaDao.insertarEncabezadoExamenLab(con, peticionQx, resultadoExamenLab, observacionExamenLab, true, -1, "", datosMedico);
									
									if(codValExamenLab<1)
										error=true;
									
									//Si es un exámen de laboratorio tipo otro se inserta en la tabla correspondiente
									if (esOtro.equals("1"))
										{
											//Descripción de otro examen de laboratorio
											String descripcionOtro=(String)this.getMapa("descripcionOtro_"+examenLabPreInst);
											//Se inserta el otro exámen de laboratorio								
											examenLabPar = preanestesiaDao.insertarExamenLabOtro(con, codValExamenLab, descripcionOtro);
											
											if(examenLabPar<1)
												error=true;
										}
									else
										{
										//Se inserta el exámen de laboratorio parametrizado								
										examenLabPar = preanestesiaDao.insertarExamenLabParametrizado(con, codValExamenLab, examenLabPreInst);
										
										if(examenLabPar<1)
											error=true;
										
										}
								}//if accion=0
							if(accion==-1)
							{
								//Si es otro exámen de laboratorio
								if (esOtro.equals("1"))
								{
								//Descripción de otro examen de laboratorio
								String descripcionOtro=(String)this.getMapa("descripcionOtro_"+examenLabPreInst);
								//Se consulta el código del encabezado del exámen de laboratorio
								codValExamenLab=preanestesiaDao.consultaCodigoEncabezadoExamenLab(con, peticionQx, descripcionOtro);
								}
								else
								{
									//Se consulta el código del encabezado del exámen de laboratorio
									codValExamenLab=preanestesiaDao.consultaCodigoEncabezadoExamenLab(con, peticionQx, examenLabPreInst);
								}
							}
						}//else
			
		//--------------------Se insertan los documentos adjuntos que tenga el exámen de laboratorio-------------------------------//
						if(tieneAdjuntos.equals("1"))
							{
								for(int j=0; j<numDocsAdj; j++)
								{
									String checkAdj=(String)this.getMapa("checkbox_"+examenLabPreInst+"_"+j);													
									String docRealAdj=(String)this.getMapa("nombreRealAdj_"+examenLabPreInst+"_"+j);
									String docGeneradoAdj=(String)this.getMapa("nombreGenAdj_"+examenLabPreInst+"_"+j);
									
									if(checkAdj!=null)
									{
										codAdjunto=preanestesiaDao.insertarAdjuntoExamenLabPreanestesia(con, codValExamenLab, docRealAdj, docGeneradoAdj);
										
										//Si es menor a 1 se sale del ciclo para abortar la transacción
										if(codAdjunto<1)
										{
											error=true;
											break;
										}
									}
								}//for
							}//if tieneAdjuntos
						
						if(error)
						{
							error=true;
							break;
						}
					}//if cuando ingresaron algo en las observaciones o resultados o adjuntaron documentos
				}//if accion!=-1 || numDocsAdj>0
			}//for
		}//if codigosOrbitaAnexos != null

		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error)
		{
			myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		
		return codValExamenLab;
	}
	
	/**
	 * Método para consultar los tipos aplicables para la Preanestesia según la institución
	 * @param con
	 * @param institucion
	 * @param peticionQx
	 * @param nroConsulta
	 * @return Collection
	 */
	
	public Collection consultarTipoParametrizado (Connection con, int institucion, int peticionQx, int nroConsulta)
	{
		Collection coleccion=null;
		try
		{	
			coleccion = preanestesiaDao.consultarTipoParametrizado(con, institucion, peticionQx, nroConsulta);
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar una tabla de parametrización por institución en la Preanestesia"+"Consulta: "+nroConsulta +" " +e.toString());
		  coleccion=null;
		}
		return coleccion;	
	}
	
	/**
	 * Metodo para consultar los examenes de laboratorio de la preanestesia de acuerdo a la institución y la petición de cirugía
	 * @param con
	 * @param institucion
	 * @param peticionQx
	 * @return
	 */
	public HashMap consultarExamenesLaboratorio(Connection con, int institucion, int peticionQx)
	{	
		return preanestesiaDao.consultarExamenesLaboratorio(con, institucion, peticionQx);
	}
	
	 /**
     * Metodo para consultar la preanestesia asociada a la petición generada 
     * @param con
     * @param peticionQx
     * @param cargarFechaPreanestesia
     * @return true si existe una preanestesia para la petición sino retorna false
     */
	public boolean cargarPreanestesia (Connection con, int peticionQx, boolean cargarFechaPreanestesia)
	{
		Collection coleccion=null;
		
		try
		{
			coleccion = preanestesiaDao.cargarPreanestesia (con, peticionQx);
			Iterator ite=coleccion.iterator();
			
			if(ite.hasNext())
				{
				HashMap col=(HashMap) ite.next();
					this.observacionesGrales = (col.get("observaciones_grales")+"").equals("null")  ? "" : (col.get("observaciones_grales")+"");
					this.tipoAnestesia = (col.get("tipo_anestesia")+"").equals("null")  ?  0 : Integer.parseInt(col.get("tipo_anestesia")+"");
					
					//Se carga la fecha y hora de la preanestesia, porque se está abriendo la funcionalidad desde la hoja de anestesia
					//por lo tanto no se permit su modificación
					if(cargarFechaPreanestesia)
						{
						this.fechaPreanestesia = (col.get("fecha_preanestesia")+"").equals("null")  ? "" : (col.get("fecha_preanestesia")+"");
						this.horaPreanestesia = (col.get("hora_preanestesia")+"").equals("null")  ? "" : (col.get("hora_preanestesia")+"");
						}
					
					return true;
				}
			else
				{
				 return false;	
				}
		}//try
		catch(Exception e)
		{
		  logger.warn("Error al Consultar la Preanestesia " +e.toString());
		  coleccion=null;
		}
	 return false;	
	}
	
    /**
     * Metodo para consultar y cargar la información de la sección Exámenes de Laboratorio
     * @param con
     * @param peticionQx
     * @return true si existe información de exámenes de laboratorio sino retorna false
     */
    public boolean cargarExamenesLabPre(Connection con, int peticionQx, int institucion) 
    {
    	Collection colExamenesLab=null;
    	int ultimoCodigoPar=0;
    	boolean entro=false;
    	
    	try
		{
    		colExamenesLab=preanestesiaDao.cargarExamenesLabPre(con, peticionQx);
    		Iterator ite=colExamenesLab.iterator();
    		for (int i=0; i<colExamenesLab.size(); i++)
    		{
    			if (ite.hasNext())
    			{
    				HashMap colExamLab=(HashMap) ite.next();
    				if (Integer.parseInt(colExamLab.get("codigo")+"")!=-1)
    				{
    					this.setMapa("resultadoExamenLab_"+colExamLab.get("codigo"), colExamLab.get("resultados"));
    					this.setMapa("observacionExamenLab_"+colExamLab.get("codigo"), colExamLab.get("observaciones"));
    					this.setMapa("datosMedico_"+colExamLab.get("codigo"), colExamLab.get("datos_medico"));
    					
    					if (!colExamLab.get("resultados").equals(""))
	    					{
	    						this.setMapa("resultadoCargado_"+colExamLab.get("codigo"), "1");
	    					}
    				
    					if (!colExamLab.get("observaciones").equals(""))
	    					{
	    						this.setMapa("observacionCargado_"+colExamLab.get("codigo"), "1");
	    					}
    					
    				}
    				else
    				{
    						if(!entro)
    						{
    							ultimoCodigoPar=preanestesiaDao.obtenerUltimoExamenLabPar(con, institucion);
    							entro=true;
    						}
    						
    						ultimoCodigoPar++;
    						this.setMapa("resultadoExamenLab_"+ultimoCodigoPar, colExamLab.get("resultados"));
        					this.setMapa("observacionExamenLab_"+ultimoCodigoPar, colExamLab.get("observaciones"));
        					this.setMapa("datosMedico_"+ultimoCodigoPar, colExamLab.get("datos_medico"));
        					
        					if (!colExamLab.get("resultados").equals(""))
	    					{
	    						this.setMapa("resultadoCargado_"+ultimoCodigoPar, "1");
	    					}
    					
    					if (!colExamLab.get("observaciones").equals(""))
	    					{
	    						this.setMapa("observacionCargado_"+ultimoCodigoPar, "1");
	    					}
    				
    				}
    			}//if ite.hasNext
    		}//for
    		
    	}
    	catch(Exception e)
		{
		  logger.warn("Error al Consultar los exámenes de laboratorio de preanestesia en el mundo" +e.toString());
		  colExamenesLab=null;
		}
    	
    	return false;
    }
    
    /**
     * Metodo para consultar y cargar la información de la sección Exámenes Fisicos
     * @param con
     * @param peticionQx
     * @return true si existe información de exámenes de laboratorio sino retorna false
     */
    public boolean cargarExamenesFisicos(Connection con, int peticionQx) 
    {
    	Collection colExamenesLab=null;

    	try
		{
    		
    		//---Cargando la informacion de los examenes fisicos parametrizados (tipo Text) 
    		colExamenesLab=preanestesiaDao.cargarExamenesFisicos(con, peticionQx);
    		Iterator ite=colExamenesLab.iterator();
    		for (int i=0; i<colExamenesLab.size(); i++)
    		{
    			if (ite.hasNext())
    			{
    				HashMap colExamLab=(HashMap) ite.next();
    				
    				
    				this.setMapaExamenFisico("exa_"+colExamLab.get("codigo"), colExamLab.get("valor"));
    				this.setMapaExamenFisico("examFisicoCargado_"+colExamLab.get("codigo"), "1");
    			}
    		}
    	
    		colExamenesLab = null;
    		//---Cargando la informacion de los examenes fisicos (tipo TextArea)
    		colExamenesLab=preanestesiaDao.cargarExamenesFisicosArea(con, peticionQx);
    		ite=colExamenesLab.iterator();
    		for (int i=0; i<colExamenesLab.size(); i++)
    		{
    			if (ite.hasNext())
    			{
    				HashMap colExamLab=(HashMap) ite.next();
    				this.setMapaExamenFisico("exa_Area_"+colExamLab.get("codigo"), colExamLab.get("valor"));
    			}
    		}
    	}
    	catch(Exception e)
		{
		  logger.warn("Error al Consultar los exámenes de fisicos de preanestesia en el mundo" +e.toString());
		  colExamenesLab=null;
		}
    	
    	return false;
    }
    
    /**
     * Metodo para consultar y cargar la información de la sección de conclusiones
     * @param con
     * @param valPreanestesia
     * @return true si existe información de conclusiones sino retorna false
     */
    public boolean cargarConclusiones(Connection con, int valPreanestesia) 
    {
    	Collection colConclusiones=null;
    	
    	try
		{
    		colConclusiones=preanestesiaDao.cargarConclusiones (con, valPreanestesia);
    		Iterator ite=colConclusiones.iterator();
    		for (int i=0; i<colConclusiones.size(); i++)
    		{
    			if (ite.hasNext())
    			{
    				HashMap colConclu=(HashMap) ite.next();
    				if(colConclu.get("codigo") != null && !colConclu.get("codigo").equals(""))
    				{
    					this.setMapaConclusion("conclusion_"+colConclu.get("codigo"), colConclu.get("valor_conclusion"));
    				}
    			}//if
    		}//for
		}//try
    	catch(Exception e)
		{
		  logger.warn("Error al Consultar las conclusiones de preanestesia en el mundo" +e.toString());
		  colConclusiones=null;
		}
    	
    	return false;
    	
    }
    
    /**
     * Metodo para insertar los examenes fisicos 
     * @param con
     * @param medico
     * @param tablaDestino : indicador para saber sobre cual tabla se inserta
     * @param codigoOtro
     * @param val_preanestesia
     * @param nombreOtro
     * @param valor
     * @return
     * @throws SQLException
     */
    public int  insertarExamenFisico(Connection con, int nroPeticion, UsuarioBasico usuario) throws SQLException
    {
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (preanestesiaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos ( insertarExamenFisico )");
		}
		//Iniciamos la transacción
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);
		

		//-Insercion de Tipos Parametrizables de los CAmpos de Tipo Text 
		Vector codigos =(Vector) this.getMapaExamenFisico().get("codigosExamen");
		Vector esInsertar =(Vector) this.getMapaExamenFisico().get("esInsertar");
		
		for(int i=0; i < codigos.size(); i++)
		{
			
			String valor = (String) this.getMapaExamenFisico().get("exa_"+codigos.elementAt(i));
			
			if (esInsertar.elementAt(i).equals("1"))
			{
				if(UtilidadCadena.noEsVacio(valor))
				{
					int cod = Integer.parseInt(codigos.elementAt(i)+"");
					
					resp1=preanestesiaDao.insertarExamenFisico(con, 0, cod, nroPeticion, "", valor);
					if (!inicioTrans||resp1<1)
					{
					    myFactory.abortTransaction(con);
						return -1;
					}
					else
					{
					    myFactory.endTransaction(con);
					}
				}	//if no es vacio
			}//if es insertar
		}
		
		//-Insercion de Tipos Parametrizables de los CAmpos de Tipo Text Area 
		Vector codigosArea =(Vector) this.getMapaExamenFisico().get("codigosExamenArea");
		
		for(int i=0; i < codigosArea.size(); i++)
		{		
			int cod = Integer.parseInt(codigosArea.elementAt(i)+"");

			String valor = (String) this.getMapaExamenFisico().get("exa_Area_"+cod);
			String valorNuevo = (String) this.getMapaExamenFisico().get("exa_Area_nuevo_"+cod);
			
			//-----Adicionar los Datos a Los datos previamente guardados
			if (UtilidadCadena.noEsVacio(valorNuevo))   
			{
				if (UtilidadCadena.noEsVacio(valor)) //--- Se Debe Modificar  
				{
					valor = UtilidadTexto.agregarTextoAObservacion(valor, valorNuevo, usuario, false);
					resp1=preanestesiaDao.insertarExamenFisico(con, 3, cod, nroPeticion, "", valor);
				}
				else //---Se debe insertar 
				{
					valor = UtilidadTexto.agregarTextoAObservacion(null, valorNuevo, usuario, false);
					resp1=preanestesiaDao.insertarExamenFisico(con, 1, cod, nroPeticion, "", valor);
				}
			}
			else{ resp1 = 1; }
			
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

		
		//-Insertar los Nuevos Examenes Fisico insertdos por el usuario
		String codigosOtros = (String) this.getMapaExamenFisico().get("codOtrosExamFis");
		
		if (UtilidadCadena.noEsVacio(codigosOtros))
		{
			for (int j = 1; j <= Integer.parseInt(codigosOtros); j++)
			{
				String nombre = (String) this.getMapaExamenFisico().get("nombreOtroExamFisico_"+j);
				String valor = (String) this.getMapaExamenFisico().get("otroExamFisico_"+j);
				
				//---Insertar Registro Por Registro 
				resp1=preanestesiaDao.insertarExamenFisico(con, 2, j, nroPeticion, nombre, valor);
				
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
		}	
		
		//-si todos los registros se insertaron correctamente  
		return 1;	
    }

		/**
	 * Método para insertar las conclusiones de la preanestesia
	 * @param con una conexion abierta con una fuente de datos
	 * @param peticionQx
	 * @return true si se insertó con exito todo sino retorna false
	 * @throws SQLException
	 */
	public boolean insertarConclusionesPreanestesia (Connection con, int peticionQx) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		boolean error=false;
		int valPreanestesia=0;
		
		if (preanestesiaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (PreanestesiaDao - insertarConclusionesPreanestesia )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if(this.getMapaConclusion("codigosConclusiones") != null)
		{
			Vector codigosConclu=(Vector) this.getMapaConclusion("codigosConclusiones");
			
			for (int i=0; i<codigosConclu.size(); i++)
			{
				int conclusionPreInst=Integer.parseInt(codigosConclu.elementAt(i)+"");
				
				//Valor de la conclusion
				String valorConclusion=(String)this.getMapaConclusion("conclusion_"+conclusionPreInst);
				
				if(valorConclusion != null && !valorConclusion.trim().equals(""))
				{
					//Se inserta la conclusión parametrizada
					valPreanestesia = preanestesiaDao.insertarConclusionPreanestesia(con, peticionQx, conclusionPreInst, valorConclusion);
					
					if (valPreanestesia < 1)
					{
						error=true;
						break;
					}
				}//if valorConclusion!=null
			}//for
		}//if codigosConclusiones != null
		
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
	
	 /**
     * Metodo para consultar la información general de la petición de cirugía 
     * @param con
     * @param peticionQx
     * @return true si existe una petición sino retorna false
     */
	public boolean cargarInfoPeticion (Connection con, int nroPeticion)
	{
		Collection coleccion=null;
		
		try
		{
			coleccion = preanestesiaDao.cargarInfoPeticion (con, nroPeticion);
			Iterator ite=coleccion.iterator();
			
			if(ite.hasNext())
				{
				HashMap col=(HashMap) ite.next();
					this.peticionCirugia = Integer.parseInt(col.get("codigo_peticion")+"");
					this.fechaPeticion = (col.get("fecha_peticion")+"").equals("null")  ? "" : (col.get("fecha_peticion")+"");
					this.horaPeticion = (col.get("hora_peticion")+"").equals("null")  ? "" : (col.get("hora_peticion")+"");
					this.codigoEstadoPeticion = Integer.parseInt(col.get("cod_estado_peticion")+"");
					this.nombreEstadoPeticion = (col.get("nom_estado_peticion")+"").equals("null")  ? "" : (col.get("nom_estado_peticion")+"");
					this.fechaCirugia = (col.get("fecha_cirugia")+"").equals("null")  ? "" : (col.get("fecha_cirugia")+"");
					this.duracionCirugia = (col.get("duracion_cirugia")+"").equals("null")  ? "" : (col.get("duracion_cirugia")+"");
					this.nombreSolicitante = (col.get("nombre_solicitante")+"").equals("null")  ? "" : (col.get("nombre_solicitante")+"");
					
					return true;
				}
			else
				{
				 return false;	
				}
		}//try
		catch(Exception e)
		{
		  logger.warn("Error al Consultar la Información general de la petición " +e.toString());
		  coleccion=null;
		}
	 return false;	
	}
//	-------------------------------------------------------------SETS Y GETS----------------------------------------------------------------//

	/**
	 * @return Returns the peticionCirugia.
	 */
	public int getPeticionCirugia()
	{
		return peticionCirugia;
	}
	/**
	 * @param peticionCirugia The peticionCirugia to set.
	 */
	public void setPeticionCirugia(int peticionCirugia)
	{
		this.peticionCirugia = peticionCirugia;
	}
		
	/**
	 * @return Returns the fechaPreanestesia.
	 */
	public String getFechaPreanestesia()
	{
		return fechaPreanestesia;
	}
	/**
	 * @param fechaPreanestesia The fechaPreanestesia to set.
	 */
	public void setFechaPreanestesia(String fechaPreanestesia)
	{
		this.fechaPreanestesia = fechaPreanestesia;
	}
	/**
	 * @return Returns the horaPreanestesia.
	 */
	public String getHoraPreanestesia()
	{
		return horaPreanestesia;
	}
	/**
	 * @param horaPreanestesia The horaPreanestesia to set.
	 */
	public void setHoraPreanestesia(String horaPreanestesia)
	{
		this.horaPreanestesia = horaPreanestesia;
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
	
	
	
	/**
	 * @return Returns the mapaConclusion.
	 */
	public HashMap getMapaConclusionCompleto()
	{
		return mapaConclusion;
	}
	/**
	 * @param mapaConclusion The mapaConclusion to set.
	 */
	public void setMapaConclusionCompleto(HashMap mapaConclusion)
	{
		this.mapaConclusion = mapaConclusion;
	}
	
	/**
	 * @return Returna la propiedad del mapa mapaConclusion.
	 */
	public Object getMapaConclusion(String key)
	{
		return mapaConclusion.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapaConclusion
	 */
	public void setMapaConclusion(String key, Object value)
	{
		this.mapaConclusion.put(key, value);
	}
//	-------------------------------------------------------------------------------------------------------//
	
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
	 * @return Retorna mapaExamenFisico.
	 */
	public HashMap getMapaExamenFisico() {
		return mapaExamenFisico;
	}
	/**
	 * @param Asigna mapaExamenFisico.
	 */
	public void setMapaExamenFisico(HashMap mapaExamenFisico) {
		this.mapaExamenFisico = mapaExamenFisico;
	}
	
	/**
	 * @return Returna la propiedad del mapa mapa.
	 */
	public Object getMapaExamenFisico(String key)
	{
		return mapaExamenFisico.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapa
	 */
	public void setMapaExamenFisico(String key, Object value)
	{
		this.mapaExamenFisico.put(key, value);
	}
	/**
	 * @return Retorna mapa.
	 */
	public HashMap getMapa() {
		return mapa;
	}
	/**
	 * @param Asigna mapa.
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
	}
	/**
	 * @return Retorna mapaConclusion.
	 */
	public HashMap getMapaConclusion() {
		return mapaConclusion;
	}
	/**
	 * @param Asigna mapaConclusion.
	 */
	public void setMapaConclusion(HashMap mapaConclusion) {
		this.mapaConclusion = mapaConclusion;
	}
	
	/**
	 * @return Returns the codigoEstadoPeticion.
	 */
	public int getCodigoEstadoPeticion()
	{
		return codigoEstadoPeticion;
	}
	/**
	 * @param codigoEstadoPeticion The codigoEstadoPeticion to set.
	 */
	public void setCodigoEstadoPeticion(int codigoEstadoPeticion)
	{
		this.codigoEstadoPeticion = codigoEstadoPeticion;
	}
	/**
	 * @return Returns the duracionCirugia.
	 */
	public String getDuracionCirugia()
	{
		return duracionCirugia;
	}
	/**
	 * @param duracionCirugia The duracionCirugia to set.
	 */
	public void setDuracionCirugia(String duracionCirugia)
	{
		this.duracionCirugia = duracionCirugia;
	}
	/**
	 * @return Returns the fechaCirugia.
	 */
	public String getFechaCirugia()
	{
		return fechaCirugia;
	}
	/**
	 * @param fechaCirugia The fechaCirugia to set.
	 */
	public void setFechaCirugia(String fechaCirugia)
	{
		this.fechaCirugia = fechaCirugia;
	}
	/**
	 * @return Returns the fechaPeticion.
	 */
	public String getFechaPeticion()
	{
		return fechaPeticion;
	}
	/**
	 * @param fechaPeticion The fechaPeticion to set.
	 */
	public void setFechaPeticion(String fechaPeticion)
	{
		this.fechaPeticion = fechaPeticion;
	}
	/**
	 * @return Returns the horaPeticion.
	 */
	public String getHoraPeticion()
	{
		return horaPeticion;
	}
	/**
	 * @param horaPeticion The horaPeticion to set.
	 */
	public void setHoraPeticion(String horaPeticion)
	{
		this.horaPeticion = horaPeticion;
	}
	/**
	 * @return Returns the nombreEstadoPeticion.
	 */
	public String getNombreEstadoPeticion()
	{
		return nombreEstadoPeticion;
	}
	/**
	 * @param nombreEstadoPeticion The nombreEstadoPeticion to set.
	 */
	public void setNombreEstadoPeticion(String nombreEstadoPeticion)
	{
		this.nombreEstadoPeticion = nombreEstadoPeticion;
	}
	/**
	 * @return Returns the nombreSolicitante.
	 */
	public String getNombreSolicitante()
	{
		return nombreSolicitante;
	}
	/**
	 * @param nombreSolicitante The nombreSolicitante to set.
	 */
	public void setNombreSolicitante(String nombreSolicitante)
	{
		this.nombreSolicitante = nombreSolicitante;
	}

	/**
	 * @return Retorna tipoAnestesia.
	 */
	public int getTipoAnestesia() {
		return tipoAnestesia;
	}

	/**
	 * @param Asigna tipoAnestesia.
	 */
	public void setTipoAnestesia(int tipoAnestesia) {
		this.tipoAnestesia = tipoAnestesia;
	}
	
//	-------------------------------------------------------FIN DE SETS Y GETS-------------------------------------------------------------//
	}
