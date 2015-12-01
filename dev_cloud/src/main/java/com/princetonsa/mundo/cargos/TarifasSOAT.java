/*
 * @(#)TarifasSoat.java
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
import java.util.HashMap;
import java.util.LinkedList;

import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TarifasSOATDao;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Clase para el manejo de un conjunto de tarifas soat
 * 
 * @version 1.0, 04-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class TarifasSOAT
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(TarifasSOAT.class);	
	
	/**
	 * Lista con las tarifas soat
	 */
	private ArrayList tarifasSOAT;
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private TarifasSOATDao tarifasSOATDao = null;
	
	/**
	 * Variable para almacenar los codigos de Servicios de la Consulta
	 */
	private String cadenaCodigosServicios;

	/**
	 * Constructora de la clase
	 */
	public TarifasSOAT()
	{
		this.tarifasSOAT = new ArrayList();
		this.init(System.getProperty("TIPOBD"));			
	}
	
	/** 
	 * Creadora de la clase TarifasSoat.java
	 * @param tarifasSoat. ArrayList, listado con las tarifas soat
	 */
	public TarifasSOAT(ArrayList tarifasSoat)
	{
		this.tarifasSOAT = new ArrayList(tarifasSoat);
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
		if( tarifasSOATDao == null)
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
					tarifasSOATDao = myFactory.getTarifasSOATDao();
				}					
			}
		}
	}
		
	
	/**
	 * Retorna la tarifa soat correspondiente al indice dado dentro de la colección.
	 * Si el indice es mayor o igual al tamaño de la colección retorna null
	 * @param indice. int, indice a retornar de la colección
	 * @return
	 */
	public TarifaSOAT getTarifaSOAT(int indice)
	{
		if( indice >= this.tarifasSOAT.size() )
			return null;
		else
			return (TarifaSOAT)this.tarifasSOAT.get(indice);		
	}
	
	/**
	 * Adiciona al final de la colección la tarifa dada.
	 * @param tarifa. TarifaSOAT, tarifa soat a ingresar
	 */
	public void setTarifaSOAT(TarifaSOAT tarifa)
	{
		this.tarifasSOAT.add(tarifa);
	}
	
	/**
	 * Retorna el número de tarifas soat existentes en la colección
	 * @return
	 */
	public int getNumTarifasSOAT()
	{
		return this.tarifasSOAT.size();
	}
	
	/**
	 * Elimina todas las tarifas soat de la colección
	 */
	public void resetTarifasSOAT()
	{
		this.tarifasSOAT = new ArrayList();
	}
	
	
	/**
	 *	Consulta todas las tarifas soat que cumplan con los parametros ingresados.
	 *
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa. int, código de la tarifa
	 * @param buscarPorCodigoTarifa, boolean, dice si se debe filtrar por el código de la tarifa o no
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario
	 * @param buscarPorCodigoEsquemaTarifario, boolean, dice si se debe filtrar por el código del esquema tarifario o no
	 * @param codigoServicio. int, código del servicio
	 * @param buscarPorCodigoServicio, boolean, dice si se debe filtrar por el código del servicio o no
	 * @param grupo. int, grupo de la tarifa
	 * @param buscarPorGrupo, boolean, dice si se debe filtrar por grupo o no
	 * @param codigoTipoLiquidacion. int, código del tipo de liquidación
	 * @param buscarPorCodigoTipoLiquidacion, boolean, dice si se debe filtrar por el codigo de tipo de liquidación o no
	 * @param valorTarifa. double, valor de la tarifa
	 * @param buscarPorValorTarifa, boolean, dice si se debe filtrar por el valor de la tarifa o no
	 * @param porcentajeIva. double, porcentaje iva
	 * @param buscarPorPorcentajeIva, boolean, dice si se debe filtrar por el porcentaje de iva o no
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 */
	public ResultadoBoolean consultar(	Connection con,
																				int codigoTarifa,
																				boolean buscarPorCodigoTarifa,
																				int codigoEsquemaTarifario,
																				boolean buscarPorCodigoEsquemaTarifario,
																				String codigoServicio,
																				boolean buscarPorCodigoServicio,
																				double grupo,
																				boolean buscarPorGrupo,
																				int codigoTipoLiquidacion,
																				boolean buscarPorCodigoTipoLiquidacion,														
																				double valorTarifa,
																				boolean buscarPorValorTarifa,
																				double porcentajeIva,
																				boolean buscarPorPorcentajeIva,
																				String nombreServicio,
																				boolean buscarPorNombreServicio,
																				boolean manejaConversionMoneda,
																				String liquidarAsocios,
																				boolean buscarPorLiquidarAsocios,
																				int index,
																				HashMap tiposMonedaTagMap) throws SQLException
	{
		ResultadoCollectionDB resultado = tarifasSOATDao.consultar( con,
		codigoTarifa, buscarPorCodigoTarifa,
		codigoEsquemaTarifario, buscarPorCodigoEsquemaTarifario,
		codigoServicio, buscarPorCodigoServicio,
		grupo, buscarPorGrupo,
		codigoTipoLiquidacion, buscarPorCodigoTipoLiquidacion,
		valorTarifa, buscarPorValorTarifa,
		porcentajeIva,buscarPorPorcentajeIva,
		nombreServicio,buscarPorNombreServicio,
		liquidarAsocios,buscarPorLiquidarAsocios);
		
		boolean esPrimero=false;
		this.cadenaCodigosServicios="";
		
		if( !resultado.isTrue() )
					return new ResultadoBoolean(false, resultado.getDescripcion());
		else
		{
			ArrayList listado = (ArrayList)resultado.getFilasRespuesta();
			int tam = 0;			
			if( listado != null && (tam = listado.size()) > 0 )
			{		
				for( int i=0; i < tam; i++ )
				{
					HashMap recargo = (HashMap)(listado).get(i);					
					TarifaSOAT temp = new TarifaSOAT();
					
					temp.setCodigo(Utilidades.convertirAEntero(recargo.get("codigo")+""));
					temp.setCodigoEsquemaTarifario(Utilidades.convertirAEntero(recargo.get("codigoesquematarifario")+""));
					temp.setNombreEsquemaTarifario(recargo.get("nombreesquematarifario").toString());
					
					if(recargo.get("grupo")!=null)
						temp.setGrupo(Utilidades.convertirADouble(recargo.get("grupo")+""));
					if(recargo.get("valortarifa")!=null)
						temp.setValorTarifa(Utilidades.convertirADouble(recargo.get("valortarifa")+""));
					
					try
					{
						temp.setPorcentajeIva(Utilidades.convertirADouble(recargo.get("porcentajeiva")+""));
					}
					catch (Exception e) 
					{
						temp.setPorcentajeIva(0);
					}
					temp.setCodigoServicio(Utilidades.convertirAEntero(recargo.get("codigoservicio")+""));
					
					if(!esPrimero)
					{
						logger.info("\n\n11111");
						this.cadenaCodigosServicios+=(Utilidades.convertirAEntero(recargo.get("codigoservicio")+""));
						esPrimero=true;
						logger.info("\n\n22222 >> "+this.cadenaCodigosServicios);
					}
					else
					{
						logger.info("\n\n3333333");
						this.cadenaCodigosServicios+=","+recargo.get("codigoservicio")+"";
						logger.info("\n\n44444444 >> "+this.cadenaCodigosServicios);
					}					
					
					temp.setNombreServicio(recargo.get("nombreservicio").toString());
					temp.setCodigoEspecialidad(Utilidades.convertirAEntero(recargo.get("codigoespecialidad")+""));
					temp.setNombreEspecialidad(recargo.get("nombreespecialidad").toString());
					
					if(recargo.get("codigotipoliquidacion")!=null)
						temp.setCodigoTipoLiquidacion(Utilidades.convertirAEntero(recargo.get("codigotipoliquidacion")+""));
					else
						temp.setCodigoTipoLiquidacion(-1);
					
					if(recargo.get("nombretipoliquidacion")!=null)
						temp.setNombreTipoLiquidacion(recargo.get("nombretipoliquidacion").toString());
					else
						temp.setNombreTipoLiquidacion("");
					
					temp.setLiquidarAsocios(recargo.get("liqasocios").toString());
					
					if(recargo.get("fechavigencia")!=null)
						temp.setFechaVigencia(recargo.get("fechavigencia")+"");
					else
						temp.setFechaVigencia("");
					
					temp.setIndex(index);
					
					//logger.info("mundo.getManejaConversionMoneda()-->"+manejaConversionMoneda+" index->"+index);
					if(manejaConversionMoneda && index>=0 && !UtilidadTexto.isEmpty(recargo.get("valortarifa")+""))
					{
						logger.info("factor->"+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(tiposMonedaTagMap.get("factorconversion_"+index).toString())));
						double factor= Utilidades.convertirADouble(tiposMonedaTagMap.get("factorconversion_"+index).toString());
						if(factor>0)
						{
							temp.setValorTarifaConversionMoneda(temp.getValorTarifa()/factor);
						}
					}
					
					//logger.info("\n\nVLOR TARIFA CONVERSION MONEDA----->"+temp.getValorTarifaConversionMoneda()+"\n\n");
					
					
					this.setTarifaSOAT(temp);
				}
					
			}
			return new ResultadoBoolean(true);
		}
				
		
	}
	
	/**
	 * Metodo que consulta todas las fechas vigencia por esquema servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param servicio
	 * @param cadenaCodigosServicio
	 * @return
	 */
	public HashMap consultarFechasVigencia(Connection con, String esquemaTarifario, String servicio, String cadenaCodigosServicios)
	{
		return this.tarifasSOATDao.consultarFechasVigencia(con, esquemaTarifario, servicio, cadenaCodigosServicios);
	}

	/**
	 * Returns the tarifasSOAT.
	 * @return ArrayList
	 */
	public ArrayList getTarifasSOAT()
	{
		return tarifasSOAT;
	}

	/**
	 * Sets the tarifasSOAT.
	 * @param tarifasSOAT The tarifasSOAT to set
	 */
	public void setTarifasSOAT(ArrayList tarifasSOAT)
	{
		this.tarifasSOAT = tarifasSOAT;
	}

	/**
	 * @return the cadenaCodigosServicios
	 */
	public String getCadenaCodigosServicios() {
		return cadenaCodigosServicios;
	}

	/**
	 * @param cadenaCodigosServicios the cadenaCodigosServicios to set
	 */
	public void setCadenaCodigosServicios(String cadenaCodigosServicios) {
		this.cadenaCodigosServicios = cadenaCodigosServicios;
	}

}
