/*
 * Creado en Aug 8, 2006
 * Andr�s Mauricio Ruiz V�lez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.mundo.pyp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.pyp.CalificacionesXCumpliMetasDao;
import com.princetonsa.mundo.UsuarioBasico;

public class CalificacionesXCumpliMetas
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CalificacionesXCumpliMetas.class);
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private CalificacionesXCumpliMetasDao  calificacionesXCumpliMetasDao = null;
	
//	---------------------------------------------------DECLARACI�N DE LOS ATRIBUTOS-----------------------------------------------------------//
	/**
	 * Tipo de r�gimen seleccionado
	 */
	private String tipoRegimen;
	
	/**
	 * Nombre del tipo de r�gimen
	 */
	private String nombreTipoRegimen;
	
	/**
	 * Mapa para guardar la informaci�n de las calificaciones
	 * por cumplimiento de metas por r�gimen
	 */
	private HashMap mapaCumpliMetas;
	
//	---------------------------------------------------FIN DECLARACI�N DE LOS ATRIBUTOS-----------------------------------------------------------//

	
	/**
	 * Constructor de la clase, inicializa en vac�o todos los atributos
	 */
	public CalificacionesXCumpliMetas ()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Este m�todo inicializa los atributos de la clase con valores vac�os
	 */
	public void reset()
	{
		this.tipoRegimen="";
		this.mapaCumpliMetas=new HashMap();
		this.nombreTipoRegimen="";
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			calificacionesXCumpliMetasDao = myFactory.getCalificacionesXCumpliMetasDao();
			wasInited = (calificacionesXCumpliMetasDao != null);
		}
		return wasInited;
	}

