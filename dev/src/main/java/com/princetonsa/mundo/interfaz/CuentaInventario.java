/*
 * Creado en Apr 18, 2006
 * Andr�s Mauricio Ruiz V�lez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.mundo.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadCadena;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.CuentaInventarioDao;


public class CuentaInventario
{
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private CuentaInventarioDao  cuentaInventarioDao = null;
	
//	---------------------------------------------------DECLARACI�N DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
	 * Centro de costo que se selecciona en la primera p�gina de clase de inventario
	 */
	private int centroCostoSeleccionado;
	
	/**
	 * C�digo de la clase de inventario seleccionado
	 */
	private int codigoClaseInventario;
	
	/**
	 * Mapa para guardar informaci�n de las clases de inventarios de la cuenta de ingreso
	 */
	private HashMap mapaClaseInventarios=new HashMap();
	
	/**
	 * Mapa para guardar informaci�n de los grupos de inventarios de la cuenta de ingreso
	 */
	private HashMap mapaGrupoInventarios=new HashMap();
	
	/**
	 * Mapa para guardar informaci�n de los subgrupos de inventarios de la cuenta de ingreso
	 */
	private HashMap mapaSubGrupoInventarios=new HashMap();
	
	/**
	 * Mapa para guardar informaci�n de los articulos de inventarios de la cuenta de ingreso
	 */
	private HashMap mapaArticuloInventarios=new HashMap();
	
//	-------------------- Eliminaci�n de cuenta contable ----------------------//
	/**
	 * Codigo del clase de inventario a la cu�l se le elimina la cuenta contable
	 */
	private int claseInventarioEliminar;
	
	/**
	 * Codigo del grupo de inventario a la cu�l se le elimina la cuenta contable
	 */
	private int grupoInventarioEliminar;
	
	/**
	 * Codigo del subGrupo de inventario a la cu�l se le elimina la cuenta contable
	 */
	private int subGrupoInventarioEliminar;
	
	/**
	 * Codigo del art�culo de inventario a la cu�l se le elimina la cuenta contable
	 */
	private int articuloInventarioEliminar;
	
	/**
	 * Nombre de la cuenta contable que se elimina
	 */
	private String nombreCuentaEliminar;
	
	/**
	 * Posici�n en el mapa del registro escogido al eliminar la cuenta contable
	 */
	private int posicionEliminar;
	

	/**
	 * Nombre de la cuenta vigencia anterior que se elimina
	 */
	private String nombreCuentaVigenciaAnteriorEliminar;
	
