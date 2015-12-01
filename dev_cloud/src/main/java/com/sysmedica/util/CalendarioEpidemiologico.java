/*
 * Creado en 16-ene-2006
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.util;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import util.UtilidadFecha;

/**
 * @author santiago
 *
 */
public class CalendarioEpidemiologico {
    
    public static SemanaEpidemiologica obtenerSemanaEpidemiologica(int numeroSemana,int anyo) {
        
        SemanaEpidemiologica semana = new SemanaEpidemiologica();
        Calendar calendario = new GregorianCalendar();
        
        Date hoy = new Date();
        calendario.setTime(hoy);
        
        numeroSemana = numeroSemana-1;
        
        int a = calendario.get(GregorianCalendar.YEAR);
        
    /*    if (anyo<1900||anyo>a) {
            return null;
        }
        else {   */
            GregorianCalendar fechaInicial = new GregorianCalendar();
            GregorianCalendar fechaFinal = new GregorianCalendar();
            
            calendario.set(Calendar.YEAR,anyo);
            calendario.set(Calendar.DAY_OF_YEAR,1);
            
            /**
             * Si el primer dia del año es un domingo, ese mismo dia
             * inicia la primera semana epidemiologica
             */
            if (calendario.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
                
	            fechaInicial.set(Calendar.YEAR,anyo);
	            fechaInicial.set(Calendar.DAY_OF_YEAR,numeroSemana*7+1);
                
                fechaFinal.set(Calendar.YEAR,fechaInicial.get(Calendar.YEAR));
                fechaFinal.set(Calendar.DAY_OF_YEAR,fechaInicial.get(Calendar.DAY_OF_YEAR));
                
                fechaFinal.add(Calendar.DAY_OF_YEAR,6);
                
                // para convertir las fechas inicial y final al formato requerido...
                String mesInicial = "";
                
                if (fechaInicial.get(Calendar.MONTH)<9) {
                    mesInicial = "0"+Integer.toString(fechaInicial.get(Calendar.MONTH)+1);
                }
                else if (fechaInicial.get(Calendar.MONTH)==9) {
                	
                	mesInicial = "10";
                }
                else if (fechaInicial.get(Calendar.MONTH)==10) {
                	
                	mesInicial = "11";
                }
                else if (fechaInicial.get(Calendar.MONTH)==11) {
                	
                	mesInicial = "12";
                }
            /*    else {
                    mesInicial = Integer.toString(fechaInicial.get(Calendar.MONTH));
                }
            */    
                String mesFinal = "";
                
                if (fechaFinal.get(Calendar.MONTH)<9) {
                    mesFinal = "0"+Integer.toString(fechaFinal.get(Calendar.MONTH)+1);
                }
                else if (fechaFinal.get(Calendar.MONTH)==9) {
                	
                	mesFinal = "10";
                }
                else if (fechaFinal.get(Calendar.MONTH)==10) {
                	
                	mesFinal = "11";
                }
                else if (fechaFinal.get(Calendar.MONTH)==11) {
                	
                	mesFinal = "12";
                }
           /*     else {
                    mesFinal = Integer.toString(fechaFinal.get(Calendar.MONTH));
                }
           */     
                String stringFechaInicial = Integer.toString(fechaInicial.get(Calendar.DAY_OF_MONTH))+"/"+mesInicial+"/"+Integer.toString(fechaInicial.get(Calendar.YEAR));
                String stringFechaFinal = Integer.toString(fechaFinal.get(Calendar.DAY_OF_MONTH))+"/"+mesFinal+"/"+Integer.toString(fechaFinal.get(Calendar.YEAR));

                semana.setFechaInicial(stringFechaInicial);
                semana.setFechaFinal(stringFechaFinal);
            }
            
            /**
             * Si el primer dia del año es un lunes, la primera semana epidemiologica
             * inicia el domingo 31 de diciembre del año anterior
             */
            else if (calendario.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY) {
                
                if (numeroSemana==0) {
                    
                    fechaInicial.set(Calendar.YEAR,anyo-1);
                    fechaInicial.set(Calendar.MONTH,11);
                    fechaInicial.set(Calendar.DAY_OF_MONTH,31);
                }
                else {
                                
	                fechaInicial.set(Calendar.YEAR,anyo);
	                fechaInicial.set(Calendar.DAY_OF_YEAR,numeroSemana*7);
                }
                
                fechaFinal.set(Calendar.YEAR,fechaInicial.get(Calendar.YEAR));
                fechaFinal.set(Calendar.DAY_OF_YEAR,fechaInicial.get(Calendar.DAY_OF_YEAR));
                
                fechaFinal.add(Calendar.DAY_OF_YEAR,6);
                
//              para convertir las fechas inicial y final al formato requerido...
                String mesInicial = "";
                
                if (fechaInicial.get(Calendar.MONTH)<9) {
                    mesInicial = "0"+Integer.toString(fechaInicial.get(Calendar.MONTH)+1);
                }
                else if (fechaInicial.get(Calendar.MONTH)==9) {
                	
                	mesInicial = "10";
                }
                else if (fechaInicial.get(Calendar.MONTH)==10) {
                	
                	mesInicial = "11";
                }
                else if (fechaInicial.get(Calendar.MONTH)==11) {
                	
                	mesInicial = "12";
                }
         /*       else {
                    mesInicial = Integer.toString(fechaInicial.get(Calendar.MONTH));
                }
         */       
                String mesFinal = "";
                
                if (fechaFinal.get(Calendar.MONTH)<9) {
                    mesFinal = "0"+Integer.toString(fechaFinal.get(Calendar.MONTH)+1);
                }
                else if (fechaFinal.get(Calendar.MONTH)==9) {
                	
                	mesFinal = "10";
                }
                else if (fechaFinal.get(Calendar.MONTH)==10) {
                	
                	mesFinal = "11";
                }
                else if (fechaFinal.get(Calendar.MONTH)==11) {
                	
                	mesFinal = "12";
                }
           /*     else {
                    mesFinal = Integer.toString(fechaFinal.get(Calendar.MONTH));
                }
             */   
                String stringFechaInicial = Integer.toString(fechaInicial.get(Calendar.DAY_OF_MONTH))+"/"+mesInicial+"/"+Integer.toString(fechaInicial.get(Calendar.YEAR));
                String stringFechaFinal = Integer.toString(fechaFinal.get(Calendar.DAY_OF_MONTH))+"/"+mesFinal+"/"+Integer.toString(fechaFinal.get(Calendar.YEAR));

                semana.setFechaInicial(stringFechaInicial);
                semana.setFechaFinal(stringFechaFinal);
            }
            
            /**
             * Si el primer dia del año es un martes, la primera semana epidemiologica
             * inicia el domingo 30 de diciembre del año anterior
             */
            else if (calendario.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY) {
                
                if (numeroSemana==0) {
                    
                    fechaInicial.set(Calendar.YEAR,anyo-1);
                    fechaInicial.set(Calendar.MONTH,11);
                    fechaInicial.set(Calendar.DAY_OF_MONTH,30);
                }
                else {
                                
	                fechaInicial.set(Calendar.YEAR,anyo);
	                fechaInicial.set(Calendar.DAY_OF_YEAR,numeroSemana*7-1);
                }
                
                fechaFinal.set(Calendar.YEAR,fechaInicial.get(Calendar.YEAR));
                fechaFinal.set(Calendar.DAY_OF_YEAR,fechaInicial.get(Calendar.DAY_OF_YEAR));
                
                fechaFinal.add(Calendar.DAY_OF_YEAR,6);
                
//              para convertir las fechas inicial y final al formato requerido...
                String mesInicial = "";
                
                if (fechaInicial.get(Calendar.MONTH)<9) {
                    mesInicial = "0"+Integer.toString(fechaInicial.get(Calendar.MONTH)+1);
                }
                else if (fechaInicial.get(Calendar.MONTH)==9) {
                	
                	mesInicial = "10";
                }
                else if (fechaInicial.get(Calendar.MONTH)==10) {
                	
                	mesInicial = "11";
                }
                else if (fechaInicial.get(Calendar.MONTH)==11) {
                	
                	mesInicial = "12";
                }
         /*       else {
                    mesInicial = Integer.toString(fechaInicial.get(Calendar.MONTH));
                }
           */     
                String mesFinal = "";
                
                if (fechaFinal.get(Calendar.MONTH)<9) {
                    mesFinal = "0"+Integer.toString(fechaFinal.get(Calendar.MONTH)+1);
                }
                else if (fechaFinal.get(Calendar.MONTH)==9) {
                	
                	mesFinal = "10";
                }
                else if (fechaFinal.get(Calendar.MONTH)==10) {
                	
                	mesFinal = "11";
                }
                else if (fechaFinal.get(Calendar.MONTH)==11) {
                	
                	mesFinal = "12";
                }
           /*     else {
                    mesFinal = Integer.toString(fechaFinal.get(Calendar.MONTH));
                }
           */     
                String stringFechaInicial = Integer.toString(fechaInicial.get(Calendar.DAY_OF_MONTH))+"/"+mesInicial+"/"+Integer.toString(fechaInicial.get(Calendar.YEAR));
                String stringFechaFinal = Integer.toString(fechaFinal.get(Calendar.DAY_OF_MONTH))+"/"+mesFinal+"/"+Integer.toString(fechaFinal.get(Calendar.YEAR));

                semana.setFechaInicial(stringFechaInicial);
                semana.setFechaFinal(stringFechaFinal);
            }
            
            /**
             * Si el primer dia del año es un miercoles, la primera semana epidemiologica
             * inicia el domingo 29 de diciembre del año anterior
             */
            else if (calendario.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY) {
                
                if (numeroSemana==0) {
                    
                    fechaInicial.set(Calendar.YEAR,anyo-1);
                    fechaInicial.set(Calendar.MONTH,11);
                    fechaInicial.set(Calendar.DAY_OF_MONTH,29);
                }
                else {
                                
	                fechaInicial.set(Calendar.YEAR,anyo);
	                fechaInicial.set(Calendar.DAY_OF_YEAR,numeroSemana*7-2);
                }
                
                fechaFinal.set(Calendar.YEAR,fechaInicial.get(Calendar.YEAR));
                fechaFinal.set(Calendar.DAY_OF_YEAR,fechaInicial.get(Calendar.DAY_OF_YEAR));
                
                fechaFinal.add(Calendar.DAY_OF_YEAR,6);
                
//              para convertir las fechas inicial y final al formato requerido...
                String mesInicial = "";
                
                if (fechaInicial.get(Calendar.MONTH)<9) {
                    mesInicial = "0"+Integer.toString(fechaInicial.get(Calendar.MONTH)+1);
                }
                else if (fechaInicial.get(Calendar.MONTH)==9) {
                	
                	mesInicial = "10";
                }
                else if (fechaInicial.get(Calendar.MONTH)==10) {
                	
                	mesInicial = "11";
                }
                else if (fechaInicial.get(Calendar.MONTH)==11) {
                	
                	mesInicial = "12";
                }
           /*     else {
                    mesInicial = Integer.toString(fechaInicial.get(Calendar.MONTH));
                }
             */   
                String mesFinal = "";
                
                if (fechaFinal.get(Calendar.MONTH)<9) {
                    mesFinal = "0"+Integer.toString(fechaFinal.get(Calendar.MONTH)+1);
                }
                else if (fechaFinal.get(Calendar.MONTH)==9) {
                	
                	mesFinal = "10";
                }
                else if (fechaFinal.get(Calendar.MONTH)==10) {
                	
                	mesFinal = "11";
                }
                else if (fechaFinal.get(Calendar.MONTH)==11) {
                	
                	mesFinal = "12";
                }
          /*      else {
                    mesFinal = Integer.toString(fechaFinal.get(Calendar.MONTH));
                }
            */    
                String stringFechaInicial = Integer.toString(fechaInicial.get(Calendar.DAY_OF_MONTH))+"/"+mesInicial+"/"+Integer.toString(fechaInicial.get(Calendar.YEAR));
                String stringFechaFinal = Integer.toString(fechaFinal.get(Calendar.DAY_OF_MONTH))+"/"+mesFinal+"/"+Integer.toString(fechaFinal.get(Calendar.YEAR));

                semana.setFechaInicial(stringFechaInicial);
                semana.setFechaFinal(stringFechaFinal);
            }
            
            /**
             * Si el primer dia del año es un jueves, la primera semana epidemiologica
             * inicia el domingo 4 de enero
             */
            if (calendario.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY) {
                             
	            fechaInicial.set(Calendar.YEAR,anyo);
	            fechaInicial.set(Calendar.DAY_OF_YEAR,numeroSemana*7+4);
                
	            fechaFinal.set(Calendar.YEAR,fechaInicial.get(Calendar.YEAR));
                fechaFinal.set(Calendar.DAY_OF_YEAR,fechaInicial.get(Calendar.DAY_OF_YEAR));
                
                fechaFinal.add(Calendar.DAY_OF_YEAR,6);
                
//              para convertir las fechas inicial y final al formato requerido...
                String mesInicial = "";
                
                if (fechaInicial.get(Calendar.MONTH)<9) {
                    mesInicial = "0"+Integer.toString(fechaInicial.get(Calendar.MONTH)+1);
                }
                else if (fechaInicial.get(Calendar.MONTH)==9) {
                	
                	mesInicial = "10";
                }
                else if (fechaInicial.get(Calendar.MONTH)==10) {
                	
                	mesInicial = "11";
                }
                else if (fechaInicial.get(Calendar.MONTH)==11) {
                	
                	mesInicial = "12";
                }
           /*     else {
                    mesInicial = Integer.toString(fechaInicial.get(Calendar.MONTH));
                }
             */   
                String mesFinal = "";
                
                if (fechaFinal.get(Calendar.MONTH)<9) {
                    mesFinal = "0"+Integer.toString(fechaFinal.get(Calendar.MONTH)+1);
                }
                else if (fechaFinal.get(Calendar.MONTH)==9) {
                	
                	mesFinal = "10";
                }
                else if (fechaFinal.get(Calendar.MONTH)==10) {
                	
                	mesFinal = "11";
                }
                else if (fechaFinal.get(Calendar.MONTH)==11) {
                	
                	mesFinal = "12";
                }
           /*     else {
                    mesFinal = Integer.toString(fechaFinal.get(Calendar.MONTH));
                }
           */     
                String stringFechaInicial = Integer.toString(fechaInicial.get(Calendar.DAY_OF_MONTH))+"/"+mesInicial+"/"+Integer.toString(fechaInicial.get(Calendar.YEAR));
                String stringFechaFinal = Integer.toString(fechaFinal.get(Calendar.DAY_OF_MONTH))+"/"+mesFinal+"/"+Integer.toString(fechaFinal.get(Calendar.YEAR));

                semana.setFechaInicial(stringFechaInicial);
                semana.setFechaFinal(stringFechaFinal);
            }
            
            /**
             * Si el primer dia del año es un viernes, la primera semana epidemiologica
             * inicia el domingo 3 de enero
             */
            else if (calendario.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY) {
                            
	            fechaInicial.set(Calendar.YEAR,anyo);
	            fechaInicial.set(Calendar.DAY_OF_YEAR,numeroSemana*7+3);
                
	            fechaFinal.set(Calendar.YEAR,fechaInicial.get(Calendar.YEAR));
                fechaFinal.set(Calendar.DAY_OF_YEAR,fechaInicial.get(Calendar.DAY_OF_YEAR));
                
                fechaFinal.add(Calendar.DAY_OF_YEAR,6);
                
//              para convertir las fechas inicial y final al formato requerido...
                String mesInicial = "";
                
                if (fechaInicial.get(Calendar.MONTH)<9) {
                    mesInicial = "0"+Integer.toString(fechaInicial.get(Calendar.MONTH)+1);
                }
                else if (fechaInicial.get(Calendar.MONTH)==9) {
                	
                	mesInicial = "10";
                }
                else if (fechaInicial.get(Calendar.MONTH)==10) {
                	
                	mesInicial = "11";
                }
                else if (fechaInicial.get(Calendar.MONTH)==11) {
                	
                	mesInicial = "12";
                }
          /*      else {
                    mesInicial = Integer.toString(fechaInicial.get(Calendar.MONTH));
                }
            */    
                String mesFinal = "";
                
                if (fechaFinal.get(Calendar.MONTH)<9) {
                    mesFinal = "0"+Integer.toString(fechaFinal.get(Calendar.MONTH)+1);
                }
                else if (fechaFinal.get(Calendar.MONTH)==9) {
                	
                	mesFinal = "10";
                }
                else if (fechaFinal.get(Calendar.MONTH)==10) {
                	
                	mesFinal = "11";
                }
                else if (fechaFinal.get(Calendar.MONTH)==11) {
                	
                	mesFinal = "12";
                }
           /*     else {
                    mesFinal = Integer.toString(fechaFinal.get(Calendar.MONTH));
                }
             */   
                String stringFechaInicial = Integer.toString(fechaInicial.get(Calendar.DAY_OF_MONTH))+"/"+mesInicial+"/"+Integer.toString(fechaInicial.get(Calendar.YEAR));
                String stringFechaFinal = Integer.toString(fechaFinal.get(Calendar.DAY_OF_MONTH))+"/"+mesFinal+"/"+Integer.toString(fechaFinal.get(Calendar.YEAR));

                semana.setFechaInicial(stringFechaInicial);
                semana.setFechaFinal(stringFechaFinal);
            }
            
            /**
             * Si el primer dia del año es un sabado, la primera semana epidemiologica
             * inicia el domingo 2 de enero
             */
            else if (calendario.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY) {
                             
	            fechaInicial.set(Calendar.YEAR,anyo);
	            fechaInicial.set(Calendar.DAY_OF_YEAR,numeroSemana*7+2);
                
	            fechaFinal.set(Calendar.YEAR,fechaInicial.get(Calendar.YEAR));
                fechaFinal.set(Calendar.DAY_OF_YEAR,fechaInicial.get(Calendar.DAY_OF_YEAR));
                
                fechaFinal.add(Calendar.DAY_OF_YEAR,6);
                
//              para convertir las fechas inicial y final al formato requerido...
                String mesInicial = ""; 
                
                if (fechaInicial.get(Calendar.MONTH)<9) {
                    mesInicial = "0"+Integer.toString(fechaInicial.get(Calendar.MONTH)+1);
                }
                else if (fechaInicial.get(Calendar.MONTH)==9) {
                	
                	mesInicial = "10";
                }
                else if (fechaInicial.get(Calendar.MONTH)==10) {
                	
                	mesInicial = "11";
                }
                else if (fechaInicial.get(Calendar.MONTH)==11) {
                	
                	mesInicial = "12";
                }
         /*       else {
                    mesInicial = Integer.toString(fechaInicial.get(Calendar.MONTH));
                }
           */     
                String mesFinal = "";
                
                if (fechaFinal.get(Calendar.MONTH)<9) {
                    mesFinal = "0"+Integer.toString(fechaFinal.get(Calendar.MONTH)+1);
                }
                else if (fechaFinal.get(Calendar.MONTH)==9) {
                	
                	mesFinal = "10";
                }
                else if (fechaFinal.get(Calendar.MONTH)==10) {
                	
                	mesFinal = "11";
                }
                else if (fechaFinal.get(Calendar.MONTH)==11) {
                	
                	mesFinal = "12";
                }
           /*     else {
                    mesFinal = Integer.toString(fechaFinal.get(Calendar.MONTH));
                }
             */   
                String stringFechaInicial = Integer.toString(fechaInicial.get(Calendar.DAY_OF_MONTH))+"/"+mesInicial+"/"+Integer.toString(fechaInicial.get(Calendar.YEAR));
                String stringFechaFinal = Integer.toString(fechaFinal.get(Calendar.DAY_OF_MONTH))+"/"+mesFinal+"/"+Integer.toString(fechaFinal.get(Calendar.YEAR));

                semana.setFechaInicial(stringFechaInicial);
                semana.setFechaFinal(stringFechaFinal);
            }
            
     //   }
        
        
        return semana;
    }
    
    
    
    
    /**
     * Metodo que devuelve el numero de semana epidemiologica de una fecha especifica
     * @param fecha : debe estar en formato dd/mm/aaaa
     * @return
     */
    public static int[] obtenerNumeroSemana(String fecha) 
    {
    	int[] res = new int[2];
    	int resultado = 0;
    	
    	int anyo = Integer.parseInt(fecha.split("/")[2]);
    	int dia = Integer.parseInt(fecha.split("/")[0]);
		int mes = Integer.parseInt(fecha.split("/")[1])-1;
		
		GregorianCalendar calendario = new GregorianCalendar();
		
		calendario.set(Calendar.DAY_OF_MONTH,dia);
		calendario.set(Calendar.MONTH,mes);
		calendario.set(Calendar.YEAR,anyo);
    	
    	for (int i=1;i<53;i++) {
    		
    		SemanaEpidemiologica semana = obtenerSemanaEpidemiologica(i,anyo);
    		
    		String fechaFinal = semana.getFechaFinal();
    		
    		int anyo2 = Integer.parseInt(fechaFinal.split("/")[2]);
    		int mes2 = Integer.parseInt(fechaFinal.split("/")[1])-1;
    		int dia2 = Integer.parseInt(fechaFinal.split("/")[0]);
    		
    		GregorianCalendar calendario2 = new GregorianCalendar();
    		
    		calendario2.set(Calendar.DAY_OF_MONTH,dia2);
    		calendario2.set(Calendar.MONTH,mes2);
    		calendario2.set(Calendar.YEAR,anyo2);
    		
    		if (calendario2.compareTo(calendario)>0) {
    			
    			resultado = i;
    			break;
    		}
    	}
    	
    	SemanaEpidemiologica sem = obtenerSemanaEpidemiologica(1,anyo+1);
    	
    	if (UtilidadFecha.esFechaMenorIgualQueOtraReferencia(sem.getFechaInicial(),fecha)) {
    		resultado = 1;
    		anyo = anyo+1;
    	}
    	
    	res[0] = resultado;
    	res[1] = anyo;
    	
    	return res;
    }
    
    
    
