package com.servinte.axioma.delegate.historiaClinica;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoParametrizabDto;
import com.servinte.axioma.dto.historiaClinica.ImagenParametrizadaDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.CurvaCrecimientoParametrizab;
import com.servinte.axioma.orm.ImagenParametrizada;
import com.servinte.axioma.orm.Sexo;

/**
 * Clase que permite el acceso a datos genericos de la Historia Clinica
 * 
 * @author hermorhu
 * @created 09-Oct-2012
 */
public class CatalogoHistoriaClinicaDelegate {
	
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Metodo encargado de obtener todas las Curvas Parametrizadas
	 * @return
	 * @throws BDException
	 * @author hermorhu
	 */
	@SuppressWarnings("unchecked")
	public List<CurvaCrecimientoParametrizabDto> consultarCurvasParametrizadas() throws BDException{
		try{
			List<CurvaCrecimientoParametrizabDto> curvasParametrizadas= null;
			persistenciaSvc= new PersistenciaSvc();
			curvasParametrizadas=(List<CurvaCrecimientoParametrizabDto>)persistenciaSvc.createNamedQuery("catalogoHistoriaClinica.consultarCurvasParametrizadas");
			return curvasParametrizadas;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * consultarCurvaParametrizada
	 * @param idCurva
	 * @return
	 * @throws IPSException
	 */
	public CurvaCrecimientoParametrizabDto consultarCurvaParametrizada(int idCurva) throws IPSException{
		persistenciaSvc= new PersistenciaSvc();
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idCurva", idCurva);
		CurvaCrecimientoParametrizabDto curva = (CurvaCrecimientoParametrizabDto) persistenciaSvc.createNamedQueryUniqueResult("catalogoHistoriaClinica.consultarCurvaParametrizada",parametros);
		return curva;
	}
	
	/**
	 * consultarImagenesParametrizadas
	 * @param idCurva
	 * @return
	 * @throws BDException
	 */
	public List<ImagenParametrizadaDto> consultarImagenesParametrizadas(int idCurva) throws BDException{
		try 
		{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("idCurva", idCurva);
			List<ImagenParametrizadaDto> lista = (List<ImagenParametrizadaDto>) persistenciaSvc.createNamedQuery("catalogoHistoriaClinica.consultarImagenesParametrizadas",parametros);
			return lista;
		} 
		catch (Exception e) 
		{
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * guardarCurvaParametrizada
	 * @param dtoccp
	 * @throws BDException
	 */
	public void guardarCurvaParametrizada(CurvaCrecimientoParametrizabDto dtoccp) throws BDException {
		try 
		{
			CurvaCrecimientoParametrizab curva = new CurvaCrecimientoParametrizab();
			if(dtoccp.getCodigoSexo()!=null)
				curva.setSexo(new Sexo(dtoccp.getCodigoSexo(), ""));
			curva.setTituloGrafica(dtoccp.getTituloGrafica());
			curva.setColorTitulo(dtoccp.getColorTitulo());
			curva.setDescripcion(dtoccp.getDescripcion());
			curva.setColorDescripcion(dtoccp.getColorDescripcion());
			curva.setEdadInicial(dtoccp.getEdadInicial());
			curva.setEdadFinal(dtoccp.getEdadFinal());
			curva.setActivo(dtoccp.getActivo());
			curva.setIndicadorError(dtoccp.getIndicadorError());
			curva.setFechaCreacion(dtoccp.getFechaCreacion());
			if(dtoccp.getId()!=null)
				curva.setId(dtoccp.getId());
			
			ImagenParametrizada imagen = new ImagenParametrizada();
			imagen.setImagenIzquierda(dtoccp.getDtoImagenesParametrizadas().getImagenIzquierda());
			imagen.setImagenDerecha(dtoccp.getDtoImagenesParametrizadas().getImagenDerecha());
			imagen.setImagenCurva(dtoccp.getDtoImagenesParametrizadas().getImagenCurva());
			imagen.setActivo(true);
			imagen.setFechaCreacion(dtoccp.getFechaCreacion());
			if(dtoccp.getDtoImagenesParametrizadas().getId() != null)
				imagen.setId(dtoccp.getDtoImagenesParametrizadas().getId());
			
			ImagenParametrizada imagenAntigua = null;
			if(dtoccp.getDtoImagenParametrizadaAntigua()!=null){
				imagenAntigua = new ImagenParametrizada();
				imagenAntigua.setImagenIzquierda(dtoccp.getDtoImagenParametrizadaAntigua().getImagenIzquierda());
				imagenAntigua.setImagenDerecha(dtoccp.getDtoImagenParametrizadaAntigua().getImagenDerecha());
				imagenAntigua.setImagenCurva(dtoccp.getDtoImagenParametrizadaAntigua().getImagenCurva());
				imagenAntigua.setActivo(false);
				imagenAntigua.setFechaCreacion(dtoccp.getDtoImagenParametrizadaAntigua().getFechaCreacion());
			}
				
			persistenciaSvc = new PersistenciaSvc();
			curva=persistenciaSvc.merge(curva);
			imagen.setCurvaCrecimientoParametrizab(curva);
			persistenciaSvc.merge(imagen);
			if(imagenAntigua!=null){
				imagenAntigua.setCurvaCrecimientoParametrizab(curva);
				persistenciaSvc.persist(imagenAntigua);
			}
		} 
		catch (Exception e) 
		{
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
}