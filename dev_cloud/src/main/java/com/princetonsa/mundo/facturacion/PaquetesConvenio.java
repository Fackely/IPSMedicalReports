package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.PaquetesConvenioDao;

public class PaquetesConvenio 
{
	
	/**
	 * Intefaz para acceder y comunicarse con la fuente de datos
	 */
	
	private PaquetesConvenioDao objetoDao;
	
	/**
	 * Mapa de paquetes
	 */
	private HashMap paquetesConMap;
	
	private int institucion;
	
	
	
		
	/**
	 *  Constructor de la clase
	 */
	
	public PaquetesConvenio() 
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
	}
	
	
	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getPaquetesConvenioDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}

	
	
	/**
	 * resetea los atributos del objeto.
	 *
	 */
	
	private void reset() 
	{
		paquetesConMap=new HashMap();
    	paquetesConMap.put("numRegistros","0");
    	institucion=ConstantesBD.codigoNuncaValido;
   	}


	public HashMap getPaquetesConMap() {
		return paquetesConMap;
	}


	public void setPaquetesConMap(HashMap paquetesConMap) {
		this.paquetesConMap = paquetesConMap;
	}
	
	
	/**
	 * consulta un paquete convenio especifico dado el codigo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarPaqueteConvenioEspecifico(Connection con,String codigo)
	{
		HashMap vo=new HashMap();
		vo.put("codigo", codigo);
		vo.put("institucion",this.institucion);
		return (HashMap)objetoDao.consultarPaquetesConvenioExistentes(con,vo).clone();
	}	

	
	/**
	 * Consulta todos los paquetes de un convenio-contrato
	 * @param con
	 * @param codigoConvenio
	 * @param codigoContrato
	 * @return
	 */
	public HashMap consultarPaquetesConvenio(Connection con,int codigoConvenio,int codigoContrato)
	{
		HashMap vo=new HashMap();
		vo.put("codigoConvenio", codigoConvenio);
		vo.put("codigoContrato", codigoContrato);
		vo.put("institucion",this.institucion);
		return (HashMap)objetoDao.consultarPaquetesConvenio(con,vo).clone();
	}	
	
	
	
	/**
	 * Consulta de Paquetes
	 * @param con
	 * @param institucion
	 */
	
	public void consultarPaquetesConvenioExistentes(Connection con, String codigo) 
	{
		HashMap vo=new HashMap();
		vo.put("codigo", codigo);
		this.paquetesConMap=objetoDao.consultarPaquetesConvenioExistentes(con,vo);
	}
	
	
	
	/**
	 * Agrega un Nuevo paquete
	 * @param con
	 * @param institucion
	 */
	
	public boolean insertar(Connection con,HashMap vo)
	{
		return objetoDao.insertar(con, vo);
	}
	

	
	
	/**
	 * Modifica un paquete Existente 
	 * @param con
	 * @param vo
	 * @return
	 */
	
	public boolean modificar(Connection con,HashMap vo)
	{
		return objetoDao.modificar(con, vo);
	}
	
	
	
	/**
	 * Elimina un Paquete
	 * @param con
	 * @param institucion
	 * @param codigoPaquete
	 * @return
	 */
	
	public boolean eliminarRegistro(Connection con, String codigo)
	{
		return objetoDao.eliminarRegistro(con,codigo);
	}


	public int getInstitucion() {
		return institucion;
	}


	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	
	

}
