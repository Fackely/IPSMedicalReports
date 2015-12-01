package com.servinte.axioma.bl.historiaClinica.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.PathSolver;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.servinte.axioma.bl.historiaClinica.interfaz.ICurvasCrecimientoMundo;
import com.servinte.axioma.delegate.historiaClinica.CatalogoHistoriaClinicaDelegate;
import com.servinte.axioma.delegate.historiaClinica.CurvasCrecimientoDelegate;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoPacienteDto;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoParametrizabDto;
import com.servinte.axioma.dto.historiaClinica.DatosAlmacenarCurvaCrecimientoDto;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;
import com.servinte.axioma.dto.historiaClinica.ImagenParametrizadaDto;
import com.servinte.axioma.dto.historiaClinica.PlantillaComponenteDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.generadorReporte.historiaClinica.curvasCrecimiento.CurvaCrecimientoDesarrolloDto;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Clase que implementa los servicios de Negocio correspondientes a la lógica asociada a las
 * Curvas de Crecimiento
 * 
 * @author hermorhu
 * @created 09-Oct-2012 
 */
public class CurvasCrecimientoMundo implements ICurvasCrecimientoMundo {
	
	private static final String DIRECTORIO_IMAGENES = PathSolver.getWEBINFPath() +"/"+ System.getProperty("directorioImagenes");
	private static final String SUBDIRECTORIO_CURVAS = "curvasCrecimiento/historicoCurvasPacientes/";
	private static final String FORMATO_IMAGEN = "png";
	
