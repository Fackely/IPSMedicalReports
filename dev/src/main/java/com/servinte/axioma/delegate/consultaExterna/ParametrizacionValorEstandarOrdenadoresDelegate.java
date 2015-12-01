/**
 * 
 */
package com.servinte.axioma.delegate.consultaExterna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.axioma.util.log.Log4JManager;


import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.consultaExterna.ValorEstandarOrdenadoresDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.ParametricaValorEstandar;
import com.servinte.axioma.orm.UnidadesConsulta;

/**
 * Clase de implementa los metodos de integracion con la base de datos
 * asociados a la parametrica de consulta externa
 * 
 * @author ginsotfu
 * @version 1.0
 * @created 26/10/2012
 */

public class ParametrizacionValorEstandarOrdenadoresDelegate{
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Metodo que obtiene ordenes tipo medicamento 
	 * 
	 * @param tipoOrden
	 * @return
	 * @throws BDException
	 * @author ginsotfu
	 */

	@SuppressWarnings("unchecked")
	public List<ValorEstandarOrdenadoresDto> obtenerMedicamentos(int tipoOrden) throws BDException{
		try{
			List<ValorEstandarOrdenadoresDto> ordenesMedicamentos=new ArrayList<ValorEstandarOrdenadoresDto>(0);
			if (tipoOrden==1){
				
				persistenciaSvc= new PersistenciaSvc();
				ordenesMedicamentos=(List<ValorEstandarOrdenadoresDto>)persistenciaSvc.createNamedQuery("catalogoConsultaExterna.consultarOrdenMedicamentos");
				
			}
			return ordenesMedicamentos ;
		}
		catch (Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo que obtiene ordenes tipo servicio 
	 * 
	 * @param tipoOrden
	 * @return
	 * @throws BDException
	 * @author ginsotfu
	 */
	
	@SuppressWarnings("unchecked")
	public List<ValorEstandarOrdenadoresDto> obtenerServicios(int tipoOrden) throws BDException{
		List<ValorEstandarOrdenadoresDto> ordenesServicios = new ArrayList<ValorEstandarOrdenadoresDto>(0);
		try{
			if (tipoOrden==0){
				persistenciaSvc= new PersistenciaSvc();
				ordenesServicios=(List<ValorEstandarOrdenadoresDto>)persistenciaSvc.createNamedQuery("catalogoConsultaExterna.consultarOrdenServicios");
				
			}
			return ordenesServicios;
		}
		catch (Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo que obtiene las unidades de agenda
	 * 
	 * @param 
	 * @return
	 * @throws BDException
	 * @author ginsotfu
	 */
	
	@SuppressWarnings("unchecked")
	public List<DtoUnidadesConsulta> consultarUnidadAgenda() throws BDException{
		List<DtoUnidadesConsulta> listaUnidadAgenda=new ArrayList<DtoUnidadesConsulta>(0);
		try{
			
			persistenciaSvc= new PersistenciaSvc();
			listaUnidadAgenda=(List<DtoUnidadesConsulta>)persistenciaSvc.createNamedQuery("catalogoConsultaExterna.consultarUnidadAgenda");
			return listaUnidadAgenda;
			
			
			}
			catch (Exception e){
				Log4JManager.error(e);
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
			}		
	}
	
	/**
	 * Metodo que guarda parametrica nueva
	 * 
	 * @param valor
	 * @return
	 * @throws BDException
	 * @author ginsotfu
	 */
	public void ingresarOrden (ValorEstandarOrdenadoresDto valor) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();

			ParametricaValorEstandar parametrica= new ParametricaValorEstandar();
			GruposServicios gruposServicios= new GruposServicios();
			UnidadesConsulta unidadesConsulta= new UnidadesConsulta();
			ClaseInventario claseInventario=new ClaseInventario();
			
				//Si es 0 es servicio 
				if(valor.getTipoOrden()==0){
					gruposServicios.setCodigo(valor.getCodigoGruposServicio());
					unidadesConsulta.setCodigo(valor.getUnidadAgenda());
					parametrica.setGruposServicios(gruposServicios);
					parametrica.setUnidadesConsulta(unidadesConsulta);
					parametrica.setValorEstOrdCita(valor.getValorEstOrdCita());
					parametrica.setValorEstSermedOrden(valor.getValorEstSermedOrden());
					parametrica.setValorEstSermedCita(valor.getValorEstSermedCita());
					parametrica.setEsServicio(true);
				}
				//Si es 1 es medicamento 
				else{
					claseInventario.setCodigo(valor.getCodigoClaseInventarios());
					unidadesConsulta.setCodigo(valor.getUnidadAgenda());
					parametrica.setClaseInventario(claseInventario);
					parametrica.setUnidadesConsulta(unidadesConsulta);
					parametrica.setValorEstOrdCita(valor.getValorEstOrdCita());
					parametrica.setValorEstSermedOrden(valor.getValorEstSermedOrden());
					parametrica.setValorEstSermedCita(valor.getValorEstSermedCita());
					parametrica.setEsServicio(false);
				}
			
			persistenciaSvc.persist(parametrica);
			
		}catch (Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}		
	
	}
	
	/**
	 * Metodo que modifica parametrica 
	 * 
	 * @param valor
	 * @return
	 * @throws BDException
	 * @author ginsotfu
	 */
	
	public void modificarOrden (ValorEstandarOrdenadoresDto valor) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			

			ParametricaValorEstandar parametrica= new ParametricaValorEstandar();
			GruposServicios gruposServicios= new GruposServicios();
			UnidadesConsulta unidadesConsulta= new UnidadesConsulta();
			ClaseInventario claseInventario=new ClaseInventario();
			
			
			if(valor.getTipoOrden()==0){
				parametrica.setCodigo(valor.getCodigo());
				gruposServicios.setCodigo(valor.getCodigoGruposServicio());
				unidadesConsulta.setCodigo(valor.getUnidadAgenda());
				parametrica.setGruposServicios(gruposServicios);
				parametrica.setUnidadesConsulta(unidadesConsulta);
				parametrica.setValorEstOrdCita(valor.getValorEstOrdCita());
				parametrica.setValorEstSermedOrden(valor.getValorEstSermedOrden());
				parametrica.setValorEstSermedCita(valor.getValorEstSermedCita());
				parametrica.setEsServicio(true);
			}
			else{
				parametrica.setCodigo(valor.getCodigo());
				claseInventario.setCodigo(valor.getCodigoClaseInventarios());
				unidadesConsulta.setCodigo(valor.getUnidadAgenda());
				parametrica.setClaseInventario(claseInventario);
				parametrica.setUnidadesConsulta(unidadesConsulta);
				parametrica.setValorEstOrdCita(valor.getValorEstOrdCita());
				parametrica.setValorEstSermedOrden(valor.getValorEstSermedOrden());
				parametrica.setValorEstSermedCita(valor.getValorEstSermedCita());
				parametrica.setEsServicio(false);
			}
			
			persistenciaSvc.merge(parametrica);
			
		}catch (Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}		

	}
	
