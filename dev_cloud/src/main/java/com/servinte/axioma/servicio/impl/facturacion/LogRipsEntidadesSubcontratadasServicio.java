package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoFiltroConsultaProcesoRipsEntidadesSub;
import com.princetonsa.dto.facturacion.DtoFiltroProcesarRipsEntidadesSub;
import com.princetonsa.dto.facturacion.DtoResultadoConsultaLogRipsEntidadesSub;
import com.princetonsa.dto.facturacion.DtoResultadoProcesarRipsEntidadesSub;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntidadesSubcontratadasMundo;
import com.servinte.axioma.orm.LogRipsEntidadesSub;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntidadesSubcontratadasServicio;

public class LogRipsEntidadesSubcontratadasServicio implements ILogRipsEntidadesSubcontratadasServicio{

	ILogRipsEntidadesSubcontratadasMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabian Becerra
	 */
	public LogRipsEntidadesSubcontratadasServicio(){
		mundo = FacturacionFabricaMundo.crearLogRipsEntidadesSubMundo();
	}
	
	public DtoResultadoProcesarRipsEntidadesSub procesarRipsEntidadesSub(DtoFiltroProcesarRipsEntidadesSub dtoFiltro){
		return mundo.procesarRipsEntidadesSub(dtoFiltro);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * 
	 * @param logRipsEntSub log generado en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntidadesSub(LogRipsEntidadesSub logRipsEntSub){
		return mundo.guardarLogRipsEntidadesSub(logRipsEntSub);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los
	 * registros de log del proceso de rips entidades subcontratadas
	 * 
	 * @param dtoFiltroConsultaProcesoRips parámetros para la consulta
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSub(DtoFiltroConsultaProcesoRipsEntidadesSub dtoFiltroConsultaProcesoRips){
		return mundo.consultarRegistrosLogRipsEntidadesSub(dtoFiltroConsultaProcesoRips);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los
	 * registros de log del proceso de rips entidades subcontratadas
	 * por su codigo pk para ser mostrado en el detalle de las consultas
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSubPorCodigoPk(long codigoPkLogSeleccionado){
		return mundo.consultarRegistrosLogRipsEntidadesSubPorCodigoPk(codigoPkLogSeleccionado);
	}
	
	/**
	 * 
	 * Este Método se encarga de ordenar los
	 * registros de log del proceso de rips entidades subcontratadas
	 * para mostrar su detalle en la página de consulta
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @return DtoResultadoConsultaLogRipsEntidadesSub
	 * @author, Fabián Becerra
	 *
	 */
	public DtoResultadoConsultaLogRipsEntidadesSub ordenarRegistrosParaDetalleLogRips(long codigoPkLogSeleccionado){
		return mundo.ordenarRegistrosParaDetalleLogRips(codigoPkLogSeleccionado);
	}
	
	/**
	 * 
	 * Este Método se encarga de organizar la información de la consulta
	 * de log del proceso de rips entidades subcontratadas de un archivo
	 * por su codigo pk y el codigo pk del archivo seleccionado
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @param codigoPkLogArchivoSeleccionado codigo pk de log rips entidades sub archivo
	 * @return DtoResultadoConsultaLogRipsEntidadesSub
	 * @author, Fabián Becerra
	 *
	 */
	
	public DtoResultadoConsultaLogRipsEntidadesSub ordenarRegistrosLogRipsEntidadesSubPorArchivo(long codigoPkLogSeleccionado, long codigoPkLogArchivoSeleccionado){
		return mundo.ordenarRegistrosLogRipsEntidadesSubPorArchivo(codigoPkLogSeleccionado, codigoPkLogArchivoSeleccionado);
	}
	
	/**
	 * 
	 * Este Método se encarga de ordenar la consulta de
	 * registros log del proceso de rips entidades subcontratadas
	 * para mostrarlos en el detalle por autorizaciones
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @return DtoResultadoConsultaLogRipsEntidadesSub
	 * @author, Fabián Becerra
	 *
	 */
	public DtoResultadoConsultaLogRipsEntidadesSub ordenarConsultaRegistrosLogRipsEntidadesSubPorNumerosAutorizacion(long codigoPkLogSeleccionado){
		return mundo.ordenarConsultaRegistrosLogRipsEntidadesSubPorNumerosAutorizacion(codigoPkLogSeleccionado);
	}
	
	/**
	 * 
	 * Este Método se encarga de ordenar la consulta
	 * log del proceso de rips entidades subcontratadas
	 * por su codigopk, el codigo del archivo y el del registro
	 * para mostrarlo en el detalle por autorizaciones
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @param codigoPkArchivo codigo pk de log rips entidades sub de archivo
	 * @param codigoPkRegistro codigo pk de log rips entidades sub de registro
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	public DtoResultadoConsultaLogRipsEntidadesSub ordenarConsultaRegistrosLogRipsEntidadesSubParaDetallePorNumeroAutorizacion(long codigoPkLogSeleccionado, long codigoPkArchivo, long codigoPkRegistro){
		return mundo.ordenarConsultaRegistrosLogRipsEntidadesSubParaDetallePorNumeroAutorizacion(codigoPkLogSeleccionado, codigoPkArchivo, codigoPkRegistro);
	}
		
}
