/*
 * Creado el May 12, 2006
 * por Julian Montoya
 */
package com.princetonsa.mundo.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.CuentaUnidadFuncionalDao;

public class CuentaUnidadFuncional 
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CuentaUnidadFuncional.class);
	
	/**
	 * para almacenar el centro de costo.
	 */
	private String centroCosto;

	/**
	 * Mapa para el manejo de las consultas
	 */
	private HashMap mapa = new HashMap();
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private CuentaUnidadFuncionalDao  cuentaUnidadFuncionalDao = null;


	/**
	 * Variable para almacenar el nombre del grupo seleccionado para mostrarlo al consultar los Tipos de ese Grupo Especifico
	 */
	private String nombreUnidadFuncional;
	
	/**
	 * Constructor de la clase, inicializa en vacío todos los atributos
	 */
	public CuentaUnidadFuncional ()
	{
		reset();
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
			cuentaUnidadFuncionalDao = myFactory.getCuentaUnidadFuncionalDao();
			wasInited = (cuentaUnidadFuncionalDao != null);
		}
		return wasInited;
	}
	
	
	/**
	 * Metodo para limpiar el objeto
	 *
	 */
	private void reset()
	{
		//this.mapa = new HashMap();
	}

	/**
	 * Metodo para cargar los grupos.
	 * @param institucion 
	 * @return
	 * @throws SQLException 
	 */

	public HashMap cargarUnidadesFuncionales(Connection con, int tipoGrupo, int institucion) throws SQLException 
	{
		if (cuentaUnidadFuncionalDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (cuentaUnidadFuncional - cargarUnidadesFuncionales )");
		}
		
		String unidadFuncional = "";
		if ( UtilidadCadena.noEsVacio(this.mapa.get("codigoUnidadFuncional")+"") )
		{
			unidadFuncional = this.mapa.get("codigoUnidadFuncional")+"";
		}
		
		return cuentaUnidadFuncionalDao.cargarUnidadesFuncionales(con,  tipoGrupo, institucion, unidadFuncional);	 
	}
 

	/**
	 * Metodo para insertar los grupos.
	 * @param institucion 
	 * @param loginUsuario
	 * @param tipoInsercion : define el tipo de insercion. 
	 * @return
	 * @throws SQLException 
	 */

	public int insertarUnidadesFuncionales(Connection con, int tipoInsercion, int institucion, String loginUsuario) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int  resp1=0;
			
		if (cuentaUnidadFuncionalDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos ( cuentaUnidadFuncional - insertarUnidadesFuncionales )");
		}
		
		//-------------------------------------------------------------------------------------------------------------------
		//----Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);
		
			int nroUnidades = 0;
			if ( UtilidadCadena.noEsVacio(this.getMapa("numRegistros")+"") )
			{
				nroUnidades = Integer.parseInt(this.getMapa("numRegistros")+"");  
			}
		    
			if (nroUnidades > 0)
			{
					if ( tipoInsercion == 0 )
					{
							for (int i = 0; i < nroUnidades; i++)
							{
								String codUnidadFuncional = this.getMapa("codigo_unidad_funcional_" + i) + "";		 	 //-Codigo de la Unidad Funcional
								String modificado = this.getMapa("modificado_" + i) + "";								 //-False si no se ha registrado información 
								String cuentaIngreso = (this.getMapa("cuentaingreso_unifun_" + i) + "").trim();		  	 //-Informacion capturada del jsp
								String h_cuentaIngreso = (this.getMapa("h_cuentaingreso_unifun_" + i) + "");		  	 //-Informacion para verificar si hubo cambio
								String cuentaMedicamento = (this.getMapa("cuentamedicamento_unifun_" + i) + "").trim();	 //-Informacion capturada del jsp
								String h_cuentaMedicamento = (this.getMapa("h_cuentamedicamento_unifun_" + i) + "");	 //-Informacion para verificar si hubo cambio
								String consecutivo = (this.getMapa("consecutivo_" + i) + "");							 //-Llave para modificar el registro. 
								//
								String cuentaIngresoVigAnterior = (this.getMapa("cuenta_ingreso_vig_anterior_" + i) + "").trim();		  	 //-Informacion capturada del jsp
								String h_cuentaIngresoVigAnterior = (this.getMapa("h_cta_ingreso_vig_anterior_" + i) + "");		  	 //-Informacion para verificar si hubo cambio
								
								String cuentaIngresoVigAnteriorMed = (this.getMapa("cuenta_med_vig_anterior_" + i) + "").trim();	 //-Informacion capturada del jsp
								String h_cuentaIngresoVigAnteriorMed = (this.getMapa("h_cuenta_med_vig_anterior_" + i) + "");	 //-Informacion para verificar si hubo cambio
								
								String rubroPresupuestal = (this.getMapa("rubro_" + i)+ "");		//-Informacion capturada del jsp
								String h_rubroPresupuestal = (this.getMapa("h_rubro_" + i) + "");	//-Informacion para verificar si hubo cambio
								
								String cuentaCosto = (this.getMapa("cuentacosto_" + i)+ "");		//-Informacion capturada del jsp
								String cuentaCostoH = (this.getMapa("cuentacostoho_" + i) + "");	//-Informacion para verificar si hubo cambio
								
								//
								if (UtilidadTexto.getBoolean(modificado) ) //---Se debe generar el LOG porque se modifico la cuenta ingreso
								{
									if ( !h_cuentaIngreso.equals(cuentaIngreso) || !h_cuentaMedicamento.equals(cuentaMedicamento) )  
									{
										String log = "\n================================MODIFICACION DE CUENTAS INTERFAZ UNIDAD FUNCIONAL================================";
										
											   if ( !codUnidadFuncional.equals("null") )
											   {	
												   log += "\n CODIGO UNIDAD FUNCIONAL 		:  " + codUnidadFuncional; 
												   log += "\n NOMBRE UNIDAD FUNCIONAL  		:  " + this.getMapa("nombre_unidad_funcional_" + i);
											   }
											   else
											   {
												   log += "\n TODAS LAS UNIDADES FUNCIONALES "; 
											   }
											   
											   if ( UtilidadCadena.noEsVacio(this.getMapa("h_cuentaingreso_unifun_" + i)+"") && (this.getMapa("h_cuentaingreso_unifun_" + i)+"").equals("0")) 
											   {
												   log += "\n CUENTA INGRESO ANTERIOR 		:  NO DEFINIDA ";
											   }
											   else
											   {   
												   log += "\n CUENTA INGRESO ANTERIOR 		:  " + this.getMapa("h_cuentaingreso_unifun_" + i);
											   }										   
											   if ( UtilidadCadena.noEsVacio(cuentaIngreso) && cuentaIngreso.equals("0")) 
											   {
												   log += "\n CUENTA INGRESO NUEVA	 		:  NO DEFINIDA ";
											   }
											   else
											   {
												   log += "\n CUENTA INGRESO NUEVA	 		:  " + cuentaIngreso;
											   }

										
											   if ( UtilidadCadena.noEsVacio(this.getMapa("h_cuentamedicamento_unifun_" + i)+"") && (this.getMapa("h_cuentamedicamento_unifun_" + i)+"").equals("0")) 
											   {
												   log += "\n CUENTA MEDICAMENTO ANTERIOR 	:  NO DEFINIDA "; 
											   }
											   else
											   {
												   log += "\n CUENTA MEDICAMENTO ANTERIOR 	:  " + this.getMapa("h_cuentamedicamento_unifun_" + i); 
											   }
											   if ( UtilidadCadena.noEsVacio(cuentaMedicamento) && cuentaMedicamento.equals("0")) 
											   {
												   log += "\n CUENTA MEDICAMENTO NUEVA	 	:  NO DEFINIDA ";
											   }
											   else
											   {
												   log += "\n CUENTA MEDICAMENTO NUEVA	 	:  " + cuentaMedicamento;
											   }
											   
											   if ( UtilidadCadena.noEsVacio(rubroPresupuestal) && rubroPresupuestal.equals("0")) 
											   {
												   log += "\n CUENTA RUBRO PRESUPUESTAL	 	:  NO DEFINIDA ";
											   }
											   else
											   {
												   log += "\n CUENTA RUBRO PRESUPUESTAL	 	:  " + rubroPresupuestal;
											   }
											   
											   if ( UtilidadCadena.noEsVacio(cuentaCostoH) && cuentaCostoH.equals("0")) 
											   {
												   log += "\n CUENTA COSTO ANTERIOR	 	:  NO DEFINIDA ";
											   }
											   else
											   {
												   log += "\n CUENTA COSTO ANTERIOR	 	:  " + cuentaCosto;
											   }
											   
											   if ( UtilidadCadena.noEsVacio(cuentaCosto) && cuentaCosto.equals("0")) 
											   {
												   log += "\n CUENTA COSTO NUEVA	 	:  NO DEFINIDA ";
											   }
											   else
											   {
												   log += "\n CUENTA COSTO NUEVA	 	:  " + cuentaCosto;
											   }
											   
											   log += "\n================================================================================================================";
											   
										//-Generar el log 
										LogsAxioma.enviarLog(ConstantesBD.logInterfazCuentaInterfazUnidadFuncionalCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
									}	
								}
		
								//logger.info("\n\n codUnidadFuncional " + codUnidadFuncional + " cuentaIngreso  " + "  " + cuentaIngreso + " cuentaMedicamento " + cuentaMedicamento +  "  modificado  " + modificado + "\n");
								
								if ( 
									( ((UtilidadCadena.noEsVacio(cuentaIngreso) && Integer.parseInt(cuentaIngreso)>0)) || (UtilidadCadena.noEsVacio(h_cuentaIngreso) && Integer.parseInt(h_cuentaIngreso)>0)) && !h_cuentaIngreso.equals(cuentaIngreso) ||
									( ((UtilidadCadena.noEsVacio(cuentaMedicamento) && Integer.parseInt(cuentaMedicamento)>0)) || (UtilidadCadena.noEsVacio(h_cuentaMedicamento) && Integer.parseInt(h_cuentaMedicamento)>0)) && !h_cuentaMedicamento.equals(cuentaMedicamento) ||
									( ((UtilidadCadena.noEsVacio(cuentaIngresoVigAnterior) && Integer.parseInt(cuentaIngresoVigAnterior)>0)) || (UtilidadCadena.noEsVacio(h_cuentaIngresoVigAnterior) && Integer.parseInt(h_cuentaIngresoVigAnterior)>0)) && !h_cuentaIngresoVigAnterior.equals(cuentaIngresoVigAnterior) ||									
									( ((UtilidadCadena.noEsVacio(cuentaIngresoVigAnteriorMed) && Integer.parseInt(cuentaIngresoVigAnteriorMed)>0)) || (UtilidadCadena.noEsVacio(h_cuentaIngresoVigAnteriorMed) && Integer.parseInt(h_cuentaIngresoVigAnteriorMed)>0)) && !h_cuentaIngresoVigAnteriorMed.equals(cuentaIngresoVigAnteriorMed) ||
									( ((UtilidadCadena.noEsVacio(rubroPresupuestal) && Integer.parseInt(rubroPresupuestal)>0)) || (UtilidadCadena.noEsVacio(h_rubroPresupuestal) && Integer.parseInt(h_rubroPresupuestal)>0)) && !h_rubroPresupuestal.equals(rubroPresupuestal)||
									( ((UtilidadCadena.noEsVacio(cuentaCosto) && Integer.parseInt(cuentaCosto)>0)) || (UtilidadCadena.noEsVacio(cuentaCostoH) && Integer.parseInt(cuentaCostoH)>0)) && !h_rubroPresupuestal.equals(cuentaCosto)
									
								   )
								{
									int conse = 0; if (UtilidadCadena.noEsVacio(consecutivo)) { conse = Integer.parseInt(consecutivo); }

									//---El cero es para indicar que se van a insertar grupos. 
									resp1=cuentaUnidadFuncionalDao.insertarUnidadesFuncionales(con, 0, institucion, codUnidadFuncional, -1, cuentaIngreso, cuentaMedicamento, conse, modificado, cuentaIngresoVigAnterior , cuentaIngresoVigAnteriorMed, rubroPresupuestal, cuentaCosto);
								} 
								else { resp1=1; }	
								
								//---Para romper el ciclo y hacer el rollback
								if (resp1 < 1) { break; }
							}

							
						//--- Verificar si se inserto información (Sobre Todas Las unidades Funcionales).
							


						//if ( (UtilidadCadena.noEsVacio(this.getMapa("todasUnidades")+"") && Integer.parseInt(this.getMapa("todasUnidades")+"")>0 ) || ( UtilidadCadena.noEsVacio(this.getMapa("todasMedicamento")+"") && Integer.parseInt(this.getMapa("todasMedicamento")+"")>0 ) )
							if ((UtilidadCadena.noEsVacio(this.getMapa("todasUnidades")+"") && Integer.parseInt(this.getMapa("todasUnidades")+"")>0 ) || 
								( UtilidadCadena.noEsVacio(this.getMapa("todasMedicamento")+"") && Integer.parseInt(this.getMapa("todasMedicamento")+"")>0 ) ||
								(UtilidadCadena.noEsVacio(this.getMapa("todasUnidadesVA")+"") && Integer.parseInt(this.getMapa("todasUnidadesVA")+"")>0 )||
								(UtilidadCadena.noEsVacio(this.getMapa("todoscuentacosto")+"") && Utilidades.convertirAEntero((this.getMapa("todoscuentacosto")+""))>0 )
								)							
							
						{
							String cuentaIng = this.getMapa("todasUnidades")+"";
							String cuentaMed = this.getMapa("todasMedicamento")+"";
							//
							String cuentaVigAntIng = this.getMapa("todasUnidadesMEDVA")+"";
							String cuentaVigAntMed = this.getMapa("todasUnidadesVAMED")+"";
							String rubroPresupuestal = (this.getMapa("todosRubros")+ "");		//-Informacion capturada del jsp
							String cuentaCosto = (this.getMapa("todoscuentacosto")+ "");		//-Informacion capturada del jsp
							
							//---El cero es para indicar que se van a insertar grupos 
							resp1=cuentaUnidadFuncionalDao.insertarUnidadesFuncionales(con, 0, institucion, ""+ConstantesBD.codigoNuncaValido, -1, cuentaIng, cuentaMed, -1, "false", cuentaVigAntIng , cuentaVigAntMed, rubroPresupuestal, cuentaCosto );
						}
				}//--------------------FIN SI ES INSERCION DE UNIDADES FUNCIONALES 			
				
				//---------------------PARA INSERCION DE CENTROS DE COSTO POR UNIDAD FUNCIONAL.
				if ( tipoInsercion == 1)
				{
					String unidadFuncionalSel = this.getMapa("codigoUnidadFuncional")+""; //-Obtener la unidad funcional seleccionada. 
					//--------Para determinar si se insertar todos los centros de costo para las unidades funcionales especificas.

					for (int i = 0; i < nroUnidades; i++)
						{
							int centroCosto = Integer.parseInt(this.getMapa("codigo_cc_" + i) + "");			//-Codigo de la Unidad Funcional
							String modificado = this.getMapa("modificado_cc_" + i) + "";	 					//-Verificar si hay registro o no. 
							String cuentaIngreso = (this.getMapa("cuentaingreso_cc_" + i) + "");				//-informacion capturada del jsp
							String h_cuentaIngreso = (this.getMapa("h_cuentaingreso_cc_" + i) + "");			//-informacion para verificar si hubo cambio
							
							String cuentaMedicamento = (this.getMapa("cuentamedicamento_cc_" + i) + "");		//-informacion capturada del jsp
							String h_cuentaMedicamento = (this.getMapa("h_cuentamedicamento_cc_" + i) + "");	//-informacion para verificar si hubo cambio

							//
							String cuentaIngresoVigAnterior = (this.getMapa("cuenta_ingreso_vig_anterior_cc_" + i) + "").trim();		  	 //-Informacion capturada del jsp
							String h_cuentaIngresoVigAnterior = (this.getMapa("h_cta_ingreso_vig_anterior_cc_" + i) + "");		  	 //-Informacion para verificar si hubo cambio
							String cuentaIngresoVigAnteriorMed = (this.getMapa("cuenta_med_vig_anterior_cc_" + i) + "").trim();	 //-Informacion capturada del jsp
							String h_cuentaIngresoVigAnteriorMed = (this.getMapa("h_cuenta_med_vig_anterior_cc_" + i) + "");	 //-Informacion para verificar si hubo cambio
							//
							
							String rubroPresupuestal = (this.getMapa("rubro_cc_" + i)+ "");			//-Informacion capturada del jsp
							String h_rubroPresupuestal = (this.getMapa("h_rubro_cc_" + i) + "");	//-Informacion para verificar si hubo cambio
							
							String cuentacostocc= (this.getMapa("cuentacostocc_" + i)+ "");
							String cuentacostocch= (this.getMapa("cuentacostocch_" + i)+ "");
							
							
							String consecutivo = (this.getMapa("consecutivo_" + i) + "");	//-Llave para modificar el registro. 
	
							if ( modificado.equals("true") ) //---Se debe generar el LOG porque se modifico la cuenta ingreso
							{
								if ( !h_cuentaIngreso.equals(cuentaIngreso) || !h_cuentaMedicamento.equals(cuentaMedicamento) ) 
								{
									String log = "\n================================MODIFICACION DE CUENTAS INTERFAZ UNIDAD FUNCIONAL CENTRO COSTO =========================="; 
										   log += "\n CODIGO UNIDAD FUNCIONAL 	:  " + unidadFuncionalSel;
										   log += "\n NOMBRE UNIDAD FUNCIONAL   :  " + this.getNombreUnidadFuncional();  
										   log += "\n CENTRO COSTO : [" +  this.getMapa("codigo_cc_" + i) + "] -- " +  this.getMapa("nombre_cc_" + i);
										   log += "\n CUENTA INGRESO ANTERIOR 	:  " + h_cuentaIngreso; 
										   log += "\n CUENTA INGRESO NUEVA	 	:  " + cuentaIngreso; 
										   log += "\n CUENTA MEDICAMENTO ANTERIOR 	:  " + h_cuentaMedicamento; 
										   log += "\n CUENTA MEDICAMENTO NUEVA	 	:  " + cuentaMedicamento;
										   log += "\n CUENTA COSTO ANTERIOR 	:  " + cuentacostocch; 
										   log += "\n CUENTA MEDICAMENTO NUEVA	 	:  " + cuentacostocc; 
										   log += "\n========================================================================================================================";
										   
									//-Generar el log 
									LogsAxioma.enviarLog(ConstantesBD.logInterfazCuentaInterfazUnidadFuncionalCCCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
								}	
							}
	
							if (
									( ((UtilidadCadena.noEsVacio(cuentaIngreso) && Integer.parseInt(cuentaIngreso)>0)) || (UtilidadCadena.noEsVacio(h_cuentaIngreso) && Integer.parseInt(h_cuentaIngreso)>0)) && !h_cuentaIngreso.equals(cuentaIngreso) ||
									( ((UtilidadCadena.noEsVacio(cuentaMedicamento) && Integer.parseInt(cuentaMedicamento)>0)) || (UtilidadCadena.noEsVacio(h_cuentaMedicamento) && Integer.parseInt(h_cuentaMedicamento)>0)) && !h_cuentaMedicamento.equals(cuentaMedicamento) ||
									( ((UtilidadCadena.noEsVacio(cuentaIngresoVigAnterior) && Integer.parseInt(cuentaIngresoVigAnterior)>0)) || (UtilidadCadena.noEsVacio(h_cuentaIngresoVigAnterior) && Integer.parseInt(h_cuentaIngresoVigAnterior)>0)) && !h_cuentaIngresoVigAnterior.equals(cuentaIngresoVigAnterior) ||									
									( ((UtilidadCadena.noEsVacio(cuentaIngresoVigAnteriorMed) && Integer.parseInt(cuentaIngresoVigAnteriorMed)>0)) || (UtilidadCadena.noEsVacio(h_cuentaIngresoVigAnteriorMed) && Integer.parseInt(h_cuentaIngresoVigAnteriorMed)>0)) && !h_cuentaIngresoVigAnteriorMed.equals(cuentaIngresoVigAnteriorMed) ||
									( ((UtilidadCadena.noEsVacio(rubroPresupuestal) && Integer.parseInt(rubroPresupuestal)>0)) || (UtilidadCadena.noEsVacio(h_rubroPresupuestal) && Integer.parseInt(h_rubroPresupuestal)>0)) && !h_rubroPresupuestal.equals(rubroPresupuestal)||
									( ((UtilidadCadena.noEsVacio(cuentacostocc) && Integer.parseInt(cuentacostocc)>0)) || (UtilidadCadena.noEsVacio(cuentacostocch) && Integer.parseInt(cuentacostocch)>0)) && !cuentacostocch.equals(cuentacostocc)
								)
							{  
								logger.info("\n\n consecutivo --->  [" + consecutivo +"] [" + UtilidadCadena.noEsVacio(consecutivo)  +"] , modificado--> "+modificado+" \n\n");

								int conse = 0; if (UtilidadCadena.noEsVacio(consecutivo)) { conse = Integer.parseInt(consecutivo); }
								
								//---El uno es para indicar que se van a insertar centros de costo por unidad funcional 
								resp1=cuentaUnidadFuncionalDao.insertarUnidadesFuncionales(con, 1, institucion, unidadFuncionalSel, centroCosto, cuentaIngreso, cuentaMedicamento, conse,  modificado, cuentaIngresoVigAnterior , cuentaIngresoVigAnteriorMed, rubroPresupuestal,cuentacostocc);
							}  
							else {resp1=1;}	
							
							//---Para romper el ciclo y hacer el rollback
							if (resp1 < 1) { break; }
						}
			}//--------------------FIN SI ES INSERCION DE UNIDADES FUNCIONALES CENTRO COSTO 			
						
			//----Barre el mapa de dieta
			if (!inicioTrans||resp1<1)
			{
			    myFactory.abortTransaction(con);
				return -1;
			}
			else
			{
			    myFactory.endTransaction(con);
			}
			
		}	//-----Si Hay Registros en el Mapa
		
	  return 0;
	}	

	/**
	 *  Metodo para eliminar la Cuenta Ingreso ó Medicamento de una Unidad especifica. 
	 * @param con
	 * @param loginUsuario 
	 * @throws SQLException 
	 */
	public int eliminarCuenta(Connection con, int institucion, String loginUsuario) throws SQLException
	{
		int resp = 0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		if (cuentaUnidadFuncionalDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos ( cuentaUnidadFuncional - eliminarCuenta )");
		}

		if (
				UtilidadCadena.noEsVacio(mapa.get("esCuentaIngreso")+"") &&
				UtilidadCadena.noEsVacio(mapa.get("centroCostoEliminar")+"") &&
				UtilidadCadena.noEsVacio(mapa.get("consecutivoEliminar")+"")
		   )
		{
			//-------------------------------------------------------------------------------------------------------------------
			//----Iniciamos la transacción, si el estado es empezar
			boolean inicioTrans;
			inicioTrans=myFactory.beginTransaction(con);
		
			int consecutivo = Integer.parseInt(mapa.get("consecutivoEliminar")+"");
			boolean esCuentaIngreso = UtilidadTexto.getBoolean(mapa.get("esCuentaIngreso")+"");
			boolean esCuentaAnterior = UtilidadTexto.getBoolean(mapa.get("esCuentaAnterior")+"");
			int centroCosto = Integer.parseInt(mapa.get("centroCostoEliminar")+"");

			resp = cuentaUnidadFuncionalDao.eliminarCuenta(con, esCuentaIngreso, consecutivo, centroCosto,  institucion, esCuentaAnterior);
			
			//-- Si se elimino se debe generar el LOG.
			if ( resp > 0 )
			{
				//--Campos para generar el LOG.
				int posicion = Integer.parseInt(mapa.get("posicionE")+"");
				String nombreCuenta = this.getMapa("nombreCuentaE") + "";
				
				String log  = "";
				if ( centroCosto == -1 )
				{
					String codUnidadFuncional = this.getMapa("codigo_unidad_funcional_" + posicion) + "";	
										
				   log = "\n================================MODIFICACION DE CUENTAS INTERFAZ UNIDAD FUNCIONAL================================";
				   if ( !codUnidadFuncional.equals("-1") )
				   {	
					   log += "\n CODIGO UNIDAD FUNCIONAL 		:  " + this.getMapa("codigo_unidad_funcional_" + posicion);
					   log += "\n NOMBRE UNIDAD FUNCIONAL  		:  " + this.getMapa("nombre_unidad_funcional_" + posicion);
				   }
				   else
				   {
					   log += "\n TODAS LAS UNIDADES FUNCIONALES "; 
				   }
				   
				   if ( esCuentaIngreso )
				   {
					   log += "\n SE ELIMINO LA CUENTA DE INGRESO ";
					   log += "\n CODIGO : " + this.getMapa("h_cuentaingreso_unifun_" + posicion);
					   log += "\n NOMBRE : " + nombreCuenta;
				   }
				   else
				   {
					   log += "\n SE ELIMINO LA CUENTA DE MEDICAMENTO ";
					   log += "\n CODIGO   : " + this.getMapa("h_cuentamedicamento_unifun_" + posicion); 
					   log += "\n NOMBRE   : " + nombreCuenta; 
				   }
				   log += "\n================================================================================================================";

				   //--- Generar el log. 
				   LogsAxioma.enviarLog(ConstantesBD.logInterfazCuentaInterfazUnidadFuncionalCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);

				}
				else
				{
					   log =  "\n=======================MODIFICACIÓN DE CUENTAS INTERFAZ UNIDAD FUNCIONAL POR CENTRO DE COSTO=======================";
					   log += "\n CÓDIGO UNIDAD FUNCIONAL  :  " + this.getMapa("codigoUnidadFuncional");
					   log += "\n CÓDIGO CENTRO COSTO      :  " + this.getMapa("codigo_cc_" + posicion);
					   log += "\n NOMBRE CENTRO COSTO      :  " + this.getMapa("nombre_cc_" + posicion);

					   if ( esCuentaIngreso )
					   {
						   log += "\n SE ELIMINO LA CUENTA DE INGRESO ";
						   log += "\n CODIGO : " + this.getMapa("cuentaingreso_cc_" + posicion);
						   log += "\n NOMBRE : " + nombreCuenta;
					   }
					   else
					   {
						   log += "\n SE ELIMINO LA CUENTA DE MEDICAMENTO ";
						   log += "\n CODIGO   : " + this.getMapa("cuentamedicamento_cc_" + posicion); 
						   log += "\n NOMBRE   : " + nombreCuenta; 
					   }
					   log += "\n================================================================================================================";
					   
					   //--- Generar el log. 
					   LogsAxioma.enviarLog(ConstantesBD.logInterfazCuentaInterfazUnidadFuncionalCCCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
				}
				
			}
			
			
			//----Barre el mapa de dieta
			if (!inicioTrans||resp<1)
			{
			    myFactory.abortTransaction(con);
				return -1;
			}
			else
			{
			    myFactory.endTransaction(con);
			}

			return resp; 
		}

		return -1;
	}
	
	/**
	 * Metodo para eliminar un Rubro
	 * @param con
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 */
	public int eliminarRubro(Connection con, int institucion, String loginUsuario)  throws SQLException
	{
		int resp = 0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		if (cuentaUnidadFuncionalDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos ( cuentaUnidadFuncional - eliminarRubro )");
		}

		if (
				UtilidadCadena.noEsVacio(mapa.get("centroCostoEliminar")+"") &&
				UtilidadCadena.noEsVacio(mapa.get("consecutivoEliminar")+"")
		   )
		{
			//-------------------------------------------------------------------------------------------------------------------
			//----Iniciamos la transacción, si el estado es empezar
			boolean inicioTrans;
			inicioTrans=myFactory.beginTransaction(con);
		
			int consecutivo = Integer.parseInt(mapa.get("consecutivoEliminar")+"");
			
			int centroCosto = Integer.parseInt(mapa.get("centroCostoEliminar")+"");

			resp = cuentaUnidadFuncionalDao.eliminarRubro(con, consecutivo, centroCosto,  institucion);
			//----Barre el mapa de dieta
			if (!inicioTrans||resp<1)
			{
			    myFactory.abortTransaction(con);
				return ConstantesBD.codigoNuncaValido;
			}
			else
			{
			    myFactory.endTransaction(con);
			}

			return resp; 
		}

		return ConstantesBD.codigoNuncaValido;	
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
	 * @return Retorna mapa.
	 */
	public Object getMapa(String key)
	{
		return mapa.get(key);
	}

	/**
	 * @param Asigna mapa.
	 */
	public void setMapa(String key, Object obj) 
	{
		this.mapa.put(key, obj);
	}

	/**
	 * @return Retorna centroCosto.
	 */
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param Asigna centroCosto.
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return Retorna nombreUnidadFuncional.
	 */
	public String getNombreUnidadFuncional() {
		return nombreUnidadFuncional;
	}

	/**
	 * @param Asigna nombreUnidadFuncional.
	 */
	public void setNombreUnidadFuncional(String nombreUnidadFuncional) {
		this.nombreUnidadFuncional = nombreUnidadFuncional;
	}
}
