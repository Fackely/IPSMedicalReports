package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanComparator;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
/**
 * @author rcancino
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Listado
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private static Logger logger = Logger.getLogger(Listado.class);
	
	public static ArrayList ordenarColumna(ArrayList lista, String ultimaPropiedad,String propiedad) throws IllegalAccessException{
		
		
		ArrayList listaOrdenada = null;

		/*for(int i=0;i<lista.size();i++){
			
			Class cl=lista.get(i).getClass();
			Field[] campos=cl.getDeclaredFields();
			AccessibleObject.setAccessible(campos,true);
			for (int k=0;k<campos.length;k++){
			}		
		
		}*/
	
		if (ultimaPropiedad != null && ultimaPropiedad.equals(propiedad)) 
		{
			//mismo orden, reversar la lista
			Collections.reverse( lista);
			return lista;
 		} 
		else 
		{
			// Ordenar por una nueva propiedad
			HashMap temporal=new HashMap();
			String objetoA="";
			String objetoB="";
			for(int i=0;i<lista.size();i++)
			{
				for(int k=0;k<i;k++)
				{
					
					try
					{
						objetoA=((HashMap)lista.get(i)).get(propiedad)+"";
						objetoB=((HashMap)lista.get(k)).get(propiedad)+"";
						if(objetoA.compareToIgnoreCase(objetoB)<0)
						{
							temporal=(HashMap)lista.get(i);
							lista.set(i, lista.get(k));
							lista.set(k, temporal);
						}
					}
					catch(ClassCastException e)
					{
						//en caso de presentar error al cast del mapa, se debe manejar como un DynaBean, lo ideal es cambiar todos los metodos
						//que aun funcionan con DynaBean por mapas o por DTO. 
						return ordenarColumnaListadoBean(lista,ultimaPropiedad,propiedad);
					}
					
				}
			}
			return lista;
         }      
		
	}	
	
	private static ArrayList ordenarColumnaListadoBean(ArrayList lista, String ultimaPropiedad,String propiedad) throws IllegalAccessException{
		
		
		ArrayList listaOrdenada = null;

		
		if (ultimaPropiedad != null && ultimaPropiedad.equals(propiedad)) 
		{
			//mismo orden, reversar la lista
			Collections.reverse( lista);
			return lista;
 		} 
		else 
		{
			// Ordenar por una nueva propiedad
			Object []userList = lista.toArray();
			userList.toString();
			int s = userList.length;
			listaOrdenada = new ArrayList(s);
            BeanComparator bc = new BeanComparator(propiedad);
            

             Arrays.sort(userList, bc);

            
			for (int i=0; i<s; i++) listaOrdenada.add(userList[i]);
			return listaOrdenada;
			
			
         }      
		
	}


	/**
	 * Metodo que realiza el ordenamiento ascendente y descendente de un HashMap.
	 * El primer ordenamiento que realiza el mapa es ascendente, en caso de que el 
	 * indice para ordenar sea igual al indice del ultimo ordenamientos, se realiza
	 * una ordenacion descendente.
	 * @param indices, String[] lista de los indices del HashMap que pertenecen al 
	 * registro(fila) <code>restricción</code> los indices deben ir en el formato (index_)
	 * @param indice, String indice de la columna por la cual se va ordenar
	 * @param ultimoIndice, String indice por el cual se realizó el ultimo ordenamiento.
	 * @param mapaDesorden, HashMap que se desea ordenar
	 * @param numeroElementos, Numero de elementos que tiene el mapa
	 * @return HashMap, Retorna el HashMap ordenado
	 * @author jlopez, jvelasquez
	 */
     /*
     * Este metodo ordena tanto, por numeros como
     * por cadenas, debido a que el sort ordena por cadenas, con los datos de 
     * tipo numero no va funcionar, por lo tanto se debe implementar un metodo de
     * ordenamiento independiente para numeros y cadenas, identificando que tipo de
     * datos son los que vienen y marcarlos, para luego ordenarlos independiente/
     * segun el tipo de dato, son divididos en dos tipo de objetos caracteres y numeros
     * se realiza el sort a los caracteres, y un bublesort a los numeros, y 
     * despues de tener ordenados ambos objetos, se unen en el HashMap.
     * 
     * Tambien se debe arreglar el metodo para que tome los indices sin el
     * formato(index_), como el numero de registros(numReg).
     * @jarloc
     */     
	public static HashMap ordenarMapa (String[] indices,String indice,String ultimoIndice, HashMap mapaDesorden, int numeroElementos)
	{
		if(indices!=null)
		{
		    HashMap mapOrdenado = new HashMap ();
		    int pos = 0;          
			if (ultimoIndice == null || !ultimoIndice.equals(indice)) 
			{
			    ArrayList arrayList = new ArrayList ();		    	    
			    for (int k = 0; k < numeroElementos; k ++)
			    {
			        arrayList.add(pos,mapaDesorden.get(indice+k));
			        pos ++;	        
			    }
			    pos = 0;
			    Object[] objectValores=new Object[arrayList.size()];
			    for (int k = 0; k < numeroElementos; k ++)
			    {
			        objectValores[pos]=mapaDesorden.get(indice+k)+ConstantesBD.separadorSplitComplejo+k;
			        pos ++;	        
			    }		    
	            /**Se verifican los tipos de datos que que se ordenaran segun la columna seleccionada, debido 
	             * a que pueden ser caracteres ó numeros, e inclusive caracteres y numeros, por lo tanto 
	             * se deben diferenciar para realizar ordenamientos distintos*/
	            Vector numeros=new Vector(); 
	            Vector caracteres=new Vector();     
	            Object objetoValor = null;
	            String objetoValorOmitiendoMayusculasDeMinusculas="";
	            if(objectValores.length!=0)
	              {
	                String[] datos;
	                for(int k=0;k<objectValores.length;k++)
	                {
	                    objetoValor=objectValores[k];
	                    datos=objetoValor.toString().split(ConstantesBD.separadorSplitComplejo);
	        		    try
	        		    {
	        		        Double.parseDouble(datos[0]); 
	        		        numeros.add(objetoValor);
	        		    }
	        		    catch(NumberFormatException e)
	        		    {
	                        //para pasar los objetos de tipo caracter a minusculas, y asi realizar el
	                        //ordenamiento como valores iguales
	                        objetoValorOmitiendoMayusculasDeMinusculas=objetoValor+"";
	                        caracteres.add(objetoValorOmitiendoMayusculasDeMinusculas.trim().toLowerCase());
	        		    } 
	                }
	              }
	            
	            //new
	            SimpleDateFormat dateFormatter = new SimpleDateFormat( "dd/MM/yyyy");
	            //new
	            
	            /**se realiza el ordenamiento de los caracteres*/
	            Object[] objectCaracteresOrdenar=null;            
	            if(!caracteres.isEmpty())
	            {                
	                objectCaracteresOrdenar=new Object[caracteres.size()];
	                for(int l=0;l<caracteres.size();l++)
	                {
	                	String fec1 = "";
	                	String fec2 = "";
	                	String hora1 = "";
	                	String hora2 = "";
	                	
	                	//SE DEBE SEPARAR EL STRING PARA VER SI EL PRIMER ELEMENTO ES UNA FECHA
	                    String fechas[] = caracteres.get(l).toString().split(ConstantesBD.separadorSplitComplejo);
	                    if (fechas.length >= 2)
	                    {
			            	fec1 = fechas[0];
			            	fec2 = fechas[1];
			            	
			            	String horas[] = fec1.split("-");
	
		                    if (horas.length >= 2)
		                    {
		                    	
		                    	hora1 = horas[0];
		                    	hora2 = horas[1];
		                    	
		                    	fec1 = hora1.trim();
		                    }
		                	
	                    }
	
		            	//VERIFICAR SI EL STRING SE PUEDE INTERPRETAR COMO FECHA
		            	if (UtilidadFecha.validarFecha(fec1))
		            	{
		            		java.util.Date theDate;
							try {
								String nuevoElemento = "";
								theDate = dateFormatter.parse(fec1);
								//SE CAMBIA AL FORMATO ANO, MES , DIA
								String fecFormatoBD = UtilidadFecha.conversionFormatoFechaABD(theDate);
								nuevoElemento = fecFormatoBD + ConstantesBD.separadorSplitComplejo + fec2 ;
								if ( hora2 == "")
								{
									nuevoElemento = fecFormatoBD + ConstantesBD.separadorSplitComplejo + fec2 ;
								}
								else
								{
									nuevoElemento = fecFormatoBD.concat(hora2) + ConstantesBD.separadorSplitComplejo + fec2 ;
								}
									
	
								caracteres.set(l, nuevoElemento);
							} catch (ParseException e) {
								e.printStackTrace();
							}
		            	}
		            	objectCaracteresOrdenar[l]=caracteres.get(l);
	                }
	                Arrays.sort(objectCaracteresOrdenar);
	            }            
	            /**Se realiza el ordenamiento de los numeros*/
	            if(!numeros.isEmpty())
	            {                
	                String [] datos1,datos2;  
	                String tem[];
	                for(int j=0;j<numeros.size();j++)
	                {   
	                    for(int i=0;i<j;i++)
	                    {
	                        datos1=(numeros.get(j)+"").split(ConstantesBD.separadorSplitComplejo);
	                        datos2=(numeros.get(i)+"").split(ConstantesBD.separadorSplitComplejo);                        
	                        if(Double.parseDouble(datos1[0])<Double.parseDouble(datos2[0]))
	                       {                           
	                           tem=datos1;
	                           numeros.set(j,datos2[0]+ConstantesBD.separadorSplitComplejo+datos2[1]);
	                           numeros.set(i,tem[0]+ConstantesBD.separadorSplitComplejo+tem[1]);                           
	                       }                              
	                    }
	                }
	            }                
	            Object[] columnaOrdenada=new Object[numeros.size()+(objectCaracteresOrdenar==null?0:objectCaracteresOrdenar.length)];
	            int n=0;
	            if(!numeros.isEmpty())               
	                for(n=0;n<numeros.size();n++)
	                 columnaOrdenada[n]=numeros.get(n);            
	            if(objectCaracteresOrdenar!=null)
	                for(int k=0;k<objectCaracteresOrdenar.length;n++,k++)                                   
	                    columnaOrdenada[n]=objectCaracteresOrdenar[k];           
	            /**La columna seleccionada ya fue ordenada, entonces se debe ordenar
	             * todo el mapa, es decir que a cada uno de las llaves de la columna con sus respectivos valores, 
	             * le correspondan el resto de las llaves en la posición indicada, para que de esta forma
	             * quede todo el mapa ordenado
	             * */
	            int numeroIdices = indices.length;
			    for(int j =0; j < columnaOrdenada.length; j++)
			    {
			        String[] datos=columnaOrdenada[j].toString().split(ConstantesBD.separadorSplitComplejo);
			        for(int i=0; i < numeroIdices; i++)
			        {
			            Object indexOld = indices[i]+datos[1];
			            Object indexNew = indices[i]+j;
			            mapOrdenado.put(indexNew,mapaDesorden.get(indexOld)); 
			        }	        
			    }
			}
			else if(ultimoIndice != null && ultimoIndice.equals(indice))
			{
				pos=0;
				for(int p=(numeroElementos-1);p>=0;p--)
				{
					for(int i=0; i < indices.length; i++)
					{
						mapOrdenado.put(indices[i]+pos,mapaDesorden.get(indices[i]+p));
					}
					pos++;
				}
			}
			return mapOrdenado;
		}
		return mapaDesorden;
	}
	
	/**
	 * Este metodo permite pasar una coleccion <code>(tipo java.util.Collection)</code>, 
	 * a un HashMap, ArrayList ó String[].
	 * El HashMap es retormnado con los indices tomados del String[] columns, que recibe como
	 * parametro, los indices del HashMap son retornados con el siguiente patron indice_#.
	 * Este indice corresponde a los nombres que son enviados por referencia en el String[], 
	 * que corresponde al parametro columns. 
	 * El ArrayList es retornado como la lista de objetos que es.
	 * El String[] contiene los datos consecutivamente, 
	 * para lo obtencion de estos es necesario iterar.
	 * Para obtener los datos en el tipo de lista deseada, se realiza un cast al objeto 
	 * retornado(ej. (HashMap)Listado.convertirCollection(String[],Collection,1) )
	 * 
	 * @param columns String[], Este parametro corresponde a los nombres que hacen referncia,
	 * 							a cada dato dentro de la coleccion.
	 * @param col Collection, La coleccion que contiene los datos que seran pasados a los
	 * 						  diferentes tipos de listas.
	 * @param codigo int, número que indica el tipo de lista que se obtendra,
	 * 					  1 HashMap, 2 ArrayList, 3 String[]
	 * @return objeto, Object encapsula el tipo de listado que se retorna, 
	 * 				   sea HashMap, ArrayList ó String[]. Se debe realizar un cast segun el tipo
	 * 				   de dato retornado.
	 * @author jlopez
	 */
	public static Object convertirCollection (String[] columns,	Collection col,int codigo)
	{
	    HashMap mapa = new HashMap ();
	    ArrayList arrayList = new ArrayList();
	    String[] stringArray =  null;
	    String index = "";
	    HashMap dyn;
	    int i=0, k=0,posArray=0,posColumns=0,posHash=0;
	    Object objeto = null; 
	    
	    if (col == null || col.isEmpty())
   	 	{
   	 	  stringArray[0]=null;
   	 	}
	    else
	    {
	        Iterator it = col.iterator();  
	        k=0;
	   	 	while (it.hasNext())
	   	 	{
	   	 	    dyn=(HashMap)it.next();
	   	 	    for(i=0; i < columns.length; i++)
	   	 	    {
	   	 	        arrayList.add(k, dyn.get(columns[i]));
	   	 	        k++;
	   	 	    }
	   	 	}
	   	 	stringArray = new String[arrayList.size()];
	   	 	for(i=0; i<arrayList.size(); i++)
		     stringArray[i] = arrayList.get(i) + "";
	   	 	for(posHash=0;posHash<col.size();posHash++) 
		    {
	   	 	    for (posColumns=0;posColumns<columns.length;posColumns++)
	   	 	    {
	   	 	        index = columns[posColumns]+"_"+posHash;
	   	 	        mapa.put(index,stringArray[posArray]);
	   	 	        posArray ++;
	   	 	    } 	 	    
	   	 	}
	   	 	if(codigo == 1)
	   	 	{
	   	 	    objeto = (Object)mapa;  
	   	 	}
	   	 	else if(codigo == 2)
	   	 	{
	   	 	    objeto = (Object)arrayList; 
	   	 	}
	   	 	else if(codigo == 3)
	   	 	{
	   	 	    objeto = (Object)stringArray;
	   	 	}
	    }
	    
	    return objeto;
	}
	
	/**
	 * Método que ordena un mapa con base en un indice especificado, el cual permite ordenar campos numericos,
	 * de fechas y tambien cadenas. Para esto cambia el vector de indices a una matriz que contiene parejas donde se indica
	 * el tipo del campo para realizar el ordenamiento, de no especificarse uno válido se ordena considerandolo como una cadena
	 * @param indices
	 * @param indice
	 * @param ultimoIndice
	 * @param mapaDesorden
	 * @param numeroElementos
	 * @return
	 */
	public static HashMap ordenarMapa(
			Object[][] indices, String indice, String ultimoIndice, HashMap mapaDesorden, int numeroElementos)
	{
		ArrayList elementosMapa = new ArrayList();
		HashMap mapaOrdenado = new HashMap();
		try
		{
			int tipoCampoIndice=1;
			for(int i=0; i<indices.length; i++)
			{
				if(indices[i][0].equals(indice))
				{
					tipoCampoIndice=(Integer)indices[i][1];
					break;
				}
			}
			for(int i=0; i<numeroElementos; i++)
			{
				ParejaMapa pareja = new ParejaMapa(mapaDesorden.get(indice+i), i, indice+i, tipoCampoIndice);
				elementosMapa.add(pareja);
			}
			if(indice.equals(ultimoIndice))
				Collections.reverse(elementosMapa);
			else
				Collections.sort(elementosMapa);
			
			for(int i=0; i<elementosMapa.size(); i++)
			{
				ParejaMapa pareja = (ParejaMapa)elementosMapa.get(i);
				int posInicialMapa = pareja.getPosicionInicial();
				for(int j=0; j<indices.length; j++)
				{
					String claveAnterior = (String)indices[j][0] + posInicialMapa;
					mapaOrdenado.put( ((String)indices[j][0])+i, mapaDesorden.get(claveAnterior));
				}
			}
			return mapaOrdenado;
		}
		catch(ParseException pe)
		{
			return mapaDesorden;
		}
	}
	
	
	/**
	 * Método implementado para sacar los errores cuando existen registros
	 * repetidos en un mapa por algún campo específico
	 * @param errores
	 * @param mapa
	 * @param llave
	 * @param valorInvalido
	 * @param mensaje
	 * @return
	 */
	public static ActionErrors validarRegistrosRepetidos(ActionErrors errores,HashMap mapa,String llave,String valorInvalido,String mensaje)
	{
		HashMap codigosComparados = new HashMap();
		String descripcion = "";
		String aux1 = "";
		String aux2 = "";
		int numRegistros = Integer.parseInt(mapa.get("numRegistros").toString());
		
		for(int i=0;i<numRegistros;i++)
		{
			aux1=mapa.get(llave+i).toString();
			
			for(int j=numRegistros-1;j>i;j--)
			{
				
				aux2=mapa.get(llave+j).toString();
				//se compara
				if(aux1.compareToIgnoreCase(aux2)==0&&!aux1.equals(valorInvalido)
					&&!aux2.equals(valorInvalido)&&!codigosComparados.containsValue(aux1))
				{
					if(descripcion.equals(""))
						descripcion = (i+1) + "";
					descripcion += "," + (j+1);
					
				}
			}
			
			if(!descripcion.equals(""))
			{
				errores.add("códigos iguales", 
						new ActionMessage("error.salasCirugia.igualesGeneral",
							mensaje,"en los registros Nº "+descripcion));
			}
			
			descripcion = "";
			codigosComparados.put(i+"",aux1);
			
		}
		
		
		return errores;
	}
	
	
	
	
	/**
	 * Metodo encargado de copiar Todos los registros de un Hashmap Filtrandolo
	 * por el index 
	 * @author Jhony Alexander Duque A.
	 * @param mapa --> Al cual se le quieren sacar los registros
	 * @param index --> posicion de donde se desean sacar los registros
	 * @param indices --> key's que contiene el mapa
	 * @return
	 */
	public static HashMap copyOnIndexMap (HashMap mapa, String index, String [] indices)
	{
		HashMap respuesta = new HashMap ();
		
		for(int i=0;i<indices.length;i++)
		{		
			if(mapa.containsKey(indices[i]+index))
			{				
				respuesta.put(indices[i]+"0", mapa.get(indices[i]+index));
				
			}
		}
		respuesta.put("numRegistros", "1");
		return respuesta;
	}
	
	
	/**
	 *Metodo encargado De copiar los elementos de un Hashmap dentro de otro,
	 *conservando los datos anteriores y anexando en el consecutivo de numRegistros 
	 *y el index.
	 * @author Jhony Alexander Duque A.
	 * @param mapaDatos --> Datos a copiar
	 * @param mapaResult --> Mapa donde se desea copiar los datos
	 * @param numRegistros --> numero de registros de mapaDatos
	 * @param indices --> indices de MapaDatos
	 * @return
	 */
	public static HashMap copyMap (HashMap mapaDatos, HashMap mapaResult, int numRegistros, String [] indices)
	{
		if (mapaDatos.containsKey("numRegistros") && Integer.parseInt(mapaDatos.get("numRegistros")+"")>0)
		{
			int numReg=0,k=0,index=0;
				
			if(mapaResult.containsKey("numRegistros"))
			{
				numReg=Integer.parseInt(mapaResult.get("numRegistros").toString())+numRegistros;
				k=Integer.parseInt(mapaResult.get("numRegistros").toString());
			}
			else
				numReg=numRegistros;
			
			for (;k<numReg;k++)
			{
				for(int i=0;i<indices.length;i++)
				{		
					if(!mapaResult.containsKey(indices[i]+k))
						mapaResult.put(indices[i]+k, mapaDatos.get(indices[i]+index));
				
				}
				
				mapaResult.put("numRegistros", k+1);
				index=index+1;
				
			}
		
			return mapaResult;
		}
		else 
			if (mapaResult.containsKey("numRegistros") && Integer.parseInt(mapaResult.get("numRegistros")+"")>0)
				return mapaResult;
			else
			{
				mapaResult.put("numRegistros",0);
				return mapaResult;
			}
	}
	
	/**
	 * Metodo encargado de copiar los elementos de un hashmap
	 * de numRegistros 1
	 * a otro en un index indicado.
	 * @param mapaDatos  --> Datos a copiar
	 * @param mapaResult  --> Mapa donde se desea copiar los datos
	 * @param indices  --> indices de MapaDatos
	 * @param index --> index donde se desea copiar los datos
	 * @return
	 */
	public static HashMap copyMapOnIndexMap (HashMap mapaDatos, HashMap mapaResult, String [] indices,int index)
	{
		
		for(int i=0;i<indices.length;i++)
			{		
				if(!mapaResult.containsKey(indices[i]+index))
					mapaResult.put(indices[i]+index, mapaDatos.get(indices[i]+"0"));
			
			}
			mapaResult.put("numRegistros", Integer.parseInt(mapaResult.get("numRegistros")+"")+1);
			
		return mapaResult;
	}
	
	/**
	 * Metodo encargado de eliminar los registros de un mapa
	 * @param mapaDatos --> Mapa de donde se va a eliminar el registros
	 * @param indices --> indices del mapaDatos
	 * @param index --> posicion del mapa a eliminar
	 * @param numReg --> cantidad de registros del mapa
	 * @return
	 */
	public static HashMap eliminarRegistroMapa (HashMap mapaDatos,String [] indices,String index,int numReg)	
	{
		logger.info("\n entre a eliminarRegistroMapa \n mapaDatos-->"+mapaDatos+" \n index -->"+index+"\n numReg-->"+numReg);
		
		HashMap mapaResult= new HashMap();
		mapaResult.put("numRegistros", 0);
		int cant=0;
		for (int i=0;i<numReg;i++)
		{
			if (!index.equals(i+""))
			{
				for (int j=0;j<indices.length;j++)
				{
					mapaResult.put(indices[j]+cant, mapaDatos.get(indices[j]+i));
				}
				cant++;
				
				mapaResult.put("numRegistros", Utilidades.convertirAEntero(mapaResult.get("numRegistros")+"")+1);
			}
			
		}
		logger.info("\n mapa al salir -->"+mapaResult);
		return mapaResult;
	}
	
	/**
	 * Metodo encargado de copiar los elementos de un hashmap
	 * filtrando por un indice indicado
	 * a otro en un index indicado.
	 * @param mapaDatos  --> Datos a copiar
	 * @param mapaResult  --> Mapa donde se desea copiar los datos
	 * @param indices  --> indices de MapaDatos
	 * @param index --> index donde se desea copiar los datos
	 * @param indexDatos --> index del mapa Datos del registro que se quiere copiar
	 * @return
	 */
	public static HashMap copyMapOnIndexMap (HashMap mapaDatos, HashMap mapaResult, String [] indices,int index, int indexDatos)
	{
		
		for(int i=0;i<indices.length;i++)
			{		
				if(!mapaResult.containsKey(indices[i]+index))
					mapaResult.put(indices[i]+index, mapaDatos.get(indices[i]+indexDatos));
			
			}
			mapaResult.put("numRegistros", Integer.parseInt(mapaResult.get("numRegistros")+"")+1);
			
		return mapaResult;
	}
	
	
	/**
	 * Metodo encargado de quitar los valores null del mapa
	 * el mapa debe tener numRegistros
	 * @param mapaDatos
	 * @return
	 */
	public static HashMap quitarNullMapa (HashMap mapaDatos, String [] indices)
	{
		int numReg = Utilidades.convertirAEntero(mapaDatos.get("numRegistros")+"");
		for (int i=0;i<numReg;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				if(!UtilidadCadena.noEsVacio(mapaDatos.get(indices[j]+i)+""))
					mapaDatos.remove(indices[j]+i);
			}
		}
		return mapaDatos;
	}
	
	/**
	 * Metodo encargado de copiar los elementos de un Hashmap
	 * indicando los indices.
	 * 
	 * @param mapaDatos  --> Datos a copiar
	 * @param indices  --> indices de MapaDatos
	 * @return
	 */
	public static HashMap copyMap (HashMap mapaDatos, String [] indices)
	{
		HashMap mapaResult = new HashMap ();
		
		for(int i=0;i<indices.length;i++)
			{		
				if(!mapaResult.containsKey(indices[i]))
					mapaResult.put(indices[i], mapaDatos.get(indices[i]));
			
			}
					
		return mapaResult;
	}
	
	/**
	 * Metodo encargado de copiar los datos de un mapa 
	 * dentro de otro, conservando los registros anteriores
	 * estos mapas no tienen indice.
	 * @param mapaDatos
	 * @param mapaResult
	 * @param indices
	 * @return
	 */
	public static HashMap copyMap (HashMap mapaDatos,HashMap mapaResult, String [] indices)
	{
				
		for(int i=0;i<indices.length;i++)
			{		
				if(!mapaResult.containsKey(indices[i]))
					mapaResult.put(indices[i], mapaDatos.get(indices[i]));
			
			}
					
		return mapaResult;
	}
	
	/**
	 * Metodo encargado de copiar un array de HashMap
	 * dentro de otro Array de HashMap
	 * @param array
	 * @param indices --> indices del Hashamap dentro del array
	 * @return
	 */
	public static ArrayList copyArray (ArrayList array,String [] indices)
	{
		ArrayList result = new ArrayList ();
		
		for (int i=0;i<array.size();i++)
			result.add(copyMap((HashMap)array.get(i), indices));
		
		return result;
	}
	
	
	
	/**
	 * Adicionado por Jose Eduardo Arias Doncel 
	 * Copia un Mapa
	 * @param mapa
	 */
	public static HashMap copyMapFree(HashMap mapa)
	{		
		HashMap tmp = new HashMap();		
		Iterator llaves=mapa.entrySet().iterator();
		while(llaves.hasNext())
		{
			String contenido=llaves.next()+"";
			tmp.put(contenido.split("=")[0],mapa.get(contenido.split("=")[0]));			
		}
		
		return tmp;
	}
	
	
	
	/**
	 * Adicionado por Jhony Alexander Duque Aristizabal.
	 * Metodo encargado de ingresarle registros a un mapa
	 * añadiendo a la cadena del key el nuevo key del mapa Datos:
	 * ejempo: profecionales_0_consecutivo_0
	 * @param mapaDatos --> Datos a ser copiados
	 * @param indiceMapadatos --> indice del maparaResult donde se van a copiar los datos
	 * viene de la siguiete manera ejemplo: profecionales_0 
	 * @param mapaResult--> Mapa donde se van a copiar los datos
	 * @param indices --Indices de mapaDatos indican los valores que se van a copiar
	 * @return
	 */
	public static HashMap copyMapIndexWithNewIndex (HashMap mapaDatos,String [] indices,HashMap mapaResult,String indiceMapaResult)
	{
		int numReg = Integer.parseInt(mapaDatos.get("numRegistros")+"");
		if (numReg>0)			
			for (int i=0;i<numReg;i++)
			{
				for(int j=0;j<indices.length;j++)
				{		
					if(!mapaResult.containsKey(indices[j]))
						mapaResult.put(indiceMapaResult+"_"+indices[j]+i , mapaDatos.get(indices[j]+i));
				
				}
				
			}
		
		 mapaResult.put(indiceMapaResult+"_numRegistros" , numReg);
		
		
		return mapaResult;
		
	}
	
	/**
	 * Metodo adicionado por Jhony Alexander Duque A.
	 * Metodo encargado de Devolver los Key's de un
	 * Mapa.
	 * de un mapa determinado
	 * @param mapa
	 * @return String []
	 */
	public static String[] obtenerKeysMapa(HashMap mapa) {
		Set<Entry<String, Object>> conjunto = mapa.entrySet();
		
		String[] listado = new String[conjunto.size()];
		int cont = 0;
		
		Iterator iterador = conjunto.iterator();
		
		while(iterador.hasNext()) {
			Entry<String, Object> elemento = (Entry<String, Object>)iterador.next();
			listado[cont] = elemento.getKey();

			//logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			//logger.info(listado);
			cont++;
		}
		return listado;
	}
	
	/**
	 * Metodo adicionado por Jhony Alexander Duque A.
	 *Metodo encargado de tomar un conjunto de datos
	 *que su llave contenga una parte de la llave a comparar.
	 *Ejemplo:
	 *se tiene un mapa con este key servicios_0_numRegistros
	 *keyToCompare --> servicios_0_
	 *el resultado va a ser un mapa con la key numRegistros 
	 * @param mapa
	 * @param keyToCompare
	 * @return
	 */
	public static HashMap obtenerMapaInterno (HashMap mapa,String keyToCompare)
	{
		//logger.info("el mapa servicios --> "+mapa);
		HashMap mapaResult = new HashMap();
		boolean ban = false;
		String [] keys= obtenerKeysMapa(mapa);
		for (int i=0;i<keys.length;i++)
		{			
			if ((keys[i]).indexOf(keyToCompare)>=0)
			{
				mapaResult.put((keys[i].split(keyToCompare))[1], mapa.get(keys[i]));
				ban=true;
			}
			
		}
	
		if (!ban)
			mapaResult.put("numRegistros", 0);
		
		return mapaResult;
	}
	
	
	
	/**
	 * Metodo que permite el ordenamiento con Rompimiento; este metodo
	 * se apoya en los siguiente Metodos:
	 * --copyOnIndexMap.
	 * --copyMap.
	 * --ordenarMapa.
	 * --detectarCambioRompimiento.
	 * @param indices --> key's que contiene el mapaDesorden
	 * @param indice -->Patron por el cual se desea ordenar
	 * @param ultimoIndice --> Ultimo patron por el cual se ordeno
	 * @param mapaDesorden --> mapa a ser ordenado
	 * @param tipoRompimiento --> key's que indica el elemento por el cual se va a hacer el rompimiento.
	 * @return
	 */
	public static HashMap ordenarMapaRompimiento (String[] indices,String indice,String ultimoIndice, HashMap mapaDesorden, String tipoRompimiento)
	{
		HashMap tmporder = new HashMap ();
		//tmp2 es para ir almacenando los registros que pertenecen al mismo
		//tipo de rompimiento.
		HashMap tmp2 = new HashMap ();
		HashMap tmp3 = new HashMap ();
		//tmp4 es donde se guarda los que se va ordenando.
		HashMap tmp4 = new HashMap ();
		Vector cambios = new Vector();
		tmporder=((HashMap)mapaDesorden.clone());
		int numReg = Integer.parseInt(mapaDesorden.get("numRegistros")+"");
		//se ordena el mapa por el tipo de rompimiento en forma ascendente.
		tmporder = (ordenarMapa(indices,tipoRompimiento,"",mapaDesorden,numReg));
		tmporder.put("numRegistros", numReg);
		//se detectan si existen cambios en el rompimiento dentro del mapa,
		//se almacenan los index donde se encuentran los cambios.
		cambios=detectarCambioRompimiento(tmporder, numReg, tipoRompimiento);
		//de ser asi entonces:
		if(cambios.size()>1)
			for (int i=0;i<numReg;i++)
			{
				//se va almacenando en tmp2 todos los registros que pertenencen al mismo tipo de rompimiento.
				//logger.info("\n el valor del mapa es ********> "+copyOnIndexMap(tmporder, i+"", indices));
				tmp2=copyMap(copyOnIndexMap(tmporder, i+"", indices), tmp2, 1, indices);
				//logger.info("mapa tmp2=> "+tmp2);
				//si el index se encuentra en cambios
				if (cambios.contains(i))
				{
					//logger.info("paso por aqui B "+tmp2);
					//logger.info("ahora estamos dentro de ordenarMapaRompimiento");
					tmp3 = new HashMap();
					//se ordena el mapa tmp2 por el patron de ordenamiento requerido
					tmp3 = (ordenarMapa(indices,indice,ultimoIndice,tmp2,Integer.parseInt(tmp2.get("numRegistros").toString())));
					//logger.info("mapa tmp3=> "+tmp3);
					//se almacena en tmp4.
					tmp3.put("numRegistros", tmp2.get("numRegistros"));
					tmp4=copyMap(tmp3, tmp4, Integer.parseInt(tmp2.get("numRegistros").toString()), indices);
					//logger.info("mapa tmp4=> "+tmp4);
					tmp2 = new HashMap();
				}
			}
		else
			return ordenarMapa(indices,indice,ultimoIndice,mapaDesorden,numReg);
			
		
		
		
		return tmp4;
	}
	
	
	 
	/**
	 * Metodo encargado de identifcar en que index existe
	 * cambio de rompimiento;
	 * @param mapa --Mapa en el cual se va a identificar el cambio de rompimiento
	 * @param numRegistros -- numRegistros del mapa a evaluar
	 * @param tipoRompimiento --> Indice por el cual se va a hacer el rompimiento.
	 * @return
	 */
	 public static Vector detectarCambioRompimiento (HashMap mapa,int numRegistros,String tipoRompimiento)
	 {
		 Vector  indexCambios= new Vector();		 
		 for (int i=0;i<numRegistros;i++)
		 {
			 try 
			 {  
				 //se evalua si existe un cambio de tipo de rompimiento.
				 if (mapa.get(tipoRompimiento+(i+1))!=null && !mapa.get(tipoRompimiento+i).equals(mapa.get(tipoRompimiento+(i+1))))
					 indexCambios.add(i);
			} 
			 catch (Exception e) 
			 {
				System.out.print("Problema Detectando el cambio en el tipo de rompimiento");
			 }
		 }
		 indexCambios.add((numRegistros-1));
		 
		 return indexCambios;
	 }
	 
	 /**
	  * Método que toma un mapa y revisa si una key específica tiene valores repetidos, y si los hay
	  * genera ActionMessages 
	  * @param errores
	  * @param listado (mapa a comparar)
	  * @param numRegistros (número de registros del mapa)
	  * @param indice (key que se va a comparar)
	  * @param mensaje (nombre del campo en plural)
	  * @return
	  */
	 public static ActionErrors validacionValoresRepetidos(ActionErrors errores, HashMap listado,int numRegistros,String indice,String mensaje)
	 {
		 
		 HashMap codigosComparados = new HashMap();
			String descripcion = "";
			String aux1 = "";
			String aux2 = "";
			
			for(int i=0;i<numRegistros;i++)
			{
				aux1=listado.get(indice+i).toString();
				
				for(int j=numRegistros-1;j>i;j--)
				{
					
					aux2=listado.get(indice+j).toString();
					//se compara
					if(aux1.compareToIgnoreCase(aux2)==0&&!aux1.equals("")
						&&!aux2.equals("")&&!codigosComparados.containsValue(aux1))
					{
						if(descripcion.equals(""))
							descripcion = (i+1) + "";
						descripcion += "," + (j+1);
						
					}
				}
				
				if(!descripcion.equals(""))
				{
					errores.add("valores iguales", 
							new ActionMessage("error.salasCirugia.igualesGeneral",
								mensaje,"en los registros Nº "+descripcion));
				}
				
				descripcion = "";
				codigosComparados.put(i+"",aux1);
				
			}
			
		return errores;
	 }
	
}

