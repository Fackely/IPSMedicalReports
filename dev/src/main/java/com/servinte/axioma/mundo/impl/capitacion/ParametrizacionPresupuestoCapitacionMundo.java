package com.servinte.axioma.mundo.impl.capitacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.capitacion.ConstantesCapitacion;

import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO;
import com.servinte.axioma.dto.capitacion.DtoLogParametrizacionPresupuesto;
import com.servinte.axioma.dto.capitacion.DtoMotivosModifiPresupuesto;
import com.servinte.axioma.dto.capitacion.DtoNivelesAtencionPresupuestoParametrizacionGeneral;
import com.servinte.axioma.dto.capitacion.DtoParamPresupCap;
import com.servinte.axioma.dto.capitacion.DtoParametrizacionGeneral;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ILogParametrizacionPresupuestoCapMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IMotivosModificacionPresupuestoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAtencionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IValorizacionPresupuestoCapGeneralMundo;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.DetalleValorizacionArt;
import com.servinte.axioma.orm.DetalleValorizacionServ;
import com.servinte.axioma.orm.LogDetalleParamPresup;
import com.servinte.axioma.orm.NivelAtencion;
import com.servinte.axioma.orm.ParamPresupuestosCap;
import com.servinte.axioma.orm.ValorizacionPresCapGen;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Define la logica de negocio relaciona con la parametrización
 * del presupuesto de capitación
 * @author diecorqu
 * @see IParametrizacionPresupuestoCapitacionMundo
 */
public class ParametrizacionPresupuestoCapitacionMundo implements IParametrizacionPresupuestoCapitacionMundo {
	
	
	private IParametrizacionPresupuestosCapDAO parametrizacionDAO;
	
	INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
	IValorizacionPresupuestoCapGeneralMundo valorizacionGeneralMundo = 
		CapitacionFabricaMundo.crearValorizacionPresupuestoCapGeneralMundo();
	IDetalleValorizacionServicioMundo detalleValorizacionServicioMundo = 
		CapitacionFabricaMundo.crearDetalleValorizacionServicioMundo();
	IDetalleValorizacionArticuloMundo detalleValorizacionArticuloMundo = 
		CapitacionFabricaMundo.crearDetalleValorizacionArticuloMundo();
	ILogParametrizacionPresupuestoCapMundo logParametrizacionMundo = 
		CapitacionFabricaMundo.crearLogParametrizacionPresupuestoCapMundo();
	ILogDetalleParametrizacionPresupuestoMundo logDetalleParametrizacionMundo =
		CapitacionFabricaMundo.crearLogDetalleParametrizacionPresupuestoMundo();
	IMotivosModificacionPresupuestoMundo motivosModificacionMundo = 
		CapitacionFabricaMundo.crearMotivosModifiaccionPresupuestoMundo();
	
