package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.dto.capitacion.DtoInconsistenciasProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;
import com.princetonsa.dto.facturacion.DtoProcesoFacturacionPresupuestoCapitacion;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.fabrica.facturacion.convenio.ConvenioFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IFacturasDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IContratoDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoFacturacionPresupuestoCapitacionMundo;
import com.servinte.axioma.orm.Contratos;


/**
 * Proceso de facturación. Anexo 1029
 * @author Cristhian Murillo
 */
public class ProcesoFacturacionPresupuestoCapitacionMundo implements IProcesoFacturacionPresupuestoCapitacionMundo
{
	private IFacturasDAO facturasDAO; 							
	private IContratoDAO contratoDAO; 							
	private ArrayList<String> listaKeysMapaArticulo;
	private ArrayList<String> listaKeysMapaServicio;
	
	private ArrayList<DtoTotalProcesoPresupuestoCapitado> totalListaAgrupadaServicioNivelAtencionGrupoServicio;
	private ArrayList<DtoTotalProcesoPresupuestoCapitado> totalListaAgrupadaArticuloNivelAtencionClaseInventario;
	

	
	/**
	 * Constructor de la clase
	 */
	public ProcesoFacturacionPresupuestoCapitacionMundo() 
	{
		facturasDAO 				= FacturacionFabricaDAO.crearFacturasDAO();
		contratoDAO 				= ConvenioFabricaDAO.crearContratoDAO();
		
		this.listaKeysMapaArticulo = new ArrayList<String>();
		this.listaKeysMapaServicio = new ArrayList<String>();
		
		this.totalListaAgrupadaServicioNivelAtencionGrupoServicio = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		this.totalListaAgrupadaArticuloNivelAtencionClaseInventario = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
	}
	
	
	
	@Override
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> realizarProcesoFacturacion(DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado) 
	{
		/*Se deben consultar las facturas que hayan sido generadas 
		 * en el rango de fechas seleccionado para el cierre. */
		DtoProcesoFacturacionPresupuestoCapitacion parametros = new DtoProcesoFacturacionPresupuestoCapitacion();
		parametros.setFechaInicial(dtoProcesoPresupuestoCapitado.getFechaInicio());
		parametros.setFechafinal(dtoProcesoPresupuestoCapitado.getFechaFin());
		parametros.setExcluiranuladas(false); // Trae todas las facturas para la fecha sin importar si fueron anuladas. Luego le resta las anuladas
		parametros.setSoloAnuladas(false);
		parametros.setInstitucion(dtoProcesoPresupuestoCapitado.getInstitucion());
		
		/* Los cálculos se deben realizar por cada convenio-contrato. */
		if(dtoProcesoPresupuestoCapitado.getConvenio() != null){
			parametros.setConvenio(dtoProcesoPresupuestoCapitado.getConvenio());
		}
		if(dtoProcesoPresupuestoCapitado.getContrato() != null){
			parametros.setContrato(dtoProcesoPresupuestoCapitado.getContrato());
		}
		
		ArrayList<DtoProcesoFacturacionPresupuestoCapitacion> listaFacturasTodas = new ArrayList<DtoProcesoFacturacionPresupuestoCapitacion>();
		listaFacturasTodas.addAll(facturasDAO.buscarFacturasPorRangoFecha(parametros));
		
		ArrayList<DtoProcesoFacturacionPresupuestoCapitacion> listaFacturasAnuladas = new ArrayList<DtoProcesoFacturacionPresupuestoCapitacion>();
		//parametros.setSoloAnuladas(true);
		parametros.setExcluiranuladas(true);
		listaFacturasAnuladas.addAll(facturasDAO.buscarFacturasPorRangoFecha(parametros));
		organizarFacturasAnuladasParaRestarTotal(listaFacturasAnuladas);
		
		// --------------------------------------------------------------------------------------
		ArrayList<DtoProcesoFacturacionPresupuestoCapitacion> listaFacturas = new ArrayList<DtoProcesoFacturacionPresupuestoCapitacion>();
		listaFacturas.addAll(listaFacturasTodas);
		listaFacturas.addAll(listaFacturasAnuladas);
		//---------------------------------------------------------------------------------------
		
		int codInstitucion = ConstantesBD.codigoNuncaValido;
		if(dtoProcesoPresupuestoCapitado.getInstitucion() != null){
			codInstitucion = dtoProcesoPresupuestoCapitado.getInstitucion();
		}
		
		/* Se listan los contratos para hacer el filtro */
		ArrayList<Contratos> listaContratos = new ArrayList<Contratos>();
		if(dtoProcesoPresupuestoCapitado.getContrato() == null)
		{
			listaContratos.addAll(contratoDAO.listarContratos(codInstitucion));
		}
		else{
			listaContratos.add(contratoDAO.findById(dtoProcesoPresupuestoCapitado.getContrato()));
		}
		//-------------------------------------------------------------------
		
		dtoProcesoPresupuestoCapitado.setListaContratos(listaContratos);
		dtoProcesoPresupuestoCapitado.setTipoProceso(ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion);
		
		//---------------------------
		ArrayList<DtoTotalProcesoPresupuestoCapitado> listaProceso = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		listaProceso = generarTotales(dtoProcesoPresupuestoCapitado, listaFacturas);
		//---------------------------
		
		return listaProceso;
	}
	

