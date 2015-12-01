package util.reportes.dinamico;

import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import java.awt.Color;

import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
 

/**
 * Estandar de estilos para los formatos de fuente de los reportes dinamicos.
 * 
 * @author Cristhian Murillo
 */
public class EstilosReportesDinamicosHistoriaClinica 
{
	/** Negrilla, Letra 12, Centro */
	public static StyleBuilder estiloTitulo = stl.style().bold().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.CENTER);
					
	/** Centro, Medio */
	public static StyleBuilder estiloCentradoMedio = stl.style().setAlignment(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
	
	/** Negrilla, Letra 8, Centro, Medio */
	public static StyleBuilder estiloEje = stl.style(estiloCentradoMedio).bold().setFontSize(7);
	
	/** Negrilla, Letra 8, Centrado */
	public static StyleBuilder estiloSubTitulo = stl.style().bold().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.CENTER);
	
	/** Negrilla, Letra 8, Centrado, Sombreado */
	public static StyleBuilder estiloSubTituloSombra = stl.style().bold().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.CENTER)
					.setBackgroundColor(Color.LIGHT_GRAY);
	
	/** Negrilla, Letra 8, Izquierda, Sombreado */
	public static StyleBuilder estiloSubTituloSombraL = stl.style().bold().setPadding(1).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT)
								.setBackgroundColor(Color.LIGHT_GRAY);
	
	/** Negrilla, Letra 8, Izquierda, Sombreado */
	public static StyleBuilder estiloTituloSombraL = stl.style().bold().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT)
								.setBackgroundColor(Color.DARK_GRAY);
	
	/** Letra 7, Derecha */
	public static StyleBuilder estiloDetalle = stl.style().setFontSize(7).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2);
	
	/** Letra 7, Derecha */
	public static StyleBuilder estiloDetalleR = stl.style(estiloDetalle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
	
	/**
	 * Letra 7, Derecha sin borde
	 */
	public static StyleBuilder estiloDetalleRSB = stl.style().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.RIGHT);
	
	/**
	 * Letra 7, Derecha sin borde
	 */
	public static StyleBuilder estiloHideRSB = stl.style().setFontSize(7);
	
	/**
	 * Letra 7, Izquierda sin borde
	 */
	public static StyleBuilder estiloDetalleLSB = stl.style().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT);
	
	/**
	 * Letra 7, Derecha sin borde bold
	 */
	public static StyleBuilder estiloSubTotalRSB = stl.style().bold().setPadding(1).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.RIGHT)
														.setBackgroundColor(Color.LIGHT_GRAY);
	
	/**
	 * Letra 8, Izquierda sin borde bold sobra light
	 */
	public static StyleBuilder estiloSubTituloLSL = stl.style().bold().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT)
														.setBackgroundColor(Color.LIGHT_GRAY);
	
	/** Letra 7, Izquierda */
	public static StyleBuilder estiloDetalleL = stl.style(estiloDetalle).setHorizontalAlignment(HorizontalAlignment.LEFT);
	
	/** Letra 7, Centro */
	public static StyleBuilder estiloDetalleC = stl.style().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.CENTER);
	
	/** Borde */
	public static StyleBuilder estiloBorde = stl.style().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)).setPadding(2);//   .setBorder(stl.pen1Point().setLineColor(Color.BLACK));
	
	/** Negrilla, Letra 8, Centrado Borde */
	public static StyleBuilder estiloSubTituloBorde = stl.style(estiloBorde).bold().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.CENTER);
	
	/** Centro, Medio, Borde */
	public static StyleBuilder estiloCentradoMedioBorde = stl.style(estiloBorde).setAlignment(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
	
	/** Negrilla, Letra 8, Centro, Medio */
	public static StyleBuilder estiloEjeCentradoMedioBorde = stl.style(estiloCentradoMedioBorde).bold().setFontSize(7);
	
	/** Negrilla, Letra 8, Centro, Medio */
	public static StyleBuilder estiloEjeCentradoMedioBordeSinBorde = stl.style().bold().setFontSize(7);
	
	
	/** Letra 7, Centro, Borde */
	public static StyleBuilder estiloDetalleCBorde = stl.style(estiloCentradoMedioBorde).setBorder(stl.pen1Point().setLineColor(Color.BLACK));
	
	/** Borde */
	public static StyleBuilder estiloBordeLeftRight = stl.style().setLeftBorder(stl.pen1Point().setLineColor(Color.BLACK))
																.setRightBorder(stl.pen1Point().setLineColor(Color.BLACK));
	
	public static StyleBuilder estiloPadre = stl.style().setPadding(1).setFontSize(7).setVerticalAlignment(VerticalAlignment.MIDDLE)
	.setBorder(stl.pen1Point().setLineColor(Color.WHITE)).setForegroudColor(new Color(255, 0, 0));
	
	/** Negrilla, Letra 8, Izquierda */
	public static StyleBuilder estiloSubTituloL = stl.style().bold().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT);
	
	/** Negrilla, Letra 8, Derecho, Sombreado */
	public static StyleBuilder estiloSubTituloSombraR = stl.style().bold().setPadding(1).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.RIGHT)
								.setBackgroundColor(Color.LIGHT_GRAY);
	
	/** Negrilla, Letra 8, Derecha Borde Negro*/
	public static StyleBuilder estiloNegrillaR = stl.style().bold().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.RIGHT);
	
	/** Negrilla, Letra 8, Izquierda Sin Borde Negro*/
	public static StyleBuilder estiloNegrillaL = stl.style().bold().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT);
	
	/** Letra 8, Derecha Sin Borde Negro*/
	public static StyleBuilder estiloBordeBR = stl.style().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.RIGHT);
	
	/** Letra 8, Izquierda Sin Borde Negro*/
	public static StyleBuilder estiloBordeBL = stl.style().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT);
	/** Letra 8, Izquierda Con Borde Negro*/
	public static StyleBuilder estiloConBordeBL = stl.style(estiloBorde).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT);

	/** Borde */
	public static StyleBuilder estiloBordeSencillo = stl.style().setBorder(stl.penThin().setLineColor(Color.BLACK));
	
	
	/** Negrilla, Letra 8, Izquierda, Sombreado  Sin Borde negro*/
	public static StyleBuilder estiloSubTituloSombraBL = stl.style().bold().setPadding(1).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT)
								.setBackgroundColor(Color.LIGHT_GRAY);
	
	/** Negrilla, Letra 8, Izquierda, Sombreado */
	public static StyleBuilder estiloSubTituloSombraRight = stl.style().bold().setPadding(1).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.RIGHT)
								.setBackgroundColor(Color.LIGHT_GRAY).setBorder(stl.pen1Point().setLineColor(Color.WHITE));
	
	/** Negrilla, Letra 8, Izquierda, Sombreado */
	public static StyleBuilder estiloTituloSombreadoL= stl.style().bold().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT)
								.setBackgroundColor(Color.LIGHT_GRAY).setBorder(stl.pen1Point().setLineColor(Color.WHITE));
	
	/** Negrilla, Letra 8, Derecho, Sombreado */
	public static StyleBuilder estiloTituloSombreadoRight = stl.style().bold().setFontSize(7).setHorizontalAlignment(HorizontalAlignment.RIGHT)
								.setBackgroundColor(Color.LIGHT_GRAY).setBorder(stl.pen1Point().setLineColor(Color.WHITE));
	
	
	/** Negrilla, Letra 8, Derecha Borde Negro*/
	public static StyleBuilder estiloNegrillaSencilla = stl.style().bold().setFontSize(7);
	
	/** Negrilla, Letra 8, Izquierda, Sombreado  Sin Borde negro*/
	public static StyleBuilder estiloSubTituloSombraBLOscura = stl.style().bold().setPadding(1).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT)
								.setBackgroundColor(Color.DARK_GRAY);
	
	/** Letra 8, Derecha Sin Borde Negro*/
	public static StyleBuilder estiloBordeBRNegrilla = stl.style().setFontSize(7).bold().setPadding(2).setHorizontalAlignment(HorizontalAlignment.RIGHT);
	
	/** Estilo para ocultar los caracteres*/
	public static StyleBuilder estiloSubTituloInvisible = stl.style().setPadding(1).setFontSize(7).setBackgroundColor(Color.LIGHT_GRAY)
																.setForegroudColor(Color.LIGHT_GRAY).setBorder(stl.pen1Point().setLineColor(Color.WHITE));
	
	/**  Letra 8, Centrado */
	public static StyleBuilder estiloSubTituloSinBold = stl.style().setFontSize(7).setPadding(2).setHorizontalAlignment(HorizontalAlignment.CENTER);
	
	/** Underline, Letra 12, Centro */
	public static StyleBuilder estiloTituloUnderline = stl.style().underline().setFontSize(8).setHorizontalAlignment(HorizontalAlignment.CENTER);
	
	/**  */
	public static StyleBuilder estiloBordeNegrilla = stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)).setPadding(2).setHorizontalAlignment(HorizontalAlignment.LEFT);
	
	/** Justifiacdo */
	public static StyleBuilder estiloBordeNegrillaJustificado = stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)).setPadding(2).setHorizontalAlignment(HorizontalAlignment.LEFT);
	
	/**
	 *Estilo Subtitulo letra 7
	 */
	public static StyleBuilder estiloSubTituloFont5=stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(7);
	
	/**
	 *Estilo Subtitulo letra 5 
	 */
	public static StyleBuilder estiloSubTitulo5=stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setFontSize(6);
	
}	
