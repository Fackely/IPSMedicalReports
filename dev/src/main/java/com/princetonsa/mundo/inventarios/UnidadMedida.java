/*
 * Creado en May 31, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.UnidadMedidaDao;
import com.princetonsa.mundo.UsuarioBasico;

public class UnidadMedida
{
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private UnidadMedidaDao  unidadMedidaDao = null;
	
//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	/**
	 * Mapa que contiene la información de las unidades de medida para la institución
	 */
	private HashMap mapa;	
//	---------------------------------------------------FIN DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
	 * Constructor de la clase, inicializa en vacío todos los atributos
	 */
	public UnidadMedida ()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Este método inicializa los atributos de la clase con valores vacíos
	 */
	public void reset()
	{
		this.mapa=new HashMap();
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
			unidadMedidaDao = myFactory.getUnidadMedidaDao();
			wasInited = (unidadMedidaDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Método que cargar las unidades de medida parametrizadas a la institución
	 * @param con
	 * @return HashMap
	 */
	public HashMap consultarUnidadesMedidaInstitucion(Connection con)
	{
		return unidadMedidaDao.consultarUnidadesMedidaInstitucion(con);
	}
	
	/**
	 * Método que guarda las nuevas unidades de medida y modifica
	 * las unidades de medida ya ingresadas 
	 * @param con
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	public boolean insertarModificarUnidadMedida(Connection con, UsuarioBasico usuario) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean error=false;
		
		if (unidadMedidaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (UnidadMedidaDao - insertarModificarUnidadMedida )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if (this.getMapa() != null)
			{
			int numRegistros=Integer.parseInt(this.getMapa("numRegistros")+"");
			for (int i=0; i<numRegistros; i++)
				{
					String estaGrabado=this.getMapa("esta_grabado_"+i)+"";
					//----------Se verifica si el registro está grabado para así insertarlo (1 -> Esta grabado,  0->Está grabado)---------//
					if (estaGrabado.equals("0"))
						{
							String acronimo=this.getMapa("acronimo_"+i)+"";
							String unidad=this.getMapa("unidad_"+i)+"";
							
							boolean unidosis=false;
							if (UtilidadTexto.getBoolean(this.getMapa("unidosis_"+i)+""))
								{
									unidosis=true;
								}
							
							boolean activo=false;
							if (UtilidadTexto.getBoolean(this.getMapa("activo_"+i)+""))
								{
									activo=true;
								}
							
							//----------Se inserta la nueva unidad de medida -------------//
							unidadMedidaDao.insertarModificarUnidadMedida (con, acronimo, unidad, unidosis, activo, acronimo, true);
						}
					else
					{
						String acronimo=this.getMapa("acronimo_"+i)+"";
						String acronimoAnt=this.getMapa("acronimoant_"+i)+"";
						String unidad=this.getMapa("unidad_"+i)+"";
						String unidadAnt=this.getMapa("unidadant_"+i)+"";
						String unidosis=this.getMapa("unidosis_"+i)+"";
						String unidosisAnt=this.getMapa("unidosisant_"+i)+"";
						String activo=this.getMapa("activo_"+i)+"";
						String activoAnt=this.getMapa("activoant_"+i)+"";
						
						if (!acronimo.trim().equals(acronimoAnt.trim()) || !unidad.trim().equals(unidadAnt.trim()) || UtilidadTexto.getBoolean(unidosis)!=UtilidadTexto.getBoolean(unidosisAnt) || UtilidadTexto.getBoolean(activo)!=UtilidadTexto.getBoolean(activoAnt))
							{
								//----------Se actualiza la unidad de medida -------------//
								unidadMedidaDao.insertarModificarUnidadMedida (con, acronimo, unidad, UtilidadTexto.getBoolean(unidosis), UtilidadTexto.getBoolean(activo), acronimoAnt, false);
								
								//-----------------------------------------------GENERACION DEL LOG AL MODIFICAR --------------------------------------------------//
								StringBuffer log=new StringBuffer();
								log.append("\n===============MODIFICACIÓN DE UNIDAD DE MEDIDA================");
								if (!acronimo.trim().equals(acronimoAnt.trim()))
								{
									log.append("\n ACRONIMO ANTERIOR :"+acronimoAnt);
								    log.append("\n ACRONIMO NUEVO :"+acronimo+"\n");
								}
																
								if(!unidad.trim().equals(unidadAnt.trim()))
								{
									log.append("\n UNIDAD ANTERIOR :"+unidadAnt);
								    log.append("\n UNIDAD NUEVA :"+unidad+"\n");
								}
								else
								{
									log.append("\n UNIDAD DE MEDIDA :"+unidad+"\n");
								}
								
								if(UtilidadTexto.getBoolean(unidosis)!=UtilidadTexto.getBoolean(unidosisAnt))
								{
									 if (UtilidadTexto.getBoolean(unidosisAnt))
										    log.append("\n UNIDOSIS ANTERIOR : [SI]");
									   else
										   log.append("\n UNIDOSIS ANTERIOR : [NO]");
									   
									   if (UtilidadTexto.getBoolean(unidosis))
										    log.append("\n UNIDOSIS NUEVA : [SI] \n");
									   else
										   log.append("\n UNIDOSIS NUEVA : [NO] \n");
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
								LogsAxioma.enviarLog(ConstantesBD.logUnidadMedidaCodigo, log.toString(), ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
							}
						
					}
				}//for
			}//if mapa!=null
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
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
	
	/**
	 * Método que elimina el registro seleccionado de la unidad de medida
	 * @param con
	 * @param acronimo
	 * @return
	 * @throws SQLException
	 */
	public boolean eliminarUnidadMedida (Connection con, String acronimo) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp=-1;
				
		if (unidadMedidaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (UnidadMedidaDao - eliminarUnidadMedida )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		resp = unidadMedidaDao.eliminarUnidadMedida (con, acronimo);
		
		//Se finaliza la transacción cuando hay error en una eliminación de datos o no se logró inicializar la transacción
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
//	---------------------------------------- SETS Y GETS ---------------------------------------------------//
	/**
	 * @return Retorna the mapa.
	 */
	public HashMap getMapa()
	{
		return mapa;
	}

	/**
	 * @param mapa The mapa to set.
	 */
	public void setMapa(HashMap mapa)
	{
		this.mapa = mapa;
	}
	
	/**
	 * @return Retorna mapa.
	 */
	public Object getMapa(Object key) {
		return mapa.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapa(Object key, Object dato) {
		this.mapa.put(key, dato);
	}
}