	/**
	 * @see com.servinte.axioma.bl.historiaClinica.interfaz.ICurvasCrecimientoMundo#consultarCurvasParametrizadas()
	 */
	@Override
	public List<CurvaCrecimientoParametrizabDto> consultarCurvasParametrizadas() throws IPSException {
		List<CurvaCrecimientoParametrizabDto> curvasParametrizadas = null;
		try{
			HibernateUtil.beginTransaction();
			CatalogoHistoriaClinicaDelegate delegate = new CatalogoHistoriaClinicaDelegate();
			curvasParametrizadas = delegate.consultarCurvasParametrizadas();
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return curvasParametrizadas;
	}

	/**
	 * @see com.servinte.axioma.bl.historiaClinica.interfaz.ICurvasCrecimientoMundo#consultarCurvasComponente(int)
	 */
	@Override
	public List<PlantillaComponenteDto> consultarCurvasComponente(int codigoComponente) throws IPSException {
		List<PlantillaComponenteDto> curvasComponente = null;
		try{
			HibernateUtil.beginTransaction();
			CurvasCrecimientoDelegate delegate = new CurvasCrecimientoDelegate();
			curvasComponente = delegate.consultarCurvasComponente(codigoComponente);
			HibernateUtil.endTransaction();
		}catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return curvasComponente;
	}

	/** 
	 * @see com.servinte.axioma.bl.historiaClinica.interfaz.ICurvasCrecimientoMundo#consultarCurvasDisponiblesComponente(int)
	 */
	@Override
	public List<CurvaCrecimientoParametrizabDto> consultarCurvasDisponiblesComponente(int codigoComponente) throws IPSException {
		List<CurvaCrecimientoParametrizabDto> curvasDisponibles = null;
		List<Integer> idsCurvasSeleccionadas = null;
		
		try{
			CurvasCrecimientoDelegate delegate = new CurvasCrecimientoDelegate();
			
			idsCurvasSeleccionadas= new ArrayList<Integer>();
			
			List<PlantillaComponenteDto>  curvasSeleccionadas= this.consultarCurvasComponente(codigoComponente);
			
			if(!curvasSeleccionadas.isEmpty()){
				for(PlantillaComponenteDto plantillaComponente : curvasSeleccionadas){
					idsCurvasSeleccionadas.add(plantillaComponente.getPlantillaCurvaCrecimiento().getId());
				}
			}else{
				idsCurvasSeleccionadas.add(ConstantesBD.codigoNuncaValido);
			}
			
			HibernateUtil.beginTransaction();
			curvasDisponibles = delegate.consultarCurvasDisponiblesComponente(idsCurvasSeleccionadas);
			HibernateUtil.endTransaction();
			
		}catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
		return curvasDisponibles;
	}

	/**
	 * @see com.servinte.axioma.bl.historiaClinica.interfaz.ICurvasCrecimientoMundo#asociarCurvasAComponente(java.util.List, int)
	 */
	@Override
	public boolean asociarCurvasAComponente(List<PlantillaComponenteDto> plantillasComponente, int codigoComponente) throws IPSException {
		Connection con=null;
		try{
			HibernateUtil.beginTransaction();
			con=UtilidadBD.abrirConexion();
			Date fechaActual=UtilidadFecha.getFechaActualTipoBD(con);
			
			CurvasCrecimientoDelegate delegate = new CurvasCrecimientoDelegate();
			delegate.asociarCurvasAComponente(plantillasComponente, codigoComponente, fechaActual);
			
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		
		return true;
	}

	/**
	 * @see com.servinte.axioma.bl.historiaClinica.interfaz.ICurvasCrecimientoMundo#eliminarCurvasComponente(java.util.List, int)
	 */
	@Override
	public boolean eliminarCurvasComponente(List<PlantillaComponenteDto> plantillasComponente, int codigoComponente)throws IPSException {
		try{
			HibernateUtil.beginTransaction();
			
			CurvasCrecimientoDelegate delegate = new CurvasCrecimientoDelegate();
			delegate.eliminarCurvasComponente(plantillasComponente, codigoComponente);
			
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return true;
	}

	/**
	 * @see com.servinte.axioma.bl.historiaClinica.interfaz.ICurvasCrecimientoMundo#consultarCurvasActivasPaciente(int, int)
	 */
	@Override
	public List<CurvaCrecimientoPacienteDto> consultarCurvasCrecimientoPaciente(int codigoPaciente, int codigoSexo, int edadPaciente) throws IPSException {
		List<CurvaCrecimientoPacienteDto> curvasPaciente = null;
		List<CurvaCrecimientoParametrizabDto> curvasActivas = null;
		try{
			HibernateUtil.beginTransaction();
			CurvasCrecimientoDelegate delegate = new CurvasCrecimientoDelegate();
			
			//Curvas Crecimiento activas para el paciente
			curvasActivas = delegate.consultarCurvasActivasPaciente(codigoSexo, edadPaciente);
			
			curvasPaciente = new LinkedList<CurvaCrecimientoPacienteDto>();
			Object[] datosUltimaCurva = null;
			
			for(CurvaCrecimientoParametrizabDto curva : curvasActivas){
				CurvaCrecimientoPacienteDto curvaPaciente = new CurvaCrecimientoPacienteDto();
				curvaPaciente.setDtoCurvaCrecimientoParametrizab(curva);
				
				datosUltimaCurva = delegate.consultarUltimaImagenCurvaModificadaXPaciente(codigoPaciente, curva.getId());
				
				if(datosUltimaCurva != null){
					curvaPaciente.setIdCurvaModificada((Integer)datosUltimaCurva[0]);
					curvaPaciente.setUrlUltimaCurvaModificada((String)datosUltimaCurva[1]);
					curvaPaciente.setIdImagenParametrizada((Integer)datosUltimaCurva[2]);
					curvaPaciente.setCoordenadasCurvaCrecimiento(delegate.consultarCoordenadasCurvaCrecimiento(codigoPaciente, curva.getDtoImagenesParametrizadas().getId()));
				}
				
				curvasPaciente.add(curvaPaciente);
			}

			HibernateUtil.endTransaction();
		}catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return curvasPaciente;
	}

	/**
	 * @see com.servinte.axioma.bl.historiaClinica.interfaz.ICurvasCrecimientoMundo#guardarCurvaCrecimiento(com.servinte.axioma.dto.historiaClinica.DatosAlmacenarCurvaCrecimientoDto)
	 */
	@Override
	public boolean guardarCurvaCrecimiento(DatosAlmacenarCurvaCrecimientoDto curvaCrecimiento) throws IPSException {
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			Date fechaActual=UtilidadFecha.getFechaActualTipoBD(con);
			SimpleDateFormat formateador = new SimpleDateFormat("yyyy_MM_dd");
			
			String fecha = formateador.format(fechaActual);
			String horaActual = UtilidadFecha.getHoraActual(con);
			
			//Se guarda la imagen en el servidor
			byte[] decodedBytes = null;
			String img64 = curvaCrecimiento.getImagenBase64();
			decodedBytes = DatatypeConverter.parseBase64Binary(img64);
			BufferedImage bfi = ImageIO.read(new ByteArrayInputStream(decodedBytes));
			
			File carpeta = new File(DIRECTORIO_IMAGENES + SUBDIRECTORIO_CURVAS); 
			if(!carpeta.exists())
				carpeta.mkdirs();
			
			String urlImagen = SUBDIRECTORIO_CURVAS.concat("curvaPaciente").concat("-").concat(String.valueOf(curvaCrecimiento.getNumeroSolicitud())).concat("-").concat(String.valueOf(curvaCrecimiento.getCodigoCurvaParametrizada())).concat("-").concat(fecha).concat("_").concat(horaActual).concat(".").concat(FORMATO_IMAGEN);
			
			File outputFile = new File(DIRECTORIO_IMAGENES.concat(urlImagen));
			
			ImageIO.write(bfi ,FORMATO_IMAGEN, outputFile);
			bfi.flush();

			curvaCrecimiento.setFechaCreacion(fechaActual);
			curvaCrecimiento.setRutaImagen(urlImagen);
			
			HibernateUtil.beginTransaction();

			CurvasCrecimientoDelegate delegate = new CurvasCrecimientoDelegate();
			
			if(!curvaCrecimiento.isEsValoracion()){
				//Guarda la Curva de la Evolucion
				delegate.guardarCurvaCrecimientoEvolucion(curvaCrecimiento);
				
			}else{
				//Guarda la Curva de la Valoracion
				delegate.guardarCurvaCrecimientoValoracion(curvaCrecimiento);
			}

			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return true;
	}
	
	/**
	 * @see com.servinte.axioma.bl.historiaClinica.interfaz.ICurvasCrecimientoMundo#consultarDatosPacienteFormatoImpresion(int, com.servinte.axioma.generadorReporte.historiaClinica.curvasCrecimiento.CurvaCrecimientoDesarrolloDto)
	 */
	public CurvaCrecimientoDesarrolloDto consultarDatosPacienteFormatoImpresion(int codigoIngreso, CurvaCrecimientoDesarrolloDto dtoCurvaCrecimientoDesarrollo) throws IPSException {
		try{
			Object[] datosPaciente = null;
			Object[] datosAcompanante = null;
			
			HibernateUtil.beginTransaction();
			CurvasCrecimientoDelegate delegate = new CurvasCrecimientoDelegate();
			
			datosPaciente = delegate.consultarDatosPacienteFormatoImpresion(codigoIngreso);
			
			if(datosPaciente != null){
				dtoCurvaCrecimientoDesarrollo.setEstadoCivil((String)datosPaciente[0]);
				dtoCurvaCrecimientoDesarrollo.setOcupacion((String)datosPaciente[1]);
				dtoCurvaCrecimientoDesarrollo.setTipoAfiliacion((String)datosPaciente[2]);
			}
			
			datosAcompanante = delegate.consultarDatosAcompananteFormatoImpresion(codigoIngreso);
			
			if(datosAcompanante != null){
				dtoCurvaCrecimientoDesarrollo.setAcompanantePaciente((String)datosAcompanante[0]);
				dtoCurvaCrecimientoDesarrollo.setTelAcompanante((String)datosAcompanante[1]);
				dtoCurvaCrecimientoDesarrollo.setParentescoAcompanante((String)datosAcompanante[2]);
				dtoCurvaCrecimientoDesarrollo.setResponsablePaciente((String)datosAcompanante[0]);
				dtoCurvaCrecimientoDesarrollo.setTelResponsable((String)datosAcompanante[1]);
				dtoCurvaCrecimientoDesarrollo.setParentescoResponsable((String)datosAcompanante[2]);
			}

			HibernateUtil.endTransaction();
		}catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return dtoCurvaCrecimientoDesarrollo;
	}
	
	@Override
	public CurvaCrecimientoParametrizabDto consultarCurvaParametrizada(int idCurva) throws IPSException {
		CurvaCrecimientoParametrizabDto curva;
		try 
		{
			HibernateUtil.beginTransaction();
			curva = new CatalogoHistoriaClinicaDelegate().consultarCurvaParametrizada(idCurva);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) 
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return curva;
	}

	@Override
	public List<ImagenParametrizadaDto> consultarImagenesParametrizadas(int idCurva) throws IPSException {
		List<ImagenParametrizadaDto> imagenesParametrizadas;
		try 
		{
			HibernateUtil.beginTransaction();
			CatalogoHistoriaClinicaDelegate delegate = new CatalogoHistoriaClinicaDelegate();
			imagenesParametrizadas = delegate.consultarImagenesParametrizadas(idCurva);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) 
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return imagenesParametrizadas;
	}

	@Override
	public void guardarCurvaParametrizada(CurvaCrecimientoParametrizabDto dtoccp) throws IPSException {
		try 
		{
			HibernateUtil.beginTransaction();
			new CatalogoHistoriaClinicaDelegate().guardarCurvaParametrizada(dtoccp);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) 
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}

	@Override
	public void eliminarCurvaParametrizada(int idCurva) throws IPSException {
		try 
		{
			HibernateUtil.beginTransaction();
			new CurvasCrecimientoDelegate().eliminarCurvaParametrizada(idCurva);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) 
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}

	@Override
	public List<CurvaCrecimientoParametrizabDto> buscarCurvaCriterios(String titulo, String descripcion, Integer edadInicial, Integer edadFinal, Integer codigoSexo, Boolean activo, Boolean inidicadorError) throws IPSException{
		List<CurvaCrecimientoParametrizabDto> curvasParametrizadas = null;
		try{
			HibernateUtil.beginTransaction();
			curvasParametrizadas = new CurvasCrecimientoDelegate().buscarCurvaCriterios(titulo, descripcion, edadInicial, edadFinal, codigoSexo, activo, inidicadorError);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return curvasParametrizadas;
	}
	
	public List<HistoricoImagenPlantillaDto> valoracionesYevolucionesPorpacienteConCurva(int codigoPaciente) throws IPSException{
		List<HistoricoImagenPlantillaDto> historico;
		try
		{
			HibernateUtil.beginTransaction();
			historico = new CurvasCrecimientoDelegate().valoracionesYevolucionesPorpacienteConCurva(codigoPaciente);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) 
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return historico;
	}

	@Override
	public List<HistoricoImagenPlantillaDto> valoracionesYevolucionesPorpacienteYporIngresoConCurva(int codigoPaciente, int idCuenta) throws IPSException {
		List<HistoricoImagenPlantillaDto> historico;
		try
		{
			HibernateUtil.beginTransaction();
			historico = new CurvasCrecimientoDelegate().valoracionesYevolucionesPorpacienteYporIngresoConCurva(codigoPaciente, idCuenta);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) 
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return historico;
	}

	@Override
	public List<HistoricoImagenPlantillaDto> valoracionesPorId(int idValoracion)throws IPSException {
		List<HistoricoImagenPlantillaDto> valoraciones;
		try
		{
			HibernateUtil.beginTransaction();
			valoraciones = new CurvasCrecimientoDelegate().valoracionesPorId(idValoracion);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) 
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return valoraciones;
	}

	@Override
	public List<HistoricoImagenPlantillaDto> evolucionesPorId(int idEvolucion)throws IPSException {
		List<HistoricoImagenPlantillaDto> evoluciones;
		try
		{
			HibernateUtil.beginTransaction();
			evoluciones = new CurvasCrecimientoDelegate().evolucionesPorId(idEvolucion);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) 
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return evoluciones;
	}
	
	
	public boolean existeCurvasAnteriores(int codigoPaciente, int codigoInstitucion, Date fechaCorteCurvas) throws IPSException{
		
		boolean existeCurvas = false;
		try
		{
			HibernateUtil.beginTransaction();
			existeCurvas = new CurvasCrecimientoDelegate().existeCurvasAnteriores(codigoPaciente, codigoInstitucion, fechaCorteCurvas);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) 
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return existeCurvas;
	}
}