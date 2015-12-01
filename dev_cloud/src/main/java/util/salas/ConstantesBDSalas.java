/*
 * Febrero 11, 2008
 */
package util.salas;

/**
 * 
 * @author Sebasti�n G�mez R.
 *	Clase para manejar las constantes del m�dulo de Salas
 */
public class ConstantesBDSalas 
{

	//***************************************************************************
	//***************CONSTANTES DE LOS TIPOS DE CIRUGIA***************************
	//*****************************************************************************
	public static final String acronimoTipoCirugiaPrincipalPrincipal = "PP";
	public static final String acronimoTipoCirugiaPrincipalBilateral = "PB";
	public static final String acronimoTipoCirugiaMayorAdicional = "MA";
	public static final String acronimoTipoCirugiaAdicionalAdicional = "AA";
	public static final String acronimoTipoCirugiaMayorBilateral = "MB";
	public static final String acronimoTipoCirugiaAdicionalBilateral = "AB";
	
	//***************************************************************************
	//***************CONSTANTES TIPOS DE LIQUIDACION SERVICIOS*********************
	//*****************************************************************************
	/**
	 * Nota estas constanters no est�n en base de datos sino que son �tiles para el proceso
	 * de la liquidacion de servicios
	 */
	public static final int codigoLiquidacionSoat = 1;
	public static final int codigoLiquidacionIss = 2;
	public static final int codigoLiquidacionRangoTiempos = 3;
	public static final int codigoLiquidacionMixta = 4;
	public static final int codigoLiquidacionTarifa = 5;
	
	//***************************************************************************
	//***************CONSTANTES INDICATIVO REGISTRO ******************************
	//*****************************************************************************
	public static final String acronimoRegistroHojaAnestesia = "HA";
	public static final String acronimoRegistroHojaQuirurgica = "HQ";
	
}