//	-----------------------------------------------------METODOS --------------------------------------------------------------------------//
	
	/**
	 * M�todo que consulta las calificaciones por cumplimiento de metas ingresadas para el 
	 * r�gimen seleccionado y la instituci�n actual
	 * @param con
	 * @param tipoRegimen
	 * @param codigoInstitucion
	 */
	public HashMap consultarCalificacionesXRegimen(Connection con, String tipoRegimen, int codigoInstitucion)
	{
		return calificacionesXCumpliMetasDao.consultarCalificacionesXRegimen (con, tipoRegimen, codigoInstitucion);
	}

	/**
	 * M�todo que elimina el registro seleccionado de la tabla
	 * @param con
	 * @param codigoCalificacion
	 * @return
	 * @throws SQLException 
	 */
	public boolean eliminarCalificacionXCumplimiento(Connection con, int codigoCalificacion) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp=-1;
				
		if (calificacionesXCumpliMetasDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexi�n con la fuente de datos (CalificacionesXCumpliMetasDao - eliminarCalificacionXCumplimiento )");
		}
		
		//Iniciamos la transacci�n
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		resp = calificacionesXCumpliMetasDao.eliminarCalificacionXCumplimiento (con, codigoCalificacion);
		
		
		//Se finaliza la transacci�n cuando hay error en la eliminaci�n o no se logr� inicializar la transacci�n
		if (!inicioTrans || resp<0)
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
	 * M�todo que guarda o actualiza las calificaciones de cumplimieto por metas
	 * registradas en un tipo de r�gimen y una instituci�n
	 * @param con
	 * @param usuario
	 * @throws SQLException 
	 */
	public boolean insertarModificarCalificacionsXCumplimiento(Connection con, UsuarioBasico usuario) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean error=false;
		int resp=0;
		
		if (calificacionesXCumpliMetasDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexi�n con la fuente de datos (CalificacionesXCumpliMetasDao - insertarModificarCalificacionsXCumplimiento )");
		}
		
		//Iniciamos la transacci�n
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if (this.getMapaCumpliMetas() != null)
			{
			int numRegistros=Integer.parseInt(this.getMapaCumpliMetas("numRegistros")+"");
						
			
			for (int i=0; i<numRegistros; i++)
			{
				String estaGrabado=this.getMapaCumpliMetas("esta_grabado_"+i)+"";
				
				//----------Se verifica si el registro est� grabado para as� insertarlo (1 -> Esta grabado,  0->No Est� grabado)---------//
				if (estaGrabado.equals("0"))
					{
						String meta=this.getMapaCumpliMetas("meta_"+i)+"";
						String rangoInicial=this.getMapaCumpliMetas("rango_inicial_"+i)+"";
						String rangoFinal=this.getMapaCumpliMetas("rango_final_"+i)+"";
						int tipoCalificacion=Integer.parseInt(this.getMapaCumpliMetas("tipo_calificacion_"+i)+"");
						
						boolean activo=false;
						if (UtilidadTexto.getBoolean(this.getMapaCumpliMetas("activo_"+i)+""))
							{
								activo=true;
							}
						
						//-----------Se inserta la calificaci�n por cumplimiento de metas ---------//
						resp=calificacionesXCumpliMetasDao.insertarModificarCalificacionsXCumplimiento (con, 0, this.tipoRegimen, meta, rangoInicial, rangoFinal, tipoCalificacion, activo, usuario.getCodigoInstitucionInt(), true);
						
						if(resp < 0)
						{
							error=true;
							break;
						}
					}//if estaGrabado
				else
				{
					int codigo=Integer.parseInt(this.getMapaCumpliMetas("codigo_"+i)+"");
					float meta=Float.parseFloat(this.getMapaCumpliMetas("meta_"+i)+"");	
					float metaAnt=Float.parseFloat(this.getMapaCumpliMetas("metaant_"+i)+"");
					float rangoInicial=Float.parseFloat(this.getMapaCumpliMetas("rango_inicial_"+i)+"");	
					float rangoInicialAnt=Float.parseFloat(this.getMapaCumpliMetas("rango_inicialant_"+i)+"");
					float rangoFinal=Float.parseFloat(this.getMapaCumpliMetas("rango_final_"+i)+"");	
					float rangoFinalAnt=Float.parseFloat(this.getMapaCumpliMetas("rango_finalant_"+i)+"");
					String tipoCalificacion=this.getMapaCumpliMetas("tipo_calificacion_"+i)+"";	
					String tipoCalificacionAnt=this.getMapaCumpliMetas("tipo_calificacionant_"+i)+"";
					String activo=this.getMapaCumpliMetas("activo_"+i)+"";
					String activoAnt=this.getMapaCumpliMetas("activoant_"+i)+"";
					
					//----------Se verifica si se modific� algo para actualizar y generar el log -------------------//
					if (meta!=metaAnt || rangoInicial !=rangoInicialAnt || rangoFinal != rangoFinalAnt || !tipoCalificacion.trim().equals(tipoCalificacionAnt) || UtilidadTexto.getBoolean(activo)!=UtilidadTexto.getBoolean(activoAnt))
					{
						
						//-----------Se actualiza la calificaci�n por cumplimiento de me!rangoFinal.trim().equals(rangoFinalAnt.trim())tas ---------//
						resp=calificacionesXCumpliMetasDao.insertarModificarCalificacionsXCumplimiento (con, codigo, this.tipoRegimen, meta+"", rangoInicial+"", rangoFinal+"", Integer.parseInt(tipoCalificacion), UtilidadTexto.getBoolean(activo), usuario.getCodigoInstitucionInt(), false);
						
						if(resp < 0)
						{
							error=true;
							break;
						}
						else
						{
							//-----------------------------------------------GENERACION DEL LOG AL MODIFICAR --------------------------------------------------//
							StringBuffer log=new StringBuffer();
							log.append("\n=========MODIFICACI�N DE CALIFICACIONES POR CUMPLIMIENTO DE METAS==========");
							
							log.append("\n NOMBRE DEL TIPO DE R�GIMEN:"+this.nombreTipoRegimen+"\n");
							
							if (meta!=metaAnt)
							{
								log.append("\n META ANTERIOR :"+metaAnt);
							    log.append("\n META NUEVA :"+meta+"\n");
							}
							
							if (rangoInicial !=rangoInicialAnt)
							{
								log.append("\n RANGO INICIAL ANTERIOR :"+rangoInicialAnt);
							    log.append("\n RANGO INICIAL NUEVO :"+rangoInicial+"\n");
							}
							
							if (rangoFinal != rangoFinalAnt)
							{
								log.append("\n RANGO FINAL ANTERIOR :"+rangoFinalAnt);
							    log.append("\n RANGO FINAL NUEVO :"+rangoFinal+"\n");
							}
							
							if (!tipoCalificacion.trim().equals(tipoCalificacionAnt.trim()))
							{
								log.append("\n TIPO CALIFICACI�N ANTERIOR :"+Utilidades.obtenerNombreTipoCalificacionPyP(con, Integer.parseInt(tipoCalificacionAnt)));
							    log.append("\n TIPO CALIFICACI�N NUEVO :"+Utilidades.obtenerNombreTipoCalificacionPyP(con, Integer.parseInt(tipoCalificacion))+"\n");
							}
							
							
							if(UtilidadTexto.getBoolean(activo)!=UtilidadTexto.getBoolean(activoAnt))
							{
								 if (UtilidadTexto.getBoolean(activoAnt))
									    log.append("\n ACTIVO ANTERIOR : [SI]");
								   else
									   log.append("\n ACTIVO ANTERIOR : [NO]");
								   
								   if (UtilidadTexto.getBoolean(activo))
									    log.append("\n ACTIVO NUEVO : [SI] \n");
								   else
									   log.append("\n ACTIVO NUEVO : [NO] \n");
							}
							
							log.append("\n==================================================================");
							//-Generar el log 
							LogsAxioma.enviarLog(ConstantesBD.logCalificacionXCumpliMetasCodigo, log.toString(), ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
							
						}//else
					}//if existi� cambio
				}//else
			}//for
			
			}//if mapa es != null
		
		//Se finaliza la transacci�n cuando hay error en una inserci�n de datos o no se logr� inicializar la transacci�n
		if (!inicioTrans || error)
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
	
//	---------------------------------------- SETS Y GETS ---------------------------------------------------//

	/**
	 * @return Retorna the tipoRegimen.
	 */
	public String getTipoRegimen()
	{
		return tipoRegimen;
	}

	/**
	 * @param tipoRegimen The tipoRegimen to set.
	 */
	public void setTipoRegimen(String tipoRegimen)
	{
		this.tipoRegimen = tipoRegimen;
	}
	
	/**
	 * @return Retorna the mapaCumpliMetas.
	 */
	public HashMap getMapaCumpliMetas()
	{
		return mapaCumpliMetas;
	}

	/**
	 * @param mapaCumpliMetas The mapaCumpliMetas to set.
	 */
	public void setMapaCumpliMetas(HashMap mapaCumpliMetas)
	{
		this.mapaCumpliMetas = mapaCumpliMetas;
	}
	
	/**
	 * @return Retorna mapaCumpliMetas.
	 */
	public Object getMapaCumpliMetas(String key) {
		return mapaCumpliMetas.get( key );
	}

	/**
	 * @param Asigna mapaCumpliMetas.
	 */
	public void setMapaCumpliMetas(String key, String dato) {
		this.mapaCumpliMetas.put(key, dato);
	}

	/**
	 * @return Retorna the nombreTipoRegimen.
	 */
	public String getNombreTipoRegimen()
	{
		return nombreTipoRegimen;
	}

	/**
	 * @param nombreTipoRegimen The nombreTipoRegimen to set.
	 */
	public void setNombreTipoRegimen(String nombreTipoRegimen)
	{
		this.nombreTipoRegimen = nombreTipoRegimen;
	}

}
