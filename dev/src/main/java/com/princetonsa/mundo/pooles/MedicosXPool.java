
/*
 * Creado   29/11/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.mundo.pooles;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.actionform.pooles.MedicosXPoolForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.MedicosXPoolDao;

/**
 * Clase para manejar la información de los médicos
 * que van a conformar un <code>Pool</code>.
 *
 * @version 1.0, 29/11/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class MedicosXPool 
{
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(MedicosXPoolForm.class);
	
    /**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
   private static MedicosXPoolDao medicoPoolDao;
   
   /**
    * Código del Médico
    */
   private int codigoMedico;
   
   /**
    * Código del pool 
    */
   private int codigoPool;
   
   /**
    * Fecha de ingreso al pool
    */
   private String fechaIngreso;
   
   /**
    * Hora de ingreso al pool
    */
   private String horaIngreso;
   
   /**
    * Fecha de retiro del pool
    */
   private String FechaRetiro;
   
   /**
    * hora de retiro del pool
    */
   private String horaRetiro;
   
   /**
    * porcentaje de participación del médico en el pool
    */
   private double porcentaje;
   
   /**
    * limpiar e inicializar atributos de la clase
    *
    */
   public void reset ()
   {
       this.codigoMedico = 0;
       this.codigoPool = 0;
       this.fechaIngreso = "";
       this.horaIngreso = "";
       this.FechaRetiro = "";
       this.horaRetiro = "";
       this.porcentaje = 0.0;
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
			    medicoPoolDao = myFactory.getMedicosXPoolDao();
				wasInited = (medicoPoolDao != null);
			}
			return wasInited;
	}
  
  /**
   * Constructor de la clase
   *
   */
  public MedicosXPool ()
  {
      this.reset ();  
      this.init (System.getProperty("TIPOBD"));
  }

  /**
	 * Metodo que realiza la consulta de uno ó varios registros médicos por pool.
	 * @param con, Connection con la fuente de datos.
	 * @param codigoPool, Codigo del pool.
	 * @param consultaUno, Boolean indica que tipo de consulta se realiza.
	 * @return map, HashMap con el resultado.
	 * @see com.princetonsa.dao.sqlbase.SqlBaseParamInstitucionDao#consultarMedicosPool(java.sql.Connection,int,boolean)
	 */
 public HashMap consultarMedicosPool (Connection con,int codigoPool,boolean consultaUno)
 {
    medicoPoolDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMedicosXPoolDao();
    HashMap map= null;
     try 
	    {
         String[] colums={
		            "codigo", 
		            "pool", 
		            "medico", 
		            "fechaIngreso",
		            "horaIngreso",
		            "fechaRetiro",
		            "horaRetiro",
		            "porcentaje",
		            "primerNombre",
		            "segundoNombre",
		            "primerApellido",
		            "segundoApellido"
		            };
         map = UtilidadBD.resultSet2HashMap(colums,medicoPoolDao.consultaPooles(con,codigoPool,consultaUno),true,true).getMapa();
		} 
	    catch (Exception e) 
	    {
			logger.warn("Error mundo MedicosXPool con el codigo->"+codigoPool+" "+e.toString());
			map = null;
		}
	    
     return map;
 }
 
 /**
  * 
  * @param con
  * @param fechaIngreso
  * @param fechaRetiro
  * @param medico
  * @param pool
  * @param porcentaje
  * @return
  */
 public HashMap consultarAvanzada (Connection con,
         											String fechaIngreso,
													String fechaRetiro,
													int medico, 
													int pool, 
													double porcentaje)
 {
    medicoPoolDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMedicosXPoolDao();
    HashMap map= null;
     try 
	    {
         String[] colums={
		            "codigo", 
		            "pool", 
		            "medico", 
		            "fechaIngreso",
		            "horaIngreso",
		            "fechaRetiro",
		            "horaRetiro",
		            "porcentaje",
		            "primerNombre",
		            "segundoNombre",
		            "primerApellido",
		            "segundoApellido"
		            };
         
                 
         map = UtilidadBD.resultSet2HashMap(colums,medicoPoolDao.consultaPoolesAvanzada(con,
                 UtilidadFecha.conversionFormatoFechaABD(fechaIngreso),
                 UtilidadFecha.conversionFormatoFechaABD(fechaRetiro),
                 medico,
                 pool,
                 porcentaje),true,true).getMapa();
		} 
	    catch (Exception e) 
	    {
			logger.warn("Error mundo MedicosXPool con el codigo->"+codigoPool+" "+e.toString());
			map = null;
		}
	    
     return map;
 }
  
