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


import com.princetonsa.actionform.pyp.ProgramaArticuloPypForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.pyp.ActividadesPypDao;
import com.princetonsa.dao.pyp.ProgramaArticuloPypDao;
import com.princetonsa.mundo.UsuarioBasico;

public class ProgramaArticuloPyp {

	
	/**
	 * DAO para el manejo de ActividadesPypDao
	 */
	ProgramaArticuloPypDao programaArticuloPypDao = null;

	   /**
     * 
     */
    private HashMap viasIngreso;
    
    /**
     * 
     */
    private String regimenGrupoEtareo;
    
    
    /**
     * 
     */
    private HashMap gruposEtareos;
    
    

	/**
	 * Constructor
	 */
	public ProgramaArticuloPyp() 
	{
		this.init(System.getProperty("TIPOBD"));
		this.viasIngreso=new HashMap();
		this.viasIngreso.put("numRegistros","0");
		this.regimenGrupoEtareo="";
		this.gruposEtareos=new HashMap();
		this.gruposEtareos.put("numRegistros","0");
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (programaArticuloPypDao == null) 
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			programaArticuloPypDao = myFactory.getProgramaArticuloPypDao();
		}	
	}
	
	/**
	 * Metodo para cargar la información General de la Funcionalidad 
	 * @param con
	 * @return
	 */
	
	public HashMap consultarInformacion(Connection con, HashMap mapaParam)
	{
		return programaArticuloPypDao.consultarInformacion(con, mapaParam);
	}

	/**
	 * Metodo para Guardar Modificar Los Articulos por Programa PYP
	 * @param con
	 * @param mapa
	 * @param programa
	 * @param nombrePrograma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	public int insertarArticulosPrograma(Connection con, HashMap mapa, String programa, String  nombrePrograma, UsuarioBasico usuario) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (programaArticuloPypDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (ProgramaArticuloPyp - insertarArticulosPrograma)"); 
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
				
				if ( UtilidadTexto.getBoolean( mapa.get("act_art_nv_" + i) +"" ) )
				{
					int codigoArticulo = Integer.parseInt(mapa.get("cod_art_nv_" + i)+"");
					resp1=programaArticuloPypDao.insertarActividadesCentroAtencion(con, 0, programa, codigoArticulo, usuario.getCodigoInstitucionInt(), false); //-El cero es para que inserte.
					if (resp1 < 1) { break; }
				}	
			}
		}	

		//-- Verificar si hubo cambios en la informacion ya registrada.
		nroRows = 0;
		if ( UtilidadCadena.noEsVacio(mapa.get("nroRegistrosReg")+"") )
		{
			nroRows = Integer.parseInt(mapa.get("nroRegistrosReg")+"");
			
			for (int i=0; i<nroRows; i++)
			{
				//-- Verificar que se Haya modificado  el regitro.   
				if ( !(mapa.get("act_act_reg_" + i)+"").equals(mapa.get("h_act_act_reg_" + i)+"") ) 
				{
					int codigoArticulo = Integer.parseInt(mapa.get("cod_act_reg_" + i)+"");
					
					
					resp1=programaArticuloPypDao.insertarActividadesCentroAtencion(con, 1, programa, codigoArticulo, usuario.getCodigoInstitucionInt(), UtilidadTexto.getBoolean(mapa.get("act_act_reg_" + i)+"")); //-El cero es para que inserte.
					
					if (resp1 < 1) { break; }
					
					//-- Si se modifico la tabla se debe generar el Log.	
					if ( resp1 > 0 )
					{
						  StringBuffer log = new StringBuffer();
						  log.append("\n=========== Modificación de un Articulo para un PROGRAMA DE SALUD PYP ======================");
						  log.append("\n CODIGO PROGRAMA  : " +  programa );
						  log.append("\n NOMBRE PROGRAMA  : " +  nombrePrograma );
						  log.append("\n CODIGO ARTíCULO  : " +  mapa.get("cod_act_reg_" + i) );
						  log.append("\n NOMBRE ARTíCULO  : " +  mapa.get("nom_act_reg_" + i) );
						  
						  if ( UtilidadTexto.getBoolean(mapa.get("h_act_act_reg_" + i)+"") )
						  {
							  log.append("\n ESTADO ANTERIOR : ACTIVO " );
							  log.append("\n ESTADO NUEVO	 : INACTIVO " );
						  }
						  else
						  {
							  log.append("\n ESTADO ANTERIOR : INACTIVO " );
							  log.append("\n ESTADO NUEVO	 : ACTIVO  " );
						  }
						  log.append("\n=======================================================================================================\n");
						
						  //-- Generar el log. 
						  LogsAxioma.enviarLog(ConstantesBD.logProgramaArticuloPypCodigo, log.toString(), ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
						 
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
	 * Metodo para eliminar una Articulo de un Programa de Salud PYP Especifico. 
	 * @param con
	 * @param indiceEliminado 
	 * @param forma 
	 * @param programa
	 * @param nombrePrograma 
	 * @param nroArticuloEliminado
	 * @param codigoInstitucionInt
	 * @return
	 * @throws SQLException 
	 */
	public int eliminarActividadesCentroAtencion(Connection con, ProgramaArticuloPypForm forma, UsuarioBasico usuario) throws SQLException 
	{
		if (programaArticuloPypDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (ProgramaArticuloPyp - eliminarActividadesCentroAtencion)"); 
		}
		
		int resp = programaArticuloPypDao.eliminarActividadesCentroAtencion(con, forma.getPrograma(), forma.getNroArticuloEliminado(), usuario.getCodigoInstitucionInt());
		
		if ( resp > 0 )
		{
			  StringBuffer log = new StringBuffer();
			  log.append("\n=========== ELiminación de un Articulo para un PROGRAMA DE SALUD PYP ======================");
			  log.append("\n CODIGO PROGRAMA  : " +  forma.getPrograma() );
			  log.append("\n NOMBRE PROGRAMA  : " +  forma.getNombrePrograma() );
			  log.append("\n CODIGO ARTíCULO  : " +  forma.getMapa("cod_act_reg_" + forma.getIndiceEliminado()) );
			  log.append("\n NOMBRE ARTíCULO  : " +  forma.getMapa("nom_act_reg_" +  forma.getIndiceEliminado()) );
			  
			  if ( UtilidadTexto.getBoolean(forma.getMapa("h_act_act_reg_" + forma.getIndiceEliminado())+"") )
			  {
				  log.append("\n ESTADO    : ACTIVO " );
			  }
			  else
			  {
				  log.append("\n ESTADO    : INACTIVO " );
			  }
			  log.append("\n=======================================================================================================\n");
			
			  //-- Generar el log. 
			  LogsAxioma.enviarLog(ConstantesBD.logProgramaArticuloPypCodigo, log.toString(), ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
			 
		} 
		
		return resp;
	}

	public HashMap getGruposEtareos()
	{
		return gruposEtareos;
	}

	public void setGruposEtareos(HashMap gruposEtareos)
	{
		this.gruposEtareos = gruposEtareos;
	}

	public ProgramaArticuloPypDao getProgramaArticuloPypDao()
	{
		return programaArticuloPypDao;
	}

	public void setProgramaArticuloPypDao(
			ProgramaArticuloPypDao programaArticuloPypDao)
	{
		this.programaArticuloPypDao = programaArticuloPypDao;
	}

	public String getRegimenGrupoEtareo()
	{
		return regimenGrupoEtareo;
	}

	public void setRegimenGrupoEtareo(String regimenGrupoEtareo)
	{
		this.regimenGrupoEtareo = regimenGrupoEtareo;
	}

	public HashMap getViasIngreso()
	{
		return viasIngreso;
	}

	public void setViasIngreso(HashMap viasIngreso)
	{
		this.viasIngreso = viasIngreso;
	}

	/**
	 * 
	 * @param con
	 * @param programa
	 * @param mapa
	 * @param codigoInstitucion
	 */
	public void cargarViasIngreso(Connection con, String programa, String actividad, String institucion)
	{
		this.setViasIngreso(programaArticuloPypDao.cargarViasIngresoActividadPrograma(con,programa,actividad,institucion));
	}

	
	public boolean eliminarRegistroViaIngreso(Connection con, String programa, String actividad, String institucion, String viaIngreso, String ocupacion) 
	{
		return programaArticuloPypDao.eliminarRegistroViaIngreso(con,programa,actividad,institucion,viaIngreso,ocupacion);
	}

	public boolean modificarRegistroViaIngreso(Connection con, String programa, String actividad, String institucion, String viaIngreso, String ocupacion, boolean solicitar,boolean programar,boolean ejecutar)
	{
		return programaArticuloPypDao.modificarRegistroViaIngreso(con,programa,actividad,institucion,viaIngreso,ocupacion,solicitar,programar,ejecutar);
	}

	public boolean insertarRegistroViaIngreso(Connection con, String programa, String actividad, String institucion, String viaIngreso, String ocupacion, boolean solicitar,boolean programar,boolean ejecutar)
	{
		return programaArticuloPypDao.insertarRegistroViaIngreso(con,programa,actividad,institucion,viaIngreso,ocupacion,solicitar,programar,ejecutar);
	}

	public void cargarGruposEtareos(Connection con, String programa, String actividad, String institucion, String regimen) 
	{
		this.setGruposEtareos(programaArticuloPypDao.cargarGruposEtareos(con,programa,actividad,institucion,regimen));
	}

	
	public boolean eliminarRegistroGrupoEtareo(Connection con, String programa, String actividad, String institucion, String regimen, String grupoEtareo) 
	{
		return programaArticuloPypDao.eliminarRegistroGrupoEtareo(con,programa,actividad,institucion,regimen,grupoEtareo);
	}

	public boolean modificarRegistroGrupoEtareo(Connection con, String programa, String actividad, String institucion, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia)
	{
		return programaArticuloPypDao.modificarRegistroGrupoEtareo(con,programa,actividad,institucion,regimen,grupoEtareo,frecuencia,tipoFrecuencia);
	}

	public boolean insertarRegistroGrupoEtareo(Connection con, String programa, String actividad, String institucion, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia)
	{
		return programaArticuloPypDao.insertarRegistroGrupoEtareo(con,programa,actividad,institucion,regimen,grupoEtareo,frecuencia,tipoFrecuencia);
	}

}