	public ParametrizacionPresupuestoCapitacionMundo () {
		parametrizacionDAO = CapitacionFabricaDAO.crearParamPresupuestosCapDAO();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#obtenerFechasVigenciasContrato(com.servinte.axioma.orm.Contratos)
	 */
	public ArrayList<String> obtenerFechasVigenciasContrato(Contratos contrato){
		ArrayList<String> fechasVigencia = new ArrayList<String>();
		Calendar cal1 = GregorianCalendar.getInstance();
		cal1.setTime(contrato.getFechaInicial());
		int yearInicial = cal1.get(Calendar.YEAR);
		Calendar cal2 = GregorianCalendar.getInstance();
		cal2.setTime(contrato.getFechaFinal());
		int yearFinal = cal2.get(Calendar.YEAR);
		Calendar cal3 = GregorianCalendar.getInstance();
		cal3.setTime(UtilidadFecha.conversionFormatoFechaStringDate(Utilidades.capturarFechaBD()));
		int yearActual = cal3.get(Calendar.YEAR);
		for (int i = yearInicial; i <= yearFinal; i++) {
			if (i >= yearActual ) {
				fechasVigencia.add(String.valueOf(i));
			}
		}
		return fechasVigencia;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#obtenerMesesVigenciasContrato(com.servinte.axioma.orm.Contratos, int)
	 */
	public Vector<Integer> obtenerMesesVigenciasContrato(Contratos contrato, int yearVigencia){
		Vector<Integer> mesesVigencia = new Vector<Integer>();
		Calendar cal1 = GregorianCalendar.getInstance();
		cal1.setTime(contrato.getFechaInicial());
		int yearInicial = cal1.get(Calendar.YEAR);
		Calendar cal2 = GregorianCalendar.getInstance();
		cal2.setTime(contrato.getFechaFinal());
		int yearFinal = cal2.get(Calendar.YEAR);
		int mesInicial = 0;
		int mesFinal = 0;
		if (yearVigencia > yearInicial && yearVigencia < yearFinal) {
			for (int i = 0; i < 12; i++) {
				mesesVigencia.add(i);
			}
		} else if (yearInicial == yearVigencia &&  yearVigencia == yearFinal) {
			mesInicial = cal1.get(Calendar.MONTH);
			mesFinal = cal2.get(Calendar.MONTH);
			for (int i = mesInicial; i <= mesFinal; i++) {
				mesesVigencia.add(i);
			}
		} else if (yearInicial == yearVigencia && yearVigencia != yearFinal) {
			mesInicial = cal1.get(Calendar.MONTH);
			mesFinal = Calendar.DECEMBER;
			for (int i = mesInicial; i <= mesFinal; i++) {
				mesesVigencia.add(i);
			}
		} else if (yearFinal == yearVigencia && yearVigencia != yearInicial) {
			mesInicial = Calendar.JANUARY;
			mesFinal = cal2.get(Calendar.MONTH);
			for (int i = mesInicial; i <= mesFinal; i++) {
				mesesVigencia.add(i);
			}
		} 
		return mesesVigencia;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#cargarDatosParametrizacionGeneral(com.servinte.axioma.orm.ParamPresupuestosCap)
	 */
	public DtoParametrizacionGeneral cargarDatosParametrizacionGeneral (ParamPresupuestosCap parametrizacion, String tipo) {
		DtoParametrizacionGeneral dto = new DtoParametrizacionGeneral();
		Vector<Integer> mesesVigencia = this.obtenerMesesVigenciasContrato(
				parametrizacion.getContratos(), Integer.parseInt(parametrizacion.getAnioVigencia()));
		ArrayList<String[]> mesesMatriz = new ArrayList<String[]>();
		ArrayList<NivelAtencion> listaNivelesContrato = null;
		ArrayList<NivelAtencion> listaNivelesParametrizacion = null;
		ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> listaDtoNivelesAtencion = null;
		if ("creacion".equals(tipo)) {
			listaNivelesContrato = 
				nivelAtencionMundo.listarNivelesAtencionContrato(parametrizacion.getContratos().getCodigo());
			listaDtoNivelesAtencion = 
				this.obtenerNivelesAtencionPresupuestoParametrizacionGen(listaNivelesContrato);
		} else if ("modificacion".equals(tipo)) {
			listaNivelesParametrizacion = 
				nivelAtencionMundo.listarNivelesAtencionParametrizacionPresupuesto(parametrizacion.getCodigo());
			listaNivelesContrato = 
				nivelAtencionMundo.listarNivelesAtencionContrato(parametrizacion.getContratos().getCodigo());
			
			//Adicionar los nuevos niveles del contrato a la parametrización de presupuesto
			//MT 5152
			boolean repetido=false;
			for(NivelAtencion nivelAtencionContrato : listaNivelesContrato){
			  for (NivelAtencion nivelAtencion : listaNivelesParametrizacion) {
				 if(nivelAtencion.getConsecutivo() == nivelAtencionContrato.getConsecutivo()){
					repetido=true;
					break;
				   }else{
					 repetido=false;
				  }
				}
			  if(!repetido){
				  listaNivelesParametrizacion.add(nivelAtencionContrato);
			  }
			  			  
		    }
			listaDtoNivelesAtencion = 
				this.obtenerNivelesAtencionPresupuestoParametrizacionGen(listaNivelesParametrizacion);
			/*listaDtoNivelesAtencion = 
				this.obtenerNivelesAtencionPresupuestoParametrizacionExistentes(listaNiveles, parametrizacion.getCodigo());*/
		}
		
		
		for (int i = 0; i < 12; i++) {
			String[] mes = new String[3];
			if (mesesVigencia.contains(i)) {
				mes[0] = "mes_"+i;
				mes[1] = "style='background-color:white;text-align:right'";
				mes[2] = "Activo";
			} else {
				mes[0] = "mes_inactivo_"+i;
				mes[1] = "disabled='disabled'";
				mes[2] = "Inactivo";
			}
			mesesMatriz.add(mes);
		}
		dto.setParametrizacionPresupuesto(parametrizacion);
		dto.setMesesMatriz(mesesMatriz);
		dto.setListaDtoNivelesAtencion(listaDtoNivelesAtencion);
		dto.setListaNivelesParametrizacion(listaNivelesParametrizacion);
		dto.setListaNivelesContrato(listaNivelesContrato);
		dto.setEstado("crear");
		
		return dto;
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#cargarValorizacionParametrizacionGeneral(com.servinte.axioma.orm.ParamPresupuestosCap)
	 */
	public DtoParametrizacionGeneral cargarValorizacionParametrizacionGeneral(ParamPresupuestosCap parametrizacionTmp) {
		DtoParametrizacionGeneral dto = new DtoParametrizacionGeneral();
		ParamPresupuestosCap parametrizacionPresupuesto = null;
		ArrayList<DtoParamPresupCap> listadoParametrizaciones = null;
		boolean existeUnicaParametrizacion = false;
		boolean existenResultados = true;
		
		/*
		 * Si se requiere una parametrización específica por contrato y año se busca y retorna
		 * si se requieren las parametrizaciónes por contrato se carga en el DtoParametrizacionGeneral
		 * la lista de parametrizaciones existentes para ese contrato
		 */
		if (Integer.parseInt(parametrizacionTmp.getAnioVigencia()) != ConstantesBD.codigoNuncaValido) {
			parametrizacionPresupuesto = 
				this.obtenerParametrizacionPresupuestoCapitado(
						parametrizacionTmp.getContratos().getCodigo(), parametrizacionTmp.getAnioVigencia());
			existeUnicaParametrizacion = true;
			if(parametrizacionPresupuesto == null) {
				existenResultados = false;
			}
		} else {
			listadoParametrizaciones = this.listarParametrizacionesPresupuestoCapxContrato(
					parametrizacionTmp.getContratos().getCodigo());
			if (!listadoParametrizaciones.isEmpty()) {
				if (listadoParametrizaciones.size() == 1) {
					parametrizacionPresupuesto = this.findById(listadoParametrizaciones.get(0).getCodigoParametrizacionPresupuesto());
					existeUnicaParametrizacion = true;
				}
			} else {
				existenResultados = false;
			}
			
		}
		if (existenResultados) {
			if (existeUnicaParametrizacion) {
				dto = this.cargarDatosParametrizacionGeneral(parametrizacionPresupuesto, "modificacion");
				dto.setValorizacionPresupuestoGeneral(
						valorizacionGeneralMundo.valoracionPresupuestoCap(parametrizacionPresupuesto.getCodigo()));
				dto.setEstado("modificar");
				/*
				 * Se pregunta si el usuario logueado tiene los permisos necesarios para poder modificar 
				 * la parametrización
				 */
				if(!Utilidades.tieneRolFuncionalidad(parametrizacionTmp.getUsuarios().getLogin(), ConstantesBD.codigoFuncionalidadConsultarModificarPresupuestoCapitacion)) {
					dto.setSoloLectura(true);
				} else {
					dto.setSoloLectura(false);
				}
			} else {
				dto.setParametrizacionPresupuesto(parametrizacionPresupuesto);
				dto.setListaParametrizaciones(listadoParametrizaciones);
			}
		} else {
			dto = this.cargarDatosParametrizacionGeneral(parametrizacionTmp, "modificacion");
		}
		return dto;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#guardarModificacionesParametrizacion(com.servinte.axioma.dto.capitacion.DtoParametrizacionGeneral, java.util.ArrayList, com.servinte.axioma.dto.capitacion.DtoLogParametrizacionPresupuesto)
	 */
	public boolean guardarModificacionesParametrizacion (DtoParametrizacionGeneral dtoGeneral, 
														 ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> listaDtoNivelesDetalle,
														 DtoLogParametrizacionPresupuesto dtoLog) {
		boolean resultado = true;
		double valorGastoDetalle = 0D;
		/*
		 * Se realiza la modificación de la parametrización del presupuesto de capitación
		 */
		if (dtoGeneral.getParametrizacionPresupuesto() != null) {
			UtilidadTransaccion.getTransaccion().begin();
			ParamPresupuestosCap parametrizacionGeneral = this.modificarParametrizacionPresupuesto(dtoGeneral.getParametrizacionPresupuesto());
			if (parametrizacionGeneral == null) {
				UtilidadTransaccion.getTransaccion().rollback();
				resultado = false;
			} else {
				UtilidadTransaccion.getTransaccion().commit();
			}
		}

		/*
		 * Se modifica la valorizacion general para la parametrizacio del presupuesto
		 */
		if (dtoGeneral.getValorizacionPresupuestoGeneral() != null && !dtoGeneral.getValorizacionPresupuestoGeneral().isEmpty()) {
			for (ValorizacionPresCapGen valorizacion : dtoGeneral.getValorizacionPresupuestoGeneral()) {
				UtilidadTransaccion.getTransaccion().begin();
				ValorizacionPresCapGen valorizacionGeneral = valorizacionGeneralMundo.modificarValorizacionPresupuestoCapitado(valorizacion);
				if (valorizacionGeneral == null) {
					UtilidadTransaccion.getTransaccion().rollback();
					resultado = false;
					break;
				} else {
					UtilidadTransaccion.getTransaccion().commit();
				}
			}
		}	

		/*
		 * Se modifica la valorizacion para los grupos de servicio y clases de inventario de la 
		 * parametrizacion del presupuesto
		 */
		
		UtilidadTransaccion.getTransaccion().begin();
		
		for (DtoNivelesAtencionPresupuestoParametrizacionGeneral dtoNivelesDetalles : listaDtoNivelesDetalle) {
			ArrayList<DetalleValorizacionArt> detallesClaseInventario = null;
			ArrayList<DetalleValorizacionServ> detallesGrupoServicio = null;
			
			if(dtoNivelesDetalles.getDetalleValorizacionArticulos().isEmpty()) {
				detallesClaseInventario = detalleValorizacionArticuloMundo.obtenerValorizacionDetalleClaseInventario(
						dtoGeneral.getParametrizacionPresupuesto().getCodigo(), dtoNivelesDetalles.getNivelAtencion().getConsecutivo());
			} else {
				detallesClaseInventario = dtoNivelesDetalles.getDetalleValorizacionArticulos();
			}
			
			if(dtoNivelesDetalles.getDetalleValorizacionServicios().isEmpty()) {
				detallesGrupoServicio = detalleValorizacionServicioMundo.obtenerValorizacionDetalleGrupoServicio(
						dtoGeneral.getParametrizacionPresupuesto().getCodigo(), dtoNivelesDetalles.getNivelAtencion().getConsecutivo());
			} else {
				detallesGrupoServicio = dtoNivelesDetalles.getDetalleValorizacionServicios();
			}
			
			if (!detallesGrupoServicio.isEmpty()) {
				for (DetalleValorizacionServ detalleValorizacionServ : detallesGrupoServicio) {
					ValorizacionPresCapGen valorizacionGeneralMes = valorizacionGeneralMundo.
							obtenerValorizacionGeneralxNivelAtencionSubSeccionMes(
									detalleValorizacionServ.getParamPresupuestosCap().getCodigo(), 
									detalleValorizacionServ.getNivelAtencion().getConsecutivo(), 
									detalleValorizacionServ.getMes(), ConstantesCapitacion.subSeccionServicio);
					
					valorGastoDetalle = (valorizacionGeneralMes.getValorGastoSubSeccion().doubleValue() * 
										 detalleValorizacionServ.getPorcentajeGasto().doubleValue()) / 100D;
					
					detalleValorizacionServ.setValorGasto(new BigDecimal(valorGastoDetalle));
					
					DetalleValorizacionServ detalleGrupoInventario = 
						detalleValorizacionServicioMundo.modificarValorizacionDetalleServicio(detalleValorizacionServ);
					if (detalleGrupoInventario == null) {
						resultado = false;
						break;
					}
				}
			}

			if (!detallesClaseInventario.isEmpty()) {
				for (DetalleValorizacionArt detalleValorizacionArt : detallesClaseInventario) {
					ValorizacionPresCapGen valorizacionGeneralMes = valorizacionGeneralMundo.
					obtenerValorizacionGeneralxNivelAtencionSubSeccionMes(
							detalleValorizacionArt.getParamPresupuestosCap().getCodigo(), 
							detalleValorizacionArt.getNivelAtencion().getConsecutivo(), 
							detalleValorizacionArt.getMes(), ConstantesCapitacion.subSeccionArticulo);
			
					valorGastoDetalle = (valorizacionGeneralMes.getValorGastoSubSeccion().doubleValue() * 
							detalleValorizacionArt.getPorcentajeGasto().doubleValue()) / 100D;
					
					detalleValorizacionArt.setValorGasto(new BigDecimal(valorGastoDetalle));
					
					UtilidadTransaccion.getTransaccion().begin();
					
					DetalleValorizacionArt detalleClaseInventario = 
						detalleValorizacionArticuloMundo.modificarValorizacionDetalleClaseInventario(detalleValorizacionArt);
					if (detalleClaseInventario == null) {
						resultado = false;
						break;
					}	
				}
			}
		}
		
		if (resultado) {
			UtilidadTransaccion.getTransaccion().commit();
		} else {
			UtilidadTransaccion.getTransaccion().rollback();
		}

		/*
		 * Se guarda el log generado de las modificaciones
		 * 
		 */

		UtilidadTransaccion.getTransaccion().begin();
		
		boolean guardoLog = logParametrizacionMundo.guardarLogParametrizacionPresupuesto(dtoLog.getLogParametrizacion());

		if (guardoLog) {
			UtilidadTransaccion.getTransaccion().commit();
		} else {
			UtilidadTransaccion.getTransaccion().rollback();
		}
		
		if (guardoLog) {
			UtilidadTransaccion.getTransaccion().begin();
			for (LogDetalleParamPresup logDetalle : dtoLog.getListaLogDetalleParametrizacion()) {
				logDetalle.setLogParamPresupuestoCap(dtoLog.getLogParametrizacion());
				logDetalleParametrizacionMundo.guardarLogDetalleParametrizacionPresupuesto(logDetalle);
			}
			UtilidadTransaccion.getTransaccion().commit();
		} else {
			UtilidadTransaccion.getTransaccion().rollback();
			resultado = false;
		}

		return resultado;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#listaMotivosModificacion()
	 */
	public ArrayList<DtoMotivosModifiPresupuesto> listaMotivosModificacion () {
		return motivosModificacionMundo.consultarTodosMotivosModificacion();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#obtenerParametrizacionPresupuestoCapitado(int, java.lang.String)
	 */
	@Override
	public ParamPresupuestosCap obtenerParametrizacionPresupuestoCapitado(
			int codContrato, String anioVigencia) {
		return parametrizacionDAO.obtenerParametrizacionPresupuestoCapitado(
				codContrato, anioVigencia);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#findById(int)
	 */
	@Override
	public ParamPresupuestosCap findById(long codParametrizacion) {
		return parametrizacionDAO.findById(codParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#listarParametrizacionesPresupuestoCap()
	 */
	@Override
	public ArrayList<ParamPresupuestosCap> listarParametrizacionesPresupuestoCap() {
		return parametrizacionDAO.listarParametrizacionesPresupuestoCap();
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#listarParametrizacionesPresupuestoCapxContrato(int)
	 */
	@Override
	public ArrayList<DtoParamPresupCap> listarParametrizacionesPresupuestoCapxContrato(
			int codContrato) {
		return parametrizacionDAO.listarParametrizacionesPresupuestoCapxContrato(codContrato);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#existeParametrizacionPresupuesto(int, java.lang.String)
	 */
	@Override
	public boolean existeParametrizacionPresupuesto(int codContrato,
			String anioVigencia) {
		return parametrizacionDAO.existeParametrizacionPresupuesto(codContrato, anioVigencia);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#obtenerNivelesAtencionPresupuestoParametrizacionGen(java.util.ArrayList)
	 */
	public ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> 
				obtenerNivelesAtencionPresupuestoParametrizacionGen
						(ArrayList<NivelAtencion> listaNivelesContrato) {
		return parametrizacionDAO.obtenerNivelesAtencionPresupuestoParametrizacionGen(listaNivelesContrato);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#guardarParametrizacionPresupuesto(com.servinte.axioma.orm.ParamPresupuestosCap)
	 */
	@Override
	public boolean guardarParametrizacionPresupuesto(
			ParamPresupuestosCap parametrizacionPresupuesto) {
		return parametrizacionDAO.guardarParametrizacionPresupuesto(parametrizacionPresupuesto);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#modificarParametrizacionPresupuesto(com.servinte.axioma.orm.ParamPresupuestosCap)
	 */
	@Override
	public ParamPresupuestosCap modificarParametrizacionPresupuesto(
			ParamPresupuestosCap parametrizacionPresupuesto) {
		return parametrizacionDAO.modificarParametrizacionPresupuesto(parametrizacionPresupuesto);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#eliminarParametrizacionPresupuesto(com.servinte.axioma.orm.ParamPresupuestosCap)
	 */
	@Override
	public void eliminarParametrizacionPresupuesto(
			ParamPresupuestosCap parametrizacion) {
		parametrizacionDAO.eliminarParametrizacionPresupuesto(parametrizacion);
		
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#existeParametrizacionPresupuestoConvenio(int, java.lang.String)
	 */
	@Override
	public boolean existeParametrizacionPresupuestoConvenio(int codConvenio,
			String anioVigencia) {
		return parametrizacionDAO.existeParametrizacionPresupuestoConvenio(codConvenio, anioVigencia);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#obtenerNivelesAtencionPresupuestoParametrizacionExistentes(java.util.ArrayList, long)
	 */
	@Override
	public ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> obtenerNivelesAtencionPresupuestoParametrizacionExistentes(
			ArrayList<NivelAtencion> listaNivelesContrato,
			long codigoParametrizacion) {
		return parametrizacionDAO.obtenerNivelesAtencionPresupuestoParametrizacionExistentes(
				listaNivelesContrato, codigoParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#existeNivelAtencionPresupuestoCapitacion(long)
	 */
	@Override
	public boolean existeNivelAtencionPresupuestoCapitacion(
			long codNivelAtencion) {
		return parametrizacionDAO.existeNivelAtencionPresupuestoCapitacion(codNivelAtencion);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#obtenerValorParametrizacionPresupuestoDetalladoServicios(int, java.lang.String, int, int, long)
	 */
	@Override
	public Double obtenerValorParametrizacionPresupuestoDetalladoServicios(
			int codContrato, String anioVigencia, int mes,
			int codigoGrupoServicio, long consecutivoNivelAtencion) {
		return parametrizacionDAO.obtenerValorParametrizacionPresupuestoDetalladoServicios(codContrato, 
									anioVigencia, mes, codigoGrupoServicio, consecutivoNivelAtencion);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#obtenerValorParametrizacionPresupuestoGeneralServiciosArticulos(int, java.lang.String, int, long, java.lang.String)
	 */
	@Override
	public Double obtenerValorParametrizacionPresupuestoGeneralServiciosArticulos(
			int codContrato, String anioVigencia, int mes,
			long consecutivoNivelAtencion, String servicioArticulo) {
		return parametrizacionDAO.obtenerValorParametrizacionPresupuestoGeneralServiciosArticulos(codContrato, 
				anioVigencia, mes, consecutivoNivelAtencion, servicioArticulo);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo#obtenerValorParametrizacionPresupuestoDetalladoArticulos(int, java.lang.String, int, int, long)
	 */
	@Override
	public Double obtenerValorParametrizacionPresupuestoDetalladoArticulos(
			int codContrato, String anioVigencia, int mes,
			int codigoClaseInventario, long consecutivoNivelAtencion) {
		return parametrizacionDAO.obtenerValorParametrizacionPresupuestoDetalladoArticulos(codContrato, 
				anioVigencia, mes, codigoClaseInventario, consecutivoNivelAtencion);
	}

}
