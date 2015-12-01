package com.princetonsa.mundo.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;

import util.ConstantesBD;
import util.Listado;
import util.Utilidades;

/**
 * @author Jose Eduardo Arias Doncel
 * */
public class ParametrizacionFormulariosRespu
{
	
	/**
	 * Indices para el listado de Plantillas
	 * */
	public static String [] indicesListadoPlantilla = {"codigoPk_","funParam_","centroCosto_","sexo_","especialidad_","codigoPlantilla_","nombrePlantilla_","servicios_","institucion_","estabd_"};
	
	
	//****************************************************************************************************
	//****************************************************************************************************
	
	/**
	 * Quita un servicio o lo añade en el listado de servicios que sirven como 
	 * filtro para la busqueda de servicios
	 * 
	 * @param HashMap datos
	 * @param String servicio
	 * @param String opcion (quitar,reCalcular,agregar)
	 * */
	public static String quitarAnadeServicioRestriccion(HashMap datos, String indicador,String opcion, int pos)
	{
		String codigoServicioInsertados = datos.get("codigosServiciosInsertados").toString();
		HashMap serviciosMap;
		int numServicios;
	
		if(!indicador.equals(""))
		{
			if(opcion.equals("quitar"))
			{
				return codigoServicioInsertados.replace(","+indicador+",",",-1,");
			}
			else if(opcion.equals("reCalcular"))
			{
				int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
				codigoServicioInsertados = "-1,";
				
				for(int j=0; j<numRegistros; j++)
				{
					serviciosMap = (HashMap)datos.get("servicios_"+j);
					numServicios = Integer.parseInt(serviciosMap.get("numRegistros").toString());				
					
					for(int i = 0; i<numServicios; i++)
					{
						if(serviciosMap.get("fueEliminadoServicio_"+i).toString().equals(ConstantesBD.acronimoNo))
							codigoServicioInsertados += serviciosMap.get("codigoServicio_"+i).toString()+",";					
					}
				}
			}
			else if(opcion.equals("reCalcularIndep"))
			{
																
			}
			else if (opcion.equals("agregar"))
			{
				if(datos.equals(""))
					codigoServicioInsertados+="-1,"+indicador+",";
				else
					codigoServicioInsertados+=indicador+",";
			}
		}
		
		return codigoServicioInsertados;
	}
	
	
	//****************************************************************************************************
	//****************************************************************************************************
	
	/**
	 *  Ordena Un Mapa HashMap a partir del patron de ordenamiento
	 *  @param HashMap mapaOrdenar
	 *  @param String patronOrdenar
	 *  @param String ultimoPatron
	 *  @return Mapa Ordenado
	 **/
	public static HashMap accionOrdenarMapa(HashMap mapaOrdenar,
									 String patronOrdenar,
									 String ultimoPatron)
	{					
		String[] indices = (String[])mapaOrdenar.get("INDICES_MAPA");
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");		
		mapaOrdenar = (Listado.ordenarMapa(indices,patronOrdenar,ultimoPatron,mapaOrdenar,numReg));	
		mapaOrdenar.put("numRegistros",numReg+"");
		mapaOrdenar.put("INDICES_MAPA",indices);		
		return mapaOrdenar;
	}
	
	//****************************************************************************************************
	//****************************************************************************************************
	
	/**
	 * Valida la adicion de un nuevo servicio o diagnostico
	 * @param ArrayList<DtoPlantillaServDiag> array
	 * @param DtoPlantillaServDiag dto 
	 * */
	public static boolean accionValidarNuevoAtributo(ArrayList<DtoPlantillaServDiag> array,DtoPlantillaServDiag dto )
	{
		for(int i = 0; i < array.size(); i++)
		{
			if(array.get(i).getEsEliminado().equals(ConstantesBD.acronimoNo))
			{
				//Servicio y Diagnostico Existen
				if(dto.getCodigoDiagnostico() > 0 
						&& dto.getCodigoServicio() > 0 
							&& dto.getCodigoDiagnostico() == array.get(i).getCodigoDiagnostico() 
								&& dto.getCodigoServicio() == array.get(i).getCodigoServicio())
				{
					//System.out.println("caso 1"); 
					return false;
				}
				
				//Servicio Existe y Diagnostico No
				if(dto.getCodigoDiagnostico() < 0 
						&& dto.getCodigoServicio() > 0		
							&& dto.getCodigoDiagnostico() == array.get(i).getCodigoDiagnostico() 
								&& dto.getCodigoServicio() == array.get(i).getCodigoServicio())
				{
					//System.out.println("caso 2");
					return false;
				}
				
				//Servicio No Existe y Diagnostico Existe
				if(dto.getCodigoServicio() < 0 
						&& dto.getCodigoDiagnostico() > 0
							&& dto.getCodigoDiagnostico() == array.get(i).getCodigoDiagnostico() 
								&& dto.getCodigoDiagnostico() == array.get(i).getCodigoDiagnostico())
				{
					//System.out.println("caso 3");
					return false;			
				}
			}
		}
		
		return true;
	}
}