	/**
	 * Genera los totales para los servicios definidos en el anexo 1029.
	 * 
	 * @return ArrayList<DtoTotalProcesoPresupuestoCapitado>
	 * 
	 * @author Cristhian Murillo
	*/
	private ArrayList<DtoTotalProcesoPresupuestoCapitado> generarTotales(DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado, ArrayList<DtoProcesoFacturacionPresupuestoCapitacion> listaFacturas)
	{
		Date fechaCierre = dtoProcesoPresupuestoCapitado.getFechaInicio();
		HashMap<String, DtoTotalProcesoPresupuestoCapitado> mapaProcesoFacturacionArticulo = new HashMap<String, DtoTotalProcesoPresupuestoCapitado>();
		HashMap<String, DtoTotalProcesoPresupuestoCapitado> mapaProcesoFacturacionServicio = new HashMap<String, DtoTotalProcesoPresupuestoCapitado>();
		
		while(fechaCierre.before(dtoProcesoPresupuestoCapitado.getFechaFin()) || fechaCierre.equals(dtoProcesoPresupuestoCapitado.getFechaFin()))
		{ 
			for (Contratos contratos : dtoProcesoPresupuestoCapitado.getListaContratos()) 
			{
				Integer convenio = contratos.getConvenios().getCodigo();
				Integer contrato = contratos.getCodigo();
				
				for (DtoProcesoFacturacionPresupuestoCapitacion factura : listaFacturas) 
				{
					if(factura.getContrato().equals(contrato))
					{
						// Recorre la lista de facturas (detcargos) consultados y Verifica si corresponde a artículo o servicio
						if(factura.getServicioDetCargo() != null)
						{
							/* Totales Facturación de Servicios por Nivel de Atención y Grupo de Servicios por cada servicio. --- */
							DtoTotalProcesoPresupuestoCapitado totalNivelAtencionGrupoServicio = new DtoTotalProcesoPresupuestoCapitado(contrato, convenio, fechaCierre);
							totalNivelAtencionGrupoServicio.inicializarParaTotalizar(ConstantesIntegridadDominio.acronimoTipoTotalNivelAtencionGrupoServicio);
							String keyMapaTotalNivelAtencionGrupoServicio = (contratos.getCodigo()+"_"+fechaCierre+"").trim()+"_"+ConstantesIntegridadDominio.acronimoTipoTotalNivelAtencionGrupoServicio;
							
							/* Totales Facturación de Servicios por Nivel de Atención y Grupo de Servicios */ 
							generarTotalPorServicioNivelAtencionGrupoServicio(mapaProcesoFacturacionServicio, keyMapaTotalNivelAtencionGrupoServicio, totalNivelAtencionGrupoServicio, factura);
						}
						
						else if(factura.getArticuloDetCargo() != null)
						{
							/* Totales Facturación de Medicamentos e Insumos por Nivel de Atención y Clase de Inventarios por cada articulo --- */
							DtoTotalProcesoPresupuestoCapitado totalNivelAtencionClaseInventarioArticulo = new DtoTotalProcesoPresupuestoCapitado(contrato, convenio, fechaCierre);
							totalNivelAtencionClaseInventarioArticulo.inicializarParaTotalizar(ConstantesIntegridadDominio.acronimoTipoTotalNivelAtencionClaseInventario);
							String keyMapaNivelAtencionClaseInventarioArticulo = (contratos.getCodigo()+"_"+fechaCierre+"").trim()+"_"+ConstantesIntegridadDominio.acronimoTipoTotalNivelAtencionClaseInventario;
							
							/* Totales Facturación de Medicamentos e Insumos por Nivel de Atención y Clase de Inventarios.  --- */
							generarTotalPorArticuloNivelAtencionClaseInventario(mapaProcesoFacturacionArticulo, keyMapaNivelAtencionClaseInventarioArticulo, totalNivelAtencionClaseInventarioArticulo, factura);
						}
					}
				}
			}
			fechaCierre = UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(fechaCierre), 1, false));
		}
		
		
		/* Se organizan los totales para asignarlos a las listas específicas */
		
		this.totalListaAgrupadaServicioNivelAtencionGrupoServicio 	= new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		this.totalListaAgrupadaArticuloNivelAtencionClaseInventario = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		
		for (String key : this.listaKeysMapaServicio)
		{
			DtoTotalProcesoPresupuestoCapitado total; total = new DtoTotalProcesoPresupuestoCapitado();
			total = mapaProcesoFacturacionServicio.get(key);
			this.totalListaAgrupadaServicioNivelAtencionGrupoServicio.add(total);
		}
		
		for (String key : this.listaKeysMapaArticulo)
		{
			DtoTotalProcesoPresupuestoCapitado total; total = new DtoTotalProcesoPresupuestoCapitado();
			total = mapaProcesoFacturacionArticulo.get(key);
			this.totalListaAgrupadaArticuloNivelAtencionClaseInventario.add(total);
		}
		
		
		return null; // si se quiere saber el resultado se debe hacer get de cada una de las listas generadas
	}
	
	
	
	
	/**
	 * Verifica si los médicamentos/insumos no tienen nivel de atención definido y genera una inconsistencia.
	 * 
	 * @author Cristhian Murillo.
	 * @param listaFacturas
	 */
	private ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> validarInconsistenciaNivelAtencion(
			DtoProcesoFacturacionPresupuestoCapitacion dtoProcesoFacturacionPresupuestoCapitacion)
	{
		DtoInconsistenciasProcesoPresupuestoCapitado dtoInconsistenciasProcesoPresupuestoCapitado;
		ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> listaInconsistencias = new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
		
		// Recorre la lista de facturas (detcargos) consultados y Verifica si se trajo algún artículo.
		if(dtoProcesoFacturacionPresupuestoCapitacion.getArticuloDetCargo() != null)
		{
			if(dtoProcesoFacturacionPresupuestoCapitacion.getNivelAtencionArticulo() == null)
			{
				// Si el artículo no tiene nivel de atención se genera una inconsistencia.
				dtoInconsistenciasProcesoPresupuestoCapitado = new DtoInconsistenciasProcesoPresupuestoCapitado();
				String articulo = dtoProcesoFacturacionPresupuestoCapitacion.getArticuloDetCargo()+" - "+dtoProcesoFacturacionPresupuestoCapitacion.getNombreArticuloDetCargo()+"";
				dtoInconsistenciasProcesoPresupuestoCapitado.setServicioMedicamento(articulo);
				dtoInconsistenciasProcesoPresupuestoCapitado.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoTipoInconsistenciaNoDefinidoNivelAtencion);
				String descripcion = "No está definido el nivel de atención para los medicamentos e insumos '"+articulo+"+'";
				dtoInconsistenciasProcesoPresupuestoCapitado.setDescripcion(descripcion);
				
				listaInconsistencias.add(dtoInconsistenciasProcesoPresupuestoCapitado);
			}
		}
		return listaInconsistencias;
	}
	
	
	
	
	/**
	 * Genera los totales por todos los Niveles de atencion y grupos del servicio
	 * @param mapaProcesoFacturacionServicio
	 * @param keyMapaTotalNivelAtencionGrupoServicio
	 * @param total
	 * @param factura
	 * 
	 * @author Cristhian Murillo
	 */
	private void generarTotalPorServicioNivelAtencionGrupoServicio(HashMap<String, DtoTotalProcesoPresupuestoCapitado> mapaProcesoFacturacionServicio, 
			String keyMapaTotalNivelAtencionGrupoServicio, DtoTotalProcesoPresupuestoCapitado total, DtoProcesoFacturacionPresupuestoCapitacion factura)
	{
		String keyMapaTotalNivelAtencionGrupoServicio_ = keyMapaTotalNivelAtencionGrupoServicio+"_"+factura.getServicioDetCargo();
		
		if(mapaProcesoFacturacionServicio.containsKey(keyMapaTotalNivelAtencionGrupoServicio_))
		{
			total =  mapaProcesoFacturacionServicio.get(keyMapaTotalNivelAtencionGrupoServicio_);
			total.setCantidadTotal(total.getCantidadTotal()+factura.getCantidadCargada());
			total.setValor(total.getValor() + factura.getTotalCantidadCargadaXvalorUnitarioCargado().doubleValue());
		}
		else{
			total.setCantidadTotal(factura.getCantidadCargada());
			total.setValor(factura.getTotalCantidadCargadaXvalorUnitarioCargado().doubleValue());
			total.setCodigoServicio(factura.getServicioDetCargo());
			total.setListaInconsistencias(validarInconsistenciaNivelAtencion(factura));	
		}
		
		if(!this.listaKeysMapaServicio.contains(keyMapaTotalNivelAtencionGrupoServicio_)){
			this.listaKeysMapaServicio.add(keyMapaTotalNivelAtencionGrupoServicio_);
		}
		
		mapaProcesoFacturacionServicio.put(keyMapaTotalNivelAtencionGrupoServicio_, total);
					
	}
	
	
	
	/**
	 * Genera los totales por todos los Niveles de atencion y grupos del servicio
	 * @param mapaProcesoFacturacionArticulo
	 * @param keyMapaNivelAtencionClaseInventarioArticulo
	 * @param total
	 * @param factura
	 * 
	 * @author Cristhian Murillo
	 */
	private void generarTotalPorArticuloNivelAtencionClaseInventario(HashMap<String, DtoTotalProcesoPresupuestoCapitado> mapaProcesoFacturacionArticulo, 
			String keyMapaNivelAtencionClaseInventarioArticulo, DtoTotalProcesoPresupuestoCapitado total, DtoProcesoFacturacionPresupuestoCapitacion factura)
	{
		String keyMapaNivelAtencionClaseInventarioArticulo_ = keyMapaNivelAtencionClaseInventarioArticulo+"_"+factura.getArticuloDetCargo();
					
		if(mapaProcesoFacturacionArticulo.containsKey(keyMapaNivelAtencionClaseInventarioArticulo_))
		{
			total =  mapaProcesoFacturacionArticulo.get(keyMapaNivelAtencionClaseInventarioArticulo_);
			total.setCantidadTotal(total.getCantidadTotal()+factura.getCantidadCargada());
			total.setValor(total.getValor() + factura.getTotalCantidadCargadaXvalorUnitarioCargado().doubleValue());
		}
		
		else{
			total.setCantidadTotal(factura.getCantidadCargada()); 
			total.setValor(factura.getTotalCantidadCargadaXvalorUnitarioCargado().doubleValue());
			total.setCodigoArticulo(factura.getArticuloDetCargo());
			total.setListaInconsistencias(validarInconsistenciaNivelAtencion(factura));
		}
		
		if(!this.listaKeysMapaArticulo.contains(keyMapaNivelAtencionClaseInventarioArticulo_)){
			this.listaKeysMapaArticulo.add(keyMapaNivelAtencionClaseInventarioArticulo_);
		}
		mapaProcesoFacturacionArticulo.put(keyMapaNivelAtencionClaseInventarioArticulo_, total);
	}

	

	/**
	 * @return valor de totalListaAgrupadaServicioNivelAtencionGrupoServicio
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaAgrupadaServicioNivelAtencionGrupoServicio() {
		return this.totalListaAgrupadaServicioNivelAtencionGrupoServicio;
	}


	/**
	 * @return valor de totalListaAgrupadaArticuloNivelAtencionClaseInventario
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaAgrupadaArticuloNivelAtencionClaseInventario() {
		return this.totalListaAgrupadaArticuloNivelAtencionClaseInventario;
	}
	
	
	
	

	/**
	 * Asigna la fecha de anulación a la fecha de la facturación y le coloca valores negativos, con el fin de que cuando este totalizando
	 * agrupe para esa fecha y reste tanto en cantidad como en valor.
	 * 
	 * @param listaFacturasAnuladas
	 *
	 * @autor Cristhian Murillo
	 */
	private void organizarFacturasAnuladasParaRestarTotal(ArrayList<DtoProcesoFacturacionPresupuestoCapitacion> listaFacturasAnuladas) 
	{
		for (DtoProcesoFacturacionPresupuestoCapitacion dtoProcesoFacturacionPresupuestoCapitacion : listaFacturasAnuladas) {
			if(dtoProcesoFacturacionPresupuestoCapitacion.getFechaAnulacion() != null){
				dtoProcesoFacturacionPresupuestoCapitacion.setFecha(dtoProcesoFacturacionPresupuestoCapitacion.getFechaAnulacion());
				dtoProcesoFacturacionPresupuestoCapitacion.setCantidadCargada(-dtoProcesoFacturacionPresupuestoCapitacion.getCantidadCargada());
				dtoProcesoFacturacionPresupuestoCapitacion.setTotalCantidadCargadaXvalorUnitarioCargado(dtoProcesoFacturacionPresupuestoCapitacion.getTotalCantidadCargadaXvalorUnitarioCargado().negate());
			}
		}
		
	}
	
}
