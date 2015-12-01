package com.sysmedica.util;

import util.ConstantesBD;

public class TipoDiagnosticos {

	public static boolean tieneFichaVigilancia(int codigo)
	{
		boolean resultado = false;
		
		switch (codigo)
		{
			case ConstantesBD.codigoFichaVIH:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaSarampion:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaRabia:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaFiebreAmarilla:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaIntoxicacionAlimentaria:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaRubeola:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaViolenciaIntrafamiliar:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaDengue:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaParalisisFlacida:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaSifilisCongenita:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaTetanosNeo:
			{
				resultado = true;
				break;
			}
		}
		
		return resultado;
	}
	
	
	public static boolean esNotificable(int codigo)
	{
		boolean resultado = false;
		
		switch (codigo)
		{
			case ConstantesBD.codigoFichaMalaria:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaIntoxicacionPlaguicidas:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaVIH:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaSarampion:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaRabia:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaFiebreAmarilla:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaIntoxicacionAlimentaria:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaRubeola:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaViolenciaIntrafamiliar:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaDengue:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaParalisisFlacida:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaSifilisCongenita:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaTetanosNeo:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaColera:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaMeningitisMeningo:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaMeningitisHemofilos:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaTetanosAdulto:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaDifteria:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaHepatitisA:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaParotiditis:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaTosferina:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaTuberculosis:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaVaricela:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaHepatitisC:
			{
				resultado = true;
				break;
			}
			case ConstantesBD.codigoFichaIntoxicaciones:
			{
				resultado = true;
				break;
			}
		}
		
		return resultado;
	}
	
	
	public static int convertirASistemaSecretaria(int codigo)
	{
		int resultado = 0;
		
		switch (codigo) {
			
			case ConstantesBD.codigoFichaMalaria:
			{
				resultado = ConstantesBD.codigoMorbilidadMalaria;
				break;
			}
			case ConstantesBD.codigoFichaIntoxicacionPlaguicidas:
			{
				resultado = ConstantesBD.codigoMorbilidadIntoxQuim;
				break;
			}
			case ConstantesBD.codigoFichaVIH:
			{
				resultado = ConstantesBD.codigoMorbilidadVIH;
				break;
			}
			case ConstantesBD.codigoFichaSarampion:
			{
				resultado = ConstantesBD.codigoMorbilidadSarampion;
				break;
			}
			case ConstantesBD.codigoFichaRabia:
			{
				resultado = ConstantesBD.codigoMorbilidadExpoRabica;
				break;
			}
			case ConstantesBD.codigoFichaFiebreAmarilla:
			{
				resultado = ConstantesBD.codigoMorbilidadFiebreAmarilla;
				break;
			}
			case ConstantesBD.codigoFichaRubeola:
			{
				resultado = ConstantesBD.codigoMorbilidadRubeola;
				break;
			}
			case ConstantesBD.codigoFichaDengue:
			{
				resultado = ConstantesBD.codigoMorbilidadDengueClasico;
				break;
			}
			case ConstantesBD.codigoFichaDengueHemo:
			{
				resultado = ConstantesBD.codigoMorbilidadDengueHemorragico;
				break;
			}
			case ConstantesBD.codigoFichaParalisisFlacida:
			{
				resultado = ConstantesBD.codigoMorbilidadParalisisFlac;
				break;
			}
			case ConstantesBD.codigoFichaSifilisCongenita:
			{
				resultado = ConstantesBD.codigoMorbilidadSifilis;
				break;
			}
			case ConstantesBD.codigoFichaTetanosNeo:
			{
				resultado = ConstantesBD.codigoMorbilidadTetanosNeo;
				break;
			}
			case ConstantesBD.codigoFichaColera:
			{
				resultado = ConstantesBD.codigoMorbilidadColera;
				break;
			}
			case ConstantesBD.codigoFichaMeningitisMeningo:
			{
				resultado = ConstantesBD.codigoMorbilidadMeningitis;
				break;
			}
			case ConstantesBD.codigoFichaMeningitisHemofilos:
			{
				resultado = ConstantesBD.codigoMorbilidadMeningitis;
				break;
			}
			case ConstantesBD.codigoFichaTetanosAdulto:
			{
				resultado = ConstantesBD.codigoMorbilidadTetanosAdulto;
				break;
			}
			case ConstantesBD.codigoFichaDifteria:
			{
				resultado = ConstantesBD.codigoMorbilidadDifteria;
				break;
			}
			case ConstantesBD.codigoFichaHepatitisA:
			{
				resultado = ConstantesBD.codigoMorbilidadHepatitisA;
				break;
			}
			case ConstantesBD.codigoFichaParotiditis:
			{
				resultado = ConstantesBD.codigoMorbilidadParotiditis;
				break;
			}
			case ConstantesBD.codigoFichaTosferina:
			{
				resultado = ConstantesBD.codigoMorbilidadTosferina;
				break;
			}
			case ConstantesBD.codigoFichaTuberculosis:
			{
				resultado = ConstantesBD.codigoMorbilidadTuberculosis;
				break;
			}
			case ConstantesBD.codigoFichaVaricela:
			{
				resultado = ConstantesBD.codigoMorbilidadVaricela;
				break;
			}
			case ConstantesBD.codigoFichaHepatitisC:
			{
				resultado = ConstantesBD.codigoMorbilidadHepatitisC;
				break;
			}
			case ConstantesBD.codigoFichaIntoxicaciones:
			{
				resultado = ConstantesBD.codigoMorbilidadEtas;
				break;
			}
			case ConstantesBD.codigoFichaLepra:
			{
				resultado = ConstantesBD.codigoMorbilidadLepra;
				break;
			}
			
			case ConstantesBD.codigoFichaRabiaAnimal:
			{
				resultado = ConstantesBD.codigoMorbilidadRabiaAnimal;
				break;
			}
			case ConstantesBD.codigoFichaRabiaHumana:
			{
				resultado = ConstantesBD.codigoMorbilidadRabiaHumana;
				break;
			}
			
		}
		
		return resultado;
	}
	
	
	
