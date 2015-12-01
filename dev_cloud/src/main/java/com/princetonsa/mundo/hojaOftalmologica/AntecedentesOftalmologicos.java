/*
 * Creado el 23-sep-2005
 * por Julian Montoya
 */
package com.princetonsa.mundo.hojaOftalmologica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import org.apache.log4j.Logger;
import util.UtilidadCadena;
import com.princetonsa.dao.AntecedentesOftalmologicosDao;
import com.princetonsa.dao.DaoFactory;

/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
@SuppressWarnings("rawtypes")
public class AntecedentesOftalmologicos {

	/**
	 * Para hacer logs de esta funcionalidad.
	*/
	private Logger logger = Logger.getLogger(HojaOftalmologica.class);
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private AntecedentesOftalmologicosDao antecedentesOftalmologicosDao = null;
  
	
	//-----------------Sección Antecedentes Personales Oftalmologicos  
	
	/**
	 * Campo para almacenar Información sobre otra
	 * enfermedad oftalmologica digitada por el usuario 
	 */
	private String otroEnferPerso;
	
	/**
	 * Campo que permite registrar desde cuando se sufre
	 * la enfermedad descrita en otroEnferPerso
	 */
	private String otroDesdeCuando;
	
	/**
	 * Campo que permite registrar el tratamiento
	 * descrita en otroEnferPerso.
	 */
	private String otroTratamiento;


	/**
    * Mapa sirve para guardar datos de los listados 
    * de los tipos de enfermedades Oftalmologicas 
    */
	   
	private HashMap mapa=new HashMap();

	/**
	 * Campo para guardar las observaciones 
	 * medicas de los antecedentes personales
	 * medicos.   
	 */
	private String observacionesPersonales;
	
	/**
	 * Campo para guardar las nuevas observaciones 
	 * medicas de los antecedentes personales medicos.   
	 */
	private String observacionesPersonalesNueva;
	
	
	/** Campo para guardar las observaciones 
	 *  medicas de los antecedentes familiares
	 *  medicos.   
	 */
	private String observacionesFamiliares;
	
	/**
	 * Campo para guardar las nuevas observaciones 
	 * medicas de los antecedentes familiares medicos.   
	 */
	private String observacionesFamiliaresNueva;
	

	
   /**
 	 * Funcion para retornar los tipos de enfermedes oftalmologicas personales y del paciente especifico
 	 * @param con
 	 * @param codigoPaciente
 	 * @param codigoInstitucion
 	 * @return Coleccion con el listado de tipos de enfermedades personales parametrizadas
 	 */
     
      public Collection consultarTiposEnferOftalPersonales(Connection con, int codigoInstitucion, int paciente) 
      {
 		AntecedentesOftalmologicosDao  antecedentesOftalmologicosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAntecedentesOftalmologicosDao(); 

 		Collection coleccion=null;
 		try
 		{	
 		   coleccion = antecedentesOftalmologicosDao.consultarTiposEnferOftalPersonales(con,codigoInstitucion, paciente);
 		}
 		catch(Exception e)
 		{
 		  logger.warn("Error al consultar los tipos de enfermedes oftalmologicas personales  " +e.toString());
 		  coleccion=null;
 		}
 		return coleccion;		
 	 }

	   /**
	   	 * Funcion para retornar los tipos de enfermedes oftalmologicas familiares 
	   	 * @param con
	   	 * @param codigoPaciente
	   	 * @param codigoInstitucion
	   	 * @return Coleccion con el listado de tipos de enfermedades personales parametrizadas
	   	 */
	       
        public Collection consultarTiposEnferOftalfamiliares (Connection con, int codigoInstitucion, int paciente) 
        {
   		AntecedentesOftalmologicosDao  antecedentesOftalmologicosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAntecedentesOftalmologicosDao(); 

   		Collection coleccion=null;
   		try
   		{	
   		   coleccion = antecedentesOftalmologicosDao.consultarTiposEnferOftalfamiliares(con,codigoInstitucion, paciente);
   		}
   		catch(Exception e)
   		{
   		  logger.warn("Error al consultar los tipos de enfermedes oftalmologicas Familiares " +e.toString());
   		  coleccion=null;
   		}
   		return coleccion;		
   	 }
        
