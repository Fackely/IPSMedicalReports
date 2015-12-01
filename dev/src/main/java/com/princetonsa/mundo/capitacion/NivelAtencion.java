/*
 * Creado el Jun 13, 2006
 * por Julian Montoya
 */
package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadCadena;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.NivelAtencionDao;


public class NivelAtencion {

	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(NivelAtencion.class);
	
	/**
	 * Variable para ingresar a la BD.  
	 */
	private NivelAtencionDao nivelAtencionDao;

	/**
	 * Mapa para guardar toda las descripciones de los Niveles de Servicios.
	 */
	private HashMap mapa;

	/**
     * Variable para contabilizar el numero de registros nuevos  
     */
    private int nroRegistrosNuevos;
    

	
	/**
	 * Constructor de la clase.
	 *
	 */
	public NivelAtencion()
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
			nivelAtencionDao = myFactory.getNivelAtencionDao();
			wasInited = (nivelAtencionDao != null);
		}
		return wasInited;
	}
	
	
	/**
	 * Metodo para cargar los niveles de servicio registrados.
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 * @throws SQLException 
	 */
	public HashMap cargarInformacion(Connection con, int codigoInstitucionInt) throws SQLException 
	{
		if (nivelAtencionDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (NivelServicio - cargarInformacion )"); 
		}
		
		return nivelAtencionDao.cargarInformacion(con, codigoInstitucionInt);
	}


	/**
	 * Metodo para insertar, eliminar y modificar Niveles de servicios.
	 * @param con
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 * @return
	 * @throws SQLException 
	 */
	public boolean insertar(Connection con, int institucion, String loginUsuario) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp=0;

		if (nivelAtencionDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (NivelServicioDao - insertar)");
		}
		
		//----Iniciamos la transacción.
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);
		
		//----Insertando los cambios de la informacion registrada anteriormente.
		int nroReg = 0;
		if ( UtilidadCadena.noEsVacio(this.mapa.get("numRegistros")+"") ) { nroReg = Integer.parseInt(this.mapa.get("numRegistros")+""); }

		for (int i = 0; i < nroReg; i++)
		{
			int consecutivo = Integer.parseInt(this.getMapa("consecutivo_" + i)+"");  
			int codigo = Integer.parseInt(this.getMapa("codigo_"+i)+"");
			
			String activo = this.getMapa("activo_" + i)+"".trim();  
			String h_activo = this.getMapa("h_activo_" + i)+"".trim();  
			String descripcion = this.getMapa("nivel_" + i)+"".trim(); 
			String h_descripcion = this.getMapa("h_nivel_" + i)+"".trim();

			//logger.info("\n\n Activo [" + activo + "] ["+ descripcion + "]\n\n");
			
			if ( !activo.equals(h_activo) || !descripcion.equals(h_descripcion) )
			{
				  StringBuffer log=new StringBuffer();
				  log.append("\n=========================MODIFICACIÓN DE NIVELES DE SERVICIOS================");
				  log.append("\n CODIGO NIVEL  	 :" + codigo);
				  
				  if ( !activo.equals(h_activo) && !descripcion.equals(h_descripcion) )
				  {
					  log.append("\n DESCRIPCÍON ANTERIOR 	:" + h_descripcion);
					  log.append("\n DESCRIPCÍON NUEVA 		:" + descripcion);
					  if ( h_activo.equals("true") )  log.append("\n SE DESACTIVO	 ");
					  else 							  log.append("\n SE ACTIVO		 ");
				  }
				  
				  if ( !activo.equals(h_activo) && descripcion.equals(h_descripcion) ) 
				  {
					  log.append("\n DESCRIPCÍON NIVEL	 :" + h_descripcion);
					  if ( h_activo.equals("true") )  log.append("\n SE DESACTIVO	 ");
					  else 							  log.append("\n SE ACTIVO		 ");
				  }
				  
				  if ( activo.equals(h_activo) && !descripcion.equals(h_descripcion) ) 
				  {
					  log.append("\n DESCRIPCÍON ANTERIOR :" + h_descripcion);
					  log.append("\n DESCRIPCÍON NUEVA 	  :" + descripcion);
					  if (UtilidadTexto.getBoolean(activo))
						log.append("\n ESTA ACTIVO ");
					  else 
						log.append("\n ESTA INACTIVO ");
				  }
				  log.append("\n=========================================================================\n");

				  //-Generar el log.
				  LogsAxioma.enviarLog(ConstantesBD.logNivelServicioCodigo, log.toString(), ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
			
				  //--Se Modifica el registro. (La operacion 1 es para modificar).
				  boolean aux = UtilidadTexto.getBoolean(activo);
				  resp=nivelAtencionDao.insertar(con, 1, consecutivo, descripcion, aux, institucion);
				  if (resp <= 0) { break; }
			}
		}
		
		//--------------------------------------------------------------------------------------------------------------
		//--------------------------------------------------------------------------------------------------------------
		//--Insertando los Niveles Nuevos.
			logger.info("\n\n this.nroRegistrosNuevos [" +this.nroRegistrosNuevos +"] \n\n");
			for(int i=0; i<=this.nroRegistrosNuevos;i++)
			{
				String descripcion = (String)this.getMapa("nivelN_"+i);
				if ( UtilidadCadena.noEsVacio(descripcion) ) 
				{
					int codigo = Integer.parseInt(this.getMapa("codigoN_"+i)+"");
					String ck = (String)this.getMapa("ckN_"+i);
	
					//logger.info("\n\n NUEVOS  codigo [" + codigo +"] [" +descripcion + "]["+ ck+ "]+ \n\n");
					
					//-Verificar si se chequeo.
					if (UtilidadCadena.vNull(ck, "on"))
					{ 
						resp=nivelAtencionDao.insertar(con, 0, codigo, descripcion, false, institucion);
						if (resp < 1) { break; }
					}
				}
				else
				 { 
					resp = 1; //-Para que no haga rollback sobre las modificaciones de los niveles registrandos antes. 
				 }
			}
			
		//--Se finaliza la transacción cuando hay error en una inserción 
		//--de datos o no se logró inicializar la transacción.
		if (!inicioTrans || resp<1)
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
	 * Metodo para eliminar un Nivel De Servicio
	 * @param con
	 * @param loginUsuario 
	 * @throws SQLException 
	 */
	public boolean eliminar(Connection con, String loginUsuario) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp=0;

		if (nivelAtencionDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (NivelServicioDao - insertar)");
		}
		
		//----Iniciamos la transacción.
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);
		
		//----Insertando los cambios de la informacion registrada anteriormente.
		int nroReg = 0, indice = -1;
		if ( UtilidadCadena.noEsVacio(this.mapa.get("regEliminado")+"") ) { nroReg = Integer.parseInt(this.mapa.get("regEliminado")+""); }
		if ( UtilidadCadena.noEsVacio(this.mapa.get("indEliminado")+"") ) { indice = Integer.parseInt(this.mapa.get("indEliminado")+""); }
		
		if ( (nroReg != 0) && (indice!=-1))
		{
			  StringBuffer log=new StringBuffer();
			  log.append("\n=========================ELIMINACION DE NIVELES DE SERVICIOS================");
			  log.append("\n CODIGO NIVEL  		  :" + this.getMapa("codigo_" + indice) );
			  log.append("\n DESCRIPCÍON ANTERIOR :" + this.getMapa("h_nivel_" + indice) );
			  if ( UtilidadTexto.getBoolean(this.getMapa("activo_" + indice)) )
			  {
				  log.append("\n ESTABA ACTIVO ");
			  }
			  else
			  {
				  log.append("\n ESTABA INACTIVO ");
			  }
			  log.append("\n=========================================================================\n");

			  //-Generar el log.
			  LogsAxioma.enviarLog(ConstantesBD.logNivelServicioCodigo, log.toString(), ConstantesBD.tipoRegistroLogEliminacion, loginUsuario);
			  resp = nivelAtencionDao.eliminar(con, nroReg);
			
		}
			
		//--Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción.
		if (!inicioTrans || resp<1)
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
	 * @return Returns the mapa.
	 */
	public String getMapa(String key) {
		return this.mapa.get(key).toString();
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapa(String key, String valor) 
	{
		this.mapa.put(key, valor); 
	}

	/**
	 * @return Retorna nivelServicioDao.
	 */
	public NivelAtencionDao getNivelServicioDao() {
		return nivelAtencionDao;
	}


	/**
	 * @param Asigna nivelServicioDao.
	 */
	public void setNivelServicioDao(NivelAtencionDao nivelServicioDao) {
		this.nivelAtencionDao = nivelServicioDao;
	}


	/**
	 * @return Retorna nroRegistrosNuevos.
	 */
	public int getNroRegistrosNuevos() {
		return nroRegistrosNuevos;
	}


	/**
	 * @param Asigna nroRegistrosNuevos.
	 */
	public void setNroRegistrosNuevos(int nroRegistrosNuevos) {
		this.nroRegistrosNuevos = nroRegistrosNuevos;
	}


}