	/**
	 * Metodo que devuelve el nombre de un evento de acuerdo al estandar de Morbilidad de la secretaria Distrital Bogota
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreMorbilidad(int codigo)
	{
		String resultado = "";
		
		switch (codigo) {
			
			case ConstantesBD.codigoMorbilidadMalaria:
			{
				resultado = ConstantesBD.nombreMorbilidadMalaria;
				break;
			}
			case ConstantesBD.codigoMorbilidadIntoxQuim:
			{
				resultado = ConstantesBD.nombreMorbilidadIntoxQuim;
				break;
			}
			case ConstantesBD.codigoMorbilidadVIH:
			{
				resultado = ConstantesBD.nombreMorbilidadVIH;
				break;
			}
			case ConstantesBD.codigoMorbilidadSarampion:
			{
				resultado = ConstantesBD.nombreMorbilidadSarampion;
				break;
			}
			case ConstantesBD.codigoMorbilidadExpoRabica:
			{
				resultado = ConstantesBD.nombreMorbilidadExpoRabica;
				break;
			}
			case ConstantesBD.codigoMorbilidadFiebreAmarilla:
			{
				resultado = ConstantesBD.nombreMorbilidadFiebreAmarilla;
				break;
			}
			case ConstantesBD.codigoMorbilidadRubeola:
			{
				resultado = ConstantesBD.nombreMorbilidadRubeola;
				break;
			}
			case ConstantesBD.codigoMorbilidadDengueClasico:
			{
				resultado = ConstantesBD.nombreMorbilidadDengueClasico;
				break;
			}
			case ConstantesBD.codigoMorbilidadDengueHemorragico:
			{
				resultado = ConstantesBD.nombreMorbilidadDengueHemorragico;
				break;
			}
			case ConstantesBD.codigoMorbilidadParalisisFlac:
			{
				resultado = ConstantesBD.nombreMorbilidadParalisisFlac;
				break;
			}
			case ConstantesBD.codigoMorbilidadSifilis:
			{
				resultado = ConstantesBD.nombreMorbilidadSifilis;
				break;
			}
			case ConstantesBD.codigoMorbilidadTetanosNeo:
			{
				resultado = ConstantesBD.nombreMorbilidadTetanosNeo;
				break;
			}
			case ConstantesBD.codigoMorbilidadColera:
			{
				resultado = ConstantesBD.nombreMorbilidadColera;
				break;
			}
			case ConstantesBD.codigoMorbilidadMeningitis:
			{
				resultado = ConstantesBD.nombreMorbilidadMeningitis;
				break;
			}
			case ConstantesBD.codigoMorbilidadTetanosAdulto:
			{
				resultado = ConstantesBD.nombreMorbilidadTetanosAdulto;
				break;
			}
			case ConstantesBD.codigoMorbilidadDifteria:
			{
				resultado = ConstantesBD.nombreMorbilidadDifteria;
				break;
			}
			case ConstantesBD.codigoMorbilidadHepatitisA:
			{
				resultado = ConstantesBD.nombreMorbilidadHepatitisA;
				break;
			}
			case ConstantesBD.codigoMorbilidadParotiditis:
			{
				resultado = ConstantesBD.nombreMorbilidadParotiditis;
				break;
			}
			case ConstantesBD.codigoMorbilidadTosferina:
			{
				resultado = ConstantesBD.nombreMorbilidadTosferina;
				break;
			}
			case ConstantesBD.codigoMorbilidadTuberculosis:
			{
				resultado = ConstantesBD.nombreMorbilidadTuberculosis;
				break;
			}
			case ConstantesBD.codigoMorbilidadVaricela:
			{
				resultado = ConstantesBD.nombreMorbilidadVaricela;
				break;
			}
			case ConstantesBD.codigoMorbilidadHepatitisC:
			{
				resultado = ConstantesBD.nombreMorbilidadHepatitisC;
				break;
			}
			case ConstantesBD.codigoMorbilidadEtas:
			{
				resultado = ConstantesBD.nombreMorbilidadEtas;
				break;
			}
			case ConstantesBD.codigoMorbilidadIntoxOh:
			{
				resultado = ConstantesBD.nombreMorbilidadIntoxOh;
				break;
			}
			case ConstantesBD.codigoMorbilidadLepra:
			{
				resultado = ConstantesBD.nombreMorbilidadLepra;
				break;
			}
			case ConstantesBD.codigoMorbilidadPeste:
			{
				resultado = ConstantesBD.nombreMorbilidadPeste;
				break;
			}
			case ConstantesBD.codigoMorbilidadRabiaAnimal:
			{
				resultado = ConstantesBD.nombreMorbilidadRabiaAnimal;
				break;
			}
			case ConstantesBD.codigoMorbilidadRabiaHumana:
			{
				resultado = ConstantesBD.nombreMorbilidadRabiaHumana;
				break;
			}
			case ConstantesBD.codigoMorbilidadTorch:
			{
				resultado = ConstantesBD.nombreMorbilidadTorch;
				break;
			}
			case ConstantesBD.codigoMorbilidadReaccionPost:
			{
				resultado = ConstantesBD.nombreMorbilidadReaccionPost;
				break;
			}
		}
		
		return resultado;
	}
	
	
	
	/**
	 * Metodo que devuelve el nombre de un evento de acuerdo al estandar de Morbilidad de la secretaria Distrital Bogota
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreBrotes(int codigo)
	{
		String resultado = "";
		
		switch (codigo) {
			
			case ConstantesBD.codigoBroteHepatitisA:
			{
				resultado = ConstantesBD.nombreBroteHepatitis;
				break;
			}
			case ConstantesBD.codigoBroteIntoxAlimentaria:
			{
				resultado = ConstantesBD.nombreBroteIntoxicacionAlimentaria;
				break;
			}
			case ConstantesBD.codigoBroteVaricela:
			{
				resultado = ConstantesBD.nombreBroteVaricela;
				break;
			}
		}
		
		return resultado;
	}
}
