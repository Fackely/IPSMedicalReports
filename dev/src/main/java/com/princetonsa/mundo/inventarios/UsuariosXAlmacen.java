
/*
 * Creado   23/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.LogsAxioma;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.UsuariosXAlmacenDao;

/**
 * 
 *
 * @version 1.0, 23/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class UsuariosXAlmacen
{
    /**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(UsuariosXAlmacen.class);
	/**
	 * DAO de este objeto, para trabajar con
	 * usuarios por almacen en la fuente de datos
	 */
	private static UsuariosXAlmacenDao usuariosDao;
	/**
	 * codigo de la institucion
	 */
	private int institucion;
	/**
	 * codigo del alamcen
	 */
	private int codAlmacen;
	/**
	 * registros de  la funcionalidad
	 */
	private HashMap mapaUXA;
	/**
	 * almacena codigos para eliminar
	 */
	private Vector registrosBDEliminar;
	/**
	 * almacena los logs
	 */
	private String log;
	/**
	 * almacena el login del usuario
	 */
	private String usuario;
	/**
	 * nombre del almacen
	 */
	private String almacen;
	/**
     * Metodo para inicializar los atributos de la clase.
     *
     */
    public void reset ()
    {   
       this.institucion=ConstantesBD.codigoNuncaValido; 
       this.codAlmacen=ConstantesBD.codigoNuncaValido;
       this.mapaUXA=new HashMap ();
       this.registrosBDEliminar= new Vector();
       this.log="";
       this.usuario="";
       this.almacen="";
    }
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( usuariosDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			usuariosDao= myFactory.getUsuariosXAlmacenDao();			
			if( usuariosDao!= null )
				return true;
		}

		return false;
	}
	
	/**
	 * Método para obtener el Dao de UsuariosXAlmacen
	 * @return
	 */
	private static UsuariosXAlmacenDao usuariosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUsuariosXAlmacenDao();
	}
	/**
	 * metodo para consultar usaurios por almacen
	 * @param con Connection 
	 * @param esVerificar  boolean
	 * @return HashMap
	 */
	public HashMap generarConsulta(Connection con)
	{
	    HashMap mapa= new HashMap ();
	    mapa=usuariosDao.generarConsulta(con,this.institucion,this.codAlmacen);
	    for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
	    {
	        mapa.put("tipoReg_"+k, "BD");	 
	        mapa.put("nombre_"+k,mapa.get("primer_apellido_"+k)+" "+mapa.get("segundo_apellido_"+k)+" "+mapa.get("primer_nombre_"+k)+" "+mapa.get("segundo_nombre_"+k));
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
			if (usuariosDao==null)
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
			            HashMap mapa=new HashMap();
			            mapa=usuariosDao.generarConsultaRegistro(con,this.institucion,Integer.parseInt(this.registrosBDEliminar.get(j)+""));
			            enTransaccion=usuariosDao.generarDelete(con,Integer.parseInt(this.registrosBDEliminar.get(j)+""),this.institucion);
			            if(!enTransaccion)
					    {
				             myFactory.abortTransaction(con);
				             logger.warn("Transaction Aborted-No se Elimino");
					    }
			            else
			                this.generarLog(mapa);
			        }
			    }
			    if(enTransaccion)
			    {			        
				  for(int k=0;k<Integer.parseInt(this.mapaUXA.get("numRegistros")+"");k++)
				  {
				      if((this.mapaUXA.get("tipoReg_"+k)+"").equals("BD"))
				      {
				        if(validarModificaciones(con,Integer.parseInt(this.mapaUXA.get("codigo_"+k)+""),(this.mapaUXA.get("login_usuario_"+k)+"")))
				        {
				            enTransaccion=usuariosDao.gerarUpdate(con,Integer.parseInt(this.mapaUXA.get("codigo_"+k)+""),(this.mapaUXA.get("login_usuario_"+k)+""),this.institucion);  
				            if(!enTransaccion)
				            {
					             myFactory.abortTransaction(con);
					             logger.warn("Transaction Aborted-No se Modifico");
				            }
				        }
				      }
				      if(enTransaccion)
				      {
					      if((this.mapaUXA.get("tipoReg_"+k)+"").equals("MEM"))
					      {
						      enTransaccion=usuariosDao.generarInsert(con,this.codAlmacen,this.mapaUXA.get("login_usuario_"+k)+"",this.institucion);
						      if(!enTransaccion)
						      {
					             myFactory.abortTransaction(con);
					             logger.warn("Transaction Aborted-No se Inserto");
						      }
					      }
				      }
				  }
				  if(enTransaccion)
				  {
				      myFactory.endTransaction(con);
				      logger.warn("End Transaction");
				  }
			    }
			}
		}catch (SQLException e) 
		{	       
	        e.printStackTrace();	        
	    } 		
	    return enTransaccion;
	}
	/**
	 * metodo para validar los registros modificados
	 * @param con
	 * @param codigo
	 * @param login
	 * @return
	 */
	private boolean validarModificaciones(Connection con,int codigo,String login)
	{
	    boolean existeMod=false;
	    HashMap mapa=new HashMap();
	    mapa=usuariosDao.generarConsultaRegistro(con,this.institucion,codigo);	    
	    for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
	    {	      
	        if(!login.equals((mapa.get("login_usuario_"+k)+""))) 
	          existeMod=true;	      
	    }
	    return existeMod;
	}
	/**
	 * metodo para generar el log
	 * @param k
	 */
    private void generarLog(HashMap mapa)
	{          
        for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
	    {
	        this.log=
						"\n            ====REGISTRO ELIMINADO===== " +
						"\n*  Almacen ["+this.almacen+"] "+       
						"\n*  Usuario ["+mapa.get("login_usuario_"+k)+""+"] "+
						"\n*  Nombre ["+mapa.get("primer_apellido_"+k)+" "+mapa.get("segundo_apellido_"+k)+" "+mapa.get("primer_nombre_"+k)+" "+mapa.get("segundo_nombre_"+k)+""+"] "+						
						"\n========================================================\n\n\n " ;
	    }		
		LogsAxioma.enviarLog(ConstantesBD.logUsuariosXAlmacenInvCodigo,this.log,ConstantesBD.tipoRegistroLogEliminacion,this.usuario);       
	}
	/**
	 * constructor	 
	 */
	public UsuariosXAlmacen()
	{
	    this.reset();
	    this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Método usado para insertar un registros de usuaios por almacen
	 * @param con
	 * @param codAlmacen
	 * @param loginUsuario
	 * @param institucion
	 * @return
	 */
	public static boolean insertarUsuario(Connection con,int codAlmacen,String loginUsuario,int institucion)
	{
		return usuariosDao().generarInsert(con, codAlmacen, loginUsuario, institucion);
	}
    /**
     * @param institucion Asigna institucion.
     */
    public void setInstitucion(int institucion) {
        this.institucion = institucion;
    }
    /**
     * @param codAlmacen Asigna codAlmacen.
     */
    public void setCodAlmacen(int codAlmacen) {
        this.codAlmacen = codAlmacen;
    }
    /**
     * @param mapaUXA Asigna mapaUXA.
     */
    public void setMapaUXA(HashMap mapaUXA) {
        this.mapaUXA = mapaUXA;
    }
    /**
     * @param registrosBDEliminar Asigna registrosBDEliminar.
     */
    public void setRegistrosBDEliminar(Vector registrosBDEliminar) {
        this.registrosBDEliminar = registrosBDEliminar;
    }
    /**
     * @param usuario Asigna usuario.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    /**
     * @param almacen Asigna almacen.
     */
    public void setAlmacen(String almacen) {
        this.almacen = almacen;
    }
}
