package com.servinte.axioma.delegate.historiaClinica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;

import com.servinte.axioma.dto.historiaClinica.CoordenadasCurvaCrecimientoDto;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoParametrizabDto;
import com.servinte.axioma.dto.historiaClinica.DatosAlmacenarCurvaCrecimientoDto;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;
import com.servinte.axioma.dto.historiaClinica.PlantillaComponenteDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.Componentes;
import com.servinte.axioma.orm.CurvaCrecimientoParametrizab;
import com.servinte.axioma.orm.EvolucionCurva;
import com.servinte.axioma.orm.Evoluciones;
import com.servinte.axioma.orm.HistoricoImagenPlantilla;
import com.servinte.axioma.orm.ImagenParametrizada;
import com.servinte.axioma.orm.PlantillaComponente;
import com.servinte.axioma.orm.PuntosImagen;
import com.servinte.axioma.orm.ValoracionCurva;
import com.servinte.axioma.orm.Valoraciones;

/**
 * Clase que permite el acceso a datos de la curva de Curva de Crecimiento
 * 
 * @author hermorhu
 * @created 09-Oct-2012
 */
public class CurvasCrecimientoDelegate {
	
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Metodo encargado de obtener las Curvas seleccionadas en el Componente
	 * @param codigoComponente
	 * @return
	 * @throws BDException
	 * @author hermorhu
	 */
	@SuppressWarnings("unchecked")
	public List<PlantillaComponenteDto> consultarCurvasComponente(int codigoComponente) throws BDException{
		try{
			List<PlantillaComponenteDto> curvasComponente = null;
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("codigoComponente", codigoComponente);
			curvasComponente=(List<PlantillaComponenteDto>)persistenciaSvc.createNamedQuery("curvasCrecimiento.consultarCurvasComponente",parametros);
			return curvasComponente;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo encargado de obtener las curvas que no hallan sido seleccionadas en el componente
	 * @param codigoComponente
	 * @return
	 * @throws BDException
	 * @author hermorhu
	 */
	@SuppressWarnings("unchecked")
	public List<CurvaCrecimientoParametrizabDto> consultarCurvasDisponiblesComponente(List<Integer> idsCurvasSelecciondas) throws BDException{
		try{
			List<CurvaCrecimientoParametrizabDto> curvasDisponibles = null;
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("idsCurvasSeleccionadas", idsCurvasSelecciondas);
			curvasDisponibles = (List<CurvaCrecimientoParametrizabDto>)persistenciaSvc.createNamedQuery("curvasCrecimiento.consultarCurvasDisponiblesComponente",parametros);
			return curvasDisponibles;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo encargado de guardar las curvas asociadas al componente
	 * @param plantillasComponente
	 * @param idComponente
	 * @param fechaActual
	 * @throws BDException
	 * @author hermorhu
	 */
	public void asociarCurvasAComponente(List<PlantillaComponenteDto> plantillasComponente, int codigoComponente, Date fechaActual) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			
			for(PlantillaComponenteDto dtoPlantillaComponente : plantillasComponente){
				
				PlantillaComponente plantillaComponente = new PlantillaComponente();
				
				plantillaComponente.setActivo(dtoPlantillaComponente.getActivo());
				
				Componentes componentes = new Componentes();
				componentes.setCodigo(codigoComponente);
				plantillaComponente.setComponentes(componentes);
				
				CurvaCrecimientoParametrizab curvaCrecimientoParametrizab = new CurvaCrecimientoParametrizab();
				curvaCrecimientoParametrizab.setId(dtoPlantillaComponente.getPlantillaCurvaCrecimiento().getId());
				plantillaComponente.setCurvaCrecimientoParametrizab(curvaCrecimientoParametrizab);
				
				if(dtoPlantillaComponente.getId() == null){
					plantillaComponente.setFechaCreacion(fechaActual);
					persistenciaSvc.persist(plantillaComponente);
				}else{
					plantillaComponente.setId(dtoPlantillaComponente.getId());
					plantillaComponente.setFechaCreacion(dtoPlantillaComponente.getFechaCreacion());
					persistenciaSvc.merge(plantillaComponente);
				}
			}
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Metodo encargado de eliminar las curvas asociadas al componente
	 * @param plantillasComponente
	 * @param codigoComponente
	 * @author hermorhu
	 */
	public void eliminarCurvasComponente(List<PlantillaComponenteDto> plantillasComponente, int codigoComponente) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			
			List<PlantillaComponenteDto> curvasComponente = consultarCurvasComponente(codigoComponente);
			Map<String, Object> parametros = new HashMap<String, Object>();
			
			for(PlantillaComponenteDto plantillaComponenteEliminar : plantillasComponente ){
				for(PlantillaComponenteDto plantillaComponente : curvasComponente ){
					if(plantillaComponenteEliminar.getId().intValue() == plantillaComponente.getId().intValue()){
						parametros.put("idPlantillaComponente", plantillaComponenteEliminar.getId());
						persistenciaSvc.createUpdateNamedQuery("curvasCrecimiento.eliminarPlantillaComponente", parametros);
					}
				}
			}

		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		
	}
	
	/**
	 * Metodo encargado de obtener las curvas para un paciente determinado
	 * @param codigoSexo
	 * @param edadPaciente
	 * @return
	 * @throws BDException
	 * @author hermorhu
	 */
	@SuppressWarnings("unchecked")
	public List<CurvaCrecimientoParametrizabDto> consultarCurvasActivasPaciente(int codigoSexo, int edadPaciente) throws BDException{
		try{
			List<CurvaCrecimientoParametrizabDto> curvasActivasPaciente = null;
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("codigoSexo", codigoSexo);
			parametros.put("edadPaciente", edadPaciente);
			curvasActivasPaciente = (List<CurvaCrecimientoParametrizabDto>)persistenciaSvc.createNamedQuery("curvasCrecimiento.consultarCurvasActivasPaciente",parametros);
			return curvasActivasPaciente;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo encargado de obtener la informacion  de la ultima imagen  del historico del paciente
	 * @param codigoPaciente
	 * @param codigoCurvaParametrizada
	 * @return
	 * @throws BDException
	 * @author hermorhu
	 */
	public Object[] consultarUltimaImagenCurvaModificadaXPaciente(int codigoPaciente, int codigoCurvaParametrizada) throws BDException{
		try{
			Object[] historicoImagen = null;
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("codigoPaciente", codigoPaciente);
			parametros.put("codigoCurvaParametrizada", codigoCurvaParametrizada);
			historicoImagen = (Object[]) persistenciaSvc.createNamedQueryFirstResult("curvasCrecimiento.consultarInfoCurvasPaciente",parametros);

			return historicoImagen;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo encargado de obtener las coordenadas de los puntos de una curva de crecimiento
	 * @param codigoCurvaPaciente
	 * @param codigoImagenParametrizada
	 * @return
	 * @throws BDException
	 * @author hermorhu
	 */
	@SuppressWarnings("unchecked")
	public List<CoordenadasCurvaCrecimientoDto> consultarCoordenadasCurvaCrecimiento(int codigoPaciente, int codigoImagenParametrizada) throws BDException{
		try{
			List<CoordenadasCurvaCrecimientoDto> coordenadasPuntos = null;
			List<Object[]> coordenadasObject = null;
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("codigoPaciente", codigoPaciente);
			parametros.put("codigoImagenParametrizada", codigoImagenParametrizada);
			coordenadasObject = (List<Object[]>)persistenciaSvc.createNamedQuery("curvasCrecimiento.consultarCoordenadasCurvaCrecimiento",parametros);
			
			CoordenadasCurvaCrecimientoDto dtoCoordenadas = null;
			
			if(coordenadasObject != null){
				coordenadasPuntos = new ArrayList<CoordenadasCurvaCrecimientoDto>();

				for(Object[] coordenadas : coordenadasObject){
					dtoCoordenadas  = new CoordenadasCurvaCrecimientoDto();
					dtoCoordenadas.setId((Integer)coordenadas[0]);
					dtoCoordenadas.setCoordenadaX((Integer)coordenadas[1]);
					dtoCoordenadas.setCoordenadaY((Integer)coordenadas[2]);
					coordenadasPuntos.add(dtoCoordenadas);
				}
			}
			
			return coordenadasPuntos;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	} 
 
	/**
	 * Metodo encarago de guardar la curva de crecimiento desde una valoracion
	 * @param curvaCrecimiento
	 * @return
	 * @throws BDException
	 * @author hermorhu
	 */
	public void guardarCurvaCrecimientoValoracion(DatosAlmacenarCurvaCrecimientoDto curvaCrecimiento) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
		
			ValoracionCurva valoracionCurva = new ValoracionCurva();
		
			CurvaCrecimientoParametrizab curvaCrecimientoParametrizab = new CurvaCrecimientoParametrizab();
			curvaCrecimientoParametrizab.setId(curvaCrecimiento.getCodigoCurvaParametrizada());
			valoracionCurva.setCurvaCrecimientoParametrizab(curvaCrecimientoParametrizab);
		
			Valoraciones valoracion = new Valoraciones();
			valoracion.setNumeroSolicitud(curvaCrecimiento.getNumeroSolicitud());
			valoracionCurva.setValoraciones(valoracion);
		
			valoracionCurva.setFechaCreacion(curvaCrecimiento.getFechaCreacion());
			
			valoracionCurva = persistenciaSvc.merge(valoracionCurva);
			
			this.guardarHistoricoCurva(valoracionCurva, curvaCrecimiento);
		
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}

	/**
	 * Metodo encarago de guardar la curva de crecimiento desde una evolucion
	 * @param curvaCrecimiento
	 * @throws BDException
	 * @author hermorhu
	 */
	public void guardarCurvaCrecimientoEvolucion(DatosAlmacenarCurvaCrecimientoDto curvaCrecimiento) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			
			EvolucionCurva evolucionCurva = new EvolucionCurva();
		
			CurvaCrecimientoParametrizab curvaCrecimientoParametrizab = new CurvaCrecimientoParametrizab();
			curvaCrecimientoParametrizab.setId(curvaCrecimiento.getCodigoCurvaParametrizada());
			evolucionCurva.setCurvaCrecimientoParametrizab(curvaCrecimientoParametrizab);
		
			Evoluciones evolucion = this.consultarEvolucionXSolicitud(curvaCrecimiento.getNumeroSolicitud());
			evolucionCurva.setEvoluciones(evolucion);
		
			evolucionCurva.setFechaCreacion(curvaCrecimiento.getFechaCreacion());
			
			evolucionCurva = persistenciaSvc.merge(evolucionCurva);
			
			this.guardarHistoricoCurva(evolucionCurva, curvaCrecimiento);
		
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * metodo encargado de guardar el historico de la imagen modificada
	 * @param valoracionEvolucionCurva
	 * @param curvaCrecimiento
	 * @throws BDException
	 * @author hermorhu
	 */
	public void guardarHistoricoCurva(Object valoracionEvolucionCurva, DatosAlmacenarCurvaCrecimientoDto curvaCrecimiento) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			
			HistoricoImagenPlantilla historicoCurva = new HistoricoImagenPlantilla();
		
			if(valoracionEvolucionCurva instanceof ValoracionCurva){
				ValoracionCurva valoracionCurva = (ValoracionCurva) valoracionEvolucionCurva;
				historicoCurva.setValoracionCurva(valoracionCurva);
			
			} else if(valoracionEvolucionCurva instanceof EvolucionCurva){
				EvolucionCurva evolucionCurva = (EvolucionCurva) valoracionEvolucionCurva;
				historicoCurva.setEvolucionCurva(evolucionCurva);
			}
			
			ImagenParametrizada imagenParametrizada = new ImagenParametrizada();
			imagenParametrizada.setId(curvaCrecimiento.getCodigoImagenParametrizada());
			historicoCurva.setImagenParametrizada(imagenParametrizada);
			
			historicoCurva.setFechaCreacion(curvaCrecimiento.getFechaCreacion());
			historicoCurva.setUrlImagen(curvaCrecimiento.getRutaImagen());
			
			historicoCurva = persistenciaSvc.merge(historicoCurva);
			
			this.guardarCoordenadasPuntos(historicoCurva, curvaCrecimiento);
		
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		
	}
	
	/**
	 * Metodo encargado de guardar las coordenadas de los puntos
	 * @param historicoCurva
	 * @param curvaCrecimiento
	 * @throws BDException
	 * @author hermorhu
	 */
	public void guardarCoordenadasPuntos(HistoricoImagenPlantilla historicoCurva, DatosAlmacenarCurvaCrecimientoDto curvaCrecimiento) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			
			PuntosImagen coordenadas = null;
	
			for(CoordenadasCurvaCrecimientoDto coordenadasCurvaCrecimiento : curvaCrecimiento.getCoordenadasPuntos()){
				
				if(coordenadasCurvaCrecimiento.getId() == null){
					coordenadas = new PuntosImagen();
				
					coordenadas.setHistoricoImagenPlantilla(historicoCurva);
					coordenadas.setFechaCreacion(curvaCrecimiento.getFechaCreacion());
				
					coordenadas.setCoordenadaX(coordenadasCurvaCrecimiento.getCoordenadaX());
					coordenadas.setCoordenadaY(coordenadasCurvaCrecimiento.getCoordenadaY());
				
					persistenciaSvc.merge(coordenadas);
				}
			}
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Metodo encargado de obtener una Evolucion
	 * @param numeroSolicitud
	 * @return
	 * @throws BDException
	 * @author hermorhu
	 */
	public Evoluciones consultarEvolucionXSolicitud(int numeroSolicitud) throws BDException{
		try{
			Evoluciones evolucion = null;
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("numeroSolicitud", numeroSolicitud);
			evolucion = (Evoluciones) persistenciaSvc.createNamedQueryFirstResult("curvasCrecimiento.consultarEvolucionXSolicitud",parametros);
			return evolucion;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo encargado de obtener datos necesarios del paciente para el formato de impresion de Curvas de Crecimiento
	 * @param codigoIngreso
	 * @return
	 * @throws BDException
	 * @author hermorhu
	 */
	public Object[] consultarDatosPacienteFormatoImpresion (int codigoIngreso) throws BDException{
		try{
			Object[] datosPacienteObject = null;
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("codigoIngreso", codigoIngreso);
			datosPacienteObject = (Object[]) persistenciaSvc.createNamedQueryFirstResult("curvasCrecimiento.consultarDatosPacienteCurvasCrecimiento",parametros);
			return datosPacienteObject;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo encargado de obtener datos necesarios del acompanante del paciente para el formato de impresion de Curvas de Crecimiento
	 * @param codigoIngreso
	 * @return
	 * @throws BDException
	 * @author hermorhu
	 */
	public Object[] consultarDatosAcompananteFormatoImpresion (int codigoIngreso) throws BDException{
		try{
			Object[] datosAcompananteObject = null;
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("codigoIngreso", codigoIngreso);
			datosAcompananteObject = (Object[]) persistenciaSvc.createNamedQueryFirstResult("curvasCrecimiento.consultarDatosAcompanante",parametros);
			return datosAcompananteObject;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * eliminarCurvaParametrizada
	 * @param idCurva
	 * @throws BDException
	 */
	public void eliminarCurvaParametrizada(int idCurva) throws BDException {
		try 
		{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("idCurva", idCurva);
			List<?> idCurvas = persistenciaSvc.createNamedQuery("curvasCrecimiento.validacionEliminarCurva",parametros);
			if(idCurvas.isEmpty())
			{
				persistenciaSvc.createUpdateNamedQuery("curvasCrecimiento.EliminarImagenCurva",parametros);
				persistenciaSvc.createUpdateNamedQuery("curvasCrecimiento.EliminarCurva",parametros);
			}
			else
				throw new IPSException(new Exception("No se puede elminar la curva seleccionada porque esta siendo usada por algun paciente"));
		} 
		catch (Exception e) 
		{
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * buscarCurvaCriterios
	 * @param titulo
	 * @param descripcion
	 * @param edadInicial
	 * @param edadFinal
	 * @param codigoSexo
	 * @param activo
	 * @param inidicadorError
	 * @return
	 * @throws BDException
	 */
	public List<CurvaCrecimientoParametrizabDto> buscarCurvaCriterios(String titulo, String descripcion, Integer edadInicial, Integer edadFinal, Integer codigoSexo, Boolean activo, Boolean inidicadorError) throws BDException{
		List<CurvaCrecimientoParametrizabDto> curvas;
		persistenciaSvc = new PersistenciaSvc();
		try
		{
			Criteria criteria=persistenciaSvc.getSession().createCriteria(CurvaCrecimientoParametrizab.class.getName(),"curvaCrecimiento");
			
			criteria.setProjection(Projections.projectionList()	.add(Projections.property("curvaCrecimiento.id"),"id")
																.add(Projections.property("curvaCrecimiento.sexo.codigo"),"codigoSexo")
																.add(Projections.property("curvaCrecimiento.tituloGrafica"),"tituloGrafica")
																.add(Projections.property("curvaCrecimiento.colorTitulo"),"colorTitulo")
																.add(Projections.property("curvaCrecimiento.descripcion"),"descripcion")
																.add(Projections.property("curvaCrecimiento.colorDescripcion"),"colorDescripcion")
																.add(Projections.property("curvaCrecimiento.edadInicial"),"edadInicial")
																.add(Projections.property("curvaCrecimiento.edadFinal"),"edadFinal")
																.add(Projections.property("curvaCrecimiento.activo"),"activo")
																.add(Projections.property("curvaCrecimiento.fechaCreacion"),"fechaCreacion")
																.add(Projections.property("curvaCrecimiento.imagenParametrizadas"),"dtoImagenesParametrizadas")
																);

			
			if(titulo != null && !titulo.equals(""))
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("curvaCrecimiento.tituloGrafica", titulo)));
			if(descripcion != null && !descripcion.equals(""))
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("curvaCrecimiento.descripcion", descripcion)));
			if(edadInicial != null)
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("curvaCrecimiento.edadInicial", edadInicial)));
			if(edadFinal != null)
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("curvaCrecimiento.edadFinal", edadFinal)));
			if(codigoSexo != null)
				if(!codigoSexo.equals(ConstantesBD.codigoNuncaValido))
					criteria.add(Restrictions.disjunction().add(Restrictions.eq("curvaCrecimiento.sexo.codigo", codigoSexo)));
				else
					criteria.add(Restrictions.disjunction().add(Restrictions.isNull("curvaCrecimiento.sexo.codigo")));
			if(activo != null)
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("curvaCrecimiento.activo", activo)));
			if(inidicadorError != null)
				criteria.add(Restrictions.disjunction().add(Restrictions.eq("curvaCrecimiento.indicadorError", inidicadorError)));
			
			criteria.setResultTransformer(Transformers.aliasToBean(CurvaCrecimientoParametrizabDto.class));
			curvas = criteria.list();
		} 
		catch (Exception e) 
		{
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return curvas;
	}
	
	/**
	 * valoracionesYevolucionesPorpacienteConCurva
	 * @param codigoPaciente
	 * @return
	 * @throws BDException
	 */
	public List<HistoricoImagenPlantillaDto> valoracionesYevolucionesPorpacienteConCurva(int codigoPaciente) throws BDException{
		List<HistoricoImagenPlantillaDto> lista = new ArrayList<HistoricoImagenPlantillaDto>();
		int posicionCodigoValoracion = 9;
		int posicionValorSignoVital = 1;
		int posicionTipo = 11;
		int posicionUnidadSignos = 2;
		try 
		{
			persistenciaSvc = new PersistenciaSvc();
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("codigoPaciente", codigoPaciente);
			
			List<Object[]> valoracionesYevoluciones = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.valoracionesYevolucionesPorpacienteConCurva",parametros);
			for (int i = 0; i < valoracionesYevoluciones.size(); i++) {
				
				Object[] valoracionYevolucion = valoracionesYevoluciones.get(i);
				
				Map<String, Object> parametrosValEvo = new HashMap<String, Object>();
				List<Object[]> signosVitalesValEvo = null;
				
				if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
				{
					if(valoracionesYevoluciones.get(i)[posicionTipo].equals(HistoricoImagenPlantillaDto.VALORACION)){
						parametrosValEvo.put("codigoValoracion", valoracionesYevoluciones.get(i)[posicionCodigoValoracion]);
						signosVitalesValEvo = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.singnosVitalesPorValoracion",parametrosValEvo);
					}
											
					if(valoracionesYevoluciones.get(i)[posicionTipo].equals(HistoricoImagenPlantillaDto.EVOLUCION)){
						parametrosValEvo.put("codigoEvolucion", valoracionesYevoluciones.get(i)[posicionCodigoValoracion]);
						signosVitalesValEvo = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.singnosVitalesPorEvolucion",parametrosValEvo);
					}
				}
				else
				{
					if(((BigDecimal)valoracionesYevoluciones.get(i)[posicionTipo]).intValue() == HistoricoImagenPlantillaDto.VALORACION){
						parametrosValEvo.put("codigoValoracion", valoracionesYevoluciones.get(i)[posicionCodigoValoracion]);
						signosVitalesValEvo = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.singnosVitalesPorValoracion",parametrosValEvo);
					}
											
					if(((BigDecimal)valoracionesYevoluciones.get(i)[posicionTipo]).intValue() == HistoricoImagenPlantillaDto.EVOLUCION){
						parametrosValEvo.put("codigoEvolucion", valoracionesYevoluciones.get(i)[posicionCodigoValoracion]);
						signosVitalesValEvo = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.singnosVitalesPorEvolucion",parametrosValEvo);
					}
				}
								
				 
				Object[] valoresSignos = new Object[signosVitalesValEvo.size() + signosVitalesValEvo.size()]; 
				for (int j = 0; j < signosVitalesValEvo.size(); j++) {
					if(signosVitalesValEvo.get(j)[posicionValorSignoVital]!=null && !signosVitalesValEvo.get(j)[posicionValorSignoVital].equals(""))
					{
						valoresSignos[j] =  signosVitalesValEvo.get(j)[posicionValorSignoVital] + " " + signosVitalesValEvo.get(j)[posicionUnidadSignos];
						valoresSignos[signosVitalesValEvo.size() + j] = signosVitalesValEvo.get(j)[posicionUnidadSignos];
					}
				}
				
				Object[] valoracionesEvolucionesYsignosVitales = new Object[valoracionYevolucion.length + valoresSignos.length];
				System.arraycopy(valoracionYevolucion, 0, valoracionesEvolucionesYsignosVitales, 0, valoracionYevolucion.length);
				System.arraycopy(valoresSignos, 0, valoracionesEvolucionesYsignosVitales, valoracionYevolucion.length, valoresSignos.length);
					
				lista.add(new HistoricoImagenPlantillaDto(valoracionesEvolucionesYsignosVitales));		
			}
		} 
		catch (Exception e) 
		{
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return lista;
	}	

	/**
	 * valoracionesYevolucionesPorpacienteYporIngresoConCurva
	 * @param codigoPaciente
	 * @param idCuenta
	 * @return
	 * @throws BDException
	 */
	public List<HistoricoImagenPlantillaDto> valoracionesYevolucionesPorpacienteYporIngresoConCurva(int codigoPaciente, int idCuenta) throws BDException {
		List<HistoricoImagenPlantillaDto> lista = new ArrayList<HistoricoImagenPlantillaDto>();
		int posicionCodigoValoracion = 9;
		int posicionValorSignoVital = 1;
		int posicionTipo = 11;
		int posicionUnidadSignos = 2;
		try 
		{
			persistenciaSvc = new PersistenciaSvc();
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("codigoPaciente", codigoPaciente);
			parametros.put("idCuenta", idCuenta);
			
			if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
				parametros.put("estado", true);
			else
				parametros.put("estado", 1);
			
			
			List<Object[]> valoracionesYevoluciones = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.valoracionesYevolucionesPorpacienteYporIngresoConCurva",parametros);
			for (int i = 0; i < valoracionesYevoluciones.size(); i++) {
				
				Object[] valoracionYevolucion = valoracionesYevoluciones.get(i);
				
				Map<String, Object> parametrosValEvo = new HashMap<String, Object>();
				List<Object[]> signosVitalesValEvo = null;
				
				if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
				{
					if(valoracionesYevoluciones.get(i)[posicionTipo].equals(HistoricoImagenPlantillaDto.VALORACION)){
						parametrosValEvo.put("codigoValoracion", valoracionesYevoluciones.get(i)[posicionCodigoValoracion]);
						signosVitalesValEvo = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.singnosVitalesPorValoracion",parametrosValEvo);
					}
											
					if(valoracionesYevoluciones.get(i)[posicionTipo].equals(HistoricoImagenPlantillaDto.EVOLUCION)){
						parametrosValEvo.put("codigoEvolucion", valoracionesYevoluciones.get(i)[posicionCodigoValoracion]);
						signosVitalesValEvo = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.singnosVitalesPorEvolucion",parametrosValEvo);
					}
				}
				else
				{
					if(((BigDecimal)valoracionesYevoluciones.get(i)[posicionTipo]).intValue() == HistoricoImagenPlantillaDto.VALORACION){
						parametrosValEvo.put("codigoValoracion", valoracionesYevoluciones.get(i)[posicionCodigoValoracion]);
						signosVitalesValEvo = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.singnosVitalesPorValoracion",parametrosValEvo);
					}
											
					if(((BigDecimal)valoracionesYevoluciones.get(i)[posicionTipo]).intValue() == HistoricoImagenPlantillaDto.EVOLUCION){
						parametrosValEvo.put("codigoEvolucion", valoracionesYevoluciones.get(i)[posicionCodigoValoracion]);
						signosVitalesValEvo = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.singnosVitalesPorEvolucion",parametrosValEvo);
					}
				}
				
								
				 
				Object[] valoresSignos = new Object[signosVitalesValEvo.size() + signosVitalesValEvo.size()]; 
				for (int j = 0; j < signosVitalesValEvo.size(); j++) {
					if(signosVitalesValEvo.get(j)[posicionValorSignoVital]!=null && !signosVitalesValEvo.get(j)[posicionValorSignoVital].equals(""))
					{
						valoresSignos[j] =  signosVitalesValEvo.get(j)[posicionValorSignoVital] + " " + signosVitalesValEvo.get(j)[posicionUnidadSignos];
						valoresSignos[signosVitalesValEvo.size() + j] = signosVitalesValEvo.get(j)[posicionUnidadSignos];
					}
				}
				
				Object[] valoracionesEvolucionesYsignosVitales = new Object[valoracionYevolucion.length + valoresSignos.length];
				System.arraycopy(valoracionYevolucion, 0, valoracionesEvolucionesYsignosVitales, 0, valoracionYevolucion.length);
				System.arraycopy(valoresSignos, 0, valoracionesEvolucionesYsignosVitales, valoracionYevolucion.length, valoresSignos.length);
					
				lista.add(new HistoricoImagenPlantillaDto(valoracionesEvolucionesYsignosVitales));		
			}
		} 
		catch (Exception e) 
		{
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return lista;
	}

	/**
	 * valoracionesPorId
	 * @param idValoracion
	 * @return
	 * @throws BDException
	 */
	public List<HistoricoImagenPlantillaDto> valoracionesPorId(int idValoracion) throws BDException {
		List<HistoricoImagenPlantillaDto> lista = new ArrayList<HistoricoImagenPlantillaDto>();
		int posicionCodigoValoracion = 9;
		int posicionValorSignoVital = 1;
		int posicionTipo = 11;
		int posicionUnidadSignos = 2;
		try
		{
			persistenciaSvc = new PersistenciaSvc();
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("idValoracion", idValoracion);
			if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
				parametros.put("estado", true);
			else
				parametros.put("estado", 1);
			
			List<Object[]> valoraciones = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.valoracionesPorpacienteConCurva",parametros);
			for (int i = 0; i < valoraciones.size(); i++) {
				
				Object[] valoracion = valoraciones.get(i);
				
				Map<String, Object> parametrosValoracion = new HashMap<String, Object>();
				List<Object[]> signosVitalesValoracion = null;
				
				parametrosValoracion.put("codigoValoracion", valoraciones.get(i)[posicionCodigoValoracion]);
				signosVitalesValoracion = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.singnosVitalesPorValoracion",parametrosValoracion);
				
				Object[] valoresSignos = new Object[signosVitalesValoracion.size() + signosVitalesValoracion.size()]; 
				for (int j = 0; j < signosVitalesValoracion.size(); j++) {
					if(signosVitalesValoracion.get(j)[posicionValorSignoVital]!=null && !signosVitalesValoracion.get(j)[posicionValorSignoVital].equals(""))
					{
						valoresSignos[j] =  signosVitalesValoracion.get(j)[posicionValorSignoVital] + " " + signosVitalesValoracion.get(j)[posicionUnidadSignos];
						valoresSignos[signosVitalesValoracion.size() + j] = signosVitalesValoracion.get(j)[posicionUnidadSignos];
					}
				}
				
				Object[] valoracionYsignos = new Object[valoracion.length	+ valoresSignos.length];
				System.arraycopy(valoracion, 0, valoracionYsignos, 0, valoracion.length);
				System.arraycopy(valoresSignos, 0, valoracionYsignos,	valoracion.length, valoresSignos.length);
				
				lista.add(new HistoricoImagenPlantillaDto(valoracionYsignos));
			}
		}
		catch (Exception e) 
		{
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return lista;
	}

	/**
	 * evolucionesPorId
	 * @param idEvolucion
	 * @return
	 * @throws BDException
	 */
	public List<HistoricoImagenPlantillaDto> evolucionesPorId(int idEvolucion) throws BDException {
		List<HistoricoImagenPlantillaDto> lista = new ArrayList<HistoricoImagenPlantillaDto>();
		int posicionCodigoValoracion = 9;
		int posicionValorSignoVital = 1;
		int posicionTipo = 11;
		int posicionUnidadSignos = 2;
		try
		{
			persistenciaSvc = new PersistenciaSvc();
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("idEvolucion", idEvolucion);
			if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
				parametros.put("estado", true);
			else
				parametros.put("estado", 1);
			
			List<Object[]> valoraciones = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.evolucionesPorpacienteConCurva",parametros);
			for (int i = 0; i < valoraciones.size(); i++) {
				
				Object[] valoracion = valoraciones.get(i);
				
				Map<String, Object> parametrosValoracion = new HashMap<String, Object>();
				List<Object[]> signosVitalesValoracion = null;
				
				parametrosValoracion.put("codigoEvolucion", valoraciones.get(i)[posicionCodigoValoracion]);
				signosVitalesValoracion = (List<Object[]>) persistenciaSvc.createNamedQuery("curvasCrecimiento.singnosVitalesPorEvolucion",parametrosValoracion);
				
				Object[] valoresSignos = new Object[signosVitalesValoracion.size() + signosVitalesValoracion.size()]; 
				for (int j = 0; j < signosVitalesValoracion.size(); j++) {
					if(signosVitalesValoracion.get(j)[posicionValorSignoVital]!=null && !signosVitalesValoracion.get(j)[posicionValorSignoVital].equals(""))
					{
						valoresSignos[j] =  signosVitalesValoracion.get(j)[posicionValorSignoVital] + " " + signosVitalesValoracion.get(j)[posicionUnidadSignos];
						valoresSignos[signosVitalesValoracion.size() + j] = signosVitalesValoracion.get(j)[posicionUnidadSignos];
					}
				}
				
				Object[] valoracionYsignos = new Object[valoracion.length	+ valoresSignos.length];
				System.arraycopy(valoracion, 0, valoracionYsignos, 0, valoracion.length);
				System.arraycopy(valoresSignos, 0, valoracionYsignos,	valoracion.length, valoresSignos.length);
				
				lista.add(new HistoricoImagenPlantillaDto(valoracionYsignos));
			}
		}
		catch (Exception e) 
		{
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return lista;
	}
	
	
	/**
	 * @param codigoPaciente
	 * @param codigoInstitucion
	 * @param fechaCorteCurvas
	 * @return
	 * @throws BDException
	 */
	public boolean existeCurvasAnteriores(int codigoPaciente, int codigoInstitucion, Date fechaCorteCurvas) throws BDException {
		
		boolean existeCurvas = false;
		
		try 
		{
			persistenciaSvc= new PersistenciaSvc();
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("codigoPaciente", codigoPaciente);
			parametros.put("codigoInstitucion", codigoInstitucion);
			parametros.put("fechaCorteCurvas", fechaCorteCurvas);
			
			Integer resultado = (Integer)persistenciaSvc.createNamedQueryFirstResult("curvasCrecimiento.existeCurvasAnteriores",parametros);
			
			if(resultado != null){
				existeCurvas = true;
			}
			
		}	
		catch (Exception e) 
		{
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		
		return existeCurvas;
		
	}
}