
/*
 * Creado   21/11/2005
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
import util.Utilidades;

import com.princetonsa.actionform.inventarios.TransaccionesValidasXCCForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.TransaccionesValidasXCCDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * 
 *
 * @version 1.0, 21/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class TransaccionesValidasXCC 
{
    /**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(TransaccionesValidasXCC.class);
	/**
	 * DAO de este objeto, para trabajar con transacciones 
	 * validas por centro de costo en la fuente de datos.
	 */
	private static TransaccionesValidasXCCDao transDao;
	/**
	 * codigo de la institución
	 */
	private int institucion;
	/**
	 * código del centro de costo
	 */
	private int codCentroCosto;
	
	/**
	 * 
	 */
	private int transaccionFiltro;
	
	/**
	 * almacana toda la información de 
	 * registros
	 */
	private HashMap mapaTrans;
	/**
     * almacena los registros a eliminar
     */
    private Vector registrosBDEliminar;
    /**
     * almacena información de logs
     */
    private String log;
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
     this.institucion=ConstantesBD.codigoNuncaValido;
     this.codCentroCosto=ConstantesBD.codigoNuncaValido;
     this.mapaTrans=new HashMap(); 
     this.registrosBDEliminar=new Vector();
     this.log="";
     this.usuario="";
     this.transaccionFiltro=ConstantesBD.codigoNuncaValido;
    }
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( transDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			transDao= myFactory.getTransaccionesValidasXCCDao();
			
			if( transDao!= null )
				return true;
		}

		return false;
	}
	/**
	 * constructor	 
	 */
	public TransaccionesValidasXCC()
	{
	    this.reset();
	    this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * metodo par generar la consulta
	 * @param con Connection
	 * @return HashMap
	 */
	public HashMap generarConsulta(Connection con,boolean esVerificar)
	{
	    HashMap mapa= new HashMap ();
	    mapa=transDao.generarConsulta(con, this.institucion,this.codCentroCosto,esVerificar, this.transaccionFiltro);
	    for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
	    {
	        mapa.put("tipoReg_"+k, "BD");
	        mapa.put("desc_tipos_trans_inventario_"+k,mapa.get("codigostrtrans_"+k)+"-"+mapa.get("desc_tipos_trans_inventario_"+k)+"-"+mapa.get("desc_tipo_concepto_"+k));
	        if((mapa.get("nom_clase_inventario_"+k)+"").equals(""))
	            mapa.put("nom_clase_inventario_"+k,"Seleccione");
	        else
	            mapa.put("nom_clase_inventario_"+k,mapa.get("cod_clase_inventario_"+k)+"-"+mapa.get("nom_clase_inventario_"+k));
	        if((mapa.get("nom_grupo_inventario_"+k)+"").equals(""))
	            mapa.put("nom_grupo_inventario_"+k,"Seleccione");
	        else
	            mapa.put("nom_grupo_inventario_"+k,mapa.get("cod_grupo_inventario_"+k)+"-"+mapa.get("nom_grupo_inventario_"+k));
	    }	   
	    return mapa;	    
	}
	
	/**
	 * metodo par generar la consulta
	 * @param con Connection
	 * @return HashMap
	 */
	public static HashMap generarConsultaStatic(Connection con, int institucion, int centroCosto, int transaccionFiltro)
	{
	    HashMap mapa= new HashMap ();
	    mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTransaccionesValidasXCCDao().generarConsulta(con, institucion,centroCosto,false,transaccionFiltro);
	    return mapa;	    
	}
	
	/**
	 * metodo para insertar sobre la BD, las 
	 * operaciones realizadas
	 * @param con Connection
	 * @param string 
	 * @return boolean
	 */
	public boolean guardarCambiosEnBDTrans(Connection con, String codTipoTransaccion)
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    
	    Utilidades.imprimirMapa(this.mapaTrans);
	    
	    
		boolean inicioTrans,enTransaccion=true;
		try 
		{
			if (transDao==null)
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
			    	    mapa=transDao.generarConsulta(con,this.institucion,Integer.parseInt(this.registrosBDEliminar.get(j)+""),true, this.transaccionFiltro);			    	   
			            enTransaccion=transDao.generarDelete(con,Integer.parseInt(this.registrosBDEliminar.get(j)+""),this.institucion);
			            if(!enTransaccion)
				         {
				             myFactory.abortTransaction(con);
				             logger.warn("Transaction Aborted-No se Elimino");
				         }
			            else
			            {
			               this.generarLog(mapa); 
			            }
			        }
			    }
			    if(enTransaccion)
			    {
				  for(int k=0;k<Integer.parseInt(this.mapaTrans.get("numRegistros")+"");k++)
				  {
				      if((this.mapaTrans.get("tipoReg_"+k)+"").equals("BD"))
				      {
				        if(validarModificaciones(con,Integer.parseInt(this.mapaTrans.get("codigo_"+k)+""),Integer.parseInt(this.mapaTrans.get("cod_tipos_trans_inventario_"+k)+""),Integer.parseInt(this.mapaTrans.get("cod_clase_inventario_"+k)+""),Integer.parseInt(this.mapaTrans.get("cod_grupo_inventario_"+k)+"")))
				        {
				        	//Se modifico por el Anexo 728
				        	//enTransaccion=transDao.gerarUpdate(con,Integer.parseInt(this.mapaTrans.get("codigo_"+k)+""),this.codCentroCosto,Integer.parseInt(this.mapaTrans.get("cod_tipos_trans_inventario_"+k)+""),Integer.parseInt(this.mapaTrans.get("cod_clase_inventario_"+k)+""),Integer.parseInt(this.mapaTrans.get("cod_grupo_inventario_"+k)+""),this.institucion);
				        	enTransaccion=transDao.gerarUpdate(con,Integer.parseInt(this.mapaTrans.get("codigo_"+k)+""),this.codCentroCosto,Integer.parseInt(codTipoTransaccion),Integer.parseInt(this.mapaTrans.get("cod_clase_inventario_"+k)+""),Integer.parseInt(this.mapaTrans.get("cod_grupo_inventario_"+k)+""),this.institucion);
				        	if(!enTransaccion)
					         {
					             myFactory.abortTransaction(con);
					             logger.warn("Transaction Aborted-No se Inserto");
					         }
				        }
				      }
				      if(enTransaccion)
				      {
					      if((this.mapaTrans.get("tipoReg_"+k)+"").equals("MEM"))
					      {
					    	  //Se modifico por el Anexo 728
					         //enTransaccion=transDao.generarInsert(con,this.codCentroCosto,Integer.parseInt(this.mapaTrans.get("cod_tipos_trans_inventario_"+k)+""),Integer.parseInt(this.mapaTrans.get("cod_clase_inventario_"+k)+""),Integer.parseInt(this.mapaTrans.get("cod_grupo_inventario_"+k)+""),this.institucion);
					    	  logger.info("ENTRA!!!!!!! INSERTAR......");
					         enTransaccion=transDao.generarInsert(con,this.codCentroCosto,Integer.parseInt(codTipoTransaccion),Integer.parseInt(this.mapaTrans.get("cod_clase_inventario_"+k)+""),Integer.parseInt(this.mapaTrans.get("cod_grupo_inventario_"+k)+""),this.institucion);
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
	                logger.warn("End Transaction !");
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
	 * metodo para validar registros modificados
	 * @param con
	 * @param codigo
	 * @param codTipoTrans
	 * @param codClase
	 * @param codGrupo
	 * @return
	 */
	private boolean validarModificaciones(Connection con,int codigo,int codTipoTrans,int codClase,int codGrupo)
	{
	    boolean existeMod=false;
	    HashMap mapa=new HashMap();
	    mapa=transDao.generarConsultaCamposAModificarStr(con,codigo,this.institucion);
	    for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
	    {
	      if(codTipoTrans!=Integer.parseInt(mapa.get("cod_tipos_trans_inventario_"+k)+""))  
	          existeMod=true;
	      if(codClase!=Integer.parseInt(mapa.get("cod_clase_inventario_"+k)+""))
	          existeMod=true;
	      if(codGrupo!=Integer.parseInt(mapa.get("cod_grupo_inventario_"+k)+""))
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
						"\n*  Centro Costo ["+this.mapaTrans.get("nom_centro_costo_"+k)+""+"] "+       
						"\n*  Transacción ["+this.mapaTrans.get("desc_tipos_trans_inventario_"+k)+""+"] "+
						"\n*  Clase ["+this.mapaTrans.get("nom_clase_inventario_"+k)+""+"] "+
						"\n*  Grupo ["+this.mapaTrans.get("nom_grupo_inventario_"+k)+""+"] "+
						"\n========================================================\n\n\n " ;
	    }		
		LogsAxioma.enviarLog(ConstantesBD.logTransaccionesValidasXCCInvCodigo,this.log,ConstantesBD.tipoRegistroLogEliminacion,this.usuario);       
	}
    /**
     * @param institucion Asigna institucion.
     */
    public void setInstitucion(int institucion) {
        this.institucion = institucion;
    }
    /**
     * @param codCentroCosto Asigna codCentroCosto.
     */
    public void setCodCentroCosto(int codCentroCosto) {
        this.codCentroCosto = codCentroCosto;
    }
    /**
     * @param mapaTrans Asigna mapaTrans.
     */
    public void setMapaTrans(HashMap mapaTrans) {
        this.mapaTrans = mapaTrans;
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
	 * @return the transaccionFiltro
	 */
	public int getTransaccionFiltro() {
		return transaccionFiltro;
	}
	/**
	 * @param transaccionFiltro the transaccionFiltro to set
	 */
	public void setTransaccionFiltro(int transaccionFiltro) {
		this.transaccionFiltro = transaccionFiltro;
	}

	//**************** INICIO MÉTODOS INCLUIDOS POR EL ANEXO 728 **************************
	/**
	 * Método que carga las Transacciones de Inventarios según el centro
	 * de costo seleccionado
	 * @param con
	 * @param codCentroCosto
	 * @return
	 */
	public HashMap<String, Object> cargarTransaccionesInventarios(Connection con, int codCentroCosto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTransaccionesValidasXCCDao().cargarTransaccionesInventarios(con, codCentroCosto, institucion);
	}
	
	/**
	 * @param con
	 * @param consecutivoTrans
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public boolean eliminarTransaccion(Connection con, String consecutivoTrans, int centroCosto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTransaccionesValidasXCCDao().eliminarTransaccion(con, consecutivoTrans, centroCosto, institucion);
	}
	
	/**
	 * Método que inserta una nueva Transacción 
	 * @param con
	 * @param formTran
	 * @return
	 */
	public boolean insertarTransaccion(Connection con, TransaccionesValidasXCCForm formTran, UsuarioBasico usuario)
	{
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("centroCosto", formTran.getCodCentroCosto());
		criterios.put("transaccion", formTran.getCodigoTransaccionSeleccionada());
		criterios.put("institucion", usuario.getCodigoInstitucion());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTransaccionesValidasXCCDao().insertarTransaccion(con, criterios);
	}
	//****************** FIN MÉTODOS INCLUIDOS POR EL ANEXO 728 *****************************
	
}