package com.princetonsa.mundo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.Utilidades;

import com.princetonsa.dao.ConsecutivosDisponiblesDao;
import com.princetonsa.dao.DaoFactory;


/**
 * @version 1.0, 29/06/2005
 * @author <a href="mailto:acardona@PrincetonSA.com">Angela Cardona</a>
 * @author [restructurada 1/12/2005] <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class ConsecutivosDisponibles
{
	private Logger logger=Logger.getLogger(ConsecutivosDisponibles.class);

	private boolean checkbox;
	
	private String nombre;
	
	private String descripcion;
	
	private String valor;
	
	private String anioVigencia;
	
	private int institucion;
	/**
	 * código del modulo
	 */
	private int modulo;
	/**
	 * código del almacen
	 */
	private int almacen;
	/**
	 * código de la empresa
	 * */
	private String empresa;
	
	/**
     * para almacenar todos los
     * datos correspondientes al formulario.
     * estados de los datos.
     */
    private HashMap mapConsecutivos;
    /**
     * para almacenar los datos de los
     * consecutivos inventarios en los
     * casos de por almacen o unico en el
     * sistema
     */
    private HashMap mapConsecInv;
    /**
     * almacena el tipo de consecutivo
     * inventario seleccionado, por almacen
     * o unico en el sistema
     */
    private String casoConsecInv;
	
    /**
	 * DAO de este objeto, para trabajar con Servicio en
	 * la fuente de datos
	 */
    private ConsecutivosDisponiblesDao consecutivoDao;
	
	/**
     * Método que limpia este objeto
     */
    public void reset()
    {
    	this.valor="";
        this.anioVigencia="";
        this.checkbox=false;
        this.mapConsecutivos = new HashMap ();
        this.modulo=ConstantesBD.codigoNuncaValido;
        this.almacen=ConstantesBD.codigoNuncaValido;
        this.mapConsecInv=new HashMap ();
        this.casoConsecInv="";
        this.empresa = "";
    }
     
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( consecutivoDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			consecutivoDao= myFactory.getConsecutivosDisponiblesDao();
			if( consecutivoDao!= null )
				return true;
		}
		return false;
	}
	  
	/**
	 * Constructor Vacío
	 */
	public ConsecutivosDisponibles()
	{
		this.reset();	
		this.init(System.getProperty("TIPOBD"));
	}
	
	
   /**
    * Método para insertar/modificar consecutivos disponibles
    * @param con
    * @param nombre   
    * @param valor
    * @param anioVigencia
    * @param institucion
    * @param checkbox
    * @param accion
    * @return boolean    
    */
	public boolean insertarModificarTrans(Connection con, String nombre,String valor, String anioVigencia,int institucion, boolean checkbox, String accion)  
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans=true;
		try 
		{
			if (consecutivoDao==null)
			{
			    throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos");
	        } 
			inicioTrans=myFactory.beginTransaction(con);
			if (!inicioTrans )
			{			    
			    myFactory.abortTransaction(con);
			    logger.warn("Transaction Aborted-No se inicio la transacción");			    
			}
			else
			{
			    if(accion=="insertar")
			    {					
			    	inicioTrans = consecutivoDao.insertar(con, nombre, valor, anioVigencia, institucion);
					if (!inicioTrans) 
					{
						myFactory.abortTransaction(con);
						logger.warn("Abort Transaction");
					}					
				}
			    else if(accion=="modificar")
				{
			    	logger.info("paso por aqui para modificar!!!");
			        inicioTrans = consecutivoDao.modificar(con,nombre,valor,anioVigencia,institucion,checkbox);
					if (!inicioTrans) 
					{
						myFactory.abortTransaction(con);
						logger.warn("Abort Transaction");
					}				
				}
				else if(accion=="insertUpdateConsecInv")
				{
				   HashMap mapa=new HashMap();
				   int numRegistros=Integer.parseInt(this.mapConsecInv.get("numRegistros")==null||(this.mapConsecInv.get("numRegistros")+"").equals("null")?"0":this.mapConsecInv.get("numRegistros")+"");
				   for(int k=0;k<numRegistros;k++)
				   {
				     mapa=consecutivoDao.consultaConsecutivosInventarios(con,this.casoConsecInv,institucion,this.almacen,true,Integer.parseInt(this.mapConsecInv.get("cod_tipo_transaccion_"+k)+""));
				     if( Utilidades.convertirAEntero(mapa.get("numRegistros")+"")==0 )
				     {			             
				         inicioTrans=consecutivoDao.insertarConsecutivoInventarios(con,this.almacen,Integer.parseInt(this.mapConsecInv.get("cod_tipo_transaccion_"+k)+""),institucion,this.casoConsecInv,this.mapConsecInv.get("valor_"+k)+"",this.mapConsecInv.get("anio_vigencia_"+k)+"");
				         if (!inicioTrans)
						 {
							myFactory.abortTransaction(con);
							logger.warn("Abort Transaction");
						 }
				     }
				     else
				     {
				         boolean check=(this.mapConsecInv.get("checkInv_"+k)+"").equals("true")?true:false;
				         inicioTrans=consecutivoDao.modificarConsecutivoInv(con,this.casoConsecInv,this.mapConsecInv.get("valor_"+k)+"",this.mapConsecInv.get("anio_vigencia_"+k)+"",institucion,check,this.almacen,Integer.parseInt(this.mapConsecInv.get("cod_tipo_transaccion_"+k)+""));  
				         if (!inicioTrans) 
						 {
							myFactory.abortTransaction(con);
							logger.warn("Abort Transaction");
						 }
				     }
				   }
				}
			    //Caso para la actualizacion de datos de la multiempresa
				else if(accion.equals("insertarUpdateConsecMultiEmpresa"))
				{
					 inicioTrans=actualizarConsecutivoMultiEmpresa(con,
							 this.mapConsecInv.get("valorConsecutivoFact").toString(),
							 this.mapConsecInv.get("anioVigencia").toString(),
							 institucion,
							 empresa
							 );		
					 
			         if (!inicioTrans) 
					 {
						myFactory.abortTransaction(con);
						logger.warn("Abort Transaction");
					 }
					
				}
			    else
					logger.warn("El tipo de transacción no esta definido[Consecutivos Disponibles]");
				
				if(inicioTrans)
				{
				   myFactory.endTransaction(con);
				   logger.warn("End Transaction");
				}
			}
		}catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    } 		
	    return inicioTrans;
	}

	/**
	 * metodo para consultar los consecutivos 
	 * disponibles por modulo
	 * @param con Connection	
	 * @param codigoInstitucion int
	 * @return HashMap
	 * @author jarloc
	 */
	public HashMap consultarConsecutivosXModulo(Connection con,int institucion,ArrayList rest) 
	{	
	    mapConsecutivos = consecutivoDao.consultaConsecutivosXModulo(con,institucion,this.modulo,rest);
	 	for(int k=0;k<Integer.parseInt(mapConsecutivos.get("numRegistros")+"");k++)
	 	{
	 	   this.setMapConsecutivos("tipo_"+k,"BD");
	 	}	
	 	return (HashMap)mapConsecutivos.clone(); 
	}
	
	
	/**
	 * metodo para consultar un solo consecutivo
	 * @param con Connection
	 * @param parametro String
	 * @return HashMap
	 * @author jarloc
	 */
	public HashMap consultarConsecutivo(Connection con,String parametro)
	{
	 HashMap map=new HashMap();
	 map=consecutivoDao.consultarConsecutivo(con,this.institucion,this.modulo,parametro);
	 return map;
	}
	/**
	 *metodo para consultar los consecutivos de
	 *inventarios en los casos de por almacen
	 *y unico en el sistema 
	 * @param con Connection
	 * @param nombre nombre del consecutivo en inventarios_modulos 
	 * @return HashMap
	 * @author jarloc 
	 */
	public HashMap consultarConsecutivosInventarios(Connection con,String nombre,boolean esUnRegistro,int trnasaccion)
	{
	    HashMap map=new HashMap();
		map=consecutivoDao.consultaConsecutivosInventarios(con,nombre,getInstitucion(),almacen,esUnRegistro,trnasaccion);		
		return map;
	}	
	/**
	 * metodo para obtener el valor de un consecutivo
	 * de los casos especiales de inventarios
	 * @param con Connection
	 * @param tipoTrans int, código del tipo de trnasacción
	 * @param almacen int, código del almacen
	 * @return int, valor del consecutivo
	 * @author jarloc
	 */
	public int obtenerConsecutivoInventario (Connection con,int tipoTrans,int almacen,int institucion)
	{
	   return  consecutivoDao.obtenerConsecutivoInventario(con, tipoTrans, almacen, institucion);
	}
	
	/**
	 * Metodo para actualiazar el valor del
	 * consecutivo de inventario
	 * @param con Connection
	 * @param valor int, valor para actualizar
	 * @param tipoTransaccion int, código del tipo de transacción
	 * @param almacen int, código del almacen
	 * @param institucion int, código de la institución
	 * @return boolean
	 * @author jarloc
	 */
	public boolean actualizarValorConsecutivoInventarios (Connection con,int tipoTransaccion,int almacen,int institucion)
	{
	   int valor=consecutivoDao.obtenerConsecutivoInventario(con, tipoTransaccion, almacen, institucion);
	   if(valor!=ConstantesBD.codigoNuncaValido)
	   {
	       valor ++;
	       return  consecutivoDao.actualizarValorConsecutivoInventarios(con,valor,tipoTransaccion,almacen,institucion);
	   }
	   else
	       return false;
	}
    /**
     * metodo para consultar los modulos validos, para generar
     * la consulta de consecutivos, solo se deben generar el
     * listado de los modulos que posean consecutivos
     * @param con Connection
     * @return HashMap
     * @author jarloc
     */
    public HashMap generarConsultaModulos(Connection con)
    {
        HashMap mapa=new HashMap();
        HashMap modulos=new HashMap();
        ArrayList array=new ArrayList();
        int pos=-1;
        mapa=consecutivoDao.consultaModulos(con);       
        for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
        {
            HashMap mapa1=new HashMap();
            mapa1=consecutivoDao.consultaConsecutivosXModulo(con,this.institucion,Integer.parseInt(mapa.get("codigo_"+k)+""),array);            
            if(!mapa1.isEmpty())
            {
                if(Integer.parseInt(mapa1.get("numRegistros")+"")!=0)
                {
                    pos++;
                    modulos.put("codigo_"+pos,mapa.get("codigo_"+k));
                    modulos.put("nombre_"+pos,mapa.get("nombre_"+k));
                    modulos.put("numRegistros",pos+"");
                }
            }            
        }  
        return (HashMap)modulos.clone();
    }
    
    /**
     * Captura la informacion de la empresa multi institucion
     * @param ArrayList listEmpresas
     * @param String codigoEmpresa
     * */
    public static HashMap capturarInfoEmpresa(ArrayList listEmpresas, String codigoEmpresa)
    {
    	HashMap respuesta = new HashMap();
    	
    	for(int i = 0 ; i< listEmpresas.size(); i++)
    	{
    		if(codigoEmpresa.equals(((HashMap)listEmpresas.get(i)).get("codigo").toString()))
    		{
    			respuesta.put("codigoEmpresa",((HashMap)listEmpresas.get(i)).get("codigo").toString());
    			respuesta.put("institucion",((HashMap)listEmpresas.get(i)).get("institucion").toString());
    			respuesta.put("valorConsecutivoFact",((HashMap)listEmpresas.get(i)).get("valorConsecutivoFact").toString());
    			respuesta.put("anioVigencia",((HashMap)listEmpresas.get(i)).get("anioVigencia").toString());
    			
    			return respuesta;
    		}
    	}    	
    	
    	return respuesta;
    }
    
    /**
     * Actualiza la informacion del consecutivo empresas institucion
	 * @param Connection con
	 * @param String ValorConsecutivo
	 * @param String anioConsecutivo
	 * @param int Institucion
	 * @param String codigoEmpresa
	 */
	public boolean actualizarConsecutivoMultiEmpresa (Connection con,String valorConsecutivo, String anioConsecutivo,int institucion, String codigoEmpresa)
	{
		HashMap parametros = new HashMap();
		parametros.put("valor",valorConsecutivo);
		parametros.put("anio",anioConsecutivo);
		parametros.put("institucion",institucion);
		parametros.put("empresa",codigoEmpresa);
		
		return consecutivoDao.actualizarConsecutivoMultiEmpresa(con, parametros);
	}
    
    
    /**
     * @param modulo Asigna modulo.
     */
    public void setModulo(int modulo) {
        this.modulo = modulo;
    }
    /**
	 * @return Returns the checkbox.
	 */
	public boolean isCheckbox() {		
		return checkbox;
	}
	/**
	 * @param checkbox The checkbox to set.
	 */
	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}
	
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
		
	/**
     * @param key String.
     * @return Object.
     */
    public Object getMapConsecutivos(String key) {
        return mapConsecutivos.get(key);
    }
    /**
     * @param key String.
     * @param value Object.
     */
    public void setMapConsecutivos(String key,Object value) 
    {
    	this.mapConsecutivos.put(key,value);
    }
    
	/**
	 * @return Returns the anioVigencia.
	 */
	public String getAnioVigencia() {
		return anioVigencia;
	}
	/**
	 * @param anioVigencia The anioVigencia to set.
	 */
	public void setAnioVigencia(String anioVigencia) {
		this.anioVigencia = anioVigencia;
	}
	/**
	 * @return Returns the institucion.
	 */
	public int getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return Returns the valor.
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * @param valor The valor to set.
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
    /**
     * @param almacen Asigna almacen.
     */
    public void setAlmacen(int almacen) {
        this.almacen = almacen;
    }
    /**
     * @param mapConsecInv Asigna mapConsecInv.
     */
    public void setMapConsecInv(HashMap mapConsecInv) {
        this.mapConsecInv = mapConsecInv;
    }
    /**
     * @param casoConsecInv Asigna casoConsecInv.
     */
    public void setCasoConsecInv(String casoConsecInv) {
        this.casoConsecInv = casoConsecInv;
    }

	/**
	 * @return the empresa
	 */
	public String getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	
	/**
	 * Generacion del get para tener acceso al Dao
	 * @return
	 */
	private static ConsecutivosDisponiblesDao getConsecutivosDisponiblesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsecutivosDisponiblesDao();		
	}
	
	/**
	 * Validacion para el tipo de consecutivo a manejar en facturas varias
	 * @param con
	 * @return
	 */
	public static int consultaTipoConsecutivoFacturasVarias(Connection con)
	{
		return getConsecutivosDisponiblesDao().validacionTipoConsecutivoFacturasVarias(con); 
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar si un consecutivo ha sido usado
	 * 
	 * @param Connection con,String nombreParametro
	 * @return String
	 * @author, Angela Maria Aguirre
	 *
	 */
	public String consultarConsecutivoUsado(Connection con,String nombreParametro){
		return consecutivoDao.consultarConsecutivoUsado(con, nombreParametro);
	}
	
}