/**
 * 
 * @param con
 * @param fechaIngreso
 * @param fechaRetiro
 * @param horaIngreso
 * @param horaRetiro
 * @param medico
 * @param pool
 * @param porcentaje
 * @return
 */
 public boolean insertarDatos (Connection con, 
         								 String fechaIngreso,
								         String fechaRetiro,
								         String horaIngreso,
								         String horaRetiro,
								         int medico, 
								         int pool, 
								         double porcentaje) 
 {
     boolean siInserto = false;
     
     if (medicoPoolDao == null) 
		{
			new SQLException(	"No se pudo inicializar la conexión con la fuente de datos (MedicosXPoolDao - insertarDatos)");
		}
     
     	siInserto = medicoPoolDao.insertarMedicoPool( con, 
			         								  UtilidadFecha.conversionFormatoFechaABD(fechaIngreso),
			         								  UtilidadFecha.conversionFormatoFechaABD(fechaRetiro),
											          horaIngreso,
											          horaRetiro,
											          medico, 
											          pool, 
											          porcentaje);
     
     	if (!siInserto) 
		{
			logger.warn("Error insertando medicos por pool (MedicosXPoolDao)");
			return false;
		}
     
     return siInserto;
 }
 
 /**
	 * Inserta ó modifica los datos de uno ó varios médicos por pool
	 * @param con, Connection con la fuente de datos
	 * @param fechaIngreso, fecha de ingreso del medico al pool
	 * @param fechaRetiro, fecha de retiro del medico al pool
	 * @param horaIngreso, hora de ingreso del medico al pool
	 * @param horaRetiro, hora de retiro del medico al pool
	 * @param medico, código del médico
	 * @param pool, código del pool
	 * @param porcentaje, porcentaje de participación
	 * @param esInsertar, boolean true si es insertar, false si es modificar
	 * @return siInserto, boolean true efectivo, false de lo contrario
	 * @throws SQLException
	 * @see com.princetonsa.dao.SqlBaseMedicosXPoolDao#insertarMedicoPool(java.sql.Connection,String,String,String,String,int,int,double)
*/
 public boolean InsertarModificar (Connection con, 
												 String fechaIngreso,
										         String fechaRetiro,
										         String horaIngreso,
										         String horaRetiro,
										         int medico, 
										         int pool, 
										         double porcentaje,
										         boolean esInsertar) throws SQLException
 {
     boolean siInserto = false;
     boolean siModifico = false;
     DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
     
     if (medicoPoolDao == null) 
		{
			throw new SQLException(	"No se pudo inicializar la conexión con la fuente de datos (MedicosXPoolDao - InsertarModificar )");
		}
     
//   ****Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;

		if (medicoPoolDao.equals(ConstantesBD.inicioTransaccion)) 
		{
			inicioTrans = myFactory.beginTransaction(con);
		} 
		else 
		{
			inicioTrans = true;
		}
		
		if(esInsertar)
		{
			siInserto = medicoPoolDao.insertarMedicoPool( con, 
					  UtilidadFecha.conversionFormatoFechaABD(fechaIngreso),
					  UtilidadFecha.conversionFormatoFechaABD(fechaRetiro),
			          horaIngreso,
			          horaRetiro,
			          medico, 
			          pool, 
			          porcentaje);
			
		
			if (!inicioTrans || !siInserto) 
			{
			    myFactory.abortTransaction(con);
				return false;
			}
			
			return siInserto;
		}
		else if(!esInsertar)
		{
		    siModifico =  medicoPoolDao.modificar(con,
											            medico,
											            pool,
											            UtilidadFecha.conversionFormatoFechaABD(fechaIngreso),
											            UtilidadFecha.conversionFormatoFechaABD(fechaRetiro),
											            porcentaje);
		    
		    if (!inicioTrans || !siModifico) 
			{
		        myFactory.abortTransaction(con);
				return false;
			}
			
			return siModifico;
		}
		
		myFactory.endTransaction(con);
		return false;
 }
 
 
/**
 * @return Retorna codigoMedico.
 */
public int getCodigoMedico() {
    return codigoMedico;
}
/**
 * @param codigoMedico Asigna codigoMedico.
 */
public void setCodigoMedico(int codigoMedico) {
    this.codigoMedico = codigoMedico;
}
/**
 * @return Retorna codigoPool.
 */
public int getCodigoPool() {
    return codigoPool;
}
/**
 * @param codigoPool Asigna codigoPool.
 */
public void setCodigoPool(int codigoPool) {
    this.codigoPool = codigoPool;
}
/**
 * @return Retorna fechaIngreso.
 */
public String getFechaIngreso() {
    return fechaIngreso;
}
/**
 * @param fechaIngreso Asigna fechaIngreso.
 */
public void setFechaIngreso(String fechaIngreso) {
    this.fechaIngreso = fechaIngreso;
}
/**
 * @return Retorna fechaRetiro.
 */
public String getFechaRetiro() {
    return FechaRetiro;
}
/**
 * @param fechaRetiro Asigna fechaRetiro.
 */
public void setFechaRetiro(String fechaRetiro) {
    FechaRetiro = fechaRetiro;
}
/**
 * @return Retorna horaIngreso.
 */
public String getHoraIngreso() {
    return horaIngreso;
}
/**
 * @param horaIngreso Asigna horaIngreso.
 */
public void setHoraIngreso(String horaIngreso) {
    this.horaIngreso = horaIngreso;
}
/**
 * @return Retorna horaRetiro.
 */
public String getHoraRetiro() {
    return horaRetiro;
}
/**
 * @param horaRetiro Asigna horaRetiro.
 */
public void setHoraRetiro(String horaRetiro) {
    this.horaRetiro = horaRetiro;
}
/**
 * @return Retorna porcentaje.
 */
public double getPorcentaje() {
    return porcentaje;
}
/**
 * @param porcentaje Asigna porcentaje.
 */
public void setPorcentaje(double porcentaje) {
    this.porcentaje = porcentaje;
}
}
