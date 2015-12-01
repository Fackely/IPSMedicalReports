package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoFiltroConsultaProcesoRipsEntidadesSub;
import com.princetonsa.dto.facturacion.DtoResultadoConsultaLogRipsEntidadesSub;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntidadesSubcontratadasDAO;
import com.servinte.axioma.orm.LogRipsEntidadesSub;
import com.servinte.axioma.orm.delegate.facturacion.LogRipsEntidadesSubcontratadasDelegate;

public class LogRipsEntidadesSubcontratadasHibernateDAO implements ILogRipsEntidadesSubcontratadasDAO{

	LogRipsEntidadesSubcontratadasDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public LogRipsEntidadesSubcontratadasHibernateDAO(){
		delegate = new LogRipsEntidadesSubcontratadasDelegate();
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
		return delegate.guardarLogRipsEntidadesSub(logRipsEntSub);
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
		return delegate.consultarRegistrosLogRipsEntidadesSub(dtoFiltroConsultaProcesoRips);
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
		return delegate.consultarRegistrosLogRipsEntidadesSubPorCodigoPk(codigoPkLogSeleccionado);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los
	 * registros de log del proceso de rips entidades subcontratadas
	 * por su codigo pk y el codigo pk del archivo seleccionado
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @param codigoPkLogArchivoSeleccionado codigo pk de log rips entidades sub archivo
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSubPorCodigoPkyArchivo(long codigoPkLogSeleccionado, long codigoPkLogArchivoSeleccionado){
		return delegate.consultarRegistrosLogRipsEntidadesSubPorCodigoPkyArchivo(codigoPkLogSeleccionado, codigoPkLogArchivoSeleccionado);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los
	 * registros log del proceso de rips entidades subcontratadas
	 * para mostrarlos en el detalle por autorizaciones
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSubPorNumerosAutorizacion(long codigoPkLogSeleccionado){
		return delegate.consultarRegistrosLogRipsEntidadesSubPorNumerosAutorizacion(codigoPkLogSeleccionado);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar un registro
	 * log del proceso de rips entidades subcontratadas
	 * por su codigopk, el codigo del archivo y el del registro
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @param codigoPkArchivo codigo pk de log rips entidades sub de archivo
	 * @param codigoPkRegistro codigo pk de log rips entidades sub de registro
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSubParaDetallePorNumeroAutorizacion(long codigoPkLogSeleccionado, long codigoPkArchivo, long codigoPkRegistro){
		return delegate.consultarRegistrosLogRipsEntidadesSubParaDetallePorNumeroAutorizacion(codigoPkLogSeleccionado, codigoPkArchivo, codigoPkRegistro);
	}
	
}
