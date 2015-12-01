/*
 * Creado el Aug 8, 2006
 * por Julian Montoya
 */
package com.princetonsa.mundo.pyp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadCadena;
import util.UtilidadTexto;

import com.princetonsa.actionform.pyp.TipoCalificacionPypForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.pyp.TipoCalificacionPypDao;
import com.princetonsa.mundo.UsuarioBasico;

public class TipoCalificacionPyp {

	
	/**
	 * Variable para el acceso a la BD. 
	 */
	TipoCalificacionPypDao tipoCalificacionPypDao = null;


	/**
	 * Constructor
	 */
	public TipoCalificacionPyp() 
	{
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (tipoCalificacionPypDao == null) 
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tipoCalificacionPypDao = myFactory.getTipoCalificacionPypDao();
		}	
	}

	
	/**
	 * Metodo para cargar la información General de la Funcionalidad 
	 * @param con
	 * @return
	 */
	
	public HashMap consultarInformacion(Connection con, HashMap mapaParam)
	{
		return tipoCalificacionPypDao.consultarInformacion(con, mapaParam);
	}

	/**
	 * Para Insertar los tipos de calificacion de Metas de PYP
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	public int insertarTipoCalificacion(Connection con, TipoCalificacionPypForm forma, UsuarioBasico usuario) throws SQLException 
	{
		HashMap mapa = forma.getMapa();
		
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (tipoCalificacionPypDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (TipoCalificacionPyp - insertarTipoCalificacion)"); 
		}
		
		//-- Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		//-- Barrer el MAPA que contiene la Informacion Nueva.
		int nroRows = 0;
		if ( UtilidadCadena.noEsVacio(mapa.get("nroRegistrosNv")+"") )
		{
			nroRows = Integer.parseInt(mapa.get("nroRegistrosNv")+"");
			for (int i=0; i<nroRows; i++)
			{
				if ( UtilidadTexto.getBoolean( mapa.get("tc_act_nv_" + i) +"" ) )
				{
					String codigo = mapa.get("tc_cod_nv_" + i)+"";
					String nombre = mapa.get("tc_nom_nv_" + i)+"";
					
					
					
					//-------------------------------------------------(Insertar/Modificar) (Consecutivo)
					resp1 = tipoCalificacionPypDao.insertarTipoCalificacion(con, 0, 0, codigo, nombre, usuario.getCodigoInstitucionInt(), false); //-El primer cero es para que inserte.
					if (resp1 < 1) { break; }
				}	
			}
		}	

		//-- Verificar si hubo cambios en la informacion ya registrada.
		nroRows = 0;
		if ( UtilidadCadena.noEsVacio(mapa.get("numRegistros")+"") )
		{
			nroRows = Integer.parseInt(mapa.get("numRegistros")+"");
			
			for (int i=0; i<nroRows; i++)
			{
				//-- Verificar que se Haya modificado  el regitro.   
				if ( 
					 !(mapa.get("tc_cod_" + i)+"").equals(mapa.get("h_tc_cod_" + i)+"") || 
					 !(mapa.get("tc_nom_" + i)+"").equals(mapa.get("h_tc_nom_" + i)+"") ||	
					 !(mapa.get("tc_act_" + i)+"").equals(mapa.get("h_tc_act_" + i)+"") 	
				   ) 
				{
					int consecutivo = Integer.parseInt(mapa.get("tc_conse_" + i)+"");
					String codigo = mapa.get("tc_cod_" + i)+"";
					String nombre = mapa.get("tc_nom_" + i)+"";
					String codigoAnt = mapa.get("h_tc_cod_" + i)+"";
					String nombreAnt = mapa.get("h_tc_nom_" + i)+"";
					
					
					//-- El uno es para modificar 
					resp1 = tipoCalificacionPypDao.insertarTipoCalificacion(con, 1, consecutivo, codigo, nombre, usuario.getCodigoInstitucionInt(), UtilidadTexto.getBoolean(mapa.get("tc_act_" + i)+"") ); 
					if (resp1 < 1) { break; }
					
					//-- Si se modifico la tabla se debe generar el Log.	
					if ( resp1 > 0 )
					{

						  StringBuffer log = new StringBuffer();
						  log.append("\n=========== Modificación de un Tipo Califición de Metas PYP ======================");
						 
						  
								  log.append("\n Codigo Anterior : " +  codigoAnt );
								  log.append("\n Codigo Nuevo : " +  codigo );
								  log.append("\n Nombre Anterior : " +  nombreAnt );
								  log.append("\n Nombre Nuevo : " +  nombre );
								  
								  if (  !(mapa.get("tc_act_" + i)+"").equals(mapa.get("h_tc_act_" + i)+"")  )
								  {
									  if ( UtilidadTexto.getBoolean(mapa.get("tc_act_" + i)+"") )
									  {
										  log.append("\n Estado Anterior : INACTIVO ");
										  log.append("\n Estado Nuevo : ACTIVO ");
									  }
									  else
									  {
										  log.append("\n Estado Anterior : ACTIVO ");
										  log.append("\n Estado Nuevo : INACTIVO");
									  }
								  }
						  //-- 		  
						  log.append("\n===================================================================================\n");
						
						  //-- Generar el log. 
						  LogsAxioma.enviarLog(ConstantesBD.logTipoCalificacionMetaPypCodigo, log.toString(), ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
					} 
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
	 * Met
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	public int eliminarTipoCalificacion(Connection con, TipoCalificacionPypForm forma , UsuarioBasico usuario) throws SQLException 
	{
		if (tipoCalificacionPypDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (TipoCalificacionPyp - eliminarTipoCalificacion)"); 
		}
		
		int codigoTipoCal = Integer.parseInt(forma.getMapa("tc_conse_" + forma.getIndiceEliminado()) + "");

		int resp = tipoCalificacionPypDao.eliminarTipoCalificacion(con, codigoTipoCal);

		//-- Si se elimino se debe generar el Log.	
		if ( resp > 0 )
		{
			  StringBuffer log = new StringBuffer();
			  log.append("\n=========== Eliminacion de un Tipo Califición de Metas PYP ======================");
			  log.append("\n Codigo : " + forma.getMapa("tc_cod_" +forma.getIndiceEliminado()) );
			  log.append("\n Nombre : " + forma.getMapa("tc_nom_" +forma.getIndiceEliminado()) );
		
			  if ( UtilidadTexto.getBoolean(forma.getMapa("tc_act_" + forma.getIndiceEliminado())+"") )
			  {
				  log.append("\n Estado : ACTIVO ");
			  }
			  else
			  {
				  log.append("\n Estado : INACTIVO ");
			  }

			  //--
			  log.append("\n===================================================================================\n");
			
			  //-- Generar el log.
			  LogsAxioma.enviarLog(ConstantesBD.logTipoCalificacionMetaPypCodigo, log.toString(), ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
		} 
		
		return resp;
	}
	
	
	
}
