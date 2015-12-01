/*
 * Ago 02, 2006
 */
package com.princetonsa.mundo.pyp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadCadena;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.pyp.ActividadesPypDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Parametrización de Actividades de Promoción y prevención
 */
public class ActividadesPyp 
{
	/**
	 * DAO para el manejo de ActividadesPypDao
	 */
	ActividadesPypDao actividadesDao = null;
	
	//**********ATRIBUTOS*****************************************
	/**
	 *Objeto usado para pasar parámetros al dao
	 */
	HashMap campos = new HashMap();
	//************************************************************
	
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public ActividadesPyp() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.campos = new HashMap();
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (actividadesDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			actividadesDao = myFactory.getActividadesPypDao();
		}	
	}
	//****************************************************************************

	//****************************************************************************
	
	//************MÉTODOS FUNC. ACTIVIDADES PROMOCION Y PREVENCION*****************************
	/**
	 * Método implementado para consultar actividades de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultar(Connection con)
	{
		return actividadesDao.consultar(con,campos);
	}
	
	/**
	 * Método implementado para insertar una actividad de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertar(Connection con)
	{
		return actividadesDao.insertar(con,campos);
	}
	
	/**
	 * Método implementado para modificar una actividad de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificar(Connection con)
	{
		return actividadesDao.modificar(con,campos);
	}
	
	/**
	 * Método implementado para eliminar una actividad de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminar(Connection con)
	{
		return actividadesDao.eliminar(con,campos);
	}
	//******************************************************************************************
	/**
	 * @return Returns the campos.
	 */
	public HashMap getCampos() {
		return campos;
	}
	/**
	 * @param campos The campos to set.
	 */
	public void setCampos(HashMap campos) {
		this.campos = campos;
	}
	/**
	 * @return Retorna un elemento del mapacampos.
	 */
	public Object getCampos(String key) {
		return campos.get(key);
	}
	/**
	 * @param campos The campos to set.
	 */
	public void setCampos(String key,Object obj) {
		this.campos.put(key,obj);
	}


	
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------Funciones de La funcionalidad de Actividades de Promocion Y Prevencion por Centros de Atención-------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Metodo para consultar toda la informacion relacionada con la funcionalidad.
	 */
	public HashMap consultarInformcion(Connection con, HashMap mapaParam) 
	{
		return actividadesDao.consultarInformacion(con, mapaParam);
	}
	
	/**
	 * 
	 * @param con
	 * @param centroAtencion 
	 * @param usuario 
	 * @param mapaCA
	 * @return
	 * @throws SQLException 
	 */
	public int insertarActividadesCentroAtencion(Connection con, HashMap mapa, int centroAtencion, int institucion, UsuarioBasico usuario) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (actividadesDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (ActividadesPyp - insertarActividadesCentroAtencion)"); 
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		//-- Barrer el MAPA que contiene la Informacion Nueva.
		int nroRows = 0;
		if ( UtilidadCadena.noEsVacio(mapa.get("nroRegistrosNv")+"") )
		{
			nroRows = Integer.parseInt(mapa.get("nroRegistrosNv")+"");
			
			for (int i=0; i<nroRows; i++)
			{
				
				if ( UtilidadTexto.getBoolean( mapa.get("activo_act_nv_" + i) +"" ) )
				{
					int codigoActividad = Integer.parseInt(mapa.get("cod_act_nv_" + i)+"");
					resp1=actividadesDao.insertarActividadesCentroAtencion(con, 0, centroAtencion, codigoActividad, institucion, false); //-El cero es para que inserte.
					if (resp1 < 1) { break; }
				}	
			}
		}	

		//-- Verificar si hubo cambios en la informacion ya registrada.  
		nroRows = 0;
		if ( UtilidadCadena.noEsVacio(mapa.get("numRegistrosActReg")+"") )
		{
			nroRows = Integer.parseInt(mapa.get("numRegistrosActReg")+"");
			
			for (int i=0; i<nroRows; i++)
			{
				
				//-- Verificar que se Haya modificado  el regitro.   
				if ( !(mapa.get("activo_reg_" + i)+"").equals(mapa.get("h_activo_reg_" + i)+"") ) 
				{
					int codigoActividad = Integer.parseInt(mapa.get("codigo_reg_act_" + i)+""); 

					
					resp1=actividadesDao.insertarActividadesCentroAtencion(con, 1, centroAtencion,  codigoActividad, institucion, UtilidadTexto.getBoolean(mapa.get("activo_reg_" + i)+"") );

					//-- Si se modifico la tabla se debe generar el Log.	
					if ( resp1 > 0 )
					{
						  StringBuffer log = new StringBuffer();
						  log.append("\n=========== MODIFICACIÓN ACTIVIDAD PROMOCIÓN Y PREVENCIÓN POR CENTRO DE ATENCIÓN ======================");
						  log.append("\n CODIGO ACTIVIDAD 	 : " +  mapa.get("codigo_reg_act_" + i) );
						  log.append("\n NOMBRE ACTIVIDAD	 : " +  mapa.get("nombre_reg_act_" + i) );
						  
						  if ( UtilidadTexto.getBoolean(mapa.get("h_activo_reg_" + i)+"") )
						  {
							  log.append("\n ESTADO ANTERIOR	 : ACTIVO " );
							  log.append("\n ESTADO NUEVO	 	 : INACTIVO " );
						  }
						  else
						  {
							  log.append("\n ESTADO ANTERIOR	 : INACTIVO " );
							  log.append("\n ESTADO NUEVO	 	 : ACTIVO " );
						  }
						  log.append("\n=======================================================================================================\n");
						
						  //-- Generar el log. 
						  LogsAxioma.enviarLog(ConstantesBD.logActividadesPypCentroAtencionCodigo, log.toString(), ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
					}
					
					
					if (resp1 < 1) { break; }
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
	 * Metodo para eliminar una Actividad PYP para un centro de Atencion Especifico.  
	 * @param con
	 * @param login 
	 * @param i 
	 * @param map 
	 * @param actividadesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	
	public int eliminarActividadesCentroAtencion(Connection con, int posEliminar, HashMap mapa, int centroAtencion, int codigoActividad, int institucion, String login) throws SQLException
	{
		if (actividadesDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (ActividadesPyp - insertarActividadesCentroAtencion)"); 
		}
		
		int resp = actividadesDao.eliminarActividadesCentroAtencion(con, centroAtencion, codigoActividad, institucion);
		
		//-- Si se ELimino se debe generar el Log.	
		if ( resp > 0 )
		{
			
			  StringBuffer log = new StringBuffer();
			  log.append("\n=========== ELIMINACIÓN ACTIVIDAD PROMOCIÓN Y PREVENCIÓN POR CENTRO DE ATENCIÓN ======================");
			  log.append("\n CODIGO ACTIVIDAD 	 : " +  mapa.get("codigo_reg_act_" + posEliminar) );
			  log.append("\n NOMBRE ACTIVIDAD	 : " +  mapa.get("nombre_reg_act_" + posEliminar) );
			  
			  if ( UtilidadTexto.getBoolean(mapa.get("activo_reg_" + posEliminar)+"") )
			  {
				  log.append("\n ESTADO : ACTIVO " );
			  }
			  else
			  {
				  log.append("\n ESTADO : INACTIVO " );
			  }
			  log.append("\n=======================================================================================================\n");
			
			  //-- Generar el log. 
			  LogsAxioma.enviarLog(ConstantesBD.logActividadesPypCentroAtencionCodigo, log.toString(), ConstantesBD.tipoRegistroLogEliminacion, login);
		}
		
		//--
		return resp;
	}
	
	
	
}