//	---------------------------------------------------FIN DE LA DECLARACI�N DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
	 * Constructor de la clase, inicializa en vac�o todos los atributos
	 */
	public CuentaInventario ()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Este m�todo inicializa los atributos de la clase con valores vac�os
	 */
	public void reset()
	{
		//----------Eliminaci�n de cuenta contable -------------//
		this.claseInventarioEliminar=ConstantesBD.codigoNuncaValido;
		this.grupoInventarioEliminar=ConstantesBD.codigoNuncaValido;
		this.subGrupoInventarioEliminar=ConstantesBD.codigoNuncaValido;
		this.articuloInventarioEliminar=ConstantesBD.codigoNuncaValido;
		this.nombreCuentaEliminar="";
		this.nombreCuentaVigenciaAnteriorEliminar= "";
		this.posicionEliminar=ConstantesBD.codigoNuncaValido;
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
			cuentaInventarioDao = myFactory.getCuentaInventarioDao();
			wasInited = (cuentaInventarioDao != null);
		}
		return wasInited;
	}

	/**
	 * M�todo para consultar las clases de inventario parametrizados para el centro de costo y 
	 * la instituci�n con la informaci�n ingresada de la cuenta de ingreso.
	 * @param con
	 * @param centroCostoSeleccionado
	 * @param institucion
	 * @return HashMap l
	 */
	
	public HashMap consultarClaseInventariosCuentaIngreso(Connection con, int centroCostoSeleccionado, int institucion)
	{
		return cuentaInventarioDao.consultarClaseInventariosCuentaIngreso(con, centroCostoSeleccionado, institucion);
	}

	/**
	 * M�todo que carga los grupos de inventario para el centro de costo seleccionado y la 
	 * clase de inventario seleccionada
	 * @param con
	 * @param mapping
	 * @param cuentaInventarioForm
	 * @return HashMap listado grupos inventario 
	 */
	public HashMap consultarGrupoInventariosCuentaIngreso(Connection con, int centroCostoSeleccionado, int claseInventarioSeleccionado)
	{
		return cuentaInventarioDao.consultarGrupoInventariosCuentaIngreso(con, centroCostoSeleccionado, claseInventarioSeleccionado);
	}

	/**
	 * M�todo que carga los subgrupos de inventario para el centro de costo seleccionado y el 
	 * grupo de inventario seleccionado
	 * @param con
	 * @param centroCostoSeleccionado
	 * @param grupoInventarioSeleccionado
	 * @return
	 */
	public HashMap consultarSubGrupoInventariosCuentaIngreso(Connection con, int centroCostoSeleccionado, int grupoInventarioSeleccionado)
	{
		return cuentaInventarioDao.consultarSubGrupoInventariosCuentaIngreso (con, centroCostoSeleccionado, grupoInventarioSeleccionado);
	}

	/**
	 * M�todo que carga los articulos de inventario para el centro de costo seleccionado y el 
	 * subgrupo de inventario seleccionado
	 * @param con
	 * @param centroCostoSeleccionado
	 * @param subgrupoInventarioSel
	 * @return
	 */
	public HashMap consultarArticulosInventarioCuentaIngreso(Connection con, int centroCostoSeleccionado, int subgrupoInventarioSel)
	{
		return cuentaInventarioDao.consultarArticulosInventarioCuentaIngreso (con, centroCostoSeleccionado, subgrupoInventarioSel);
	}
	
	/**
	 * M�todo para insertar la cuenta de ingreso de cada una de las clases
	 * de inventario para el centro de costo seleccionado
	 * @param con
	 * @param loginUsuario 
	 * @return
	 * @throws SQLException
	 */
	 @SuppressWarnings("unused")
	public boolean insertarClasesInventario(Connection con, String loginUsuario) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int respClaseInv=0;
		int respClaseInvNul=0;
		boolean error=false;
		
		if (cuentaInventarioDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexi�n con la fuente de datos (CuentaInventarioDao - insertarClasesInventario )");
		}
		
		//Iniciamos la transacci�n
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if (this.getMapaClaseInventarios("codsClasesInventario") != null)
		{
			Vector codsClasesInventario=(Vector) this.getMapaClaseInventarios("codsClasesInventario");
			
			for (int i=0; i<codsClasesInventario.size(); i++)
			{
				String cuentaIngresoNueva=(String)this.getMapaClaseInventarios("cuenta_ingreso_"+codsClasesInventario.elementAt(i));
				String cuentaIngresoAnt=(String)this.getMapaClaseInventarios("cuenta_ingresoAnt_"+codsClasesInventario.elementAt(i));
				
				String cuentaVigenciaAnteriorNueva=(String)this.getMapaClaseInventarios("cuenta_ingreso_vig_anterior_"+codsClasesInventario.elementAt(i));
				String cuentaVigenciaAnteriorAnt =(String)this.getMapaClaseInventarios("cuentaIngresoClaseVigenciaAnteAnt_"+codsClasesInventario.elementAt(i));
				//logger.info("cuentaIngresoNueva-->"+cuentaIngresoNueva);
				//logger.info("cuentaIngresoAnt-->"+cuentaIngresoAnt+"\n");
				
					int esModificar=Integer.parseInt(this.getMapaClaseInventarios("es_modificar_"+codsClasesInventario.elementAt(i))+"");
					int claseInventario=Integer.parseInt(this.getMapaClaseInventarios("claseInventario_"+codsClasesInventario.elementAt(i))+"");
										
					//logger.info("esModificar-->"+esModificar);
					//logger.info("claseInventario-->"+claseInventario+"\n");
					//------------Si esModicar es igual a 1 se actualiza el registro sino se inserta-----------------------//
					if (esModificar==1)
						{
							//-------Si es vac�o se guarda -1, para que al comparar no genere el log ya que el nuevo viene -1
							/*if(!UtilidadCadena.noEsVacio(cuentaIngresoAnt))
								cuentaIngresoAnt="-1";*/
							
							//-----Se verifica si cambiaron el valor de la cuenta de ingreso para modificar o insertar la cuenta de ingreso ---------//
							//if (!cuentaIngresoNueva.equals(cuentaIngresoAnt))
						if ((!cuentaIngresoNueva.equals(cuentaIngresoAnt)) || (!cuentaVigenciaAnteriorNueva.equals(cuentaVigenciaAnteriorAnt))  )	
							{
								String nombreClaseInventario=(String)this.getMapaClaseInventarios("nom_clase_inventario_"+codsClasesInventario.elementAt(i));
								String nombreCuentaIngAnt="";
								String nombreCuentaIngNueva="";
								
								String nombreCuentaVigenciaAnteriorAnt="";
								String nombreCuentaVigenciaAnteriorNueva="";
								
								if(UtilidadCadena.noEsVacio(cuentaIngresoAnt))
									nombreCuentaIngAnt=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaIngresoAnt));
								
								if(UtilidadCadena.noEsVacio(cuentaIngresoNueva))
									nombreCuentaIngNueva=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaIngresoNueva));
								///
								if(UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorAnt))
									nombreCuentaVigenciaAnteriorAnt=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaVigenciaAnteriorAnt));
								
								if(UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorNueva))
									nombreCuentaVigenciaAnteriorNueva=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaVigenciaAnteriorNueva));
								///
								//-----------------------------------------------GENERACION DEL LOG AL MODIFICAR --------------------------------------------------//
							   String log = "\n====================MODIFICACION DE CLASE DE INVENTARIO======================="; 
							   log += "\n CODIGO CLASE INVENTARIO :  " + claseInventario; 
							   log += "\n NOMBRE CLASE INVENTARIO :  " +nombreClaseInventario; 
							   log += "\n CUENTA INGRESO ANTERIOR  :  " + nombreCuentaIngAnt; 
							   log += "\n CUENTA INGRESO NUEVA  :  " + nombreCuentaIngNueva;
							   log += "\n CUENTA VIGENCIA ANTERIOR NUEVA  :  " + nombreCuentaVigenciaAnteriorNueva; 
							   log += "\n CUENTA VIGENCIA ANTERIOR ANTERIOR  :  " + nombreCuentaVigenciaAnteriorAnt; 							   
							   log += "\n==================================================================================================";
							   //-Generar el log 
							   LogsAxioma.enviarLog(ConstantesBD.logInterfazFactInventarioClaseInvModCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
							   //------------------------------------------------FIN GENERACI�N LOG--------------------------------------------------------------------------//
								
							   respClaseInv=cuentaInventarioDao.insertarActualizarClaseInventario (con, this.centroCostoSeleccionado, claseInventario, cuentaIngresoNueva, 0, cuentaVigenciaAnteriorNueva);
							   respClaseInvNul=cuentaInventarioDao.eliminarCuentasContablesNulas (con,1);
							   
							}
						}
					else
						{
						//--Inserta si es diferente de vac�o
						if(UtilidadCadena.noEsVacio(cuentaIngresoNueva) || UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorNueva))
								{	
								respClaseInv=cuentaInventarioDao.insertarActualizarClaseInventario (con, this.centroCostoSeleccionado, claseInventario, cuentaIngresoNueva, 1, cuentaVigenciaAnteriorNueva);
								respClaseInvNul=cuentaInventarioDao.eliminarCuentasContablesNulas (con, 1);
								
								}
						}
					
					//logger.info("\n\nRESPUESTA DE INSERTAR CLASE INVENTARIO-------->"+respClaseInv+"\n");
					if (respClaseInv < 0)
					{
						error=true;
						break;
					}
				//}//if se modifico la cuenta de ingreso 
			}//for
			
		}//if codsClasesInventario != null
		
		//------Si existi� alg�n error se aborta la transacci�n sino se finaliza normalmente la transacci�n----//
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
	 * M�todo para insertar la cuenta de ingreso de cada uno de los grupos
	 * de inventario para el centro de costo seleccionado
	 * @param con
	 * @param loginUsuario 
	 * @return
	 * @throws SQLException
	 */
	 @SuppressWarnings("unused")
	public boolean insertarGruposInventario(Connection con, String loginUsuario) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int respGrupoInv=0;
		int respGrupoInvNul=0;
		boolean error=false;
		
		if (cuentaInventarioDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexi�n con la fuente de datos (CuentaInventarioDao - insertarGruposInventario )");
		}
		
		//Iniciamos la transacci�n
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if (this.getMapaGrupoInventarios("codsGruposInventario") != null)
		{
			Vector codsGruposInventario=(Vector) this.getMapaGrupoInventarios("codsGruposInventario");
			
			for (int i=0; i<codsGruposInventario.size(); i++)
			{
				String cuentaIngresoNueva=(String)this.getMapaGrupoInventarios("cuenta_ingreso_"+codsGruposInventario.elementAt(i));
				String cuentaIngresoAnt=(String)this.getMapaGrupoInventarios("cuenta_ingresoAnt_"+codsGruposInventario.elementAt(i));
				
				String cuentaVigenciaAnteriorNueva=(String)this.getMapaGrupoInventarios("cuenta_ingreso_vig_anterior_"+codsGruposInventario.elementAt(i));
				String cuentaVigenciaAnteriorAnt =(String)this.getMapaGrupoInventarios("cuentaIngresoClaseVigenciaAnteAnt_"+codsGruposInventario.elementAt(i));
				
				//logger.info("cuentaIngresoNueva-->"+cuentaIngresoNueva);
				//logger.info("cuentaIngresoAnt-->"+cuentaIngresoAnt+"\n");
				
					int esModificar=Integer.parseInt(this.getMapaGrupoInventarios("es_modificar_"+codsGruposInventario.elementAt(i))+"");
					int grupoInventario=Integer.parseInt(this.getMapaGrupoInventarios("grupoInventario_"+codsGruposInventario.elementAt(i))+"");
					
					//logger.info("esModificar-->"+esModificar);
					//logger.info("grupoInventario-->"+grupoInventario);
					//logger.info("claseInventario-->"+this.codigoClaseInventario+"\n");
					//------------Si esModicar es igual a 1 se actualiza el registro sino se inserta-----------------------//
					if (esModificar==1)
						{
						//-------Si es vac�o se guarda -1, para que al comparar no genere el log ya que el nuevo viene -1
						/*if(!UtilidadCadena.noEsVacio(cuentaIngresoAnt))
							cuentaIngresoAnt="-1";*/
						
						//-----Se verifica si cambiaron el valor de la cuenta de ingreso para modificar o insertar la cuenta de ingreso ---------//
						if ((!cuentaIngresoNueva.equals(cuentaIngresoAnt)) || (!cuentaVigenciaAnteriorNueva.equals(cuentaVigenciaAnteriorAnt))  )
							{
							String nombreGrupoInventario=(String)this.getMapaGrupoInventarios("nom_grupo_inventario_"+codsGruposInventario.elementAt(i));
							String nombreCuentaIngAnt="";
							String nombreCuentaIngNueva="";
							
							String nombreCuentaVigenciaAnteriorAnt="";
							String nombreCuentaVigenciaAnteriorNueva="";
							
							if(UtilidadCadena.noEsVacio(cuentaIngresoAnt))
								nombreCuentaIngAnt=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaIngresoAnt));
							
							if(UtilidadCadena.noEsVacio(cuentaIngresoNueva))
								nombreCuentaIngNueva=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaIngresoNueva));
							
							if(UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorAnt))
								nombreCuentaVigenciaAnteriorAnt=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaVigenciaAnteriorAnt));
							
							if(UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorNueva))
								nombreCuentaVigenciaAnteriorNueva=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaVigenciaAnteriorNueva));
							
							//-----------------------------------------------GENERACION DEL LOG AL MODIFICAR --------------------------------------------------//
						   String log = "\n======================MODIFICACION DE GRUPO DE INVENTARIO====================="; 
						   log += "\n CODIGO GRUPO INVENTARIO :  " + grupoInventario; 
						   log += "\n NOMBRE GRUPO INVENTARIO :  " +nombreGrupoInventario; 
						   log += "\n CUENTA INGRESO ANTERIOR  :  " + nombreCuentaIngAnt; 
						   log += "\n CUENTA INGRESO NUEVA  :  " + nombreCuentaIngNueva; 
						   log += "\n CUENTA VIGENCIA ANTERIOR NUEVA  :  " + nombreCuentaVigenciaAnteriorNueva; 
						   log += "\n CUENTA VIGENCIA ANTERIOR ANTERIOR  :  " + nombreCuentaVigenciaAnteriorAnt; 									   
						   log += "\n==================================================================================================";
						   //-Generar el log 
						   LogsAxioma.enviarLog(ConstantesBD.logInterfazFactInventarioGrupoInvModCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
						   //------------------------------------------------FIN GENERACI�N LOG--------------------------------------------------------------------------//
						   
							respGrupoInv=cuentaInventarioDao.insertarActualizarGrupoInventario (con, this.centroCostoSeleccionado, grupoInventario, this.codigoClaseInventario, cuentaIngresoNueva, 0, cuentaVigenciaAnteriorNueva);
							respGrupoInvNul=cuentaInventarioDao.eliminarCuentasContablesNulas (con,2);
							}
						}
					else
						{
						//--Inserta si es diferente de vac�o
						if(UtilidadCadena.noEsVacio(cuentaIngresoNueva) || UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorNueva))
							{
							respGrupoInv=cuentaInventarioDao.insertarActualizarGrupoInventario (con, this.centroCostoSeleccionado, grupoInventario, this.codigoClaseInventario, cuentaIngresoNueva, 1, cuentaVigenciaAnteriorNueva);
							respGrupoInvNul=cuentaInventarioDao.eliminarCuentasContablesNulas (con,2);
							}
						}
					
					//logger.info("\n\nRESPUESTA DE INSERTAR GRUPO INVENTARIO-------->"+respGrupoInv+"\n");
					if (respGrupoInv < 0)
					{
						error=true;
						break;
					}
				
				//}//if se modifico la cuenta de ingreso 
			}//for
			
		}//if codsGruposInventario != null
		
		//------Si existi� alg�n error se aborta la transacci�n sino se finaliza normalmente la transacci�n----//
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
	 * M�todo para insertar la cuenta de ingreso de cada uno de los subgrupos
	 * de inventario para el centro de costo, clase y grupo de inventario seleccionado
	 * @param con
	 * @param loginUsuario 
	 * @return
	 * @throws SQLException
	 */
	 @SuppressWarnings("unused")
	public boolean insertarSubGruposInventario(Connection con, String loginUsuario) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int respSubGrupoInv=0;
		int respSubGrupoInvNul=0;
		boolean error=false;
		
		if (cuentaInventarioDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexi�n con la fuente de datos (CuentaInventarioDao - insertarSubGruposInventario )");
		}
		
		//Iniciamos la transacci�n
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if (this.getMapaSubGrupoInventarios("codsSubGruposInventario") != null)
		{
			Vector codsSubGruposInventario=(Vector) this.getMapaSubGrupoInventarios("codsSubGruposInventario");
			
			for (int i=0; i<codsSubGruposInventario.size(); i++)
			{
				String cuentaIngresoNueva=(String)this.getMapaSubGrupoInventarios("cuenta_ingreso_"+codsSubGruposInventario.elementAt(i));
				String cuentaIngresoAnt=(String)this.getMapaSubGrupoInventarios("cuenta_ingresoAnt_"+codsSubGruposInventario.elementAt(i));
				
				String cuentaVigenciaAnteriorNueva=(String)this.getMapaSubGrupoInventarios("cuenta_ingreso_vig_anterior_"+codsSubGruposInventario.elementAt(i));
				String cuentaVigenciaAnteriorAnt =(String)this.getMapaSubGrupoInventarios("cuentaIngresoClaseVigenciaAnteAnt_"+codsSubGruposInventario.elementAt(i));
				
				//logger.info("cuentaIngresoNueva-->"+cuentaIngresoNueva);
				//logger.info("cuentaIngresoAnt-->"+cuentaIngresoAnt+"\n");
				
					int esModificar=Integer.parseInt(this.getMapaSubGrupoInventarios("es_modificar_"+codsSubGruposInventario.elementAt(i))+"");
					int subGrupoInventario=Integer.parseInt(this.getMapaSubGrupoInventarios("subGrupoInventario_"+codsSubGruposInventario.elementAt(i))+"");
					
					//logger.info("esModificar-->"+esModificar);
					//logger.info("subGrupoInventario-->"+subGrupoInventario+"\n");
										
					//------------Si esModicar es igual a 1 se actualiza el registro sino se inserta-----------------------//
					if (esModificar==1)
						{
						//-------Si es vac�o se guarda -1, para que al comparar no genere el log ya que el nuevo viene -1
						/*if(!UtilidadCadena.noEsVacio(cuentaIngresoAnt))
							cuentaIngresoAnt="-1";*/
						
						//-----Se verifica si cambiaron el valor de la cuenta de ingreso para modificar o insertar la cuenta de ingreso ---------//
						if ((!cuentaIngresoNueva.equals(cuentaIngresoAnt)) || (!cuentaVigenciaAnteriorNueva.equals(cuentaVigenciaAnteriorAnt))  )
							{
							String nombreSubGrupoInventario=(String)this.getMapaSubGrupoInventarios("nom_subgrupo_inventario_"+codsSubGruposInventario.elementAt(i));
							String nombreCuentaIngAnt="";
							String nombreCuentaIngNueva="";
							
							String nombreCuentaVigenciaAnteriorAnt="";
							String nombreCuentaVigenciaAnteriorNueva="";
							
							if(UtilidadCadena.noEsVacio(cuentaIngresoAnt))
								nombreCuentaIngAnt=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaIngresoAnt));
							
							if(UtilidadCadena.noEsVacio(cuentaIngresoNueva))
								nombreCuentaIngNueva=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaIngresoNueva));
							
							if(UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorAnt))
								nombreCuentaVigenciaAnteriorAnt=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaVigenciaAnteriorAnt));
							
							if(UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorNueva))
								nombreCuentaVigenciaAnteriorNueva=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaVigenciaAnteriorNueva));
							
							//-----------------------------------------------GENERACION DEL LOG AL MODIFICAR --------------------------------------------------//
						   String log = "\n=====================MODIFICACION DE SUB-GRUPO DE INVENTARIO========================"; 
						   log += "\n CODIGO SUB-GRUPO INVENTARIO :  " + subGrupoInventario; 
						   log += "\n NOMBRE SUB-GRUPO INVENTARIO :  " +nombreSubGrupoInventario; 
						   log += "\n CUENTA INGRESO ANTERIOR  :  " + nombreCuentaIngAnt; 
						   log += "\n CUENTA INGRESO NUEVA  :  " + nombreCuentaIngNueva; 
						   log += "\n CUENTA VIGENCIA ANTERIOR NUEVA  :  " + nombreCuentaVigenciaAnteriorNueva; 
						   log += "\n CUENTA VIGENCIA ANTERIOR ANTERIOR  :  " + nombreCuentaVigenciaAnteriorAnt; 							   
						   log += "\n==================================================================================================";
						   //-Generar el log 
						   LogsAxioma.enviarLog(ConstantesBD.logInterfazFactInventarioSubGrupoInvModCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
						   //------------------------------------------------FIN GENERACI�N LOG--------------------------------------------------------------------------//
							
							respSubGrupoInv=cuentaInventarioDao.insertarActualizarSubGrupoInventario (con, this.centroCostoSeleccionado, subGrupoInventario, cuentaIngresoNueva, 0, cuentaVigenciaAnteriorNueva);
							respSubGrupoInvNul=cuentaInventarioDao.eliminarCuentasContablesNulas (con,3);
							}
						}
					else
						{
						//--Inserta si es diferente de vac�o
						if(UtilidadCadena.noEsVacio(cuentaIngresoNueva) || UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorNueva))
							{
							respSubGrupoInv=cuentaInventarioDao.insertarActualizarSubGrupoInventario (con, this.centroCostoSeleccionado, subGrupoInventario, cuentaIngresoNueva, 1, cuentaVigenciaAnteriorNueva);
							respSubGrupoInvNul=cuentaInventarioDao.eliminarCuentasContablesNulas (con,3);
							}
						}
					
					//logger.info("\n\nRESPUESTA DE INSERTAR SUBGRUPO INVENTARIO-------->"+respSubGrupoInv+"\n");
					if (respSubGrupoInv < 0)
					{
						error=true;
						break;
					}
				
				//}//if se modifico la cuenta de ingreso 
			}//for
			
		}//if codsSubGruposInventario != null
		
		//------Si existi� alg�n error se aborta la transacci�n sino se finaliza normalmente la transacci�n----//
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
	 * M�todo para insertar la cuenta de ingreso de cada uno de los articulos
	 * de inventario para el centro de costo, clase, subgrupo y  grupo de inventario seleccionado
	 * @param con
	 * @param loginUsuario 
	 * @return
	 * @throws SQLException
	 */
	 @SuppressWarnings("unused")
	public boolean insertarArticulosInventario(Connection con, String loginUsuario) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int respArticuloInv=0;
		int respArticuloInvNul=0;
		boolean error=false;
		
		if (cuentaInventarioDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexi�n con la fuente de datos (CuentaInventarioDao - insertarArticulosInventario )");
		}
		
		//Iniciamos la transacci�n
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if (this.getMapaArticuloInventarios("codsArticulosInventario") != null)
		{
			Vector codsArticulosInventario=(Vector) this.getMapaArticuloInventarios("codsArticulosInventario");
			
			for (int i=0; i<codsArticulosInventario.size(); i++)
			{
				String cuentaIngresoNueva=(String)this.getMapaArticuloInventarios("cuenta_ingreso_"+codsArticulosInventario.elementAt(i));
				String cuentaIngresoAnt=(String)this.getMapaArticuloInventarios("cuenta_ingresoAnt_"+codsArticulosInventario.elementAt(i));
				
				String cuentaVigenciaAnteriorNueva=(String)this.getMapaArticuloInventarios("cuenta_ingreso_vig_anterior_"+codsArticulosInventario.elementAt(i));
				String cuentaVigenciaAnteriorAnt =(String)this.getMapaArticuloInventarios("cuentaIngresoClaseVigenciaAnteAnt_"+codsArticulosInventario.elementAt(i));

				/*logger.info("cuentaIngresoNueva-->"+cuentaIngresoNueva);
				logger.info("cuentaIngresoAnt-->"+cuentaIngresoAnt+"\n");*/
				
					int esModificar=Integer.parseInt(this.getMapaArticuloInventarios("es_modificar_"+codsArticulosInventario.elementAt(i))+"");
					int articuloInventario=Integer.parseInt(this.getMapaArticuloInventarios("articuloInventario_"+codsArticulosInventario.elementAt(i))+"");
					
					//logger.info("esModificar-->"+esModificar);
					//logger.info("articuloInventario-->"+articuloInventario+"\n");
										
					//------------Si esModicar es igual a 1 se actualiza el registro sino se inserta-----------------------//
					if (esModificar==1)
						{
						//-------Si es vac�o se guarda -1, para que al comparar no genere el log ya que el nuevo viene -1
						/*if(!UtilidadCadena.noEsVacio(cuentaIngresoAnt))
							cuentaIngresoAnt="-1";*/
						
						//-----Se verifica si cambiaron el valor de la cuenta de ingreso para modificar o insertar la cuenta de ingreso ---------//
						if ((!cuentaIngresoNueva.equals(cuentaIngresoAnt)) || (!cuentaVigenciaAnteriorNueva.equals(cuentaVigenciaAnteriorAnt))  )
							{
							String nombreArticuloInventario=(String)this.getMapaArticuloInventarios("nom_articulo_inventario_"+codsArticulosInventario.elementAt(i));
							String nombreCuentaIngAnt="";
							String nombreCuentaIngNueva="";
							
							String nombreCuentaVigenciaAnteriorAnt="";
							String nombreCuentaVigenciaAnteriorNueva="";

							
							if(UtilidadCadena.noEsVacio(cuentaIngresoAnt))
								nombreCuentaIngAnt=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaIngresoAnt));
							
							if(UtilidadCadena.noEsVacio(cuentaIngresoNueva))
								nombreCuentaIngNueva=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaIngresoNueva));

							if(UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorAnt))
								nombreCuentaVigenciaAnteriorAnt=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaVigenciaAnteriorAnt));
							
							if(UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorNueva))
								nombreCuentaVigenciaAnteriorNueva=Utilidades.obtenerNombreCuentaContable(con, Integer.parseInt(cuentaVigenciaAnteriorNueva));

							//-----------------------------------------------GENERACION DEL LOG AL MODIFICAR --------------------------------------------------//
						   String log = "\n===================MODIFICACION DE ARTICULO DE INVENTARIO======================"; 
						   log += "\n CODIGO ARTICULO INVENTARIO :  " + articuloInventario; 
						   log += "\n NOMBRE ARTICULO INVENTARIO :  " +nombreArticuloInventario; 
						   log += "\n CUENTA INGRESO ANTERIOR  :  " + nombreCuentaIngAnt; 
						   log += "\n CUENTA INGRESO NUEVA  :  " + nombreCuentaIngNueva; 
						   log += "\n CUENTA VIGENCIA ANTERIOR NUEVA  :  " + nombreCuentaVigenciaAnteriorNueva; 
						   log += "\n CUENTA VIGENCIA ANTERIOR ANTERIOR  :  " + nombreCuentaVigenciaAnteriorAnt; 							   
						   log += "\n==================================================================================================";
						   //-Generar el log 
						   LogsAxioma.enviarLog(ConstantesBD.logInterfazFactInventarioArticuloInvModCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
						   //------------------------------------------------FIN GENERACI�N LOG--------------------------------------------------------------------------//
							
							respArticuloInv=cuentaInventarioDao.insertarActualizarArticuloInventario (con, this.centroCostoSeleccionado, articuloInventario, cuentaIngresoNueva, 0, cuentaVigenciaAnteriorNueva);
							respArticuloInvNul=cuentaInventarioDao.eliminarCuentasContablesNulas (con,4);
							}
						}
					else
						{
						//--Inserta si es diferente de vac�o
						if(UtilidadCadena.noEsVacio(cuentaIngresoNueva) || UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorNueva))
							{	
							respArticuloInv=cuentaInventarioDao.insertarActualizarArticuloInventario (con, this.centroCostoSeleccionado, articuloInventario, cuentaIngresoNueva, 1, cuentaVigenciaAnteriorNueva);
							respArticuloInvNul=cuentaInventarioDao.eliminarCuentasContablesNulas (con,4);
							}
						}
					
					//logger.info("\n\nRESPUESTA DE INSERTAR ARTICULO INVENTARIO-------->"+respArticuloInv+"\n");
					if (respArticuloInv < 0)
					{
						error=true;
						break;
					}
				
				//}//if se modifico la cuenta de ingreso 
			}//for
			
		}//if codsArticulosInventario != null
		
		//------Si existi� alg�n error se aborta la transacci�n sino se finaliza normalmente la transacci�n----//
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
	 * M�todo que elimina de acuerdo al valor enviado en el par�metro tabla eliminar, 
	 * la cuenta contable respectiva con su respectivo log
	 * tablaEliminar 1 -> ClaseInventario
	 * 				 2 -> GrupoInventario
	 * 				 3 -> SubGrupoInventario
	 * 				 4 -> ArticuloInventario
	 * @param con
	 * @param tablaEliminar
	 * @param loginUsuario
	 */
	public int eliminarCuentaContable(Connection con, int tablaEliminar, String loginUsuario) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int  resp1=0; 
			
		if (cuentaInventarioDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexi�n con la fuente de datos (cuentaInventarioDao - eliminarCuentaContable )");
		}
		
		//----Iniciamos la transacci�n
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);
		
		//--------Se verifica que la posici�n sea diferente de -1 
		if (this.posicionEliminar!=ConstantesBD.codigoNuncaValido)
		{
			//int claseInventarioElim=0, grupoInventarioElim=0, subGrupoInventarioElim=0, articuloInventarioElim=0;
			
			resp1=cuentaInventarioDao.eliminarCuentaContable(con, tablaEliminar, this.centroCostoSeleccionado, this.claseInventarioEliminar, this.grupoInventarioEliminar, this.subGrupoInventarioEliminar, this.articuloInventarioEliminar);
			
			//-- Generar el LOG si fu� exitosa la eliminada
			if (resp1 > 0)
			{
				String log = "";
				if ( tablaEliminar == 1 )
				{
					log = "\n===========ELIMINACI�N DE CUENTA CONTABLE CLASE DE INVENTARIO=============";
					log += "\n C�DIGO CLASE INVENTARIO :  " +this.getMapaClaseInventarios("claseInventario_"+this.posicionEliminar);
					log += "\n NOMBRE CLASE INVENTARIO :  " +this.getMapaClaseInventarios("nom_clase_inventario_"+this.posicionEliminar);
					log += "\n CUENTA CONTABLE:  " + nombreCuentaEliminar;
					//log += "\n CUENTA VIGENCIA ANTERIOR :  " + nombreCuentaVigenciaAnteriorEliminar;
					log += "\n==========================================================================";
					//-- Generar el log. 
					LogsAxioma.enviarLog(ConstantesBD.logInterfazFactInventarioClaseInvModCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, loginUsuario);
				}
				
				if ( tablaEliminar == 2)
				{
					log = "\n===========ELIMINACI�N DE CUENTA CONTABLE GRUPO INVENTARIO=============";
					log += "\n C�DIGO GRUPO INVENTARIO :  " +this.getMapaGrupoInventarios("grupoInventario_"+this.posicionEliminar);
					log += "\n NOMBRE GRUPO INVENTARIO :  " +this.getMapaGrupoInventarios("nom_grupo_inventario_"+this.posicionEliminar);
					log += "\n CUENTA CONTABLE:  " + nombreCuentaEliminar; 
					//log += "\n CUENTA VIGENCIA ANTERIOR :  " + nombreCuentaVigenciaAnteriorEliminar;
					log += "\n==========================================================================";
					//-- Generar el log. 
					LogsAxioma.enviarLog(ConstantesBD.logInterfazFactInventarioGrupoInvModCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, loginUsuario);
				}
				
				if ( tablaEliminar == 3)
				{
					log = "\n===========ELIMINACI�N DE CUENTA CONTABLE SUB-GRUPO INVENTARIO=============";
					log += "\n C�DIGO SUB-GRUPO INVENTARIO :  " +this.getMapaSubGrupoInventarios("subGrupoInventario_"+this.posicionEliminar);
					log += "\n NOMBRE SUB-GRUPO INVENTARIO :  " +this.getMapaSubGrupoInventarios("nom_subgrupo_inventario_"+this.posicionEliminar);
					log += "\n CUENTA CONTABLE:  " + nombreCuentaEliminar; 
					//log += "\n CUENTA VIGENCIA ANTERIOR :  " + nombreCuentaVigenciaAnteriorEliminar;
					log += "\n==========================================================================";
					//-- Generar el log. 
					LogsAxioma.enviarLog(ConstantesBD.logInterfazFactInventarioSubGrupoInvModCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, loginUsuario);
				}
				
				if ( tablaEliminar == 4)
				{
					log = "\n===========ELIMINACI�N DE CUENTA CONTABLE ARTICULO INVENTARIO=============";
					log += "\n C�DIGO ARTICULO INVENTARIO :  " +this.getMapaArticuloInventarios("articuloInventario_"+this.posicionEliminar);
					log += "\n NOMBRE ARTICULO INVENTARIO :  " +this.getMapaArticuloInventarios("nom_articulo_inventario_"+this.posicionEliminar);
					log += "\n CUENTA CONTABLE:  " + nombreCuentaEliminar; 
					//log += "\n CUENTA VIGENCIA ANTERIOR :  " + nombreCuentaVigenciaAnteriorEliminar;
					log += "\n==========================================================================";
					//-- Generar el log. 
					LogsAxioma.enviarLog(ConstantesBD.logInterfazFactInventarioArticuloInvModCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, loginUsuario);
				}
				
			}//if resp1 > 0
			
		}//if posicion!=-1
		
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


	
	
	
	//----------------------------------------------------------------------- SETS Y GETS --------------------------------------------------------------//
	/**
	 * @return Retorna the centroCostoSeleccionado.
	 */
	public int getCentroCostoSeleccionado()
	{
		return centroCostoSeleccionado;
	}

	/**
	 * @param centroCostoSeleccionado The centroCostoSeleccionado to set.
	 */
	public void setCentroCostoSeleccionado(int centroCostoSeleccionado)
	{
		this.centroCostoSeleccionado = centroCostoSeleccionado;
	}

	/**
	 * @return Retorna the codigoClaseInventario.
	 */
	public int getCodigoClaseInventario()
	{
		return codigoClaseInventario;
	}

	/**
	 * @param codigoClaseInventario The codigoClaseInventario to set.
	 */
	public void setCodigoClaseInventario(int codigoClaseInventario)
	{
		this.codigoClaseInventario = codigoClaseInventario;
	}

	/**
	 * @return Retorna the mapaArticuloInventarios.
	 */
	public HashMap getMapaArticuloInventarios()
	{
		return mapaArticuloInventarios;
	}

	/**
	 * @param mapaArticuloInventarios The mapaArticuloInventarios to set.
	 */
	public void setMapaArticuloInventarios(HashMap mapaArticuloInventarios)
	{
		this.mapaArticuloInventarios = mapaArticuloInventarios;
	}
	
	/**
	 * @return Retorna mapaArticuloInventarios.
	 */
	public Object getMapaArticuloInventarios(Object key) {
		return mapaArticuloInventarios.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaArticuloInventarios(Object key, Object dato) {
		this.mapaArticuloInventarios.put(key, dato);
	}

	/**
	 * @return Retorna the mapaClaseInventarios.
	 */
	public HashMap getMapaClaseInventarios()
	{
		return mapaClaseInventarios;
	}

	/**
	 * @param mapaClaseInventarios The mapaClaseInventarios to set.
	 */
	public void setMapaClaseInventarios(HashMap mapaClaseInventarios)
	{
		this.mapaClaseInventarios = mapaClaseInventarios;
	}
	
	/**
	 * @return Retorna mapaClaseInventarios.
	 */
	public Object getMapaClaseInventarios(Object key) {
		return mapaClaseInventarios.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaClaseInventarios(Object key, Object dato) {
		this.mapaClaseInventarios.put(key, dato);
	}

	/**
	 * @return Retorna the mapaGrupoInventarios.
	 */
	public HashMap getMapaGrupoInventarios()
	{
		return mapaGrupoInventarios;
	}

	/**
	 * @param mapaGrupoInventarios The mapaGrupoInventarios to set.
	 */
	public void setMapaGrupoInventarios(HashMap mapaGrupoInventarios)
	{
		this.mapaGrupoInventarios = mapaGrupoInventarios;
	}
	
	/**
	 * @return Retorna mapaGrupoInventarios.
	 */
	public Object getMapaGrupoInventarios(Object key) {
		return mapaGrupoInventarios.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaGrupoInventarios(Object key, Object dato) {
		this.mapaGrupoInventarios.put(key, dato);
	}

	/**
	 * @return Retorna the mapaSubGrupoInventarios.
	 */
	public HashMap getMapaSubGrupoInventarios()
	{
		return mapaSubGrupoInventarios;
	}

	/**
	 * @param mapaSubGrupoInventarios The mapaSubGrupoInventarios to set.
	 */
	public void setMapaSubGrupoInventarios(HashMap mapaSubGrupoInventarios)
	{
		this.mapaSubGrupoInventarios = mapaSubGrupoInventarios;
	}
	
	/**
	 * @return Retorna mapaSubGrupoInventarios.
	 */
	public Object getMapaSubGrupoInventarios(Object key) {
		return mapaSubGrupoInventarios.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaSubGrupoInventarios(Object key, Object dato) {
		this.mapaSubGrupoInventarios.put(key, dato);
	}

	public int getArticuloInventarioEliminar() {
		return articuloInventarioEliminar;
	}

	public void setArticuloInventarioEliminar(int articuloInventarioEliminar) {
		this.articuloInventarioEliminar = articuloInventarioEliminar;
	}

	public int getClaseInventarioEliminar() {
		return claseInventarioEliminar;
	}

	public void setClaseInventarioEliminar(int claseInventarioEliminar) {
		this.claseInventarioEliminar = claseInventarioEliminar;
	}

	public int getGrupoInventarioEliminar() {
		return grupoInventarioEliminar;
	}

	public void setGrupoInventarioEliminar(int grupoInventarioEliminar) {
		this.grupoInventarioEliminar = grupoInventarioEliminar;
	}

	public String getNombreCuentaEliminar() {
		return nombreCuentaEliminar;
	}

	public void setNombreCuentaEliminar(String nombreCuentaEliminar) {
		this.nombreCuentaEliminar = nombreCuentaEliminar;
	}

	public int getPosicionEliminar() {
		return posicionEliminar;
	}

	public void setPosicionEliminar(int posicionEliminar) {
		this.posicionEliminar = posicionEliminar;
	}

	public int getSubGrupoInventarioEliminar() {
		return subGrupoInventarioEliminar;
	}

	public void setSubGrupoInventarioEliminar(int subGrupoInventarioEliminar) {
		this.subGrupoInventarioEliminar = subGrupoInventarioEliminar;
	}

	public String getNombreCuentaVigenciaAnteriorEliminar() {
		return nombreCuentaVigenciaAnteriorEliminar;
	}

	public void setNombreCuentaVigenciaAnteriorEliminar(
			String nombreCuentaVigenciaAnteriorEliminar) {
		this.nombreCuentaVigenciaAnteriorEliminar = nombreCuentaVigenciaAnteriorEliminar;
	}
	
}
