/*
 * @(#)RecargosTarifas.java
 * 
 * Created on 04-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RecargosTarifasDao;

/**
 * Clase para el manejo de un conjunto de recargos de tarifas 
 * 
 * @version 1.0, 04-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class RecargosTarifas
{
	
	private String nombreConvenio;
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RecargosTarifas.class);	
	
	/**
	 * Lista con los recargos de las tarifas 
	 */
	private ArrayList recargosTarifas;
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private RecargosTarifasDao recargosTarifasDao = null; 
	
	
	/**
	 * Constructora de la clase
	 */
	public RecargosTarifas()
	{
		this.recargosTarifas = new ArrayList();
		this.init(System.getProperty("TIPOBD"));			
	}
	
	/** 
	 * Creadora de la clase RecargosTarifas
	 * @param recargosTarifas. ArrayList, listado con los recargos de las Tarifas
	 */
	public RecargosTarifas(ArrayList recargosTarifas)
	{
		this.recargosTarifas = new ArrayList(recargosTarifas);
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public void init(String tipoBD)
	{
		if( recargosTarifasDao == null)
		{
			if(tipoBD==null)
			{
				logger.error("No esta llegando el tipo de base de datos");
				System.exit(1);
			}
			else
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				if (myFactory != null)
				{
					recargosTarifasDao = myFactory.getRecargosTarifasDao();
				}					
			}
		}
	}
		
	
	/**
	 * Retorna el recargo de la tarifa al indice dado dentro de la colección.
	 * Si el indice es mayor o igual al tamaño de la colección retorna null
	 * @param indice. int, indice a retornar de la colección
	 * @return
	 */
	public RecargoTarifa getRecargoTarifa(int indice)
	{
		if( indice >= this.recargosTarifas.size() )
			return null;
		else
			return (RecargoTarifa)this.recargosTarifas.get(indice);		
	}
	
	/**
	 * Adiciona al final de la colección el recargo de tarifa dada.
	 * @param tarifa. RecargoTarifa, REcargo de la Tarifa a ingresar
	 */
	public void setRecargoTarifa(RecargoTarifa recargoTarifa)
	{
		this.recargosTarifas.add(recargoTarifa);
	}
	
	/**
	 * Retorna el número de recargos existentes en la colección
	 * @return
	 */
	public int getNumRecargosTarifas()
	{
		return this.recargosTarifas.size();
	}
	
	/**
	 * Elimina todos los recargos de la colección
	 */
	public void resetRecargosTarifas()
	{
		this.recargosTarifas = new ArrayList();
	}
	
	/**
	 * Consulta todos los recargos que cumplan con los parametros ingresados.
	 * 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigo, int, código del recargo a modificar
	 * @param buscarPorCodigo, boolean, dice si se debe filtrar por código o no
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param buscarPorPorcentaje, boolean, dice si se debe filtrar por porcentaje o no
	 * @param valor. double, valor de recargo de la tarifa
	 * @param buscarPorValor, boolean, dice si se debe filtrar por valor o no
	 * @param codigoTipoRecargo. int, código del tipo de recargo
	 * @param buscarPorCodigoTipoRecargo, boolean, dice si se debe filtrar por el código del Tipo de Recargo o no
	 * @param codigoServicio. int, código del servicio asociado esta tarifa, 0 es para todos
	 * @param buscarPorCodigoServicio, boolean, dice si se debe filtrar por el código del Servicio o no
	 * @param codigoEspecialidad. int, código de la especialidad asociado esta tarifa, 0 es para todas
	 * @param buscarPorCodigoEspecialidad, boolean, dice si se debe filtrar por el código de la especialidad o no
	 * @param codigoViaIngreso. int, código de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param buscarPorCodigoViaIngreso, boolean, dice si se debe filtrar por el código de la Via de Ingreso o no
	 * @param codigoContrato. int, código del contrato para el cual es válido este recargo
	 * @param buscarPorCodigoContrato, boolean, dice si se debe filtrar por el código de Contrato o no
	 * @return ResultadoBoolean, true si la modificacion fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public ResultadoBoolean consultar(	Connection con,
																int codigo,
																boolean buscarPorCodigo,
																double porcentaje,
																boolean buscarPorPorcentaje,
																double valor,
																boolean buscarPorValor,
																int codigoTipoRecargo,
																boolean buscarPorCodigoTipoRecargo,
																int codigoServicio,
																boolean buscarPorCodigoServicio,
																int codigoEspecialidad,
																boolean buscarPorCodigoEspecialidad,
																int codigoViaIngreso,
																boolean buscarPorCodigoViaIngreso,
																int codigoContrato,
																boolean buscarPorCodigoContrato,
																int codigoConvenio) throws SQLException
	{
	
	ResultadoCollectionDB resultado = recargosTarifasDao.consultar(con,
	codigo,
	buscarPorCodigo,
	porcentaje,
	buscarPorPorcentaje,
	valor,
	buscarPorValor,
	codigoTipoRecargo,
	buscarPorCodigoTipoRecargo,
	codigoServicio,
	buscarPorCodigoServicio,
	codigoEspecialidad,
	buscarPorCodigoEspecialidad,
	codigoViaIngreso,
	buscarPorCodigoViaIngreso,
	codigoContrato,
	buscarPorCodigoContrato,
	codigoConvenio);
		if( !resultado.isTrue() )
			return new ResultadoBoolean(false, resultado.getDescripcion());
		else
		{
			LinkedList listado = (LinkedList)resultado.getFilasRespuesta();
			int tam = 0;			
			if( listado != null && (tam = listado.size()) > 0 )
			{		
				for( int i=0; i < tam; i++ )
				{
					HashMap recargo = (HashMap)(listado).get(i);					
					RecargoTarifa temp = new RecargoTarifa();
					temp.setCodigo(((Integer)recargo.get("codigo")).intValue());
					temp.setValor(((Double)recargo.get("valor")).doubleValue());
					temp.setPorcentaje(((Double)recargo.get("porcentaje")).doubleValue());

					if(recargo.get("codigoespecialidad")!=null){
						temp.setCodigoEspecialidad(((Integer)recargo.get("codigoespecialidad")).intValue());
					}else{
						temp.setCodigoEspecialidad(0);
						temp.setNombreEspecialidad("Todas");
					}
					temp.setCodigoContrato(((Integer)recargo.get("codigocontrato")).intValue());
					temp.setNumeroContrato(recargo.get("numerocontrato").toString());
					temp.setConvenio(((Integer)recargo.get("codigoconvenio")).intValue());
					temp.setNombreConvenio(recargo.get("nombreconvenio").toString());
					if(i==0){
						this.setNombreConvenio(recargo.get("nombreconvenio").toString());
					}
					if(recargo.get("codigoservicio")!=null){
						temp.setCodigoServicio(((Integer)recargo.get("codigoservicio")).intValue());
						temp.setNombreServicio(recargo.get("descripcion").toString());
					}else{
						temp.setCodigoServicio(0);		
						temp.setNombreServicio("Todos");				
					}
					
					
					temp.setCodigoTipoRecargo(((Integer)recargo.get("codigotiporecargo")).intValue());
					
					if(recargo.get("nombreviaingreso")!=null){
						temp.setNombreViaIngreso(recargo.get("nombreviaingreso").toString());
						temp.setCodigoViaIngreso(((Integer)recargo.get("viaingreso")).intValue());
					}else{
						temp.setNombreViaIngreso("Todas");
						temp.setCodigoViaIngreso(0);
					}
					
					temp.setNombreTipoRecargo(recargo.get("nombretiporecargo").toString());
				
					this.setRecargoTarifa(temp);
				}
					
			}
			return new ResultadoBoolean(true);
		}
	
	
	}	
	
	
	/**
	 * Consulta Avanzada de los parametros ingresados (esta consulta reemplaza la anterior por motivos de modelado).
	 * 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigo, int, código del recargo a modificar
	 * @param buscarPorCodigo, boolean, dice si se debe filtrar por código o no
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param buscarPorPorcentaje, boolean, dice si se debe filtrar por porcentaje o no
	 * @param valor. double, valor de recargo de la tarifa
	 * @param buscarPorValor, boolean, dice si se debe filtrar por valor o no
	 * @param codigoTipoRecargo. int, código del tipo de recargo
	 * @param buscarPorCodigoTipoRecargo, boolean, dice si se debe filtrar por el código del Tipo de Recargo o no
	 * @param codigoServicio. int, código del servicio asociado esta tarifa, 0 es para todos
	 * @param buscarPorCodigoServicio, boolean, dice si se debe filtrar por el código del Servicio o no
	 * @param codigoEspecialidad. int, código de la especialidad asociado esta tarifa, 0 es para todas
	 * @param buscarPorCodigoEspecialidad, boolean, dice si se debe filtrar por el código de la especialidad o no
	 * @param codigoViaIngreso. int, código de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param buscarPorCodigoViaIngreso, boolean, dice si se debe filtrar por el código de la Via de Ingreso o no
	 * @param codigoContrato. int, código del contrato para el cual es válido este recargo
	 * @param buscarPorCodigoContrato, boolean, dice si se debe filtrar por el código de Contrato o no
	 * @return Collection
	 */
	public Collection listarRecargosAvanzada(Connection con,
														int codigo,
														boolean buscarPorCodigo,
														double porcentaje,
														boolean buscarPorPorcentaje,
														double valor,
														boolean buscarPorValor,
														int codigoTipoRecargo,
														boolean buscarPorCodigoTipoRecargo,
														int codigoServicio,
														boolean buscarPorCodigoServicio,
														int codigoEspecialidad,
														boolean buscarPorCodigoEspecialidad,
														int codigoViaIngreso,
														boolean buscarPorCodigoViaIngreso,
														int codigoContrato,
														boolean buscarPorCodigoContrato,
														int codigoConvenio)
	{
	    recargosTarifasDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecargosTarifasDao();
	    ResultadoCollectionDB coleccion ;
	    
	    try 
	    {
			coleccion =  recargosTarifasDao.consultar(con,
													    	codigo,
													    	buscarPorCodigo,
													    	porcentaje,
													    	buscarPorPorcentaje,
													    	valor,
													    	buscarPorValor,
													    	codigoTipoRecargo,
													    	buscarPorCodigoTipoRecargo,
													    	codigoServicio,
													    	buscarPorCodigoServicio,
													    	codigoEspecialidad,
													    	buscarPorCodigoEspecialidad,
													    	codigoViaIngreso,
													    	buscarPorCodigoViaIngreso,
													    	codigoContrato,
													    	buscarPorCodigoContrato,
													    	codigoConvenio);
		} 
	    catch (Exception e) 
	    {
			logger.warn("Error mundo Recargos Tarifas con el codigo->"+codigo+" "+e.toString());
			coleccion = null;
		}
	    
		return (Collection) coleccion;
	}
	
	/**
	 * Returns the recargosTarifas.
	 * @return ArrayList
	 */
	public ArrayList getRecargosTarifas()
	{
		return recargosTarifas;
	}

	/**
	 * Sets the recargosTarifas.
	 * @param recargosTarifas The recargosTarifas to set
	 */
	public void setRecargosTarifas(ArrayList recargosTarifas)
	{
		this.recargosTarifas = recargosTarifas;
	}
	

	/**
	 * Returns the nombreConvenio.
	 * @return String
	 */
	public String getNombreConvenio()
	{
		return nombreConvenio;
	}

	/**
	 * Sets the nombreConvenio.
	 * @param nombreConvenio The nombreConvenio to set
	 */
	public void setNombreConvenio(String nombreConvenio)
	{
		this.nombreConvenio = nombreConvenio;
	}

}
