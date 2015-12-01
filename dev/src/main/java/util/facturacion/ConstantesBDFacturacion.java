/*
 * Abr 27, 2007
 */
package util.facturacion;

/**
 * Constantes para el modulo de facturación
 *
 * @author Sebastián Gómez
 */
public interface ConstantesBDFacturacion 
{
	/**=============CLASIFICACION TIPOS DE CONVENIO=========================================*/
	public static final int codigoClasTipoConvenioSOAT = 1;
	public static final int codigoClasTipoConvenioPOS = 2;
	public static final int codigoClasTipoConvenioEPS = 3;
	public static final int codigoClasTipoConvenioARP = 4;
	public static final int codigoClasTipoConvenioARS = 5;
	public static final int codigoClasTipoConvenioVinculado = 6;
	public static final int codigoClasTipoConvenioAseguradora = 7;
	public static final int codigoClasTipoConvenioEmpresarial = 8;
	public static final int codigoClasTipoConvenioParticular = 9;
	public static final int codigoClasTipoConvenioPrepagada = 10;
	public static final int codigoClasTipoConvenioEventoCatastrofico = 11;
	public static final int codigoClasTipoConvenioOtra = 12;
	
	/**=============SALDO INTERFAZ FACTURA=========================================*/
	public static final int codigoNoExisteInfoSaldoInterfaz = 0;
	public static final int codigoNoEsIgualInfoSaldoInterfaz = 1;
	public static final int codigoEsIgualInfoSaldoInterfaz = 2;
	
	
	/**=============REPORTE TOTALES SERVICIOS ARTICULOS VALORIZADOS=========================================*/
	public static final String servicioNivelAtencion = "servicioNivelAtencion";
	public static final String servicioGrupoServicio = "servicioGrupoServicio";
	public static final String servicioServicio = "servicioServicio";
	public static final String medicamentoInsumoNivelAtencion = "medicamentoInsumoNivelAtencion";
	public static final String medicamentoInsumoClaseInventario = "medicamentoInsumoClaseInventario";
	public static final String medicamentoInsumoMedicamentoInsumo = "medicamentoInsumoMedicamentoInsumo";
	
	/**=============TIPOS DE CONSULTA TOTAL SERVICIOS ARTICULOS VALORIZADOS=========================================*/
	public static final int codigoTipoConsultaOrdenado = 1;
	public static final int codigoTipoConsultaAutorizado = 2;
	public static final int codigoTipoConsultaCargado = 3;
	public static final int codigoTipoConsultaFacturado = 4;
	
}
