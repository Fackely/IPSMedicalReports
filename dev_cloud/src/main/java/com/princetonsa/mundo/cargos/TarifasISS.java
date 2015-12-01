/*
 * @(#)TarifasISS.java
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

import org.apache.log4j.Logger;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TarifasISSDao;

/**
 * Clase para el manejo de un conjunto de tarifas iss
 * 
 * @version 1.0, 04-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class TarifasISS
{
	
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(TarifasISS.class);	
	
	/**
	 * Lista con las tarifas iss
	 */
	private ArrayList tarifasISS;
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private TarifasISSDao tarifasISSDao = null;
	
	/**
	 * Variable para almacenar los codigos de Servicios de la Consulta
	 */
	private String cadenaCodigosServicios;

	/**
	 * Constructora de la clase
	 */
	public TarifasISS()
	{
		this.tarifasISS = new ArrayList();
		this.init(System.getProperty("TIPOBD"));			
	}
	
	/** 
	 * Creadora de la clase TarifasISS.java
	 * @param tarifasISS. ArrayList, listado con las tarifas ISS
	 */
	public TarifasISS(ArrayList tarifasISS)
	{
		this.tarifasISS = new ArrayList(tarifasISS);
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
		if( tarifasISSDao == null)
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
					tarifasISSDao = myFactory.getTarifasISSDao();
				}					
			}
		}
	}
		
	
	/**
	 * Retorna la tarifa iss correspondiente al indice dado dentro de la colección.
	 * Si el indice es mayor o igual al tamaño de la colección retorna null
	 * @param indice. int, indice a retornar de la colección
	 * @return
	 */
	public TarifaISS getTarifaISS(int indice)
	{
		if( indice >= this.tarifasISS.size() )
			return null;
		else
			return (TarifaISS)this.tarifasISS.get(indice);		
	}
	
	/**
	 * Adiciona al final de la colección la tarifa dada.
	 * @param tarifa. TarifaISS, tarifa ISS a ingresar
	 */
	public void setTarifaISS(TarifaISS tarifa)
	{
		this.tarifasISS.add(tarifa);
	}
	
	/**
	 * Retorna el número de tarifas ISS existentes en la colección
	 * @return
	 */
	public int getNumTarifasISS()
	{
		return this.tarifasISS.size();
	}
	
	/**
	 * Elimina todas las tarifas ISS de la colección
	 */
	public void resetTarifasISS()
	{
		this.tarifasISS = new ArrayList();
	}
	
	/**
	 *	Consulta todas las tarifas iss que cumplan con los parametros ingresados.
	 *
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoTarifa. int, código de la tarifa
	 * @param buscarPorCodigoTarifa, boolean, dice si se debe filtrar por el código de la tarifa o no
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario
	 * @param buscarPorCodigoEsquemaTarifario, boolean, dice si se debe filtrar por el código del esquema tarifario o no
	 * @param codigoServicio. int, código del servicio
	 * @param buscarPorCodigoServicio, boolean, dice si se debe filtrar por el código del servicio o no
	 * @param codigoTipoLiquidacion. int, código del tipo de liquidación
	 * @param buscarPorCodigoTipoLiquidacion, boolean, dice si se debe filtrar por el codigo de tipo de liquidación o no
	 * @param valorTarifa. double, valor de la tarifa
	 * @param buscarPorValorTarifa, boolean, dice si se debe filtrar por el valor de la tarifa o no
	 * @param porcentajeIva. double, porcentaje iva
	 * @param buscarPorPorcentajeIva, boolean, dice si se debe filtrar por el porcentaje de iva o no
	 * @param liquidar Asocios
	 * @param buscarPorLiquidarAsocios
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
																		double valorTarifa,
																		boolean buscarPorValorTarifa,																		
																		double porcentajeIva,
																		boolean buscarPorPorcentajeIva,
																		String nombreServicio,
																		boolean buscarPorNombreServicio,
																		int codigoTipoLiquidacion,
																		boolean buscarPorCodigoTipoLiquidacion,
																		boolean manejaConversionMoneda,
																		String liquidarAsocios,
																		boolean buscarLiqudacioAsocios,
																		int index,
																		HashMap tiposMonedaTagMap,
																		int institucion) throws SQLException
	{
		ResultadoCollectionDB resultado = tarifasISSDao.consultar(con,
	 	codigoTarifa,
		buscarPorCodigoTarifa,
		codigoEsquemaTarifario,
		buscarPorCodigoEsquemaTarifario,
		codigoServicio,
		buscarPorCodigoServicio,
		valorTarifa,
		buscarPorValorTarifa,																		
		porcentajeIva,
		buscarPorPorcentajeIva,
		nombreServicio,
		buscarPorNombreServicio,
		codigoTipoLiquidacion,
		buscarPorCodigoTipoLiquidacion,
		liquidarAsocios,
		buscarLiqudacioAsocios,
		institucion
		);		
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
					TarifaISS temp = new TarifaISS();
					temp.setCodigo(Utilidades.convertirAEntero(recargo.get("codigo")+""));
					temp.setCodigoEsquemaTarifario(Utilidades.convertirAEntero(recargo.get("codigoesquematarifario")+""));
					temp.setNombreEsquemaTarifario(recargo.get("nombreesquematarifario").toString());
					if(recargo.get("valortarifa")!=null)
						temp.setValorTarifa(Utilidades.convertirADouble(recargo.get("valortarifa")+""));
					temp.setPorcentajeIva((Utilidades.convertirADouble(recargo.get("porcentajeiva")+"",true)));
					temp.setCodigoServicio( Utilidades.convertirAEntero(recargo.get("codigoservicio")+""));
					
					if(!esPrimero)
					{
						logger.info("\n\n11111");
						this.cadenaCodigosServicios+=recargo.get("codigoservicio")+"";
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
					temp.setCodigoTipoLiquidacion( Utilidades.convertirAEntero(recargo.get("codigotipoliquidacion")+""));
					temp.setNombreTipoLiquidacion(recargo.get("nombretipoliquidacion").toString());
					temp.setLiquidarAsocios(recargo.get("liqasocios").toString());
					if(recargo.get("fechavigencia")!=null)
						temp.setFechaVigencia(recargo.get("fechavigencia")+"");
					else
						temp.setFechaVigencia("");
					temp.setIndex(index);
					
					logger.info("mundo.getManejaConversionMoneda()-->"+manejaConversionMoneda+" index->"+index);
					if(manejaConversionMoneda && index>=0 && !UtilidadTexto.isEmpty(recargo.get("valortarifa")+""))
					{
						logger.info("factor->"+UtilidadTexto.formatearExponenciales(Double.parseDouble(tiposMonedaTagMap.get("factorconversion_"+index).toString())));
						double factor= Double.parseDouble(tiposMonedaTagMap.get("factorconversion_"+index).toString());
						if(factor>0)
						{
							temp.setValorTarifaConversionMoneda(temp.getValorTarifa()/factor);
						}
					}
					
					//logger.info("\n\nVLOR TARIFA CONVERSION MONEDA----->"+temp.getValorTarifaConversionMoneda()+"\n\n");
					
					if(recargo.get("unidades")!=null && !recargo.get("unidades").equals(""))
						try{
							temp.setUnidades(Double.parseDouble(recargo.get("unidades")+""));
						}
						catch (NumberFormatException e) {
							// Hasta aqui no debería llegar, pero hay que asegurar que no salga error
							logger.error("Error cargando las unidades del servicio "+e);
							e.printStackTrace();
							temp.setUnidades(0);
						}
					this.setTarifaISS(temp);
					
				}
			
			}
			return new ResultadoBoolean(true);
		}

			
	}
	
	/**
	 * Metodo que consulta todas las fechas vigencia por esquema tarifario servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param servicio
	 * @return
	 */
	public HashMap consultarFechasVigencia(Connection con, String esquemaTarifario, String servicio, String cadCodServ)
	{
		return this.tarifasISSDao.consultarFechasVigencia(con, esquemaTarifario, servicio, cadCodServ);
	}

	/**
	 * Returns the tarifasISS.
	 * @return ArrayList
	 */
	public ArrayList getTarifasISS()
	{
		return tarifasISS;
	}

	/**
	 * Sets the tarifasISS.
	 * @param tarifasISS The tarifasISS to set
	 */
	public void setTarifasISS(ArrayList tarifasISS)
	{
		this.tarifasISS = tarifasISS;
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
