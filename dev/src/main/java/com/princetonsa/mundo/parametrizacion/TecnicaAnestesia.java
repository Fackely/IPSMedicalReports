package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.parametrizacion.TecnicaAnestesiaDao;
import com.servinte.axioma.dto.salascirugia.TipoAnestesiaDto;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * 
 * @author wilson
 *
 */
public class TecnicaAnestesia 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static TecnicaAnestesiaDao tecnicaDao;
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public TecnicaAnestesia() 
	{
		this.init (System.getProperty("TIPOBD"));
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
			tecnicaDao = myFactory.getTecnicaAnestesiaDao();
			wasInited = (tecnicaDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> obtenerTecnicaAnestesia(Connection con, int numeroSolicitud, int centroCosto, int institucion, Vector<String> tiposAnestInstCCNoMostrar)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTecnicaAnestesiaDao().obtenerTecnicaAnestesia(con, numeroSolicitud, centroCosto, institucion, tiposAnestInstCCNoMostrar);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existeTecnicaAnestesia(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTecnicaAnestesiaDao().existeTecnicaAnestesia(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<Object, Object> cargarTecnicaAnestesiaSolicitud(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTecnicaAnestesiaDao().cargarTecnicaAnestesiaSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean insertarTecnicaAnestesia(Connection con, int numeroSolicitud, int tipoAnestesia, int tipoAnestesiaInstCC, String loginUsuario)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTecnicaAnestesiaDao().insertarTecnicaAnestesia(con, numeroSolicitud, tipoAnestesia, tipoAnestesiaInstCC, loginUsuario);
    }
    
    /**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean eliminarTecnicaAnestesia(Connection con, int numeroSolicitud)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTecnicaAnestesiaDao().eliminarTecnicaAnestesia(con, numeroSolicitud);
    }
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerCodigoTecnicaDadoTecnicaCCInst(Connection con, int tipoAnestesiaInstCC)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTecnicaAnestesiaDao().obtenerCodigoTecnicaDadoTecnicaCCInst(con, tipoAnestesiaInstCC);
	}
	
	/**
	 * Método para consultar los tipos aplicables para la hoja de anestesia según la institución
	 * @param con
	 * @param institucion
	 * @param nroConsulta
	 * @return Collection
	 */
	
	public static Collection consultarTipoParametrizado (Connection con, int institucion,int nroConsulta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTecnicaAnestesiaDao().consultarTipoParametrizado(con, institucion, nroConsulta);	
	}
	
	/**
     * Metodo para consultar y cargar la información de la sección Técnica de Anestesia
     * @param con
     * @param nroSolicitud
     * @return true si existe información de técnicas de anestesia
     */
    public static HashMap<Object, Object> cargarTecnicaAnestesiaGeneralRegional(Connection con, int nroSolicitud) 
    {
    	Collection colTecAnestesia=null;
    	HashMap<Object, Object> mapa= new HashMap<Object, Object>();
    	
    	try
		{
    		colTecAnestesia=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTecnicaAnestesiaDao().cargarTecnicaAnestesiaGeneralRegional(con, nroSolicitud);
    		Iterator ite=colTecAnestesia.iterator();
    		for (int i=0; i<colTecAnestesia.size(); i++)
    		{
    			if (ite.hasNext())
    			{
    				HashMap colTecAnest=(HashMap) ite.next();
    				mapa.put("valorTecAnestesia_"+colTecAnest.get("codigo"), colTecAnest.get("valor"));
    			}
    		}//for
    		
		}
    	catch(Exception e)
		{
    		e.printStackTrace();
		}
    	return mapa;
    }
    
    
	/**
	 * Método para insertar las técnicas de anestesia general y regional parametrizadas
	 * @param con una conexion abierta con una fuente de datos
	 * @param nroSolicitud
	 * @return tecAnestesia
	 * @throws SQLException
	 */
	public static int insertarTecnicaAnestesiaGeneralRegional (Connection con, int nroSolicitud, HashMap<Object, Object> mapa) 
	{
		boolean error=false;
		int codTecAnestesia=0;
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=UtilidadBD.iniciarTransaccion(con);
		
		System.out.print("codigotecanes es null-->"+mapa.get("codigosTecAnestesia"));
		if(mapa.get("codigosTecAnestesia") != null)
		{
			//if(mapa.get("esInsertar") != null)
			{
				Vector codigos=(Vector) mapa.get("codigosTecAnestesia");
				//Vector vecAccion=(Vector) mapa.get("esInsertar");
				
				for (int i=0; i<codigos.size(); i++)
				{
					int tecAnestesiaInst=Integer.parseInt(codigos.elementAt(i)+"");
					//int accion=Integer.parseInt(vecAccion.elementAt(i)+"");
					
					//Si la accion es 0 no modifica ni inserta el registro
					//if(accion != 0)
					{
						//Valor de la técnica de anestesia
						String valorTecAnestesia=(String)mapa.get("valorTecAnestesia_"+tecAnestesiaInst);
						
						
						//Se inserta o se modifica la técnica de anestesia parametrizada
						codTecAnestesia=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTecnicaAnestesiaDao().insertarTecnicaAnestesiaGeneralRegional (con, nroSolicitud, tecAnestesiaInst, valorTecAnestesia);
								
						if(codTecAnestesia < 1)
						{
							error=true;
							break;
						}
					}//if accion != 0
				}//for
				
			}//if esInsertar != null
		}//if codigosTecAnestesia != null
		
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error)
		{
			UtilidadBD.abortarTransaccion(con);
			return -1;
		}
		else
		{
		    UtilidadBD.finalizarTransaccion(con);
		}
		
		return codTecAnestesia;
	}
    
	/**
	 * Consulta las tecnicas de anestesia (tipos de anestesia) registrados en la hoja de anestesia
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return tipos de anestesia de la hoja de anestesia
	 * @throws IPSException
	 * @author jeilones
	 * @created 16/07/2013
	 */
	public static List<TipoAnestesiaDto> consultarTecnicasAnestesiaXSolicitud(Connection con, int numeroSolicitud) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTecnicaAnestesiaDao().consultarTecnicasAnestesiaXSolicitud(con, numeroSolicitud);
	}
    
}