    /**
     * Metodo que obtiene en que periodo epidemiologico esta una fecha especifica
     * @param fecha
     * @return
     */
    public static int[] obtenerNumeroPeriodo(String fecha)
    {
    	int res[] = new int[2];
    	int resultado = 0;
    	
    	int valorSemana = obtenerNumeroSemana(fecha)[0];
    	
    	int valorPeriodo = valorSemana/4;
    	
    	res[0] = valorPeriodo;
    	res[1] = obtenerNumeroSemana(fecha)[1];
    	                                    
    //	resultado = valorPeriodo;
    	
    	return res;
    }
    
    
    /**
     * Dada una fecha de nacimiento dd/mm/aaaa y una fecha de referencia dd/mm/aaaa devuelve la
     * edad en un Vector cuya primera posicion es el valor de la edad y la segunda posicion es la unidad de
     * medida de la edad. Si la edad es menor a un mes, se presenta en Dias, si es menor a 2 años, se da en Meses
     * y en adelante se da en Años. 
     * @param fechaNacimiento
     * @param fechaReferencia
     * @return
     */
    public static Vector obtenerEdadDetallada(String fechaNacimiento, String fechaReferencia)
    {
    	Vector resultado = new Vector();
    	int edad = 0;
    	String tipoEdad = "";
    	
    	int diaNacimiento = Integer.parseInt(fechaNacimiento.split("/")[0]);
    	int mesNacimiento = Integer.parseInt(fechaNacimiento.split("/")[1]);
    	int anyoNacimiento = Integer.parseInt(fechaNacimiento.split("/")[2]);
    	
    	int diaReferencia = Integer.parseInt(fechaReferencia.split("/")[0]);
    	int mesReferencia = Integer.parseInt(fechaReferencia.split("/")[1]);
    	int anyoReferencia = Integer.parseInt(fechaReferencia.split("/")[2]);
    	
    	if (anyoReferencia<anyoNacimiento) {
    		
    		return null;
    	}
    	else {
    		
    		edad = anyoReferencia - anyoNacimiento;
    		
    		if (edad==0) {
    			
    			if (mesReferencia-mesNacimiento==0) {
    				edad = diaReferencia - diaNacimiento;
    				tipoEdad = "D";
    			}
    			else if (mesReferencia-mesNacimiento==1) {
    				edad = 30 - diaNacimiento + diaReferencia;
    				tipoEdad = "D";
    			}
    			else {
    				edad = mesReferencia - mesNacimiento;
    				tipoEdad = "M";
    			}
    		}
    		else if (edad==1) {
    			
    			edad = 12 - mesNacimiento + mesReferencia; 
    		}
    		else if (edad==2) {
    			
    			edad = 24 - mesNacimiento + mesReferencia;
    		}
    		else {
    			
    			if (mesReferencia<mesNacimiento) {
    				
    				edad--;
    			}
    			
    			tipoEdad = "A";
    		}
    		
    		resultado.add(Integer.toString(edad));
			resultado.add(tipoEdad);
    	}
    	    	
    	return resultado;
    }
    
    
    
    
    public static String obtenerGrupoEdad(String fechaNacimiento,String fechaReferencia)
    {
    	String resultado = "";
    	
    	String grupo0 = "< 1 AÑO";
    	String grupo1 = "1 - 4";
    	String grupo2 = "5 - 9";
    	String grupo3 = "10 - 14";
    	String grupo4 = "15 - 19";
    	String grupo5 = "20 - 24";
    	String grupo6 = "25 - 29";
    	String grupo7 = "30 - 34";
    	String grupo8 = "35 - 39";
    	String grupo9 = "40 - 44";
    	String grupo10 = "45 - 49";
    	String grupo11 = "50 - 54";
    	String grupo12 = "55 - 59";
    	String grupo13 = "60 - 64";
    	String grupo14 = "65 - 69";
    	String grupo15 = "70 - 74";
    	String grupo16 = "75 - 79";
    	String grupo17 = "80 - 84";
    	String grupo18 = "85 - 89";
    	String grupo19 = "90 - 94";
    	String grupo20 = "95 - 99";
    	String grupo21 = "100 - 104";
    	String grupo22 = "105 - 109";
    	String grupo23 = "110 - 114";
    	
    	String diaNacimiento,mesNacimiento,anyoNacimiento, diaReferencia,mesReferencia,anyoReferencia;
    	
    	
    	diaNacimiento = fechaNacimiento.split("/")[0];
    	mesNacimiento = fechaNacimiento.split("/")[1];
    	anyoNacimiento = fechaNacimiento.split("/")[2];
    	
    	diaReferencia = fechaReferencia.split("/")[0];
    	mesReferencia = fechaReferencia.split("/")[1];
    	anyoReferencia = fechaReferencia.split("/")[2];
    	
    	
    	int edad = UtilidadFecha.calcularEdad(diaNacimiento,mesNacimiento,anyoNacimiento,diaReferencia,mesReferencia,anyoReferencia);
    	
    	int grupo = 1 + edad/5;
    	
    	
    	switch (grupo)
    	{
    		case 0:
    		{
    			resultado = grupo0;
    			break;
    		}
    		case 1:
    		{
    			resultado = grupo1;
    			break;
    		}
    		case 2:
    		{
    			resultado = grupo2;
    			break;
    		}
    		case 3:
    		{
    			resultado = grupo3;
    			break;
    		}
    		case 4:
    		{
    			resultado = grupo4;
    			break;
    		}
    		case 5:
    		{
    			resultado = grupo5;
    			break;
    		}
    		case 6:
    		{
    			resultado = grupo6;
    			break;
    		}
    		case 7:
    		{
    			resultado = grupo7;
    			break;
    		}
    		case 8:
    		{
    			resultado = grupo8;
    			break;
    		}
    		case 9:
    		{
    			resultado = grupo9;
    			break;
    		}
    		case 10:
    		{
    			resultado = grupo10;
    			break;
    		}
    		case 11:
    		{
    			resultado = grupo11;
    			break;
    		}
    		case 12:
    		{
    			resultado = grupo12;
    			break;
    		}
    		case 13:
    		{
    			resultado = grupo13;
    			break;
    		}
    		case 14:
    		{
    			resultado = grupo14;
    			break;
    		}
    		case 15:
    		{
    			resultado = grupo15;
    			break;
    		}
    		case 16:
    		{
    			resultado = grupo16;
    			break;
    		}
    		case 17:
    		{
    			resultado = grupo17;
    			break;
    		}
    		case 18:
    		{
    			resultado = grupo18;
    			break;
    		}
    		case 19:
    		{
    			resultado = grupo19;
    			break;
    		}
    		case 20:
    		{
    			resultado = grupo20;
    			break;
    		}
    		case 21:
    		{
    			resultado = grupo21;
    			break;
    		}
    		case 22:
    		{
    			resultado = grupo22;
    			break;
    		}
    		case 23:
    		{
    			resultado = grupo23;
    			break;
    		}
    	}
    	
    	return resultado;
    }
}