	/**
	 * Metodo que elimina parametrica 
	 * 
	 * @param valor
	 * @return
	 * @throws BDException
	 * @author ginsotfu
	 */
	
	public void eliminarOrden (ValorEstandarOrdenadoresDto valor )throws BDException{
		
		try{
			
			persistenciaSvc= new PersistenciaSvc();
			HashMap<String, Object>parametros=new HashMap<String, Object>(0);
			parametros.put("codigo",valor.getCodigo());

			persistenciaSvc.createUpdateNamedQuery("catalogoConsultaExterna.eliminarOrden", parametros);	
			
		}catch (Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}	
	}
	
	
	
	/**
	 * Consulta para realizar validacion de parametrica servicios
	 * 
	 * @param codigoParametrica
	 * @param codigoGrupoServicio
	 * @param codigoUnidadAgenda
	 * @return
	 * @throws BDException
	 * @author ginsotfu
	 */
	
	public boolean consultarValidacionServicio (Integer codigoParametrica, int codigoGrupoServicio, int codigoUnidadAgenda )throws BDException{
		boolean hayParametros=false;
		try{
			persistenciaSvc= new PersistenciaSvc();
			if (codigoParametrica==null){
				HashMap<String, Object>parametros=new HashMap<String, Object>(0);
				parametros.put("codigoGS",codigoGrupoServicio);
				parametros.put("codigoUA",codigoUnidadAgenda);
				Long resultado=(Long)persistenciaSvc.createNamedQueryUniqueResult("catalogoConsultaExterna.consultarValidacionServicio", parametros);	
				if(resultado>0){
					hayParametros=true;
				}	
			}
			else {
				HashMap<String, Object>parametros=new HashMap<String, Object>(0);
				parametros.put ("codigoPa", codigoParametrica);
				parametros.put("codigoGS",codigoGrupoServicio);
				parametros.put("codigoUA",codigoUnidadAgenda);
				Long resultado=(Long)persistenciaSvc.createNamedQueryUniqueResult("catalogoConsultaExterna.consultarValidacionServicioModificar", parametros);	
				if(resultado>0){
					hayParametros=true;
				}	
				
			}
		}catch (Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}	
		return hayParametros;
	}
	
	/**
	 * Consulta para realizar validacion de parametrica medicamentos
	 * 
	 * @param codigoParametrica
	 * @param codigoClaseInventario
	 * @param codigoUnidadAgenda
	 * @return
	 * @throws BDException
	 * @author ginsotfu
	 */
	
	public boolean consultarValidacionMedicamento (Integer codigoParametrica, int codigoClaseInventario, int codigoUnidadAgenda )throws BDException{
		boolean hayParametros=false;
		try{
			persistenciaSvc= new PersistenciaSvc();
			if (codigoParametrica==null){
				HashMap<String, Object>parametros=new HashMap<String, Object>(0);
				parametros.put("codigoCI",codigoClaseInventario);
				parametros.put("codigoUA",codigoUnidadAgenda);
				Long resultado=(Long)persistenciaSvc.createNamedQueryUniqueResult("catalogoConsultaExterna.consultarValidacionMedicamento", parametros);	
				if(resultado>0){
					hayParametros=true;
				}
			}
			else {
				HashMap<String, Object>parametros=new HashMap<String, Object>(0);
				parametros.put ("codigoPa", codigoParametrica);
				parametros.put("codigoCI",codigoClaseInventario);
				parametros.put("codigoUA",codigoUnidadAgenda);
				Long resultado=(Long)persistenciaSvc.createNamedQueryUniqueResult("catalogoConsultaExterna.consultarValidacionMedicamentoModificar", parametros);	
				if(resultado>0){
					hayParametros=true;
				}	
			}
			
		}catch (Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}	
		return hayParametros;
	}
} 