        /**
         * Metodo para consultar los tipos de parentescos registrados  
         * @param con
         * @return
         */

        public Collection consultarTiposParentesco (Connection con) 
        {
   		AntecedentesOftalmologicosDao  antecedentesOftalmologicosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAntecedentesOftalmologicosDao(); 

   		Collection coleccion=null;
   		try
   		{	
   		   coleccion = antecedentesOftalmologicosDao.consultarTiposParentesco(con);
   		}
   		catch(Exception e)
   		{
   		  logger.warn("Error al consultar los tipos de enfermedes oftalmologicas Familiares " +e.toString());
   		  coleccion=null;
   		}
   		return coleccion;		
   	 }

    
      
  /**
   * Funcion para insertar los tipos de enfermedades oftalmológicas para un determinado paciente  
   * @param con
   * @param paciente
   */
      
  	public int insertarEnferOftalPersoMedicos(Connection con, int paciente) throws SQLException
	{
		boolean inicioTrans;
		int  resp1=0;

		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		
		if (antecedentesOftalmologicosDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (antecedentesOftalmologicosDao - insertarEnferOftalPersoMedicos )");
		}

		
		inicioTrans=myFactory.beginTransaction(con);
		
		//--Barrer el HashMap para insertar los datos seleccionados   
		Vector codigos = (Vector) this.getMapa("codigosTipoEnferOftal");
		
		//--Vector que indicar si los registro se van a actualizar o se van a insertar   
		Vector actualizar = (Vector) this.getMapa("actualizar");
		
		//-Para saber si esta insertando informacion de las enfermedades digitadas por el usuario
		Vector indicadores = (Vector) this.getMapa("indicadores"); 
		
		//-Estos son los check's que se van a insertar a la base de datos  
		String  ck = (String) this.getMapa("enferCheck"); 
		String  chNv = (String) this.getMapa("enferCheckOtro"); 

		String vectorCk [] = ck.split(",");
		String vectorCkNv [] = chNv.split(",");
		
		
			for(int i=0; i<codigos.size();i++)
			{ 
				//------El codigo del tipo de enfermedad oftalmologica 
				int tipoEnfer = Integer.parseInt(codigos.elementAt(i)+"");
				int act = Integer.parseInt(actualizar.elementAt(i)+"");
				int ind = Integer.parseInt(indicadores.elementAt(i)+"");
				
				String desdeCuando = (String)this.getMapa("desdeCuando_"+tipoEnfer);
				String tratamiento = (String)this.getMapa("tratamiento_"+tipoEnfer);


				//-Se insertara sobre la tabla ant_oftal_perso_medicos
				if (ind == 0)
				{
					//--Solo insertar las filas que tienen el check Activado....
					if ( estaElemento(vectorCk, tipoEnfer) )
					  {
						if (act == 0) //-Se va a insertar el registro
						{
							resp1 = antecedentesOftalmologicosDao.insertarEnferOftalPersoMedicos(con, paciente, tipoEnfer, desdeCuando, tratamiento, act);
						}
						else
						{
							if ( UtilidadCadena.noEsVacio(desdeCuando) || UtilidadCadena.noEsVacio(tratamiento)  )
							  {
								resp1 = antecedentesOftalmologicosDao.insertarEnferOftalPersoMedicos(con, paciente, tipoEnfer, desdeCuando, tratamiento, act);
							  }	
						}
			  	  	  }
				}
				else  //--Se insertara sobre la tabla ant_oftal_perso_med_otro
				{
						if  ( UtilidadCadena.noEsVacio(desdeCuando) || UtilidadCadena.noEsVacio(tratamiento)  )						{
							resp1 = antecedentesOftalmologicosDao.insertarOtraEnferOftalDet(con, tipoEnfer, paciente, desdeCuando, tratamiento, act); //-Act indica que se quiere hacer 0 insertar 1 modificar
						}	
				}
			} 
			
		//--------insertar las otras enfermedades registradas por el usuario
		String codOtro = (String)this.getMapa("codigosOtrosEnfer");	
		if ( UtilidadCadena.noEsVacio(codOtro) )
		{
			int otroCod = Integer.parseInt(codOtro);
	        for(int i=1; i<=otroCod; i++)
			{
	        	String enfer = (String) this.getMapa("otrosEnfer_" + i);
	        	String otroDesde = (String) this.getMapa("otroDesde_" + i);
	        	String otroTrata = (String) this.getMapa("otroTrata_" + i);
	
	          //-Insertar el tipo de enfermedad primero en la tabla OTROS (otro_enfer_oftal)
	          //--Solo insertar las filas que tienen el check Activado.... 
			  if ( estaElemento(vectorCkNv, i) && UtilidadCadena.noEsVacio(enfer))
			    {
	        		int c = antecedentesOftalmologicosDao.insertarOtraEnferOftal(con, enfer);
					resp1 = antecedentesOftalmologicosDao.insertarOtraEnferOftalDet(con, c, paciente, otroDesde, otroTrata, 0); //-el cero es para indicar que se quiere insertar
	        	}
        	}
		}    
        
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
  	 * @param vectorCk
  	 * @param tipoEnfer
  	 * @return
  	 */
  	private boolean estaElemento(String[] vectorCk, int tipoEnfer)
  	{
  		for (int i = 0; i < vectorCk.length; i++) 
  		{
  			if (!vectorCk[i].trim().equals(""))
  			if ( Integer.parseInt(vectorCk[i]) == tipoEnfer )
  			  return true;
		}
	
  		return false;
  	}

	/**
  	 * Metodo para insertar los antecedentes Oftalmologicos personales quirurgicos  
	 * @param con
	 * @param paciente
  	 * @throws SQLException
	 */
	public int insertarEnferOftalPersoQuirur(Connection con, int paciente) throws SQLException
	{
		boolean inicioTrans;
		int  resp1=0;
		
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		
		if (antecedentesOftalmologicosDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (antecedentesOftalmologicosDao - insertarEnferOftalPersoQuirur )");
		}
		
		inicioTrans=myFactory.beginTransaction(con);
		
		//--Obtener los codigos de los procedimientos que se an registrado con anterioridad   
		Vector codigosReg = (Vector) this.getMapa("codigosReg");
		Vector modificarReg = (Vector) this.getMapa("modificarReg");
		
			for(int i=1;i<=codigosReg.size();i++)
			{ 
				//------El codigo del procedimiento 
				int tipoProc = Integer.parseInt(codigosReg.elementAt(i-1)+"");
				int hayMod = Integer.parseInt(modificarReg.elementAt(i-1)+"");
				
				//-Verificar que el registro tenga campos que se iran a  modificar
				if (hayMod==0)
				{
					String proce = (String)this.getMapa("nombreQuirur_"+tipoProc);
					String fecha = (String)this.getMapa("fechaQuirur_"+tipoProc);
					String causa = (String)this.getMapa("causaQuirur_"+tipoProc);
				
					//--Solo insertar las que tienen la información (en el campo desde cuando)
					//-El uno al final indica que se va a modificar un procedimiento
					resp1 = antecedentesOftalmologicosDao.insertarEnferOftalPersoQuirur(con, tipoProc, paciente,  proce, fecha, causa, 1);
				}	
			} 
			
		//----insertar las otras enfermedades registradas por el usuario---
		String codOtro = (String)this.getMapa("codigosOtrosQuirur");	
		if ( UtilidadCadena.noEsVacio(codOtro) )
		{
			int otroCod = Integer.parseInt(codOtro);
	        for(int i=1; i<=otroCod; i++)
			{
				String proce = (String)this.getMapa("otroNombreQuirur_"+i);
				String fecha = (String)this.getMapa("otroFechaQuirur_"+i);
				String causa = (String)this.getMapa("otroCausaQuirur_"+i);
	
				//--Insertar Información si el nombre del procedimiento no esta vacio 
				if ( UtilidadCadena.noEsVacio(proce) )
				  {
					//-El cero al final indica que se va a insertar nuevos procedimientos
					resp1 = antecedentesOftalmologicosDao.insertarEnferOftalPersoQuirur(con, i, paciente,  proce, fecha, causa, 0);
				  }
			}
		}    
        
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
  	 * Metodo para insertar los antecedentes Oftalmologicos familiares   
	 * @param con
	 * @param paciente
  	 * @throws SQLException
	 */
	public int insertarEnferOftalFamDetalle(Connection con, int paciente) throws SQLException 
	{
		boolean inicioTrans;
		int  resp1=0, respuesta=0;
		
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		
		if (antecedentesOftalmologicosDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (antecedentesOftalmologicosDao - insertarEnferOftalFamDetalle )");
		}
		
		inicioTrans=myFactory.beginTransaction(con);
		
		//--Obtener los codigos de los procedimientos que se han registrado con anterioridad   
		Vector codigosEnfer = (Vector) this.getMapa("codigosEnferFam");
		Vector codigosFam = (Vector) this.getMapa("codigosFamDet");
		Vector indicadores = (Vector) this.getMapa("indicadores");
		
			for(int i=0;i<codigosEnfer.size();i++)
			{ 
				
				//------Verificar si
				int cod = Integer.parseInt(codigosEnfer.elementAt(i)+"");
				int codFam = Integer.parseInt(codigosFam.elementAt(i)+""); 
				int codInd = Integer.parseInt(indicadores.elementAt(i)+""); 
				
				String proce = (String)this.getMapa("codEnfer_" + cod + "-" + codInd);
				String proceMod = (String)this.getMapa("codEnferMod_" + cod + "-" + codInd);
				
				
				
				if ( UtilidadCadena.noEsVacio(proce) )
				{
					String [] vec = proce.split("-");
					String [] vecMod = proceMod.split("-");
					

					//-insertar en el detalle ...
					if (codFam == -1)
					{
						resp1 = antecedentesOftalmologicosDao.insertarEnferOftalFamDetalle(con, paciente, cod, codFam);
						for (int j = 0; j < vec.length; j++) 
						{ 
						  	respuesta = antecedentesOftalmologicosDao.insertarEnferOftalFamDetallePadece(con, resp1, Integer.parseInt(vec[j]), 0);
						}  	
					}
					else
					{

						for (int j = 0; j < vec.length; j++) 
						{

						  //-para no tratar de insertar los familiares que ya estaban	
						  if ( j > (vecMod.length-1) ) 
						  {

						  	if (codInd == 0) //-Insertar datos en la tabla parametrizada
						  	{	

						  		respuesta = antecedentesOftalmologicosDao.insertarEnferOftalFamDetallePadece(con, codFam, Integer.parseInt(vec[j]), 0);
						  	}
						  	else //-Insertar informacion sobre la tabla de enfermedades digitadas por el usuario
						  	{
						  		respuesta = antecedentesOftalmologicosDao.insertarOtraEnferOftalFamDet(con, codFam, Integer.parseInt(vec[j]));	
						  	}
						  } 
						} 
					}
						
				}
			} 
			
		//----insertar las otras enfermedades registradas por el usuario-----
		String codOtro = (String)this.getMapa("codOtros");
				
		if ( UtilidadCadena.noEsVacio(codOtro) )
		{
			int vecOtros = Integer.parseInt(codOtro);
			for (int i = 1; i <= vecOtros; i++)
			{
	        	//-Insertar la nueva en fermedad
				String enfer = (String) this.getMapa("otrosEnfer_" + (i));
				
				
	        	//-Insertar el tipo de enfermedad primero en la tabla OTROS (otro_enfer_oftal) 	
				if ( UtilidadCadena.noEsVacio(enfer) )
	        	{
	        		String proce = (String)this.getMapa("codOtroEnfer_" + i); //-Los codigos de los Pararentezcos...
	        		
	        		//-Si no selecciono parentezcos en la nueva enfermedad no inserte...
	        		if (!proce.equals(""))
	        		{
						respuesta = antecedentesOftalmologicosDao.insertarOtraEnferOftalFam(con, enfer, paciente);
	
		        		int res = 0;
		        		String [] vecOtro = proce.split("-");
		        		
		        		if ( UtilidadCadena.noEsVacio(proce) )
						{
							for (int j = 0; j < vecOtro.length; j++) 
							{
								//-Insertar el detalle
							    res = antecedentesOftalmologicosDao.insertarOtraEnferOftalFamDet(con, respuesta, Integer.parseInt(vecOtro[j]));
							}
							respuesta = res;
						}	
	        		}
					
	        	}
				
			}
		}  
        
		
		if (!inicioTrans||respuesta<1)
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
  	 * Metodo para insertar informacion en la tabla de los antecedentes personales 
  	 * de un paciente especifico.
  	 * @param con
  	 * @param paciente
  	 * @param codAuxPaciente = parametro que indica si existia antecedentes oftalmologicos para el paciente especifico 
  	 * @return
  	 * @throws SQLException
  	 */
 	public int insertarEnferOftalPersonales(Connection con, int paciente, int codAuxPaciente) throws SQLException
	{
		boolean inicioTrans;
		int  resp1=0;
		

		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (antecedentesOftalmologicosDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (antecedentesOftalmologicosDao - insertarEnferOftalPersonales )");
		}
		inicioTrans=myFactory.beginTransaction(con);
		resp1 = antecedentesOftalmologicosDao.insertarEnferOftalPersonales(con, paciente, this.observacionesPersonales, codAuxPaciente);
		
				
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
  	 * Metodo para insertar informacion en la tabla de los antecedentes familiares  
  	 * de un paciente especifico.
  	 * @param con
  	 * @param paciente
  	 * @param codAuxPaciente = parametro que indica si existia antecedentes oftalmologicos para el paciente especifico 
  	 * @return
  	 * @throws SQLException
  	 */
 	public int insertarEnferOftalFamiliares(Connection con, int paciente, int codAuxPaciente) throws SQLException
	{
		boolean inicioTrans;
		int  resp1=0;
		

		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (antecedentesOftalmologicosDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (antecedentesOftalmologicosDao - insertarEnferOftalFamiliares )");
		}
		inicioTrans=myFactory.beginTransaction(con);
		resp1 = antecedentesOftalmologicosDao.insertarEnferOftalFamiliares(con, paciente, this.observacionesFamiliares, codAuxPaciente);
		
				
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
     * Funcion para verificar si existe datos del paciente en la tabla ant_oftal_personales 
     * (indispensable para insertar datos en la tabla ant_oftal_perso_medicos) 
     * @param con
     * @param paciente : es el codigo del paciente
     * @return
     */
    
    public int verificarAntOftalPersonales(Connection con, int paciente)
    {
    	return antecedentesOftalmologicosDao.verificarAntOftalPersonales(con, paciente);
    }
    
    /**
     * Metodo para cargar las observaciones generales oftalmologicas personales 
     * @param con
     * @param paciente
     * @return
     */
    public String cargarObservaciones(Connection con, int paciente)
    {
    	return antecedentesOftalmologicosDao.cargarObservaciones(con, paciente);
    }
    
    /**
     * Metodo para cargar hora de las observaciones generales oftalmologicas personales 
     * @param con
     * @param paciente
     * @return
     */
    public String cargarHoraObservaciones(Connection con, int paciente)
    {
    	return antecedentesOftalmologicosDao.cargarHoraObservaciones(con, paciente);
    }
    
    /**
     * Metodo para cargar fecha de las observaciones generales oftalmologicas personales 
     * @param con
     * @param paciente
     * @return
     */
    public String cargarFechaObservaciones(Connection con, int paciente)
    {
    	return antecedentesOftalmologicosDao.cargarFechaObservaciones(con, paciente);
    }
    
    /**
     * Metodo para cargar las observaciones generales oftalmologicas familiares 
     * @param con
     * @param paciente
     * @return
     */
    public String cargarObservacionesFamiliares(Connection con, int paciente)
    {
    	return antecedentesOftalmologicosDao.cargarObservacionesFamiliares(con, paciente);
    }
    
    /**
     * Metodo para cargar hora de las observaciones generales oftalmologicas familiares 
     * @param con
     * @param paciente
     * @return
     */
    
    public String cargarHoraObservacionesFamiliares(Connection con, int paciente)
    {
    	return antecedentesOftalmologicosDao.cargarHoraObservacionesFamiliares(con, paciente);
    }
    
    /**
     * Metodo para cargar fecha de las observaciones generales oftalmologicas familiares 
     * @param con
     * @param paciente
     * @return
     */
    
    public String cargarFechaObservacionesFamiliares(Connection con, int paciente)
    {
    	return antecedentesOftalmologicosDao.cargarFechaObservacionesFamiliares(con, paciente);
    }
    
    
    
    
    /**
     * Funcion para verificar si existe datos del paciente en la tabla ant_oftal_familiares  
     * (indispensable para insertar datos en la tabla ant_oftal_fam_detalle) 
     * @param con
     * @param paciente : es el codigo del paciente
     * @return
     */
    
    public int verificarAntOftalFamiliares(Connection con, int paciente)
    {
    	return antecedentesOftalmologicosDao.verificarAntOftalFamiliares(con, paciente);
    }


	/**
	 * Metodo para insertar otro enfermedad oftal digitada por el paciente
	 * @param con 
	 * @throws SQLException
	 */
	public int insertarOtraEnferOftal(Connection con) throws SQLException 
	{
		boolean inicioTrans;
		int  resp1=0;

		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (antecedentesOftalmologicosDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (antecedentesOftalmologicosDao - insertarOtraEnferOftal )");
		}
		inicioTrans=myFactory.beginTransaction(con);
		
		if (this.otroEnferPerso.trim().equals(""))
		{
			resp1 = antecedentesOftalmologicosDao.insertarOtraEnferOftal(con, this.otroEnferPerso);
			if (!inicioTrans||resp1<1)
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
		else
		{
			return 0;
		}
	}
	
	/**
	 * Metodo para insertar la información de detalle de la "otra"  enfermedad oftalmologica
	 * @param con
	 * @param paciente
	 * @param codigo
	 * @throws SQLException
	 */
	public int insertarOtraEnferOftalDet(Connection con, int paciente, int codigo) throws SQLException 
	{
		boolean inicioTrans;
		int  resp1=0;

		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (antecedentesOftalmologicosDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (antecedentesOftalmologicosDao - insertarOtraEnferOftalDet )");
		}
		inicioTrans=myFactory.beginTransaction(con);
		resp1 = antecedentesOftalmologicosDao.insertarOtraEnferOftalDet(con, codigo, paciente, this.otroDesdeCuando, this.otroTratamiento, 0);
				
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
 	 * Metodo para consultar las enfermedades registradas de un paciente especifico   
 	 * @param con
 	 * @param paciente : codigo del paciente
 	 * @return
 	 */
  	public  Collection consutarEnferOftalPersoPaciente(Connection con, int paciente) 
   	{
 		AntecedentesOftalmologicosDao  antecedentesOftalmologicosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAntecedentesOftalmologicosDao(); 

 		Collection coleccion=null;
 		try
 		{	
 		   coleccion = antecedentesOftalmologicosDao.consutarEnferOftalPersoPaciente(con, paciente);
 		}
 		catch(Exception e)
 		{
 		  logger.warn("Error al consultar las enfermedes oftalmologicas personales de un paciente especifico  " +e.toString());
 		  coleccion=null;
 		}
 		return coleccion;		

   	}
	
  	/**
   	 * Metodo para consultar los antecedentes quirurgicos del paciente   
   	 * @param con
   	 * @param paciente : codigo del paciente
   	 * @return
   	 */
  	public Collection consutarEnferOftalPersoQuirurPaciente(Connection con, int paciente) 
   	{
  		AntecedentesOftalmologicosDao  antecedentesOftalmologicosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAntecedentesOftalmologicosDao(); 

 		Collection coleccion=null;
 		try
 		{	
 		   coleccion = antecedentesOftalmologicosDao.consutarEnferOftalPersoQuirurPaciente(con, paciente);
 		}
 		catch(Exception e)
 		{
 		  logger.warn("Error al consultar los antecedentes quirurgicos del paciente " +e.toString());
 		  coleccion=null;
 		}
 		return coleccion;		
   	}
  	
  	
  	/**
  	 * Metodo para consultar las enfermedades familiares registradas anteriormente 
  	 * @param con
  	 * @param paciente
  	 * @return
  	 */
  	public Collection consutarEnferOftalFamPadece(Connection con, int paciente) 
   	{
  		AntecedentesOftalmologicosDao  antecedentesOftalmologicosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAntecedentesOftalmologicosDao(); 

 		Collection coleccion=null;
 		try
 		{	
 		   coleccion = antecedentesOftalmologicosDao.consutarEnferOftalFamPadece(con, paciente);
 		}
 		catch(Exception e)
 		{
 		  logger.warn("Error al consultar los antecedentes oftalmologicos Familiares " +e.toString());
 		  coleccion=null;
 		}
 		return coleccion;		
   	}
	
		/**
	     * Constructor de la clase, inicializa en vacío todos los atributos
		 */
		public AntecedentesOftalmologicos() 
		  {
		  	this.init(System.getProperty("TIPOBD"));
		  }

		/**
		 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
		 * @param tipoBD el tipo de base de datos que va a usar este objeto
		 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
		 * son los nombres y constantes definidos en <code>DaoFactory</code>.
		 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
		 */
		public boolean init(String tipoBD)
		{
			boolean wasInited = false;
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

			if (myFactory != null)
			{
				antecedentesOftalmologicosDao = myFactory.getAntecedentesOftalmologicosDao();
				wasInited = (antecedentesOftalmologicosDao != null);
			}
			return wasInited;
		}
		
		
		/**
		 * @return Returns the mapa.
		 */
		public HashMap getMapaCompleto()
		{
			return mapa;
		}
		/**
		 * @param mapa The mapa to set.
		 */
		public void setMapaCompleto(HashMap mapaAntecedentes)
		{
			this.mapa = mapaAntecedentes;
		}
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
		@SuppressWarnings("unchecked")
		public void setMapa(String key, Object value)
		{
			this.mapa.put(key, value);
		}
	/**
	 * @return Retorna otroDesdeCuando.
	 */
	public String getOtroDesdeCuando() {
		return otroDesdeCuando;
	}
	/**
	 * @param Asigna otroDesdeCuando.
	 */
	public void setOtroDesdeCuando(String otroDesdeCuando) {
		this.otroDesdeCuando = otroDesdeCuando;
	}
	/**
	 * @return Retorna otroEnferPerso.
	 */
	public String getOtroEnferPerso() {
		return otroEnferPerso;
	}
	/**
	 * @param Asigna otroEnferPerso.
	 */
	public void setOtroEnferPerso(String otroEnferPerso) {
		this.otroEnferPerso = otroEnferPerso;
	}
	/**
	 * @return Retorna otroTratamiento.
	 */
	public String getOtroTratamiento() {
		return otroTratamiento;
	}
	/**
	 * @param Asigna otroTratamiento.
	 */
	public void setOtroTratamiento(String otroTratamiento) {
		this.otroTratamiento = otroTratamiento;
	}

	/**
	 * @return Retorna observacionesPersonales.
	 */
	public String getObservacionesPersonales() {
		return observacionesPersonales;
	}
	/**
	 * @param Asigna observacionesPersonales.
	 */
	public void setObservacionesPersonales(String observacionesPersonales) {
		this.observacionesPersonales = observacionesPersonales;
	}
	/**
	 * @return Retorna observacionesPersonalesNueva.
	 */
	public String getObservacionesPersonalesNueva() {
		return observacionesPersonalesNueva;
	}
	/**
	 * @param Asigna observacionesPersonalesNueva.
	 */
	public void setObservacionesPersonalesNueva(
			String observacionesPersonalesNueva) {
		this.observacionesPersonalesNueva = observacionesPersonalesNueva;
	}
	/**
	 * @return Retorna observacionesFamiliares.
	 */
	public String getObservacionesFamiliares() {
		return observacionesFamiliares;
	}
	/**
	 * @param Asigna observacionesFamiliares.
	 */
	public void setObservacionesFamiliares(String observacionesFamiliares) {
		this.observacionesFamiliares = observacionesFamiliares;
	}
	/**
	 * @return Retorna observacionesFamiliaresNueva.
	 */
	public String getObservacionesFamiliaresNueva() {
		return observacionesFamiliaresNueva;
	}
	/**
	 * @param Asigna observacionesFamiliaresNueva.
	 */
	public void setObservacionesFamiliaresNueva(
			String observacionesFamiliaresNueva) {
		this.observacionesFamiliaresNueva = observacionesFamiliaresNueva;
	}
}
