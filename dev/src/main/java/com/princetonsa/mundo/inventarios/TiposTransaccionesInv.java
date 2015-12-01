
/*
 * Creado   11/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.TiposTransaccionesInvDao;

/**
 * 
 *
 * @version 1.0, 11/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class TiposTransaccionesInv 
{
    /**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(TiposTransaccionesInv.class);
	/**
	 * DAO de este objeto, para trabajar con tipos transacciones inventario
	 * en la fuente de datos
	 */    
    private static TiposTransaccionesInvDao tiposDao;
    /**
     * almacena los registros de 
     * tipos de transacciones
     */
    private HashMap mapaTipos;
    /**
     * código del tipo de transaccion
     */
    private String codigo;
    /**
     * descripción del tipo de transacción
     */
    private String descripcion;
    /**
     * código del tipo de concepto
     */
    private int codConcepto;
    /**
     * código del tipo de costo
     */
    private int codCosto;
    /**
     * true o false
     */
    private String activo;
    
    /**
     * Codigo de la interfaz
     */
    private String codigoInterfaz;
    /**
     * 
     */
    private String indicativo_consignacion;
    /**
     * código de la institución
     */
    private int institucion;
    /**
     * códigos de los registroa a eliminar
     */
    private ArrayList registrosBDEliminar;
    /**
     * número de registros que seran eliminados
     */
    private int contRegistrosEliminar;
    /**
     * almacena los loogs
     */
    private String log="";
    /**
     * login del usuario
     */
    private String usuario;
    /**
     * Metodo para inicializar los atributos de la clase.
     *
     */
    public void reset ()
    {      
        this.mapaTipos=new HashMap() ; 
        this.codigo="";
        this.descripcion="";
        this.codConcepto=ConstantesBD.codigoNuncaValido;
        this.codCosto=ConstantesBD.codigoNuncaValido;
        this.activo="";
        this.indicativo_consignacion="";
        this.institucion=ConstantesBD.codigoNuncaValido;
        this.registrosBDEliminar=new ArrayList();
        this.contRegistrosEliminar=0;
        this.log="";
        this.usuario="";
        this.codigoInterfaz = "";
    }
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( tiposDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tiposDao= myFactory.getTiposTransaccionesInvDao();
			
			if( tiposDao!= null )
				return true;
		}

		return false;
	}
	/**
	 * constructor
	 */
	public TiposTransaccionesInv()
	{
	    this.reset();
	    this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * metodo para generar la consulta
	 * @param con Connection
	 * @return HashMap
	 */
	public HashMap generarConsultaTransacciones(Connection con)
	{
	 HashMap mapa=tiposDao.generarConsultaTiposTransaccionesInv(con,this.codConcepto,this.codCosto,this.codigo,this.descripcion,this.activo,this.indicativo_consignacion,this.institucion,this.codigoInterfaz);
	 for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
	 {
	   mapa.put("tipoReg_"+k,"BD");  
	 }
	 return mapa;
	}
	/**
	 * metodo para insertar sobre la BD, las 
	 * operaciones realizadas
	 * @param con Connection
	 * @return boolean
	 */
	public boolean guardarCambiosEnBDTrans(Connection con)
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans,enTransaccion=true;
		try 
		{
			if (tiposDao==null)
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
			  if(!this.registrosBDEliminar.isEmpty())
			  {
			     for(int j=0;j<this.registrosBDEliminar.size();j++)
			     {
			         enTransaccion=tiposDao.generarDeleteTiposTransaccionesInv(con,Integer.parseInt(this.registrosBDEliminar.get(j)+""));
			         if(!enTransaccion)
			          {
			              myFactory.abortTransaction(con);
			              logger.warn("Transaction Aborted-No se Elimino");
			          }
			     }
			  }
			  if(enTransaccion)
			  {
				  for(int k=0;k<Integer.parseInt(this.mapaTipos.get("numRegistros")+"");k++)
				  {
				      if((this.mapaTipos.get("tipoReg_"+k)+"").equals("BD"))
				      {
				       boolean esModificado=validarModificaciones(con,Integer.parseInt(this.mapaTipos.get("consecutivo_"+k)+""),this.mapaTipos.get("activo_"+k)+"",this.mapaTipos.get("indicativo_consignacion_"+k)+"",this.mapaTipos.get("descripcion_"+k)+"",this.mapaTipos.get("codigo_interfaz_"+k)+"");
				       if(esModificado)
				       {
				           enTransaccion=tiposDao.generarUpdateTiposTransaccionesInv(con,Integer.parseInt(this.mapaTipos.get("consecutivo_"+k)+""), this.mapaTipos.get("descripcion_"+k)+"",this.mapaTipos.get("activo_"+k)+"",this.mapaTipos.get("indicativo_consignacion_"+k)+"",this.mapaTipos.get("codigo_interfaz_"+k)+"");
				           if(!enTransaccion)
					          {
					              myFactory.abortTransaction(con);
					              logger.warn("Transaction Aborted-No se Modifico");
					          }
				           else
				           {
				               this.llenarLog(k);
				               this.generarLog(k);
				           }
				       }
				      }
				      if(enTransaccion)
					  {
					      if((this.mapaTipos.get("tipoReg_"+k)+"").equals("MEM"))
					      {				          
					          enTransaccion=tiposDao.generarInsertTiposTransaccionesInv(con,Integer.parseInt(this.mapaTipos.get("codigo_concepto_"+k)+""),Integer.parseInt(this.mapaTipos.get("codigo_costo_"+k)+""),this.mapaTipos.get("codigo_"+k)+"",this.mapaTipos.get("descripcion_"+k)+"",this.mapaTipos.get("activo_"+k)+"",this.mapaTipos.get("indicativo_consignacion_"+k)+"",this.institucion,this.mapaTipos.get("codigo_interfaz_"+k)+"");
					          if(!enTransaccion)
					          {
					              myFactory.abortTransaction(con);
					              logger.warn("Transaction Aborted-No se Inserto");
					          }
					      }
					  }
				  }
			  }
			  if(enTransaccion)
			    {			        
			        myFactory.endTransaction(con);
	                logger.warn("End Transaction !");
			    }
			}
		}catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    } 		
	    return enTransaccion;
	}
	/**
	 * metodo para validar si existen modificaciones
	 * @param con
	 * @param consecutivo
	 * @param activo
	 * @param descripcion
	 * @param codigoInterfaz 
	 * @return
	 */
	private boolean validarModificaciones(Connection con,int consecutivo,String activo,String indicativo_consignacion, String descripcion, String codigoInterfaz)
	{
	    boolean existeMod=false;
	    ResultSetDecorator rs=tiposDao.verificarModificacionesInv(con,consecutivo);
	    try {
            while(rs.next())
            {
                if(!descripcion.equals(rs.getString("descripcion")))
                {
                    this.descripcion=rs.getString("descripcion");
                    existeMod=true;
                }
                if(!indicativo_consignacion.equals(rs.getString("indicativo_consignacion")))
                {
                    this.indicativo_consignacion=rs.getString("indicativo_consignacion");
                    existeMod=true;
                }
                if(!activo.equals(Boolean.toString(UtilidadTexto.getBoolean(rs.getString("activo")))))
                {
                    this.activo=Boolean.toString(UtilidadTexto.getBoolean(rs.getString("activo")));
                    existeMod=true;
                }
                if(!codigoInterfaz.trim().equals(rs.getString("codigo_interfaz").trim()))
                {
                	this.codigoInterfaz = rs.getString("codigo_interfaz");
                	existeMod = true;
                }
            }
        } catch (SQLException e) {           
            e.printStackTrace();
        }
	    return existeMod;
	}
	/**
     * llenar el log de modificaciones
     * @param k
     */
    private void llenarLog(int index)
    {     
	    this.log=	"\n			====INFORMACION ORIGINAL===== " +
		 			"\n*  Código ["+this.mapaTipos.get("codigo_"+index)+""+"] " ;	 			 
	    if(!this.descripcion.equals(""))
	        this.log+="\n*  Descripción ["+this.descripcion+"] ";
	    if(!this.activo.equals(""))
	    {
	        String s1=(this.activo).equals("true")?"Si":"No";
	        this.log+="\n*  Activo ["+s1+"] ";
	    }
	    if(!this.codigoInterfaz.equals(""))
	        this.log+="\n*  Código Interfaz ["+this.codigoInterfaz+"] ";
	    this.log+="\n\n";
    }
	/**
	 * metodo para generar el log
	 * @param k
	 */
    private void generarLog(int index)
	{          
        this.log+=
					"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
					"\n*  Código ["+this.mapaTipos.get("codigo_"+index)+""+"] " ;					
		
        if(!this.descripcion.equals(""))
	        this.log+="\n*  Descripción ["+this.mapaTipos.get("descripcion_"+index)+""+"] ";
	    if(!this.activo.equals(""))
	    {
	        String s1=(this.mapaTipos.get("activo_"+index)+"").equals("true")?"Si":"No";
	        this.log+="\n*  Activo ["+s1+"] ";
	    }
	    if(!this.mapaTipos.get("codigo_interfaz_"+index).toString().equals(""))
	    	this.log += "\n* Código Interfaz ["+this.mapaTipos.get("codigo_interfaz_"+index)+"]";
	    this.log+="\n========================================================\n\n\n " ;
		
		LogsAxioma.enviarLog(ConstantesBD.logTiposTransaccionesInvCodigo,this.log,ConstantesBD.tipoRegistroLogModificacion,this.usuario);       
	}
    /**
     * @return Retorna mapaTipos.
     */
    public HashMap getMapaTipos() {
        return mapaTipos;
    }
    /**
     * @param mapaTipos Asigna mapaTipos.
     */
    public void setMapaTipos(HashMap mapaTipos) {
        this.mapaTipos = mapaTipos;
    }
    /**
     * @param activo Asigna activo.
     */
    public void setActivo(String activo) {
        this.activo = activo;
    }
    /**
     * @param codConcepto Asigna codConcepto.
     */
    public void setCodConcepto(int codConcepto) {
        this.codConcepto = codConcepto;
    }
    /**
     * @param codCosto Asigna codCosto.
     */
    public void setCodCosto(int codCosto) {
        this.codCosto = codCosto;
    }
    /**
     * @param codigo Asigna codigo.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    /**
     * @param descripcion Asigna descripcion.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @param institucion Asigna institucion.
     */
    public void setInstitucion(int institucion) {
        this.institucion = institucion;
    }
    /**
     * @return Retorna registrosBDEliminar.
     */
    public ArrayList getRegistrosBDEliminar() {
        return registrosBDEliminar;
    }
    /**
     * @param registrosBDEliminar Asigna registrosBDEliminar.
     */
    public void setRegistrosBDEliminar(ArrayList registrosBDEliminar) {
        this.registrosBDEliminar = registrosBDEliminar;
    }
    /**
     * @return Retorna registrosBDEliminar.
     */
    public Object getRegistrosBDEliminar(int pos) {
        return registrosBDEliminar.get(pos);
    }
    /**
     * @param registrosBDEliminar Asigna registrosBDEliminar.
     */
    public void setRegistrosBDEliminar(int pos,Object value) {
        this.registrosBDEliminar.add(pos, value);
    }
    /**
     * @param contRegistrosEliminar Asigna contRegistrosEliminar.
     */
    public void setContRegistrosEliminar(int contRegistrosEliminar) {
        this.contRegistrosEliminar = contRegistrosEliminar;
    }
    /**
     * @param usuario Asigna usuario.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
	public String getIndicativo_consignacion() {
		return indicativo_consignacion;
	}
	public void setIndicativo_consignacion(String indicativo_consignacion) {
		this.indicativo_consignacion = indicativo_consignacion;
	}
	/**
	 * @return the codigoInterfaz
	 */
	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}
	/**
	 * @param codigoInterfaz the codigoInterfaz to set
	 */
	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}
}
