/*
 * Julio 21, 2009
 */
package util.interfaz;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.UtilidadesInterfazDao;
import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.dto.administracion.DtoTiposRetencion;
import com.princetonsa.dto.facturacion.DtoConceptoRetencionTercero;
import com.princetonsa.dto.interfaz.DtoCuentaContable;
import com.princetonsa.dto.interfaz.DtoInterfazDatosDocumentoS1E;
import com.princetonsa.dto.interfaz.DtoInterfazS1EInfo;
import com.princetonsa.dto.interfaz.DtoRetencion;
import com.princetonsa.enu.facturacion.TipoAplicacionEnum;
import com.princetonsa.mundo.InstitucionBasica;



/**
 * Clase par ael manejo de los métodos centralizados
 * del módulo de interfaz
 * @author Sebastián Gómez R.
 *
 */
public class UtilidadesInterfaz 
{
	/**
	 * Manejador de log de la clase
	 */
	private static Logger logger=Logger.getLogger(UtilidadesInterfaz.class);
	
	
	
	public static UtilidadesInterfazDao utilidadDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesInterfazDao();
	}

	
	/**
	 * Método para consultar los datos de la cuenta contable de la interfaz convenio
	 * @param con
	 * @param codigoConvenio
	 * @param tipoRegimen
	 * @param tipoCuenta
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContableInterfazConvenio(Connection con,int codigoConvenio,String tipoRegimen,int tipoCuenta,boolean cuentaContable,boolean cuentaCxCapitacion)
	{
		return utilidadDao().consultarCuentaContableInterfazConvenio(con, codigoConvenio, tipoRegimen, tipoCuenta,cuentaContable,cuentaCxCapitacion);
	}
	
	/**
	 * Método para obtener la informacion de la cuenta contable de cuenta interfaz unidad funcional
	 * @param con
	 * @param codigoCentroCosto
	 * @param cuentaIngreso
	 * @param cuentaMedicamento
	 * @param cuentaIngresoVigAnterior
	 * @param cuentaMedVigAnterior
	 * @param cuentaContableCosto
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContableInterfazUnidadFuncional(Connection con,int codigoCentroCosto,boolean cuentaIngreso,boolean cuentaMedicamento, boolean cuentaIngresoVigAnterior,boolean cuentaMedVigAnterior,boolean cuentaContableCosto)
	{
		return utilidadDao().consultarCuentaContableInterfazUnidadFuncional(con, codigoCentroCosto, cuentaIngreso, cuentaMedicamento, cuentaIngresoVigAnterior, cuentaMedVigAnterior, cuentaContableCosto);
	}
	
	/**
	 * Método para consultar la cuenta contable de la ionterfaz inventarios
	 * @param con
	 * @param codigoArticulo
	 * @param codigoCentroCosto
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContableInterfazInventarios(Connection con,int codigoArticulo,int codigoCentroCosto)
	{
		return utilidadDao().consultarCuentaContableInterfazInventarios(con, codigoArticulo, codigoCentroCosto);
	}
	/**
	 * Método para consultar la cuenta contabl de la interfaz de servicio
	 * @param con
	 * @param codigoServicio
	 * @param codigoCentroCosto
	 * @param cuentaIngreso
	 * @param cuentaIngresoVigAnterior
	 * @param cuentaContableCosto
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContableInterfazServicio(Connection con,int codigoServicio,int codigoCentroCosto,boolean cuentaIngreso,boolean cuentaIngresoVigAnterior,boolean cuentaContableCosto)
	{
		return utilidadDao().consultarCuentaContableInterfazServicio(con, codigoServicio, codigoCentroCosto, cuentaIngreso, cuentaIngresoVigAnterior, cuentaContableCosto);
	}
	
	/**
	 * Método que consulta la cuenta contable de la parametriacion de subgrupo/grupo/clase
	 * @param con
	 * @param codigoArticulo
	 * @param cuentaCosto
	 * @param cuentaInventario
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContableSubgrupoGrupoClase(Connection con,int codigoArticulo,boolean cuentaCosto,boolean cuentaInventario)
	{
		return utilidadDao().consultarCuentaContableSubgrupoGrupoClase(con, codigoArticulo, cuentaCosto, cuentaInventario);
	}
	
	/**
	 * Método para consultar los datos de la cuenta contable
	 * @param con
	 * @param cuentaContable
	 * @return
	 */
	public static DtoCuentaContable consultarDatosCuentaContable(Connection con,DtoCuentaContable cuentaContable)
	{
		return utilidadDao().consultarDatosCuentaContable(con, cuentaContable);
	}
	
	/**
	 * Método para consultar la cuenta contable de cuenta interfaz inventario ingreso
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoArticulo
	 * @param cuentaIngreso
	 * @param cuentaIngresoVigAnterior
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContableInterfazInvIngreso(Connection con,int codigoCentroCosto,int codigoArticulo,boolean cuentaIngreso,boolean cuentaIngresoVigAnterior)
	{
		return utilidadDao().consultarCuentaContableInterfazInvIngreso(con, codigoCentroCosto, codigoArticulo, cuentaIngreso, cuentaIngresoVigAnterior);
	}
	
	/**
	 * Método para calcular la retencion x interfaz
	 * @param con
	 * @param documentos
	 * @param parametrizacion
	 * @param nitTercero
	 * @param retencion
	 * @return
	 */
	public static DtoRetencion calcularRetencionInterfaz(Connection con,ArrayList<DtoInterfazDatosDocumentoS1E> documentos,DtoInterfazS1EInfo parametrizacion,String nitTercero, boolean retencion,double valorBase)
	{
		DtoRetencion retenciones = new DtoRetencion();
		int codigoTipoDocumento = Utilidades.convertirAEntero(documentos.get(0).getTipoDocumento());
		String claseDocumento = "";
		String numeroDocumento = "";
		String fechaDocumento = "";
		double valorDocumento = 0;
		ArrayList<DtoConceptoRetencionTercero> conceptosTercero = new ArrayList<DtoConceptoRetencionTercero>();
		
		
		
		
		//IDENTIFICAR LA CLASE DE DOCUMENTO****************************************************
		switch(codigoTipoDocumento)
		{
			case ConstantesBD.codigoTipoDocInteFacturaPaciente:
			case ConstantesBD.codigoTipoDocInteAnulaFacturaPaciente:
			case ConstantesBD.codigoTipoDocInteAjustesFactPaciente:
				//retencion
				if(retencion)
				{
					claseDocumento = ConstantesIntegridadDominio.acronimoDocHonorariosFacturasPacientes;
				}
				//autoretencion
				else
				{
					claseDocumento = ConstantesIntegridadDominio.acronimoDocFacturasPacientes;
				}
				numeroDocumento = documentos.size()>1?documentos.get(1).getNumeroDocumento():documentos.get(0).getNumeroDocumento();
				fechaDocumento = documentos.size()>1?documentos.get(1).getFecha():documentos.get(0).getFecha();
				valorDocumento = Utilidades.convertirADouble(documentos.size()>1?documentos.get(1).getValor():documentos.get(0).getValor(), true);
			break;
			case ConstantesBD.codigoTipoDocInteAutoServicioEntSub:
			case ConstantesBD.codigoTipoDocInteAnulaAutorServicioEntSub:
				claseDocumento = ConstantesIntegridadDominio.acronimoDocServiciosAutorizacionEntidadesSub;
				numeroDocumento = documentos.get(0).getNumeroDocumento();
				fechaDocumento = documentos.get(0).getFecha();
				valorDocumento = Utilidades.convertirADouble(documentos.get(0).getValor(), true);
			break;
			case ConstantesBD.codigoTipoDocInteDespachoMed:
				claseDocumento = ConstantesIntegridadDominio.acronimoDocDespachoMedEntidadesSubcontratadas;
				numeroDocumento = documentos.get(1).getNumeroDocumento();
				fechaDocumento = documentos.get(0).getFecha();
				valorDocumento = Utilidades.convertirADouble(documentos.get(0).getValor(), true);
			break;
			case ConstantesBD.codigoTipoDocInteDevolucionMedi:
			case ConstantesBD.codigoTipoDocInteDespaPedidoInsumo:
			case ConstantesBD.codigoTipoDocInteDevolPedidoInsumo:
			case ConstantesBD.codigoTipoDocInteDespachoPedidoQx:
			case ConstantesBD.codigoTipoDocInteDevolucionPedidoQx:
			case ConstantesBD.codigoTipoDocInteCargosDirectosArt:
			case ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo:
				claseDocumento = ConstantesIntegridadDominio.acronimoDocDespachoMedEntidadesSubcontratadas;
				numeroDocumento = documentos.get(0).getNumeroDocumento();
				fechaDocumento = documentos.get(0).getFecha();
				valorDocumento = Utilidades.convertirADouble(documentos.get(0).getValor(), true);
			break;
			case ConstantesBD.codigoTipoDocInteFacturasVarias:
			case ConstantesBD.codigoTipoDocInteAnulaFacturasVarias:
				claseDocumento = ConstantesIntegridadDominio.acronimoDocFacturasVarias;
				numeroDocumento = documentos.size()>1?documentos.get(1).getNumeroDocumento():documentos.get(0).getNumeroDocumento();
				fechaDocumento = documentos.size()>1?documentos.get(1).getFecha():documentos.get(0).getFecha();
				valorDocumento = Utilidades.convertirADouble(documentos.size()>1?documentos.get(1).getValor():documentos.get(0).getValor(), true);
			break;
			case ConstantesBD.codigoTipoDocInteCCCapitacion:
			case ConstantesBD.codigoTipoDocInteAjustesCCCapitacion:
				claseDocumento = ConstantesIntegridadDominio.acronimoDocCuentasCobroCapitacion;
				numeroDocumento = documentos.get(0).getNumeroDocumento();
				fechaDocumento = documentos.get(0).getFecha();
				valorDocumento = Utilidades.convertirADouble(documentos.get(0).getValor(), true);
			break;
				
		}
		//************************************************************************************
		
		//************SEGUN CLASE DOCUMENTO***************************************************
		// --------------- PROCESOS DE RETENCION ----------------------------------------------------
		if(claseDocumento.equals(ConstantesIntegridadDominio.acronimoDocHonorariosFacturasPacientes))
		{
			//Método usado para la validación de integral
			ResultadoBoolean mismoGrupoServicio = new ResultadoBoolean(false,"");
			
			//Se verifica parámetro interfaz S1E
			if(UtilidadTexto.getBoolean(parametrizacion.getRealizarCalRetenCxh()))
			{
				mismoGrupoServicio = utilidadDao().validacionMismoGrupoServicioFactura(con, numeroDocumento,true);
				
				if(documentos.get(0).getNumeroDocumento().equals("10546"))
					logger.info("¿Mismo grupo servicio?: "+mismoGrupoServicio);
				
				////Se verifica que al menos se hay encontrado un grupo 
				if(!mismoGrupoServicio.getDescripcion().equals(""))
				{
					conceptosTercero = procesoRetencionInicial(
						con, 
						!mismoGrupoServicio.isTrue(), 
						Utilidades.convertirAEntero(parametrizacion.getInstitucionBasica().getCodigo()), 
						ConstantesBD.codigoNuncaValido, 
						Utilidades.convertirAEntero(mismoGrupoServicio.getDescripcion()), 
						ConstantesBD.codigoNuncaValido, 
						nitTercero, 
						ConstantesIntegridadDominio.acronimoDocHonorariosFacturasPacientes, 
						ConstantesIntegridadDominio.acronimoConceptoRetencionIntegralClaseDoc,
						true, //retencion
						retenciones,
						documentos.get(0).getNumeroDocumento(),
						fechaDocumento
						);
					if(documentos.get(0).getNumeroDocumento().equals("10546"))
						logger.info("Nro de conceptos de tercero: "+conceptosTercero.size());
					
					if(conceptosTercero.size()>0)
					{
						retenciones = utilidadDao().consultarTarifaRetencion(
							con, 
							conceptosTercero, 
							!mismoGrupoServicio.isTrue(), 
							Utilidades.convertirAEntero(mismoGrupoServicio.getDescripcion()), 
							ConstantesBD.codigoNuncaValido, //clase 
							ConstantesBD.codigoNuncaValido,  //concepto factura varia
							fechaDocumento, 
							valorBase);
						retenciones.setRetencion(true); //se define que es retencion
						
						if(documentos.get(0).getNumeroDocumento().equals("10546"))
							logger.info("¿valor total de la retencion?: "+retenciones.consultarTotalValorRetencion());
					}
				}
				else
				{
					retenciones.getInconsistenciasRetencion().add("Problemas verificando el detalle de factura para calculo de retencion");
				}
			}
				
				
			
			
		}
		else if(claseDocumento.equals(ConstantesIntegridadDominio.acronimoDocServiciosAutorizacionEntidadesSub))
		{
			//Método usado para la validación de integral
			ResultadoBoolean mismoGrupoServicio = new ResultadoBoolean(false,"");
			
			//Se verifica parámetro interfaz S1E
			if(UtilidadTexto.getBoolean(parametrizacion.getRealizarCalRestenCxes()))
			{
				mismoGrupoServicio = utilidadDao().validacionMismoGrupoServicioAutorizacionEntidadSub(con, numeroDocumento);
				////Se verifica que al menos se hay encontrado un grupo 
				if(!mismoGrupoServicio.getDescripcion().equals(""))
				{
					conceptosTercero = procesoRetencionInicial(
						con, 
						!mismoGrupoServicio.isTrue(), 
						Utilidades.convertirAEntero(parametrizacion.getInstitucionBasica().getCodigo()), 
						ConstantesBD.codigoNuncaValido, 
						Utilidades.convertirAEntero(mismoGrupoServicio.getDescripcion()), 
						ConstantesBD.codigoNuncaValido, 
						nitTercero, 
						ConstantesIntegridadDominio.acronimoDocServiciosAutorizacionEntidadesSub, 
						ConstantesIntegridadDominio.acronimoConceptoRetencionIntegralClaseDoc,
						true, //retencion
						retenciones,
						documentos.get(0).getNumeroDocumento(),
						fechaDocumento
						);
					
					if(conceptosTercero.size()>0)
					{
						retenciones = utilidadDao().consultarTarifaRetencion(
							con, 
							conceptosTercero, 
							!mismoGrupoServicio.isTrue(), 
							Utilidades.convertirAEntero(mismoGrupoServicio.getDescripcion()), 
							ConstantesBD.codigoNuncaValido, //clase 
							ConstantesBD.codigoNuncaValido,  //concepto factura varia
							fechaDocumento, 
							valorBase);
						retenciones.setRetencion(true); //se define que es retencion
					}
				}
				else
				{
					retenciones.getInconsistenciasRetencion().add("Problemas verificando el detalle de autorizacion para calculo de retencion");
				}
			}
		}
		else if(claseDocumento.equals(ConstantesIntegridadDominio.acronimoDocDespachoMedEntidadesSubcontratadas))
		{
			//Método usado para validacion de integral
			ResultadoBoolean mismaClaseInventario = new ResultadoBoolean(false,"");
			
			///Se verifica parámetro interfaz S1E
			if(UtilidadTexto.getBoolean(parametrizacion.getRealizarCalRetenCxda()))
			{
				mismaClaseInventario = utilidadDao().validacionMismaClaseDespachoMedicamentos(con, numeroDocumento, codigoTipoDocumento);
				////Se verifica que al menos se hay encontrado una clase 
				if(!mismaClaseInventario.getDescripcion().equals(""))
				{
					conceptosTercero = procesoRetencionInicial(
						con, 
						!mismaClaseInventario.isTrue(), 
						Utilidades.convertirAEntero(parametrizacion.getInstitucionBasica().getCodigo()), 
						Utilidades.convertirAEntero(mismaClaseInventario.getDescripcion()), 
						ConstantesBD.codigoNuncaValido, 
						ConstantesBD.codigoNuncaValido, 
						nitTercero, 
						ConstantesIntegridadDominio.acronimoDocDespachoMedEntidadesSubcontratadas, 
						ConstantesIntegridadDominio.acronimoConceptoRetencionIntegralClaseDoc,
						true, //retencion
						retenciones,
						documentos.get(0).getNumeroDocumento(),
						fechaDocumento
						);
					
					if(conceptosTercero.size()>0)
					{
						retenciones = utilidadDao().consultarTarifaRetencion(
							con, 
							conceptosTercero, 
							!mismaClaseInventario.isTrue(),
							ConstantesBD.codigoNuncaValido, //grupo
							Utilidades.convertirAEntero(mismaClaseInventario.getDescripcion()),
							ConstantesBD.codigoNuncaValido,  //concepto factura varia
							fechaDocumento, 
							valorBase);
						retenciones.setRetencion(true); //se define que es retencion
					}
				}
				else
				{
					retenciones.getInconsistenciasRetencion().add("Problemas verificando el detalle de autorizacion para calculo de retencion");
				}
			}
			
		}
		//-------------------- PROCESOS DE AUTORETENCION ---------------------------------------------------
		else if(claseDocumento.equals(ConstantesIntegridadDominio.acronimoDocFacturasPacientes))
		{
			//Método usado para la validación de integral
			ResultadoBoolean mismoGrupoServicio = new ResultadoBoolean(false,"");
			ResultadoBoolean mismaClaseInventario = new ResultadoBoolean(false,"");
			
			if(documentos.get(0).getNumeroDocumento().equals("10546"))
				logger.info("¿Validacion fctura paciente autorretencion? "+utilidadDao().validacionFacturaPacienteAutoretencion(con, numeroDocumento).isTrue());
			//Se verifica parámetro interfaz S1E y que la factura sea válida para el calculo de esta clase de documento
			if(UtilidadTexto.getBoolean(parametrizacion.getRealizarCalAutoretFp())&&
				utilidadDao().validacionFacturaPacienteAutoretencion(con, numeroDocumento).isTrue())
			{
				//Método para tomar el numero de servicios [0] y numero de articulos [1]
				int[] numServArt = utilidadDao().consultarNumServiciosYArticulosFactura(con, numeroDocumento);
				
				if(documentos.get(0).getNumeroDocumento().equals("10546"))
				{
					logger.info("numero de servicios: "+numServArt[0]);
					logger.info("numero de articulos: "+numServArt[1]);
				}
				//La factura solo tiene servicios
				if(numServArt[0]>0 && numServArt[1] == 0)
				{
					mismoGrupoServicio = utilidadDao().validacionMismoGrupoServicioFactura(con, numeroDocumento,false);
					if(documentos.get(0).getNumeroDocumento().equals("10546"))
						logger.info("¿mismo grupo servico? "+mismoGrupoServicio.isTrue());
					////Se verifica que al menos se hay encontrado un grupo 
					if(!mismoGrupoServicio.getDescripcion().equals(""))
					{
						conceptosTercero = procesoRetencionInicial(
							con, 
							!mismoGrupoServicio.isTrue(), 
							Utilidades.convertirAEntero(parametrizacion.getInstitucionBasica().getCodigo()), 
							ConstantesBD.codigoNuncaValido, 
							Utilidades.convertirAEntero(mismoGrupoServicio.getDescripcion()), 
							ConstantesBD.codigoNuncaValido, 
							nitTercero, 
							ConstantesIntegridadDominio.acronimoDocFacturasPacientes, 
							ConstantesIntegridadDominio.acronimoConceptoAutoretencionIntegralClaseDoc,
							false, //retencion
							retenciones,
							documentos.get(0).getNumeroDocumento(),
							fechaDocumento
							);
						if(documentos.get(0).getNumeroDocumento().equals("10546"))
							logger.info("conceptosTercero: "+conceptosTercero.size());
						
						if(conceptosTercero.size()>0)
						{
							retenciones = utilidadDao().consultarTarifaRetencion(
								con, 
								conceptosTercero, 
								!mismoGrupoServicio.isTrue(), 
								Utilidades.convertirAEntero(mismoGrupoServicio.getDescripcion()), 
								ConstantesBD.codigoNuncaValido, //clase 
								ConstantesBD.codigoNuncaValido,  //concepto factura varia
								fechaDocumento, 
								valorDocumento);
							retenciones.setRetencion(false); //se define que es autoretencion
						}
					}
					else
					{
						retenciones.getInconsistenciasRetencion().add("Problemas verificando el detalle de servicios factura para calculo de retencion");
					}
				}
				//La factura solo tiene articulos
				else if(numServArt[0]==0 && numServArt[1] > 0)
				{
					mismaClaseInventario = utilidadDao().validacionMismaClaseFactura(con, numeroDocumento);
					
					if(documentos.get(0).getNumeroDocumento().equals("10546"))
						logger.info("¿misma clase inventario? "+mismaClaseInventario.isTrue());
					////Se verifica que al menos se hay encontrado una clase 
					if(!mismaClaseInventario.getDescripcion().equals(""))
					{
						conceptosTercero = procesoRetencionInicial(
							con, 
							!mismaClaseInventario.isTrue(), 
							Utilidades.convertirAEntero(parametrizacion.getInstitucionBasica().getCodigo()), 
							Utilidades.convertirAEntero(mismaClaseInventario.getDescripcion()), 
							ConstantesBD.codigoNuncaValido, //codigo grupo
							ConstantesBD.codigoNuncaValido, //codigo concepto
							nitTercero, 
							ConstantesIntegridadDominio.acronimoDocFacturasPacientes, 
							ConstantesIntegridadDominio.acronimoConceptoAutoretencionIntegralClaseDoc,
							false, //retencion
							retenciones,
							documentos.get(0).getNumeroDocumento(),
							fechaDocumento
							);
						if(documentos.get(0).getNumeroDocumento().equals("10546"))
							logger.info("conceptosTercero: "+conceptosTercero.size());
						if(conceptosTercero.size()>0)
						{
							retenciones = utilidadDao().consultarTarifaRetencion(
								con, 
								conceptosTercero, 
								!mismaClaseInventario.isTrue(),
								ConstantesBD.codigoNuncaValido, //grupo
								Utilidades.convertirAEntero(mismaClaseInventario.getDescripcion()),
								ConstantesBD.codigoNuncaValido,  //concepto factura varia
								fechaDocumento, 
								valorDocumento);
							retenciones.setRetencion(false); //se define que es autoretencion
						}
					}
					else
					{
						retenciones.getInconsistenciasRetencion().add("Problemas verificando el detalle de articulos factura para calculo de retencion");
					}
				}
				//La factura tiene servicios y articulos
				else
				{
					if(documentos.get(0).getNumeroDocumento().equals("10546"))
						logger.info("¡tiene articulos como servicios!");
					conceptosTercero = procesoRetencionInicial(
							con, 
							true, 
							Utilidades.convertirAEntero(parametrizacion.getInstitucionBasica().getCodigo()), 
							ConstantesBD.codigoNuncaValido, // codigo clase 
							ConstantesBD.codigoNuncaValido, //codigo grupo
							ConstantesBD.codigoNuncaValido, //codigo concepto
							nitTercero, 
							ConstantesIntegridadDominio.acronimoDocFacturasPacientes, 
							ConstantesIntegridadDominio.acronimoConceptoAutoretencionIntegralClaseDoc,
							false, //retencion
							retenciones,
							documentos.get(0).getNumeroDocumento(),
							fechaDocumento
							);
					if(documentos.get(0).getNumeroDocumento().equals("10546"))
						logger.info("conceptosTercero: "+conceptosTercero.size());
						if(conceptosTercero.size()>0)
						{
							retenciones = utilidadDao().consultarTarifaRetencion(
								con, 
								conceptosTercero, 
								true,
								ConstantesBD.codigoNuncaValido, //grupo
								ConstantesBD.codigoNuncaValido, //clase
								ConstantesBD.codigoNuncaValido,  //concepto factura varia
								fechaDocumento, 
								valorDocumento);
							retenciones.setRetencion(false); //se define que es autoretencion
						}
				}
				
				
				
				
				
				
				
				
			}
			
		}
		else if(claseDocumento.equals(ConstantesIntegridadDominio.acronimoDocFacturasVarias))
		{
			//Método usado para la validación de integral
			ResultadoBoolean mismoConceptoFV = new ResultadoBoolean(false,"");
			
			
			//Se verifica parámetro interfaz S1E 
			if(UtilidadTexto.getBoolean(parametrizacion.getRealizarCalAutoretFv()))
			{
				mismoConceptoFV = utilidadDao().validacionMismoConceptoFacturaVaria(con, numeroDocumento);
				////Se verifica que al menos se hay encontrado un concepto
				if(!mismoConceptoFV.getDescripcion().equals(""))
				{
					conceptosTercero = procesoRetencionInicial(
						con, 
						!mismoConceptoFV.isTrue(), 
						Utilidades.convertirAEntero(parametrizacion.getInstitucionBasica().getCodigo()), 
						ConstantesBD.codigoNuncaValido, 
						ConstantesBD.codigoNuncaValido,
						Utilidades.convertirAEntero(mismoConceptoFV.getDescripcion()),
						nitTercero, 
						ConstantesIntegridadDominio.acronimoDocFacturasVarias, 
						ConstantesIntegridadDominio.acronimoConceptoAutoretencionIntegralClaseDoc,
						false, //retencion
						retenciones,
						documentos.get(0).getNumeroDocumento(),
						fechaDocumento
						);
					
					if(conceptosTercero.size()>0)
					{
						retenciones = utilidadDao().consultarTarifaRetencion(
							con, 
							conceptosTercero, 
							!mismoConceptoFV.isTrue(), 
							ConstantesBD.codigoNuncaValido,  //grupo 
							ConstantesBD.codigoNuncaValido, //clase 
							
							Utilidades.convertirAEntero(mismoConceptoFV.getDescripcion()),
							fechaDocumento, 
							valorDocumento);
						retenciones.setRetencion(false); //se define que es retencion
					}
				}
				else
				{
					retenciones.getInconsistenciasRetencion().add("Problemas verificando el detalle de factura varia para calculo de retencion");
				}
			}
		}
		else if(claseDocumento.equals(ConstantesIntegridadDominio.acronimoDocCuentasCobroCapitacion))
		{
			//Se verifica parámetro interfaz S1E 
			if(UtilidadTexto.getBoolean(parametrizacion.getRealizarCalAutoretCxCC()))
			{
				conceptosTercero = procesoRetencionInicial(
					con, 
					true, 
					Utilidades.convertirAEntero(parametrizacion.getInstitucionBasica().getCodigo()), 
					ConstantesBD.codigoNuncaValido, // codigo clase 
					ConstantesBD.codigoNuncaValido, //codigo grupo
					ConstantesBD.codigoNuncaValido, //codigo concepto
					nitTercero, 
					ConstantesIntegridadDominio.acronimoDocCuentasCobroCapitacion, 
					ConstantesIntegridadDominio.acronimoConceptoAutoretencionIntegralClaseDoc,
					false, //retencion
					retenciones,
					documentos.get(0).getNumeroDocumento(),
					fechaDocumento
					);
				
				if(conceptosTercero.size()>0)
				{
					retenciones = utilidadDao().consultarTarifaRetencion(
						con, 
						conceptosTercero, 
						true,
						ConstantesBD.codigoNuncaValido, //grupo
						ConstantesBD.codigoNuncaValido, //clase
						ConstantesBD.codigoNuncaValido,  //concepto factura varia
						fechaDocumento, 
						valorDocumento);
					retenciones.setRetencion(false); //se define que es autoretencion
				}
			}
		}
		//*************************************************************************************
		
		//*************VALIDACION DE CONCEPTOS CON RETENCION************************************
		//Se verifica cuales conceptos quedan con retencion
		for(DtoConceptoRetencionTercero concepto:retenciones.getConceptos())
		{
			if(documentos.get(0).getNumeroDocumento().equals("10546"))
			{
				logger.info("calculo de retencion exitoso? "+concepto.isCalculoRetencionExitoso());
				logger.info("cancelar retencion? "+concepto.isCancelarRetencion());
			}
			
			if(!concepto.isCalculoRetencionExitoso()&&!concepto.isCancelarRetencion())
			{
				retenciones.getInconsistenciasRetencion().add("El concepto de retencion: "+concepto.getDescripcion()+" no tiene parametrizacion para calcular tarifa");
			}
		}
		//****************************************************************************************
		
		retenciones.insertarConceptosConRetencion();
		return retenciones;
	}
	
	/**
	 * Método para hacer el proceso de retencion inicial
	 * @param con
	 * @param integral
	 * @param codigoInstitucion
	 * @param codigoClase
	 * @param codigoGrupo
	 * @param codigoConcepto
	 * @param nitTercero
	 * @param claseDocumento
	 * @param seccion
	 * @param retenciones 
	 * @param numeroDocumento 
	 * @param fechaDocumento 
	 * @return
	 */
	private static ArrayList<DtoConceptoRetencionTercero>  procesoRetencionInicial(Connection con,boolean integral,int codigoInstitucion,int codigoClase,int codigoGrupo,int codigoConcepto,String nitTercero,String claseDocumento,String seccion,boolean retencion, DtoRetencion retenciones, String numeroDocumento, String fechaDocumento)
	{
		ArrayList<DtoTiposRetencion> tiposRetencion = new ArrayList<DtoTiposRetencion>();
		ArrayList<DtoConceptosRetencion> conceptos = new ArrayList<DtoConceptosRetencion>();
		ArrayList<DtoConceptoRetencionTercero> conceptosTercero = new ArrayList<DtoConceptoRetencionTercero>();
		ArrayList<DtoConceptoRetencionTercero> conceptosDefinitivos = new ArrayList<DtoConceptoRetencionTercero>();
		boolean encontroInformacion = false;
		
		if(numeroDocumento.equals("10546"))
			logger.info("¿integral? "+integral);
		if(!integral)
		{
			//NO INTEGRAL
			//Se consultan los tipos de retencion qeu aplican para el grupo de servicio encontraro
			if(numeroDocumento.equals("10546"))
				logger.info("codigoGrupo servicio: "+codigoGrupo);
			tiposRetencion = utilidadDao().consultarTiposRetencionXClaseGrupoConceptoFV(con, codigoInstitucion, codigoClase, codigoGrupo, codigoConcepto);
			if(numeroDocumento.equals("10546"))
				logger.info("tipos retencion encontrados: "+tiposRetencion.size());
			if(tiposRetencion.size()>0)
			{
				encontroInformacion = true;
				conceptosTercero = utilidadDao().consultarConceptosRetencionXTercero(con, tiposRetencion, new ArrayList<DtoConceptosRetencion>(), nitTercero, false,fechaDocumento);
				if(numeroDocumento.equals("10546"))
					logger.info("conceptos retencion de tercero encontrados? "+conceptosTercero.size());
			}
		}
		else
		{
			//INTEGRAL
			//se consultan los conceptos parametrizados en interfaz sistema 1E por la clase de documento que aplica
			conceptos = utilidadDao().consultarConceptosRetencionParamS1E(con, claseDocumento, seccion, codigoInstitucion);
			if(numeroDocumento.equals("10546"))
				logger.info("conceptos encontrados: "+conceptos.size());
			if(conceptos.size()>0)
			{
				encontroInformacion = true;
				conceptosTercero = utilidadDao().consultarConceptosRetencionXTercero(con, new ArrayList<DtoTiposRetencion>(), conceptos, nitTercero, true, fechaDocumento);
				if(numeroDocumento.equals("10546"))
					logger.info("conceptos retencion de tercero encontrados? "+conceptosTercero.size());
			}
			
			
		}
		
		if(numeroDocumento.equals("10546"))
			logger.info("¿Encontró informacion?"+encontroInformacion);
		if(encontroInformacion)
		{
			if(conceptosTercero.size()==0)
			{
				retenciones.getInconsistenciasRetencion().add("No se encontraron conceptos de retencion "+(integral?"integral":"no integral")+" para el tercero: "+nitTercero+". Funcionalidad Facturacion / Mantenimiento / Convenios / Terceros");
			}
			else
			{
				//VALIDACION RETENCION
				if(retencion)
				{
					for(DtoConceptoRetencionTercero conceptoTercero:conceptosTercero)
					{
						if(conceptoTercero.getTipoAplicacion().getCodigo()==TipoAplicacionEnum.SUJETO_A_RETENCION.getPosicion())
						{
							conceptosDefinitivos.add(conceptoTercero);
						}
					}
				}
				//VALIDACION AUTORETENCION
				else
				{
					for(DtoConceptoRetencionTercero conceptoTercero:conceptosTercero)
					{
						if(conceptoTercero.isIndicativoAgenteRetenedor())
						{
							//logger.info("!Teiene indicativo de agente retendor¡");
							conceptosDefinitivos.add(conceptoTercero);
						}
					}
				}
			}
		}
		
			
		return conceptosDefinitivos;
	
	}
	
}
